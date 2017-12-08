<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="acf" uri="/acf/tld/acf-taglib" %> 

<div class="col-md-12 nopadding">
	<acf:Region id="reg_search" title="SEARCH" type="search">
		<acf:RegionAction>
			<a href="#" onClick="$(this).parents('.widget-box').pForm$clear();">Clear</a>
		</acf:RegionAction>
		
		<form id="frm_search" class="form-horizontal" data-role="search" >
	    	<div class="form-group">
	      		<div class="col-xs-4">
	      			<label for=s_section_id style="display:block">Section ID.</label>
	      			<acf:ComboBox id="s_section_id" name="section_id" editable="false" multiple="false">
	      				<acf:Bind on="initData"><script>
		 					$(this).acfComboBox("init", ${sectionid} );
		 				</script></acf:Bind>
		 			</acf:ComboBox>
	        	</div>
	    	</div>
		</form>
		
		
	</acf:Region>

</div>

<acf:Region id="reg_list" title="ITEM CATEGORY LIST" type="form">
	<div class="widget-main no-padding">
		<div class="col-md-12 form-padding">
			<acf:Grid id="grid_browse" url="apff009-search.ajax" readonly="true">
				<acf:Column name="section_id" caption="Section ID" width="150"></acf:Column>			
				<acf:Column name="item_category_no" caption="Item Category" width="150"></acf:Column>
				<acf:Column name="item_category_description" caption="Item Category Description" width="500"></acf:Column>
			</acf:Grid>
	    </div>
	</div>
</acf:Region>


<form id="frm_main" class="form-horizontal" data-role="form" >

	<acf:Region id="reg_form" title="ITEM CATEGORY MAINTENANCE">
		<div class="form-group">		
			<div class="col-xs-12 form-padding oneline">
				<label class="control-label col-md-2" for="section_id">Section ID.</label>
		 		<div class="col-md-4">
		 		   <acf:ComboBox id="f_section_id" name="section_id" checkMandatory="true">  
      			       <acf:Bind on="initData"><script>
	 					 $(this).acfComboBox("init", ${sectionid} );
	 				   </script></acf:Bind>  
                    </acf:ComboBox>
		 		</div>
	 		</div>

			<div class="col-xs-12 form-padding oneline">  	
				<label class="control-label col-md-2" for="item_category_no">Item Cat. No.</label>
		 		<div class="col-md-3">		 		    
			   		<acf:TextBox id="item_category_no" name="item_category_no" checkMandatory="true" maxlength="3" forceCase="upper"></acf:TextBox> 
		 		</div>
	 		</div>
	
			<div class="col-xs-12 form-padding oneline">  	
				<label class="control-label col-md-2" for="item_category_description">Item Cat. Description</label>
		 		<div class="col-md-4">
			   		<acf:TextBox id="item_category_description" name="item_category_description" checkMandatory="true" maxlength="30"></acf:TextBox> 
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
	searchKey : "section_id",
	
	browseGrid: $("#grid_browse"),
	browseKey: "section_id,item_category_no", 

	initMode: "",
	recordForm: $("#frm_main"),
	recordKey: {
		section_id: "${section_id}",
		item_category_no: "$(itme_category_no)",
	},
	getUrl: "apff009-get-form.ajax",
	saveUrl: "apff009-save.ajax",

}).executeSearchBrowserForm();

$(document).on('amend', function() {
	$("#frm_main #f_section_id").disable();
});

</script>