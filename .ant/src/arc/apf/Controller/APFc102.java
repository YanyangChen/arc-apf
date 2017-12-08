package arc.apf.Controller;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import acf.acf.Abstract.ACFaAppController;
import acf.acf.Database.ACFdSQLAssSelect;
import acf.acf.Database.ACFdSQLRule;
import acf.acf.Database.ACFdSQLWhere;
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
import acf.acf.Model.ACFmGridResult;
import arc.apf.Dao.ARCoBusinessPlatform;
import arc.apf.Dao.ARCoProgrammeMaster;
import arc.apf.Dao.ARCoSection;
import arc.apf.Dao.ARCoWPConsumptionItem;
import arc.apf.Dao.ARCoWPLabourConsumption;
import arc.apf.Dao.ARCoWPOtherMaterialConsumption;
import arc.apf.Model.ARCmBusinessPlatform;
import arc.apf.Model.ARCmDirectBudget;
import arc.apf.Model.ARCmDirectBudgetDetails;
import arc.apf.Model.ARCmIndirectBudget;
import arc.apf.Model.ARCmProgrammeMaster;
import arc.apf.Model.ARCmWPConsumptionItem;
import arc.apf.Model.ARCmWPLabourConsumption;
import arc.apf.Model.ARCmWPOtherMaterialConsumption;
import arc.apf.Service.ARCsModel;
import arc.apf.Service.ARCsProgrammeMaster;
import arc.apf.Service.ARCsSection;
import arc.apf.Static.APFtMapping;

@Controller  //testing rtc deliver and accept 06
@Scope("session")
@ACFgFunction(id="APFF102")
@RequestMapping(value=APFtMapping.APFF102)

public class APFc102 extends ACFaAppController{

	    String programme_no;
		String sectionval;
		BigDecimal total_item;
		BigDecimal total_others;
		BigDecimal total_labour;
		java.text.DecimalFormat df = new java.text.DecimalFormat("###,##0.00");
		   
		@Autowired ARCsProgrammeMaster programmeMasterService;
	   
		@Autowired ARCoProgrammeMaster programmeMasterDao;
		@Autowired ARCoBusinessPlatform  businessPlatformDao;
		@Autowired ARCoWPConsumptionItem WPConsumptionItemDao;
		@Autowired ARCoWPLabourConsumption LabourConsumptionDao;
		@Autowired ARCoWPOtherMaterialConsumption OtherMaterialConsumptionDao;

		@Autowired ARCsSection sectionService;
	   
		private class SearchItem extends ACFgSearch {	
			
	    	String sql = null;
	    	ACFdSQLAssSelect select;
					
