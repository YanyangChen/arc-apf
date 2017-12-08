<%@ page import="acf.acf.General.jsp.*"%>
<%@ page import="java.util.*"%>
<%@ taglib prefix="acf" uri="/acf/tld/acf-taglib"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- PAGE CONTENT BEGINS -->



<script>
var sltedv;		
							
var subsectionlist = [
{"action":2,"version":1,"id":"1","text":"1"}
, {"action":2,"version":1,"id":"2","text":"2"}
, {"action":2,"version":1,"id":"3","text":"3"}
, {"action":2,"version":1,"id":"4","text":"4"}
, {"action":2,"version":1,"id":"5","text":"5"}
, {"action":2,"version":1,"id":"6","text":"6"}
, {"action":2,"version":1,"id":"7","text":"7"}
]							
		
jsonlist = JSON.stringify(subsectionlist);	
GetSubSectionId = jsonlist;			
</script>

<div class="col-md-12 nopadding">
	<acf:Region id="reg_div_list" title="PROGRAMME SEARCH" type="search">
		<acf:RegionAction>
			<a href="#" onClick="$(this).parents('.widget-box').pForm$clear();">Clear</a>
		</acf:RegionAction>
		<form id="frm_search" class="form-horizontal" data-role="search">
			<div class="form-group">
				<div class="col-md-2">
					<label for=s_programme_no style="display: block">Programme
						No.</label>
<%-- 					<acf:ComboBox id="s_programme_no" name="programme_no" --%>
<%-- 						editable="true" multiple="false"> --%>
<%-- 						<acf:Bind on="initData"> --%>
<!-- 							<script> -->

<!-- 	 				</script> -->
<%-- 						</acf:Bind> --%>
<%-- 					</acf:ComboBox> --%>
					<!--<acf:Bind on="initData"><script>
	 					$(this).acfComboBox("init", ${modules} );
	 				</script></acf:Bind>-->
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

				<div class="col-md-4">
					<label for=s_programme_name style="display: block">Programme
						Name</label>
					<acf:TextBox id="s_programme_name" name="programme_name"
						pickTime="false" />
				</div>

				<div class="col-md-4">
					<label for=s_chinese_programme_name style="display: block">Chin. Programme
						Name</label>
					<acf:TextBox id="s_chinese_programme_name" name="chinese_programme_name"
						pickTime="false" />
				</div>

				<div class="col-md-3">
					<label for=s_business_platform style="display: block">Business Platform</label>
					<acf:ComboBox id="s_business_platform" name="business_platform"
						pickTime="false">
						<acf:Bind on="initData">
							<script>
	 					$(this).acfComboBox("init", ${BusinessPlatform} );
	 					
	 				</script>
						</acf:Bind>
					</acf:ComboBox>
				</div>



				<div class="col-md-3">
					<label for=s_department style="display: block">Department</label>
					<acf:ComboBox id="s_department" name="department" editable="true"
						multiple="false">
						<acf:Bind on="initData">
							<script>
	 					$(this).acfComboBox("init", ${Department} );
	 				</script>
						</acf:Bind>
					</acf:ComboBox>
				</div>

			</div>
		</form>
	</acf:Region>
</div>


<div class="col-xs-12 nopadding">
	<form id="frm_main" class="form-horizontal" data-role="form">
		<acf:Region id="reg_div_list" type="list" title="PROGRAMME LIST">
			<acf:RegionAction>
				<a href="javascript:$('#grid_browse').pGrid$prevRecord();">Previous</a>
			&nbsp;
			<a href="javascript:$('#grid_browse').pGrid$nextRecord();">Next</a>
			</acf:RegionAction>
			<div class="col-xs-12">
				<acf:Grid id="grid_browse" url="apff011-search.ajax" readonly="true" autoLoad="false">
					<acf:Column name="programme_no" caption="Prog. No." width="75"></acf:Column>
					<acf:Column name="programme_name" caption="Eng Prog. Name"
						width="150"></acf:Column>
						<acf:Column name="chinese_programme_name" caption="Chin Prog. Name"
						width="150"></acf:Column>
					<acf:Column name="business_platform" caption="Business Platform"
						width="100"></acf:Column>
					<acf:Column name="programme_type" caption="Prog. Type"
						width="150"></acf:Column>
						<acf:Column name="department" caption="Prog. Nature/Department"
						width="150"></acf:Column>
					<acf:Column name="no_of_episode" caption="No. of Episode"
						width="100"></acf:Column>
					<acf:Column name="principal_setting_designer" caption="P.Setting Designer"
						width="100"></acf:Column>
					<acf:Column name="principal_costume_designer" caption="P.Costume Designer"
						width="100"></acf:Column>
					<acf:Column name="executive_producer" caption="Executive Producer"
						width="100"></acf:Column>

				</acf:Grid>
			</div>
		</acf:Region>

		<acf:Region id="mod_main" title="PROGRAMME MAINTENANCE" type="form">
			<div class="col-xs-12 form-padding oneline">
				<label class="control-label col-md-2" for="programme_no">Programme No.:</label>
				<div class="col-md-2">
