package org.egov.ptis.domain.service.courtverdict;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_SECOND_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.bean.demand.DemandDetail;
import org.egov.ptis.client.bill.PTBillServiceImpl;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.CourtVerdict;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.repository.courtverdict.CourtVerdictRepository;
import org.egov.ptis.domain.repository.master.structureclassification.StructureClassificationRepository;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.exceptions.TaxCalculatorExeption;
import org.egov.ptis.master.service.PropertyUsageService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class CourtVerdictDCBService {

    @Autowired
    private CourtVerdictRepository courtVerdictRepo;
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    StructureClassificationRepository structureDAO;
    @Autowired
    PropertyUsageService propertyUsageService;
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    private PersistenceService<T, Serializable> persistenceService;
    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<PropertyImpl> propertyWorkflowService;
    @Autowired
    private InstallmentHibDao installmentDao;
    @Autowired
    private PtDemandDao ptDemandDAO;
    @Autowired
    private PTBillServiceImpl ptBillServiceImpl;
    private static final Logger LOGGER = Logger.getLogger(CourtVerdictService.class);

    public PropertyImpl modifyDemand(PropertyImpl newProperty, PropertyImpl oldProperty) {
        PropertyImpl modProperty = null;

        try {
            modProperty = (PropertyImpl) propertyService.modifyDemand(newProperty, oldProperty);
        } catch (final TaxCalculatorExeption e) {

            LOGGER.error("forward : There are no Unit rates defined for chosen combinations", e);
            return newProperty;
        }
        return modProperty;
    }


    public void updateDemandDetails(CourtVerdict courtVerdict) {

        Set<EgDemandDetails> demandDetails = propertyService.getCurrrentDemand(courtVerdict.getProperty()).getEgDemandDetails();

        for (final EgDemandDetails dmdDetails : demandDetails)
            for (final DemandDetail dmdDetailBean : courtVerdict.getDemandDetailBeanList()) {
                Boolean isUpdateAmount = Boolean.FALSE;
                Boolean isUpdateCollection = Boolean.FALSE;
                dmdDetailBean.setInstallment(installmentDao.findById(dmdDetailBean.getInstallment().getId(), false));
                if (dmdDetailBean.getRevisedAmount() != null
                        && dmdDetailBean.getInstallment()
                                .equals(dmdDetails.getEgDemandReason().getEgInstallmentMaster())
                        && dmdDetails.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()
                                .equalsIgnoreCase(dmdDetailBean.getReasonMaster()))
                    isUpdateAmount = true;

                if (dmdDetailBean.getRevisedCollection() != null
                        && dmdDetails.getEgDemand().getEgInstallmentMaster()
                                .equals(propertyTaxCommonUtils.getCurrentInstallment())
                        && dmdDetails.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()
                                .equalsIgnoreCase(dmdDetailBean.getReasonMaster())
                        && dmdDetails.getEgDemandReason().getEgInstallmentMaster()
                                .equals(dmdDetailBean.getInstallment()))
                    isUpdateCollection = true;

                if (isUpdateAmount)
                    dmdDetails.setAmount(dmdDetailBean.getRevisedAmount() != null
                            ? dmdDetailBean.getActualAmount().subtract(dmdDetailBean.getRevisedAmount())
                            : BigDecimal.ZERO);
                if (isUpdateCollection)
                    dmdDetails.setAmtCollected(
                            dmdDetailBean.getRevisedCollection() != null ? dmdDetailBean.getRevisedCollection()
                                    : BigDecimal.ZERO);

                if (isUpdateAmount || isUpdateCollection) {
                    dmdDetails.setModifiedDate(new Date());
                    break;
                }
            }
        final List<Ptdemand> currPtdemand;
        final javax.persistence.Query qry = entityManager.createNamedQuery("QUERY_CURRENT_PTDEMAND");
        qry.setParameter("basicProperty", courtVerdict.getProperty().getBasicProperty());
        qry.setParameter("installment", propertyTaxCommonUtils.getCurrentInstallment());
        currPtdemand = qry.getResultList();

        if (currPtdemand != null) {
            final Ptdemand ptdemand = (Ptdemand) currPtdemand.get(0).clone();
            ptdemand.setBaseDemand(getTotalDemand(demandDetails));
            ptdemand.setEgDemandDetails(demandDetails);
            ptdemand.setEgptProperty(courtVerdict.getProperty());
            ptdemand.getDmdCalculations().setCreatedDate(new Date());
            persistenceService.applyAuditing(ptdemand.getDmdCalculations());
            courtVerdict.getProperty().getPtDemandSet().clear();
            courtVerdict.getProperty().getPtDemandSet().add(ptdemand);
        }
    }

    private BigDecimal getTotalDemand(Set<EgDemandDetails> dmndDetails) {
        BigDecimal totalDmd = BigDecimal.ZERO;
        for (EgDemandDetails newDemandDetails : dmndDetails) {
            totalDmd = totalDmd.add(newDemandDetails.getAmount());
        }
        return totalDmd;
    }

    public Map<String, String> validateDemand(List<DemandDetail> demandDetailBeanList) {

        HashMap<String, String> errors = new HashMap<>();

        for (final DemandDetail dd : demandDetailBeanList) {
            dd.setInstallment(installmentDao.findById(dd.getInstallment().getId(), false));
            if (dd.getRevisedCollection().compareTo(dd.getActualAmount().subtract(dd.getRevisedAmount())) > 0) {
                errors.put("revisedCollection",
                        "revised.collection.greater");
            }
        }
        return errors;
    }

    public void addDemandDetails(CourtVerdict courtVerdict) {

        List<DemandDetail> demandDetailList = getDemandDetails(courtVerdict);
        courtVerdict.setDemandDetailBeanList(demandDetailList);

    }

    private List<DemandDetail> setDemandBeanList(List<EgDemandDetails> newDmndDetails, List<EgDemandDetails> oldDmndDetails) {

        List<DemandDetail> demandDetailList = new ArrayList<>();

        int i = 0;
        for (final EgDemandDetails demandDetail : newDmndDetails) {
            for (final EgDemandDetails oldDemandDetail : oldDmndDetails) {
                if (oldDemandDetail.getEgDemandReason().getEgInstallmentMaster()
                        .equals(demandDetail.getEgDemandReason().getEgInstallmentMaster())
                        && oldDemandDetail.getEgDemandReason().getEgDemandReasonMaster()
                                .equals(demandDetail.getEgDemandReason().getEgDemandReasonMaster())) {
                    final Installment installment = demandDetail.getEgDemandReason().getEgInstallmentMaster();
                    final String reasonMaster = demandDetail.getEgDemandReason().getEgDemandReasonMaster()
                            .getReasonMaster();
                    final BigDecimal revisedAmount = oldDemandDetail.getAmount().subtract(demandDetail.getAmount());
                    final BigDecimal revisedCollection = demandDetail.getAmtCollected();
                    final DemandDetail dmdDtl = createDemandDetailBean(installment, reasonMaster, oldDemandDetail.getAmount(),
                            revisedAmount,
                            oldDemandDetail.getAmtCollected(), revisedCollection);
                    demandDetailList.add(i, dmdDtl);

                    break;
                }
            }
            i++;
        }
        return demandDetailList;
    }

    private DemandDetail createDemandDetailBean(final Installment installment, final String reasonMaster,
            final BigDecimal amount, final BigDecimal revisedAmount, final BigDecimal amountCollected,
            final BigDecimal revisedCollection) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into createDemandDetailBean");
            LOGGER.debug("createDemandDetailBean - installment=" + installment + ", reasonMaster=" + reasonMaster
                    + ", amount=" + amount + ", amountCollected=" + amountCollected);
        }

        final DemandDetail demandDetail = new DemandDetail();
        demandDetail.setInstallment(installment);
        demandDetail.setReasonMaster(reasonMaster);
        demandDetail.setActualAmount(amount);
        demandDetail.setRevisedAmount(revisedAmount);
        demandDetail.setActualCollection(amountCollected);
        demandDetail.setRevisedCollection(revisedCollection);
        demandDetail.setIsCollectionEditable(true);
        return demandDetail;
    }

    public CourtVerdict updateDemand(CourtVerdict courtVerdict) {

        List<DemandDetail> demandDetailList = getDemandDetails(courtVerdict);
        BigDecimal totalCollectionAmt = BigDecimal.ZERO;
        for (DemandDetail demandDetail : demandDetailList) {
            if (demandDetail.getActualCollection().compareTo(demandDetail.getRevisedCollection()) >= 0)
                totalCollectionAmt = totalCollectionAmt.add(demandDetail.getActualCollection());
            else
                totalCollectionAmt = totalCollectionAmt.add(demandDetail.getRevisedCollection());
        }

        Ptdemand ptDemandNew = propertyService.getCurrrentDemand(courtVerdict.getProperty());

        if (ptDemandNew.getEgDemandDetails() != null) {
            for (EgDemandDetails egDemandDetails : ptDemandNew.getEgDemandDetails()) {

                totalCollectionAmt = updateCollection(totalCollectionAmt, egDemandDetails);
            }

            if (totalCollectionAmt.compareTo(BigDecimal.ZERO) > 0) {
                final Installment currSecondHalf = propertyTaxUtil.getInstallmentsForCurrYear(new Date())
                        .get(CURRENTYEAR_SECOND_HALF);
                final EgDemandDetails advanceDemandDetails = ptBillServiceImpl.getDemandDetail(ptDemandNew, currSecondHalf,
                        DEMANDRSN_CODE_ADVANCE);
                if (advanceDemandDetails == null) {
                    final EgDemandDetails dmdDetails = ptBillServiceImpl.insertDemandDetails(DEMANDRSN_CODE_ADVANCE,
                            totalCollectionAmt, currSecondHalf);
                    ptDemandNew.getEgDemandDetails().add(dmdDetails);
                } else
                    advanceDemandDetails.getAmtCollected().add(totalCollectionAmt);
            }
        }

        return courtVerdict;
    }

    private BigDecimal updateCollection(BigDecimal totalColl, EgDemandDetails newDemandDetail) {
        BigDecimal remaining = totalColl;
        if (newDemandDetail != null) {
            newDemandDetail.setAmtCollected(ZERO);
            if (remaining.compareTo(BigDecimal.ZERO) > 0) {
                if (remaining.compareTo(newDemandDetail.getAmount()) <= 0) {
                    newDemandDetail.setAmtCollected(remaining);
                    newDemandDetail.setModifiedDate(new Date());
                    remaining = BigDecimal.ZERO;
                } else {
                    newDemandDetail.setAmtCollected(newDemandDetail.getAmount());
                    newDemandDetail.setModifiedDate(new Date());
                    remaining = remaining.subtract(newDemandDetail.getAmount());
                }
            }
        }
        return remaining;
    }

    public List<DemandDetail> getDemandDetails(CourtVerdict courtVerdict) {
        Set<EgDemandDetails> newDemandDetails = (ptDemandDAO.getNonHistoryCurrDmdForProperty(courtVerdict.getProperty()))
                .getEgDemandDetails();
        Set<EgDemandDetails> oldDemandDetails = (ptDemandDAO
                .getNonHistoryCurrDmdForProperty(courtVerdict.getBasicProperty().getProperty()))
                        .getEgDemandDetails();
        List<EgDemandDetails> newDmndDetails = new ArrayList<>(newDemandDetails);
        List<EgDemandDetails> oldDmndDetails = new ArrayList<>(oldDemandDetails);

        if (!newDmndDetails.isEmpty())
            newDmndDetails = sortDemandDetails(newDmndDetails);

        if (!oldDmndDetails.isEmpty())
            oldDmndDetails = sortDemandDetails(oldDmndDetails);

        return setDemandBeanList(newDmndDetails, oldDmndDetails);
    }

    public List<DemandDetail> setDemandBeanList(List<EgDemandDetails> demandDetails) {

        List<DemandDetail> demandDetailList = new ArrayList<>();

        for (final EgDemandDetails demandDetail : demandDetails) {
            final Installment installment = demandDetail.getEgDemandReason().getEgInstallmentMaster();
            final String reasonMaster = demandDetail.getEgDemandReason().getEgDemandReasonMaster()
                    .getReasonMaster();
            final DemandDetail dmdDtl = createDemandDetailBean(installment, reasonMaster, demandDetail.getAmount(),
                    demandDetail.getAmtCollected());
            demandDetailList.add(dmdDtl);
        }
        return demandDetailList;
    }

    private DemandDetail createDemandDetailBean(final Installment installment, final String reasonMaster,
            final BigDecimal amount, final BigDecimal amountCollected) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into createDemandDetailBean");
            LOGGER.debug("createDemandDetailBean - installment=" + installment + ", reasonMaster=" + reasonMaster
                    + ", amount=" + amount + ", amountCollected=" + amountCollected);
        }

        final DemandDetail demandDetail = new DemandDetail();
        demandDetail.setInstallment(installment);
        demandDetail.setReasonMaster(reasonMaster);
        demandDetail.setActualAmount(amount);
        demandDetail.setActualCollection(amountCollected);
        demandDetail.setIsCollectionEditable(true);
        return demandDetail;
    }

    public List<EgDemandDetails> sortDemandDetails(List<EgDemandDetails> demandDetails) {
        Collections.sort(demandDetails, new Comparator<EgDemandDetails>() {

            @Override
            public int compare(EgDemandDetails dmdDtl1, EgDemandDetails dmdDtl2) {
                return dmdDtl1.getEgDemandReason().getEgInstallmentMaster()
                        .compareTo(dmdDtl2.getEgDemandReason().getEgInstallmentMaster());
            }

        }.thenComparing(new Comparator<EgDemandDetails>() {

            @Override
            public int compare(EgDemandDetails dmdDtl1, EgDemandDetails dmdDtl2) {
                return dmdDtl1.getEgDemandReason().getEgDemandReasonMaster().getOrderId()
                        .compareTo(dmdDtl2.getEgDemandReason().getEgDemandReasonMaster().getOrderId());
            }
        }));
        return demandDetails;
    }
}