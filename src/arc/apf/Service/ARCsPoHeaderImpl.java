package arc.apf.Service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import acf.acf.Abstract.ACFaAppController;
import acf.acf.Database.ACFdSQLAssSelect;
import acf.acf.General.core.ACFgRawModel;
import acf.acf.General.core.ACFgRequestParameters;
import acf.acf.General.core.ACFgResponseParameters;
import acf.acf.Static.ACFtDBUtility;
import arc.apf.Dao.ARCoAutoGenNo;
import arc.apf.Model.ARCmAutoGenNo;
import arc.apf.Static.APFtUtilityAndGlobal;


@Service
public class ARCsPoHeaderImpl extends ARCsPoHeader {

	 @Autowired ARCoAutoGenNo AutoGenDao;
    public ARCsPoHeaderImpl() throws Exception {
        super();
        // TODO Auto-generated constructor stub
    }
   
    public String getSectionNameById(String section_id) throws Exception
    {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
       /* ass.setCustomSQL("SELECT listagg(staff_name,chr(9)) as staff_name from ( " +
                         "  SELECT v.*, case when chi_name <> '' then chi_name else eng_name end as staff_name FROM APM_LOCAL_TEMP_PROG_PROD_MEMBER_VIEW v " +
                         ") " +
                         "WHERE prog_no = '%s' " +
                         "AND member_type = '%s' "
                         ,prog_no, staff_type);*/
        ass.setCustomSQL(
                "SELECT s.section_name from arc_section s "+
                "WHERE s.section_id = '%s'"
                ,section_id);
        
        List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        
        return result.size()>0 ? result.get(0).getString("section_name") : "";
        
    }
    
    public List<ACFgRawModel> getSupplierCode() throws Exception {// for apwf003 and apwf004 combobox 2017/02/01
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        //ass.setCustomSQL("select mod_id as id, mod_id || ' - ' || mod_name as text from acf_module order by mod_seq");
        ass.setCustomSQL("select distinct supplier_code as id, supplier_code as text from arc_po_header");
        return ass.executeQuery();
        
    }
    
    public List<ACFgRawModel> getPurchaseOrderNo() throws Exception {// for apwf004 combobox 2017/02/01
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        //ass.setCustomSQL("select mod_id as id, mod_id || ' - ' || mod_name as text from acf_module order by mod_seq");
        ass.setCustomSQL("select distinct purchase_order_no as id, purchase_order_no as text from arc_po_header");
        return ass.executeQuery();
        
    }
    
    public String generate_name() throws Exception { 
        ACFgResponseParameters resParam = new ACFgResponseParameters();
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        java.text.DecimalFormat df = new java.text.DecimalFormat("#####0");
        ass.setCustomSQL("select * from arc_auto_gen_no "
                + "where form_id = 'WP'");
        
        List<ACFgRawModel> name = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        BigDecimal six_digit_serial_no_addone = name.get(0).getBigDecimal("six_digit_serial_no");
        String form_id = name.get(0).getString("form_id");
        BigDecimal system_year = name.get(0).getBigDecimal("system_year");
        BigDecimal system_month = name.get(0).getBigDecimal("system_month");
        six_digit_serial_no_addone = six_digit_serial_no_addone.add(new BigDecimal(1)); //po number add one
        ARCmAutoGenNo updatelist = AutoGenDao.selectItem(form_id, system_year, system_month);
        System.out.println(updatelist.six_digit_serial_no);
        updatelist.six_digit_serial_no = six_digit_serial_no_addone;
        AutoGenDao.updateItem(updatelist); // update to database after added
        
        
        ass.setCustomSQL("select * from arc_auto_gen_no "
                + "where form_id = 'WP'");
        
        List<ACFgRawModel> newname = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        resParam.put("form_id",  newname.get(0).getString("form_id"));
        resParam.put("six_digit_serial_no",  APFtUtilityAndGlobal.m_Lpad(df.format(newname.get(0).getBigDecimal("six_digit_serial_no")),"0",6));
        String a = "WP" + APFtUtilityAndGlobal.m_Lpad(df.format(newname.get(0).getBigDecimal("six_digit_serial_no")),"0",6);
        //return resParam;
        return a;
    }