<!-- 			regular repression for checking first 4 characters	https://stackoverflow.com/questions/19631633/regular-expression-for-first-4-characters -->
					<acf:TextBox id="programme_no" name="programme_no" readonly="false"
						disabled="true" checkMandatory="true" button="fa-search"
						maxlength="9" allowKey="[0-9]" format="^[8]{1}[0-9]{8}|[9]{3}[0-9]{6}$" >
<!-- 						formatInstruction="Invalid format, Temporary programme must start with '999'" -->
						<acf:Bind target="button" on="click">
							<script>
      						Dialog.create()
							.setCaption("Search")
							.setWidth(1250)
							.addDismissButton("OK", function(){
								if ($.type(programme_no) == "string"){
									Action.modify();
									$("#frm_main #programme_no").setValue(programme_no);
									programme_no = "";
								}
							})
							.setResultCallback(function(result) {
								programme_no = result.pgm_num;
							})
							.showUrl("../../apf/apff011/apff011-search-cpl-prog");
      			
      			</script>
						</acf:Bind>
						<acf:Bind on="change" modified="true">
							<script>
   						var programme_no = $(this).getValue(); //what this prog_no refer to?
//    						$(this).setFormatInstruction("Invalid format, Temporary programme must start with '999'");
   						if ($.trim(programme_no) != "" && Action.getMode() == 'new' && programme_no.charAt(0) == "8"){//when it's on new mode and programme is set and the first character is equal to 8.    
	      					$.ajax({
								headers: {
									'Accept'       : 'application/json',
									'Content-Type' : 'application/json; charset=utf-8'
								},
								async  : false,
								type   : "POST",
								url    : "apff011-get-cpl-programme.ajax",
								data   : JSON.stringify({
									'programme_no' : programme_no
								}),
								success: function(data) {
								$("#frm_main").data("noTrigger",true);// a function of 'data' type?
									 if (data.pgm) {
									 $("#frm_main #chinese_programme_name").setValue($.trim(data.pgm.chinese_title));
									 $("#frm_main #programme_name").setValue($.trim(data.pgm.title));
									 $("#frm_main #no_of_episode").setValue(data.pgm.num_of_epi);
									 //console.log(data.pgm.pgm_type);
									 	if (data.pgm.pgm_type == 'L'){
	   										$("#frm_main #programme_type").setValue("LOCAL")}
										else {
	   										if (data.pgm.pgm_type == 'F')
	    										$("#frm_main #programme_type").setValue("FOREIGN")};
									 //$("#frm_main #programme_type").setValue(data.pgm.pgm_type);
									 $("#frm_main #prog_no").setError("", "APM011V");
									 }
									 else
									 {
									 $("#frm_main #chinese_programme_name").setValue("");
									 $("#frm_main #programme_name").setValue("");
									 $("#frm_main #no_of_episode").setValue("");
									 //console.log(data.pgm.pgm_type);
									 $("#frm_main #programme_type").setValue("");
									 $("#frm_main #prog_no").setError(ACF.getQtipHint('APM011V'), "APM011V");
									 }
									//	2nd pass rule 3
									//	From Epi., To Epi., No. of Epi., VTR Period, EFP Period and Oversea Shooting will be retrieved from CPL database 
									// 	and displayed on screen when the programme no. is created in this function. 
									//	Users are allowed to override them if users got the more up-to-date data than that of CPL.
									if (data.prod) {
									if (data.prod.studio_start_dt && data.prod.studio_start_dt != '1900-01-01')
											$("#frm_main #vtr_from_date").setValue(moment(data.prod.studio_start_dt).format('x'));
										else
											$("#frm_main #vtr_from_date").setValue("");
											
									if (data.prod.studio_start_dt && data.prod.studio_start_dt != '1900-01-01')
											$("#frm_main #vtr_to_date").setValue(moment(data.prod.studio_end_dt).format('x'));
										else
											$("#frm_main #vtr_to_date").setValue("");
											
									if (data.prod.studio_start_dt && data.prod.studio_start_dt != '1900-01-01')
											$("#frm_main #efp_from_date").setValue(moment(data.prod.efp_start_dt).format('x'));
										else
											$("#frm_main #efp_from_date").setValue("");
											
									if (data.prod.studio_start_dt && data.prod.studio_start_dt != '1900-01-01')
											$("#frm_main #efp_to_date").setValue(moment(data.prod.efp_end_dt).format('x'));
										else
											$("#frm_main #efp_to_date").setValue("");
									
									//$("#frm_main #vtr_from_date").setValue(data.prod.studio_start_dt);
									//$("#frm_main #vtr_to_date").setValue(data.prod.studio_end_dt);
									//$("#frm_main #efp_from_date").setValue(data.prod.efp_start_dt);
									//$("#frm_main #efp_to_date").setValue(data.prod.efp_end_dt);
									}
									else {
										//$("#frm_main #first_air_nw").setValue("");
										$("#frm_main #efp_from_date").setValue("");
										$("#frm_main #efp_to_date").setValue("");
										$("#frm_main #vtr_from_date").setValue("");
										$("#frm_main #vtr_to_date").setValue("");
									}
									if (data.cast) {
									$("#frm_main #executive_producer").setValue(data.cast.ep_name);
									
									//console.log(data.cast);
									
									}
									else
									{
									$("#frm_main #executive_producer").setValue("");
									}
								}
							});
						}
   					</script>
						</acf:Bind>
					</acf:TextBox>
				</div>

				<label class="control-label col-md-2" for="programme_name">Prog.
					Name:</label>
				<div class="col-md-6">
					<acf:TextBox id="programme_name" name="programme_name"
						readonly="false" disabled="true" checkMandatory="true" />

				</div>

			</div>

			<div class="hidden">
				<label class="control-label col-md-2" for="transfer_status">Transfer Status:</label>
				<div class="col-md-4">
					<acf:TextBox id="transfer_status" name="transfer_status" readonly="false" disable="true" />
				</div>
			</div>
			
			<div class="hidden">
				<label class="control-label col-md-2" for="transfer_from_date">Transfer
					From Date.:</label>
				<div class="col-md-4">
					<acf:DateTimePicker id="transfer_from_date" name="transfer_from_date"
						readonly="false" disabled="true" />

				</div>

				<label class="control-label col-md-2" for="transfer_to_date">Transfer
					To Date.:</label>
				<div class="col-md-4">
					<acf:DateTimePicker id="transfer_to_date" name="transfer_to_date"
						readonly="false" disabled="true" />

				</div>

				<label class="control-label col-md-2" for="transfer_remarks">Transfer
					Remarks.:</label>
				<div class="col-md-4">
					<acf:TextBox id="transfer_remarks" name="transfer_remarks"
						readonly="false" disabled="true" />

				</div>
				
				


			</div>

			<div class="col-xs-12 form-padding oneline">
				<label class="control-label col-md-2" for="chinese_programme_name">Chin
					Prog. Name:</label>
				<div class="col-md-7">
					<acf:TextBox id="chinese_programme_name"
						name="chinese_programme_name" readonly="false" disabled="true"
						checkMandatory="true" />

				</div>



			</div>

			<div class="col-xs-12 form-padding oneline">
				<label class="control-label col-md-2" for="business_platform">Business
					Platform:</label>
				<div class="col-md-4">
					<acf:ComboBox id="business_platform" name="business_platform" readonly="false" disabled="false">
						
	 				</acf:ComboBox>
				</div>

				<label class="control-label col-md-2" for="description">Prog.
					Nature/Dept:</label>
				<div class="col-md-4">
					<acf:ComboBox id="department" name="department" readonly="false" disabled="false" checkMandatory="true">

					</acf:ComboBox>
				</div>

			</div>

			<div class="col-xs-12 form-padding oneline">
				<label class="control-label col-md-2" for="no_of_episode">No.
					of Episode:</label>
				<div class="col-md-2">
					<acf:TextBox id="no_of_episode" name="no_of_episode"
						readonly="false" disabled="true" checkMandatory="true" />

				</div>
				<label class="control-label col-md-1" for="programme_type">Prog.
					Type:</label>
				<div class="col-md-2">
					<acf:ComboBox id="programme_type" name="programme_type"
						readonly="false" disabled="true" checkMandatory="true">
						<acf:Bind on="initData">
							<script>
	 					$(this).acfComboBox("init", ${ProgrammeType} );
	 				</script>
						</acf:Bind>
					</acf:ComboBox>

				</div>

				<label class="control-label col-md-1" for="transfer_status_full">Transfer
					Status:</label>
				<div class="col-md-2">
					<acf:TextBox id="transfer_status_full" name="transfer_status_full"
						readonly="true" disabled="true" />

				</div>

			</div>

			<div class="col-md-12 form-padding oneline">
				<label class="control-label col-md-2" for="vtr_from_date">VTR
					Period Start:</label>
				<div class="col-md-2">
					<acf:DateTimePicker id="vtr_from_date" name="vtr_from_date"
						readonly="false" pickDate="true" pickTime="false" checkMandatory="true">
						<acf:Bind on="validate"><script>
							
			   				var ts = $(this).getValue();
			   				if (ts != "" && ts > $("#vtr_to_date").getValue()) { // 2015/01/01 20:00
			   					//$(this).setError(ACF.getQtipHint("Earlier than start date!"), "EXE003");
			   					console.log("entered condition")
			   					$(this).setError(ACF.getQtipHint("APF011E"), "APF011E");
			   					//ACF.getQtipHint("Earlier than start date!");
			   					//Alert.showError(ACF.getDialogMessage("ACFF004E"), callback);
			   				}
			   				else {
			   				if($(this).isError() == true)
			   					{
			   					//$(this).clearError();
			   					$(this).setError("", "APF011E");
			   					$("#vtr_to_date").setError("", "APF011E");
			   					// disable + enable = clearerror...
			   					}
			   				if($("#vtr_to_date").isError() == true)
			   					{
			   					//$(this).clearError();
			   					$(this).setError("", "APF011E");
			   					$("#vtr_to_date").setError("", "APF011E");
			   					// disable + enable = clearerror...
			   					}
			   					
			   				}
			   			</script></acf:Bind>
				</acf:DateTimePicker>
				</div>

				<label class="control-label col-md-1" for="vtr_to_date">End:</label>
				<div class="col-md-2">
					<acf:DateTimePicker id="vtr_to_date" name="vtr_to_date"
						readonly="false" pickDate="true" pickTime="false" checkMandatory="true">
						<acf:Bind on="validate"><script>
			   				var ts = $(this).getValue();
			   				if (ts != "" && ts < $("#vtr_from_date").getValue()) { // 2015/01/01 20:00
			   					//$(this).setError(ACF.getQtipHint("Earlier than start date!"), "EXE003");
			   					$(this).setError(ACF.getQtipHint("APF011E"), "APF011E");
			   					
			   					//ACF.getQtipHint("Earlier than start date!");
			   					//Alert.showError(ACF.getDialogMessage("ACFF004E"), callback);
			   				}
			   				else {
			   				if($(this).isError() == true)
			   					{
			   					//$(this).clearError();
			   					$(this).setError("", "APF011E");
			   					$("#efp_from_date").setError("", "APF011E");
			   					// disable + enable = clearerror...
			   					}
			   				if($("#vtr_from_date").isError() == true)
			   					{
			   					//$(this).clearError();
			   					$(this).setError("", "APF011E");
			   					$("#vtr_from_date").setError("", "APF011E");
			   					// disable + enable = clearerror...
			   					}
			   					
			   				}
			   			</script></acf:Bind>
					</acf:DateTimePicker>
				</div>
			</div>
			<div class="col-md-12 form-padding oneline">
				<label class="control-label col-md-2" for="efp_from_date">EFP
					Period Start:</label>
				<div class="col-md-2">
					<acf:DateTimePicker id="efp_from_date" name="efp_from_date"
						readonly="false" pickDate="true" pickTime="false" checkMandatory="true">
						<acf:Bind on="validate"><script>
			   				var ts = $(this).getValue();
			   				if (ts != "" && ts > $("#efp_to_date").getValue()) { // 2015/01/01 20:00
			   					//$(this).setError(ACF.getQtipHint("Earlier than start date!"), "EXE003");
			   					$(this).setError(ACF.getQtipHint("APF011E"), "APF011E");
			   					//ACF.getQtipHint("Earlier than start date!");
			   					//Alert.showError(ACF.getDialogMessage("ACFF004E"), callback);
			   				}
			   				else {
			   				if($(this).isError() == true)
			   					{
			   					//$(this).clearError();
			   					$(this).setError("", "APF011E");
			   					$("#efp_to_date").setError("", "APF011E");
			   					// disable + enable = clearerror...
			   					}
			   				if($("#efp_to_date").isError() == true)
			   					{
			   					//$(this).clearError();
			   					$(this).setError("", "APF011E");
			   					$("#efp_to_date").setError("", "APF011E");
			   					// disable + enable = clearerror...
			   					}
			   					
			   				}
			   			</script></acf:Bind>
						</acf:DateTimePicker>

				</div>

				<label class="control-label col-md-1" for="efp_to_date">End:</label>
				<div class="col-md-2">
					<acf:DateTimePicker id="efp_to_date" name="efp_to_date"
						readonly="false" pickDate="true" pickTime="false" checkMandatory="true">
					<acf:Bind on="validate"><script>
			   				var ts = $(this).getValue();
			   				if (ts != "" && ts < $("#efp_from_date").getValue()) { // 2015/01/01 20:00
			   					//$(this).setError(ACF.getQtipHint("Earlier than start date!"), "EXE003");
			   					$(this).setError(ACF.getQtipHint("APF011E"), "APF011E");
			   					//ACF.getQtipHint("Earlier than start date!");
			   					//Alert.showError(ACF.getDialogMessage("ACFF004E"), callback);
			   				}
			   				else {
			   				if($(this).isError() == true)
			   					{
			   					//$(this).clearError();
			   					$(this).setError("", "APF011E");
			   					$("#efp_from_date").setError("", "APF011E");
			   					// disable + enable = clearerror...
			   					}
			   				if($("#efp_from_date").isError() == true)
			   					{
			   					//$(this).clearError();
			   					$(this).setError("", "APF011E");
			   					$("#efp_from_date").setError("", "APF011E");
			   					// disable + enable = clearerror...
			   					}
			   				}
			   			</script></acf:Bind>
					</acf:DateTimePicker>
				</div>

			</div>



			<div class="col-xs-12 form-padding oneline">
				<label class="control-label col-md-2" for="executive_producer">Executive
					Producer</label>
				<div class="col-md-4">
					<acf:TextBox id="executive_producer" name="executive_producer"
						readonly="false" disabled="true" />

				</div>
				
				<label class="control-label col-md-2" for="cost_first_input_date">Cost First Input Date.:</label>
				<div class="col-md-4">
					<acf:DateTimePicker id="cost_first_input_date" name="cost_first_input_date"
						readonly="true" disabled="true" pickTime="false" useDefValIfEmpty="true" />

				</div>
			</div>

			<div class="col-xs-12 form-padding oneline">
				<label class="control-label col-md-2" for="setting_designer">P.
					Designer Setting:</label>
				<div class="col-md-4">
					<acf:TextBox id="principal_setting_designer"
						name="principal_setting_designer" readonly="false" disabled="true" />

				</div>

				<label class="control-label col-md-2" for="setting_designer">Designer
					Setting:</label>
				<div class="col-md-4">
					<acf:TextBox id="setting_designer" name="setting_designer"
						readonly="false" disabled="true" />

				</div>

			</div>

			<div class="col-xs-12 form-padding oneline">
				<label class="control-label col-md-2" for="costume_designer">P.
					Designer Costume:</label>
				<div class="col-md-4">
					<acf:TextBox id="principal_costume_designer"
						name="principal_costume_designer" readonly="false" disabled="true" />

				</div>

				<label class="control-label col-md-2" for="costume_designer">Designer
					Costume:</label>
				<div class="col-md-4">
					<acf:TextBox id="costume_designer" name="costume_designer"
						readonly="false" disabled="true" />

				</div>
			</div>



			<div class="col-xs-12 form-padding oneline">
				<label class="control-label col-md-2" for="transfer_from_programme">Transfer
					from prog.:</label>
				<div class="col-md-4">
					<acf:TextBox id="transfer_from_programme"
						name="transfer_from_programme" readonly="true" disabled="true" />

				</div>

				<label class="control-label col-md-2" for="transfer_to_programme">Transfer
					to prog:</label>
				<div class="col-md-4">
					<acf:TextBox id="transfer_to_programme"
						name="transfer_to_programme" readonly="true" disabled="true" />

				</div>

			</div>

		</acf:Region>
			
		<acf:Region id="indirect_budgert" type="form"
			title="INDIRECT BUDGET">

			<div class="col-xs-12">
			
			
		<div class="hidden">
