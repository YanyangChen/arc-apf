<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="acf" uri="/acf/tld/acf-taglib" %> 

<div class="col-md-12 nopadding">
	<acf:Region id="reg_search" title="SEARCH" type="search">
		<form id="frm_search" class="form-horizontal" data-role="search" >
	    	<div class="form-group">
	      		<div class="col-lg-2 col-md-2 col-sm-2 col-xs-2">
	      			<label for=supplier_code style="display:block">SUPPLIER CODE</label>
	      			<acf:ComboBox id="s_supplier_code" name="supplier_code">
	      			<acf:Bind on="initData"><script>
	 					$(this).acfComboBox("init", ${modules} );
	 				</script></acf:Bind>
	      			</acf:ComboBox>
	        	</div>
	      		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
	      			<label for=supplier_code style="display:block">SUPPLIER NAME</label>
	      			<acf:TextBox id="s_supplier_name" name="supplier_name"></acf:TextBox>
	        	</div>
	        	
	    	</div>
		</form>
	</acf:Region>
</div>

<!--   <acf:Region id="reg_form" title="SUPPLIER LIST">-->
	<form id="frm_main" class="form-horizontal" data-role="form" >
	
 	<acf:Region id="reg_div_list" type="list" title="SUPPLIER LIST">
   		<acf:RegionAction> 
			<a href="javascript:$('#grid_browse').pGrid$prevRecord();">Previous</a>
			&nbsp;
			<a href="javascript:$('#grid_browse').pGrid$nextRecord();">Next</a>
		</acf:RegionAction>
		<div class="col-xs-12">
			<acf:Grid id="grid_browse" url="apff002-search.ajax" readonly="true" autoLoad="false">
				<acf:Column name="supplier_code" caption="Supplier Code" width="50"></acf:Column>
				<acf:Column name="supplier_name" caption="Supplier Name" width="200"></acf:Column>
			
			</acf:Grid>
	    </div>
 	</acf:Region>
	
	<acf:Region id="reg_mod_main" title="SUPPLIER MAINTENANCE" type="form">
	
		<div class="form-group">		
			<div class="col-xs-12 form-padding oneline">
				<label class="control-label col-md-2" for="supplier_code">Supplier Code:</label>
		 		<div class="col-md-1">
			   		<acf:TextBox id="supplier_code" name="supplier_code" forceCase = "upper" maxlength="04" checkMandatory="true"/>
		 		</div>
	 		</div>

			<div class="col-xs-12 form-padding oneline">  	
				<label class="control-label col-md-2" for="supplier_name" >Supplier Name:</label>
		 		<div class="col-md-6">
			   		<acf:TextBox id="supplier_name" name="supplier_name" maxlength="60" /> 
		 		</div>
	 		</div>
	
	
			<div class="col-xs-12 form-padding oneline">  	
				<label class="control-label col-md-2" for="telephone_no">Telephone:</label>
		 		<div class="col-md-2">
			   		<acf:TextBox id="telephone_no" name="telephone_no" maxlength="12" format="^([- ]|[0-9]|){0,12}$"/> 
		 		</div>
	 		</div>
	
			<div class="col-xs-12 form-padding oneline">  	
				<label class="control-label col-md-2" for="fax_no">Fax. No.:</label>
		 		<div class="col-md-2">
			   		<acf:TextBox id="fax_no" name="fax_no" maxlength="12" format="^([- ]|[0-9]|){0,12}$"/> 
		 		</div>
	 		</div>

			<div class="col-xs-12 form-padding oneline">  	
				<label class="control-label col-md-2" for="contact_person">Contact Person:</label>
		 		<div class="col-md-4">
			   		<acf:TextBox id="contact_person" name="contact_person" maxlength="30" /> 
		 		</div>
	 		</div>
	 		
	 		
			<div class="col-xs-12 form-padding oneline">  	
				<label class="control-label col-md-2" for="email_address">Email:</label>
		 		<div class="col-md-4">
			   		<acf:TextBox id="email_address" name="email_address" maxlength="40" allowKey="[A-Za-z0-9._%+-@]" format="^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$"/> 
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
	searchKey: "supplier_code",
	browseKey: "supplier_code",
	//searchForm: $("#frm_search"),
	//browseKey: "section_no",
	
	
	//initMode: "${mode}",
	recordForm: $("#frm_main"),
	recordKey: {
		mod_id: "${supplier_code}"
	},
	getUrl: "apff002-get-form.ajax",
	saveUrl: "apff002-save.ajax",
	ready: function() { Action.setMode("search"); }
}).executeSearchBrowserForm(); 

$(document).on('clone', function() {
	$("#frm_main #supplier_code").setValue("");
});

$(document).on('amend', function() {
	$("#frm_main #supplier_code").disable();
});

</script>
					