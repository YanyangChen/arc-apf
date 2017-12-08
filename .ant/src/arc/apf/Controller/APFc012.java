package arc.apf.Controller;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import acf.acf.Abstract.ACFaAppController;
import acf.acf.Database.ACFdSQLAssDelete;
import acf.acf.Database.ACFdSQLAssInsert;
import acf.acf.Database.ACFdSQLAssSelect;
import acf.acf.Database.ACFdSQLAssUpdate;
import acf.acf.Database.ACFdSQLRule;
import acf.acf.Database.ACFdSQLRule.RuleCase;
import acf.acf.Database.ACFdSQLRule.RuleCondition;
import acf.acf.General.annotation.ACFgAuditKey;
import acf.acf.General.annotation.ACFgFunction;
import acf.acf.General.annotation.ACFgTransaction;
import acf.acf.General.core.ACFgRawModel;
import acf.acf.General.core.ACFgRequestParameters;
import acf.acf.General.core.ACFgResponseParameters;
import acf.acf.General.core.ACFgSearch;
import acf.acf.Interface.ACFiCallback;
import acf.acf.Interface.ACFiSQLAssWriteInterface;
import acf.acf.Static.ACFtUtility;
import arc.apf.Dao.ARCoProgrammeMaster;
import arc.apf.Dao.ARCoProgrammeTransferHistory;
import arc.apf.Dao.ARCoSection;
import arc.apf.Model.ARCmItemInventory;
import arc.apf.Model.ARCmPhotoConsumptionHeader;
import arc.apf.Model.ARCmProgrammeMaster;
import arc.apf.Model.ARCmProgrammeTransferHistory;
import arc.apf.Service.ARCsModel;
import arc.apf.Service.ARCsProgrammeMaster;
import arc.apf.Service.ARCsProgrammeTransferHistory;
import arc.apf.Service.ARCsSection;
import arc.apf.Static.APFtMapping;

@Controller  //testing rtc deliver and accept 06
@Scope("session")
@ACFgFunction(id="APFF012")
@RequestMapping(value=APFtMapping.APFF012)

public class APFc012 extends ACFaAppController{
	
	   @ACFgAuditKey String programme_no;
	   @ACFgAuditKey String from_programme_no;
	   @ACFgAuditKey Timestamp transfer_date;	
	   
	   @Autowired ARCsProgrammeTransferHistory ProgrammeTransferHistoryService;
	   @Autowired ARCsProgrammeMaster ProgrammeMasterService;
	   
	   @Autowired ARCoProgrammeTransferHistory ProgrammeTransferHistoryDao;
	   @Autowired ARCoProgrammeMaster ProgrammeMasterDao;
	   
	   String prog_head;

    private class Search extends ACFgSearch {
        public Search() {
            super();
            this.setKey("transfer_date","from_programme_no");
            StringBuilder sb = new StringBuilder()
			.append("select * from ("
					+ "select h.from_programme_no, h.to_programme_no, h.transfer_date as transfer_date, h.remarks, p1.programme_name, p2.programme_name, "
					+ "h.from_programme_no || ' ' || p1.programme_name as pgftext, h.to_programme_no || ' ' || p2.programme_name pgttext "
					+ "from arc_programme_transfer_history h, arc_programme_master p1, arc_programme_master p2 "
					+ "where h.from_programme_no = p1.programme_no and h.to_programme_no = p2.programme_no"
					+ ")");
            this.setCustomSQL(sb.toString());
            //addRule(new ACFdSQLRule("programme_no", RuleCondition.EQ, null, RuleCase.Sensitive)); 
           
        }// ACF will forward the content to client and post to the grid which ID equals to “grid_browse”.
        