<!-- 			<div class="col-md-1"></div> -->
			
			<div class="col-md-2">
				<p id="container_for_select_container" ></p>
			</div>
			<div class="col-md-2">
				<p id="container_for_select_container2"  ></p>
			</div>
			
			<div class="hidden">
				<p id="container_for_select_container3"  ></p>
			</div>
		</div>
		
				<acf:Grid id="indirect_browse"
					url="apff011-section-and-indirect-budget.ajax" autoLoad="false"
					readonly="false" addable="true" deletable="true" editable="true"
					rowNum="9999" multiLineHeader="true">
					<acf:Column name="section_id" type="select" columnKey="false"
						checkMandatory="true" editable="true" caption="Section ID."
						width="75" initData="${GetSectionId}">
						<acf:Bind on="validate">
							<script>
				function validation (newValue, oldValue, newData, oldData, id) {
				var sub_section_id = newData.sub_section_id;
				
				//var section_id = newData.section_id;
							if (newValue != sub_section_id.slice(0,2))
									{console.log("should set error here!");
									
									warning = ACF.getQtipHint("APF011V");
									return warning;
									}
				
				//console.log(sub_section_id);
				$.ajax({
								headers: {
									'Accept'       : 'application/json',
									'Content-Type' : 'application/json; charset=utf-8'
								},
								async  : false,
								type   : "POST",
								url    : "${ctx}/arc/apf/apf-get-section-name-by-id-and-subid.ajax",
								data   : JSON.stringify({
									
									'section_id'	: newValue,
									'sub_section_id' : sub_section_id.slice(3,4)
									
								}),
								
								success: function(data) {
									console.log(data.section_name);
									if (data.section_name != null) 
									{
									console.log(data.section_name);
									console.log(newValue);
									console.log(sub_section_id.slice(0,2));
										$("#indirect_browse").setRowData(id, {section_name: data.section_name});
									if (newValue != sub_section_id.slice(0,2))
									{console.log("should set error here!");
									$(this).setError(ACF.getQtipHint("APF004E"),"APF004E");
									warning = ACF.getQtipHint("APF004E");
									return warning;
									}
									
									}
									else {
// 									console.log("should set error here!")
// 									return ACF.getQtipHint("APF004E");
// 									//console.log("should set error here!")
									}
								}
							});
							}
				</script>
						</acf:Bind>
					</acf:Column>
					<acf:Column name="sub_section_id" type="select" columnKey="false"
						checkMandatory="true" editable="true" caption="Sub Section ID."
						width="300" initData="${GetSubSectionId}">
						<acf:Bind on="validate">
							<script>
							function validation (newValue, oldValue, newData, oldData, id) {
							var section_id = newData.section_id;
							if (section_id != newValue.slice(0,2))
									{console.log("should set error here!");
									
									warning = ACF.getQtipHint("APF011V");
									return warning;
									}
							//getSectName (newValue, oldValue, newData, oldData, id);
							//var x=document.getElementById("container_for_select_container3").value;
// 							console.log(sltedv);
								
// 							if ()
// 							if (newValue == 11)
// 								{
// 								$("#indirect_browse").setRowData(id, {sub_section_id: document.getElementById("container_for_select_container3").innerHTML});
// 								}
							}
							</script>
							</acf:Bind>
					</acf:Column>
					<acf:Column name="section_name" editable="true"
						caption="Section Name" checkMandatory="true" hidden="true" align="right" width="100"></acf:Column>
					<acf:Column name="indirect_budget_amount" editable="true"
						caption="Indirect Budget" align="right" width="100"></acf:Column>
					<acf:Column name="indirect_budget_hour" editable="true"
						caption="Budget Hour" align="right" width="60"></acf:Column>
					<acf:Column name="created_at" caption="" hidden="true"></acf:Column>
					<acf:Column name="programme_no" caption="" hidden="true"></acf:Column>
					<acf:Column name="created_by" caption="" hidden="true"></acf:Column>
					<acf:Column name="modified_at" caption="" hidden="true"></acf:Column>

				</acf:Grid>

			</div>

		</acf:Region>
		<div class="col-md-15 form-padding oneline">
			<div class="col-md-8"></div>
			<label class="control-label col-md-1"
				for="total_indirect_and_budget_amount">Total</label>
			<div class="col-md-2">
				<acf:TextBox id="total_indirect_amount" name="total_indirect_amount"
					readonly="true" useNumberFormat="true" align="right"></acf:TextBox>
			</div>
			<div class="col-md-1">
				<acf:TextBox id="total_hour_amount" name="total_hour_amount"
					readonly="true" useNumberFormat="true" align="right"></acf:TextBox>
			</div>
		</div>
		<acf:Region id="direct_budget" title="DIRECT BUDGET" type="form">
			<div class="col-md-12">

				<acf:Grid id="direct_browse" url="apff011-direct-budget.ajax"
					autoLoad="false" readonly="false" addable="true" deletable="true"
					editable="true" rowNum="9999" multiLineHeader="true">
					<acf:Column name="direct_budget_description" editable="true"
						columnKey="true" caption="Direct Budget Items"
						width="200" checkMandatory="true"></acf:Column>
					<acf:Column name="direct_budget_amount" editable="true"
						columnKey="true" caption="Direct Budget" width="40" align="right"
						checkMandatory="true"></acf:Column>
					<acf:Column name="created_at" caption="" hidden="true"></acf:Column>
					<acf:Column name="sequence_no" caption="" hidden="true"></acf:Column>
					<acf:Column name="modified_by" caption="" hidden="true"></acf:Column>
					<acf:Column name="created_by" caption="" hidden="true"></acf:Column>
					<acf:Column name="modified_at" caption="" hidden="true"></acf:Column>
					<acf:Column name="programme_no" caption="" hidden="true"></acf:Column>
				</acf:Grid>
			</div>
		</acf:Region>

		<div class="col-md-15 form-padding oneline">
			<div class="col-md-8"></div>
			<label class="control-label col-md-2"
				for="total_indirect_and_budget_amount">Total Direct Budget</label>
			<div class="col-md-2">
				<acf:TextBox id="total_direct_amount" name="total_direct_amount"
					readonly="true" useNumberFormat="true" align="right"></acf:TextBox>
			</div>

		</div>

		<acf:Region id="direct_budget_details" title="DIRECT BUDGET DETAILS"
			type="form">
			<div class="col-md-12">
				<acf:Grid id="detail_budget_browse"
					url="apff011-detail-direct-budget.ajax" autoLoad="false"
					readonly="false" addable="true" deletable="true" editable="true"
					rowNum="9999" multiLineHeader="true">
					<acf:Column name="account_allocation" editable="true"
						columnKey="true" caption="Budget A/C Alloc" type="select"
						width="100" checkMandatory="true" columnKey="true"
						initData="${GetAccountAllocation}">
						<acf:Bind on="validate">
							<script>
			function validation (newValue, oldValue, newData, oldData, id) {
				var id = $("#detail_budget_browse").pGrid$getSelectedId();
				//var budget_account_allocation = $("#detail_budget_browse").getRowData(id).budget_account_allocation;
			//	console.log(sub_section_id);
			
				$.ajax({
								headers: {
									'Accept'       : 'application/json',
									'Content-Type' : 'application/json; charset=utf-8'
								},
								async  : false,
								type   : "POST",
								url    : "${ctx}/arc/apf/apf-allocation-account.ajax",
								data   : JSON.stringify({
									
									'budget_account_allocation'	: newValue
									
								}),
								success: function(data) {
									if (data.budget_account_description != null) {
								console.log(data);
										$("#detail_budget_browse").setRowData(id, {budget_account_description: data.budget_account_description});
									}
									else {
									}
								}
							});
						}	
				</script>
						</acf:Bind>
					</acf:Column>
