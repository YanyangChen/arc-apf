<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="acf" uri="/acf/tld/acf-taglib" %> 

<div class="col-md-12 nopadding">
	<acf:Region id="reg_search" title="SEARCH" type="search">
		<acf:RegionAction>
			<a href="#" onClick="$(this).parents('.widget-box').pForm$clear();">Clear</a>
		</acf:RegionAction>
		
		<form id="frm_search" class="form-horizontal" data-role="search" >
	    	<div class="form-group">
	      		<div class="col-md-4">
	      			<label for=s_section_id style="display:block">Section ID.</label>
	      			<acf:ComboBox id="s_section_id" name="section_id" editable="false" multiple="false">
	      				<acf:Bind on="initData"><script>
		 					$(this).acfComboBox("init", ${sectionid} );
		 				</script></acf:Bind>
		 			</acf:ComboBox>
	        	</div>
	        	
	        	        	
      		<div class="col-md-2">    
      			<label for="p_from_year" style="display:block">From Year</label>  
      			<acf:TextBox id="s_ye_ar" name="ye_ar" maxlength="4" checkNumeric="true" checkMandatory="false">
      					<acf:Bind on="change"><script>
	 					var yr_val = $(this).acfTextBox("getValue");
	 					if (yr_val < 1999 || yr_val > 2099) {			 					
	 						//Alert.showError(ACF.getDialogMessage("ACF040E"));
	 					}
      				</script></acf:Bind>
      			</acf:TextBox>
        	</div>
	        	
	    	</div>
		</form>
		
		
	</acf:Region>

</div>

<acf:Region id="reg_list" title="LABOUR TYPE LIST" type="form">
	<div class="widget-main no-padding">
		<div class="col-md-12 form-padding">
			<acf:Grid id="grid_browse" url="apff010-search.ajax" readonly="true">
				<acf:Column name="ye_ar" caption="Year" width="50"></acf:Column>			
				<acf:Column name="section_id" caption="Section ID" width="100"></acf:Column>
				<acf:Column name="labour_type" caption="Labour Type" width="100"></acf:Column>
				<acf:Column name="labour_type_description" caption="Labour Type Description" width="300"></acf:Column>
			</acf:Grid>
	    </div>
	</div>
</acf:Region>