        @Override
        public Search setValues(ACFgRequestParameters param) throws Exception { //use the search class to setup an object
            super.setValues(param);// param is a object, "Search" 's mother class passed
                if(!param.isEmptyOrNull("transfer_from_date")) {
                wheres.and("transfer_date", ACFdSQLRule.RuleCondition.GE, param.get("transfer_from_date", Timestamp.class));
                }
                if(!param.isEmptyOrNull("transfer_to_date")) {
                wheres.and("transfer_date", ACFdSQLRule.RuleCondition.LT, new Timestamp(param.get("transfer_to_date", Long.class) + 24*60*60*1000));
                }
                
                if(!param.isEmptyOrNull("s_programme_no")) prog_head = (param.get("s_programme_no", String.class)).substring(0,3);
                
                if((!param.isEmptyOrNull("s_programme_no")) && prog_head.matches("999")) {  // temporary programme
                    wheres.and("from_programme_no", ACFdSQLRule.RuleCondition.EQ, param.get("s_programme_no", String.class));
                }

                if((!param.isEmptyOrNull("s_programme_no")) && !prog_head.matches("999")){
                    wheres.and("to_programme_no", ACFdSQLRule.RuleCondition.EQ, param.get("s_programme_no", String.class));
                }
            return this;
        }
        
    }
    Search search = new Search();
		

@RequestMapping(value=APFtMapping.APFF012_MAIN, method=RequestMethod.GET)
public String main(ModelMap model) throws Exception {
    model.addAttribute("ProgrammeNoAndName", ProgrammeMasterService.getProgNamePairs()); //acf's function, get data from ACFDB
    
   return view();
	
}

public static Timestamp getDefaultTimestamp(){
	return getDateOnly(getTimestamp(1900, Calendar.JANUARY, 1));
}
	
public static Timestamp getDateOnly(Timestamp timestamp){
	Date datetime = new Date(timestamp.getTime());
	return new Timestamp(DateUtils.truncate(datetime, Calendar.DATE).getTime());
}

public static Timestamp getTimestamp(int year, int month, int date){
	Calendar c = new GregorianCalendar(year, month, date);
	return new Timestamp(c.getTimeInMillis());
}

@RequestMapping(value=APFtMapping.APFF012_TRANSFER_TO_PGM_AJAX, method=RequestMethod.POST)
@ResponseBody
public ACFgResponseParameters getProgramme(@RequestBody ACFgRequestParameters param) throws Exception {
    setAuditKey("programme_no", param.get("programme_no", String.class));
    getResponseParameters().put("programme_fields",         ProgrammeMasterService.getToProgrammeFields((param.get("programme_no", String.class))));

    return getResponseParameters();
	
}

@RequestMapping(value=APFtMapping.APFF012_NEW_TRANSFER_TO_PGM_AJAX, method=RequestMethod.POST)
@ResponseBody
public ACFgResponseParameters getProgrammeForNew(@RequestBody ACFgRequestParameters param) throws Exception {
    setAuditKey("programme_n", param.get("programme_n", String.class));
    getResponseParameters().put("programme_fields",         ProgrammeMasterService.getToProgrammeFields((param.get("programme_n", String.class))));

    return getResponseParameters();
	
}

@RequestMapping(value=APFtMapping.APFF012_SEARCH_AJAX, method=RequestMethod.POST)
@ResponseBody
public ACFgResponseParameters getGrid(@RequestBody ACFgRequestParameters param) throws Exception {
    //The method getGrid responds to AJAX by obtain the Search JSON result and put in variable “grid_browse”.
    // ACF will forward the content to client and post to the grid which ID equals to “grid_browse”.
    search.setConnection(getConnection("ARCDB")); //get connection to the database
    search.setValues(param);
    search.setFocus(transfer_date,from_programme_no); //set two keys
    System.out.println(param);

    return new ACFgResponseParameters().set("grid_browse", search.getGridResult()); // can only be called once, otherwise reset parameter
}

@RequestMapping(value=APFtMapping.APFF012_GET_FORM_AJAX, method=RequestMethod.POST)
@ResponseBody
public ACFgResponseParameters getForm(@RequestBody ACFgRequestParameters param) throws Exception {
	transfer_date = param.get("transfer_date", Timestamp.class); //pick the value of parameter “func_id” from client
    from_programme_no = param.get("from_programme_no", String.class); //pick the value of parameter “func_id” from client
    return new ACFgResponseParameters().set("frm_main", ProgrammeTransferHistoryDao.selectItem(transfer_date,from_programme_no)); //change dao here //set two keys!!
}

@ACFgTransaction
@RequestMapping(value=APFtMapping.APFF012_SAVE_AJAX, method=RequestMethod.POST)
@ResponseBody
public ACFgResponseParameters save(@RequestBody ACFgRequestParameters param) throws Exception { //function in the upper right "save" button
  //the controller obtains the changes of form data 
	
    List<ARCmProgrammeTransferHistory> amendments = param.getList("form", ARCmProgrammeTransferHistory.class);
    
    final Timestamp defaultTimestamp = getDefaultTimestamp();
    
    ARCmProgrammeTransferHistory amendhdr = amendments.get(0);
    final String trf_fr_prog_no = amendhdr.from_programme_no;
    final String trf_to_prog_no = amendhdr.to_programme_no;
    final Timestamp trf_date = amendhdr.transfer_date;
    final String trf_remarks = amendhdr.remarks;
    final String trf_status_p = "P";
    final String trf_status_a = "A";
    
    ARCmProgrammeTransferHistory lastItem = ProgrammeTransferHistoryDao.saveItems(amendments, new ACFiSQLAssWriteInterface<ARCmProgrammeTransferHistory>(){
        
        //interface for the related functions
        @Override
        public boolean insert(ARCmProgrammeTransferHistory newItem, ACFdSQLAssInsert ass) throws Exception {

        	ass.setAfterExecute(new ACFiCallback() {
                @Override
                public void callback() throws Exception {
             
                }
            });
        	
			ARCmProgrammeMaster before_fr = ProgrammeMasterDao.selectItem(trf_fr_prog_no);
			ProgrammeMasterService.updateFromTransferStatus(before_fr, trf_status_p, trf_to_prog_no, trf_date, trf_remarks);
			
			ARCmProgrammeMaster before_to = ProgrammeMasterDao.selectItem(trf_to_prog_no);
			ProgrammeMasterService.updateToTransferStatus(before_to, trf_status_p, trf_fr_prog_no, trf_date, trf_remarks);
       	
            return false;
        }


		@Override
        public boolean update(ARCmProgrammeTransferHistory oldItem, final ARCmProgrammeTransferHistory newItem, ACFdSQLAssUpdate ass) throws Exception {

        	ass.setAfterExecute(new ACFiCallback() {
               
				@Override
                public void callback() throws Exception {

                }
            });
        	        	
            return false;
        }

        @Override
        public boolean delete(ARCmProgrammeTransferHistory oldItem, ACFdSQLAssDelete ass) throws Exception {
        	
			ARCmProgrammeMaster before_fr = ProgrammeMasterDao.selectItem(trf_fr_prog_no);
			ProgrammeMasterService.updateFromTransferStatus(before_fr, trf_status_a,"", defaultTimestamp,"");
			
			ARCmProgrammeMaster before_to = ProgrammeMasterDao.selectItem(trf_to_prog_no);
			ProgrammeMasterService.updateToTransferStatus(before_to, trf_status_a,"", defaultTimestamp,"");

        	
            return false;
        }
    });
    
    return new ACFgResponseParameters();
}
}