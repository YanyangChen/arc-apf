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
import acf.acf.General.core.ACFgRequestParameters;
import acf.acf.General.core.ACFgResponseParameters;
import acf.acf.General.core.ACFgSearch;
import arc.apf.Service.ARCsSectionLabour;
import arc.apf.Service.ARCsSection;
import arc.apf.Dao.ARCoSection;
import arc.apf.Model.ARCmSection;
import arc.apf.Service.ARCsLabourType;
import arc.apf.Dao.ARCoLabourType;
import arc.apf.Model.ARCmLabourType;
import arc.apf.Service.ARCsMonthlyManhour;
import arc.apf.Dao.ARCoMonthlyManhour;
import arc.apf.Model.ARCmMonthlyManhour;
import arc.apf.Static.APFtMapping;

@Controller
@Scope("session")
@ACFgFunction(id="APFF010")
@RequestMapping(value=APFtMapping.APFF010)
public class APFc010 extends ACFaAppController {

	@Autowired ARCoSection sectionDao;
	@Autowired ARCsSection sectionService;
	@Autowired ARCoLabourType labourTypeDao;
	@Autowired ARCsLabourType labourTypeService;
	@Autowired ARCoMonthlyManhour monthlyManhourDao;
	@Autowired ARCsMonthlyManhour monthlyManhourService;
	@Autowired ARCsSectionLabour sectionLabourService;


	@ACFgAuditKey String labour_type;	
	@ACFgAuditKey String section_id;	
	@ACFgAuditKey String ye_ar;	
	
	Search search = new Search();
	
	private class Search extends ACFgSearch {
		public Search() {
			
			super();
			
			//setModel(ARCmMonthlyManhour.class);
			//setKey("labour_type","ye_ar");
			//addRule(new ACFdSQLRule("ye_ar", RuleCondition.EQ, null, RuleCase.Insensitive));	
			//addRule(new ACFdSQLRule("section_id", RuleCondition.EQ, null, RuleCase.Insensitive));	
	
    		setCustomSQL("select * from (select t1.ye_ar, t1.section_id, t1.labour_type, t2.labour_type_description " +
                    "from arc_monthly_manhour t1, arc_labour_type t2 " +
                    "where t1.labour_type = t2.labour_type " +
        			"group by t1.ye_ar, t1.section_id, t1.labour_type, t2.labour_type_description) "); 
            setKey("labour_type", "ye_ar");	
            
			addRule(new ACFdSQLRule("section_id", RuleCondition.EQ, null, RuleCase.Insensitive));	
			addRule(new ACFdSQLRule("ye_ar", RuleCondition.GE, null, RuleCase.Insensitive));	
		}
	}

	@RequestMapping(value=APFtMapping.APFF010_MAIN, method=RequestMethod.GET)
	public String main(ModelMap model) throws Exception {

		model.addAttribute("section_id", section_id);
		model.addAttribute("labour_type", labour_type);
		model.addAttribute("ye_ar", ye_ar);
		
		model.addAttribute("sectionid", sectionService.getAllSectionValue());
		model.addAttribute("labourtype", labourTypeService.getAllEffLabourType());	
		model.addAttribute("sectionLabour", sectionLabourService.getSectionLabourIndex());

		getResponseParameters().set("s_section_id", sectionService.getAllSectionValue());
		
		return view();
	}

	@RequestMapping(value=APFtMapping.APFF010_SEARCH_AJAX, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters getGrid(@RequestBody ACFgRequestParameters param) throws Exception {

		search.setConnection(getConnection("ARCDB"));
		search.setValues(param);
		search.setFocus(labour_type);

		return new ACFgResponseParameters().set("grid_browse", search.getGridResult());
	}

	
	@RequestMapping(value=APFtMapping.APFF010_GET_FORM_AJAX, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters getForm(@RequestBody ACFgRequestParameters param) throws Exception {
	
		labour_type = param.get("labour_type", String.class);
		ye_ar = param.get("ye_ar", String.class);
		System.out.println("form " + labour_type + " yr " + ye_ar);
		
		getAllManhour(param);
		
		return getResponseParameters().set("frm_main", monthlyManhourDao.selectItem(labour_type, ye_ar));
	}

	@RequestMapping(value=APFtMapping.APFF010_LIST_AJAX, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters getAllManhour(@RequestBody ACFgRequestParameters param) throws Exception {

 		System.out.println("list " + labour_type + " yr " + ye_ar);
 		
 		ACFdSQLAssSelect select = new ACFdSQLAssSelect();     
        select.setCustomSQL("select * from arc_monthly_manhour " + 
	                        "where labour_type = '"+labour_type+"' " +
        		            "  and ye_ar = '"+ye_ar+"' ");
	
        select.setKey("labour_type","ye_ar");

        return getResponseParameters().set("grid_manhour", select.executeGridQuery(getConnection("ARCDB"), param));

	}	
	

	@ACFgTransaction
	@RequestMapping(value=APFtMapping.APFF010_SAVE_AJAX, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters save(@RequestBody ACFgRequestParameters param) throws Exception {

		List<ARCmMonthlyManhour> amendments = param.getList("Manhour", ARCmMonthlyManhour.class);
		//amendments.get(0).sub_section_id = "0";
		ARCmMonthlyManhour mm  = monthlyManhourDao.saveItems(amendments);
		
		return new ACFgResponseParameters();
	}

}