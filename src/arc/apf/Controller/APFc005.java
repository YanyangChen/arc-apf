// package arc.apf.Controller;
//
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Scope;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.ModelMap;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.web.bind.annotation.ResponseBody;

// import acf.acf.Abstract.ACFaAppController;
// import acf.acf.General.annotation.ACFgFunction;
// import arc.apf.Static.APFtMapping;

// @Controller
// @Scope("session")
// @ACFgFunction(id="APFF002")
// @RequestMapping(value=APFtMapping.APFF002)
// public class APFc002 extends ACFaAppController {
// 
// 	@RequestMapping(value=APFtMapping.APFF002_MAIN, method=RequestMethod.GET)
// 	public String main(ModelMap model) throws Exception {
// 		return view();
// 	}
// 
// 
// }

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

import acf.acf.Database.ACFdSQLAssDelete;
import acf.acf.Database.ACFdSQLAssInsert;
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
import acf.acf.Interface.ACFiSQLAssWriteInterface;
import acf.acf.Abstract.ACFaAppController;
import arc.apf.Dao.APFoLocation;
import arc.apf.Dao.ARCoSupplier;
import arc.apf.Dao.ARCoGroup;
import arc.apf.Model.APFmLocation;
import arc.apf.Model.ARCmGroup;
import arc.apf.Model.ARCmSupplier;
import arc.apf.Service.APFsModule;
import arc.apf.Service.ARCsSection;
import arc.apf.Service.ARCsSupplier;
import arc.apf.Service.ARCsGroup;
import arc.apf.Static.APFtMapping;
//import arc.apw.Static.APWtMapping;

@Controller
@Scope("session")
@ACFgFunction(id="APFF005")
@RequestMapping(value=APFtMapping.APFF005)
public class APFc005 extends ACFaAppController {

    @Autowired ARCsGroup groupService;
    @Autowired ARCsSection sectionService;
    @Autowired ARCoGroup groupDao;
   // @Autowired APFsFuncGp funcGpService; //click the object and click import
    @ACFgAuditKey String group_no;
    @ACFgAuditKey String section_id;
    @ACFgAuditKey String sub_section_id;
    

    private class Search extends ACFgSearch {
        public Search() {
            super();
            setCustomSQL("select * from (select gp.section_id, gp.sub_section_id, se.section_name, gp.group_no, gp.group_name " +
                    "from arc_section se, arc_group gp " +
                    "where se.section_id = gp.section_id " +
                    "and se.sub_section_id = gp.sub_section_id" +
                    " and se.sub_section_id = '0')");  
              setKey("section_id","sub_section_id","group_no");
    //        setModel(ARCmGroup.class);
            addRule(new ACFdSQLRule("group_no", RuleCondition.EQ, null, RuleCase.Insensitive));
            addRule(new ACFdSQLRule("section_id", RuleCondition.EQ, null, RuleCase.Insensitive));
        }
    }
    Search search = new Search();
    @RequestMapping(value=APFtMapping.APFF005_MAIN, method=RequestMethod.GET)
    public String main(ModelMap model) throws Exception {
        
        model.addAttribute("SectionNo", sectionService.getAllSectionName34());
        return view();
    }
    
    ////form maintenance here, how to modify? imoprt apff001.java and remove grid in jsp file
    @RequestMapping(value=APFtMapping.APFF005_SEARCH_AJAX, method=RequestMethod.POST)
    @ResponseBody
     public ACFgResponseParameters getGrid(@RequestBody ACFgRequestParameters param) throws Exception {
          //The method getGrid responds to AJAX by obtain the Search JSON result and put in variable “grid_browse”.
            // ACF will forward the content to client and post to the grid which ID equals to “grid_browse”.
            search.setConnection(getConnection("ARCDB")); //get connection to the database
            search.setValues(param);
            
            System.out.println("param:"+param);
            search.setFocus(section_id, sub_section_id, group_no);
           // System.out.println("param:"+param);
           // System.out.println(search.getGridResult());
            return new ACFgResponseParameters().set("grid_browse", search.getGridResult()); // can only be called once, otherwise reset parameter
        }
    
    @RequestMapping(value=APFtMapping.APFF005_GET_SECTION_NAME_AJAX, method=RequestMethod.POST)
    @ResponseBody
    public ACFgResponseParameters getSectionName(@RequestBody ACFgRequestParameters param) throws Exception {
        setAuditKey("section_id", param.get("section_id", String.class));
       // getResponseParameters().put("programme",        progService.unfillItem(progService.getActTempProgramme(param.get("prog_no", String.class))));
       // getResponseParameters().put("producer_code",    prodMemberService.getProducerListValuePairs(param.get("prog_no", String.class)));
       // getResponseParameters().put("aa_staff",         prodMemberService.getStaffNameByProgNo((param.get("prog_no", String.class)), "AA"));
        getResponseParameters().put("section_name",       sectionService.getSectionNameByID((param.get("section_id", String.class))));
      //getResponseParameters().put("ep",               prodMemberService.getStaffByProgNo(param.get("prog_no", String.class), "EP"));
        return getResponseParameters();
    } 
    
    
     @RequestMapping(value=APFtMapping.APFF005_GET_FORM_AJAX, method=RequestMethod.POST)
        @ResponseBody
        public ACFgResponseParameters getForm(@RequestBody ACFgRequestParameters param) throws Exception {
            group_no = param.get("group_no", String.class); //pick the value of parameter “func_id” from client
            section_id = param.get("section_id", String.class);
            sub_section_id = param.get("sub_section_id", String.class);
            //retrieves the result by DAO, and put in the variable “frm_main”. 
            //ACF will forward the content to client and post to the form which ID equals to “frm_main”
            return new ACFgResponseParameters().set("frm_main", groupDao.selectItem(section_id,sub_section_id,group_no)); //change dao here
        }
     @ACFgTransaction
        @RequestMapping(value=APFtMapping.APFF005_SAVE_AJAX, method=RequestMethod.POST)
        @ResponseBody
        public ACFgResponseParameters save(@RequestBody ACFgRequestParameters param) throws Exception { //function in the upper right "save" button
          //the controller obtains the changes of form data 
            List<ARCmGroup> amendments = param.getList("form", ARCmGroup.class);
            //and call DAO to save the changes
            ARCmGroup lastItem = groupDao.saveItems(amendments, new ACFiSQLAssWriteInterface<ARCmGroup>(){
                
                
                //interface for the related functions
                @Override
                public boolean insert(ARCmGroup newItem, ACFdSQLAssInsert ass) throws Exception {
                   return false;
                }

                @Override
                public boolean update(ARCmGroup oldItem, ARCmGroup newItem, ACFdSQLAssUpdate ass) throws Exception {
                    return false;
                }

                @Override
                public boolean delete(ARCmGroup oldItem, ACFdSQLAssDelete ass) throws Exception {
                    return false;
                }
            });
            group_no = lastItem!=null? lastItem.group_no: null;

            return new ACFgResponseParameters();
        }
}


