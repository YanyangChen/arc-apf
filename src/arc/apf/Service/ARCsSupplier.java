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
public abstract class ARCsSupplier extends ACFaAppService{
    
    public ARCsSupplier() throws Exception {
        super();
        // TODO Auto-generated constructor stub
    }
    public abstract String getSupplierNameById(String supplier_code) throws Exception;
    public abstract List<ACFgRawModel> getSupplierPairsExcCash() throws Exception;
    public abstract String getSupplierNameCash(String supplier_code) throws Exception;

    //public abstract List<ACFgRawModel> getSupplierPairs() throws Exception;
    public abstract String getSectionNameById(String supplier_code) throws Exception;//for apfc ajax retrieve， AC, 2017/04/10
    public abstract List<ACFgRawModel> getSupplierCodefromitem() throws Exception;//for apwc combobox， AC, 2017/04/10 
    public abstract List<ACFgRawModel> getSupplierPairs() throws Exception;
    
    public abstract String getSupplierDesc(String suppliercode) throws Exception;//for despatch date maint. get supplier desc,CN, 2017/06/12
    
}