package arc.apf.Service;

import java.util.List;
import java.math.BigDecimal;
import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import acf.acf.Database.ACFdSQLAssSelect;
import acf.acf.General.core.ACFgRawModel;
import acf.acf.Static.ACFtDBUtility;

@Service
public class ARCsLabourTypempl  {
//public class ARCsLabourTypempl extends ARCsLabourType {

    public ARCsLabourTypempl() throws Exception {
        super();
        // TODO Auto-generated constructor stub
    }
   
        public  List<ACFgRawModel> getLabourUnits(String labour_type) throws Exception//for combobox 2017/03/21
    {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
       
        ass.setCustomSQL(
                "SELECT * from arc_labour_type l "+
                "WHERE l.labour_type = '%s'"
                ,labour_type);
        

        List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        return result;
        
    }
    
    public List<ACFgRawModel> getLabourType() throws Exception {//for combobox 2017/03/21
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        //ass.setCustomSQL("select mod_id as id, mod_id || ' - ' || mod_name as text from acf_module order by mod_seq");
        ass.setCustomSQL("select labour_type as id, labour_type as text from arc_labour_type order by labour_type");
        return ass.executeQuery();
        
    }

	public List<ACFgRawModel> getAllEffLabourType() throws Exception {
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
		ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
		
		ass.setCustomSQL("select distinct labour_type as id, labour_type  || ' - ' || labour_type_description as text from arc_labour_type " + 
						 "where effective_from_date <= current timestamp and effective_to_date >= current timestamp " +
						 "order by labour_type");

		return ass.executeQuery();

	}

	public List<ACFgRawModel> getAllEffLabourTypebySection(String sectionid) throws Exception {  // for labour type combo box,CN,2017/05/15
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
		ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
	
        System.out.println("apt " + sectionid);
		
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

    


}