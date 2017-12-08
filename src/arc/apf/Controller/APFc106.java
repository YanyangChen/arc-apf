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
import acf.acf.Database.ACFdSQLWhere;
import acf.acf.General.annotation.ACFgAuditKey;
import acf.acf.General.annotation.ACFgFunction;
import acf.acf.General.annotation.ACFgTransaction;
import acf.acf.General.core.ACFgRawModel;
import acf.acf.General.core.ACFgRequestParameters;
import acf.acf.General.core.ACFgResponseParameters;
import acf.acf.General.core.ACFgSearch;
import acf.acf.Interface.ACFiCallback;
import acf.acf.Interface.ACFiSQLAssWriteInterface;
import acf.acf.Model.ACFmGridResult;
import arc.apf.Service.ARCsLabourConsumption;
import arc.apf.Dao.ARCoLabourConsumption;
import arc.apf.Model.ARCmLabourConsumption;
import arc.apf.Service.ARCsLabourType;
import arc.apf.Service.ARCsProgrammeMaster;
import arc.apf.Service.ARCsSection;
import arc.apf.Static.APFtMapping;


@Controller
@Scope("session")
@ACFgFunction(id="APFF106")
@RequestMapping(value=APFtMapping.APFF106)
public class APFc106 extends ACFaAppController {

	@Autowired ARCoLabourConsumption labourConsumptionDao;

	@Autowired ARCsLabourType        labourTypeService;
	@Autowired ARCsProgrammeMaster   programmeMasterService;
	@Autowired ARCsSection           sectionService;
	
	String programmeno;
	String sectionval;
	BigDecimal total_l;
	BigDecimal total_p;
	
	Timestamp defaultTimestamp = getDefaultTimestamp();	
	
	private class Search extends ACFgSearch {	
		
    	String sql = null;
    	ACFdSQLAssSelect select;
				
		public Search(){
		super();
		sql= "select * from (select t1.process_date, t1.section_id, t1.programme_no, t3.programme_name, t1.from_episode_no, t1.to_episode_no, t1.job_description, " + 
		             "t2.labour_type, sum(t2.no_of_hours) as total_hours, t2.hourly_rate, t4.section_name, " +
				     "(sum(t2.no_of_hours) * t2.hourly_rate) as labour_cost " +
                     "from arc_labour_consumption t1, arc_labour_consumption_details t2, " +
				     "     arc_programme_master t3, arc_section t4 " +
                     "where t1.process_date = t2.process_date and t1.section_id = t2.section_id " +
                     "and t1.programme_no = t2.programme_no and t2.programme_no = t3.programme_no " +
                     "and t1.from_episode_no = t2.from_episode_no and t1.to_episode_no = t2.to_episode_no " +
                     "and t1.section_id = t4.section_id and t4.sub_section_id = '0' " +
                     "group by t1.process_date, t1.section_id, t1.programme_no, t3.programme_name, t1.from_episode_no, t1.to_episode_no, t1.job_description, " +
                     "t2.labour_type, t2.hourly_rate, t4.section_name)";
		setCustomSQL(sql);
        setKey("process_date","section_id","programme_no","from_episode_no","to_episode_no","labour_type");	

    	
        select = new ACFdSQLAssSelect();
		select.setCustomSQL("select (sum(labour_cost)) as total_l from (" + sql + ")");
        
		//addRule(new ACFdSQLRule("labour_type", RuleCondition.EQ,null, RuleCase.Insensitive));	
		//addRule(new ACFdSQLRule("programme_no", RuleCondition.EQ,null, RuleCase.Insensitive));	

		}

