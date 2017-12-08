package arc.apf.Controller;

import java.util.List;
import java.math.BigDecimal;

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
import acf.acf.Database.ACFdSQLRule.RuleCase;
import acf.acf.Database.ACFdSQLRule.RuleCondition;
import acf.acf.General.annotation.ACFgAuditKey;
import acf.acf.General.annotation.ACFgFunction;
import acf.acf.General.annotation.ACFgTransaction;
import acf.acf.General.core.ACFgRawModel;
import acf.acf.General.core.ACFgRequestParameters;
import acf.acf.General.core.ACFgResponseParameters;
import acf.acf.General.core.ACFgSearch;
import acf.acf.Static.ACFtDBUtility;
import arc.apf.Service.ARCsAutoGenNo;
import arc.apf.Dao.ARCoAutoGenNo;
import arc.apf.Model.ARCmAutoGenNo;
import arc.apf.Static.APFtMapping;

@Controller
@Scope("session")
@ACFgFunction(id="APFF008")
@RequestMapping(value=APFtMapping.APFF008)
public class APFc008 extends ACFaAppController {

	@Autowired ARCoAutoGenNo autoGenNoDao;
	@Autowired ARCsAutoGenNo autoGenNoService;

	@ACFgAuditKey String form_id;	
	@ACFgAuditKey String system_yy;	
	@ACFgAuditKey String system_mm;	
	
	Search search = new Search();
	
	private class Search extends ACFgSearch {
		public Search() {
			super();
		
			//setModel(ARCmAutoGenNo.class);
			setKey("form_id","system_year","system_month");
			addRule(new ACFdSQLRule("form_id", RuleCondition.EQ, null, RuleCase.Insensitive));				
			
		}
		
		@Override
		public Search setValues(ACFgRequestParameters param) throws Exception {
			
			super.setValues(param);
			

			if(!param.isEmptyOrNull("p_year_month")) {
				String fr_yymm = param.get("p_year_month", String.class);
				String fr_yy = fr_yymm.substring(2,4);
				String fr_mm = fr_yymm.substring(5,7);
				
				//System.out.println(fr_yy + "*" +fr_mm);

				if (param.isEmptyOrNull("form_id")){

					setCustomSQL("select * from (select form_id, description, system_year, system_month, '   ' as sys_yymm, " + 
	    				"six_digit_serial_no as last_auto_no from arc_auto_gen_no where " +
	    				"(form_id != 'AT' and form_id != 'PAT') " +
	                    "union " +
	                    "select form_id, description, system_year, system_month, system_year || '/' || system_month as sys_yymm, " + 
	    				"three_digit_serial_no as last_auto_no from arc_auto_gen_no where " +
	    				"(form_id = 'AT' or form_id = 'PAT') and " +
	                    "(system_year = '"+fr_yy+"' and system_month = '"+fr_mm+"' ))");
				}
				else {
					String f_form_id = param.get("form_id", String.class);
		    		setCustomSQL("select * from (select form_id, description, system_year, system_month, '   ' as sys_yymm, " + 
		    				"six_digit_serial_no as last_auto_no from arc_auto_gen_no where " +
		    				"(form_id != 'AT' and form_id != 'PAT') and " +
		                    " form_id = '"+f_form_id+"' " +
		                    "union " +
		                    "select form_id, description, system_year, system_month, system_year || '/ ' || system_month as sys_yymm, " + 
		    				"three_digit_serial_no as last_auto_no from arc_auto_gen_no where " +
		    				"(form_id = 'AT' or form_id = 'PAT') and " +
		                    "(system_year = '"+fr_yy+"' and system_month = '"+fr_mm+"' ) and " +
		                    " form_id = '"+f_form_id+"')");	
	    			
	    		}
			}
			else {	
				if (param.isEmptyOrNull("form_id")){
					setCustomSQL("select form_id, description, system_year, system_month, '   ' as sys_yymm, " + 
							"six_digit_serial_no as last_auto_no from arc_auto_gen_no " +
							"where (form_id != 'AT' and form_id != 'PAT') " +
							"union " +
							"select form_id, description, system_year, system_month, system_year || '/' || system_month as sys_yymm, " + 
							"three_digit_serial_no as last_auto_no from arc_auto_gen_no " +
							"where (form_id = 'AT' or form_id = 'PAT') ");	
				}
				else {
					String f_form_id = param.get("form_id", String.class);
					
					setCustomSQL("select form_id, description, system_year, system_month, '   ' as sys_yymm, " + 
							"six_digit_serial_no as last_auto_no from arc_auto_gen_no " +
							"where (form_id != 'AT' and form_id != 'PAT') " +
							"and form_id = '"+f_form_id+"' " +
							"union " +
							"select form_id, description, system_year, system_month, system_year || '/' || system_month as sys_yymm, " + 
							"three_digit_serial_no as last_auto_no from arc_auto_gen_no " +
							"where (form_id = 'AT' or form_id = 'PAT') "+
							"and form_id = '"+f_form_id+"' ");	

				}
			}
			
			return this;
		}				
	}

