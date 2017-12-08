package arc.apf.Service;
//package acf.acf.Service;

import java.math.BigDecimal;
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
public abstract class ARCsOtherMaterial extends ACFaAppService{
    
    public ARCsOtherMaterial() throws Exception {
        super();
        // TODO Auto-generated constructor stub
    }

    public abstract String getSectionNameById(String supplier_code) throws Exception;
    public abstract List<ACFgRawModel> getOtherMaterial() throws Exception;//for combobox, AC, 2017/03/10
    public abstract String getUnitcost(String other_material) throws Exception;//for ajax retrieve, AC, 2017/05/02
}