<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="acf" uri="/acf/tld/acf-taglib" %> 

<core:set var="isDialog" value="${ not empty param.type ? true : false }" />

<script>

	function consumptionSearch() {
			console.log("consumption search");

			var others_param = $("#frm_search").pSearch$getCriteria();
			var photo_param = $("#frm_search").pSearch$getCriteria();
			
			$("#grid_labour_consumption").pGrid$addCriteria(others_param);
			$("#grid_labour_consumption").pGrid$loadRecord();
			
			$("#grid_photo_labour").pGrid$addCriteria(photo_param);
			$("#grid_photo_labour").pGrid$loadRecord();
			
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
		<acf:RegionAction>
			<a href="#" onClick="$(this).parents('.widget-box').pForm$clear();">Clear</a>
		</acf:RegionAction>
		
 		<form id="frm_search" class="form-horizontal" data-role="search" >
	    	<div class="form-group">

 	      		<div class="col-md-2">
	      			<label for=s_labour_type style="display:block">Labour Type</label>
	      			<acf:ComboBox id="s_labour_type" name="labour_type" editable="true" multiple="false">
	      				<acf:Bind on="initData"><script>$(this).acfComboBox("init",${labourselect});</script></acf:Bind>
	      			</acf:ComboBox>
	        	</div>	  
	        	
	      		<div class="col-md-2">
	      			<label for=s_section_id style="display:block">Others Section Id.</label>
	      			<acf:ComboBox id="s_section_id" name="section_id" editable="true" maxlength = "2" multiple="true">
	      			<acf:Bind on="initData"><script>
		 					$(this).acfComboBox("init", ${sectionselect} );
			   			</script></acf:Bind></acf:ComboBox>
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


	    	</div>
	    	
		</form>
		
		
	</acf:Region>

</div>


		
<acf:Region id="labour_consumption_headers" title="LABOUR CONSUMPTION ENQUIRY" type="list">
	<div class="col-xs-12">
	<div class="tabbable">

	<ul class="nav nav-tabs" id="catTab">
		<li class="active"><a data-toggle="tab" href="#tab_others">Others</a></li>
		<li><a data-toggle="tab" href="#tab_photo">Photography</a></li>
	</ul>

	<div class="tab-content">
		<div id="tab_others" class="tab-pane fade in active">
		<div class="col-md-12 float-none">			
			<acf:Grid id="grid_labour_consumption" url="apff106-get-labour-consumption.ajax" readonly="true" autoLoad="false"> 
				<acf:Column type="date" name="process_date" caption="Date" width="50"></acf:Column>
				<acf:Column name="job_description" caption="Job Description" width="100"></acf:Column>
				<acf:Column name="programme_no" caption="Prog. No." width="50"></acf:Column>	
				<acf:Column name="programme_name" caption="Prog. Name" width="100"></acf:Column>	
				<acf:Column name="from_episode_no" caption="Epi# From" width="50"></acf:Column>
				<acf:Column name="to_episode_no" caption="Epi# To" width="50"></acf:Column>
				<acf:Column name="labour_type" caption="Lab. Type" width="70"></acf:Column>	
				<acf:Column name="total_hours" caption="No. of hour" width="30" type="number" align="right" useNumberFormat="true" decimalPlaces="2"></acf:Column>
				<acf:Column name="hourly_rate" caption="Hourly Rate($)" width="50" type="number" align="right" useNumberFormat="true" decimalPlaces="2"></acf:Column>
				<acf:Column name="section_name" caption="Section Name" width="70"></acf:Column>
				<acf:Column name="labour_cost" caption="Labour Cost($)" width="70" type="number" align="right" useNumberFormat="true" decimalPlaces="2"></acf:Column>	

				<acf:Column name="section_id" caption="" hidden = "true"></acf:Column>
				
				<acf:Bind on="selectRow"><script>
						record = $("#grid_labour_consumption").pGrid$getSelectedRecord();
						result = record;
						Dialog.get().setResult(result);
				</script></acf:Bind>
				<acf:Bind on="init"><script>
						Dialog.get().resize();
						$(window).trigger('resize');
				</script></acf:Bind>			
			</acf:Grid>
			<div class="col-xs-12" style="height:20px"></div>
			<div class="col-md-9"></div>	
     		<label class="control-label col-md-1" for="ttl_others">Total Amount</label>
			<div class="col-md-2">
			<acf:TextBox id="ttl_others" name="ttl_others"></acf:TextBox></div>	
			
		</div>
		</div>

		<div id="tab_photo" class="tab-pane fade">
			<div class="col-md-12 float-none">		
			<acf:Grid id="grid_photo_labour" url="apff106-get-photo-labour.ajax" rowNum="5" readonly="true" autoLoad="false">
				<acf:Column type="date" name="input_date" caption="Date" width="50"></acf:Column>
				<acf:Column type="job_no" name="job_no" caption="Job No." width="50"></acf:Column>
				<acf:Column name="job_description" caption="Job Description" width="100"></acf:Column>
				<acf:Column name="programme_no" caption="Prog. No." width="50"></acf:Column>	
				<acf:Column name="programme_name" caption="Prog. Name" width="100"></acf:Column>	
				<acf:Column name="from_episode_no" caption="Epi# From" width="50"></acf:Column>
				<acf:Column name="to_episode_no" caption="Epi# To" width="50"></acf:Column>
				<acf:Column name="labour_type" caption="Lab. Type" width="70"></acf:Column>	
				<acf:Column name="total_hours" caption="No. of hour" width="30" type="number" align="right" useNumberFormat="true" decimalPlaces="2"></acf:Column>
				<acf:Column name="hourly_rate" caption="Hourly Rate($)" width="50" type="number" align="right" useNumberFormat="true" decimalPlaces="2"></acf:Column>
				<acf:Column name="photo_labour_cost" caption="Labour Cost($)" width="70" type="number" align="right" useNumberFormat="true" decimalPlaces="2"></acf:Column>								
			</acf:Grid>
			<div class="col-xs-12" style="height:20px"></div>
			<div class="col-md-9"></div>	
     		<label class="control-label col-md-1" for="ttl_photo">Total Amount</label>
			<div class="col-md-2">
			<acf:TextBox id="ttl_photo" name="ttl_photo"></acf:TextBox></div>	
			
		</div>
		</div>			

	</div>
	</div>	        	
   	</div>  
   	    		        	
</acf:Region>    	

<form id="frm_main" class="form-horizontal" data-role="form" >   	
	<div class="col-xs-12" style="height:20px"></div>
			
		<div class="col-xs-12 form-padding oneline">
       	<div class="hidden">  
      			<acf:TextBox id="total_others" name="total_others"></acf:TextBox>
      			<acf:TextBox id="total_photo" name="total_photo"></acf:TextBox>
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
	searchForm:$("#frm_search"),
	searchKey : "section_id,process_date,programme_no,labour_type,select_fr_date,select_to_date",
	browseGrid: $("#grid_labour_consumption"),
	browseKey: "section_id,process_date,programme_no,labour_type,from_episode_no,to_episode_no,select_fr_date,select_to_date", 
    redirect: "/",
	initMode: "none",
	recordForm: $("#frm_main"),
	recordKey: {
	    total_others: "$(total_others)",
		total_photo: "$(total_photo)",
	},
	getUrl:  "apff106-get-form.ajax",
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
		
		if($(e.target).text() == "Others") {	
			result = $("#grid_labour_consumption").pGrid$getSelectedRecord();	
			$("#ttl_others").setValue($("#frm_main #total_others").getValue());
		}
		else {if ($(e.target).text() == "Photography") {
			result = $("#grid_photo_labour").pGrid$getSelectedRecord();
			$("#ttl_photo").setValue($("#frm_main #total_photo").getValue());
		} 
		}
		
		Dialog.get().setResult(result);
		$(window).trigger('resize');
});

$("#grid_photo_labour").on("afterLoadRecord", function(){
	$("#ttl_photo").setValue($("#frm_main #total_photo").getValue());
});
$("#grid_labour_consumption").on("afterLoadRecord", function(){
	$("#ttl_others").setValue($("#frm_main #total_others").getValue());
	
});
</script>