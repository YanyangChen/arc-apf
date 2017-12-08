package arc.apf.Controller;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.math.BigDecimal;

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
import acf.acf.Database.ACFdSQLAssUpdate;
import acf.acf.Database.ACFdSQLAssSelect;
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
import arc.apf.Dao.ARCoBusinessPlatform;
import arc.apf.Dao.ARCoProgrammeMaster;
import arc.apf.Dao.ARCoWPConsumptionHeader;
import arc.apf.Model.ARCmBusinessPlatform;
import arc.apf.Model.ARCmPaymentRequest;
import arc.apf.Model.ARCmProgrammeMaster;
import arc.apf.Model.ARCmWPConsumptionHeader;
import arc.apf.Model.ARCmWPConsumptionItem;
import arc.apf.Service.ARCsWPConsumptionHeader;
import arc.apf.Static.APFtMapping;



@Controller
@Scope("session")
@ACFgFunction(id="APFF104")
@RequestMapping(value=APFtMapping.APFF104)
public class APFc104 extends ACFaAppController {

	@Autowired ARCoWPConsumptionHeader WPconsumptionHeaderDao;
	@Autowired ARCsWPConsumptionHeader WPconsumptionHeaderService;
	@Autowired ARCoBusinessPlatform  businessPlatformDao;
	@Autowired ARCoProgrammeMaster   programmeMasterDao;


	@ACFgAuditKey String consumption_form_no;
	@ACFgAuditKey String programme_no;	
	BigDecimal total_i;
	BigDecimal total_o;
	BigDecimal total_l;
		
	private class SearchItemConsumeDtl extends ACFgSearch {
		
    	String sql = null;
    	ACFdSQLAssSelect select;
		
			public SearchItemConsumeDtl() {
				super();
				sql = "select * from (select t1.consumption_form_no as consumption_form_no, t1.construction_no as construction_no, t1.completion_date as completion_date, " +
						 "t1.programme_no as programme_no, t2.programme_name as programme_name, " +
						 "t1.from_episode_no as from_episode_no, t1.to_episode_no as to_episode_no, t3.account_allocation as account_allocation, " +
						 "t3.item_no as item_no, t3.consumption_quantity as consumption_quantity, t3.unit_cost as unit_cost, " +
						 "t3.purchase_order_no as purchase_order_no " +
		                 "from arc_wp_consumption_header t1, arc_programme_master t2, arc_wp_consumption_item t3 " +
				         "where t1.programme_no = t2.programme_no "+
		                 "  and t1.consumption_form_no = t3.consumption_form_no)";
	            setCustomSQL(sql);
	            setKey("consumption_form_no","item_no","purchase_order_no");	
	
	            select = new ACFdSQLAssSelect();
	    		select.setCustomSQL("select (sum(consumption_quantity * unit_cost)) as total_i from (" + sql + ")");
	            
				// addRule(new ACFdSQLRule("programme_no", RuleCondition.EQ,null, RuleCase.Insensitive));	

			}

