<%@ taglib prefix="acf" uri="/acf/tld/acf-taglib" %> 

<script>
	function selectDates_OnValidate(sender, e){
		var selectStart = $("#p_report_date_from");
		var selectEnd = $("#p_report_date_to");
		var startDate = moment(parseInt(selectStart.getValue()));
		var endDate = moment(parseInt(selectEnd.getValue()));
		if(startDate.isValid() && endDate.isValid()){
			if(startDate.diff(endDate, "days") > 0)
				$(sender).setError(ACF.getQtipHint('ACF069V'), "DateRange");
			else selectStart.add(selectEnd).setError(null, "DateRange");
		}else selectStart.add(selectEnd).setError(null, "DateRange");
	}
</script>	


<!-- PAGE CONTENT BEGINS -->
<form id="frm_main" class="form-horizontal" data-role="form" >
<div class="col-md-12 nopadding">
	<acf:Region id="reg_sect_main" title="PAYMENT REQUEST TRANSACTION CHECKING LIST (COSTUME)">
		   	
	   	<div class="col-xs-12 form-padding oneline">
        	<div class="col-md-4">      
      			<label class="control-label col-md-2" for="p_report_date_from">Start Date</label>
      			<acf:DateTimePicker id="p_report_date_from" name="p_report_date_from" pickDate="true" pickTime="false"> 
      			<acf:Bind on="validate"><script>selectDates_OnValidate(this, e);</script></acf:Bind></acf:DateTimePicker> 	     
        	</div>
        	
      		<div class="col-md-4">      
      			<label class="control-label col-md-2" for="p_report_date_to">End Date</label>
      			<acf:DateTimePicker id="p_report_date_to" name="p_report_date_to" pickDate="true" pickTime="false" checkMandatory="true">
      			<acf:Bind on="validate"><script>selectDates_OnValidate(this, e);</script></acf:Bind></acf:DateTimePicker> 	      	
        	</div>
        	
    	</div>
    		
    	    	
     </acf:Region>
</div>
</form>

<script>
Controller.setOption({
	form: 	  	 $("#frm_main"),
	reportID: 	 "APFF114",
	saveUrl:  	 "apff114-report.ajax",
	downloadUrl: "apff114-get-pdf",
}).executeReportBrowser();
</script>
