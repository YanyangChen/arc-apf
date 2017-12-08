<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="acf" uri="/acf/tld/acf-taglib" %> 

<core:set var="isDialog" value="${ not empty param.type ? true : false }" />

<script>
	var programme_no = '${programme_no}';

	function consumptionSearch() {
			console.log("consumption search");
			var item_param = $("#frm_search").pSearch$getCriteria();
			var other_param = $("#frm_search").pSearch$getCriteria();
			var labour_param = $("#frm_search").pSearch$getCriteria();
			
			$("#grid_other_consumption").pGrid$addCriteria(other_param);
			$("#grid_other_consumption").pGrid$loadRecord();
			
			$("#grid_labour_consumption").pGrid$addCriteria(labour_param);
			$("#grid_labour_consumption").pGrid$loadRecord();
			
			$("#grid_item_consumption").pGrid$addCriteria(item_param);
			$("#grid_item_consumption").pGrid$loadRecord();
			
	}

	function selectDates_OnValidate(sender, e){
		var selectStart = $("#s_select_fr_date");
		var selectEnd = $("#s_select_to_date");
		var startDate = moment(parseInt(selectStart.getValue()));
		var endDate = moment(parseInt(selectEnd.getValue()));
		if(startDate.isValid() && endDate.isValid()){
			if(startDate.diff(endDate, "days") > 0)
				$(sender).setError(ACF.getQtipHint('ACF069V'), "DateRange");
			else selectStart.add(selectEnd).setError(null, "DateRange");
		}else selectStart.add(selectEnd).setError(null, "DateRange");
	}
</script>	

<div class="col-md-12 nopadding">
	<acf:Region id="reg_search" title="SEARCH" type="search">

		<core:if test="${isDialog }">
			<div class="right" style="padding:25px 0 0;">
				<acf:Button id="consumption_search" title="Search" icon="fa-search">
					<acf:Bind on="click"><script>consumptionSearch();</script></acf:Bind>
				</acf:Button>
				<acf:Button id="consumption_clear" title="Clear" icon="fa-eraser">
					<acf:Bind on="click"><script>
						$('#frm_search').pSearch$clear();
						consumptionSearch();
					</script></acf:Bind>
				</acf:Button>
			</div>
		</core:if>
		
		
		<form id="frm_search" class="form-horizontal" data-role="search" >
	    	<div class="form-group">

	      		<div class="col-md-2">
	      			<label for=s_fr_form style="display:block">Consumption Form No.     From</label>
	      			<acf:TextBox id="s_fr_form" name="fr_form_no" editable="true" maxlength = "8"></acf:TextBox>    			
	        	</div>

	      		<div class="col-md-2">
	      			<label for=s_to_form style="display:block">To</label>
	      			<acf:TextBox id="s_to_form" name="to_form_no" editable="true" maxlength = "8"></acf:TextBox>	      			
	        	</div>


	       		<div class="col-md-2">
	      			<label for=s_select_fr_date style="display:block">Start Date</label>
	      			<acf:DateTimePicker id="s_select_fr_date" name="select_fr_date" pickTime="false">
	      				<acf:Bind on="validate"><script>selectDates_OnValidate(this, e);</script></acf:Bind></acf:DateTimePicker> 	      			
	        	</div>
	        	
	        	<div class="col-md-2">
	      			<label for=s_select_to_date style="display:block">End Date</label>
	      			<acf:DateTimePicker id="s_select_to_date" name="select_to_date" pickTime="false">
	      				<acf:Bind on="validate"><script>selectDates_OnValidate(this, e);</script></acf:Bind></acf:DateTimePicker> 	      			
	        	</div>

	      		<div class="col-md-2">
	      			<label for=s_programme_no style="display:block">Programme No.</label>
	      			<acf:TextBox id="s_programme_no" name="programme_no" button="fa-search" maxlength="9" allowKey="[tT0-9]" forceCase="upper" format="(T[0-9]{8}|[0-9]{1,9})"> 
	      				<acf:Bind target="button" on="click"><script>
	      					programme_no = null;
		   		    		Dialog.create()
								.setCaption("Search")
								.setWidth(1000)
								.addDismissButton("OK", function(){
									if ($.type(programme_no) == "string"){
										$("#frm_search #s_programme_no").setValue(programme_no);
										programme_no = "";
									}
								})
								.setResultCallback(function(result) {
									programme_no = result.programme_no;
								})
								.showUrl("../../apf/apff011/apff011-search-arc-prog");
	   					</script></acf:Bind>
	      			</acf:TextBox>
	      		</div>

	    	</div>
	    	
		</form>
		
		
	</acf:Region>



	
