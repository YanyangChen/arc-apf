<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="acf" uri="/acf/tld/acf-taglib" %> 

<core:set var="isDialog" value="${ not empty param.type ? true : false }" />

<script>
	function consumptionSearch() {
			console.log("prog. consumption search");

			var item_param = $("#frm_search").pSearch$getCriteria();
			var labour_param = $("#frm_search").pSearch$getCriteria();
			
			$("#grid_item_consumption").pGrid$addCriteria(item_param);
			$("#grid_item_consumption").pGrid$loadRecord();

			$("#grid_labour_consumption").pGrid$addCriteria(labour_param);
			$("#grid_labour_consumption").pGrid$loadRecord();			
	}

	function selectDates_OnValidate(sender, e){
		var selectStart = $("#s_select_fr_date");
		var selectEnd = $("#s_select_to_date");
		var startDate = moment(parseInt(selectStart.getValue()));
		var endDate = moment(parseInt(selectEnd.getValue()));
		if(startDate.isValid() && endDate.isValid()){
			if(startDate.diff(endDate, "days") > 0)
				$(sender).setError(ACF.getQtipHint('ACF069V'), "DateRange");
			else selectStart.add(selectEnd).setError(null, "DateRange");
		}else selectStart.add(selectEnd).setError(null, "DateRange");
	}
</script>

<div class="col-md-12 nopadding">
	<acf:Region id="reg_search" title="SEARCH" type="search">
		<acf:RegionAction>
			<a href="#" onClick="$(this).parents('.widget-box').pForm$clear();">Clear</a>
		</acf:RegionAction>
		
 		<form id="frm_search" class="form-horizontal" data-role="search" >
	    	<div class="form-group">

			<div class="col-xs-12 form-padding">
	      		<div class="col-md-2">
	      			<label for=s_programme_no style="display:block">Programme No.</label>
	      			<acf:TextBox id="s_programme_no" name="programme_no" button="fa-search" maxlength="9" allowKey="[tT0-9]" forceCase="upper" checkMandatory="true" format="(T[0-9]{8}|[0-9]{1,9})"> 
	      				<acf:Bind target="button" on="click"><script>
	      					programme_no = null;
		   		    		Dialog.create()
								.setCaption("Search")
								.setWidth(1000)
								.addDismissButton("OK", function(){
									if ($.type(programme_no) == "string"){
										$("#frm_search #s_programme_no").setValue(programme_no);
										programme_no = "";
									}
								})
								.setResultCallback(function(result) {
									programme_no = result.programme_no;
								})
								.showUrl("../../apf/apff011/apff011-search-arc-prog");
	   					</script></acf:Bind>
	      			</acf:TextBox>
	      		</div> 	

	       		<div class="col-md-2">
	      			<label for=s_select_fr_date style="display:block">Selection Start Date</label>
	      			<acf:DateTimePicker id="s_select_fr_date" name="select_fr_date" pickTime="false">
	      				<acf:Bind on="validate"><script>selectDates_OnValidate(this, e);</script></acf:Bind></acf:DateTimePicker> 	      			
	        	</div>
	        	
	        	<div class="col-md-2">
	      			<label for=s_select_to_date style="display:block">End Date</label>
	      			<acf:DateTimePicker id="s_select_to_date" name="select_to_date" pickTime="false">
	      				<acf:Bind on="validate"><script>selectDates_OnValidate(this, e);</script></acf:Bind></acf:DateTimePicker> 	      			
	        	</div> 
	        </div>

			<div class="col-md-10 form-padding">
	      		<label for=s_section_id style="display:block">Section Id.</label>
	      		<acf:ComboBox id="s_section_id" name="section_id" editable="true" maxlength = "2" multiple="true">
	      			<acf:Bind on="initData"><script>
		 					$(this).acfComboBox("init", ${sectionselect} );
			   	</script></acf:Bind></acf:ComboBox>
	        </div>	 
	        	
	        </div>  	  	    	
		</form>
				
	</acf:Region>

</div>

