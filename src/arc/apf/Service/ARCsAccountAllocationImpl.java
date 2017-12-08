package arc.apf.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import acf.acf.Database.ACFdSQLAssSelect;
import acf.acf.General.core.ACFgRawModel;
import acf.acf.Static.ACFtDBUtility;


@Service
public class ARCsAccountAllocationImpl extends ARCsAccountAllocation {

    public ARCsAccountAllocationImpl() throws Exception {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public String getBudgetAccountDescription(String budget_account_allocation) throws Exception
    {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
       /* ass.setCustomSQL("SELECT listagg(staff_name,chr(9)) as staff_name from ( " +
                         "  SELECT v.*, case when chi_name <> '' then chi_name else eng_name end as staff_name FROM APM_LOCAL_TEMP_PROG_PROD_MEMBER_VIEW v " +
                         ") " +
                         "WHERE prog_no = '%s' " +
                         "AND member_type = '%s' "
                         ,prog_no, staff_type);*/
        ass.setCustomSQL(
                "SELECT s.budget_account_description from arc_account_allocation s "+
                "WHERE s.budget_account_allocation = '%s'"
                ,budget_account_allocation);
        
        List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        
        return result.size()>0 ? result.get(0).getString("budget_account_description") : "";
        
    }

    public List<ACFgRawModel> getBudgetAccountAllocation() throws Exception {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        //ass.setCustomSQL("select mod_id as id, mod_id || ' - ' || mod_name as text from acf_module order by mod_seq");
        ass.setCustomSQL("select budget_account_allocation as id, budget_account_allocation || ' - ' || budget_account_description as text from arc_account_allocation");
        return ass.executeQuery();
        
    }
    
    public List<ACFgRawModel> getActualAccountAllocation() throws Exception {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        //ass.setCustomSQL("select mod_id as id, mod_id || ' - ' || mod_name as text from acf_module order by mod_seq");
        ass.setCustomSQL("select actual_account_allocation as id, actual_account_allocation as text from arc_account_allocation");
        return ass.executeQuery();
        
    }

    public List<ACFgRawModel> getActualAcAllocationCombo() throws Exception {  // for a/c allocation combo box,CN,2017/05/15
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        ass.setCustomSQL("select actual_account_allocation as id, actual_account_allocation || '-' || actual_account_description as text from arc_account_allocation");
        return ass.executeQuery();
        
    }
    
    /* sf 
     * get all a/c alloc code
     * key = actual_account_allocation
     * action = select
     * fields = actual_account_allocation 
     */
        
    public List<ACFgRawModel> getAcAllocPairs() throws Exception {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        //ass.setCustomSQL("select mod_id as id, mod_id || ' - ' || mod_name as text from acf_module order by mod_seq");
        ass.setCustomSQL("select actual_account_allocation as id, actual_account_allocation || ' - ' || actual_account_description as text from arc_account_allocation");
        return ass.executeQuery();
        
    }
    
}