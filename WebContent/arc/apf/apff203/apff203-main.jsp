<%@ taglib prefix="acf" uri="/acf/tld/acf-taglib" %> 

<!-- PAGE CONTENT BEGINS -->
<form id="frm_main" class="form-horizontal" data-role="form" >
<div class="col-md-12 nopadding">
	<acf:Region id="reg_sect_main" title="COSTUME : PAYMENT REQUEST TRANSACTION CHECKING LIST IN PDF">

    	<div class="col-xs-12 form-padding oneline">
        	<label class="control-label col-md-2" for="p_modified_date_fr">Modified Date From</label>
      		<div class="col-md-4">      
      			<acf:DateTimePicker id="p_modified_date_fr" name="p_modified_date_fr" pickDate="true" pickTime="false" checkMandatory="true"/>
        	</div>
    	</div>  	
    	<div class="col-xs-12 form-padding oneline">
        	<label class="control-label col-md-2" for="p_modified_date_to">Modified Date To</label>
      		<div class="col-md-4">      
      			<acf:DateTimePicker id="p_modified_date_to" name="p_modified_date_to" pickDate="true" pickTime="false" checkMandatory="false" useDefValIfEmpty="true" defVal="2099-12-31" value="" />
        	</div>
    	</div>
    	 
    	<div class="col-xs-12 form-padding oneline">
    		<label class="control-label col-md-2" for="p_modified_by">Modified By</label>
    		<div class="col-md-4">      
      			<acf:TextBox id="p_modified_by" name="p_modified_by" maxlength="8" forceCase="upper" />
        	</div>
	      	  
	    </div>
	     
	    <div class="col-xs-12 form-padding oneline">
    	
	    	<label class="control-label col-md-2" for="p_payment_form_no_fr">PAT/AT No. From</label>
    		<div class="col-md-4">      
      			<acf:TextBox id="p_payment_form_no_fr" name="p_payment_form_no_fr" maxlength="11" forceCase="upper" />
        	</div>
	    </div>

 		<div class="col-xs-12 form-padding oneline">
    	
	    	<label class="control-label col-md-2" for="p_payment_form_no_to">PAT/AT No. To</label>
    		<div class="col-md-4">      
      			<acf:TextBox id="p_payment_form_no_to" name="p_payment_form_no_to" maxlength="11" forceCase="upper" />
        	</div>
	    </div>
	     
     </acf:Region>
</div>
</form>

<script>
Controller.setOption({
	form: 	  	 $("#frm_main"),
	reportID: 	 "APFF203",
	saveUrl:  	 "apff203-report.ajax",
	downloadUrl: "apff203-get-pdf",
	
	getSaveData: function(){
		var p_period_from = $("#frm_main #p_modified_date_fr").getValue();
		var p_period_to = $("#frm_main #p_modified_date_to").getValue();
		var p_modified_by  = $("#frm_main #p_modified_by").getValue();
		var p_form_no_from = $("#frm_main #p_payment_form_no_fr").getValue();
		var p_form_no_to = $("#frm_main #p_payment_form_no_to").getValue();
		
		
		
		
		if (p_period_from  != "") {	
			p_period_from = moment(p_period_from, 'x').format("YYYY-MM-DD");
			p_period_from += "T00:00:00";
		} else {
	    	p_period_from = "NA";
		}
	
		if (p_period_to  != "") {
			p_period_to = moment(p_period_to, 'x').format("YYYY-MM-DD");
			p_period_to += "T15:59:59";
		} else {
			p_period_to += "2099-12-31T15:59:59";
		
		}
		
				
		if (p_form_no_from != "") {
			;
		} else {
			p_form_no_from = " ";
		}
		
		if (p_form_no_to != "") {
			;
		} else {
			p_form_no_to = "PAT9999999";
		}
		
		return JSON.stringify({
			'p_modified_date_fr' : p_period_from,
			'p_modified_date_to' : p_period_to,
		    'p_modified_by'      : p_modified_by,
		    'p_payment_form_no_fr' : p_form_no_from,
			'p_payment_form_no_to' : p_form_no_to,
		    
		});
	},
	
	ready: function(){
		$("#p_modified_by").setValue("${current_user}");
	},
	
}).executeReportBrowser();
</script>