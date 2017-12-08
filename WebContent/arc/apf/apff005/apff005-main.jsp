<%@ page import="acf.acf.General.jsp.*"%>
<%@ page import="java.util.*" %>
<%@ taglib prefix="acf" uri="/acf/tld/acf-taglib" %> 
<%
	ACFgJspHelper helper = new ACFgJspHelper(session);
	helper.setConnection("ARCDB");
%>

<div class="col-xs-12 nopadding">
	<acf:Region id="reg_func_search" type="search" title="GROUP NO. SEARCH">
		<acf:RegionAction>
			<a href="#" onClick="$(this).parents('.widget-box').pForm$clear();">Clear</a>
		</acf:RegionAction>
		<form id="frm_search" class="form-horizontal" data-role="search" >
	    	<div class="form-group">

	     		<div class="col-lg-3 col-md-4 col-sm-4 col-xs-6">
	      			<label for=s_section_id style="display:block">SECTION NO.</label>
	      			<acf:ComboBox id="s_section_id" name="section_id" editable="true" multiple="false">
	      	  		<acf:Bind on="initData"><script>
	 						$(this).acfComboBox("init", ${SectionNo} );
	 				</script></acf:Bind> 
	      			</acf:ComboBox>
	        	</div>
	        	 
	        		
	      		<div class="col-lg-2 col-md-2 col-sm-2 col-xs-3">
	      			<label for=s_func_id style="display:block">GROUP NO.</label>
	      			<acf:TextBox id="s_group_no" name="group_no" maxlength="2"/>  
	      		</div>
	
	    	</div>
		</form>

	</acf:Region>
	
	<form id="frm_main" class="form-horizontal" data-role="form" >
		<acf:Region id="reg_div_list" type="list" title="GROUP NO.LIST">
   		<acf:RegionAction>
			<a href="javascript:$('#grid_browse').pGrid$prevRecord();">Previous</a>
			&nbsp;
			<a href="javascript:$('#grid_browse').pGrid$nextRecord();">Next</a>
		</acf:RegionAction>
		<div class="col-xs-12">
			<acf:Grid id="grid_browse" url="apff005-search.ajax" readonly="true" autoLoad="false">
				<acf:Column name="section_id" caption="Section" width="100"></acf:Column>
				<acf:Column name="section_name" caption="Section name" width="350"></acf:Column>
				<acf:Column name="group_no" caption="Group No." width="100"></acf:Column>
				<acf:Column name="sub_section_id" caption="Sub Section Id" width="100" hidden="true"></acf:Column>
				<acf:Column name="group_name" caption="Group Name" width="350"></acf:Column>
				
			</acf:Grid>
	    </div>
	</acf:Region>
	
		<acf:Region id="reg_func_main" type="form" title="GROUP NO. MAINTENANCE">
    	<div class="col-xs-12 form-padding">
     		<label class="control-label col-md-2" for="section_id">Section No.:</label>
      		<div class="col-md-1">
      			<acf:TextBox id="section_id" name="section_id" maxlength="02" editable="false" multiple="false" format="[0]{1}[3-4]{1}" formatInstruction="Invalid Section Id., only 03 for Woodshop or 04 for Paintshop is allowed" checkMandatory="true">
      			
      			<acf:Bind on="change"><script>
   						section_id = $(this).getValue(); //this bloack is the same as onLoadSucess except $this value
   						
   						if (section_id == ""){
   							//$("#frm_main #supplier_desc").setValue("");	
   						}
   						else {					
	   						$.ajax({
								headers: {
									'Accept'       : 'application/json',
									'Content-Type' : 'application/json; charset=utf-8'
								},
								async  : false,
								type   : "POST",
								url    : "${ctx}/arc/apf/apff005/apff005-apf-get-section-name.ajax",
								                     
								data   : JSON.stringify({
									'section_id'	: section_id,
								}),
								success: function(data) {
									if (data.section_name != null) {
										$("#frm_main #section_name").setValue(data.section_name);
									}
									else {
										//$("#frm_main #supplier_desc").setValue("");
									}
								}
							});
   						}
					</script></acf:Bind>
      			</acf:TextBox>
        	</div>

     		<label class="control-label col-md-2" for="section_name">Section Name:</label>
      		<div class="col-md-4">       
      			<acf:TextBox id="section_name" name="section_name" maxlength="30"/>
      		</div>
      		
      		<div class="hidden">       
      			<acf:TextBox id="sub_section_id" name="sub_section_id" maxlength="01"/>
      			   
      		</div>
    	</div>  
    	<div class="col-xs-12 form-padding">
     		<label class="control-label col-md-2" for="group_no">Group No.:</label>
      		<div class="col-md-1">
      			<acf:TextBox id="group_no" name="group_no" maxlength="02" format="[0-9]{1}[1-9]{1}" formatInstruction="Invalid Group No., only 01 to 99 is allowed" checkMandatory="true"/> 
      		</div>
     		<label class="control-label col-md-2" for="group_name">Group Name:</label>
      		<div class="col-md-4">       
      			<acf:TextBox id="group_name" name="group_name" maxlength="30" checkMandatory="true"/>
      			   
      		</div>
      		
      		
      		
    	</div>  
    	
		<!-- <div class="col-xs-12 form-padding">
			<label class="control-label col-md-2" for="func_icon_name">Icon Name:</label>
    		 <div class="col-md-4">
    		    <acf:TextBox id="func_icon_name" name="func_icon_name" maxlength="16" button="fa-search">
	   		    	<acf:Bind target="button" on="click"><script>
	   		    		Dialog
							.create()
							.setCaption("Select icon")
							.setWidth(1250)
							.addDismissButton("Cancel")
							.setResultCallback(function(icon) {
								$("#func_icon_name").acfTextBox('setValue', icon);
								if($('#func_icon_name').acfTextBox('isModified'))
									$(document).trigger('modify');
							})
							.showUrl("../acf-select-icon-dialog");
	   		    	</script></acf:Bind>
    		    </acf:TextBox>
    		</div>
     		<label class="control-label col-md-2" for="func_status">Function Status:</label>
      		<div class="col-md-4">
	    		<acf:CheckBox id="func_status" name="func_status" label="Enable" choice="1,0"/>	
      		</div>
    	</div> -->
		<!--<div class="col-xs-12 form-padding">
			<label class="control-label col-md-2" for="allow_new">Allow Privileges:</label>
    		<div class="col-md-10">
    			<acf:CheckBox id="allow_view" name="allow_view" label="View" choice="1,0"/>
    			&nbsp;
    			<acf:CheckBox id="allow_new" name="allow_new" label="New" choice="1,0"/>
    			&nbsp;
    			<acf:CheckBox id="allow_clone" name="allow_clone" label="Clone" choice="1,0"/>
    			&nbsp;
    			<acf:CheckBox id="allow_amend" name="allow_amend" label="Amend" choice="1,0"/>
    			&nbsp;
    			<acf:CheckBox id="allow_delete" name="allow_delete" label="Delete" choice="1,0"/>
    		</div>
		</div>
		<div class="col-xs-12 form-padding">
			<label class="control-label col-md-2" for="is_menu">Show Menu:</label>
    		<div class="col-md-4">
    			<acf:CheckBox id="is_menu" name="is_menu" label="Menu" choice="1,0"/>
    		</div>
			<label class="control-label col-md-2" for="is_tran_log">Transaction Log:</label>
    		<div class="col-md-4">
    			<acf:CheckBox id="is_tran_log" name="is_tran_log" label="Enable" choice="1,0"/>
    		</div>
		</div>-->
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
 <!--  	<input type="hidden" id="allow_print" name="allow_print" value="1"/> -->   
	</acf:Region>
	</form>
	
	
