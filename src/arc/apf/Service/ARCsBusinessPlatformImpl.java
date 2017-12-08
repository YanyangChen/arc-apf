package arc.apf.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acf.acf.Database.ACFdSQLAssSelect;
import acf.acf.General.core.ACFgRawModel;
import acf.acf.Static.ACFtDBUtility;
import acf.acf.Static.ACFtUtility;
import arc.apf.Dao.ARCoBusinessPlatform;


@Service
public class ARCsBusinessPlatformImpl extends ARCsBusinessPlatform {

    public ARCsBusinessPlatformImpl() throws Exception {
        super();
        // TODO Auto-generated constructor stub
    }
   
    public String getDepartmentByBusinessPlatform(String business_platform) throws Exception//For apff011 get content in form, AC, 2017/03/20
    {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
       /* ass.setCustomSQL("SELECT listagg(staff_name,chr(9)) as staff_name from ( " +
                         "  SELECT v.*, case when chi_name <> '' then chi_name else eng_name end as staff_name FROM APM_LOCAL_TEMP_PROG_PROD_MEMBER_VIEW v " +
                         ") " +
                         "WHERE prog_no = '%s' " +
                         "AND member_type = '%s' "
                         ,prog_no, staff_type);*/
        ass.setCustomSQL(
                "SELECT bp.department from arc_business_platform bp "+
                "WHERE bp.business_platform = '%s'"
                ,business_platform);
        
        List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        
        return result.size()>0 ? result.get(0).getString("department") : "";
        
    }
    
    public List<ACFgRawModel> getBBusinessPlatform() throws Exception {//For apff011 get content in form, AC, 2017/03/20
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        //ass.setCustomSQL("select mod_id as id, mod_id || ' - ' || mod_name as text from acf_module order by mod_seq");
        ass.setCustomSQL("select distinct business_platform as id, business_platform || ' - ' || description as text from arc_business_platform where department = '00' order by business_platform");
        return ass.executeQuery();
        
    }

    public List<ACFgRawModel> getBDepartment() throws Exception {//For apff011 get content in form, AC, 2017/03/20
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        //ass.setCustomSQL("select mod_id as id, mod_id || ' - ' || mod_name as text from acf_module order by mod_seq");
        ass.setCustomSQL("select department as id, business_platform || ' - ' || department || ' - ' || Description as text from arc_business_platform where department != '00' order by department asc");
        return ass.executeQuery();
        
    }

	public String getBusinessPlatform(String business_platform, int length) throws Exception {
		return business_platform;
	}
    
	public List<ACFgRawModel> getAllBusinessPlatformValue() throws Exception {  // for business platform combo box,CN,2017/05/15
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
		ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
		ass.setCustomSQL("select business_platform as id, business_platform  || ' - ' || description as text from arc_business_platform where department = '00' order by business_platform");
		return ass.executeQuery();

}


	public List<ACFgRawModel> getAllDepartmentValue() throws Exception {  // for department combo box,CN,2017/05/15
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
		ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
		ass.setCustomSQL("select t1.department as id, t1.department || '-' || t1.description " +
				"from arc_business_platform t1 " +
				"where t1.department > '00' " +
				"order by t1.business_platform, t1.department");
		return ass.executeQuery();

	}

	public String getAllBusinessDepartmentValue() throws Exception {  // for apff007,CN,2017/05/15
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
		ass.setCustomSQL("select t1.business_platform, t1.business_platform || '- ' || t1.description, t2.department, t2.department  || '-' || t2.description " +
				"from arc_business_platform t1, arc_business_platform t2 " +
				"where t1.business_platform = t2.business_platform and " +
				"      t1.department = '00' and " +
				"      t2.department > '00'  " +
				"order by t1.business_platform, t2.department");
		return ACFtUtility.toJson(ass.getRows(ACFtDBUtility.getConnection("ARCDB")));
	}

	public String getBusiPlatformDesc(String busi_platform, String dept) throws Exception {  // for apff007,CN,2017/05/15
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
		ass.setCustomSQL("select * from (select description as busi_description from arc_business_platform " +
            		  	 "where business_platform = '"+busi_platform+"' and department = '"+dept+"')");
		List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
		return result.size()>0 ? result.get(0).getString("busi_description") : "";

	}

	public boolean isDeptExisted(String busi_platform) throws Exception {     // for apff007,CN,2017/07/25
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
		ass.setCustomSQL("select * from arc_business_platform " +
   		  	 "where business_platform = '"+busi_platform+"' and department != '00'");
		List<ACFgRawModel> list = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
		return !list.isEmpty();
	}

}