package arc.apf.Dao;


import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import acf.acf.Abstract.ACFaAppDao;
import acf.acf.Static.ACFtDBUtility;
import arc.apf.Model.ARCmPOHeader;
import arc.apf.Model.ARCmWPConsumptionHeader;
import arc.apf.Service.ARCsPoHeader;

@Repository
public class ARCoWPConsumptionHeader extends ACFaAppDao<ARCmWPConsumptionHeader> { //it should extends the object under Module file source

    public ARCoWPConsumptionHeader() throws Exception {
        super();
    }
    @Autowired ARCsPoHeader PoHeaderService;
    @Override
    protected Connection getConnection() throws Exception {
        Connection conn = ACFtDBUtility.getConnection("ARCDB");
        return conn;
    }
    
    public void beforeInsertTrigger(ARCmWPConsumptionHeader newItem) throws Exception {
    	if (newItem.section_id.equals("03")){
		newItem.consumption_form_no = PoHeaderService.generate_name_4_WC();
    	}
    	if (newItem.section_id.equals("04")){
    		newItem.consumption_form_no = PoHeaderService.generate_name_4_PC();
        }
		System.out.println("testing---------------------------------------------------------------" + newItem.section_id);

	}
}