</div>

<script>
/*
$("#frm_search").pForm$setRelatedComboBox(${moduleGroups}, [$("#frm_search #s_sec_no"), $("#frm_search #s_func_gp_name")]);
$("#frm_main").pForm$setRelatedComboBox(${moduleGroups}, [$("#frm_main #mod_id"), $("#frm_main #func_gp_name")]);
*/



Controller.setOption({
	searchForm: $("#frm_search"),
	browseGrid: $("#grid_browse"),
	browseKey: "section_id,sub_section_id,group_no",
	//redirect: "acff012-form",
	
	//initMode: "${mode}",
	recordForm: $("#frm_main"),
	recordKey: {
		section_id: "${section_id}",
		sub_section_id: "${sub_section_id}",
		group_no: "${group_no}",
	},
	getUrl: "apff005-get-form.ajax",
	saveUrl: "apff005-save.ajax",
	
}).executeSearchBrowserForm();

$(document).on('new', function() {
	 $("#frm_main #sub_section_id").setValue("0");
	 $("#frm_main #section_name").disable();
});

$(document).on('clone', function() {
	 $("#frm_main #sub_section_id").setValue("0");
	 $("#frm_main #section_name").disable();
	 
});

$(document).on('amend', function() {
	 $("#frm_main #section_name").disable();
});
</script>
<%
	helper.close();
%>