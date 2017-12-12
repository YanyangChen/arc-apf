package arc.apf.Dao;


import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import acf.acf.Abstract.ACFaAppDao;
import acf.acf.Static.ACFtDBUtility;
import arc.apf.Model.ARCmWPConsumptionHeader;
import arc.apf.Model.ARCmWPConsumptionItem;
import arc.apf.Service.ARCsPoHeader;

@Repository
public class ARCoWPConsumptionItem extends ACFaAppDao<ARCmWPConsumptionItem> { //it should extends the object under Module file source

    public ARCoWPConsumptionItem() throws Exception {
        super();
    }
    @Autowired ARCsPoHeader PoHeaderService;
    @Override
    protected Connection getConnection() throws Exception {
        Connection conn = ACFtDBUtility.getConnection("ARCDB");
        return conn;
    }
    
    public void beforeInsertTrigger(ARCmWPConsumptionItem newItem) throws Exception {

    	if (newItem.consumption_form_no.equals("") || newItem.consumption_form_no == null)
    	{
    	//get woodshop's consumption No if its PO begins with W
    	if (newItem.purchase_order_no.charAt(0) == 'W'){
		newItem.consumption_form_no = PoHeaderService.get_name_4_WC();
    	}
    	
    	//get paintshop's consumption No if its PO begins with P
    	if (newItem.purchase_order_no.charAt(0) == 'P'){
    		newItem.consumption_form_no = PoHeaderService.get_name_4_PC();
        }}
		System.out.println("testing---------------------------------------------------------------" + newItem.consumption_form_no);

	}
}