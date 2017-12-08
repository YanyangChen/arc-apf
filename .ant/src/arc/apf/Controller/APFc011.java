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


//import cal.exe.Model.EXEmFunction;
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
import acf.acf.General.core.ACFgRequestParameters;
import acf.acf.General.core.ACFgResponseParameters;
import acf.acf.General.core.ACFgSearch;
import acf.acf.Interface.ACFiCallback;
import acf.acf.Interface.ACFiSQLAssWriteInterface;
import acf.acf.Service.ACFsList;
import acf.acf.Static.ACFtUtility;
import arc.apf.Dao.ARCoDirectBudget;
import arc.apf.Dao.ARCoDirectBudgetDetails;
import arc.apf.Dao.ARCoIndirectBudget;
import arc.apf.Dao.ARCoLabourType;
import arc.apf.Dao.ARCoProgrammeMaster;
import arc.apf.Dao.ARCoSection;
import arc.apf.Model.ARCmCPLPgmBasic;
import arc.apf.Model.ARCmDirectBudget;
import arc.apf.Model.ARCmDirectBudgetDetails;
import arc.apf.Model.ARCmIndirectBudget;
import arc.apf.Model.ARCmPPRLocalProd;
//import arc.apf.Model.ARCmPPRPgmBasic;
import arc.apf.Model.ARCmPPRPgmBasicHist;
import arc.apf.Model.ARCmPPRPgmCasting;
import arc.apf.Model.ARCmProgrammeMaster;
import arc.apf.Service.ARCsAccountAllocation;
import arc.apf.Service.ARCsBusinessPlatform;
import arc.apf.Service.ARCsCPLPgmBasic;
import arc.apf.Service.ARCsIndirectBudget;
import arc.apf.Service.ARCsModel;
import arc.apf.Service.ARCsPPRLocalProd;
//import arc.apf.Service.ARCsPPRPgmBasic;
import arc.apf.Service.ARCsPPRPgmBasicHist;
import arc.apf.Service.ARCsPPRPgmCasting;
import arc.apf.Service.ARCsProgrammeMaster;
import arc.apf.Service.ARCsSection;
import arc.apf.Static.APFtMapping;


@Controller
@Scope("session")
@ACFgFunction(id="APFF011")
@RequestMapping(value=APFtMapping.APFF011)
public class APFc011 extends ACFaAppController {

    //protected static final ACFaBaseDao<APFmLocation> SectionDao = null;
    @Autowired ARCsModel moduleService;
    @Autowired ACFsList ListService;
    @Autowired ARCsProgrammeMaster ProgrammeMasterService;
    @Autowired ARCsBusinessPlatform BusinessPlatformService;
    @Autowired ARCsSection SectionService;
    @Autowired ARCsIndirectBudget IndirectBudgeService;
    @Autowired ARCsAccountAllocation AccountAllocationService;
    @Autowired ARCsCPLPgmBasic pprPgmBasicService;
    @Autowired ARCsPPRLocalProd pprLocalProdService;
    @Autowired ARCsPPRPgmBasicHist pprPgmBasicHistService;
    @Autowired ARCsPPRPgmCasting pprPgmCastingService;
    @Autowired ARCsSection sectionService;
    
    
   // @Autowired ACFoFunction functionDao;
    @Autowired ARCoLabourType LabourDao;
    @Autowired ARCoProgrammeMaster ProgrammeMasterDao;
    @Autowired ARCoIndirectBudget IndirectBudgetDao;
    @Autowired ARCoSection SectionDao;
    @Autowired ARCoDirectBudgetDetails DirectBudgetDetailsDao;
    @Autowired ARCoDirectBudget DirectBudgetDao;
  //  @Autowired APFoSection sectionDao;
   // @Autowired APFsFuncGp funcGpService; //click the object and click import
    @ACFgAuditKey String labour_type;
    @ACFgAuditKey String programme_no;
   // @ACFgAuditKey String APF_PROGRAMME_MASTER;
 
    
	@ACFgAuditKey String business_platform;	  // string fields for ARC programme search dialog,CN,2017/05/15
	@ACFgAuditKey String department;		
	@ACFgAuditKey String programme_name;	
	@ACFgAuditKey String chinese_programme_name;
	@ACFgAuditKey String programme_type;