<form id="frm_main" class="form-horizontal" data-role="form">
		<acf:Region id="reg_form" title="PROGRAMME BASIC INFO.">

			<div class="col-xs-12 form-padding">
				<label class="control-label col-md-2" for="programme_no">Programme No. :</label>
				<div class="col-md-2">
					<acf:TextBox id="programme_no" name="programme_no" checkMandatory="false" maxlength="9" >
						<acf:Bind on="change"><script>
	   					programme_no = $(this).getValue();                  

   						if ($.trim(programme_no) != ""){ 
	   						$.ajax({
								headers: {
									'Accept'       : 'application/json',
									'Content-Type' : 'application/json; charset=utf-8'
								},
								async  : false,
								type   : "POST",
								url    : "apff102-get-programme-bp.ajax",
								data   : JSON.stringify({
									'programme_no'	: programme_no,
								
								}),
								success: function(data) {
									if (data.busi_desc) {
								     $("#frm_main #busi_description").setValue($.trim(data.busi_desc.description));
									}
									if (data.dept_desc) {
								     $("#frm_main #dept_description").setValue($.trim(data.dept_desc.description));
									}
									
								}
							});
   						}
					</script></acf:Bind></acf:TextBox>	
				</div>
			</div>
			
			<div class="col-xs-12 form-padding">
				<label class="control-label col-md-2" for="programme_name">Programme Name :</label>
				<div class="col-md-6">
					<acf:TextBox id="programme_name" name="programme_name" maxlength="60" />
				</div>
			</div>
			
			<div class="col-xs-12 form-padding">
				<label class="control-label col-md-2" for="chinese_programme_name">Chin. Programme Name :</label>
				<div class="col-md-4">
					<acf:TextBox id="chinese_programme_name" name="chinese_programme_name" checkMandatory="false" maxlength="20" />
				</div>
			</div>
			
			<div class="col-xs-12 form-padding">
				<label class="control-label col-md-2" for="busi_description">Business Platform :</label>

				<div class="col-md-4">
					<acf:TextBox id="busi_description" name="busi_description" maxlength="30" />
				</div>
				
				<label class="control-label col-md-2" for="dept_description">Prog. Nature/ Dept. :</label>

				<div class="col-md-4">
					<acf:TextBox id="dept_description" name="dept_descriptiom" maxlength="30" />
				</div>

			</div>

			<div class="col-xs-12 form-padding">
				<label class="control-label col-md-2" for="no_of_episode">No. of Episode :</label>

				<div class="col-md-2">
					<acf:TextBox id="no_of_episode" name="no_of_episode" checkMandatory="false" maxlength="5" />
				</div>
					
				<label class="control-label col-md-1" for="programme_type">Prog. Type :</label>

				<div class="col-md-2">
					<acf:TextBox id="programme_type" name="programme_type" checkMandatory="false" maxlength="10" />
				</div>
		

				<label class="control-label col-md-1" for="s_transfer_status">Transfer Status :</label>

				<div class="col-md-2">
					<acf:TextBox id="s_transfer_status" name="s_transfer_status"/>
	
				</div>
				
			</div>
			
			<div class="col-xs-12 form-padding">
				<label class="control-label col-md-2" for="vtr_from_date">VTR Period Start :</label>

				<div class="col-md-2">
					<acf:DateTime id="vtr_from_date" name="vtr_from_date" pickTime="false" readonly="true" />
				</div>
				
				<label class="control-label col-md-1" for="vtr_to_date">End :</label>

				<div class="col-md-2">
					<acf:DateTime id="vtr_to_date" name="vtr_to_date" pickTime="false" readonly="true" />
				</div>
			</div>
			
			<div class="col-xs-12 form-padding">
				<label class="control-label col-md-2" for="efp_from_date">EFP Period Start:</label>
				<div class="col-md-2">
					<acf:DateTime id="efp_from_date" name="efp_from_date" pickTime="false" readonly="true" />
				</div>
				<label class="control-label col-md-1" for="efp_to_date">End :</label>
				<div class="col-md-2">
					<acf:DateTime id="efp_to_date" name="efp_to_date" pickTime="false" readonly="true" />
				</div>
			</div>
			
			<div class="col-xs-12 form-padding">
				<label class="control-label col-md-2" for="executive_producer">Executive Producer :</label>

				<div class="col-md-4">
					<acf:TextBox id="executive_producer" name="executive_producer"
						checkMandatory="false" maxlength="30" />
				</div>
			</div>
			
			<div class="col-xs-12 form-padding">
				<label class="control-label col-md-2"
					for="principal_setting_designer">P. Designer Setting :</label>

				<div class="col-md-4">
					<acf:TextBox id="principal_setting_designer"
						name="principal_setting_designer" checkMandatory="false" maxlength="30" />
				</div>
				
				<label class="control-label col-md-2" for="setting_designer">Designer Setting :</label>

				<div class="col-md-4">
					<acf:TextBox id="setting_designer" name="setting_designer" maxlength="30" />
				</div>
			</div>
			
			<div class="col-xs-12 form-padding">
				<label class="control-label col-md-2"
					for="principal_costume_designer">P. Designer Costume :</label>

				<div class="col-md-4">
					<acf:TextBox id="principal_costume_designer"
						name="principal_costume_designer" checkMandatory="false" maxlength="30" />
				</div>
				
				<label class="control-label col-md-2" for="costume_designer">Designer Costume :</label>

				<div class="col-md-4">
					<acf:TextBox id="costume_designer" name="costume_designer" maxlength="30" />
				</div>
			</div>


		</acf:Region>

		
