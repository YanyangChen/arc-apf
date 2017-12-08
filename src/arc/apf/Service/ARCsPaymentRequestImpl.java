package arc.apf.Service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acf.acf.Database.ACFdSQLAssSelect;
import acf.acf.General.core.ACFgRawModel;
import acf.acf.General.core.ACFgResponseParameters;
import acf.acf.Static.ACFtDBUtility;
import arc.apf.Dao.ARCoAutoGenNo;
import arc.apf.Model.ARCmAutoGenNo;
import arc.apf.Static.APFtUtilityAndGlobal;


@Service
public class ARCsPaymentRequestImpl extends ARCsPaymentRequest {

	@Autowired ARCoAutoGenNo AutoGenDao;
    public ARCsPaymentRequestImpl() throws Exception {
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
    
    public List<ACFgRawModel> getPaymentRequestByNo() throws Exception {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
  //     ass.setCustomSQL("select supplier_code as id, supplier_code || ' - ' || supplier_name as text from arc_supplier");
        ass.setCustomSQL("select payment_form_no as id, payment_form_no "
                + " as text from arc_payment_request where section_id = '08' and supplier_code <> 'CASH'");
            
        return ass.executeQuery();

    }
    public List<ACFgRawModel> getPaymentRequestByNoCash() throws Exception {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
  //     ass.setCustomSQL("select supplier_code as id, supplier_code || ' - ' || supplier_name as text from arc_supplier");
        ass.setCustomSQL("select payment_form_no as id, payment_form_no "
                + " as text from arc_payment_request where section_id = '08' and supplier_code = 'CASH'");
            
        return ass.executeQuery();

    }

    
    public String generate_no_at(String sys_yy, String sys_mm) throws Exception { 
        ACFgResponseParameters resParam = new ACFgResponseParameters();
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        java.text.DecimalFormat df = new java.text.DecimalFormat("##0");
        ass.setCustomSQL("select * from arc_auto_gen_no "
                + "where form_id = 'AT' and system_year = '"+sys_yy+"' and system_month = '"+sys_mm+"'");
        
        List<ACFgRawModel> name = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        BigDecimal three_digit_serial_no_addone = name.get(0).getBigDecimal("three_digit_serial_no");
        String form_id = name.get(0).getString("form_id");
        BigDecimal system_year = name.get(0).getBigDecimal("system_year");
        BigDecimal system_month = name.get(0).getBigDecimal("system_month");
        three_digit_serial_no_addone = three_digit_serial_no_addone.add(new BigDecimal(1)); //AT number add one
        ARCmAutoGenNo updatelist = AutoGenDao.selectItem(form_id, system_year, system_month);
      //  System.out.println("------------------------ get AT function-----------------------------" + updatelist.three_digit_serial_no);
        updatelist.three_digit_serial_no = three_digit_serial_no_addone;
        AutoGenDao.updateItem(updatelist); // update to database after added
        
       // ass.setCustomSQL("select * from arc_auto_gen_no "
       // 		+ "where form_id = 'AT' and system_year = '"+sys_yy+"' and system_month = '"+sys_mm+"'");
        
        List<ACFgRawModel> newname = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        resParam.put("form_id",  newname.get(0).getString("form_id"));
        resParam.put("three_digit_serial_no",  APFtUtilityAndGlobal.m_Lpad(df.format(newname.get(0).getBigDecimal("three_digit_serial_no")),"0",3));
        String sysmonth = system_month.toString();
        if (sysmonth.length() == 1)
        {sysmonth = "0" + sysmonth;}
        String a = newname.get(0).getString("form_id") + system_year  + sysmonth + APFtUtilityAndGlobal.m_Lpad(df.format(newname.get(0).getBigDecimal("three_digit_serial_no")),"0",3);
        System.out.println("------------------------ display name-----------------------------" + a);
        //return resParam;
        return a;
    }

    public String generate_no_pat(String sys_yy, String sys_mm) throws Exception { 
        ACFgResponseParameters resParam = new ACFgResponseParameters();
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        java.text.DecimalFormat df = new java.text.DecimalFormat("##0");
        ass.setCustomSQL("select * from arc_auto_gen_no "
                + "where form_id = 'PAT' and system_year = '"+sys_yy+"' and system_month = '"+sys_mm+"'");
        
        List<ACFgRawModel> name = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        BigDecimal three_digit_serial_no_addone = name.get(0).getBigDecimal("three_digit_serial_no");
        String form_id = name.get(0).getString("form_id");
        BigDecimal system_year = name.get(0).getBigDecimal("system_year");
        BigDecimal system_month = name.get(0).getBigDecimal("system_month");
        three_digit_serial_no_addone = three_digit_serial_no_addone.add(new BigDecimal(1)); //po number add one
        ARCmAutoGenNo updatelist = AutoGenDao.selectItem(form_id, system_year, system_month);
        System.out.println("------------------------ get PAT function-----------------------------" + updatelist.three_digit_serial_no);
   //     System.out.println(updatelist.three_digit_serial_no);
        updatelist.three_digit_serial_no = three_digit_serial_no_addone;
        AutoGenDao.updateItem(updatelist); // update to database after added
        
        
   //     ass.setCustomSQL("select * from arc_auto_gen_no "
   //             + "where form_id = 'PAT' and system_year = '"+sys_yy+"' and system_month = '"+sys_mm+"'");
   //     
        List<ACFgRawModel> newname = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        resParam.put("form_id",  newname.get(0).getString("form_id"));
        resParam.put("three_digit_serial_no",  APFtUtilityAndGlobal.m_Lpad(df.format(newname.get(0).getBigDecimal("three_digit_serial_no")),"0",3));
        String sysmonth = system_month.toString();
        if (sysmonth.length() == 1)
        {sysmonth = "0" + sysmonth;}
        String a = newname.get(0).getString("form_id") + system_year  + sysmonth + APFtUtilityAndGlobal.m_Lpad(df.format(newname.get(0).getBigDecimal("three_digit_serial_no")),"0",3);
        System.out.println("------------------------ display name-----------------------------" + a);
        //return resParam;
        return a;
    }  
    



}