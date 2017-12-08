package arc.apf.Controller;

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
import acf.acf.Database.ACFdSQLRule;
import acf.acf.Database.ACFdSQLRule.RuleCase;
import acf.acf.Database.ACFdSQLRule.RuleCondition;
import acf.acf.General.annotation.ACFgAuditKey;
import acf.acf.General.annotation.ACFgFunction;
import acf.acf.General.annotation.ACFgTransaction;
import acf.acf.General.core.ACFgRequestParameters;
import acf.acf.General.core.ACFgResponseParameters;
import acf.acf.General.core.ACFgSearch;
import arc.apf.Service.ARCsSection;
import arc.apf.Dao.ARCoSection;
import arc.apf.Model.ARCmSection;
import arc.apf.Service.ARCsItemCategory;
import arc.apf.Dao.ARCoItemCategory;
import arc.apf.Model.ARCmItemCategory;
import arc.apf.Static.APFtMapping;

@Controller
@Scope("session")
@ACFgFunction(id="APFF009")
@RequestMapping(value=APFtMapping.APFF009)
public class APFc009 extends ACFaAppController {

	@Autowired ARCoSection sectionDao;
	@Autowired ARCsSection sectionService;
	
	@Autowired ARCoItemCategory itemCategoryDao;
	@Autowired ARCsItemCategory itemCategoryService;
	
	@ACFgAuditKey String section_id;	
	@ACFgAuditKey String item_category_no;	
	
	Search search = new Search();
	
	private class Search extends ACFgSearch {
		public Search() {
			super();
			setModel(ARCmSection.class);
			addRule(new ACFdSQLRule("section_id", RuleCondition.EQ, null, RuleCase.Insensitive));			
			
			setModel(ARCmItemCategory.class);
			addRule(new ACFdSQLRule("section_id", RuleCondition.EQ, null, RuleCase.Insensitive));			
		}
	}

	@RequestMapping(value=APFtMapping.APFF009_MAIN, method=RequestMethod.GET)
	public String main(ModelMap model) throws Exception {

		model.addAttribute("itemcat", itemCategoryService.getAllItemCategory());
		
		model.addAttribute("sectionid", sectionService.getAllSectionValue());
		getResponseParameters().set("s_section_id", sectionService.getAllSectionValue());
		
		return view();
	}

	@RequestMapping(value=APFtMapping.APFF009_SEARCH_AJAX, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters getGrid(@RequestBody ACFgRequestParameters param) throws Exception {

		search.setConnection(getConnection("ARCDB"));
		search.setValues(param);
		search.setFocus(section_id);

		return new ACFgResponseParameters().set("grid_browse", search.getGridResult());
	}

	@RequestMapping(value=APFtMapping.APFF009_GET_FORM_AJAX, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters getForm(@RequestBody ACFgRequestParameters param) throws Exception {
		
		section_id = param.get("section_id", String.class);
		item_category_no = param.get("item_category_no", String.class);
		return new ACFgResponseParameters().set("frm_main", itemCategoryDao.selectItem(section_id, item_category_no));
	}
	
	@ACFgTransaction
	@RequestMapping(value=APFtMapping.APFF009_SAVE_AJAX, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters save(@RequestBody ACFgRequestParameters param) throws Exception {
		
		List<ARCmItemCategory> amendments = param.getList("form", ARCmItemCategory.class);
		ARCmItemCategory item = itemCategoryDao.saveItems(amendments);
		
		return new ACFgResponseParameters();
	}

}