			public SearchItem(){
			super();
			sql= "select * from ( select prog_no, sec_id, ac_alloc, sum(amt) as sub_amt, ac_alloc_desc, compare_date, sec_name from ("
				 + "(select b.programme_no as prog_no, a.section_id as sec_id, b.account_allocation as ac_alloc, "
				 + "sum(b.unit_cost * b.consumption_quantity) as amt, x.actual_account_description as ac_alloc_desc, "
				 + "a.completion_date as compare_date, z.section_name as sec_name "
				 + "from arc_wp_consumption_header a, "
				 + "arc_wp_consumption_item b,   "
				 + "arc_account_allocation  x,   "
				 + "arc_section  z               "
				 + "where a.cancel_indicator = 'N'    and "
				 + "a.consumption_form_no = b.consumption_form_no and "
				 + "a.completion_date = b.completion_date and "
				 + "a.programme_no = b.programme_no and "
				 + "b.account_allocation  = x.actual_account_allocation and "
				 + "a.section_id   = z.section_id  and z.sub_section_id = '0' "
				 + "group by b.programme_no, a.section_id, b.account_allocation, x.actual_account_description, a.completion_date, z.section_name) " + 
			     "union " 
				 + "(select d.programme_no as prog_no, c.section_id as sec_id, d.account_allocation as ac_alloc, "
				 + "sum(d.payment_amount) as amt, x.actual_account_description as ac_alloc_desc, "
				 + "c.request_date as compare_date, z.section_name as sec_name "
				 + "from arc_payment_request c, "
				 + "arc_payment_details d,      "
				 + "arc_account_allocation  x,  "
				 + "arc_section  z              "
				 + "where c.payment_form_no = d.payment_form_no  and "
				 + "d.account_allocation  = x.actual_account_allocation and "
				 + "c.section_id   = z.section_id  and z.sub_section_id = '0' "
				 + "group by d.programme_no, c.section_id, d.account_allocation, x.actual_account_description, c.request_date, z.section_name) " + 
			     "union " 
				 + "(select f.programme_no as prog_no, e.section_id as sec_id, f.account_allocation as ac_alloc, "
				 + "sum(f.consumption_quantity * f.unit_cost) as amt, x.actual_account_description as ac_alloc_desc, "
				 + "e.input_date as compare_date, z.section_name as sec_name "
				 + "from arc_photo_consumption_header e, "
				 + "arc_photo_consumption_item f,        "
				 + "arc_account_allocation  x,           "
				 + "arc_section  z                       "
				 + "where e.cancel_indicator = 'N'    and "
				 + "e.programme_no = f.programme_no   and "
				 + "e.job_no       = f.job_no         and "
				 + "f.account_allocation  = x.actual_account_allocation and "
				 + "e.section_id   = z.section_id  and z.sub_section_id = '0' "
				 + "group by f.programme_no, e.section_id, f.account_allocation, x.actual_account_description, e.input_date, z.section_name) " + 
			     "union " 
				 + "(select h.programme_no as prog_no, g.section_id as sec_id, h.account_allocation as ac_alloc, "
				 + "sum(h.other_material_amount) as amt, x.actual_account_description as ac_alloc_desc, "
				 + "g.input_date as compare_date, z.section_name as sec_name "
				 + "from arc_photo_consumption_header g, "
				 + "arc_photo_other_material_consumption h, "
				 + "arc_account_allocation  x,           "
				 + "arc_section  z                       "
				 + "where g.cancel_indicator = 'N'    and "
				 + "h.programme_no = g.programme_no   and "
				 + "h.job_no       = g.job_no         and "
				 + "h.input_date   = g.input_date         and "
				 + "h.account_allocation  = x.actual_account_allocation and "
				 + "g.section_id   = z.section_id  and z.sub_section_id = '0' "
				 + "group by h.programme_no, g.section_id, h.account_allocation, x.actual_account_description, g.input_date, z.section_name) "
				 + "union "
				 + "(select i.programme_no as prog_no, i.section_id as sec_id, j.account_allocation as ac_alloc, "
				 + "sum(j.other_material_amount) as amt, x.actual_account_description as ac_alloc_desc, "
				 + "i.input_date as compare_date, z.section_name as sec_name "
				 + "from arc_wp_consumption_header i, "
				 + "arc_wp_other_material_consumption  j,   "
				 + "arc_account_allocation  x,    "
				 + "arc_section  z                "
				 + "where i.cancel_indicator = 'N'    and "
				 + "i.consumption_form_no = j.consumption_form_no and "
				 + "j.account_allocation  = x.actual_account_allocation and "
				 + "i.section_id   = z.section_id  and z.sub_section_id = '0' "
				 + "group by i.programme_no, i.section_id, j.account_allocation, x.actual_account_description, i.input_date, z.section_name) "
				 + ")"
				 + "group by prog_no, sec_id, ac_alloc, ac_alloc_desc, compare_date, sec_name "
			     + " order by prog_no, sec_id, ac_alloc, compare_date ) " ;
			
			setCustomSQL(sql);
	        setKey("prog_no","sec_id","ac_alloc");	
	    	
	        select = new ACFdSQLAssSelect();
			select.setCustomSQL("select (sum(sub_amt)) as total_item from (" + sql + ")");
			

			}

