<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="acf" uri="/acf/tld/acf-taglib" %> 

<div class="col-md-12 nopadding">
	<acf:Region id="reg_search" title="SEARCH" type="search">
		<form id="frm_search" class="form-horizontal" data-role="search" >
	    	<div class="form-group">
	      		<div class="col-lg-2 col-md-1 col-sm-1 col-xs-1">
	      			<label for=actual_account_allocation style="display:block">ACTUAL A/C ALLOC. TYPE</label>
	      			<acf:ComboBox id="s_actual_account_allocation" name="actual_account_allocation">
	      			<acf:Bind on="initData"><script>
	 					$(this).acfComboBox("init", ${modules} );
	 				</script></acf:Bind>
	      			</acf:ComboBox>
	        	</div>
	      		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
	      			<label for=s_actual_account_allocation style="display:block">ACTUAL A/C ALLOC. DESC. </label>
	      			<acf:TextBox id="s_actual_account_description" name="actual_account_description"></acf:TextBox>
	        	</div>
	        	
	    	</div>
		</form>
	</acf:Region>
</div>

<!--   <acf:Region id="reg_form" title="A/C ALLOC. LIST">-->
	<form id="frm_main" class="form-horizontal" data-role="form" >
	
 	<acf:Region id="reg_div_list" type="list" title="A/C ALLOC. LIST">
   		<acf:RegionAction> 
			<a href="javascript:$('#grid_browse').pGrid$prevRecord();">Previous</a>
			&nbsp;
			<a href="javascript:$('#grid_browse').pGrid$nextRecord();">Next</a>
		</acf:RegionAction>
		<div class="col-xs-12">
			<acf:Grid id="grid_browse" url="apff006-search.ajax" readonly="true" autoLoad="false">
				<acf:Column name="actual_account_allocation" caption="Actual A/C Alloc." width="50"></acf:Column>
				<acf:Column name="actual_account_description" caption="Actual A/C Alloc. Desc." width="200"></acf:Column>
	   			<acf:Column name="budget_account_allocation" caption="Budget A/C Alloc." width="50"></acf:Column>
				<acf:Column name="budget_account_description" caption="Budget A/C Alloc. Desc." width="200"></acf:Column>
	
			
			</acf:Grid>
	    </div>
 	</acf:Region>
	
	<acf:Region id="reg_mod_main" title="A/C ALLOC. MAINTENANCE" type="form">
	
		<div class="form-group">		
			<div class="col-xs-12 form-padding oneline">
				<label class="control-label col-md-2" for="actual_account_allocation">Actual Alloc. Type:</label>
		 		<div class="col-md-1">
			   		<acf:TextBox id="actual_account_allocation" name="actual_account_allocation" forceCase = "upper" maxlength="02" format="^([A-Z ]|[0-9]){0,2}$" formatInstruction="Invalid format, A/C Alloc. Code should be two-characters " checkMandatory="true" />
		 		</div>
	 		</div>

		
			<div class="col-xs-12 form-padding oneline">  	
				<label class="control-label col-md-2" for="actual_account_description" >Actual Alloc. Desc.:</label>
		 		<div class="col-md-4">
			   		<acf:TextBox id="actual_account_description" name="actual_account_description" maxlength="30" checkMandatory="true" /> 
		 		</div>
	 		</div>
	 		
			<div class="col-xs-12 form-padding oneline">  	
				<label class="control-label col-md-2" for="actual_report_caption_1" >Actual Report Caption 1:</label>
		 		<div class="col-md-2">
			   		<acf:TextBox id="actual_report_caption_1" name="actual_report_caption_1" maxlength="15"></acf:TextBox> 
		 		</div>
	 		</div>
	
			<div class="col-xs-12 form-padding oneline">  	
				<label class="control-label col-md-2" for="actual_report_caption_2" >Actual Report Caption 2:</label>
		 		<div class="col-md-2">
			   		<acf:TextBox id="actual_report_caption_2" name="actual_report_caption_2" maxlength="15"></acf:TextBox> 
		 		</div>
	 		</div>
	
			<div class="col-xs-12 form-padding oneline">
				<label class="control-label col-md-2" for="budget_account_allocation">Budget Alloc. Type:</label>
		 		<div class="col-md-1">
			   		<acf:TextBox id="budget_account_allocation" name="budget_account_allocation" forceCase = "upper" maxlength="02" format="^([A-Z ]|[0-9]){0,2}$" formatInstruction="Invalid format, A/C Alloc. Code should be two-characters " checkMandatory="true" />
		 		</div>
	 		</div>

		
			<div class="col-xs-12 form-padding oneline">  	
				<label class="control-label col-md-2" for="budget_account_description" >Budget Alloc. Desc.:</label>
		 		<div class="col-md-4">
			   		<acf:TextBox id="budget_account_description" name="budget_account_description" maxlength="30" checkMandatory="true"></acf:TextBox> 
		 		</div>
	 		</div>
	 		
			<div class="col-xs-12 form-padding oneline">  	
				<label class="control-label col-md-2" for="budget_report_caption_1" >Budget Report Caption 1:</label>
		 		<div class="col-md-2">
			   		<acf:TextBox id="budget_report_caption_1" name="budget_report_caption_1" maxlength="15"></acf:TextBox> 
		 		</div>
	 		</div>
	
			<div class="col-xs-12 form-padding oneline">  	
				<label class="control-label col-md-2" for="budget_report_caption_2" >Budget Report Caption 2:</label>
		 		<div class="col-md-2">
			   		<acf:TextBox id="budget_report_caption_2" name="budget_report_caption_2" maxlength="15"></acf:TextBox> 
		 		</div>
	 		</div>
	 		<div class="col-xs-12 form-padding oneline">  	
				<label class="control-label col-md-2" for="sec_cost_element" >Secondary Cost Element:</label>
		 		<div class="col-md-2">
			   		<acf:TextBox id="sec_cost_element" name="sec_cost_element" maxlength="10" format="^([0-9]){10}$" formatInstruction="Invalid format, Secondary Cost Element should be 10 digits " checkMandatory="true" /> 
		 		</div>
	 		</div>
	

		</div>
	</acf:Region>	
		
	<acf:Region id="reg_stat" title="UPDATE STATISTICS">
		<div class="col-xs-12 form-padding">
     		<label class="control-label col-md-2" for="modified_at">Modified At:</label>
      		<div class="col-md-2">          
        		<acf:DateTime id="modified_at" name="modified_at" readonly="true" useSeconds="true"/>  	
        	</div>
     		<label class="control-label col-md-2" for="created_at">Created At:</label>
      		<div class="col-md-2"> 
      			<acf:DateTime id="created_at" name="created_at" readonly="true" useSeconds="true"/>           
      		</div>
    	</div>
    	<div class="col-xs-12 form-padding">
     		<label class="control-label col-md-2" for="modified_by">Modified By:</label>
      		<div class="col-md-2">          
        		<acf:TextBox id="modified_by" name="modified_by" readonly="true"/>  	
        	</div>
     		<label class="control-label col-md-2" for="created_by">Created By:</label>
      		<div class="col-md-2"> 
      			<acf:TextBox id="created_by" name="created_by" readonly="true"/>           
      		</div>
    	</div>
    	
	</acf:Region>
		
	</form>
<!-- </acf:Region> -->

<script>
Controller.setOption({
	searchForm: $("#frm_search"),
	browseGrid: $("#grid_browse"),
	searchKey: "actual_account_allocation",
	browseKey: "actual_account_allocation",
	//searchForm: $("#frm_search"),
	//browseKey: "supplier code",
	
	
	//initMode: "${mode}",
	recordForm: $("#frm_main"),
	recordKey: {
		actual_account_allocation: ""
	},
	getUrl: "apff006-get-form.ajax",
	saveUrl: "apff006-save.ajax",
}).executeSearchBrowserForm(); 

$(document).on('amend', function() {
	$("#frm_main #actual_acccount_allocation").disable();
});


$(document).on('view', function() {
	$("#frm_main").pForm$disableAll();
});

$(document).on('clone', function() {
     
     $("#frm_main #budget_account_allocation").setValue("");
});





</script>
