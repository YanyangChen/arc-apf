<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="acf" uri="/acf/tld/acf-taglib" %> 
	
<div class="col-md-12 nopadding">
	<acf:Region id="reg_func_search" type="search" title="SEARCH">
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
	
</div>


<form id="frm_main" class="form-horizontal" data-role="form" >
		
	<acf:Region id="payment_request_headers" title="PRINT PAYMENT REQUEST FORM" type="form">

		<div class="col-md-12">			
			<acf:Grid id="grid_payment_request" url="apff201-list.ajax" editable="true" autoLoad="false" addable="false" deletable="false" rowNum="9999" >
				<acf:Column name="section_id" caption="Section ID" width="60"></acf:Column>
				<acf:Column name="payment_form_no" caption="PAT/AT No." width="100"></acf:Column>
				<acf:Column name="request_date" caption="Request Date" width="100" type="date"></acf:Column>
				<acf:Column name="payment_purpose" caption="Purpose" width="200"></acf:Column>
				<acf:Column name="supplier_code" caption="Pay to Supplier Code" width="60"></acf:Column>
				<acf:Column name="supplier_name" caption="Supplier Name" width="60"></acf:Column>
				<acf:Column name="created_by" caption="User ID" width="80"></acf:Column>
				<acf:Column name="print_indicator" caption="Print" width="100" type="checkBox" readonly="false" editable="true"></acf:Column>
	       		<acf:Column name="modified_at" caption="" hidden="true"></acf:Column>
	       		<acf:Column name="printed_at" caption="" hidden="true"></acf:Column>
	       		<acf:Column name="printed_by" caption="" hidden="true"></acf:Column>
	       		
	       		
	       </acf:Grid>
			
	    </div>
	    
		<div class="hidden">
			<input id="print_indicator" name="print_indicator" value="0"/>
 		</div>
 		
 		<div class="col-xs-12 form-padding oneline">
	    <div class="col-md-11">
      		</div>	
     		
     		<acf:Button id="Generate_Report" title="Print">
			<acf:Bind on="click">
	    			<script>
	    				
					
	  					$.ajax({
						headers: {
							'Accept'       : 'application/json',
							'Content-Type' : 'application/json; charset=utf-8'
						},
						async  : false,
						type   : "POST",
						url    : "${ctx}/arc/apf/apff201/apff201-apf-generate-report.ajax",
						data   : JSON.stringify({
							'payment_request' : $("#grid_payment_request").pGrid$getModifiedRecord()
						}),
						success: function(data) {
							if (data.report) {
								report = data.report;
								popupPrint(report);
								
							} else {
								Alert.showError(ACF.getDialogMessage("ACF040E"));
							}
						}
						
						
	    			});
	    			</script>
	    		</acf:Bind>
</acf:Button>
     		        	
    	</div>
	</acf:Region>    	
		
	</form>

<script>

Controller.setOption({
	searchForm:$("#frm_search"),
	searchKey : "payment_form_no,payment_purpose,section_id,supplier_code,supplier_name,request_date,created_by,payment_form_no_from,payment_form_no_to,request_start_date,request_end_date,print_indicator",
	browseKey: "payment_form_no,payment_purpose,section_id,supplier_code,supplier_name,request_date,created_by,payment_form_no_from,payment_form_no_to,request_start_date,request_end_date,print_indicator", 

	initMode: "none",
	recordForm: $("#frm_main"),
	recordKey: {
	    payment_form_no: "$(payment_form_no)",
	},
	getUrl: "apff201-list.ajax",
	saveUrl: "apff201-save.ajax",

	onLoadSuccess: function(data)
	{
	//getSupplierDesc();
	},


	getSaveData: function() {
		//var request_records = $("#grid_payment_request").pGrid$getModifiedRecord();
		return JSON.stringify({
			'payment_request' : $("#grid_payment_request").pGrid$getModifiedRecord(), 
		});
	},	


}).executeSingleRecSearchBrowserForm();

$(document).on("view", function() {


});

// $(document).on("save", function() {
// console.log($("#grid_payment_request").pGrid$getModifiedRecord());

// var rowKeys = $("#grid_payment_request").getDataIDs();
	
// 	for ( i = 0; i < (rowKeys.length * 2); i++) {
// 	if ($("#grid_payment_request").pGrid$getModifiedRecord()[i]["version"] == "2"  &&  $("#grid_payment_request").pGrid$getModifiedRecord()[i]["print_indicator"] == "1")
// 	{
	
// 	console.log($("#grid_payment_request").pGrid$getModifiedRecord()[i]["payment_form_no"]);
	
// 	}
// 	//item_no_string = item_no_string.concat($("#grid_item").pGrid$getRecord()[i+1]["item_no"],",")
// 	}




</script>