	@RequestMapping(value=APFtMapping.APFF008_MAIN, method=RequestMethod.GET)
	public String main(ModelMap model) throws Exception {

		model.addAttribute("form_id", form_id);
		model.addAttribute("system_year", system_yy);
		model.addAttribute("system_monthe", system_mm);
		
		model.addAttribute("formid", autoGenNoService.getAllFormIdValue());
		getResponseParameters().set("s_form_id", autoGenNoService.getAllFormIdValue());
		
		return view();
	}

	@RequestMapping(value=APFtMapping.APFF008_SEARCH_AJAX, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters getGrid(@RequestBody ACFgRequestParameters param) throws Exception {

		search.setConnection(getConnection("ARCDB"));
		search.setValues(param);
		search.setFocus(form_id,system_yy,system_mm);

		return new ACFgResponseParameters().set("grid_browse", search.getGridResult());
	}

	@RequestMapping(value=APFtMapping.APFF008_GET_FORM_AJAX, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters getForm(@RequestBody ACFgRequestParameters param) throws Exception {

		form_id = param.get("form_id", String.class);
		system_yy = param.get("system_year", String.class);
		system_mm = param.get("system_month", String.class);
		//System.out.println(form_id + "*" + system_yy + "*" + system_mm);
		
		getFormValue(param);
		
		return getResponseParameters().set("frm_main", autoGenNoDao.selectItem(form_id, system_yy, system_mm));
	}
	

 	@RequestMapping(value=APFtMapping.APFF008_LIST_AJAX, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters getFormValue(@RequestBody ACFgRequestParameters param) throws Exception {

 		System.out.println(form_id + "*" + system_yy + "*" + system_mm);
 		ACFdSQLAssSelect select = new ACFdSQLAssSelect();     

        
		if (("AT".equals(form_id)) || ("PAT".equals(form_id))){
	        select.setCustomSQL("select form_id, description, system_year, system_month, system_year || '/' || system_month as sys_yymm, " + 
	                "three_digit_serial_no as last_auto_no from arc_auto_gen_no where form_id = '"+form_id+"' and (system_year > '"+system_yy+"' or (system_year = '"+system_yy+"' and system_month >= '"+system_mm+"'))");

		}
		else {
	        select.setCustomSQL("select form_id, description, system_year, system_month, '  ' as sys_yymm, " + 
	                "six_digit_serial_no as last_auto_no from arc_auto_gen_no where form_id = '"+form_id+"' and (system_year > '"+system_yy+"' or (system_year = '"+system_yy+"' and system_month >= '"+system_mm+"'))");

		}
		
        select.setKey("form_id","system_year","system_month");

        return getResponseParameters().set("grid_autono", select.executeGridQuery(getConnection("ARCDB"), param));

		
	}	
}