	        @Override
	        public SearchItem setValues(ACFgRequestParameters param) throws Exception { 
	            super.setValues(param);// param is a object, "SearchItem" 's mother class passed
	            
	            if(!param.isEmptyOrNull("programme_no")) {	          	
	            wheres.and("prog_no", ACFdSQLRule.RuleCondition.EQ, param.get("programme_no", String.class));
	            select.wheres.and("prog_no", ACFdSQLRule.RuleCondition.EQ, param.get("programme_no", String.class));
	            }

	            if(!param.isEmptyOrNull("select_fr_date")) {	           	
	            wheres.and("compare_date", ACFdSQLRule.RuleCondition.GE, param.get("select_fr_date", Timestamp.class));
	            select.wheres.and("compare_date", ACFdSQLRule.RuleCondition.GE, param.get("select_fr_date", Timestamp.class));
	            }
	            if(!param.isEmptyOrNull("select_to_date")) {      	
	            wheres.and("compare_date", ACFdSQLRule.RuleCondition.LE, new Timestamp(param.get("select_to_date", Long.class) + 24*60*60*1000));
	            select.wheres.and("compare_date", ACFdSQLRule.RuleCondition.LE, new Timestamp(param.get("select_to_date", Long.class) + 24*60*60*1000));
	            }
	            
	            if(!param.isEmptyOrNull("section_id")) {	 
	        		String search_section = param.get("section_id", String.class);
	        		
	        		ACFdSQLWhere w1 = new ACFdSQLWhere();
	        		ACFdSQLWhere w2 = new ACFdSQLWhere();
	            
	        		for (String sectionid : search_section.split("\t")) {
	        			param.put(sectionid, sectionval);
	        			w1.or("sec_id", ACFdSQLRule.RuleCondition.EQ, sectionid);
	        			w2.or("sec_id", ACFdSQLRule.RuleCondition.EQ, sectionid);
	        		}
	        		wheres.and(w1);
	       			select.wheres.and(w2);
	        	}
	            
	       		List<ACFgRawModel> lg = select.executeQuery(getConnection());
	       		total_item = lg.get(0).getBigDecimal("total_item");
	            if (total_item == null)
	                total_item = BigDecimal.ZERO;
	            	                        
				return this;
			}			
			
		}

		private class SearchLabour extends ACFgSearch {	
			
	    	String sql = null;
	    	ACFdSQLAssSelect select;
					
