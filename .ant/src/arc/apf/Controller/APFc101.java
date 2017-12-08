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
@ACFgFunction(id="APFF101")
@RequestMapping(value=APFtMapping.APFF101)

public class APFc101 extends ACFaAppController{

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
			sql= "select * from (select t1.programme_no, t1.consumption_form_no as key_no, t1.section_id, t4.section_name, t2.item_no, t3.item_description_1, t1.completion_date as compare_date, " + 
			     "sum(t2.consumption_quantity) as sub_qty, t2.unit_cost, t2.account_allocation, (sum(t2.consumption_quantity) * t2.unit_cost) as sub_item_amount " + 
			     "from arc_wp_consumption_header t1, arc_wp_consumption_item t2, arc_item_master t3, arc_section t4 " + 
			     "where t1.consumption_form_no = t2.consumption_form_no and t1.cancel_indicator = 'N' and t2.item_no = t3.item_no " +
				 "  and t1.section_id = t4.section_id and t4.sub_section_id = '0' " + 
			     "group by t1.programme_no, t1.consumption_form_no, t1.section_id, t4.section_name, t2.item_no, t3.item_description_1, t1.completion_date, t2.unit_cost, t2.account_allocation " +
			     "union " +
			     "select t1.programme_no, t1.job_no as key_no, t1.section_id, t4.section_name, t2.item_no, t3.item_description_1, t1.input_date as compare_date, " +
			     "sum(t2.consumption_quantity) as sub_qty, t2.unit_cost, t2.account_allocation, (sum(t2.consumption_quantity) * t2.unit_cost) as sub_item_amount " +
			     "from arc_photo_consumption_header t1, arc_photo_consumption_item t2, arc_item_master t3, arc_section t4 " + 
			     "where t1.job_no = t2.job_no and t1.cancel_indicator = 'N' and t2.item_no = t3.item_no " +
				 "  and t1.section_id = t4.section_id and t4.sub_section_id = '0' " + 
			     "group by t1.programme_no, t1.job_no, t1.section_id, t4.section_name, t2.item_no, t3.item_description_1, t1.input_date, t2.unit_cost, t2.account_allocation)";

			setCustomSQL(sql);
	        setKey("item_no","section_id","unit_cost","account_allocation","key_no");	
	    	