<%-- 					<acf:Column name="budget_account_description" editable="true" --%>
<%-- 						caption="A/C Alloc. Desc" width="100" hidden="false"></acf:Column> --%>
					<acf:Column name="direct_budget_amount" editable="true"
						columnKey="true" checkMandatory="true"
						caption="Direct Budget Amount" align="right" width="40"></acf:Column>
					<acf:Column name="created_at" caption="" hidden="true"></acf:Column>
					<acf:Column name="modified_by" caption="" hidden="true"></acf:Column>
					<acf:Column name="created_by" caption="" hidden="true"></acf:Column>
					<acf:Column name="modified_at" caption="" hidden="true"></acf:Column>
					<acf:Column name="programme_no" caption="" hidden="true"
						columnKey="true"></acf:Column>
					<acf:Column name="budget_account_allocation" caption=""
						hidden="true"></acf:Column>
					<acf:Bind on="getNewRecord">
						<script>
						//$(this).data("newRecord", {programme_no:$("#frm_main #programme_no").getValue()});
					</script>
					</acf:Bind>
				</acf:Grid>
			</div>
		</acf:Region>

		<div class="col-md-15 form-padding oneline">
			<div class="col-md-8"></div>
			<label class="control-label col-md-2"
				for="total_indirect_and_budget_amount">Total Detailed Budget</label>
			<div class="col-md-2">
				<acf:TextBox id="total_detail_direct_amount"
					name="total_detail_direct_amount" readonly="true"
					useNumberFormat="true" align="right"></acf:TextBox>
			</div>

		</div>

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
		</acf:Region>
















	</form>
