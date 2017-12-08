package arc.apf.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import acf.acf.Database.ACFdSQLAssSelect;
import acf.acf.General.core.ACFgRawModel;
import acf.acf.Static.ACFtDBUtility;

import arc.apf.Model.ARCmAutoGenNo;

@Service
public class ARCsAutoGenNoImpl extends ARCsAutoGenNo {

    public ARCsAutoGenNoImpl() throws Exception {
        super();
        // TODO Auto-generated constructor stub
    }
   
	public List<ACFgRawModel> getAllFormIdValue() throws Exception { // for apff008,CN,2017/05/15
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
		ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
		ass.setCustomSQL("select distinct form_id as id, form_id  || ' - ' || description as text from arc_auto_gen_no order by form_id");
		return ass.executeQuery();

	}
 
	public List<ARCmAutoGenNo> getPOForm(String formid, String sys_yy, String sys_mm) throws Exception {  // for apff008,CN,2017/05/15
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
		ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        ass.setCustomSQL("select form_id, description, system_year || '/' || system_month as sys_yymm, " + 
                "six_digit_serial_no as last_auto_no from arc_auto_gen_no where form_id = '"+formid+"' and system_year = '"+sys_yy+"' and system_month = '"+sys_mm+"'");
		return ass.executeQuery();


	}
	
	public List<ARCmAutoGenNo> getPRForm(String formid, String sys_yy, String sys_mm) throws Exception {  // for apff008,CN,2017/05/15
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
		ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        ass.setCustomSQL("select form_id, description, system_year || '/' || system_month as sys_yymm, " + 
                "three_digit_serial_no as last_auto_no from arc_auto_gen_no where form_id = '"+formid+"' and system_year = '"+sys_yy+"' and system_month = '"+sys_mm+"'");
        return ass.executeQuery();

	}


}