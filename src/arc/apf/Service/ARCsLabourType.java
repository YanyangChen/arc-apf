package arc.apf.Service;

import java.util.List;
import java.math.BigDecimal;
import java.sql.Timestamp;

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
public abstract class ARCsLabourType extends ACFaAppService{
    
    public ARCsLabourType() throws Exception {
        super();
        // TODO Auto-generated constructor stub
    }

    public abstract List<ACFgRawModel> getLabourUnits(String labour_type) throws Exception;//for combobox 2017/03/21
//    public abstract List<ACFgRawModel> getLabourType() throws Exception;//for combobox 2017/03/21
   
	public abstract List<ACFgRawModel> getAllEffLabourType() throws Exception;
	public abstract List<ACFgRawModel> getAllEffLabourType_4_paintshop() throws Exception;
	
	public abstract List<ACFgRawModel> getAllEffLabourTypebySection(String sectionid) throws Exception;  // for labour type combo box,CN,2015/05/15
	public abstract BigDecimal getEffHourlyRatebyLabour(String labourtype, Timestamp inputdate) throws Exception;  // for labour consumption,CN,2017/05/15
}