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
import arc.apf.Model.ARCmItemReceiveHistory;
import arc.apf.Model.ARCmLabourConsumption;
import arc.apf.Model.ARCmLabourConsumptionDetails;
import arc.apf.Service.ARCsLabourConsumptionDetails;
import arc.apf.Service.ARCsPhotoOtherMaterialConsumption;

@Repository
public class ARCoLabourConsumptionDetails extends ACFaAppDao<ARCmLabourConsumptionDetails> { //it should extends the object under Module file source
	
    @Autowired ARCsLabourConsumptionDetails labourConsumptionDetailsService;

    public ARCoLabourConsumptionDetails() throws Exception {
        super();
    }
    
    @Override
    protected Connection getConnection() throws Exception {
        Connection conn = ACFtDBUtility.getConnection("ARCDB");
        return conn;
    }
    
    @Override
    public ARCmLabourConsumptionDetails saveItems(List<ARCmLabourConsumptionDetails> amendments) throws Exception {  // for labour consumption generate sequence no,CN,2017/05/29

        return super.saveItems(amendments, new ACFiSQLAssWriteInterface<ARCmLabourConsumptionDetails>() {

            @Override
            public boolean insert(ARCmLabourConsumptionDetails newItem, ACFdSQLAssInsert ass)
                    throws Exception {
                ass.columns.put("sequence_no", labourConsumptionDetailsService.GenerateSequenceNo(newItem.process_date, newItem.section_id, newItem.programme_no, newItem.from_episode_no, newItem.to_episode_no));
                return false;
            }

            @Override
            public boolean update(ARCmLabourConsumptionDetails oldItem, ARCmLabourConsumptionDetails newItem,
                    ACFdSQLAssUpdate ass) throws Exception {

                return false;
            }

            @Override
            public boolean delete(ARCmLabourConsumptionDetails oldItem, ACFdSQLAssDelete ass)
                    throws Exception {

                return false;
            }

        });
    }    
    
}