			public SearchLabour(){
			super();
			
			sql= "select * from ( select prog_no, sec_id, lab_type, sum(lab_amt) as sub_lab_amt, sum(no_of_hrs) as sub_no_of_hrs, hrly_rate, lab_type_desc, compare_date, sec_name from ("
					 + "(select b.programme_no as prog_no, a.section_id as sec_id, b.labour_type as lab_type, "
					 + "sum(b.hourly_rate * b.no_of_hours) as lab_amt, sum(b.no_of_hours) as no_of_hrs, b.hourly_rate as hrly_rate, "
					 + "y.labour_type_description as lab_type_desc, a.completion_date as compare_date, z.section_name as sec_name "
					 + "from arc_wp_consumption_header a, "
					 + "arc_wp_labour_consumption b,   "
					 + "arc_labour_type y,     "
					 + "arc_section  z         "
					 + "where a.cancel_indicator = 'N'    and "
					 + "a.consumption_form_no = b.consumption_form_no and "
					 + "b.labour_type  = y.labour_type and "
					 + "a.section_id   = z.section_id  and z.sub_section_id = '0' "
					 + "group by b.programme_no, a.section_id, b.labour_type, y.labour_type_description, b.hourly_rate, a.completion_date, z.section_name) " + 
				     "union " 
					 + "(select d.programme_no as prog_no, c.section_id as sec_id, d.labour_type as lab_type, "
					 + "sum(d.hourly_rate * d.no_of_hours) as lab_amt, sum(d.no_of_hours) as no_of_hrs, d.hourly_rate as hrly_rate,  "
					 + "y.labour_type_description as lab_type_desc, c.process_date as compare_date, z.section_name as sec_name "
					 + "from arc_labour_consumption c, "
					 + "arc_labour_consumption_details d, "
					 + "arc_labour_type y,    "
					 + "arc_section  z        "
					 + "where c.cancel_indicator = 'N'  and "
					 + "c.programme_no = d.programme_no and "
					 + "c.process_date = d.process_date and "
					 + "c.from_episode_no = d.from_episode_no and "
					 + "c.to_episode_no = d.to_episode_no and "
					 + "d.labour_type  = y.labour_type  and "
					 + "c.section_id   = d.section_id   and c.section_id   = z.section_id  and z.sub_section_id = '0'  "
					 + "group by d.programme_no, c.section_id, d.labour_type, y.labour_type_description, d.hourly_rate, c.process_date, z.section_name) " + 
				     "union " 
					 + "(select f.programme_no as prog_no, e.section_id as sec_id, f.labour_type as lab_type, "
					 + "sum(f.hourly_rate * f.no_of_hours) as lab_amt, sum(f.no_of_hours) as no_of_hrs, f.hourly_rate as hrly_rate,   "
					 + "y.labour_type_description as lab_type_desc, e.input_date as compare_date, z.section_name as sec_name "
					 + "from arc_photo_consumption_header e, "
					 + "arc_photo_labour_consumption f,      "
					 + "arc_labour_type y,                   "
					 + "arc_section z                        "
					 + "where e.cancel_indicator = 'N'    and "
					 + "e.programme_no = f.programme_no   and "
					 + "e.job_no       = f.job_no         and "
					 + "f.labour_type  = y.labour_type    and "
					 + "e.section_id   = z.section_id  and z.sub_section_id = '0' "
					 + "group by f.programme_no, e.section_id, f.labour_type, y.labour_type_description, f.hourly_rate, e.input_date, z.section_name) "  
					 + ")"
					 + "group by prog_no, sec_id, lab_type, lab_type_desc, hrly_rate, compare_date, sec_name "
				     + " order by prog_no, sec_id, lab_type, compare_date ) " ;

		
			setCustomSQL(sql);
	        setKey("prog_no","sec_id","lab_type", "hrly_rate");
	        
	         select = new ACFdSQLAssSelect();
			 select.setCustomSQL("select (sum(sub_lab_amt)) as total_labour from (" + sql + ")");

			}

	        @Override
	        public SearchLabour setValues(ACFgRequestParameters param) throws Exception { 
	            super.setValues(param);// param is a object, "SearchLabour" 's mother class passed
	            
	            if(!param.isEmptyOrNull("programme_no")) {	          	
	            wheres.and("prog_no", ACFdSQLRule.RuleCondition.EQ, param.get("programme_no", String.class));
	            select.wheres.and("prog_no", ACFdSQLRule.RuleCondition.EQ, param.get("programme_no", String.class));
	            }

	            if(!param.isEmptyOrNull("select_fr_date")) {	           	
	            wheres.and("compare_date", ACFdSQLRule.RuleCondition.GE, param.get("select_fr_date", Timestamp.class));
	            select.wheres.and("compare_date", ACFdSQLRule.RuleCondition.GE, param.get("select_fr_date", Timestamp.class));
	            }
	            if(!param.isEmptyOrNull("select_to_date")) {      	
	            wheres.and("compare_date", ACFdSQLRule.RuleCondition.LE, new Timestamp(param.get("select_to_date", Long.class) + 24*60*60*1000));
	            select.wheres.and("compare_date", ACFdSQLRule.RuleCondition.LE, new Timestamp(param.get("select_to_date", Long.class) + 24*60*60*1000));
	            }
	            
	            if(!param.isEmptyOrNull("section_id")) {	 
	        		String search_section = param.get("section_id", String.class);
	        		
	        		ACFdSQLWhere w1 = new ACFdSQLWhere();
	        		ACFdSQLWhere w2 = new ACFdSQLWhere();
	            
	        		for (String sectionid : search_section.split("\t")) {
	        			param.put(sectionid, sectionval);
	        			w1.or("sec_id", ACFdSQLRule.RuleCondition.EQ, sectionid);
	        			w2.or("sec_id", ACFdSQLRule.RuleCondition.EQ, sectionid);
	        		}
	        		wheres.and(w1);
	       			select.wheres.and(w2);
	        	}
	            
	       		List<ACFgRawModel> lg = select.executeQuery(getConnection());
	       		total_labour = lg.get(0).getBigDecimal("total_labour");
	            if (total_labour == null)
	                total_labour = BigDecimal.ZERO;
	            	                        
				return this;
			}			
			
		}
		
