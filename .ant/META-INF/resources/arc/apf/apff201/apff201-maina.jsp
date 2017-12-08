<%@ page import="acf.acf.General.jsp.*"%>
<%@ page import="java.util.*" %>
<%@ taglib prefix="acf" uri="/acf/tld/acf-taglib" %> 
<%
	ACFgJspHelper helper = new ACFgJspHelper(session);
	helper.setConnection("ARCDB");
%>

<div class="col-xs-12 nopadding">
	<acf:Region id="reg_func_search" type="search" title="SECTION SEARCH">
		<acf:RegionAction>
			<a href="#" onClick="$(this).parents('.widget-box').pForm$clear();">Clear</a>
		</acf:RegionAction>
		<form id="frm_search" class="form-horizontal" data-role="search" >
	    	<div class="form-group">
				
				<div class="col-md-1">
	      			<label for=s_section_id style="display:block">Section ID</label>
	      			
	      			<acf:ComboBox id="s_section_id" name="section_id">
	      			<acf:Bind on="initData"><script>
	 					$(this).acfComboBox("init", ${sectionid} );
	 				</script></acf:Bind>
	      			</acf:ComboBox>    			
	        	</div>
				
				<div class="col-md-1">
	      			<label for=s_fr_form style="display:block">PAT/AT No. From</label>
	      			<acf:TextBox id="payment_form_no_from" name="payment_form_no_from" editable="true" maxlength = "11" forceCase="upper"></acf:TextBox>    			
	        	</div>

	      		<div class="col-md-1">
	      			<label for=s_to_form style="display:block">PAT/AT No. To</label>
	      			<acf:TextBox id="payment_form_no_to" name="payment_form_no_to" editable="true" maxlength = "11" forceCase="upper"></acf:TextBox>	      			
	        	</div>

				
	      		
	      		<div class="col-lg-2 col-md-2 col-sm-2 col-xs-2">
	      			<label for=supplier_code style="display:block">SUPPLIER CODE</label>
	      			<acf:ComboBox id="s_supplier_code" name="supplier_code">
	      			<acf:Bind on="initData"><script>
	 					$(this).acfComboBox("init", ${suppliercode} );
	 				</script></acf:Bind>
	      			</acf:ComboBox>
	        	</div>
	       		
	       		<div class="col-md-2">
	      			<label for=s_request_start_date style="display:block">Request Start Date</label>
	      			<acf:DateTimePicker id="s_request_start_date" name="request_start_date" pickTime="false">
	      				</acf:DateTimePicker> 	      			
	        	</div>
	        	
	        	<div class="col-md-2">
	      			<label for=s_request_end_date style="display:block">Request End Date</label>
	      			<acf:DateTimePicker id="s_request_end_date" name="request_end_date" pickTime="false">
	      				</acf:DateTimePicker> 	      			
	        	</div>

	      		<div class="col-md-1">
	      			<label for=s_created_by style="display:block">User ID</label>
	      			<acf:TextBox id="s_created_by" name="created_by" editable="true" maxlength = "11" forceCase="upper"></acf:TextBox>    			
	        	</div>
	     
	       		
	    	</div>
		</form>

	</acf:Region>
	
	<form id="frm_main" class="form-horizontal" data-role="form" >
		<acf:Region id="reg_div_list" type="list" title="MATERIAL COST ENQUIRY ">
   		<acf:RegionAction>
			<a href="javascript:$('#grid_browse').pGrid$prevRecord();">Previous</a>
			&nbsp;
			<a href="javascript:$('#grid_browse').pGrid$nextRecord();">Next</a>
		</acf:RegionAction>
		<div class="col-xs-12">
			<acf:Grid id="grid_browse" url="apff201-get-form.ajax" readonly="true" autoLoad="false" rowNum="1">
				<acf:Column name="show_records" caption=" " width="60"></acf:Column>
			</acf:Grid>

	    </div>
	</acf:Region>
		<acf:Region id="reg_div_list" type="list" title="MATERIAL COST ENQUIRY2 ">
   		<acf:RegionAction>
			<a href="javascript:$('#grid_browse').pGrid$prevRecord();">Previous</a>
			&nbsp;
			<a href="javascript:$('#grid_browse').pGrid$nextRecord();">Next</a>
		</acf:RegionAction>
		<div class="col-xs-12">
			<acf:Grid id="grid2_browse" url="apff201-get-form.ajax" editable="true" autoLoad="false" addable="true" deletable="true" rowNum="9999" multiLineHeader="true">
				<acf:Column name="section_id" caption="Section ID" width="60"></acf:Column>
				<acf:Column name="payment_form_no" caption="PAT/AT No." width="100"></acf:Column>
				<acf:Column name="request_date" caption="Request Date" width="100" type="date"></acf:Column>
				<acf:Column name="payment_purpose" caption="Purpose" width="200"></acf:Column>
				<acf:Column name="supplier_code" caption="Pay to Supplier Code" width="60"></acf:Column>
				<acf:Column name="created_by" caption="User ID" width="80"></acf:Column>
				<acf:Column name="print" caption="Print" width="100" type="checkBox" readonly="false" editable="true"></acf:Column>
	       </acf:Grid>

	    </div>
	    </acf:Region>
	
	<acf:Region id="reg_mod_main" title="Report MAINTENANCE" type="form">	
			
			<div class="col-xs-12 form-padding">
				<label class="control-label col-md-2" for="payment_form_no">Payment Form No:</label>

				<div class="col-md-2">

					<acf:TextBox id="payment_form_no" name="payment_form_no" maxlength="8" checkMandatory="false"/>
				</div>
			</div>
			
	</acf:Region>
	
	<acf:Region id="reg_stat" title="UPDATE STATISTICS">
		<div class="col-xs-12 form-padding oneline">
     		<label class="control-label col-md-2" for="modified_at">Modified At:</label>
      		<div class="col-md-4">          
        		<acf:DateTime id="modified_at" name="modified_at" readonly="true" useSeconds="true"/>  	
        	</div>
     		<label class="control-label col-md-2" for="created_at">Created At:</label>
      		<div class="col-md-4"> 
      			<acf:DateTime id="created_at" name="created_at" readonly="true" useSeconds="true"/>           
      		</div>
    	</div>
    	<div class="col-xs-12 form-padding oneline">
     		<label class="control-label col-md-2" for="modified_by">Modified By:</label>
      		<div class="col-md-4">          
        		<acf:TextBox id="modified_by" name="modified_by" readonly="true"/>  	
        	</div>
     		<label class="control-label col-md-2" for="created_by">Created By:</label>
      		<div class="col-md-4"> 
      			<acf:TextBox id="created_by" name="created_by" readonly="true"/>           
      		</div>
    	</div>

	</acf:Region>	
	</form>
	
	
</div>

<script>



Controller.setOption({
	searchForm: $("#frm_search"),
	browseGrid: $("#grid_browse"),
	browseKey: "section_id,sub_section_id",
	//redirect: "acff012-form",
	
	//initMode: "${mode}",
	recordForm: $("#frm_main"),
	recordKey: {
		section_id: "${section_id}",
		sub_section_id: "${sub_section_id}",
	},
	getUrl: "apff201-get-form.ajax",
	saveUrl: "apff201-save.ajax",
ready: function() { Action.setMode("search"); }	
}).executeSearchBrowserForm();

$(document).on('new', function() {
	$("#frm_main #sub_section_id").setValue("0");
});

</script>
<%
	helper.close();
%>