</div>

<script>

Controller.setOption({
	searchForm: $("#frm_search"),
	browseGrid: $("#grid_browse"),
	searchKey: "programme_no",
	browseKey: "programme_no",
	//searchForm: $("#frm_search"),
	//browseKey: "section_no",
	
	
	//initMode: "${mode}",
	recordForm: $("#frm_main"),
	recordKey: {
		programme_no: "${programme_no}", //labout_type on the left is the key of the table, the value on the right is redundant(design problem).
		//supplier_code: "${supplier_code}"
	},
	getUrl: "apff011-get-form.ajax",
	saveUrl: "apff011-save.ajax",
	onLoadSuccess: function(data)
	{
	
	if ($("#frm_main #transfer_status").getValue() == 'A'){
	   	$("#frm_main #transfer_status_full").setValue("ACTIVE")}
	else {
	   	if ($("#frm_main #transfer_status").getValue() == 'I')
	    	$("#frm_main #transfer_status_full").setValue("INACTIVE")
	   	else {
	    	if ($("#frm_main #transfer_status").getValue() == 'P')
	        	$("#frm_main #transfer_status_full").setValue("PENDING")}};
	
	$("#total_indirect_amount").setValue($("#indirect_browse").pGrid$getSumOfColumn("indirect_budget_amount"));
	$("#total_hour_amount").setValue($("#indirect_browse").pGrid$getSumOfColumn("indirect_budget_hour"));
	$("#total_direct_amount").setValue($("#direct_browse").pGrid$getSumOfColumn("direct_budget_amount"));
	$("#total_detail_direct_amount").setValue($("#detail_budget_browse").pGrid$getSumOfColumn("direct_budget_amount"));
	},
	
	getSaveData: function() {
		//console.log(moment("1900-01-01"));
		
		$("#indirect_browse").pGrid$setHiddenValueForAllRecords("programme_no", $("#frm_main #programme_no").getValue());
		$("#direct_browse").pGrid$setHiddenValueForAllRecords("programme_no", $("#frm_main #programme_no").getValue());
		$("#detail_budget_browse").pGrid$setHiddenValueForAllRecords("programme_no", $("#frm_main #programme_no").getValue());
		return JSON.stringify({
			'form' : Controller.opt.recordForm.pForm$getModifiedRecord( Action.getMode() ),
			'IndirectBudget' : $("#indirect_browse").pGrid$getModifiedRecord(), 
			'AccountAllocation' : $("#detail_budget_browse").pGrid$getModifiedRecord(),
			'DirectBudget' : $("#direct_browse").pGrid$getModifiedRecord(), 
		//	'Item' : $("#grid_item").pGrid$getModifiedRecord(),
		//	'producer' : $("#grid_producer").pGrid$getModifiedRecord(),
		//	'director' : $("#grid_director").pGrid$getModifiedRecord(),
		});
	},
	ready: function() { Action.setMode("search"); }
	}).executeSearchBrowserForm();
	
	$(document).on('new', function() {
	$('#indirect_browse').pGrid$clear();
	$('#direct_browse').pGrid$clear();
	$('#detail_budget_browse').pGrid$clear();
	$("#frm_main #transfer_status").setValue('A');
	$("#frm_main #transfer_status_full").setValue('ACTIVE');
	$("#frm_main #transfer_from_programme").setValue('');
	$("#frm_main #transfer_to_programme").setValue('');
	$("#frm_main #transfer_from_date").setValue(moment("1900-01-01"));
	$("#frm_main #transfer_to_date").setValue(moment("1900-01-01"));
	//$("#frm_main #cost_first_input_date").setValue(moment("1900-01-01"));
	//$("#frm_main #vtr_to_date").setValue(moment("1900-01-01"));
	});
	$(document).on('amend', function() {
	
	$("#frm_main #transfer_from_programme").disable();
	$("#frm_main #transfer_to_programme").disable();
	$("#frm_main #transfer_status").disable();
	
	var check_pg_no = $("#frm_main #programme_no").getValue();
	if (!check_pg_no.startsWith(999))
	{
	$("#frm_main #chinese_programme_name").disable();
	$("#frm_main #programme_type").disable();
	$("#frm_main #programme_name").disable();
	$("#frm_main #no_of_episode").disable();
	$("#frm_main #vtr_from_date").disable();
	$("#frm_main #vtr_to_date").disable();
	$("#frm_main #efp_from_date").disable();
	$("#frm_main #efp_to_date").disable();
	$("#frm_main #executive_producer").disable();
	}
	});
	</script>
	<script>
