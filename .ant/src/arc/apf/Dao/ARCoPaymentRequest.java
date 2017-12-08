package arc.apf.Dao;


import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import acf.acf.Static.ACFtDBUtility;
import acf.acf.Static.ACFtUtility;
import acf.acf.Abstract.ACFaAppDao;
import arc.apf.Model.ARCmPaymentRequest;
import arc.apf.Service.ARCsPaymentRequest;

@Repository
public class ARCoPaymentRequest extends ACFaAppDao<ARCmPaymentRequest> { //it should extends the object under Module file source

    @Autowired ARCsPaymentRequest PaymentRequestService;
    
    public ARCoPaymentRequest() throws Exception {
        super();
    }
    
    @Override
    protected Connection getConnection() throws Exception {
        Connection conn = ACFtDBUtility.getConnection("ARCDB");
        return conn;
    }
    public void beforeInsertTrigger(ARCmPaymentRequest newItem) throws Exception {
        	
    	System.out.print("******************************");
    	Timestamp now = ACFtUtility.now();;
    	SimpleDateFormat formatYear = new SimpleDateFormat("yy");
    	String a = formatYear.format(now);
    	System.out.print(a);
    	SimpleDateFormat formatMonth = new SimpleDateFormat("MM");
    	String b = formatMonth.format(now);
    	System.out.print(b);
        //updateItem(newItem.payment_request_no);
       if (newItem.form_id.equals("AT"))
         {      
            newItem.payment_form_no = PaymentRequestService.generate_no_at(a,b);
         }
       if (newItem.form_id.equals("PAT"))
         {  
           newItem.payment_form_no = PaymentRequestService.generate_no_pat(a,b);
         }
    }
    
    
}