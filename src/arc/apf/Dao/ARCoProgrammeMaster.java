package arc.apf.Dao;


import java.sql.Connection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import acf.acf.Abstract.ACFaAppDao;
import acf.acf.Database.ACFdSQLAssDelete;
import acf.acf.Database.ACFdSQLAssInsert;
import acf.acf.Database.ACFdSQLAssUpdate;
import acf.acf.Interface.ACFiSQLAssWriteInterface;
import acf.acf.Static.ACFtDBUtility;
import acf.acf.Static.ACFtUtility;
import arc.apf.Model.ARCmLabourConsumptionDetails;
import arc.apf.Model.ARCmProgrammeMaster;
import arc.apf.Service.ARCsDirectBudget;
import arc.apf.Service.ARCsProgrammeMaster;

@Repository
public class ARCoProgrammeMaster extends ACFaAppDao<ARCmProgrammeMaster> { //it should extends the object under Module file source
	
	@Autowired ARCsProgrammeMaster ProgrammeMasterService;
    public ARCoProgrammeMaster() throws Exception {
        super();
    }
    
    @Override
    protected Connection getConnection() throws Exception {
        Connection conn = ACFtDBUtility.getConnection("ARCDB");
        return conn;
    } 
    
    @Override
    public ARCmProgrammeMaster saveItems(List<ARCmProgrammeMaster> amendments) throws Exception {  // for labour consumption generate sequence no,CN,2017/05/29

        return super.saveItems(amendments, new ACFiSQLAssWriteInterface<ARCmProgrammeMaster>() {

            @Override
            public boolean insert(ARCmProgrammeMaster newItem, ACFdSQLAssInsert ass)
                    throws Exception {
            	ass.columns.put("cost_first_input_date", ProgrammeMasterService.UpdateFirstInput(newItem.programme_no));
                return false;
            }

            @Override
            public boolean update(ARCmProgrammeMaster oldItem, ARCmProgrammeMaster newItem,
                    ACFdSQLAssUpdate ass) throws Exception {
            	ass.columns.put("cost_first_input_date", ProgrammeMasterService.UpdateFirstInput(newItem.programme_no));
            	//ass.columns.put("cost_first_input_date", ACFtUtility.now());
            	//UpdateFirstInput(String programme_no)
            	//newItem.cost_first_input_date = ACFtUtility.now();
                return false;
            }

            @Override
            public boolean delete(ARCmProgrammeMaster oldItem, ACFdSQLAssDelete ass)
                    throws Exception {

                return false;
            }

        });
    }    
}