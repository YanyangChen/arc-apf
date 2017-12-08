<%@ page import="acf.acf.General.jsp.*"%>
<%@ page import="java.util.*"%>
<%@ taglib prefix="acf" uri="/acf/tld/acf-taglib"%>

<script>
function transpgm(sender, e){
		var prog_head = $.trim(programme_no).substr(0,3);
		if (prog_head != '999'){
		 	$("#frm_main #from_programme_no").setError(ACF.getQtipHint('APF009V'), "APF009V");
    	}else  $("#frm_main #from_programme_no").setError("", "APF009V");
		
		if (programme_no == ""){
   							// no transfer from programme no	
   						}
   						
   						else {					
	   						$.ajax({
								headers: {
									'Accept'       : 'application/json',
									'Content-Type' : 'application/json; charset=utf-8'
								},
								async  : false,
								type   : "POST",
								url    : "${ctx}/arc/apf/apff012/apff012-transfer-to-pgm.ajax",
								data   : JSON.stringify({
									'programme_no'	: programme_no,
								}),
								success: function(data) {
									if (data.programme_fields != null) {
										$("#frm_main #from_programme_no").setError("", "APF008V");
									
									    $("#frm_main #programme_name").setValue(data.programme_fields[0].prog_name_n);
										$("#frm_main #chinese_programme_name").setValue(data.programme_fields[0].chin_prog_name_n);
										
										if (data.programme_fields[0].transfer_status_n == 'A'){
	    									$("#frm_main #transfer_status").setValue("ACTIVE")}
	    								else {
	    									if (data.programme_fields[0].transfer_status_n == 'I')
	    										$("#frm_main #transfer_status").setValue("INACTIVE")
	    									else {
	    										if (data.programme_fields[0].transfer_status_n == 'P')
	    											$("#frm_main #transfer_status").setValue("PENDING")}};
	    											
										if(data.programme_fields[0].transfer_status_n != 'A'){
			   								$("#frm_main #from_programme_no").setError(ACF.getQtipHint('APF008V'), "APF008V");
										}	    											
									}
									else {
										// programme master record not found
									}
								}
							});
   						}
	}
	
	function newtranspgm(sender, e){
		var prog_head = $.trim(programme_n).substr(0,3);
		if (prog_head == '999'){
		 	$("#frm_main #to_programme_no").setError(ACF.getQtipHint('APF006V'), "APF006V");
    	}else  $("#frm_main #to_programme_no").setError("", "APF006V");
	
		if (programme_n == ""){
    							// no transfer to programme no
   						}
   						
   						else {					
	   						$.ajax({
								headers: {
									'Accept'       : 'application/json',
									'Content-Type' : 'application/json; charset=utf-8'
								},
								async  : false,
								type   : "POST",
								url    : "${ctx}/arc/apf/apff012/apff012-new-transfer-to-pgm.ajax",
								data   : JSON.stringify({
									'programme_n'	: programme_n,
								}),
								success: function(data) {
									if (data.programme_fields != null) {
										$("#frm_main #to_programme_no").setError("", "APF008V");
										$("#frm_main #to_programme_no").setError("", "APF007V");
										
										$("#frm_main #prog_name_n").setValue(data.programme_fields[0].prog_name_n);									
										$("#frm_main #chin_prog_name_n").setValue(data.programme_fields[0].chin_prog_name_n);
										
										if (data.programme_fields[0].transfer_status_n == 'A'){
	    									$("#frm_main #transfer_status_n").setValue("ACTIVE")}
	    								else {
	    									if (data.programme_fields[0].transfer_status_n == 'I')
	    										$("#frm_main #transfer_status_n").setValue("INACTIVE")
	    									else {
	    										if (data.programme_fields[0].transfer_status_n == 'P')
	    											$("#frm_main #transfer_status_n").setValue("PENDING")}};
	    											
										if(data.programme_fields[0].transfer_status_n != 'A'){
			   								$("#frm_main #to_programme_no").setError(ACF.getQtipHint('APF008V'), "APF008V");
										}	  
										 											
										if(data.programme_fields[0].transfer_from_programme_n != ''){
			   								$("#frm_main #to_programme_no").setError(ACF.getQtipHint('APF007V'), "APF007V");
										}	    	  											
	    											
									
									}
									else {
										// programme master record not found
									}
								}
							});
   						}
	}
