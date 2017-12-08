package arc.apf.Dao;


import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;





import acf.acf.Abstract.ACFaAppDao;
import acf.acf.Static.ACFtDBUtility;
import arc.apf.Model.ARCmPOHeader;
import arc.apf.Service.ARCsPoHeader;



@Repository
public class ARCoPOHeader extends ACFaAppDao<ARCmPOHeader> { //it should extends the object under Module file source

	@Autowired ARCsPoHeader PoHeaderService;
	
    public ARCoPOHeader() throws Exception {
        super();
    }
    
    @Override
    protected Connection getConnection() throws Exception {
        Connection conn = ACFtDBUtility.getConnection("ARCDB");
        return conn;
    }
    
    public void beforeInsertTrigger(ARCmPOHeader newItem) throws Exception {
    	if (newItem.section_id.equals("03")){
		newItem.purchase_order_no = PoHeaderService.generate_name();
    	}
    	if (newItem.section_id.equals("04")){
    		newItem.purchase_order_no = PoHeaderService.generate_name_4_PP();
        }
		System.out.println("testing---------------------------------------------------------------" + newItem.section_id);
		//updateItem(newItem.purchase_order_no);
		//ARCsPoHeader.updateEventNo();
		//If newItem.form_id == 'AP'
		//{}
		//else newItem.form_id == 'PAT'
		//{}
	}
}