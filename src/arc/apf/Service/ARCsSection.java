package arc.apf.Service;
//package acf.acf.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acf.acf.Abstract.ACFaAppService;
import acf.acf.Abstract.ACFaSQLAss;
import acf.acf.Dao.ACFoModule;
import acf.acf.Dao.ACFoModuleOwner;
import acf.acf.Database.ACFdSQLAssDelete;
import acf.acf.Database.ACFdSQLAssInsert;
import acf.acf.Database.ACFdSQLAssSelect;
import acf.acf.Database.ACFdSQLAssUpdate;
import acf.acf.General.core.ACFgRawModel;
import acf.acf.Interface.ACFiCallback;
import acf.acf.Interface.ACFiSQLAssInterface;
import acf.acf.Interface.ACFiSQLAssWriteInterface;
import acf.acf.Model.ACFmModule;
import acf.acf.Static.ACFtDBUtility;
import acf.acf.Static.ACFtUtility;


@Service
public abstract class ARCsSection extends ACFaAppService{
    
    public ARCsSection() throws Exception {
        super();
        // TODO Auto-generated constructor stub
    }

    public abstract String getSectionNameByID(String supplier_code) throws Exception;// for APF ajax retrieve AC 2017/02/01
    public abstract List<ACFgRawModel> getSectionId() throws Exception;// for APF combobox AC 2017/02/01
    public abstract List<ACFgRawModel> getSubSectionId() throws Exception;
    public abstract String getSubSectionAndSectionId() throws Exception;
    public abstract String getSectionNameByIDAndSubID(String supplier_code, String sub_section_id) throws Exception; // for APF ajax retrieve AC 2017/02/01
 
	public abstract List<ACFgRawModel> getAllSectionValue() throws Exception;  // for section combo box,CN,2017/05/15
	public abstract List<ACFgRawModel> getAllSubSection(String sectionid) throws Exception;  // for sub section combo box,CN,2017/05/15
	public abstract List<ACFgRawModel> getSectionValueExcl349() throws Exception;  // for apff106 section combo box,CN,2017/07/28
	public abstract String getSectionName(String sectionid) throws Exception;  // for apff106 display section name in grid result,CN,2017/06/30

    public abstract String getSectionNameById(String section_id) throws Exception;  // SF
    public abstract List<ACFgRawModel> getAllSectionValuePairs() throws Exception;  // SF 
    public abstract List<ACFgRawModel> getAllSectionName34() throws Exception;	    // SF
    public abstract List<ACFgRawModel> getAllSubSectionName8() throws Exception;    // SF 
    public abstract List<ACFgRawModel> getAllSubSectionName7() throws Exception;    // SF 
    
}