</script>

<!-- PAGE CONTENT BEGINS -->
<div class="col-md-12 nopadding">
	<acf:Region id="reg_div_list" title="TRANSFER SEARCH" type="search">
		<acf:RegionAction>
			<a href="#" onClick="$(this).parents('.widget-box').pForm$clear();">Clear</a>
		</acf:RegionAction>
		<form id="frm_search" class="form-horizontal" data-role="search">
			<div class="form-group">
			
			<div class="col-lg-2 col-md-6 col-sm-6 col-xs-12">
					<label for=s_transfer_from_date style="display: block">Transfer
						From Date</label>
					<acf:DateTimePicker id="s_transfer_from_date"
						name="transfer_from_date" pickTime="false" />
				</div>
				<div class="col-lg-2 col-md-6 col-sm-6 col-xs-12">
					<label for=s_transfer_to_date style="display: block">Transfer
						To Date</label>
					<acf:DateTimePicker id="s_transfer_to_date" name="transfer_to_date"
						pickTime="false" />
				</div>
				
				<div class="col-lg-2 col-md-6 col-sm-6 col-xs-12">

						<label for=s_programme_no style="display: block">Programme No.</label>

						<acf:TextBox id="s_programme_no" name="s_programme_no" button="fa-search" maxlength="9" allowKey="[tT0-9]" forceCase="upper" format="(T[0-9]{8}|[0-9]{1,9})"> 
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
</div>	

	<form id="frm_main" class="form-horizontal" data-role="form">
	
		<acf:Region id="reg_div_list" type="list" title="TRANSFER HISTORY">

			<div class="col-xs-12">
				<acf:Grid id="grid_browse" url="apff012-search.ajax" readonly="true" autoLoad="false">
					<acf:Column name="pgftext" caption="Programme No." width="500"></acf:Column>
					<acf:Column name="pgttext" caption="Transfer To Programme" width="500"></acf:Column>
					<acf:Column name="transfer_date" caption="Transfer Date" type="date" width="100"></acf:Column>
					
					<acf:Column name="from_programme_no" caption="" width="100" hidden="true"></acf:Column>
					<acf:Column name="transfer_date" caption="" width="100" hidden="true"></acf:Column>
				</acf:Grid>
			</div>
		</acf:Region>

		<acf:Region id="reg_mod_main" title="TRASNFER MAINTENANCE">	
		
			<div class="col-xs-12 form-padding">
				<label class="control-label col-md-2" for="transfer_date">Transfer Date:</label>

				<div class="col-md-2">

					<acf:DateTimePicker id="transfer_date" name="transfer_date" maxlength="8" pickTime="false" checkMandatory="true">
					<acf:Bind on="change">
					<script>
					var today = new Date();
					var someDate = new Date();
					someDate.setDate(today.getDate() - 1); 
					$(this).setError("", "APF012V");
					
					if( $(this).getValue() < someDate ){
      				$(this).setError(ACF.getQtipHint("APF012V"), "APF012V");
     			   	}
					</script>
					</acf:Bind>
					</acf:DateTimePicker>
				</div>
			</div>		
		
			<div class="col-xs-12 form-padding">
				<label class="control-label col-md-2" for="from_programme_no">Programme No:</label>
		 		<div class="col-md-2">
					<acf:TextBox id="from_programme_no" name="from_programme_no" button="fa-search" maxlength="9" checkMandatory="true" allowKey="[tT0-9]" forceCase="upper" format="(T[0-9]{8}|[0-9]{1,9})"> 
	      				<acf:Bind target="button" on="click"><script>
	      					programme_no = null;
		   		    		Dialog.create()
								.setCaption("Search")
								.setWidth(1000)
								.addDismissButton("OK", function(){
									if ($.type(programme_no) == "string"){
										$("#frm_main #from_programme_no").setValue(programme_no);
										programme_no = "";
									}
								})
								.setResultCallback(function(result) {
									programme_no = result.programme_no;
								})
								.showUrl("../../apf/apff011/apff011-search-arc-prog");
	   					</script></acf:Bind>
	   					
						<acf:Bind on="change">
							<script>
   						     programme_no = $(this).getValue(); 
   						     transpgm(programme_no,e);

							</script>
						</acf:Bind>
	   					
	      			</acf:TextBox>
		 		</div>
		 		
		 		<label class="control-label col-md-2" for="transfer_status">Transfer Status:</label>
				<div class="col-md-2">

					<acf:TextBox id="transfer_status" name="transfer_status" checkMandatory="false" readonly="true"/>

				</div>
				
			</div>
			
			<div class="col-xs-12 form-padding">
				<label class="control-label col-md-2" for="programme_name">Prog. Name:</label>

				<div class="col-md-6">

					<acf:TextBox id="programme_name" name="programme_name" checkMandatory="false" maxlength="60" readonly="true"/>

				</div>
			</div>				
				
				
			<div class="col-xs-12 form-padding">
				<label class="control-label col-md-2" for="chinese_programme_name">Chin. Prog. Name:</label>

				<div class="col-md-4">

					<acf:TextBox id="chinese_programme_name" name="chinese_programme_name" checkMandatory="false" maxlength="60" readonly="true"/>
				</div>
			</div>


			<div class="col-xs-12 form-padding">
				<label class="control-label col-md-2" for="to_programme_no">Transfer to New Prog. No:</label>

				<div class="col-md-2">

					<acf:TextBox id="to_programme_no" name="to_programme_no" checkMandatory="true" maxlength="9" button="fa-search">
					
						
						<acf:Bind target="button" on="click"><script>
	      					programme_no = null;
		   		    		Dialog.create()
								.setCaption("Search")
								.setWidth(1000)
								.addDismissButton("OK", function(){
									if ($.type(programme_no) == "string"){
										$("#frm_main #to_programme_no").setValue(programme_no);
										programme_no = "";
									}
								})
								.setResultCallback(function(result) {
									programme_no = result.programme_no;
								})
								.showUrl("../../apf/apff011/apff011-search-arc-prog");
	   					</script></acf:Bind>
	   						   					
							<acf:Bind on="change">
							<script>
						programme_n = $(this).getValue(); //this bloack is the same as onLoadSucess except $this value
   						
   						newtranspgm(programme_n,e);
   						</script>
						</acf:Bind>
					</acf:TextBox>
				</div>

				<label class="control-label col-md-2" for="transfer_status">Transfer Status:</label>
				<div class="col-md-2">

					<acf:TextBox id="transfer_status_n" name="transfer_status_n" checkMandatory="false" readonly="true"/>
				</div>
				
			</div>
			
			<div class="col-xs-12 form-padding">			
				<label class="control-label col-md-2" for="prog_name_n">Prog. Name (New):</label>

				<div class="col-md-6">

					<acf:TextBox id="prog_name_n" name="prog_name_n" checkMandatory="false" maxlength="60" readonly="true"/>
				</div>
			</div>
			
			
			
			 <div class="col-xs-12 form-padding">
				<label class="control-label col-md-2" for="chin_prog_name_n">Chin. Prog. Name (New):</label>

				<div class="col-md-4">

					<acf:TextBox id="chin_prog_name_n" name="chin_prog_name_n" checkMandatory="false" maxlength="60" readonly="true"/>
				</div>
			</div> 
			
			
			
			<div class="col-xs-12 form-padding">
				<label class="control-label col-md-2" for="remarks">Remarks:</label>

				<div class="col-md-6">

					<acf:TextBox id="remarks" name="remarks" checkMandatory="false" maxlength="60" />
				</div>
			</div>

			
