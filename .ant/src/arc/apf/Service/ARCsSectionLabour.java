package arc.apf.Service;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

import acf.acf.Abstract.ACFaAppService;
import acf.acf.Database.ACFdSQLAssSelect;
import acf.acf.General.core.ACFgRawModel;
import acf.acf.Static.ACFtDBUtility;
import acf.acf.Static.ACFtUtility;

@Service
public class ARCsSectionLabour extends ACFaAppService {
	
	public ARCsSectionLabour() throws Exception {
		super();
	}
	
	public String getSectionLabourIndex() throws Exception {
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
		ass.setCustomSQL("select t1.section_id, t1.section_id || '-' || t1.section_name, t2.labour_type, t2.labour_type || '-' || t2.labour_type_description " +
				"from arc_section t1, arc_labour_type t2 " +
				"where t1.section_id = t2.section_id " +
				"and t1.sub_section_id = '0' " +
				"and t2.effective_from_date <= current timestamp and t2.effective_to_date >= current timestamp " +
				"order by t1.section_id, t2.labour_type");
		return ACFtUtility.toJson(ass.getRows(ACFtDBUtility.getConnection("ARCDB")));
	}

	public String getLabourSubSection(String labourtype, Timestamp inputdate) throws Exception {  // for aptf003 & apcf003,CN,2017/05/31
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
		
		ass.setCustomSQL("select t1.sub_section_id  || ' - ' || t1.section_name as sub_section " + 
                         "  from arc_section t1, arc_labour_type t2 " +
                         " where t1.section_id = t2.section_id " +
                         "   and t1.sub_section_id = t2.sub_section_id " +
                         "   and t2.labour_type = '"+labourtype+"' " + 
				         "   and t2.effective_from_date <= '"+inputdate+"' and t2.effective_to_date >= '"+inputdate+"' ");

		List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
		return result.size()>0 ? result.get(0).getString("sub_section") : "";

	}

}