<!--  	<acf:Region id="reg_stat" title="UPDATE STATISTICS">
			<div class="col-xs-12 form-padding oneline">
				<label class="control-label col-md-2" for="modified_at">Modified
					At:</label>
				<div class="col-md-4">
					<acf:DateTime id="modified_at" name="modified_at" readonly="true"
						useSeconds="true" />
				</div>
				<label class="control-label col-md-2" for="created_at">Created
					At:</label>
				<div class="col-md-4">
					<acf:DateTime id="created_at" name="created_at" readonly="true"
						useSeconds="true" />
				</div>
			</div>
			<div class="col-xs-12 form-padding oneline">
				<label class="control-label col-md-2" for="modified_by">Modified
					By:</label>
				<div class="col-md-4">
					<acf:TextBox id="modified_by" name="modified_by" readonly="true" />
				</div>
				<label class="control-label col-md-2" for="created_by">Created
					By:</label>
				<div class="col-md-4">
					<acf:TextBox id="created_by" name="created_by" readonly="true" />
				</div>
			</div>
		</acf:Region> -->


<acf:Region id="consumption_headers" title="COST SUMMATION " type="list">
	<div class="col-xs-12">
	<div class="tabbable">

	<ul class="nav nav-tabs" id="catTab">
		<li class="active"><a data-toggle="tab" href="#tab_item">MATERIAL </a></li>
		<li><a data-toggle="tab" href="#tab_labour">LABOUR</a></li>
	</ul>

	<div class="tab-content">
		<div id="tab_item" class="tab-pane fade in active">
		<div class="col-md-12 float-none">	
				<acf:Grid id="grid_item_consumption" url="apff102-get-item.ajax" rowNum="20" readonly="true" autoLoad="false">
				    
				    <acf:Column name="sec_id" caption="Section Id. " width="50"></acf:Column>
				    <acf:Column name="sec_name" caption="Section Name " width="150"></acf:Column>
				    
					<acf:Column name="ac_alloc" caption="A/C Alloc." width="50"></acf:Column>
					<acf:Column name="ac_alloc_desc" caption="A/C Alloc. Desc." width="150"></acf:Column>
					<acf:Column name="compare_date" caption="selected Date"  width="100" type="date"></acf:Column>
					<acf:Column name="sub_amt" caption="Amount" width="200" type="number" align="right" useNumberFormat="true" decimalPlaces="2"></acf:Column>
					<acf:Column name="prog_no" caption="" hidden = "true"></acf:Column>

				</acf:Grid>
			<div class="col-xs-12" style="height:20px"></div>
			<div class="col-md-9"></div>	
     		<label class="control-label col-md-1" for="ttl_item">Total Amount</label>
			<div class="col-md-2">
			<acf:TextBox id="ttl_item" name="ttl_item"></acf:TextBox></div>	
			
		</div>
		</div>

		<div id="tab_labour" class="tab-pane fade">
			<div class="col-md-12 float-none">
				<acf:Grid id="grid_labour_consumption" url="apff102-get-labour.ajax" rowNum="20" readonly="true" autoLoad="false">
					<acf:Column name="sec_id" caption="Section ID." width="50"></acf:Column>
					<acf:Column name="sec_name" caption="Section Name " width="100"></acf:Column>

					<acf:Column name="lab_type" caption="Labour Type" width="50"></acf:Column>
					<acf:Column name="lab_type_desc" caption="Labour Type Desc." width="100"></acf:Column>
					 <acf:Column name="compare_date" caption="selected Date"  width="100" type="date"></acf:Column>
					<acf:Column name="sub_no_of_hrs" caption="No. of Hour" width="50" type="number" align="right" useNumberFormat="true" decimalPlaces="2"></acf:Column>
					<acf:Column name="hrly_rate" caption="Hour Rate" width="50" type="number" align="right" useNumberFormat="true" decimalPlaces="2"></acf:Column>
					
					
					<acf:Column name="sub_lab_amt" caption="Amount ($)" width="80" type="number" align="right" useNumberFormat="true" decimalPlaces="2"></acf:Column>
				    <acf:Column name="compare_date" caption="selected Date"  width="100" type="date"></acf:Column>
					<acf:Column name="prog_no" caption="" hidden = "true"></acf:Column>
					
				</acf:Grid>
			<div class="col-xs-12" style="height:20px"></div>
			<div class="col-md-9"></div>	
     		<label class="control-label col-md-1" for="ttl_labour">Total Amount</label>
			<div class="col-md-2">
			<acf:TextBox id="ttl_labour" name="ttl_labour"></acf:TextBox></div>	
			
		</div>
		</div>			

	</div>
	</div>	        	
   	</div>  
   	    		        	
