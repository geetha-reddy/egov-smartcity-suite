/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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

var reportdatatable;

$(document)
		.ready(
				function() {
					$('#type').val($('#typeDefaultVal').val());
					usagetableContainer = $("#tblulbDCBcollection"); 
					$('#tblulbDCBcollection').hide();
					$('#tblulbDCBcollectionheader').hide();
					$('#uldDCBCollectionReportSearch')
							.on('click',
									function(event) {
								event.preventDefault();
								
					 usagetableContainer.dataTable({
						type : 'GET',
						responsive : true,
						destroy : true,
						"bPaginate": false,
						"bInfo": false,
						"autoWidth" : false,
						"bDestroy" : true,
						"sDom" : "<'row'<'col-xs-19 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-19'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
						"oTableTools" : {
							"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
							"aButtons" : [ 
							                {
									             "sExtends": "xls",
				                                 "sTitle": "ULB Wise DCB Report",
				                                 "mColumns": [ 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26]
				                                	 
								             },{
									             "sExtends": "print",
				                                 "sTitle": "ULB Wise DCB Report"
								             }]
						},
						ajax : {
							url : "/ptis/reports/ulbWiseDCBList?"+$("#bcDailyCollectionReportForm").serialize()
						
						},
						columns : [
								{
									"sTitle" : "Sl.no",
								},
								{
									"data" : "district",
									"sTitle" : "District"
								},
								{
									"data" : "ulbName",
									"sTitle" : "Ulb Name"
								},
								{
									"data" : "ulbCode",
									"sTitle" : "Ulb Code"
								},
								{
									"data" : "collectorname",
									"sTitle" : "Name" ,"sWidth": "180px"
								},
								{
									"data" : "mobilenumber",
									"sTitle" : "Mobile Number"
								},
								{
									"data" : "totalaccessments",
									"sTitle" : " No. of assessments", "sClass": "text-right"
								},
								{
									"data" : "arrears_demand",
									"sTitle" : "Arrear tax", "sClass": "text-right"
								},
								{
									"data" : "arrears_penalty",
									"sTitle" : "Arrear interest", "sClass": "text-right"
								},
								{
									"data" : "current_demand",
									"sTitle" : "Current tax", "sClass": "text-right"
								},
								{
									"data" : "current_penalty",
									"sTitle" : "Current interest", "sClass": "text-right"
								},
								{
									"data" : "target_total_demand",
									"sTitle" : "Total", "sClass": "text-right"
								},
								{
									"data" : "target_total_demandInterest",
									"sTitle" : "Total Interest", "sClass": "text-right"
								},
								
								{
									"data" : "arrears_demand_collection",
									"sTitle" : "Arrear tax", "sClass": "text-right"
								},
								{
									"data" : "arrears_penalty_collection",
									"sTitle" : "Arrear interest", "sClass": "text-right"
								},
								{
									"data" : "current_demand_collection",
									"sTitle" : "Current tax", "sClass": "text-right"
								},
								{
									"data" : "current_penalty_collection",
									"sTitle" : "Current interest", "sClass": "text-right"
								},
								{
									"data" : "cummulative_total_Collection",
									"sTitle" : "Total", "sClass": "text-right"
								},
								{
									"data" : "cummulative_total_CollectionInterest",
									"sTitle" : "Total Interest", "sClass": "text-right"
								},
								{
									"data" : "cummulative_total_CollectionPercentage",
									"sTitle" : "Total collection % (without interest)", "sClass": "text-right"
								},
								{
									"data" : "cummulative_total_CollectionInterestPercentage",
									"sTitle" : "Total collection % (with interest)", "sClass": "text-right"
								},
								{
									"data" : "balance_arrearTax",
									"sTitle" : "Arrear tax", "sClass": "text-right"
								},
								{
									"data" : "balance_arrearInterest",
									"sTitle" : "Arrear interest", "sClass": "text-right"
								},
								{
									"data" : "balance_currentTax",
									"sTitle" : "Current tax", "sClass": "text-right"
								},
								{
									"data" : "balance_currentInterest",
									"sTitle" : "Current interest", "sClass": "text-right"
								},
								{
									"data" : "balance_total",
									"sTitle" : "Total", "sClass": "text-right"
								},{
								"data" : "balance_totalInterest",
								"sTitle" : "Total Interest", "sClass": "text-right"
								}
							
								 ],"footerCallback" : function(row, data, start, end, display) {
										var api = this.api(), data;
										if (data.length == 0) {
											$('#report-footer').hide();
										} else {
											$('#report-footer').show();
										}
										if (data.length > 0) {
											for(var i=7;i<=26;i++)
											{
											  updateTotalFooter(i, api);
											}
										}
									},
									"aoColumnDefs" : [ {
										"aTargets" : [ 2, 3, 4, 5, 6],
										"mRender" : function(data, type, full) {
											return data;
										}
									} ],
									"fnRowCallback" : function(nRow, aData, iDisplayIndex){
						                $("td:first", nRow).html(iDisplayIndex +1);
						               return nRow;
									}
					});
					/*$('#btnsearch')
							.on('click',
									function(event) {
										event.preventDefault();
										
										jQuery('.loader-class').modal('hide');
										
										reportdatatable.fnSetColumnVis(1, true);
									});*/
					 
					 $('#tblulbDCBcollection').show();
					 $('#tblulbDCBcollectionheader').show();
					 
					 
							});
				
					$('#region').change(function(){
						$.ajax({
							url: "/ptis/reports/getRegionHeirarchyByType",    
							type: "GET",
							data: {
								regionName : $('#region').val(),   
								type : 'DISTRICT'
							},
							dataType: "json",
							success: function (response) {
								$('#districtId').empty();
								$('#districtId').append($("<option value=''>All</option>"));
								$('#cityId').empty();
								$('#cityId').append($("<option value=''>All</option>"));
								
								$.each(response, function(index, value) {
									$('#districtId').append($('<option>').text(value.name).attr('value', value.name));
								});
								
							}, 
							error: function (response) {
								
							}
						});
					});
					
					$('#districtId').change(function(){
						$.ajax({
							url: "/ptis/reports/getRegionHeirarchyByType",    
							type: "GET",
							data: {
								regionName : $('#districtId').val(),   
								type : 'CITY'
							},
							dataType: "json",
							success: function (response) {
								$('#cityId').empty();
								$('#cityId').append($("<option value=''>All</option>"));
								$.each(response, function(index, value) {
									$('#cityId').append($('<option>').text(value.name).attr('value', value.name));
								});
								
							}, 
							error: function (response) {
							}
						});
					});
					
				
				});