		@RequestMapping(value=APFtMapping.APFF102_MAIN, method=RequestMethod.GET)
		public String main(ModelMap model) throws Exception {
	
			model.addAttribute("sectionselect", sectionService.getAllSectionValue());
			return view();
	
		}



		@RequestMapping(value=APFtMapping.APFF102_GET_FORM_AJAX, method=RequestMethod.POST)
		@ResponseBody
		public ACFgResponseParameters getForm(@RequestBody ACFgRequestParameters param) throws Exception {
			programme_no = param.get("programme_no", String.class); 

			//if(!param.isEmptyOrNull("programme_no")){
			//	getItemTable(param); 
			//	getAcTable(param); 
			//	getLabourTable(param);}
    
			return getResponseParameters().set("frm_main", programmeMasterDao.selectItem(programme_no)); //change dao here //set two keys!!
		}

		SearchItem searchItem = new SearchItem();
		
		@RequestMapping(value=APFtMapping.APFF102_GET_ITEM_AJAX, method=RequestMethod.POST) //get rows of the second grid
		@ResponseBody
		public ACFgResponseParameters getItemTable(@RequestBody ACFgRequestParameters reqparam) throws Exception {                                
			searchItem.setConnection(getConnection("ARCDB"));
			searchItem.setValues(reqparam);
	
			ACFgResponseParameters resParam = this.getResponseParameters();	
		
			ACFmGridResult grid = searchItem.getGridResult();		
			this.getResponseParameters().set("grid_item_consumption", grid);
		
			ACFgRawModel TtlItem= new ACFgRawModel();
			TtlItem.set("total_item", df.format(total_item));
			this.getResponseParameters().set("frm_main", TtlItem);
		
			return resParam;
     
		}



		SearchLabour searchLabour = new SearchLabour();

		@RequestMapping(value=APFtMapping.APFF102_GET_LABOUR_AJAX, method=RequestMethod.POST) //get rows of the third grid
		@ResponseBody
		public ACFgResponseParameters getLabourTable(@RequestBody ACFgRequestParameters reqparam) throws Exception {                                
			searchLabour.setConnection(getConnection("ARCDB"));
			searchLabour.setValues(reqparam);
	
			ACFgResponseParameters resParam = this.getResponseParameters();	
		
			ACFmGridResult grid = searchLabour.getGridResult();		
			this.getResponseParameters().set("grid_labour_consumption", grid);
		
			ACFgRawModel TtlLabour= new ACFgRawModel();
			TtlLabour.set("total_labour", df.format(total_labour));
			this.getResponseParameters().set("frm_main", TtlLabour);
		
			return resParam;
		}

		@RequestMapping(value=APFtMapping.APFF102_GET_PROG_BP_AJAX, method=RequestMethod.POST)
		@ResponseBody
		public ACFgResponseParameters getProgBusiPlatform(@RequestBody ACFgRequestParameters param) throws Exception {
			ACFgResponseParameters resParam = new ACFgResponseParameters();
    
			programme_no = param.get("programme_no", String.class);    
			ARCmProgrammeMaster pgm = programmeMasterDao.selectItem(programme_no);

			String busi_platform = pgm.business_platform;
			String dept          = pgm.department;

			ARCmBusinessPlatform busi_desc = businessPlatformDao.selectItem(busi_platform,"00");
			ARCmBusinessPlatform dept_desc = businessPlatformDao.selectItem(busi_platform, dept);

			resParam.set("busi_desc", busi_desc);
			resParam.set("dept_desc", dept_desc);
    
			return resParam;
		}

}