        @Override
        public Search setValues(ACFgRequestParameters param) throws Exception { //use the search class to setup an object
            super.setValues(param);// param is a object, "Search" 's mother class passed
            
            if(!param.isEmptyOrNull("labour_type")) {	          	
            wheres.and("labour_type", ACFdSQLRule.RuleCondition.EQ, param.get("labour_type", String.class));
            select.wheres.and("labour_type", ACFdSQLRule.RuleCondition.EQ, param.get("labour_type", String.class));
            }
            if(!param.isEmptyOrNull("programme_no")) {	          	
            wheres.and("programme_no", ACFdSQLRule.RuleCondition.EQ, param.get("programme_no", String.class));
            select.wheres.and("programme_no", ACFdSQLRule.RuleCondition.EQ, param.get("programme_no", String.class));
            }

            if(!param.isEmptyOrNull("select_fr_date")) {	
            //System.out.println(param.get("select_fr_date", Timestamp.class));	            	
            wheres.and("process_date", ACFdSQLRule.RuleCondition.GE, param.get("select_fr_date", Timestamp.class));
            select.wheres.and("process_date", ACFdSQLRule.RuleCondition.GE, param.get("select_fr_date", Timestamp.class));
            }
            if(!param.isEmptyOrNull("select_to_date")) {
            //System.out.println(param.get("select_to_date", Timestamp.class));	                  	
            wheres.and("process_date", ACFdSQLRule.RuleCondition.LE, new Timestamp(param.get("select_to_date", Long.class) + 24*60*60*1000));
            select.wheres.and("process_date", ACFdSQLRule.RuleCondition.LE, new Timestamp(param.get("select_to_date", Long.class) + 24*60*60*1000));
            }
            
            if(!param.isEmptyOrNull("section_id")) {	 
        		String search_section = param.get("section_id", String.class);
        		//System.out.println(search_section);
        		
        		ACFdSQLWhere w1 = new ACFdSQLWhere();
        		ACFdSQLWhere w2 = new ACFdSQLWhere();
            
        		for (String sectionid : search_section.split("\t")) {
        			//System.out.println(sectionid);
        			param.put(sectionid, sectionval);
        			w1.or("section_id", ACFdSQLRule.RuleCondition.EQ, sectionid);
        			w2.or("section_id", ACFdSQLRule.RuleCondition.EQ, sectionid);
        		}
        		wheres.and(w1);
       			select.wheres.and(w2);
        	}
            
       		List<ACFgRawModel> lg = select.executeQuery(getConnection());
       		total_l = lg.get(0).getBigDecimal("total_l");
            if (total_l == null)
                total_l = BigDecimal.ZERO;
            System.out.println("* others sum " + total_l);
            	                        
			return this;
		}			
		
	}

	private class SearchPhotoLabour extends ACFgSearch {
				
		String sql = null;
		ACFdSQLAssSelect select;

		public SearchPhotoLabour(){
		super();
		sql="select * from (select t1.input_date, t1.job_no, t1.programme_no, t3.programme_name, t1.from_episode_no, t1.to_episode_no, t1.job_description, " + 
	             "t2.labour_type, sum(t2.no_of_hours) as total_hours, t2.hourly_rate, (sum(t2.no_of_hours) * t2.hourly_rate) as photo_labour_cost " +
                "from arc_photo_consumption_header t1, arc_photo_labour_consumption t2, arc_programme_master t3 " +
                "where t1.input_date = t2.input_date and t1.job_no = t2.job_no " +
                "and t1.programme_no = t2.programme_no " +
                "and t2.programme_no = t3.programme_no " +
                "group by t1.input_date, t1.job_no, t1.programme_no, t3.programme_name, t1.from_episode_no, t1.to_episode_no, t1.job_description, " +
                "t2.labour_type, t2.hourly_rate)";
        setCustomSQL(sql);
		setKey("job_no","labour_type");
	
        select = new ACFdSQLAssSelect();
		select.setCustomSQL("select (sum(photo_labour_cost)) as total_p from (" + sql + ")");
		
		//addRule(new ACFdSQLRule("labour_type", RuleCondition.EQ,null, RuleCase.Insensitive));	
		//addRule(new ACFdSQLRule("programme_no", RuleCondition.EQ,null, RuleCase.Insensitive));	

		}

