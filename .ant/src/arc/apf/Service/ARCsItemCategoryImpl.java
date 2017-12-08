package arc.apf.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import acf.acf.Database.ACFdSQLAssSelect;
import acf.acf.General.core.ACFgRawModel;
import acf.acf.Static.ACFtDBUtility;


@Service
public class ARCsItemCategoryImpl extends ARCsItemCategory {

    public ARCsItemCategoryImpl() throws Exception {
        super();

    }
   
	public String getItemCategory(String item_category_no, int length) throws Exception {
	return item_category_no;
	}

	public List<ACFgRawModel> getAllItemCategoryValue() throws Exception { 
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
		ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
		ass.setCustomSQL("select item_category_no as id, item_category_description as text from arc_item_category");
		return ass.executeQuery();

	}

	public List<ACFgRawModel> getAllItemCategory() throws Exception { // for apff009,CN,2017/05/15
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
		ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
		ass.setCustomSQL("select item_category_no as id, item_category_no || ' - ' || item_category_description as text from arc_item_category");
		return ass.executeQuery();

	}

	public List<ACFgRawModel> getAllSectionItemCat(String sectionid) throws Exception { // for aphf001,CN,2017/05/15
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
		ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
		ass.setCustomSQL("select item_category_no as id, item_category_no || '   ' || item_category_description as text from arc_item_category where section_id = '" +sectionid +"'");
		return ass.executeQuery();

	}
    public List<ACFgRawModel> getCat_Noapw() throws Exception {//for apwf001 combobox, AC, 2017/03/18
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        //ass.setCustomSQL("select mod_id as id, mod_id || ' - ' || mod_name as text from acf_module order by mod_seq");
        ass.setCustomSQL("select distinct item_category_no as id, item_category_no || ' - ' || item_category_description as text from arc_item_category where section_id = '03'");
        return ass.executeQuery();
        
    }
    
    public List<ACFgRawModel> getCat_Noapp() throws Exception {//for apwf001 combobox, AC, 2017/03/18
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        //ass.setCustomSQL("select mod_id as id, mod_id || ' - ' || mod_name as text from acf_module order by mod_seq");
        ass.setCustomSQL("select distinct item_category_no as id, item_category_no || ' - ' || item_category_description as text from arc_item_category where section_id = '04'");
        return ass.executeQuery();
        
    }


}