	        @Override
	        public SearchItemConsumeDtl setValues(ACFgRequestParameters param) throws Exception { //use the search class to setup an object
	            super.setValues(param);// param is a object, "Search" 's mother class passed

	            System.out.println(param);
	            if(!param.isEmptyOrNull("programme_no")) {
	            System.out.println(param.get("programme_no", String.class));	
	            wheres.and("programme_no", ACFdSQLRule.RuleCondition.EQ, param.get("programme_no", String.class));
	            select.wheres.and("programme_no", ACFdSQLRule.RuleCondition.EQ, param.get("programme_no", String.class));
	            }
	            System.out.println(param);
	            if(!param.isEmptyOrNull("fr_form_no")) {
	            System.out.println(param.get("fr_form_no", String.class));	
	            wheres.and("consumption_form_no", ACFdSQLRule.RuleCondition.GE, param.get("fr_form_no", String.class));
	            select. wheres.and("consumption_form_no", ACFdSQLRule.RuleCondition.GE, param.get("fr_form_no", String.class));
	            }
	            if(!param.isEmptyOrNull("to_form_no")) {
	            System.out.println(param.get("to_form_no", String.class));		
	            wheres.and("consumption_form_no", ACFdSQLRule.RuleCondition.LE, param.get("to_form_no", String.class));
	            select.wheres.and("consumption_form_no", ACFdSQLRule.RuleCondition.LE, param.get("to_form_no", String.class));
	            }            
	            if(!param.isEmptyOrNull("select_fr_date")) {	
	            System.out.println(param.get("select_fr_date", Timestamp.class));	            	
	            wheres.and("completion_date", ACFdSQLRule.RuleCondition.GE, param.get("select_fr_date", Timestamp.class));
	            select.wheres.and("completion_date", ACFdSQLRule.RuleCondition.GE, param.get("select_fr_date", Timestamp.class));
	            }
	            if(!param.isEmptyOrNull("select_to_date")) {
	            System.out.println(param.get("select_to_date", Timestamp.class));	                  	
	            wheres.and("completion_date", ACFdSQLRule.RuleCondition.LE, new Timestamp(param.get("select_to_date", Long.class) + 24*60*60*1000));
	            select.wheres.and("completion_date", ACFdSQLRule.RuleCondition.LE, new Timestamp(param.get("select_to_date", Long.class) + 24*60*60*1000));
	            }
	            
	       		List<ACFgRawModel> lg = select.executeQuery(getConnection());
	       		total_i = lg.get(0).getBigDecimal("total_i");
	            if (total_i == null)
	                total_i = BigDecimal.ZERO;
	            System.out.println("select item sum " + total_i);
	            	            
				return this;
			}					

	}	
	
	private class SearchOtherConsumeDtl extends ACFgSearch {
		
		String sql = null;
		ACFdSQLAssSelect select;
		
		public SearchOtherConsumeDtl() {
			
				super();
				sql = "select * from (select t1.consumption_form_no as consumption_form_no, t1.construction_no as construction_no, t1.completion_date as completion_date, " +
						 "t1.programme_no as programme_no, t2.programme_name as programme_name, " +
						 "t1.from_episode_no as from_episode_no, t1.to_episode_no as to_episode_no, t3.account_allocation as account_allocation, " +
						 "t3.other_material_description as other_material_description, t3.other_material_amount as other_material_amount, " +
						 "t3.sequence_no as sequence_no " +
		                 "from arc_wp_consumption_header t1, arc_programme_master t2, arc_wp_other_material_consumption t3 " +
				         "where t1.programme_no = t2.programme_no " +
		                 "  and t1.consumption_form_no = t3.consumption_form_no)";
	            setCustomSQL(sql);
	            setKey("consumption_form_no","sequence_no");	
	        	
	            select = new ACFdSQLAssSelect();
	    		select.setCustomSQL("select (sum(other_material_amount)) as total_o from (" + sql + ")");
	            
				//addRule(new ACFdSQLRule("programme_no", RuleCondition.EQ,null, RuleCase.Insensitive));	

			}

	        @Override
	        public SearchOtherConsumeDtl setValues(ACFgRequestParameters param) throws Exception { //use the search class to setup an object
	            super.setValues(param);// param is a object, "Search" 's mother class passed

	            if(!param.isEmptyOrNull("programme_no")) {
	            System.out.println(param.get("programme_no", String.class));	
	            wheres.and("programme_no", ACFdSQLRule.RuleCondition.EQ, param.get("programme_no", String.class));
	            select.wheres.and("programme_no", ACFdSQLRule.RuleCondition.EQ, param.get("programme_no", String.class));
	            }
	            if(!param.isEmptyOrNull("fr_form_no")) {
	            System.out.println(param.get("fr_form_no", String.class));	
	            wheres.and("consumption_form_no", ACFdSQLRule.RuleCondition.GE, param.get("fr_form_no", String.class));
	            select.wheres.and("consumption_form_no", ACFdSQLRule.RuleCondition.GE, param.get("fr_form_no", String.class));
	            }
	            if(!param.isEmptyOrNull("to_form_no")) {
	            System.out.println(param.get("to_form_no", String.class));		
	            wheres.and("consumption_form_no", ACFdSQLRule.RuleCondition.LE, param.get("to_form_no", String.class));
	            select.wheres.and("consumption_form_no", ACFdSQLRule.RuleCondition.LE, param.get("to_form_no", String.class));
	            }            
	            if(!param.isEmptyOrNull("select_fr_date")) {	
	            System.out.println(param.get("select_fr_date", Timestamp.class));	            	
	            wheres.and("completion_date", ACFdSQLRule.RuleCondition.GE, param.get("select_fr_date", Timestamp.class));
	            select.wheres.and("completion_date", ACFdSQLRule.RuleCondition.GE, param.get("select_fr_date", Timestamp.class));
	            }
	            if(!param.isEmptyOrNull("select_to_date")) {
	            System.out.println(param.get("select_to_date", Timestamp.class));	                  	
	            wheres.and("completion_date", ACFdSQLRule.RuleCondition.LE, new Timestamp(param.get("select_to_date", Long.class) + 24*60*60*1000));
	            select.wheres.and("completion_date", ACFdSQLRule.RuleCondition.LE, new Timestamp(param.get("select_to_date", Long.class) + 24*60*60*1000));
	            }
	
	       		List<ACFgRawModel> lg = select.executeQuery(getConnection());
	       		total_o = lg.get(0).getBigDecimal("total_o");
	            if (total_o == null)
	                total_o = BigDecimal.ZERO;
	            System.out.println("select other sum " + total_o);

				return this;
			}	
	}
	