<acf:Region id="consumption_headers" title="COSTS ENQUIRY BY CONSUMPTION FORM" type="list">
	<div class="col-xs-12">
	<div class="tabbable">

	<ul class="nav nav-tabs" id="catTab">
		<li class="active"><a data-toggle="tab" href="#tab_item">Item Material</a></li>
		<li><a data-toggle="tab" href="#tab_other_material">Other Material</a></li>
		<li><a data-toggle="tab" href="#tab_labour">Labour</a></li>
	</ul>

	<div class="tab-content">
	
		<div id="tab_item" class="tab-pane fade in active">
		<div class="col-md-12 float-none">			
			<acf:Grid id="grid_item_consumption" url="apff104-get-item-consumption.ajax" rowNum="15" readonly="true" autoLoad="false">
				<acf:Column name="consumption_form_no" caption="Consumption Form No." width="80" editable="false"></acf:Column>
				<acf:Column name="construction_no" caption="Construction No." width="80" editable="false"></acf:Column>
				<acf:Column type="date" name="completion_date" caption="Completion Date" width="50"></acf:Column>
				<acf:Column name="programme_no" caption="Programme No." width="50" editable="false"></acf:Column>
				<acf:Column name="programme_name" caption="Programme Name" width="120" editable="false" ></acf:Column>			
				<acf:Column name="from_episode_no" caption="EPI No.from" width="50" editable="false"></acf:Column>
				<acf:Column name="to_episode_no" caption="EPI No. to" width="50" editable="false"></acf:Column>
				<acf:Column name="account_allocation" caption="A/C Alloc." width="30" editable="false"></acf:Column>
				<acf:Column name="item_no" caption="Item No." width="50" editable="false"></acf:Column>
				<acf:Column name="consumption_quantity" caption="Qty." width="30" editable="false"></acf:Column>												
				<acf:Column name="item_cost" caption="Material Cost ($)" width="70" editable="false" type="number" align="right" useNumberFormat="true" decimalPlaces="2"></acf:Column>	
				<acf:Column name="unit_cost" caption="" hidden="true"></acf:Column>
				<acf:Column name="purchase_order_no" caption="" hidden = "true"></acf:Column>
					<acf:Bind on="selectRow"><script>
						record = $("#grid_item_consumption").pGrid$getSelectedRecord();
						result = record;
						Dialog.get().setResult(result);
					</script></acf:Bind>
					<acf:Bind on="init"><script>
						Dialog.get().resize();
						$(window).trigger('resize');
					</script></acf:Bind>    
			</acf:Grid>
			<div class="col-xs-12" style="height:20px"></div>
			<div class="col-md-9">
      		</div>	
     		<label class="control-label col-md-1" for="ttl_item">Total Amount</label>
      		<div class="col-md-2">
      			<acf:TextBox id="ttl_item" name="ttl_item" readonly="true" align="left" useNumberFormat="true" decimalPlaces="2"  useDefValIfEmpty="true" DefVal="0">
      			</acf:TextBox>
        	</div>	  
		</div>
		</div>
		
		<div id="tab_other_material" class="tab-pane fade">
			<div class="col-md-12 float-none">		
			<acf:Grid id="grid_other_consumption" url="apff104-get-other-consumption.ajax" rowNum="15" readonly="true" autoLoad="false">
				<acf:Column name="consumption_form_no" caption="Consumption Form No." width="80" editable="false"></acf:Column>
				<acf:Column name="construction_no" caption="Construction No." width="80" editable="false"></acf:Column>
				<acf:Column type="date" name="completion_date" caption="Completion Date" width="50"></acf:Column>
				<acf:Column name="programme_no" caption="Programme No." width="50" editable="false"></acf:Column>
				<acf:Column name="programme_name" caption="Programme Name" width="120" editable="false" ></acf:Column>			
				<acf:Column name="from_episode_no" caption="EPI No.from" width="50" editable="false"></acf:Column>
				<acf:Column name="to_episode_no" caption="EPI No. to" width="50" editable="false"></acf:Column>
				<acf:Column name="account_allocation" caption="A/C Alloc." width="30" editable="false"></acf:Column>
				<acf:Column name="other_material_description" caption="Particular for Other Material Cost" width="100" editable="false"></acf:Column>											
				<acf:Column name="other_material_amount" caption="Other Material Cost ($)" width="70" editable="false" type="number" align="right" useNumberFormat="true" decimalPlaces="2"></acf:Column>							
				<acf:Column name="sequence_no" caption="" hidden = "true"></acf:Column>
			</acf:Grid>
			<div class="col-xs-12" style="height:20px"></div>
			<div class="col-md-9">
      		</div>	
     		<label class="control-label col-md-1" for="ttl_other">Total Amount</label>
      		<div class="col-md-2">
      			<acf:TextBox id="ttl_other" name="ttl_other" readonly="true" align="left" useNumberFormat="true" decimalPlaces="2"  useDefValIfEmpty="true" DefVal="0">
      			</acf:TextBox>
        	</div>	  
			</div>
		</div>


		<div id="tab_labour" class="tab-pane fade">
			<div class="col-md-12 float-none">					
			<acf:Grid id="grid_labour_consumption" url="apff104-get-labour-consumption.ajax" rowNum="15" readonly="true" autoLoad="false">
				<acf:Column name="consumption_form_no" caption="Consumption Form No." width="80" editable="false"></acf:Column>
				<acf:Column name="construction_no" caption="Construction No." width="80" editable="false"></acf:Column>
				<acf:Column type="date" name="completion_date" caption="Completion Date" width="50"></acf:Column>
				<acf:Column name="programme_no" caption="Programme No." width="50" editable="false"></acf:Column>
				<acf:Column name="programme_name" caption="Programme Name" width="120" editable="false" ></acf:Column>			
				<acf:Column name="from_episode_no" caption="EPI No.from" width="50" editable="false"></acf:Column>
				<acf:Column name="to_episode_no" caption="EPI No. to" width="50" editable="false"></acf:Column>
				<acf:Column name="labour_type" caption="Lab. Type" width="30" editable="false"></acf:Column>
				<acf:Column name="no_of_hours" caption="No. of hour" width="50" editable="false" type="number" align="right" useNumberFormat="true" decimalPlaces="2"></acf:Column>
				<acf:Column name="hourly_rate" caption="Hourly Rate ($)" width="40" editable="false" type="number" align="right" useNumberFormat="true" decimalPlaces="2"></acf:Column>												
				<acf:Column name="labour_cost" caption="Labour Cost ($)" width="60" editable="false" type="number" align="right" useNumberFormat="true" decimalPlaces="2"></acf:Column>	
			</acf:Grid>
			<div class="col-xs-12" style="height:20px"></div>
			<div class="col-md-9">
      		</div>	
     		<label class="control-label col-md-1" for="ttl_labour">Total Amount</label>
      		<div class="col-md-2">
      			<acf:TextBox id="ttl_labour" name="ttl_labour" readonly="true" align="left" useNumberFormat="true" decimalPlaces="2"  useDefValIfEmpty="true" DefVal="0">
      			</acf:TextBox>
        	</div>	  
			</div>
		</div>			

	</div>
	</div>	        	
   	</div>  
   	    		        	
