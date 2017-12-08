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
import arc.apf.Model.ARCmPaymentDetails;
import arc.apf.Model.ARCmWPOtherMaterialConsumption;
import arc.apf.Service.ARCsModel;
import arc.apf.Service.ARCsPaymentDetails;

@Repository
public class ARCoPaymentDetails extends ACFaAppDao<ARCmPaymentDetails> { //it should extends the object under Module file source

	@Autowired ARCsPaymentDetails PaymentDetailsService;
    public ARCoPaymentDetails() throws Exception {
        super();
    }
    
    @Override
    protected Connection getConnection() throws Exception {
        Connection conn = ACFtDBUtility.getConnection("ARCDB");
        return conn;
    }
    
    public ARCmPaymentDetails saveItems(List<ARCmPaymentDetails> amendments, final String abc) throws Exception {

        return super.saveItems(amendments, new ACFiSQLAssWriteInterface<ARCmPaymentDetails>() {

            @Override
            public boolean insert(ARCmPaymentDetails newItem, ACFdSQLAssInsert ass)
                    throws Exception {
                ass.columns.put("payment_form_no", abc);
                
                System.out.println("**DTL**" + newItem.payment_form_no);
                
            	// ass.columns.put("sequence_no", PaymentDetailsService.GenerateSequenceNoPd(newItem.payment_form_no));
               	ass.columns.put("sequence_no", PaymentDetailsService.GenerateSequenceNoPd(abc));
                
                return false;
            }

            @Override
            public boolean update(ARCmPaymentDetails oldItem, ARCmPaymentDetails newItem,
                    ACFdSQLAssUpdate ass) throws Exception {
                // TODO Auto-generated method stub
           
                return false;
            }

            @Override
            public boolean delete(ARCmPaymentDetails oldItem, ACFdSQLAssDelete ass)
                    throws Exception {
                // TODO Auto-generated method stub
                return false;
            }

        });
    }
}