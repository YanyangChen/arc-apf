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

import arc.apf.Service.ARCsBusinessPlatform;
import arc.apf.Dao.ARCoBusinessPlatform;
import arc.apf.Model.ARCmBusinessPlatform;
import arc.apf.Static.APFtMapping;

@Controller
@Scope("session")
@ACFgFunction(id="APFF007")
@RequestMapping(value=APFtMapping.APFF007)
public class APFc007 extends ACFaAppController {

	@Autowired ARCoBusinessPlatform businessPlatformDao;
	@Autowired ARCsBusinessPlatform businessPlatformService;

	@ACFgAuditKey String business_platform;	
	@ACFgAuditKey String department;	
	
	Search search = new Search();
	
	private class Search extends ACFgSearch {
		public Search() {
			super();
			
    		setCustomSQL("select * from (select t1.business_platform, t1.department, t1.description " +
                    "from arc_business_platform t1 " +
                    "order by t1.business_platform, t1.department)");
            setKey("business_platform","department");	
            
			addRule(new ACFdSQLRule("business_platform", RuleCondition.EQ, null, RuleCase.Insensitive));			
			
		}
	}

	@RequestMapping(value=APFtMapping.APFF007_MAIN, method=RequestMethod.GET)
	public String main(ModelMap model) throws Exception {

		model.addAttribute("busiplatform", businessPlatformService.getAllBusinessPlatformValue());

		getResponseParameters().set("s_business_platform", businessPlatformService.getAllBusinessPlatformValue());
		
		return view();
	}

	@RequestMapping(value=APFtMapping.APFF007_SEARCH_AJAX, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters getGrid(@RequestBody ACFgRequestParameters param) throws Exception {

		search.setConnection(getConnection("ARCDB"));
		search.setValues(param);
		search.setFocus(business_platform);

		return new ACFgResponseParameters().set("grid_browse", search.getGridResult());
	}

	
    @RequestMapping(value=APFtMapping.APFF007_GET_BUSI_DESC_AJAX, method=RequestMethod.POST)
    @ResponseBody
    public ACFgResponseParameters getProgramme(@RequestBody ACFgRequestParameters param) throws Exception {
		setAuditKey("business_platform", param.get("business_platform", String.class));
		getResponseParameters().put("busi_description", businessPlatformService.getBusiPlatformDesc(param.get("business_platform", String.class), "00"));
		return getResponseParameters();
    } 
    
	@RequestMapping(value=APFtMapping.APFF007_GET_FORM_AJAX, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters getForm(@RequestBody ACFgRequestParameters param) throws Exception {
		
		business_platform = param.get("business_platform", String.class);
		department = param.get("department", String.class);
		return new ACFgResponseParameters().set("frm_main", businessPlatformDao.selectItem(business_platform, department));
	}
	
	@ACFgTransaction
	@RequestMapping(value=APFtMapping.APFF007_SAVE_AJAX, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters save(@RequestBody ACFgRequestParameters param) throws Exception {
		
		List<ARCmBusinessPlatform> amendments = param.getList("form", ARCmBusinessPlatform.class);	
		ARCmBusinessPlatform  bp = amendments.get(0);
		int action = bp.getAction();			
		String p_dept = "00";
	    String p_business_platform = amendments.get(0).business_platform;
		
		if ((action == 1) && (!amendments.get(0).department.equals("00"))){
						
			if (businessPlatformDao.selectItem(p_business_platform, p_dept)==null){
						throw exceptionService.error("APF002E");
			}	
		}

		if((action == 3) && (amendments.get(0).department.equals("00"))){
			if(businessPlatformService.isDeptExisted(p_business_platform))
				throw exceptionService.error("APF003E");
		}
		
		businessPlatformDao.saveItems(amendments);
				
		return new ACFgResponseParameters();
	}

}
