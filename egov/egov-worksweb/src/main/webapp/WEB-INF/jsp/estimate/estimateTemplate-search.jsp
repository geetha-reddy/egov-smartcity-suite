<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ include file="/includes/taglibs.jsp" %>
<script>

function setupSubTypes(elem){
    categoryId=elem.options[elem.selectedIndex].value;
    populatesubType({category:categoryId});
}

loadSubType= function(req, res){
	if(dom.get('subTypeOfWork').value != ""){
		dom.get('subType').value=dom.get('subTypeOfWork').value;
	}
}

function submitEstimateTemplateSearchForm() {
	var mode=document.getElementById('mode').value;
	document.estimateTemplateSearchForm.status.disabled=false;
    document.estimateTemplateSearchForm.action='${pageContext.request.contextPath}/estimate/estimateTemplate-searchDetails.action?mode='+mode;
    dom.get('estimateTemplateCode').value="";
	dom.get('typeOfWork').value=""; 
	dom.get('subTypeOfWork').value="";
    document.estimateTemplateSearchForm.submit();
}

function bodyOnLoad(){
	if(dom.get('estimateTemplateCode').value != ""){
		dom.get('code').value=dom.get('estimateTemplateCode').value;  
	}
	if(dom.get('typeOfWork').value != ""){
		dom.get('workType').value=dom.get('typeOfWork').value;
		populatesubType({category:dom.get('workType').value});
	}
	 <s:if test="%{sourcePage.equals('searchForEstimate')}">
	 	document.estimateTemplateSearchForm.status.value=1;
	 	document.estimateTemplateSearchForm.status.disabled=true;
	 </s:if>
}
</script>

<html>
<title><s:text name='page.title.estimate.template.search'/></title>
<body onload="bodyOnLoad()" class="yui-skin-sam">

<script src="<cdn:url value='/resources/js/works.js?rnd=${app_release_no}'/>"></script>
   <s:hidden name="mode" id="mode" value="%{mode}" />
<div class="new-page-header">
	<s:text name="estimate.search.estimate.template"/>
</div>   
    <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="alert alert-danger" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
   </s:if>
   <s:if test="%{hasActionMessages()}">
       <div class="messagestyle">
        	<s:actionmessage theme="simple"/>
       </div>
   </s:if>
   
<s:form theme="simple" name="estimateTemplateSearchForm" onsubmit="return validateDataBeforeSubmit(this);" cssClass="form-horizontal form-groups-bordered">

  <s:hidden name="estimateTemplateCode" id="estimateTemplateCode" />
  <s:hidden name="typeOfWork" id="typeOfWork" />
  <s:hidden name="subTypeOfWork" id="subTypeOfWork" />
  
 <div class="alert alert-danger" id="estimateTemplate_error"	style="display: none;"></div>
 <div class="panel panel-primary" data-collapsed="0" style="text-align:left">
	<div class="panel-heading">
		<div class="panel-title">
		   <s:text name='title.search.criteria' />
		</div>
	</div>
	<div class="panel-body">
	  	<div class="form-group">
			<label class="col-sm-3 control-label text-right">
			    <s:text name="estimate.work.type" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:select headerKey="-1" headerValue="Select from below" name="workType" id="workType" cssClass="form-control" list="dropdownData.parentCategoryList" listKey="id" listValue="name" value="%{workType.id}" onChange="setupSubTypes(this);"/>
                <egov:ajaxdropdown id="categoryDropdown" fields="['Text','Value']" dropdownId='subType' url='estimate/ajaxEstimate-subcategories.action' selectedValue="%{subType.id}" afterSuccess="loadSubType" /> 
			</div>
			<label class="col-sm-2 control-label text-right">
			    <s:text name="estimate.work.subtype" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:select headerKey="-1" headerValue="Select from below" name="subType" value="%{subType.id}" id="subType" cssClass="form-control" list="dropdownData.categoryList" listKey="id" listValue="name"/>
			</div>
	  	</div>
		
		<div class="form-group">
			<label class="col-sm-3 control-label text-right">
			    <s:text name="estimate.template.search.code" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:textfield name="code" value="%{code}" id="code" cssClass="form-control" /> 
			</div>
			<label class="col-sm-2 control-label text-right">
			    <s:text name="estimate.template.search.name" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:textarea name="name" cols="35" cssClass="form-control" id="name" value="%{name}"/> 
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-3 control-label text-right">
			    <s:text name="estimate.template.search.status" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:select headerKey="1" list="#{'0':'INACTIVE', '1':'ACTIVE'}"  name="status"  value="%{1}" id="status" cssClass="form-control"/>
			</div>
		</div>
	</div>
</div>
  
<div class="row">
	<div class="col-xs-12 text-center buttonholdersearch">
		<input type="submit" class="btn btn-primary" value="Search" id="searchButton" name="button" onclick="submitEstimateTemplateSearchForm()" />&nbsp;
        <input type="button" class="btn btn-default" value="Close" id="closeButton" name="closeButton" onclick="window.close();" />
	</div>
</div>

<div class="row report-section">
   <s:if test="%{searchResult.fullListSize != 0}">
		<div class="col-md-12 table-header text-left">
		  <s:text name="searchEst.result"/>
		</div>
   </s:if>	
   <div class="col-md-12 report-table-container">
	   <%@ include file='estimateTemplate-searchResults.jsp'%> 
   </div>
</div>

<s:hidden name="sourcePage" id="sourcePage"	value="%{sourcePage}" />
								
</s:form>
</body>

</html>