</acf:Region>

	


		<acf:Region id="reg_stat" title="UPDATE STATISTICS">
			<div class="col-xs-12 form-padding oneline">
				<label class="control-label col-md-2" for="modified_at">Modified
					At:</label>
				<div class="col-md-4">
					<acf:DateTime id="modified_at" name="modified_at" readonly="true"
						useSeconds="true" />
				</div>
				<label class="control-label col-md-2" for="created_at">Created
					At:</label>
				<div class="col-md-4">
					<acf:DateTime id="created_at" name="created_at" readonly="true"
						useSeconds="true" />
				</div>
			</div>
			<div class="col-xs-12 form-padding oneline">
				<label class="control-label col-md-2" for="modified_by">Modified
					By:</label>
				<div class="col-md-4">
					<acf:TextBox id="modified_by" name="modified_by" readonly="true" />
				</div>
				<label class="control-label col-md-2" for="created_by">Created
					By:</label>
				<div class="col-md-4">
					<acf:TextBox id="created_by" name="created_by" readonly="true" />
				</div>
			</div>
			<div class="hidden">
				<label class="control-label col-md-2" for="modified_at_n">Modified
					At:</label>
				<div class="col-md-4">
					<acf:DateTime id="modified_at_n" name="modified_at_n" readonly="true"
						useSeconds="true" />
				</div>
				<label class="control-label col-md-2" for="created_at_n">Created
					At:</label>
				<div class="col-md-4">
					<acf:DateTime id="created_at_n" name="created_at_n" readonly="true"
						useSeconds="true" />
				</div>
			</div>
			<div class="hidden">
				<label class="control-label col-md-2" for="modified_by_n">Modified
					By:</label>
				<div class="col-md-4">
					<acf:TextBox id="modified_by_n" name="modified_by_n" readonly="true" />
				</div>
				<label class="control-label col-md-2" for="created_by_n">Created
					By:</label>
				<div class="col-md-4">
					<acf:TextBox id="created_by_n" name="created_by_n" readonly="true" />
				</div>
			</div>
		</acf:Region>
		