var i, theContainer, theSelect, theOptions, numOptions, anOption; //definition of html object (incomplete)
//first combobox options
theOptions = ['option 1','option 2','option 3','option 4','option 5','option 6','option 7','option 8','option 9']; 

//second combobox options
twoDoptionsdoptions =   
[
['option1 1','option1 2','option1 3'],
['option2 1','option2 2','option2 3'],
['option3 1','option3 2','option3 3'],
['option4 1','option4 2','option4 3'],
['option5 1','option5 2','option5 3'],
['option6 1','option6 2','option6 3'],
['option7 1','option7 2','option7 3'],
['option8 1','option8 2','option8 3'],
['option9 1','option9 2','option9 3']
];


// Create the container <div>
theContainer = document.createElement('div');
theContainer.id = 'my_new_div';

theContainer2 = document.createElement('div');
theContainer2.id = 'my_new_div2';

theContainer3 = document.createElement('div');
theContainer3.id = 'my_new_div3';

// Create the <select>
theSelect = document.createElement('select');

// Give the <select> some attributes
theSelect.name = 'name_of_select';
theSelect.id = 'id_of_select';
theSelect.className = 'class_of_select';


// Create the <select>
theSelect2 = document.createElement('select');

// Give the <select> some attributes
theSelect2.name = 'name_of_select2';
theSelect2.id = 'id_of_select2';
theSelect2.className = 'class_of_select2';