</acf:Region>    	
</div>

<form id="frm_main" class="form-horizontal" data-role="form" >   	
	<div class="col-xs-12" style="height:20px"></div>
			
		<div class="col-xs-12 form-padding oneline">
        	<div class="hidden">  
      			<acf:TextBox id="total_item" name="total_item"></acf:TextBox>
      			<acf:TextBox id="total_other" name="total_other"></acf:TextBox>
      			<acf:TextBox id="total_labour" name="total_labour"></acf:TextBox>
        	</div>	    	  

    	</div> 
 
</form>

<core:choose>
	<core:when test="${isDialog }">
		<script>consumptionSearch();</script>
	</core:when>
	<core:otherwise>
		<script>

Controller.setOption({

	searchForm: $("#frm_search"),
	browseGrid: $("#grid_item_consumption"),
	browseKey: "consumption_form_no,programme_no,fr_form_no,to_form_no,select_fr_date,select_to_date", 
	redirect: "/",
	recordForm: $("#frm_main"),  
	recordKey: {
		total_item: "${total_item}",
		total_other: "${total_other}",
		total_labour: "${total_labour}",				
	},
	getUrl: "apff104-get-form.ajax",
	saveUrl: "none",
	
	onSearch: function(){
		consumptionSearch();
	},
	ready: function() { Action.setMode("search"); }	
}).executeSearchBrowserForm();
		</script>
	</core:otherwise>
