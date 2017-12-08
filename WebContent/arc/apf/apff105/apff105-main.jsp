<%@ page import="acf.acf.General.jsp.*"%>
<%@ page import="java.util.*" %>
<%@ taglib prefix="acf" uri="/acf/tld/acf-taglib" %> 
<%
	ACFgJspHelper helper = new ACFgJspHelper(session);
	helper.setConnection("ARCDB");
%>

<script>
	var programme_no = '${programme_no}';

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

<div class="col-xs-12 nopadding">
	<acf:Region id="reg_func_search" type="search" title="MATERIAL COST ENQUIRY SEARCH">
		<acf:RegionAction>
			<a href="#" onClick="$(this).parents('.widget-box').pForm$clear();">Clear</a>
		</acf:RegionAction>
		<form id="frm_search" class="form-horizontal" data-role="search" >
	    	<div class="form-group">
				
				
				<div class="col-md-1">
	      			<label for=s_fr_form style="display:block">PAT/AT No. From</label>
	      			<acf:TextBox id="s_fr_form_no" name="fr_form_no" editable="true" maxlength = "11" forceCase="upper"></acf:TextBox>    			
	        	</div>

	      		<div class="col-md-1">
	      			<label for=s_to_form style="display:block">PAT/AT No. To</label>
	      			<acf:TextBox id="s_to_form_no" name="to_form_no" editable="true" maxlength = "11" forceCase="upper"></acf:TextBox>	      			
	        	</div>

				<div class="col-md-2">
	      			<label for=s_programme_no style="display:block">Prog. No.</label>
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
	      		
	      		<div class="col-lg-2 col-md-2 col-sm-2 col-xs-2">
	      			<label for=supplier_code style="display:block">SUPPLIER CODE</label>
	      			<acf:ComboBox id="s_supplier_code" name="supplier_code">
	      			<acf:Bind on="initData"><script>
	 					$(this).acfComboBox("init", ${modules} );
	 				</script></acf:Bind>
	      			</acf:ComboBox>
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

	      		
	     <!--	<div class="col-lg-2 col-md-2 col-sm-2 col-xs-2">
	      			<label for=s_section_id style="display:block">SECTION NO.</label>
	      			<acf:ComboBox id="s_section_id" name="section_id" editable="true" multiple="false">
	      	  		<acf:Bind on="initData"><script>
	 						$(this).acfComboBox("init", ${SectionNo} );
	 				</script></acf:Bind> 
	      			</acf:ComboBox>
	        	</div>
	        	 
	        	
	   		  	<div class="col-lg-2 col-md-2 col-sm-2 col-xs-2">
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
	        
	      		
	        
	      		<div class="clearfix"></div>
	      		<div class="col-lg-3 col-md-3 col-sm-4 col-xs-4">
	      			<label for=s_po_date_from style="display:block">START DATE </label>
	      			<acf:DateTimePicker id="s_request_date_from" name="request_date_from" pickDate="true" pickTime="false" displayformat="YYYY/MM/DD"/>
	      		</div>
	      		<div class="col-lg-3 col-md-3 col-sm-4 col-xs-4">
	      			<label for=s_po_date_to style="display:block">END DATE </label>
	      			<acf:DateTimePicker id="s_request_date_to" name="request_date_to" pickDate="true" pickTime="false" displayformat="YYYY/MM/DD"/>
	      		</div>
	
	     -->
	       		
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
			<acf:Grid id="grid_browse" url="apff105-search.ajax" readonly="true" autoLoad="false">
				
				<acf:Column name="payment_form_no" caption="PAT/AT No." width="100"></acf:Column>
				<acf:Column name="sequence_no" caption="sequence No." width="100" hidden="true"></acf:Column>
				<acf:Column name="request_date" caption="Request Date" width="100" type="date"></acf:Column>
			
				<acf:Column name="supplier_code" caption="Supplier Code" width="60"></acf:Column>
				<acf:Column name="supplier_name" caption="Supplier Name" width="200"></acf:Column>
				<acf:Column name="invoice_no" caption="Invoice No." width="80"></acf:Column>
				<acf:Column name="despatch_date" caption="Despatch date" width="100"  type="date"></acf:Column>
			   	<acf:Column name="programme_no"  caption="Prog. No." width="100"></acf:Column> 
    		  	<acf:Column name="programme_name"  caption="Prog. Name" width="100"></acf:Column> 
    		  	<acf:Column name="from_episode_no"  caption="Epi # From" width="100"></acf:Column> 
    		  	<acf:Column name="to_episode_no"  caption="Epi # To" width="100"></acf:Column> 
    			<acf:Column name="particulars"   caption="Particulars" width="150"></acf:Column>
	 			<acf:Column name="section_name" caption="Section Name" width="200"></acf:Column>
				<acf:Column name="payment_amount" caption="Material Cost ($)" width="200"  align="right" useNumberFormat="true"></acf:Column>
	
	       </acf:Grid>

	    </div>
	    
	    <div class="col-md-12 form-padding oneline">
	    	<div class="col-md-9"> </div>	
      		
     		<label class="control-label col-md-1" for="total_po_amount">Total: </label>
      		<div class="col-md-2">
      			<acf:TextBox id="total_po_amount" name="total_po_amount" readonly="true" width="200"  align= "right" useNumberFormat="true"></acf:TextBox>
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
	browseKey: "payment_form_no, sequence_no",
	//redirect: "acff012-form",
	
	//initMode: "${mode}",
	recordForm: $("#frm_dummy"),  // avoid to clear up the value of total amount by using a dummy form id.
	recordKey: {
		section_id: "${section_id}",
		sub_section_id: "${sub_section_id}",
		group_no: "${group_no}",
	},
	getUrl: "apff105-get-form.ajax",
	saveUrl: "apff105-save.ajax",
	ready: function() { Action.setMode("search"); },
		
	onLoadSuccess: function(data)
	{
	console.log("testing---------------------1");
	
	// $("#total_po_amount").setValue($("#grid_browse").pGrid$getSumOfColumn("po_amount"));
	console.log("testing---------------------2");
	},
	
	
}).executeSearchBrowserForm();

$("#grid_browse").on("afterLoadRecord", function(data){
// $("#total_po_amount").setValue($("#grid_browse").pGrid$getTotalOfColumn("payment_amount"));

var s =$("#grid_browse").pGrid$getTotalOfColumn("payment_amount") ;
s = Math.round(s*100)/100;
$("#total_po_amount").setValue(s);
});

</script>
<%
	helper.close();
%>