        private class Search extends ACFgSearch {
        public Search() {
            super();
            setModel(ARCmProgrammeMaster.class);
            addRule(new ACFdSQLRule("programme_no", RuleCondition.EQ, null, RuleCase.Insensitive));
            addRule(new ACFdSQLRule("programme_name", RuleCondition._LIKE_, null, RuleCase.Insensitive));
            addRule(new ACFdSQLRule("chinese_programme_name", RuleCondition._LIKE_, null, RuleCase.Insensitive));
            addRule(new ACFdSQLRule("business_platform", RuleCondition.EQ, null, RuleCase.Insensitive));
            addRule(new ACFdSQLRule("department", RuleCondition.EQ, null, RuleCase.Insensitive));
            //addRule(new ACFdSQLRule("location_name", RuleCondition.EQ, null, RuleCase.Insensitive));
        }
        
       
        
    }
        
        private class SearchSectionAndIndirectBudget extends ACFgSearch {
    		public SearchSectionAndIndirectBudget() {
    			super();
    			this.setKey("programme_no","section_id");
    			
    			StringBuilder sb = new StringBuilder()
    				.append("select * from (select PM.programme_no, IB.section_id, IB.modified_at, IB.created_at, IB.created_by, concat(IB.section_id,'-'||IB.sub_section_id) sub_section_id, IB.indirect_budget_amount, IB.indirect_budget_hour ")
    				.append("from arc_programme_master PM, arc_indirect_budget IB ")
    				.append("where PM.programme_no = IB.programme_no)");
    			this.setCustomSQL(sb.toString());
    			this.addRule(new ACFdSQLRule("programme_no", RuleCondition.EQ));
    		}
    	}
        
        private class SearchProgramme extends ACFgSearch {
            public SearchProgramme() {
                super();
                setModel(ARCmCPLPgmBasic.class);
                addRule(new ACFdSQLRule("pgm_num", RuleCondition.EQ, null, RuleCase.Insensitive));
                addRule(new ACFdSQLRule("clean_eng_title", RuleCondition._LIKE_, null, RuleCase.Insensitive));
                addRule(new ACFdSQLRule("clean_chi_title", RuleCondition._LIKE_, null, RuleCase.Insensitive));
            }
    }

    	private class SearchARCPgm extends ACFgSearch {  // for ARC programme search dialog,CN,2017/05/15
    		public SearchARCPgm() {
    			super();
    			   			
    	        setModel(ARCmProgrammeMaster.class);
    			addRule(new ACFdSQLRule("programme_no", RuleCondition.EQ, null, RuleCase.Insensitive));
    			addRule(new ACFdSQLRule("programme_name", RuleCondition._LIKE_, null, RuleCase.Insensitive));
    			addRule(new ACFdSQLRule("chinese_programme_name", RuleCondition._LIKE_, null, RuleCase.Insensitive));
    			addRule(new ACFdSQLRule("business_platform", RuleCondition.EQ, null, RuleCase.Insensitive));
    			addRule(new ACFdSQLRule("department", RuleCondition.EQ, null, RuleCase.Insensitive));
    			addRule(new ACFdSQLRule("programme_type", RuleCondition.EQ, null, RuleCase.Insensitive));
    		}		
 
            @Override
            public SearchARCPgm setValues(ACFgRequestParameters param) throws Exception { //use the search class to setup an object
                super.setValues(param);
                	wheres.and("transfer_status", ACFdSQLRule.RuleCondition.NE,"I",RuleCase.Insensitive);
                	
                    if(!param.isEmptyOrNull("temp_on_off"))
                    wheres.and("programme_no", ACFdSQLRule.RuleCondition.LIKE_,param.get("temp_on_off", String.class));
     
                return this;
    		}	
    		
    }    
        
    Search search = new Search();
    @RequestMapping(value=APFtMapping.APFF011_MAIN, method=RequestMethod.GET)
    
