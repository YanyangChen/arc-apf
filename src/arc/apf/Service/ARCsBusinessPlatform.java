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
public abstract class ARCsBusinessPlatform extends ACFaAppService{
    
    public ARCsBusinessPlatform() throws Exception {
        super();
        // TODO Auto-generated constructor stub
    }

    public abstract String getDepartmentByBusinessPlatform(String supplier_code) throws Exception;//For apff011 get content in form, AC, 2017/03/20
    
    public abstract List<ACFgRawModel> getBBusinessPlatform() throws Exception; //For apff011 combobox, AC, 2017/03/20
    
    public abstract List<ACFgRawModel> getBDepartment() throws Exception; //For apff011 combobox, AC, 2017/03/20

    public abstract String getBusinessPlatform(String business_platform, int length) throws Exception;
   	public abstract List<ACFgRawModel> getAllBusinessPlatformValue() throws Exception;    // for business platform combo box,CN,2017/05/15
   	public abstract List<ACFgRawModel> getAllDepartmentValue() throws Exception;          // for department combo box,CN,2017/05/17
   	public abstract String getAllBusinessDepartmentValue() throws Exception;              // for apff007,CN,2017/05/15
   	public abstract String getBusiPlatformDesc(String busi_platform, String dept) throws Exception;  // for apff007,CN,2017/05/15
   	public abstract boolean isDeptExisted(String busi_platform) throws Exception;  // for apff007,CN,2017/07/25
}