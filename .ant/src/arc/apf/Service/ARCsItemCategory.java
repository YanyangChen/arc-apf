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


@Service
public abstract class ARCsItemCategory extends ACFaAppService{
    
    public ARCsItemCategory() throws Exception {
        super();

    }

	public abstract String getItemCategory(String item_category_no, int length) throws Exception;
	public abstract List<ACFgRawModel> getAllItemCategoryValue() throws Exception;
	public abstract List<ACFgRawModel> getAllItemCategory() throws Exception; // for apff009,CN,2017/05/15
	public abstract List<ACFgRawModel> getAllSectionItemCat(String sectionid) throws Exception; // for aphf001,CN,2017/05/15
    public abstract List<ACFgRawModel> getCat_Noapw()  throws Exception; //for apwf001 combobox, AC, 2017/03/18
    public abstract List<ACFgRawModel> getCat_Noapp()  throws Exception; //for apwf001 combobox, AC, 2017/03/18
}