	private class SearchLabourConsumeDtl extends ACFgSearch {
		
		String sql = null;
		ACFdSQLAssSelect select;

		public SearchLabourConsumeDtl() {
			super();
			sql = "select * from (select t1.consumption_form_no as consumption_form_no, t1.construction_no as construction_no, t1.completion_date as completion_date, " +
					 "t1.programme_no as programme_no, t2.programme_name as programme_name, " +
					 "t1.from_episode_no as from_episode_no, t1.to_episode_no as to_episode_no, " +
					 "t3.labour_type as labour_type, t3.no_of_hours as no_of_hours, t3.hourly_rate as hourly_rate " +
	                 "from arc_wp_consumption_header t1, arc_programme_master t2, arc_wp_labour_consumption t3 " +
			         "where t1.programme_no = t2.programme_no "+
	                 "  and t1.consumption_form_no = t3.consumption_form_no)";
            setCustomSQL(sql);
            setKey("consumption_form_no","labour_type");	

            select = new ACFdSQLAssSelect();
    		select.setCustomSQL("select (sum(no_of_hours * hourly_rate)) as total_l from (" + sql + ")");
    		
			// addRule(new ACFdSQLRule("programme_no", RuleCondition.EQ,null, RuleCase.Insensitive));	

		}

        @Override
        public SearchLabourConsumeDtl setValues(ACFgRequestParameters param) throws Exception { //use the search class to setup an object
            super.setValues(param);// param is a object, "Search" 's mother class passed

            if(!param.isEmptyOrNull("programme_no")) {
            System.out.println(param.get("programme_no", String.class));	
            wheres.and("programme_no", ACFdSQLRule.RuleCondition.EQ, param.get("programme_no", String.class));
            select.wheres.and("programme_no", ACFdSQLRule.RuleCondition.EQ, param.get("programme_no", String.class));
            }
            if(!param.isEmptyOrNull("fr_form_no")) {
            System.out.println(param.get("fr_form_no", String.class));	
            wheres.and("consumption_form_no", ACFdSQLRule.RuleCondition.GE, param.get("fr_form_no", String.class));
            select.wheres.and("consumption_form_no", ACFdSQLRule.RuleCondition.GE, param.get("fr_form_no", String.class));
            }
            if(!param.isEmptyOrNull("to_form_no")) {
            System.out.println(param.get("to_form_no", String.class));		
            wheres.and("consumption_form_no", ACFdSQLRule.RuleCondition.LE, param.get("to_form_no", String.class));
            select.wheres.and("consumption_form_no", ACFdSQLRule.RuleCondition.LE, param.get("to_form_no", String.class));
            }            
            if(!param.isEmptyOrNull("select_fr_date")) {	
            System.out.println(param.get("select_fr_date", Timestamp.class));	            	
            wheres.and("completion_date", ACFdSQLRule.RuleCondition.GE, param.get("select_fr_date", Timestamp.class));
            select.wheres.and("completion_date", ACFdSQLRule.RuleCondition.GE, param.get("select_fr_date", Timestamp.class));
            }
            if(!param.isEmptyOrNull("select_to_date")) {
            System.out.println(param.get("select_to_date", Timestamp.class));	                  	
            wheres.and("completion_date", ACFdSQLRule.RuleCondition.LE, new Timestamp(param.get("select_to_date", Long.class) + 24*60*60*1000));
            select.wheres.and("completion_date", ACFdSQLRule.RuleCondition.LE, new Timestamp(param.get("select_to_date", Long.class) + 24*60*60*1000));
            }
            
       		List<ACFgRawModel> lg = select.executeQuery(getConnection());
       		total_l = lg.get(0).getBigDecimal("total_l");
            if (total_l == null)
                total_l = BigDecimal.ZERO;
            System.out.println("select labour sum " + total_l);
                        
			return this;
		}		
    }	
	