</form>


<script>
	Controller.setOption({
		searchForm : $("#frm_search"),
		browseGrid : $("#grid_browse"),
		searchKey : "transfer_date,from_programme_no",
		browseKey : "transfer_date,from_programme_no",
		recordForm : $("#frm_main"),
		recordKey : {
		    transfer_date : "$(transfer_date)",
			from_programme_no  : "${from_programme_no}",
		},
		getUrl : "apff012-get-form.ajax",
		saveUrl : "apff012-save.ajax",
		
		getSaveData: function() {
			return JSON.stringify({
			'form' : Controller.opt.recordForm.pForm$getModifiedRecord( Action.getMode() ),
			'a_mode' : $("#frm_main #a_mode").getValue(),//'form.transfer_to_programme' : $("#frm_main #programme_n").getValue(),

		});

	},
	ready: function() { Action.setMode("search"); }
	}).executeSearchBrowserForm();
		
$(document).on("new", function() {
var currentDate = new Date();
currentDate.setHours(0);
$("#frm_main #transfer_date").setValue(currentDate);
$("#frm_main #transfer_date").setError("", "APF012V");
});

$(document).on('delete', function() {

	if ($("#frm_main #transfer_status").getValue() == 'INACTIVE') {
		Action.view();
		msg = "Transfer programme " + $("#frm_main #from_programme_no").getValue() + " already process. Deletion is not allowed.";
        Dialog.create($("#dialog1_arc"))
        .setCaption("MESSAGE")
        .addDismissButton("Close")
        .showHtml(msg);
	}  
	
});

</script>