theSelect3 = document.createElement('select');

// Give the <select> some attributes
theSelect3.name = 'name_of_select3';
theSelect3.id = 'id_of_select3';
theSelect3.className = 'class_of_select3';



// Define combination and output value of two comboboxes' options
theSelect.onchange = function () {
    // Do whatever you want to do when the select changes
   // alert('You selected option '+this.selectedIndex);
   var length = theSelect2.options.length;
	for (i = length - 1 ; i >= 0 ; i--) {
  	//theSelect2.options[i] = null;
    theSelect2.remove(i);
	}
   k = theSelect.value;
   console.log(k);
   //console.log('-below-');
   console.log(length);
   numOptions2 = twoDoptionsdoptions[k].length;
for (i = 0; i < numOptions2; i++) {
    anOption3 = document.createElement('option');
    anOption3.value = i;
    anOption3.innerHTML = twoDoptionsdoptions[k][i];
    theSelect2.appendChild(anOption3);
}
  } 


theSelect2.onchange = function () {
    // Do whatever you want to do when the select changes
   // alert('You selected option '+this.selectedIndex);
   var length = theSelect3.options.length;
	for (i = length - 1 ; i >= 0 ; i--) {
  	//theSelect2.options[i] = null;
    theSelect3.remove(i);
	}
   sltedv = this.selectedIndex;
   Option = document.createElement('option');
    Option.value = sltedv;
    Option.innerHTML = sltedv + 1;
   theSelect3.appendChild(Option);
   //document.getElementById('container_for_select_container3').appendChild(v);
  } 
