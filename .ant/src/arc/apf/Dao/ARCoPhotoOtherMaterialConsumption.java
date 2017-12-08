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

import arc.apf.Model.ARCmPhotoOtherMaterialConsumption;
import arc.apf.Service.ARCsPhotoOtherMaterialConsumption;

@Repository
public class ARCoPhotoOtherMaterialConsumption extends ACFaAppDao<ARCmPhotoOtherMaterialConsumption> { 

	@Autowired ARCsPhotoOtherMaterialConsumption photoOtherMaterialConsumptionService;
	
    public ARCoPhotoOtherMaterialConsumption() throws Exception {
        super();
    }
    
    @Override
    protected Connection getConnection() throws Exception {
        Connection conn = ACFtDBUtility.getConnection("ARCDB");
        return conn;
    }
    
    @Override
    public ARCmPhotoOtherMaterialConsumption saveItems(List<ARCmPhotoOtherMaterialConsumption> amendments) throws Exception { // for aphf002,CN,2017/05/15

        return super.saveItems(amendments, new ACFiSQLAssWriteInterface<ARCmPhotoOtherMaterialConsumption>() {

            @Override
            public boolean insert(ARCmPhotoOtherMaterialConsumption newItem, ACFdSQLAssInsert ass)
                    throws Exception {
                ass.columns.put("sequence_no", photoOtherMaterialConsumptionService.GenerateSequenceNo(newItem.job_no));
                return false;
            }

            @Override
            public boolean update(ARCmPhotoOtherMaterialConsumption oldItem, ARCmPhotoOtherMaterialConsumption newItem,
                    ACFdSQLAssUpdate ass) throws Exception {

                return false;
            }

            @Override
            public boolean delete(ARCmPhotoOtherMaterialConsumption oldItem, ACFdSQLAssDelete ass)
                    throws Exception {

                return false;
            }

        });
    }
    
}