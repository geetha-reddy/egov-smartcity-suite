/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.restapi.web.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.restapi.model.ImpactAnalysisResponse;
import org.egov.restapi.service.ImpactAnalysisService;
import org.egov.restapi.util.JsonConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestImpactAnalysisController {

    public static final String API_IMPACTANALYSIS = "/public/impactanalysis";
    private static final Logger LOGGER = Logger.getLogger(RestImpactAnalysisController.class);
    @Autowired
    ImpactAnalysisService impactAnalysisService;

    @RequestMapping(value = API_IMPACTANALYSIS, method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String getImpactAnalysisData(@RequestParam String date, @RequestParam String interval,
            final HttpServletResponse response) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getImpactAnalysisData API Request parameters: date" + date + ", interval:" + interval);
        if (StringUtils.isNotEmpty(date) && StringUtils.isNotEmpty(interval)) {
            List<ImpactAnalysisResponse> impactAnalysisList = impactAnalysisService.getImpactAnalysisData(new Long(date),
                    new Long(interval));
            if (impactAnalysisList.isEmpty()) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("getImpactAnalysisData- Data not found");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return JsonConvertor.convert("Data not found");
            } else
                return JsonConvertor.convert(impactAnalysisList);
        } else {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("getImpactAnalysisData- Date and interval data not entered");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return JsonConvertor.convert("Date and interval data are mandatory");
        }
    }
}