    //model is a spring framework object
    public String main(ModelMap model) throws Exception {
        
        model.addAttribute("ProgrammeNo", ProgrammeMasterService.getProgrammeNo()); 
        model.addAttribute("BusinessPlatform", BusinessPlatformService.getBBusinessPlatform());
        model.addAttribute("Department", BusinessPlatformService.getBDepartment());
        model.addAttribute("BDepartment", BusinessPlatformService.getBDepartment());
        model.addAttribute("BBusinessPlatform", BusinessPlatformService.getBBusinessPlatform());
        model.addAttribute("ProgrammeType", ListService.getListValuePairs("APF_PROGRAMME_MASTER")); //key must in quote
        model.addAttribute("GetSectionId", SectionService.getSectionId()); 
        model.addAttribute("GetSubSectionId",  SectionService.getSubSectionId()); //key must in quote
        model.addAttribute("GetAccountAllocation", AccountAllocationService.getBudgetAccountAllocation()); //key must in quote
        model.addAttribute("SubSectionAndSectionId", sectionService.getSubSectionAndSectionId()); //key must in quote
        model.addAttribute("businessDepartment", BusinessPlatformService.getAllBusinessDepartmentValue());
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
	    
    
    ////form maintenance here, how to modify? imoprt apff001.java and remove grid in jsp file
    @RequestMapping(value=APFtMapping.APFF011_SEARCH_AJAX, method=RequestMethod.POST)
    @ResponseBody
     public ACFgResponseParameters getGrid(@RequestBody ACFgRequestParameters param) throws Exception {
         search = new Search();
         
            search.setConnection(getConnection("ARCDB")); //get connection to the database
            search.setValues(param);
            search.setFocus(programme_no);
            return new ACFgResponseParameters().set("grid_browse", search.getGridResult()); // can only be called once, otherwise reset parameter
        }
    
    @RequestMapping(value=APFtMapping.APFF011_DIRECT_BUDGET, method=RequestMethod.POST)
    @ResponseBody
     public ACFgResponseParameters getDirectBudget(@RequestBody ACFgRequestParameters param) throws Exception {
    	
 //-------------------o/s------------   	
    	ACFdSQLAssSelect select = new ACFdSQLAssSelect();
      select.setCustomSQL("select * from (select PM.programme_no, DB.direct_budget_description, DB.direct_budget_amount, DB.sequence_no, DB.modified_at, DB.modified_by, DB.created_at, DB.created_by "
              + "from arc_programme_master PM, arc_direct_budget DB "
              + "where PM.programme_no = DB.programme_no)");
      select.setKey("programme_no");
      select.wheres.and("programme_no", programme_no);
      //select.orders.put("seq", true);
      return getResponseParameters().set("direct_browse", select.executeGridQuery(getConnection("ARCDB"), param));
      
//--------------------------------------
        }
    
    @RequestMapping(value=APFtMapping.APFF011_DETAIL_DIRECT_BUDGET, method=RequestMethod.POST)
    @ResponseBody
     public ACFgResponseParameters getDetailDirectBudget(@RequestBody ACFgRequestParameters param) throws Exception {
        ACFdSQLAssSelect select = new ACFdSQLAssSelect();

      
      select.setCustomSQL("select distinct * from (select AA.budget_account_description, AA.budget_account_allocation, DBD.direct_budget_amount, DBD.modified_at, "
              + "DBD.created_at, DBD.created_by, DBD.modified_by, DBD.account_allocation, DBD.programme_no "
              + "from arc_direct_budget_details DBD, arc_account_allocation AA "
              + "where AA.budget_account_allocation = DBD.account_allocation "
              + ")");

        
      select.setKey("programme_no","budget_account_allocation");
      select.wheres.and("programme_no", programme_no);
      //select.orders.put("seq", true);
      return getResponseParameters().set("detail_budget_browse", select.executeGridQuery(getConnection("ARCDB"), param));
        }
    
    
    @RequestMapping(value=APFtMapping.APFF011_SECTION_AND_INDIRECT_BUDGET, method=RequestMethod.POST)
    @ResponseBody
     public ACFgResponseParameters getSectionAndIndirectBudget(@RequestBody ACFgRequestParameters param) throws Exception {
    	//composition
    	SearchSectionAndIndirectBudget search = new SearchSectionAndIndirectBudget();
    	search.setConnection(getConnection("ARCDB"));
    	search.setKey("section_id","sub_section_id");
    	//pass by reference pass search parameters
    	param.put("programme_no", programme_no);
    	
    	//accept parameter object for search object
    	search.setValues(param);
    	
    	//get the controller's responser
		ACFgResponseParameters resParam = this.getResponseParameters();
		
		//pass search object's result to the responser
		resParam.set("indirect_browse", search.getGridResult());
		
		//let responser refresh the web site pass value to jsp
		return resParam;
        }
    
    
    
    
     @RequestMapping(value=APFtMapping.APFF011_GET_FORM_AJAX, method=RequestMethod.POST)
        @ResponseBody
        public ACFgResponseParameters getForm(@RequestBody ACFgRequestParameters param) throws Exception {
         programme_no = param.get("programme_no", String.class); 
         
         getSectionAndIndirectBudget(param.getRequestParameter("indirect_browse"));
         getDirectBudget(param.getRequestParameter("direct_browse"));
         getDetailDirectBudget(param.getRequestParameter("detail_budget_browse"));
            return getResponseParameters().set("frm_main", ProgrammeMasterDao.selectItem(programme_no)); //change dao here
        }
     
     @RequestMapping(value=APFtMapping.APFF011_CPL_PROG_SEARCH_AJAX, method=RequestMethod.POST)
     @ResponseBody
     public ACFgResponseParameters getCPLProgGrid(@RequestBody ACFgRequestParameters param) throws Exception {
         SearchProgramme searchPgm = new SearchProgramme();
         searchPgm.setConnection(getConnection("CPLDB"));
         searchPgm.setValues(param);//set parameters of the table to fit for searching rules
         searchPgm.setFocus(programme_no);
         
         return new ACFgResponseParameters().set("grid_cpl_programme_browse", searchPgm.getGridResult());
     }     
     
     @RequestMapping(value=APFtMapping.APFF011_GET_CPL_PROGRAMME_AJAX, method=RequestMethod.POST)
     @ResponseBody
     public ACFgResponseParameters getCPLProgramme(@RequestBody ACFgRequestParameters param) throws Exception {
         ACFgResponseParameters resParam = new ACFgResponseParameters();
         
         programme_no = param.get("programme_no", String.class);
 
         BigDecimal pgm_num = programme_no.equals("") ? null : new BigDecimal(programme_no);
         
         ARCmCPLPgmBasic pgm = pprPgmBasicService.selectItemByPgmNum(pgm_num);
         
         ARCmPPRLocalProd prod = pprLocalProdService.selectItemByPgmNum(pgm_num);
         
         ARCmPPRPgmCasting cast = pprPgmCastingService.selectItemByPgmNum(pgm_num);
         
         ARCmPPRPgmBasicHist pgmHist = pprPgmBasicHistService.getLatestHistory(programme_no);
         
         resParam.set("pgm", pgm);
         resParam.set("prod", prod);
         resParam.set("cast", cast);
         resParam.set("pgmHist", pgmHist);
         
         return resParam;
     }

 	@RequestMapping(value=APFtMapping.APFF011_SEARCH_ARC_PROGRAMME, method=RequestMethod.GET)
 	public String a_index(ModelMap model) throws Exception {  // for ARC programme search dialog,CN,2017/05/15

 		model.addAttribute("businessPlatform", BusinessPlatformService.getAllBusinessPlatformValue());
 		model.addAttribute("Department", BusinessPlatformService.getAllDepartmentValue());
 		model.addAttribute("businessDepartment", BusinessPlatformService.getAllBusinessDepartmentValue());
 		 model.addAttribute("SubSectionAndSectionId", SectionService.getSubSectionAndSectionId()); 

 		return view();
 	}
 	
 	
     @RequestMapping(value=APFtMapping.APFF011_GRID_ARC_PROGRAMME_AJAX, method=RequestMethod.POST)
     @ResponseBody
     public ACFgResponseParameters getARCProgGrid(@RequestBody ACFgRequestParameters param) throws Exception {  // for ARC programme search dialog,CN,2017/05/15
 		
         SearchARCPgm searchARC = new SearchARCPgm();
         searchARC.setConnection(getConnection("ARCDB"));
         
         searchARC.setValues(param);//set parameters of the table to fit for searching rules
         searchARC.setFocus(programme_no);

         return new ACFgResponseParameters().set("grid_arc_programme_browse", searchARC.getGridResult());
     }    	
     
     @ACFgTransaction
        @RequestMapping(value=APFtMapping.APFF011_SAVE_AJAX, method=RequestMethod.POST)
        @ResponseBody
        public ACFgResponseParameters save(@RequestBody ACFgRequestParameters param) throws Exception { //function in the upper right "save" button
          //the controller obtains the changes of form data 
            List<ARCmProgrammeMaster> amendments = param.getList("form", ARCmProgrammeMaster.class);
            final List<ARCmIndirectBudget> IndirectBudgetamendments = param.getList("IndirectBudget", ARCmIndirectBudget.class);
            final List<ARCmDirectBudget> DirectBudgetamendments = param.getList("DirectBudget", ARCmDirectBudget.class);
            final List<ARCmDirectBudgetDetails> DirectBudgetDetailsamendments = param.getList("AccountAllocation", ARCmDirectBudgetDetails.class);
  
            Timestamp defaultTimestamp = getDefaultTimestamp();
            
            ARCmProgrammeMaster Hdramend = amendments.get(0);
            if (Hdramend.cost_first_input_date == null) Hdramend.cost_first_input_date = defaultTimestamp;
                                   
            //and call DAO to save the changes
            ARCmProgrammeMaster lastItem = ProgrammeMasterDao.saveItems(amendments, new ACFiSQLAssWriteInterface<ARCmProgrammeMaster>(){
            	//
                
               
                //interface for the related functions
                @Override
                public boolean insert(ARCmProgrammeMaster newItem, ACFdSQLAssInsert ass) throws Exception {
                	               	
                	ass.setAfterExecute(new ACFiCallback() {
                        @Override
                        
                        public void callback() throws Exception {
                        	if (IndirectBudgetamendments != null)
                            	IndirectBudgeService.setModel(IndirectBudgetamendments);
                                IndirectBudgetDao.saveItems(IndirectBudgetamendments);
                            if (DirectBudgetamendments != null)
                                DirectBudgetDao.saveItems(DirectBudgetamendments);
                            if (DirectBudgetDetailsamendments != null)
                                DirectBudgetDetailsDao.saveItems(DirectBudgetDetailsamendments);
                          
                        }
                    });
                    return false;
                }

                @Override
                public boolean update(ARCmProgrammeMaster oldItem, ARCmProgrammeMaster newItem, ACFdSQLAssUpdate ass) throws Exception {
                	//ProgrammeMasterDao.updateItem(newItem);
                	// ass.columns.put("cost_first_input_date", ProgrammeMasterService.UpdateFirstInput(newItem.programme_no));
                	ass.setAfterExecute(new ACFiCallback() {
                        @Override
                        public void callback() throws Exception {
                            if (IndirectBudgetamendments != null){
                            	List<ARCmIndirectBudget> IndirectBudgetamendments2 = IndirectBudgetamendments;
                            	//make the incomplete record look like a table record
                            	IndirectBudgeService.setModel(IndirectBudgetamendments2);
                                IndirectBudgetDao.saveItems(IndirectBudgetamendments2);}
                            if (DirectBudgetamendments != null)
                                DirectBudgetDao.saveItems(DirectBudgetamendments);
                            
                            if (DirectBudgetDetailsamendments != null)
                                System.out.println(DirectBudgetDetailsamendments);
                            DirectBudgetDetailsDao.saveItems(DirectBudgetDetailsamendments);
                          
                        }
                    });
                    return false;
                }

                @Override
                public boolean delete(ARCmProgrammeMaster oldItem, ACFdSQLAssDelete ass) throws Exception {
                    return false;
                }
            });
            programme_no = lastItem!=null? lastItem.programme_no: null;

            return new ACFgResponseParameters();
        }
}