</core:choose>

<script>
$('#catTab a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
		$(window).trigger("resize");
		console.log("tab aa");
		
		if($(e.target).text() == "Item Material") {	
			result = $("#grid_item_consumption").pGrid$getSelectedRecord();	
			$("#ttl_item").setValue($("#frm_main #total_item").getValue());
		}
		else {if ($(e.target).text() == "Other Material") {
			result = $("#grid_other_consumption").pGrid$getSelectedRecord();
			$("#ttl_other").setValue($("#frm_main #total_other").getValue());
		} else if ($(e.target).text() == "Labour") {
			result = $("#grid_labour_consumption").pGrid$getSelectedRecord();
			$("#ttl_labour").setValue($("#frm_main #total_labour").getValue());}	
		}
		
		Dialog.get().setResult(result);
		$(window).trigger('resize');
});

$("#grid_labour_consumption").on("afterLoadRecord", function(){
	calculateLabourAmt();
	$("#ttl_labour").setValue($("#frm_main #total_labour").getValue());
});
$("#grid_other_consumption").on("afterLoadRecord", function(){
	$("#ttl_other").setValue($("#frm_main #total_other").getValue());
});
$("#grid_item_consumption").on("afterLoadRecord", function(){
	calculateAmount();	
	$("#ttl_item").setValue($("#frm_main #total_item").getValue());
});

function calculateAmount(){
	$.each($("#grid_item_consumption").pGrid$getRecord(), function(id, rec){
		console.log(id + " ~ " + rec.unit_cost + " "  + rec.item_no);
		if(rec.unit_cost) {
			$("#grid_item_consumption").setRowData(id, {
				item_cost: rec.unit_cost * rec.consumption_quantity
			});
		}
	});
}

function calculateLabourAmt(){
	$.each($("#grid_labour_consumption").pGrid$getRecord(), function(id, rec){
		console.log(id + " ~ " + rec.hourly_rate + " " + rec.no_of_hours);
		if(rec.hourly_rate) {
			$("#grid_labour_consumption").setRowData(id, {
				labour_cost: rec.hourly_rate * rec.no_of_hours
			});
		}
	});
}
</script>