;

//initialize two dynamic Comboboxes
numOptions = theOptions.length;
for (i = 0; i < numOptions; i++) {
    anOption = document.createElement('option');
    anOption.value = i;
    anOption.innerHTML = theOptions[i];
    theSelect.appendChild(anOption);
}
 numOptions2 = twoDoptionsdoptions[0].length;
for (i = 0; i < numOptions2; i++) {
    anOption3 = document.createElement('option');
    anOption3.value = i;
    anOption3.innerHTML = 'NULL';
    theSelect2.appendChild(anOption3);
}


function myFunction(p1, p2) {
    return p1 * p2;
}
//document.getElementById("demo").innerHTML = myFunction(4, 3);
document.getElementById('container_for_select_container').appendChild(theContainer);
theContainer.appendChild(theSelect);
document.getElementById('container_for_select_container2').appendChild(theContainer2);
theContainer2.appendChild(theSelect2);
document.getElementById('container_for_select_container3').appendChild(theContainer3);
theContainer3.appendChild(theSelect3);
// $("#indirect_browse").pForm$setRelatedComboBox(${SubSectionAndSectionId}, [$("#indirect_browse #section_id"), $("#indirect_browse #sub_section_id")]);

$("#frm_main").pForm$setRelatedComboBox(${businessDepartment}, [$("#frm_main #business_platform"), $("#frm_main #department")]);
	
</script>
