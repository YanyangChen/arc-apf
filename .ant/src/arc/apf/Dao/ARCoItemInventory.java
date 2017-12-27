package arc.apf.Dao;


import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import acf.acf.Abstract.ACFaAppDao;
import acf.acf.Static.ACFtDBUtility;
import acf.acf.Static.ACFtUtility;
import arc.apf.Model.ARCmItemInventory;

@Repository
public class ARCoItemInventory extends ACFaAppDao<ARCmItemInventory> { //it should extends the object under Module file source

	@Autowired ARCoPOHeader POHeaderDao;
    public ARCoItemInventory() throws Exception {
        super();
    }
    
    @Override
    protected Connection getConnection() throws Exception {
        Connection conn = ACFtDBUtility.getConnection("ARCDB");
        return conn;
    }
    
    public void beforeUpdateTrigger(ARCmItemInventory oldItem, ARCmItemInventory newItem) throws Exception {
//    	if (newItem.received_quantity.intValue() != 0)
//    	{newItem.receive_date = ACFtUtility.now();}
	}
    
    public void afterUpdateTrigger(ARCmItemInventory oldItem, ARCmItemInventory newItem) throws Exception {
    	
    	newItem.receive_date = POHeaderDao.selectItem(newItem.purchase_order_no).latest_receive_date;
	}
}