<%@ taglib prefix="acf" uri="/acf/tld/acf-taglib" %> 

<!-- PAGE CONTENT BEGINS -->
<form id="frm_main" class="form-horizontal" data-role="form" >
<div class="col-md-12 nopadding">
	<acf:Region id="reg_sect_main" title="PHOTOGRAPHY : MATERIAL & LABOUR CONSUMPTION TRANSACTION CHECKING LIST IN PDF">

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
    	
     </acf:Region>
</div>
</form>

<script>
Controller.setOption({
	form: 	  	 $("#frm_main"),
	reportID: 	 "APFF202",
	saveUrl:  	 "apff202-report.ajax",
	downloadUrl: "apff202-get-pdf",
	
	getSaveData: function(){
		var p_period_from = $("#frm_main #p_modified_date_fr").getValue();
		var p_period_to = $("#frm_main #p_modified_date_to").getValue();
		
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
		return JSON.stringify({
			'p_modified_date_fr' : p_period_from,
			'p_modified_date_to' : p_period_to,
		});
	}
	
}).executeReportBrowser();
</script>