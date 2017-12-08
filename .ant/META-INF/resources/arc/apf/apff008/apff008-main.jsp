<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="acf" uri="/acf/tld/acf-taglib" %> 

<div class="col-xs-12 nopadding">
	<acf:Region id="reg_search" title="SEARCH" type="search">
		<acf:RegionAction>
			<a href="#" onClick="$(this).parents('.widget-box').pForm$clear();">Clear</a>
		</acf:RegionAction>
		
		<form id="frm_search" class="form-horizontal" data-role="search" >
	    	<div class="form-group">
	      		<div class="col-md-4">
	      			<label for=s_form_id style="display:block">Form ID.</label>
	      			<acf:ComboBox id="s_form_id" name="form_id" editable="false" multiple="false">
	      				<acf:Bind on="initData"><script>
		 					$(this).acfComboBox("init", ${formid} );
		 				</script></acf:Bind>
		 			</acf:ComboBox>
	        	</div>
	        	   	
  				<div class="col-md-2">  
       			<label for=p_year_month style="display:block">System Month</label>
        			<acf:DateTimePicker id="p_year_month" name="p_year_month" pickTime="false" checkMandatory="false" viewMode="months" displayformat="YYYY/MM">
        			</acf:DateTimePicker>
				</div>

    		</div>   	
	    	
		</form>
		
		
	</acf:Region>

</div>

<acf:Region id="autono_list" title="AUTO-NO GENERATOR LIST" type="form">
		<div class="col-md-8 form-padding">
			<acf:Grid id="grid_browse" url="apff008-search.ajax" readonly = "true">
				<acf:Column name="form_id" caption="Form ID" width="50">	</acf:Column>			
				<acf:Column name="description" caption="Description" width="300"></acf:Column>
				<acf:Column name="sys_yymm" caption="System Month  " width="100"></acf:Column>
				<acf:Column name="last_auto_no" caption="Last Autogen No." width="100"></acf:Column>
				<acf:Column name="system_year" caption="  " hidden = "true"></acf:Column>
				<acf:Column name="system_month" caption="  " hidden = "true"></acf:Column>
			</acf:Grid>
	    </div>
</acf:Region>


<form id="frm_main" class="form-horizontal" data-role="form" >

	<acf:Region id="reg_form" title="SYSTEM AUTO-NUMBER GENERATOR MAINTENANCE">
		<div class="form-group">		
			<div class="col-xs-12 form-padding oneline">
				<label class="control-label col-md-2" for="form_id">Form ID</label>
		 		<div class="col-md-2">
		 		   <acf:TextBox id="form_id" name="form_id" > </acf:TextBox>   
		 		</div>
	 		</div>

			<div class="col-xs-12 form-padding oneline">  	
				<label class="control-label col-md-2" for="descriptionp">Form ID Description</label>
		 		<div class="col-md-6">		 		    
			   		<acf:TextBox id="description" name="description"></acf:TextBox> 
		 		</div>
	 		</div>
	
		</div>

		<div class="widget-main no-padding">
		   <div class="col-xs-12 form-padding oneline">
		   		<label class="control-label col-md-2" for="grid_autono"></label>
		   		<div class="col-md-4">	   
				<acf:Grid id="grid_autono" url="apff008-list.ajax" readonly="true"autoLoad="false">				      
				<acf:Column name="form_id" caption="Form ID" width="50"></acf:Column>		
				<acf:Column name="sys_yymm" caption="Systen Month" width="50"></acf:Column>
				<acf:Column name="last_auto_no" caption="Last Autogen No." width="50"></acf:Column>	
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
	searchKey : "form_id,system_id,system_month",
	
	browseGrid: $("#grid_browse"),
	browseKey: "form_id,system_year,system_month", 

	initMode: "",
	recordForm: $("#frm_main"),
	recordKey: {
		form_id: "${form_id}",
		system_year: "$(system_year)",
		system_month: "$(system_month)",
	},
	getUrl: "apff008-get-form.ajax",
	saveUrl: "none",

}).executeSearchBrowserForm();

</script>