<%@ page import="acf.acf.General.jsp.*"%>
<%@ page import="acf.acf.General.core.ACFgRawModel" %>
<%@ page import="java.util.*"%>
<%@ taglib prefix="acf" uri="/acf/tld/acf-taglib" %> 


<!-- PAGE CONTENT BEGINS -->
<form id="frm_programme_search" class="form-horizontal" data-role="form" >
   	<div class="form-group">
    	<div class="col-lg-2 col-md-6 col-sm-6 col-xs-12">
    		<label for=programme_no style="display:block">Programme No.</label>
    		<acf:TextBox id="programme_no" name="programme_no" maxlength="9">
    			<acf:Bind target="textBox" on="enter"><script>
					programmeSearch();
				</script></acf:Bind></acf:TextBox>
      	</div>  
      	
      	<div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
   			<label for=programme_name style="display:block">English Prog. Name</label>
   			<acf:TextBox id="programme_name" name="programme_name">
   				<acf:Bind target="textBox" on="enter"><script>
					programmeSearch();
				</script></acf:Bind></acf:TextBox>
      	</div>  
      	<div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
   			<label for=chinese_programme_name style="display:block">Chinese Prog. Name</label>
   			<acf:TextBox id="chinese_porgramme_name" name="chinese_programme_name">
   				<acf:Bind target="textBox" on="enter"><script>
					programmeSearch();
				</script></acf:Bind></acf:TextBox>
      	</div> 
      	
       	<div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
   			<label for=business_platform style="display:block">Business Platform</label>
	      			<acf:ComboBox id="f_business_platform" name="business_platform" editable="false" multiple="false">  
		    		</acf:ComboBox>
      	</div>  

       	<div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
   			<label for=department style="display:block">Department</label>
	      			<acf:ComboBox id="f_department" name="department" editable="false" multiple="false">  
		    		</acf:ComboBox>
      	</div>  
      	      	
       	<div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
   			<label for=programme_type style="display:block">Programme Type</label>
 			   		<acf:ButtonGroup id="programme_type" name="programme_type" type="radio">
			    		<setting>
			    			<option id="_foreign" value="FOREIGN" label="Foreign"/>
			    			<option id="_local" value="LOCAL" label="Local"/>
			    		</setting>
			    	</acf:ButtonGroup> 	
      	</div>     

       	<div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
   			<label for=temp_on_off style="display:block">&nbsp;</label>
			    	<acf:ButtonGroup id="temp_on_off" name="temp_on_off" type="radio">
   						<setting>
			    			<option id="_temp" value="999" label="Temporary"/>
                  		</setting>
					</acf:ButtonGroup> 	
      	</div>     

    <input type="hidden" id="dialog_search" name="dialog_search" value="1"/>
      	
      	<div class="search-button col-lg-2 col-md-3 col-sm-6 col-xs-12">
      	   <label style="display:block">&nbsp;</label>
           <span class="btn-group">
			<acf:Button id="dialog_search_btn" title="Search" icon="fa-search">
				<acf:Bind on="click"><script>
					programmeSearch();
				</script></acf:Bind>
			</acf:Button>
			<acf:Button id="dialog_clear_btn" title="" icon="fa-eraser">
				<acf:Bind on="click"><script>
					$('#frm_programme_search').pSearch$clear();
					programmeSearch();
				</script></acf:Bind>
			</acf:Button>
		</span>
	</div>      		
  	</div>	    	

</form>

<div class="col-md-12">
	<acf:Grid id="grid_arc_programme_browse" url="../../apf/apff011/apff011-grid-arc-programme.ajax" rowNum="10" readonly="true" autoLoad="false">
		<acf:Column name="programme_no" caption="Programme No." width="100"></acf:Column>
		<acf:Column name="programme_name" caption="Programme Name" width="300"></acf:Column>
		<acf:Column name="chinese_programme_name" caption="Chinese Programme Name" width="300"></acf:Column>
		<acf:Column name="business_platform" caption="Business Platform" width="100"></acf:Column>
		<acf:Column name="department" caption="Department" width="100"></acf:Column>
		
		<acf:Bind on="selectRow"><script>
			record = $(this).pGrid$getSelectedRecord();
			result = record;
			Dialog.get().setResult(result);
		</script></acf:Bind>
		<acf:Bind on="init"><script>
			Dialog.get().resize();
			$(window).trigger('resize');
		</script></acf:Bind>
	</acf:Grid>
</div>

<script>
function programmeSearch() {
	var parameters = $("#frm_programme_search").pSearch$getCriteria();
	console.log(parameters);
	$("#grid_arc_programme_browse").pGrid$addCriteria(parameters);
	$("#grid_arc_programme_browse").pGrid$loadRecord();
};

programmeSearch();

$("#frm_programme_search").pForm$setRelatedComboBox(${businessDepartment}, [$("#frm_programme_search #f_business_platform"), $("#frm_programme_search #f_department")]);

</script>