	        select = new ACFdSQLAssSelect();
			select.setCustomSQL("select (sum(sub_item_amount)) as total_item from (" + sql + ")");

			}

	        @Override
	        public SearchItem setValues(ACFgRequestParameters param) throws Exception { 
	            super.setValues(param);// param is a object, "SearchItem" 's mother class passed
	            
	            if(!param.isEmptyOrNull("programme_no")) {	          	
	            wheres.and("programme_no", ACFdSQLRule.RuleCondition.EQ, param.get("programme_no", String.class));
	            select.wheres.and("programme_no", ACFdSQLRule.RuleCondition.EQ, param.get("programme_no", String.class));
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
	        			w1.or("section_id", ACFdSQLRule.RuleCondition.EQ, sectionid);
	        			w2.or("section_id", ACFdSQLRule.RuleCondition.EQ, sectionid);
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

		private class SearchOthers extends ACFgSearch {	
			
	    	String sql = null;
	    	ACFdSQLAssSelect select;
					
			public SearchOthers(){
			super();
			sql= "select * from (select t1.programme_no,  t1.section_id, t4.section_name, t1.completion_date as compare_date, " + 
				 "       t2.account_allocation, t3.actual_account_description, sum(t2.other_material_amount) as sub_others_amount, t1.consumption_form_no as key_no " +
				 " from arc_wp_consumption_header t1, arc_wp_other_material_consumption t2, arc_account_allocation t3, arc_section t4 " +
				 "where t1.consumption_form_no = t2.consumption_form_no and t1.cancel_indicator = 'N' and t2.account_allocation = t3.actual_account_allocation " +
				 "  and t1.section_id = t4.section_id and t4.sub_section_id = '0' " +
				 "group by t1.programme_no, t4.section_name, t1.consumption_form_no, t1.section_id, t1.completion_date, t2.account_allocation, t3.actual_account_description " +
				 "union " +
				 "select t2.programme_no,  t1.section_id, t4.section_name, t1.request_date as compare_date, " +
				 "       t2.account_allocation, t3.actual_account_description, sum(t2.payment_amount) as sub_others_amount, t1.payment_form_no as key_no " +
				 " from arc_payment_request t1, arc_payment_details t2, arc_account_allocation t3, arc_section t4 " +
				 "where t1.payment_form_no = t2.payment_form_no and t2.account_allocation = t3.actual_account_allocation " +
				 "  and t1.section_id = t4.section_id and t4.sub_section_id = '0' " +
				 "group by t2.programme_no, t4.section_name, t1.payment_form_no, t1.section_id, t1.request_date, t2.account_allocation, t3.actual_account_description " +
				 "union " +
				 "select t1.programme_no, t1.section_id, t4.section_name, t1.input_date as compare_date, t2.account_allocation, t3.actual_account_description, " +
				 "       sum(t2.other_material_amount) as sub_others_amount, t1.job_no as key_no " +
				 " from arc_photo_consumption_header t1, arc_photo_other_material_consumption t2, arc_account_allocation t3, arc_section t4 " + 
				 "where t1.programme_no = t2.programme_no and  t1.cancel_indicator = 'N' and t1.job_no = t2.job_no and t2.account_allocation = t3.actual_account_allocation " +
				 "  and t1.section_id = t4.section_id and t4.sub_section_id = '0' " +
				 "group by t1.programme_no, t4.section_name, t1.job_no, t1.section_id, t1.input_date, t2.account_allocation, t3.actual_account_description) ";
		
			setCustomSQL(sql);
	        setKey("account_allocation","section_id","key_no","compare_date");	
	    	
	        select = new ACFdSQLAssSelect();
			select.setCustomSQL("select (sum(sub_others_amount)) as total_others from (" + sql + ")");

			}

	        @Override
	        public SearchOthers setValues(ACFgRequestParameters param) throws Exception { 
	            super.setValues(param);
	            
	            if(!param.isEmptyOrNull("programme_no")) {	          	
	            wheres.and("programme_no", ACFdSQLRule.RuleCondition.EQ, param.get("programme_no", String.class));
	            select.wheres.and("programme_no", ACFdSQLRule.RuleCondition.EQ, param.get("programme_no", String.class));
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
	        			w1.or("section_id", ACFdSQLRule.RuleCondition.EQ, sectionid);
	        			w2.or("section_id", ACFdSQLRule.RuleCondition.EQ, sectionid);
	        		}
	        		wheres.and(w1);
	       			select.wheres.and(w2);
	        	}

	            List<ACFgRawModel> lg = select.executeQuery(getConnection());
	       		total_others = lg.get(0).getBigDecimal("total_others");
	            if (total_others == null)
	                total_others = BigDecimal.ZERO;
	            	                        
				return this;
			}			
			
		}
		
		private class SearchLabour extends ACFgSearch {	
			
	    	String sql = null;
	    	ACFdSQLAssSelect select;
					
			public SearchLabour(){
			super();
			sql= "select * from (select t1.programme_no,  t1.section_id, t4.section_name, t1.completion_date as compare_date, " +
				 "       t2.labour_type, t3.labour_type_description, sum(t2.no_of_hours) as sub_hours, t2.hourly_rate, " +  
				 "       t1.consumption_form_no as key_no, t1.consumption_form_no as sort_field, (sum(t2.no_of_hours) * t2.hourly_rate) as sub_labour_amount " +
				 "from arc_wp_consumption_header t1, arc_wp_labour_consumption t2, arc_labour_type t3, arc_section t4 " + 
				 "where t1.consumption_form_no = t2.consumption_form_no and t1.cancel_indicator = 'N' and t2.labour_type = t3.labour_type " +
				 "  and t3.effective_to_date = (select max(ta.effective_to_date) from arc_labour_type ta where ta.labour_type = t2.labour_type) " +
				 "  and t1.section_id = t4.section_id and t4.sub_section_id = '0' " +  
				 "group by t1.programme_no, t1.section_id, t4.section_name, t2.labour_type, t3.labour_type_description, t2.hourly_rate, t1.consumption_form_no, t1.completion_date " + 
				 "union " +
				 "select t1.programme_no, t1.section_id, t4.section_name, t1.input_date as compare_date, t2.labour_type, t3.labour_type_description, " +
				 "       sum(t2.no_of_hours) as sub_hours, t2.hourly_rate, t1.job_no as key_no, t1.job_no as sort_field, (sum(t2.no_of_hours) * t2.hourly_rate) as sub_labour_amount " +
				 "from arc_photo_consumption_header t1, arc_photo_labour_consumption t2, arc_labour_type t3, arc_section t4 " + 
				 "where t1.job_no = t2.job_no and t1.cancel_indicator = 'N' and t2.labour_type = t3.labour_type " +
				 "  and t3.effective_to_date = (select max(ta.effective_to_date) from arc_labour_type ta where ta.labour_type = t2.labour_type) " +
				 "  and t1.section_id = t4.section_id and t4.sub_section_id = '0' " + 
				 "group by t1.programme_no, t1.section_id, t4.section_name, t2.labour_type, t3.labour_type_description, t2.hourly_rate, t1.job_no, t1.input_date  " +
				 "union " +
				 "select t1.programme_no, t1.section_id, t4.section_name, t1.process_date as compare_date, t2.labour_type, t3.labour_type_description, " +
				 "       sum(t2.no_of_hours) as sub_hours, t2.hourly_rate, ' ' as key_no, char(t1.process_date) as sort_field, (sum(t2.no_of_hours) * t2.hourly_rate) as sub_labour_amount " +
				 "from arc_labour_consumption t1, arc_labour_consumption_details t2, arc_labour_type t3, arc_section t4 " + 
				 "where t1.programme_no = t2.programme_no and t1.process_date = t2.process_date and t1.cancel_indicator = 'N' and t1.section_id = t2.section_id " +
				 "  and t1.from_episode_no = t2.from_episode_no and t1.to_episode_no = t2.to_episode_no " +
				 "  and t2.labour_type = t3.labour_type " + 
				 "  and t3.effective_to_date = (select max(ta.effective_to_date) from arc_labour_type ta where ta.labour_type = t2.labour_type) " +
				 "  and t1.section_id = t4.section_id and t4.sub_section_id = '0' " +
				 "group by t1.programme_no, t1.section_id, t4.section_name, t2.labour_type, t3.labour_type_description, t2.hourly_rate, ' ', t1.process_date)";
		
			setCustomSQL(sql);
	        setKey("section_id","labour_type","hourly_rate","sort_field","compare_date");	
	    	
	        select = new ACFdSQLAssSelect();
			select.setCustomSQL("select (sum(sub_labour_amount)) as total_labour from (" + sql + ")");

			}

	        @Override
	        public SearchLabour setValues(ACFgRequestParameters param) throws Exception { 
	            super.setValues(param);// param is a object, "SearchLabour" 's mother class passed
	            
	            if(!param.isEmptyOrNull("programme_no")) {	          	
	            wheres.and("programme_no", ACFdSQLRule.RuleCondition.EQ, param.get("programme_no", String.class));
	            select.wheres.and("programme_no", ACFdSQLRule.RuleCondition.EQ, param.get("programme_no", String.class));
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
	        			w1.or("section_id", ACFdSQLRule.RuleCondition.EQ, sectionid);
	        			w2.or("section_id", ACFdSQLRule.RuleCondition.EQ, sectionid);
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
		
		@RequestMapping(value=APFtMapping.APFF101_MAIN, method=RequestMethod.GET)
		public String main(ModelMap model) throws Exception {
	
			model.addAttribute("sectionselect", sectionService.getAllSectionValue());
			return view();
	
		}



		@RequestMapping(value=APFtMapping.APFF101_GET_FORM_AJAX, method=RequestMethod.POST)
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
		
		@RequestMapping(value=APFtMapping.APFF101_GET_ITEM_AJAX, method=RequestMethod.POST) //get rows of the second grid
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

		SearchOthers searchOthers = new SearchOthers();
		
		@RequestMapping(value=APFtMapping.APFF101_GET_ACCOUNT_ALLOCATION_AJAX, method=RequestMethod.POST) //get rows of the second grid
		@ResponseBody
		public ACFgResponseParameters getAcTable(@RequestBody ACFgRequestParameters reqparam) throws Exception {                                
						
			searchOthers.setConnection(getConnection("ARCDB"));
			searchOthers.setValues(reqparam);
	
			ACFgResponseParameters resParam = this.getResponseParameters();	
		
			ACFmGridResult grid = searchOthers.getGridResult();	
			this.getResponseParameters().set("grid_others_consumption", grid);
		
			ACFgRawModel TtlOthers= new ACFgRawModel();
			TtlOthers.set("total_others", df.format(total_others));
			this.getResponseParameters().set("frm_main", TtlOthers);
		
			return resParam;
   
		}

		SearchLabour searchLabour = new SearchLabour();

		@RequestMapping(value=APFtMapping.APFF101_GET_LABOUR_AJAX, method=RequestMethod.POST) //get rows of the third grid
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

		@RequestMapping(value=APFtMapping.APFF101_GET_PROG_BP_AJAX, method=RequestMethod.POST)
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