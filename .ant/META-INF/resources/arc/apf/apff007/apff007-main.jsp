<%@ page import="acf.acf.General.jsp.*"%>
<%@ page import="java.util.*" %>
<%@ taglib prefix="acf" uri="/acf/tld/acf-taglib" %> 

<div class="col-md-12 nopadding">
	<acf:Region id="reg_search" title="SEARCH" type="search">
		<acf:RegionAction>
			<a href="#" onClick="$(this).parents('.widget-box').pForm$clear();">Clear</a>
		</acf:RegionAction>
		
		<form id="frm_search" class="form-horizontal" data-role="search" >
	    	<div class="form-group">
	      		<div class="col-xs-4">
	      			<label for=s_business_platform style="display:block">Busi. Platform No.</label>
	      			<acf:ComboBox id="s_business_platform" name="business_platform" editable="false" multiple="false">
	      				<acf:Bind on="initData"><script>
		 					$(this).acfComboBox("init", ${busiplatform} );
		 				</script></acf:Bind>
		 			</acf:ComboBox>
	        	</div>
	    	</div>
		</form>
		
		
	</acf:Region>

</div>

<acf:Region id="reg_list" title="BUSINESS PLATFORM LIST" type="form">
	<acf:RegionAction>
		<a href="javascript:$('#grid_browse').pGrid$prevRecord();">Previous</a>
		&nbsp;
		<a href="javascript:$('#grid_browse').pGrid$nextRecord();">Next</a>
	</acf:RegionAction>
	<div class="col-xs-12">
			<acf:Grid id="grid_browse" url="apff007-search.ajax" readonly="true">
				<acf:Column name="business_platform" caption="Busi. Platform" width="150"></acf:Column>					
				<acf:Column name="department" caption="Prog. Nature/ Dept. No." width="150"></acf:Column>
				<acf:Column name="description" caption="Description" width="500"></acf:Column>
			</acf:Grid>
	</div>
</acf:Region>


<form id="frm_main" class="form-horizontal" data-role="form" >

	<acf:Region id="reg_form" title="BUSI. PLATFORM & PROG. NATURE/ DEPT. MAINTENANCE">
		<div class="form-group">		
			<div class="col-xs-12 form-padding oneline">
				<label class="control-label col-md-2" for="business_platform">Busi. Platform No.</label>
		 		<div class="col-md-3">
		 		   <acf:ComboBox id="business_platform" name="business_platform" editable="true" maxlength = "2" checkMandatory="true">  
      			       <acf:Bind on="initData"><script>
	 					 $(this).acfComboBox("init", ${busiplatform} );
	 				   </script></acf:Bind> 
	 				   
	 				   <acf:Bind on="change" modified="true"><script>

   						business_platform = $(this).getValue(); //this bloack is the same as onLoadSucess except $this value

   						if (business_platform == ""){
   							//$("#frm_main #busi_description").setValue("");	
   						}
   						else {					
	   						$.ajax({
								headers: {
									'Accept'       : 'application/json',
									'Content-Type' : 'application/json; charset=utf-8'
								},
								async  : false,
								type   : "POST",
								url    : "apff007-get-busi-desc.ajax",
								data   : JSON.stringify({
									'business_platform'	: business_platform,
								
								}),
								success: function(data) {
									if (data.busi_description != null) {
										$("#frm_main #busi_description").setValue(data.busi_description);
									}
									else {
										//$("#frm_main #busi_description").setValue("");
									}
								}
							});
   						}
					</script></acf:Bind>
					 
                    </acf:ComboBox>
		 		</div>
	 		</div>

<!-- 		<div class="col-xs-12 form-padding oneline">  	
				<label class="control-label col-md-2" for="description">Busi. Platform Desc.</label>
		 		<div class="col-md-6">		 		    
			   		<acf:TextBox id="busi_description" name="busi_description" editable= "false"></acf:TextBox> 
		 		</div>
	 		</div>   -->
	
			<div class="col-xs-12 form-padding oneline">  	
				<label class="control-label col-md-2" for="department">Prog. Nature/ Dept. No.</label>
		 		<div class="col-md-3">
			   		<acf:TextBox id="department" name="department" checkMandatory="true" maxlength="2" forceCase="upper"></acf:TextBox> 
		 		</div>
	 		</div>

			<div class="col-xs-12 form-padding oneline">  	
				<label class="control-label col-md-2" for="description">Prog. Nature/ Dept. Desc.</label>
		 		<div class="col-md-6">
			   		<acf:TextBox id="description" name="description" checkMandatory="true" maxlength="30"></acf:TextBox> 
		 		</div>
	 		</div>

			<div class="col-xs-12 form-padding oneline">  	
				<label class="control-label col-md-2" for="report_caption">Report Caption</label>
		 		<div class="col-md-4">
			   		<acf:TextBox id="report_caption" name="report_caption" checkMandatory="true" maxlength="15" forceCase="upper"></acf:TextBox> 
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
	searchKey : "business_platform",
	
	browseGrid: $("#grid_browse"),
	browseKey: "business_platform,department", 

	initMode: "",
	recordForm: $("#frm_main"),
	recordKey: {
		section_id: "${business_platform}",
		department: "$(department)",
	},
	getUrl: "apff007-get-form.ajax",
	saveUrl: "apff007-save.ajax",
	
	onLoadSuccess: function(data){		
	business_platform = $("#frm_main #business_platform").getValue();
   									
					$.ajax({
						headers: {
							'Accept'       : 'application/json',
							'Content-Type' : 'application/json; charset=utf-8'
						},
						async  : false,
						type   : "POST",
						url    : "apff007-get-busi-desc.ajax",
						data   : JSON.stringify({
							'business_platform'	: business_platform,
						}),
						success: function(data) {

							if (data.busi_description != null) {
								$("#frm_main #busi_description").setValue(data.busi_description);								
							}
							else {
								$("#frm_main #busi_description").setValue("");	
							}
						}
					});
   				
	},

}).executeSearchBrowserForm();

$(document).on('new', function() {
	$("#frm_main #busi_description").disable();
});

$(document).on('amend', function() {
	$("#frm_main #business_platform").disable();
	$("#frm_main #busi_description").disable();
});
</script>