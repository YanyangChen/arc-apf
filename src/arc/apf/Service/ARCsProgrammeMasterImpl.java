package arc.apf.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import acf.acf.Database.ACFdSQLAssSelect;
import acf.acf.General.core.ACFgRawModel;
import acf.acf.Static.ACFtDBUtility;
import acf.acf.Static.ACFtUtility;
import arc.apf.Dao.ARCoProgrammeMaster;
import arc.apf.Model.ARCmProgrammeMaster;

@Service
public class ARCsProgrammeMasterImpl extends ARCsProgrammeMaster {

	@Autowired ARCoProgrammeMaster programmeMasterDao;
	
    public ARCsProgrammeMasterImpl() throws Exception {
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
   
    public List<ACFgRawModel> getProgNoPairs() throws Exception {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        //ass.setCustomSQL("select programme_no as id, programme_no || ' - ' || programme_name as text from arc_programme_master order by programme_no asc");
        ass.setCustomSQL("select programme_no as id, programme_no as text from arc_programme_master order by programme_no asc");
        return ass.executeQuery();
        
    }
    
    public List<ACFgRawModel> getProgNamePairs() throws Exception {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        ass.setCustomSQL("select programme_no as id, programme_no || ' - ' || programme_name as text from arc_programme_master order by programme_no asc");
       // ass.setCustomSQL("select programme_no as id, programme_no as text from arc_programme_master order by programme_no asc");
        return ass.executeQuery();
        
    }
    
    public List<ACFgRawModel> getToProgrammeFields(String programme_no) throws Exception  {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
       
        ass.setCustomSQL(
                "SELECT transfer_status as transfer_status_n, programme_name as prog_name_n, "
                + "modified_at as modified_at_n, modified_by as modified_by_n, created_by as created_by_n, created_at as created_at_n, "
                + "vtr_from_date as vtr_from_date_n, vtr_to_date as vtr_to_date_n, efp_from_date as efp_from_date_n, efp_to_date as efp_to_date_n, "
                + "transfer_from_date as transfer_from_date_n, transfer_to_date as transfer_to_date_n, "
                + "transfer_from_programme as transfer_from_programme_n, transfer_to_programme as transfer_to_programme_n, "
                + "chinese_programme_name as chin_prog_name_n from arc_programme_master i "+
                "WHERE i.programme_no= '%s'"
                ,programme_no);
        
        
       /*
        *  ass.setCustomSQL(
                "SELECT * from apw_item i "+
                "WHERE i.item_no= '%1$s'" +                
                "and i.item_no= '%2$s'"
                ,id,id);
        * 
        * */ 
        List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        return result;
      //  return result.size()>0 ? result.get(0).getString("supplier_desc") : "";
        
    }
    
    public List<ACFgRawModel> getNewToProgrammeFields(String programme_no) throws Exception  {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
       
        ass.setCustomSQL(
                "SELECT * from arc_programme_master "+//1
                "WHERE programme_no= '%s'"
                ,programme_no);
        
        
       /*
        *  ass.setCustomSQL(
                "SELECT * from apw_item i "+
                "WHERE i.item_no= '%1$s'" +                
                "and i.item_no= '%2$s'"
                ,id,id);
        * 
        * */ 
        List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        return result;
      //  return result.size()>0 ? result.get(0).getString("supplier_desc") : "";
        
    }
    
    public String getBusinessPlatformDesc(String programme_no) throws Exception
    {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setCustomSQL("SELECT bp.description, bp.business_platform, bp.department, pg.department "
                + "from arc_business_platform bp, arc_programme_master pg "
                +"WHERE bp.business_platform = pg.business_platform and "
                + " bp.department = pg.department and  "
                + " pg.programme_no = '%s'"
                ,programme_no);
        
        List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        
        return result.size()>0 ? result.get(0).getString("description") : "";
        
    }
    
    public Timestamp UpdateFirstInput(String programme_no) throws Exception//generate sequence number for apff011 grid record 2017/03/15
    {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
       /* ass.setCustomSQL("SELECT listagg(staff_name,chr(9)) as staff_name from ( " +
                         "  SELECT v.*, case when chi_name <> '' then chi_name else eng_name end as staff_name FROM APM_LOCAL_TEMP_PROG_PROD_MEMBER_VIEW v " +
                         ") " +
                         "WHERE prog_no = '%s' " +
                         "AND member_type = '%s' "
                         ,prog_no, staff_type);*/
        ass.setCustomSQL(
                "SELECT cost_first_input_date from arc_programme_master pm "+
                "WHERE pm.programme_no = '%s'"
                ,programme_no);
        
        List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        Timestamp cost_first_input_date = result.get(0).getTimestamp("cost_first_input_date");
        if (cost_first_input_date.equals(Timestamp.valueOf("1900-01-01 00:00:00.000")))
        {   
        	System.out.println(" --------------------if condition reached -------------------");
        	return ACFtUtility.now();
        
        }
        else{
        	System.out.println(" -----------------------if condition not reached -----------------------");
        	return cost_first_input_date;
        }
    }
    
    public List<ACFgRawModel> getProgrammeNo() throws Exception {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        //ass.setCustomSQL("select mod_id as id, mod_id || ' - ' || mod_name as text from acf_module order by mod_seq");
        ass.setCustomSQL("select programme_no as id, programme_name as text from arc_programme_master order by programme_no asc");
        return ass.executeQuery();
        
    }
    
    public List<ACFgRawModel> getBusinessPlatform() throws Exception {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        //ass.setCustomSQL("select mod_id as id, mod_id || ' - ' || mod_name as text from acf_module order by mod_seq");
        ass.setCustomSQL("select business_platform as id, business_platform as text from arc_programme_master");
        return ass.executeQuery();
        
    }
    
    public List<ACFgRawModel> getDepartment() throws Exception {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        //ass.setCustomSQL("select mod_id as id, mod_id || ' - ' || mod_name as text from acf_module order by mod_seq");
        ass.setCustomSQL("select department as id, department as text from arc_programme_master order by department asc");
        return ass.executeQuery();
        
    }
    
    public List<ACFgRawModel> getBBusinessPlatform() throws Exception {//For apff011 get content in form, AC, 2017/03/20
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        //ass.setCustomSQL("select mod_id as id, mod_id || ' - ' || mod_name as text from acf_module order by mod_seq");
        ass.setCustomSQL("select distinct business_platform as id, business_platform || ' - ' || description as text from arc_business_platform where department != '00' order by department asc");
        return ass.executeQuery();
        
    }

    public List<ACFgRawModel> getBDepartment() throws Exception {//For apff011 get content in form, AC, 2017/03/20
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
        //ass.setCustomSQL("select mod_id as id, mod_id || ' - ' || mod_name as text from acf_module order by mod_seq");
        ass.setCustomSQL("select department as id, business_platform || ' - ' || department || ' - ' || Description as text from arc_business_platform where department != '00' order by department asc");
        return ass.executeQuery();
        
    }
    
    public String getProgrammeName(String programme_no) throws Exception
    {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
       
        ass.setCustomSQL(
                "SELECT p.programme_name from arc_programme_master p "+
                "WHERE p.programme_no= '%s'"
                ,programme_no);
       List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
       return result.size()>0 ? result.get(0).getString("programme_name") : "";
      //  return result.size()>0 ? result.get(0).getString("supplier_desc") : "";
        
    }
    
    public  String getProgrammePlatform(String programme_no) throws Exception
    {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
       
        ass.setCustomSQL(
                "SELECT p.business_platform from arc_programme_master p "+
                "WHERE p.programme_no= '%s'"
                ,programme_no);
       List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
       return result.size()>0 ? result.get(0).getString("business_platform") : "";
      //  return result.size()>0 ? result.get(0).getString("supplier_desc") : "";
        
    }
    
    public  String  getProgrammeDepartment(String programme_no) throws Exception
    {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
       
        ass.setCustomSQL(
                "SELECT p.department from arc_programme_master p "+
                "WHERE p.programme_no= '%s'"
                ,programme_no);
       List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
       return result.size()>0 ? result.get(0).getString("department") : "";
      //  return result.size()>0 ? result.get(0).getString("supplier_desc") : "";
        
    }

    public Timestamp getCost1stInputDate(String programme_no) throws Exception //  for labour consumption checking,CN,20170727
    {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setCustomSQL(
                "SELECT cost_first_input_date from arc_programme_master p "+
                "WHERE p.programme_no = '%s'"
                ,programme_no);
        
        List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        Timestamp cost_first_input_date = result.get(0).getTimestamp("cost_first_input_date");

        return cost_first_input_date;
    }

	public void updateCost1stInputDate(ARCmProgrammeMaster before, Timestamp cost_1st_input_date) throws Exception  //  for labour consumption updating,CN,20170727
	{
		ARCmProgrammeMaster update = before.clone(ARCmProgrammeMaster.class);
		update.cost_first_input_date = cost_1st_input_date;
		programmeMasterDao.updateItem(before, update);
	}

	public boolean isProgNoExisted(String programme_no) throws Exception {     // for apcf001 & apcf002/ SF,2017/08/14
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
		ass.setCustomSQL("select * from arc_programme_master p " +
   		  	 "where p.programme_no = '%s'"
             ,programme_no);
		List<ACFgRawModel> list = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
		return !list.isEmpty();
	}

	public void updateFromTransferStatus(ARCmProgrammeMaster before, String trf_status, String trf_programme, Timestamp trf_date, String trf_remarks) throws Exception  //  for programme transfer,CN,20171017
	{
		ARCmProgrammeMaster update = before.clone(ARCmProgrammeMaster.class);
		update.transfer_status = trf_status;
		update.transfer_to_programme = trf_programme;
		update.transfer_to_date = trf_date;
		update.transfer_remarks = trf_remarks;
		programmeMasterDao.updateItem(before, update);
	}

	public void updateToTransferStatus(ARCmProgrammeMaster before, String trf_status, String trf_programme, Timestamp trf_date, String trf_remarks) throws Exception  //  for programme transfer,CN,20171017
	{
		ARCmProgrammeMaster update = before.clone(ARCmProgrammeMaster.class);
		update.transfer_status = trf_status;
		update.transfer_from_programme = trf_programme;
		update.transfer_from_date = trf_date;
		update.transfer_remarks = trf_remarks;
		programmeMasterDao.updateItem(before, update);
	}
}