</acf:Region>   

		<div class="col-xs-12 form-padding oneline">
       	<div class="hidden">  
      			<acf:TextBox id="total_item" name="total_item"></acf:TextBox>
      			<acf:TextBox id="total_labour" name="total_labour"></acf:TextBox>
      			<acf:TextBox id="transfer_status" name="transfer_status"></acf:TextBox>
        	</div> 	    	  

    	</div> 

</form>

<core:choose>
	<core:when test="${isDialog }">
		<script>consumptionSearch();</script>
	</core:when>
	<core:otherwise>

<script>

Controller.setOption({
	searchForm: $("#frm_search"),
	searchKey: "programme_no",
	browseKey: "programme_no",	
	
	initMode: "${mode}",
	recordForm: $("#frm_main"),
	recordKey: {
		programme_no: "${programme_no}"
	},
	getUrl: "apff102-get-form.ajax",
	saveUrl: "none",
	onLoadSuccess: function(data){
	    if ($("#frm_main #transfer_status").getValue() == 'A'){
	    	$("#frm_main #s_transfer_status").setValue("ACTIVE")}
	    else {
	    	if ($("#frm_main #transfer_status").getValue() == 'I')
	    	$("#frm_main #s_transfer_status").setValue("INACTIVE")
	    	else {
	    	if ($("#frm_main #transfer_status").getValue() == 'P')
	    	$("#frm_main #s_transfer_status").setValue("PENDING")}};
	    if ($("#frm_main #programme_no").getValue() != "") consumptionSearch();
	}
}).executeSingleRecSearchBrowserForm();

</script>
	</core:otherwise>
</core:choose>

<script>
$('#catTab a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
		$(window).trigger("resize");
		
		if($(e.target).text() == "ITEM") {	
			result = $("#grid_item_consumption").pGrid$getSelectedRecord();	
			$("#ttl_item").setValue($("#frm_main #total_item").getValue());
		}
		else {if ($(e.target).text() == "LABOUR") {
			result = $("#grid_labour_consumption").pGrid$getSelectedRecord();
			$("#ttl_labour").setValue($("#frm_main #total_labour").getValue());
		} 
		}
		
		
		Dialog.get().setResult(result);
		$(window).trigger('resize');
});

$("#grid_item_consumption").on("afterLoadRecord", function(){
	$("#ttl_item").setValue($("#frm_main #total_item").getValue());
});
$("#grid_labour_consumption").on("afterLoadRecord", function(){
	$("#ttl_labour").setValue($("#frm_main #total_labour").getValue());	
});
</script>