    public String generate_name_4_PP() throws Exception { 
        ACFgResponseParameters resParam = new ACFgResponseParameters();
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        java.text.DecimalFormat df = new java.text.DecimalFormat("#####0");
        ass.setCustomSQL("select * from arc_auto_gen_no "
                + "where form_id = 'PP'");
        
        List<ACFgRawModel> name = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        BigDecimal six_digit_serial_no_addone = name.get(0).getBigDecimal("six_digit_serial_no");
        String form_id = name.get(0).getString("form_id");
        BigDecimal system_year = name.get(0).getBigDecimal("system_year");
        BigDecimal system_month = name.get(0).getBigDecimal("system_month");
        six_digit_serial_no_addone = six_digit_serial_no_addone.add(new BigDecimal(1)); //po number add one
        ARCmAutoGenNo updatelist = AutoGenDao.selectItem(form_id, system_year, system_month);
        System.out.println(updatelist.six_digit_serial_no);
        updatelist.six_digit_serial_no = six_digit_serial_no_addone;
        AutoGenDao.updateItem(updatelist); // update to database after added
        
        
        ass.setCustomSQL("select * from arc_auto_gen_no "
                + "where form_id = 'PP'");
        
        List<ACFgRawModel> newname = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        resParam.put("form_id",  newname.get(0).getString("form_id"));
        resParam.put("six_digit_serial_no",  APFtUtilityAndGlobal.m_Lpad(df.format(newname.get(0).getBigDecimal("six_digit_serial_no")),"0",6));
        String a = "PP" + APFtUtilityAndGlobal.m_Lpad(df.format(newname.get(0).getBigDecimal("six_digit_serial_no")),"0",6);
        //return resParam;
        return a;
    }
    
    public String generate_name_4_WC() throws Exception { 
        ACFgResponseParameters resParam = new ACFgResponseParameters();
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        java.text.DecimalFormat df = new java.text.DecimalFormat("#####0");
        ass.setCustomSQL("select * from arc_auto_gen_no "
                + "where form_id = 'WC'");
        
        List<ACFgRawModel> name = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        BigDecimal six_digit_serial_no_addone = name.get(0).getBigDecimal("six_digit_serial_no");
        String form_id = name.get(0).getString("form_id");
        BigDecimal system_year = name.get(0).getBigDecimal("system_year");
        BigDecimal system_month = name.get(0).getBigDecimal("system_month");
        six_digit_serial_no_addone = six_digit_serial_no_addone.add(new BigDecimal(1)); //po number add one
        ARCmAutoGenNo updatelist = AutoGenDao.selectItem(form_id, system_year, system_month);
        System.out.println(updatelist.six_digit_serial_no);
        updatelist.six_digit_serial_no = six_digit_serial_no_addone;
        AutoGenDao.updateItem(updatelist); // update to database after added
        
        
        ass.setCustomSQL("select * from arc_auto_gen_no "
                + "where form_id = 'WC'");
        
        List<ACFgRawModel> newname = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        resParam.put("form_id",  newname.get(0).getString("form_id"));
        resParam.put("six_digit_serial_no",  APFtUtilityAndGlobal.m_Lpad(df.format(newname.get(0).getBigDecimal("six_digit_serial_no")),"0",6));
        String a = "WC" + APFtUtilityAndGlobal.m_Lpad(df.format(newname.get(0).getBigDecimal("six_digit_serial_no")),"0",6);
        //return resParam;
        return a;
    }
    
    public String generate_name_4_PC() throws Exception { 
        ACFgResponseParameters resParam = new ACFgResponseParameters();
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        java.text.DecimalFormat df = new java.text.DecimalFormat("#####0");
        ass.setCustomSQL("select * from arc_auto_gen_no "
                + "where form_id = 'PC'");
        
        List<ACFgRawModel> name = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        BigDecimal six_digit_serial_no_addone = name.get(0).getBigDecimal("six_digit_serial_no");
        String form_id = name.get(0).getString("form_id");
        BigDecimal system_year = name.get(0).getBigDecimal("system_year");
        BigDecimal system_month = name.get(0).getBigDecimal("system_month");
        six_digit_serial_no_addone = six_digit_serial_no_addone.add(new BigDecimal(1)); //po number add one
        ARCmAutoGenNo updatelist = AutoGenDao.selectItem(form_id, system_year, system_month);
        System.out.println(updatelist.six_digit_serial_no);
        updatelist.six_digit_serial_no = six_digit_serial_no_addone;
        AutoGenDao.updateItem(updatelist); // update to database after added
        
        
        ass.setCustomSQL("select * from arc_auto_gen_no "
                + "where form_id = 'PC'");
        
        List<ACFgRawModel> newname = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        resParam.put("form_id",  newname.get(0).getString("form_id"));
        resParam.put("six_digit_serial_no",  APFtUtilityAndGlobal.m_Lpad(df.format(newname.get(0).getBigDecimal("six_digit_serial_no")),"0",6));
        String a = "PC" + APFtUtilityAndGlobal.m_Lpad(df.format(newname.get(0).getBigDecimal("six_digit_serial_no")),"0",6);
        //return resParam;
        return a;
    }

}