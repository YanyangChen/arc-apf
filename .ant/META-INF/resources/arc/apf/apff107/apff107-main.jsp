<%@ page import="acf.acf.General.jsp.*"%>
<%@ page import="java.util.*" %>
<%@ taglib prefix="acf" uri="/acf/tld/acf-taglib" %> 
<%
	ACFgJspHelper helper = new ACFgJspHelper(session);
	helper.setConnection("ARCDB");
%>

<div class="col-xs-12 nopadding">
	<acf:Region id="reg_func_search" type="search" title="PO ENQUIRY SEARCH">
		<acf:RegionAction>
			<a href="#" onClick="$(this).parents('.widget-box').pForm$clear();">Clear</a>
		</acf:RegionAction>
		<form id="frm_search" class="form-horizontal" data-role="search" >
	    	<div class="form-group">

	     		<div class="col-lg-2 col-md-2 col-sm-2 col-xs-2">
	      			<label for=s_section_id style="display:block">SECTION ID.</label>
	      			<acf:ComboBox id="s_section_id" name="section_id" editable="true" multiple="false">
	      	  		<acf:Bind on="initData"><script>
	 						$(this).acfComboBox("init", ${SectionNo} );
	 				</script></acf:Bind> 
	      			</acf:ComboBox>
	        	</div>
	        	 
	        	
	   		<!--  	<div class="col-lg-2 col-md-2 col-sm-2 col-xs-2">
	      			<label for=supplier_code style="display:block">SUPPLIER CODE</label>
	      			<acf:ComboBox id="s_supplier_code" name="supplier_code">
	      			<acf:Bind on="initData"><script>
	 					$(this).acfComboBox("init", ${modules} );
	 				</script></acf:Bind>
	      			</acf:ComboBox>
	        	</div>
	        	
	      		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
	      			<label for=supplier_name style="display:block">SUPPLIER NAME</label>
	      			<acf:TextBox id="s_supplier_name" name="supplier_name"></acf:TextBox>
	        	</div>
	        -->
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
	        
	      		<div class="clearfix"></div>
	      		<div class="col-lg-3 col-md-3 col-sm-4 col-xs-4">
	      			<label for=s_po_date_from style="display:block">PO DATE FROM</label>
	      			<acf:DateTimePicker id="s_po_date_from" name="po_date_from" pickDate="true" pickTime="false" displayformat="YYYY/MM/DD"/>
	      		</div>
	      		<div class="col-lg-3 col-md-3 col-sm-4 col-xs-4">
	      			<label for=s_po_date_to style="display:block">PO DATE TO</label>
	      			<acf:DateTimePicker id="s_po_date_to" name="po_date_to" pickDate="true" pickTime="false" displayformat="YYYY/MM/DD"/>
	      		</div>
	
	
	    	</div>
		</form>

	</acf:Region>
	
	<form id="frm_main" class="form-horizontal" data-role="form" >
		<acf:Region id="reg_div_list" type="list" title="PURCHASE ORDER ENQUIRY">
   		<acf:RegionAction>
			<a href="javascript:$('#grid_browse').pGrid$prevRecord();">Previous</a>
			&nbsp;
			<a href="javascript:$('#grid_browse').pGrid$nextRecord();">Next</a>
		</acf:RegionAction>
		<div class="col-xs-12">
			<acf:Grid id="grid_browse" url="apff107-search.ajax" readonly="true" autoLoad="false">
			  	<acf:Column name="section_id" caption="Section" width="100" hidden="true"></acf:Column> 
				<acf:Column name="purchase_order_no" caption="PO. No." width="100"></acf:Column>
				<acf:Column name="purchase_order_date" caption="PO. Date" width="100" type="date"></acf:Column>
     			<acf:Column name="remarks" caption="Remarks" width="350"></acf:Column>
     			<acf:Column name="section_name" caption="Section Name" width="200"></acf:Column>
 				<acf:Column name="po_amount" caption="PO. Amount" width="200"  align= "right" useNumberFormat="true"></acf:Column>
      	  		<acf:Column name="supplier_name" caption="Supplier Name" width="200"></acf:Column>
     			<acf:Column name="order_quantity" caption="Order Qty" width="200" hidden="true"></acf:Column>
      			<acf:Column name="unit_cost" caption="Unit Cost" width="200" hidden="true"></acf:Column>
	       </acf:Grid>
	    </div>
	    
	    <div class="col-md-12 form-padding oneline">
	    	<div class="col-md-9"> </div>	
      		
     		<label class="control-label col-md-1" for="total_po_amount">Total: </label>
      		<div class="col-md-2">
      			<acf:TextBox id="total_po_amount" name="total_po_amount" readonly="true"  width="200"  align= "right"  useNumberFormat="true"></acf:TextBox>
        	</div>	        	
    	</div>
	    
	    
	    
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
	browseKey: "purchase_order_no",
	
	recordForm: $("#frm_dummy"),  // avoid to clear up the value of total amount by using a dummy form id.
	recordKey: {
		purchase_order_no: "${purchase_order_no}",
	},
	getUrl: "apff107-get-form.ajax",
	saveUrl: "apff107-save.ajax",
	ready: function() { Action.setMode("search"); },
	onLoadSuccess: function(data)
	{
	console.log("testing---------------------1");
	$("#total_po_amount").setValue($("#grid_browse").pGrid$getTotalOfColumn("po_amount"));
	console.log("tewsting---------------------2");
	},
	
	
}).executeSearchBrowserForm();

$("#grid_browse").on("afterLoadRecord", function(data){
// $("#total_po_amount").setValue($("#grid_browse").pGrid$getSumOfColumn("po_amount"));
var s =$("#grid_browse").pGrid$getTotalOfColumn("po_amount") ;
s = Math.round(s*100)/100;
$("#total_po_amount").setValue(s);
});

</script>
<%
	helper.close();
%>