	@RequestMapping(value=APFtMapping.APFF104_MAIN, method=RequestMethod.GET)
	public String main(ModelMap model) throws Exception {
		
		return view();
	}
			

	@RequestMapping(value=APFtMapping.APFF104_GET_FORM_AJAX, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters getForm(@RequestBody ACFgRequestParameters param) throws Exception {
		
		System.out.println("get form" + param);
		ACFgResponseParameters resParam = this.getResponseParameters();		
		
		//this.getItemConsumption(param);
		//this.getOtherConsumption(param);
		//this.getLabourConsumption(param);

		return resParam;
	}

	SearchItemConsumeDtl  searchItem = new SearchItemConsumeDtl();
	
	@RequestMapping(value=APFtMapping.APFF104_GET_ITEM_CONSUMPTION_AJAX, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters getItemConsumption(@RequestBody ACFgRequestParameters reqparam) throws Exception {

		java.text.DecimalFormat df = new java.text.DecimalFormat("###,##0.00");
		

		searchItem.setConnection(getConnection("ARCDB"));
		searchItem.setValues(reqparam);
		
		ACFgResponseParameters resParam = this.getResponseParameters();	
		
		ACFmGridResult grid = searchItem.getGridResult();
		this.getResponseParameters().set("grid_item_consumption", grid);
		
		ACFgRawModel TtlItem = new ACFgRawModel();
		TtlItem.set("total_item", df.format(total_i));
		this.getResponseParameters().set("frm_main", TtlItem);

		return resParam;
	}	
	
	SearchOtherConsumeDtl  searchOther = new SearchOtherConsumeDtl();
	
	@RequestMapping(value=APFtMapping.APFF104_GET_OTHER_CONSUMPTION_AJAX, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters getOtherConsumption(@RequestBody ACFgRequestParameters reqparam) throws Exception {
		java.text.DecimalFormat df = new java.text.DecimalFormat("###,##0.00");
		
		searchOther.setConnection(getConnection("ARCDB"));
		searchOther.setValues(reqparam);

		ACFgResponseParameters resParam = this.getResponseParameters();	
		
		ACFmGridResult grid = searchOther.getGridResult();
		this.getResponseParameters().set("grid_other_consumption", grid);
		
		ACFgRawModel TtlOther = new ACFgRawModel();
		TtlOther.set("total_other", df.format(total_o));
		this.getResponseParameters().set("frm_main", TtlOther);

		return resParam;		
	}	
	
	SearchLabourConsumeDtl  searchLabour = new SearchLabourConsumeDtl();
	
	@RequestMapping(value=APFtMapping.APFF104_GET_LABOUR_CONSUMPTION_AJAX, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters getLabourConsumption(@RequestBody ACFgRequestParameters reqparam) throws Exception {
		java.text.DecimalFormat df = new java.text.DecimalFormat("###,##0.00");
		
		searchLabour.setConnection(getConnection("ARCDB"));
		searchLabour.setValues(reqparam);

		ACFgResponseParameters resParam = this.getResponseParameters();	
		
		ACFmGridResult grid = searchLabour.getGridResult();
		this.getResponseParameters().set("grid_labour_consumption", grid);
				
		ACFgRawModel TtlLabour = new ACFgRawModel();
		TtlLabour.set("total_labour", df.format(total_l));
		this.getResponseParameters().set("frm_main", TtlLabour);

		return resParam;	
	}	
		

}