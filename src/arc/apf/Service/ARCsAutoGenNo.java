package arc.apf.Service;

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


import arc.apf.Model.ARCmAutoGenNo;

@Service
public abstract class ARCsAutoGenNo extends ACFaAppService{
    
    public ARCsAutoGenNo() throws Exception {
        super();
    }
   
	public abstract List<ACFgRawModel> getAllFormIdValue() throws Exception; // for apff008,CN,2017/05/15
	public abstract List<ARCmAutoGenNo> getPOForm(String form_id, String sys_yy, String sys_mm) throws Exception; // for apff008,CN,2017/05/15
	public abstract List<ARCmAutoGenNo> getPRForm(String form_id, String sys_yy, String sys_mm) throws Exception; // for apff008,CN,2017/05/15

    
}