<form id="frm_main" class="form-horizontal" data-role="form" >

	<acf:Region id="reg_form" title="MONTHLY MANHOUR MAINTENANCE">
		<div class="form-group">		
			<div class="col-xs-12 form-padding oneline">
				<label class="control-label col-md-2" for="section_id">Section ID.</label>
		 		<div class="col-md-4">
		 		   <acf:ComboBox id="f_section_id" name="section_id" editable="false" multiple="false" checkMandatory="true">  
                    </acf:ComboBox>
		 		</div>
	 		</div>

			<div class="col-xs-12 form-padding oneline">  	
				<label class="control-label col-md-2" for="labour_tyep">Labour Type</label>
		 		<div class="col-md-4">
		 		   <acf:ComboBox id="f_labour_type" name="labour_type" editable="false" multiple="false" checkMandatory="true">  
                    </acf:ComboBox>
		 		</div>			

	 		</div>			
	
		</div>

 	  	<div class="widget-main no-padding">
  		   <div class="col-xs-12 form-padding oneline">
		   		<label class="control-label col-md-2" for="grid_manhour">No. of Manhour/Month</label>
		   		<div class="col-md-8">
				<acf:Grid id="grid_manhour" url="apff010-list.ajax" addable = "true" deletable="true" editable="true" rowNum="9999" autoLoad="false" multiLineHeader="true">
				    <acf:Column name="ye_ar" caption="Year" width="50" editable="true" checkMandatory="true" readonlyIfOld="true" columnKey="true"></acf:Column>
					<acf:Column name="jan_man_hour" caption="Jan" width="50" editable="true" maxlength="3" checkNumeric="true" checkMandatory="true"></acf:Column>
					<acf:Column name="feb_man_hour" caption="Feb" width="50" editable="true" maxlength="3" checkNumeric="true" checkMandatory="true"></acf:Column>
					<acf:Column name="mar_man_hour" caption="Mar" width="50" editable="true" maxlength="3" checkNumeric="true" checkMandatory="true"></acf:Column>
					<acf:Column name="apr_man_hour" caption="Apr" width="50" editable="true" maxlength="3" checkNumeric="true" checkMandatory="true"></acf:Column>
					<acf:Column name="may_man_hour" caption="May" width="50" editable="true" maxlength="3" checkNumeric="true" checkMandatory="true"></acf:Column>
					<acf:Column name="jun_man_hour" caption="Jun" width="50" editable="true" maxlength="3" checkNumeric="true" checkMandatory="true"></acf:Column>
					<acf:Column name="jul_man_hour" caption="Jul" width="50" editable="true" maxlength="3" checkNumeric="true" checkMandatory="true"></acf:Column>
					<acf:Column name="aug_man_hour" caption="Aug" width="50" editable="true" maxlength="3" checkNumeric="true" checkMandatory="true"></acf:Column>
					<acf:Column name="sep_man_hour" caption="Sep" width="50" editable="true" maxlength="3" checkNumeric="true" checkMandatory="true"></acf:Column>
					<acf:Column name="oct_man_hour" caption="Oct" width="50" editable="true" maxlength="3" checkNumeric="true" checkMandatory="true"></acf:Column>
					<acf:Column name="nov_man_hour" caption="Nov" width="50" editable="true" maxlength="3" checkNumeric="true" checkMandatory="true"></acf:Column>
					<acf:Column name="dec_man_hour" caption="Dec" width="50" editable="true" maxlength="3" checkNumeric="true" checkMandatory="true"></acf:Column>	
					<acf:Column name="section_id" caption="" hidden = "true"></acf:Column>	
					<acf:Column name="labour_type" caption="" hidden = "true"></acf:Column>	
					<acf:Column name="sub_section_id" caption="" hidden = "true"></acf:Column>	
				</acf:Grid>	 
				</div>
      		</div>
   		</div>  	
					
	</acf:Region>
		
		
	<acf:Region id="reg_stat" title="UPDATE STATISTICS">
		<div class="col-xs-12 form-padding">
     		<label class="control-label col-md-2" for="modified_at">Modified At:</label>
      		<div class="col-md-4">          
        		<acf:DateTime id="modified_at" name="modified_at" readonly="true" useSeconds="true"/>  	
        	</div>
     		<label class="control-label col-md-2" for="created_at">Created At:</label>
      		<div class="col-md-4"> 
      			<acf:DateTime id="created_at" name="created_at" readonly="true" useSeconds="true"/>           
      		</div>
    	</div>
    	<div class="col-xs-12 form-padding">
     		<label class="control-label col-md-2" for="modified_by">Modified By:</label>
      		<div class="col-md-4">          
        		<acf:TextBox id="modified_by" name="modified_by" readonly="true"/>  	
        	</div>
     		<label class="control-label col-md-2" for="created_by">Created By:</label>
      		<div class="col-md-4"> 
      			<acf:TextBox id="created_by" name="created_by" readonly="true"/>           
      		</div>
    	</div>
    	<input type="hidden" id="allow_print" name="allow_print" value="1"/>
	</acf:Region>
		
	</form>

<script>

Controller.setOption({
	searchForm:$("#frm_search"),
	searchKey : "section_id,ye_ar,labour_type",
	
	browseGrid: $("#grid_browse"),
	browseKey: "ye_ar,labour_type", 

	initMode: "",
	recordForm: $("#frm_main"),
	recordKey: {
		labour_type: "$(labour_type)",
		ye_ar: "$(ye_ar)",
	},
	getUrl: "apff010-get-form.ajax",
	saveUrl: "apff010-save.ajax",

	onNew: function(){
		$("#frm_main").pForm$clear();
		$("#frm_main").pForm$enableAll();
		
		$("#grid_manhour").pGrid$clear();

	},

	onClone: function() {
		$("#frm_main").pForm$enableAll();

		$("#grid_manhour").pGrid$copyRecord();
	},

	getSaveData: function() {

		$("#grid_manhour").pGrid$setHiddenValueForAllRecords("section_id", $("#frm_main #f_section_id").getValue());
		$("#grid_manhour").pGrid$setHiddenValueForAllRecords("labour_type", $("#frm_main #f_labour_type").getValue());
		$("#grid_manhour").pGrid$setHiddenValueForAllRecords("sub_section_id", "0");
		
		return JSON.stringify({
			'Manhour' : $("#grid_manhour").pGrid$getModifiedRecord(),
		});
	},

	onSaveSuccessCallback: function(data, mode) {
		if (mode == "amend" || mode == "clone") {
			$("#grid_manhour").pGrid$clear();
		}
	},

}).executeSearchBrowserForm();

$(document).on('amend', function() {
	$("#frm_main #f_section_id").disable();
	$("#frm_main #f_labour_type").disable();
});

$("#frm_main").pForm$setRelatedComboBox(${sectionLabour}, [$("#frm_main #f_section_id"), $("#frm_main #f_labour_type")]);

</script>