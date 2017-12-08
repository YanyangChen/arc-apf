package arc.apf.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import acf.acf.Database.ACFdSQLAssSelect;
import acf.acf.General.core.ACFgRawModel;
import acf.acf.Static.ACFtDBUtility;


@Service
public class ARCsSupplierImpl extends ARCsSupplier {

    public ARCsSupplierImpl() throws Exception {
        super();
        // TODO Auto-generated constructor stub
    }
   
    public String getSectionNameById(String section_id) throws Exception//for apfc ajax retrieve， AC, 2017/04/10
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

    public List<ACFgRawModel> getSupplierCodefromitem() throws Exception {//for apwc combobox， AC, 2017/04/10 
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        //ass.setCustomSQL("select mod_id as id, mod_id || ' - ' || mod_name as text from acf_module order by mod_seq");
        ass.setCustomSQL("select distinct supplier_code as id, supplier_code || '-' || supplier_name as text from arc_supplier");
        return ass.executeQuery();
        
    }

    public List<ACFgRawModel> getAllSupplierValue() throws Exception {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        ass.setCustomSQL("select supplier_code as id, supplier_code || supplier_code || supplier_name as text from apf_supplier");
        return ass.executeQuery();

}
    
    /* sf 
     * get all supplier code, name
     * key = supplier code
     * action = select
     * fields = supplier code 
     */
        
    public List<ACFgRawModel> getSupplierPairs() throws Exception {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        ass.setCustomSQL("select s.supplier_code as id, s.supplier_code || ' - ' || s.supplier_name as text from arc_supplier s order by supplier_code");
  //      ass.setCustomSQL("select supplier_code as id, supplier_code "
  //              + " as text from arc_supplier");
            
        return ass.executeQuery();

}
    public String getSupplierNameCash(String supplier_code) throws Exception
    {
     ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
     /* sf 
      * get all supplier name (Cash only)   
      * key = supplier code  
      * action = select
      * fields = supplier name
      */
      
     ass.setCustomSQL(
             "SELECT s.supplier_name from arc_supplier s "+
             "WHERE s.supplier_code = 'CASH'  "
            ,supplier_code);
     
     List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
    
     return result.size()>0 ? result.get(0).getString("supplier_name") : "";
     
      }
    
    public String getSupplierNameById(String supplier_code) throws Exception
    {
     ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
     /* sf 
      * get all supplier name  
      * key = supplier code
      * action = select
      * fields = supplier name
      */
      
     ass.setCustomSQL(
             "SELECT s.supplier_name from arc_supplier s "+
            "WHERE s.supplier_code = '%s'"
            ,supplier_code);
     
     List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
    
     return result.size()>0 ? result.get(0).getString("supplier_name") : "";
     
      }
    
    public List<ACFgRawModel> getSupplierPairsExcCash() throws Exception {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        ass.setCustomSQL("select s.supplier_code as id, s.supplier_code || ' - ' || s.supplier_name as text from arc_supplier s "
                + " where s.supplier_code <> 'CASH' ");
  //      ass.setCustomSQL("select supplier_code as id, supplier_code "
  //              + " as text from arc_supplier");
            
        return ass.executeQuery();

}
    
    /* CN
     * get supplier name by supplier code
     * key = supplier code
     * action = select
     * fields = supplier code + supplier name
     */
        
    public String getSupplierDesc(String suppliercode) throws Exception {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();

        ass.setCustomSQL("select s.supplier_code || ' - ' || s.supplier_name as supplier_desc from arc_supplier s " +
                         " where s.supplier_code = '"+suppliercode+"' ");
            
		List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
		return result.size()>0 ? result.get(0).getString("supplier_desc") : "";


    }    
    
}