function averageTotalFooter(colidx, api,totalsize) { 
	// Remove the formatting to get integer data for summation
	var intVal = function(i) {
		return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1
				: typeof i === 'number' ? i : 0;
	};

	// Total over all pages
	total = api.column(colidx).data().reduce(function(a, b) {
		return intVal(a) + intVal(b);
	});

	// Total over this page
	pageTotal = api.column(colidx, {
		page : 'current'
	}).data().reduce(function(a, b) {
		return intVal(a) + intVal(b);
	}, 0);
	// Update footer
	$(api.column(colidx).footer()).html(
			formatNumberInr((pageTotal/totalsize).toFixed(2)) + ' (' + formatNumberInr((total/totalsize).toFixed(2)) + ')');
}

function updateTotalFooter(colidx, api) {
	// Remove the formatting to get integer data for summation
	var intVal = function(i) {
		return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1
				: typeof i === 'number' ? i : 0;
	};

	// Total over all pages
	total = api.column(colidx).data().reduce(function(a, b) {
		return intVal(a) + intVal(b);
	});

	// Update footer
	$(api.column(colidx).footer()).html(total.toFixed(2));//formatNumberInr()
}
function formatNumberInr(x) {
	if (x) {
		x = x.toString();
		var afterPoint = '';
		if (x.indexOf('.') > 0)
			afterPoint = x.substring(x.indexOf('.'), x.length);
		x = Math.floor(x);
		x = x.toString();
		var lastThree = x.substring(x.length - 3);
		var otherNumbers = x.substring(0, x.length - 3);
		if (otherNumbers != '')
			lastThree = ',' + lastThree;
		var res = otherNumbers.replace(/\B(?=(\d{2})+(?!\d))/g, ",")
				+ lastThree + afterPoint;
		return res;
	}
	return x;
}