       @Override
        public SearchPhotoLabour setValues(ACFgRequestParameters param) throws Exception { //use the search class to setup an object
            super.setValues(param);
            
            if(!param.isEmptyOrNull("labour_type")) {	            	
            wheres.and("labour_type", ACFdSQLRule.RuleCondition.EQ, param.get("labour_type", String.class));
            select.wheres.and("labour_type", ACFdSQLRule.RuleCondition.EQ, param.get("labour_type", String.class));
            }            
            if(!param.isEmptyOrNull("programme_no")) {	            	
            wheres.and("programme_no", ACFdSQLRule.RuleCondition.EQ, param.get("programme_no", String.class));
            select.wheres.and("programme_no", ACFdSQLRule.RuleCondition.EQ, param.get("programme_no", String.class));
            }
            if(!param.isEmptyOrNull("select_fr_date")) {	
            //System.out.println(param.get("select_fr_date", Timestamp.class));	            	
            wheres.and("input_date", ACFdSQLRule.RuleCondition.GE, param.get("select_fr_date", Timestamp.class));
            select.wheres.and("input_date", ACFdSQLRule.RuleCondition.GE, param.get("select_fr_date", Timestamp.class));
            }
            if(!param.isEmptyOrNull("select_to_date")) {
            //System.out.println(param.get("select_to_date", Timestamp.class));	                  	
            wheres.and("input_date", ACFdSQLRule.RuleCondition.LE, new Timestamp(param.get("select_to_date", Long.class) + 24*60*60*1000));
            select.wheres.and("input_date", ACFdSQLRule.RuleCondition.LE, new Timestamp(param.get("select_to_date", Long.class) + 24*60*60*1000));
            }
             
       		List<ACFgRawModel> lg = select.executeQuery(getConnection());
       		total_p = lg.get(0).getBigDecimal("total_p");
            if (total_p == null)
                total_p = BigDecimal.ZERO;
            System.out.println("* photo sum " + total_p);
  
			return this;
		}			
		
	}
	
	@RequestMapping(value=APFtMapping.APFF106_MAIN, method=RequestMethod.GET)
	public String main(ModelMap model) throws Exception {

		model.addAttribute("sectionselect", sectionService. getSectionValueExcl349());
		model.addAttribute("labourselect", labourTypeService.getAllEffLabourType());
		
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
		
	@RequestMapping(value=APFtMapping.APFF106_GET_FORM_AJAX, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters getForm(@RequestBody ACFgRequestParameters param) throws Exception {
		
		ACFgResponseParameters resParam = this.getResponseParameters();		
		// this.getLabourConsumption(param);
		// this.getPhotoLabour(param);

		return resParam;
	}

	Search search = new Search();
	
	@RequestMapping(value=APFtMapping.APFF106_GET_LABOUR_CONSUMPTION_AJAX, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters getLabourConsumption(@RequestBody ACFgRequestParameters reqparam) throws Exception {

        java.text.DecimalFormat df = new java.text.DecimalFormat("###,##0.00");
		
		search.setConnection(getConnection("ARCDB"));
		search.setValues(reqparam);
	
		ACFgResponseParameters resParam = this.getResponseParameters();	
		
		ACFmGridResult grid = search.getGridResult();		
		this.getResponseParameters().set("grid_labour_consumption", grid);
		
		ACFgRawModel TtlOthers= new ACFgRawModel();
		TtlOthers.set("total_others", df.format(total_l));
		this.getResponseParameters().set("frm_main", TtlOthers);
		
		return resParam;

	}	

	SearchPhotoLabour searchpl = new SearchPhotoLabour();
	
	@RequestMapping(value=APFtMapping.APFF106_GET_PHOTO_LABOUR_AJAX, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters getPhotoLabour(@RequestBody ACFgRequestParameters reqparam) throws Exception {

        java.text.DecimalFormat df = new java.text.DecimalFormat("###,##0.00");
		
		searchpl.setConnection(getConnection("ARCDB"));
		searchpl.setValues(reqparam);
		
		ACFgResponseParameters resParam = this.getResponseParameters();	

		ACFmGridResult grid = searchpl.getGridResult();
		this.getResponseParameters().set("grid_photo_labour", grid);
		
		ACFgRawModel TtlPhoto= new ACFgRawModel();
		TtlPhoto.set("total_photo", df.format(total_p));
		this.getResponseParameters().set("frm_main", TtlPhoto);
		
		return resParam;
	}	
	
    @RequestMapping(value=APFtMapping.APFF106_GET_SECTION_NAME_AJAX, method=RequestMethod.POST)
    @ResponseBody
    public ACFgResponseParameters getSectionName(@RequestBody ACFgRequestParameters param) throws Exception {
 
        String section_id= param.get("section_id", String.class);
        
        getResponseParameters().put("section_name", sectionService.getSectionName(section_id));
        return getResponseParameters();
    }

    @RequestMapping(value=APFtMapping.APFF106_GET_PROGRAMME_NAME_AJAX, method=RequestMethod.POST)
    @ResponseBody
    public ACFgResponseParameters getProgrammeName(@RequestBody ACFgRequestParameters param) throws Exception {
 
        String programme_no= param.get("programme_no", String.class);
        
        getResponseParameters().put("programme_name", programmeMasterService.getProgrammeName(programme_no));
        return getResponseParameters();
    }

}