package arc.apf.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import acf.acf.Database.ACFdSQLAssSelect;
import acf.acf.General.core.ACFgRawModel;
import acf.acf.Static.ACFtDBUtility;
import acf.acf.Static.ACFtUtility;


@Service
public class ARCsSectionImpl extends ARCsSection {

    public ARCsSectionImpl() throws Exception {
        super();
        // TODO Auto-generated constructor stub
    }
   
    public String getSectionNameByID(String section_id) throws Exception// for APF ajax retrieve AC 2017/02/01
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
    
    public List<ACFgRawModel> getSectionId() throws Exception {// for APF combobox AC 2017/02/01
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        //ass.setCustomSQL("select mod_id as id, mod_id || ' - ' || mod_name as text from acf_module order by mod_seq");
        ass.setCustomSQL("select section_id as id, section_id || ' - ' || section_name as text from arc_section where sub_section_id = 0");
        return ass.executeQuery();
        
    }
    
    public List<ACFgRawModel> getSubSectionId() throws Exception {// for APF combobox AC 2017/02/01
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        //ass.setCustomSQL("select mod_id as id, mod_id || ' - ' || mod_name as text from acf_module order by mod_seq");
        ass.setCustomSQL("select sub_section_id, section_id, section_name, section_id || '-' || sub_section_id as id,  section_id || '-' || sub_section_id || '-' || section_name as text from arc_section "
        		+ "order by section_id, sub_section_id");
        return ass.executeQuery();
        
    }
    
    public String getSubSectionAndSectionId() throws Exception{
    	ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
		ass.setCustomSQL("select t1.section_id, t1.section_id || '-' || t1.section_name, t2.sub_section_id, t2.section_id || '-' || t2.sub_section_id || '-' || t2.section_name " +
				"from arc_section t1, arc_section t2 " +
				"where t1.section_id = t2.section_id " +
				"and t1.sub_section_id = 0 " +
//				"      t2.sub_section_id > 0  " +
				"order by t1.section_id, t2.sub_section_id");
		return ACFtUtility.toJson(ass.getRows(ACFtDBUtility.getConnection("ARCDB")));
    	
    }
    
    public String getSectionNameByIDAndSubID(String section_id, String sub_section_id) throws Exception// for APF ajax retrieve AC 2017/02/01
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
                "WHERE s.section_id = '%1$s' "+
                "and s.sub_section_id = '%2$s' "
                ,section_id, sub_section_id);
        
        List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        
        return result.size()>0 ? result.get(0).getString("section_name") : "";
        
    }

	public List<ACFgRawModel> getAllSectionValue() throws Exception {  // for section combo box,CN,2017/05/15
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
		ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
		ass.setCustomSQL("select section_id as id, section_id  || ' - ' || section_name as text from arc_section where sub_section_id = '0' order by section_id");
		return ass.executeQuery();

	}

	public List<ACFgRawModel> getAllSubSection(String sectionid) throws Exception {  // for sub section combo box,CN,2017/05/15
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
		ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
		ass.setCustomSQL("select sub_section_id as id, sub_section_id  || ' - ' || section_name as text from arc_section where section_id = '"+sectionid+"' order by sub_section_id");
		return ass.executeQuery();

	}

	public List<ACFgRawModel> getSectionValueExcl349() throws Exception {  // for apff106 section combo box,CN,2017/07/28
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
		ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
		ass.setCustomSQL("select section_id as id, section_id  || ' - ' || section_name as text from arc_section " +
		                 "where sub_section_id = '0' and section_id != '03' and section_id != '04' and section_id != '09'" +
				         "order by section_id");
		return ass.executeQuery();

	}

    public String getSectionName(String sectionid) throws Exception {  // for apff106 display section name in grid result,CN,2017/06/30
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
       
        ass.setCustomSQL(
                "SELECT p.section_name from arc_section p "+
                "WHERE p.section_id = '"+sectionid+"' and p.sub_section_id = '0'");
       List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
       return result.size()>0 ? result.get(0).getString("section_name") : "";
        
    }
	
	   /* sf 
     * get section name by section_id + sub_section_id
     * key = section_id, sub_section_id
     * action = select
     * fields = section name  
     */
    
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
                "WHERE s.section_id = '%s' "
                + "and s.sub_section_id = '0'"
                        
                ,section_id);
        
        List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        
        return result.size()>0 ? result.get(0).getString("section_name") : "";
        
    }
    
    public List<ACFgRawModel> getAllSectionValuePairs() throws Exception {
        /*
        List<ACFmModule> modules = moduleDao.selectCachedItems();
        Collections.sort(modules);

        List<ACFgRawModel> results = new LinkedList<ACFgRawModel>();
        for(ACFmModule item: modules) {
            ACFgRawModel m = new ACFgRawModel();
            m.set("id", item.mod_id).set("text", item.mod_id + "-" + item.mod_name);
            results.add(m);
        }
        return results;
        */

        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        //ass.setCustomSQL("select mod_id as id, mod_id || ' - ' || mod_name as text from acf_module order by mod_seq");
        ass.setCustomSQL("select section_id as id, section_id || ' - ' || dds_code as text from arc_section order by section_id");
        return ass.executeQuery();

    }

    public List<ACFgRawModel> getAllSectionName34() throws Exception {
        
        /* sf 
         * get section name for woodshop and paintshop 
         * key = section id. 
         * action = select
         * fields = section id. '03' & '04'  only  
         */
        
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        //ass.setCustomSQL("select mod_id as id, mod_id || ' - ' || mod_name as text from acf_module order by mod_seq");
        //  ass.setCustomSQL("select section_id as id, section_id || ' - ' || section_name as text from arc_section order by section_id");
        
         ass.setCustomSQL("select section_id as id, section_id || ' - ' || section_name as text from arc_section " +  
                          "WHERE (sub_section_id = '0' and (section_id = '03' OR section_id = '04')) " +
                           "order by section_id");
        return ass.executeQuery();

    }

    public List<ACFgRawModel> getAllSubSectionName8() throws Exception {
         /* sf 
         * get sub section id & name for costume 
         * key = section id. 
         * action = select
         * fields = section id. '08' only  
         */
        

        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        ass.setCustomSQL("select sub_section_id as id, sub_section_id || ' - ' || section_name as text from arc_section " +  
                "WHERE (section_id = '08' ) and (sub_section_id > '0') " +
                 "order by sub_section_id");
        return ass.executeQuery();

    }

    public List<ACFgRawModel> getAllSubSectionName7() throws Exception {
        /* sf 
        * get sub section id & name for costume 
        * key = section id. 
        * action = select
        * fields = section id. '08' only  
        */
       

       ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
       ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
       ass.setCustomSQL("select sub_section_id as id, sub_section_id || ' - ' || section_name as text from arc_section " +  
               "WHERE (section_id = '07' ) and (sub_section_id > '0') " +
                "order by sub_section_id");
       return ass.executeQuery();

   }

}