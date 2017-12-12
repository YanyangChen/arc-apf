package arc.apf.Service;
//package acf.acf.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import acf.acf.Abstract.ACFaAppService;
import acf.acf.Abstract.ACFaSQLAss;
import acf.acf.Dao.ACFoModule;
import acf.acf.Dao.ACFoModuleOwner;
import acf.acf.Database.ACFdSQLAssDelete;
import acf.acf.Database.ACFdSQLAssInsert;
import acf.acf.Database.ACFdSQLAssSelect;
import acf.acf.Database.ACFdSQLAssUpdate;
import acf.acf.General.core.ACFgRawModel;
import acf.acf.General.core.ACFgRequestParameters;
import acf.acf.General.core.ACFgResponseParameters;
import acf.acf.Interface.ACFiCallback;
import acf.acf.Interface.ACFiSQLAssInterface;
import acf.acf.Interface.ACFiSQLAssWriteInterface;
import acf.acf.Model.ACFmModule;
import acf.acf.Static.ACFtDBUtility;
import acf.acf.Static.ACFtUtility;


@Service
public abstract class ARCsPoHeader extends ACFaAppService{
    
    public ARCsPoHeader() throws Exception {
        super();
        // TODO Auto-generated constructor stub
    }

    public abstract String getSectionNameById(String supplier_code) throws Exception;
    public abstract List<ACFgRawModel> getSupplierCode() throws Exception;// for apwf003 and apwf004 combobox 2017/02/01
    public abstract List<ACFgRawModel> getPurchaseOrderNo() throws Exception;// for apwf004 combobox 2017/02/01
    public abstract String generate_name() throws Exception;
    public abstract String generate_name_4_PP() throws Exception;
    public abstract String generate_name_4_WC() throws Exception;
    public abstract String generate_name_4_PC() throws Exception;
    public abstract String get_name_4_PC() throws Exception;
    public abstract String get_name_4_WC() throws Exception;
 
}