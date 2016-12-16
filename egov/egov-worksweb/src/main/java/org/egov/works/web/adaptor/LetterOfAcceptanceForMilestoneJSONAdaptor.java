/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

package org.egov.works.web.adaptor;

import java.lang.reflect.Type;

import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class LetterOfAcceptanceForMilestoneJSONAdaptor implements JsonSerializer<WorkOrderEstimate> {

    @Override
    public JsonElement serialize(final WorkOrderEstimate workOrderEstimate, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (workOrderEstimate != null)
            if (workOrderEstimate.getWorkOrder().getEstimateNumber() != null) {
                jsonObject.addProperty("estimateNumber", workOrderEstimate.getEstimate().getEstimateNumber());
                jsonObject.addProperty("typeOfWork", workOrderEstimate.getEstimate().getLineEstimateDetails()
                        .getLineEstimate().getTypeOfWork().getName());
                if (workOrderEstimate.getEstimate().getLineEstimateDetails().getLineEstimate()
                        .getSubTypeOfWork() != null)
                    jsonObject.addProperty("subTypeOfWork", workOrderEstimate.getEstimate().getLineEstimateDetails()
                            .getLineEstimate().getSubTypeOfWork().getName());
                else
                    jsonObject.addProperty("subTypeOfWork", "");
                jsonObject.addProperty("estimateDate", workOrderEstimate.getEstimate().getLineEstimateDetails()
                        .getLineEstimate().getLineEstimateDate().toString());
                jsonObject.addProperty("nameOfTheWork",
                        workOrderEstimate.getEstimate().getLineEstimateDetails().getNameOfWork());
                jsonObject.addProperty("workIdentificationNumber",
                        workOrderEstimate.getEstimate().getLineEstimateDetails().getProjectCode().getCode());
                jsonObject.addProperty("workOrderNumber", workOrderEstimate.getWorkOrder().getWorkOrderNumber());
                jsonObject.addProperty("workOrderAmount", workOrderEstimate.getWorkOrder().getWorkOrderAmount());
                jsonObject.addProperty("workOrderDate", workOrderEstimate.getWorkOrder().getWorkOrderDate().toString());

                jsonObject.addProperty("workOrderId", workOrderEstimate.getWorkOrder().getId());
                jsonObject.addProperty("workOrderEstimateId", workOrderEstimate.getId());
            }
        return jsonObject;
    }
}
