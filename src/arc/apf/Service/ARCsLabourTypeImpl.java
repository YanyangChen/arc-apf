package arc.apf.Service;

import java.util.List;
import java.util.regex.Pattern;
import java.math.BigDecimal;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acf.acf.Database.ACFdSQLAssSelect;
import acf.acf.Abstract.ACFaAppService;
import acf.acf.General.core.ACFgRawModel;
import acf.acf.Static.ACFtDBUtility;
import acf.acf.Static.ACFtUtility;
import arc.apf.Dao.ARCoLabourType;

@Service
public class ARCsLabourTypeImpl extends ARCsLabourType {
	
	@Autowired ARCoLabourType labourTypeDao;
	
	public ARCsLabourTypeImpl() throws Exception {
		super();
	}

//		public String getLabourType(String labour_type, int length) throws Exception {
//		return labour_type;
//	}
	
	public List<ACFgRawModel> getAllEffLabourType() throws Exception {
			ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
			ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
			
			ass.setCustomSQL("select distinct labour_type as id, labour_type  || ' - ' || labour_type_description as text from arc_labour_type " + 
							 "where effective_from_date <= current timestamp and effective_to_date >= current timestamp " +
							 "order by labour_type");

			return ass.executeQuery();

	}
	
	public List<ACFgRawModel> getAllEffLabourType_4_paintshop() throws Exception {
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
		ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
		
		ass.setCustomSQL("select distinct labour_type as id, labour_type  || ' - ' || labour_type_description as text from arc_labour_type " + 
						 "where effective_from_date <= current timestamp and effective_to_date >= current timestamp " +
						 "and section_id = '04'" +
						 "order by labour_type");

		return ass.executeQuery();

}
	
	public List<ACFgRawModel> getAllEffLabourType_4_woodshop() throws Exception {
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
		ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
		
		ass.setCustomSQL("select distinct labour_type as id, labour_type  || ' - ' || labour_type_description as text from arc_labour_type " + 
						 "where effective_from_date <= current timestamp and effective_to_date >= current timestamp " +
						 "and section_id = '03'" +
						 "order by labour_type");

		return ass.executeQuery();

}
	

	public List<ACFgRawModel> getAllEffLabourTypebySection(String sectionid) throws Exception {  // for labour type combo box,CN,2017/05/15
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
		ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
		
		ass.setCustomSQL("select distinct labour_type as id, labour_type  || ' - ' || labour_type_description as text from arc_labour_type " + 
						 "where effective_from_date <= current timestamp and effective_to_date >= current timestamp " +
				         "  and section_id = '"+sectionid+"' " +
						 "order by labour_type");

		return ass.executeQuery();

	}
	
	public BigDecimal getEffHourlyRatebyLabour(String labourtype, Timestamp inputdate) throws Exception {  
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();

		ass.setCustomSQL("select * from arc_labour_type " + 
						 "where effective_from_date <= '"+inputdate+"' and effective_to_date >= '"+inputdate+"' " +
				         "  and labour_type = '"+labourtype+"' ");

		List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
		return result.size()>0 ? result.get(0).getBigDecimal("hourly_rate") : BigDecimal.ONE;

	}

    public  List<ACFgRawModel> getLabourUnits(String labour_type) throws Exception
    {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
       
        ass.setCustomSQL(
                "SELECT * from arc_labour_type l "+
                "WHERE effective_from_date <= current timestamp and effective_to_date >= current timestamp and l.labour_type = '%s'"
                ,labour_type);
        
        List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        return result;
        
    }

}