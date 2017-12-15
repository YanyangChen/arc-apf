package arc.apf.Dao;


import java.sql.Connection;

import org.springframework.stereotype.Repository;

import acf.acf.Abstract.ACFaAppDao;
import acf.acf.Static.ACFtDBUtility;
import acf.acf.Static.ACFtUtility;
import arc.apf.Model.ARCmItemInventory;

@Repository
public class ARCoItemInventory extends ACFaAppDao<ARCmItemInventory> { //it should extends the object under Module file source

    public ARCoItemInventory() throws Exception {
        super();
    }
    
    @Override
    protected Connection getConnection() throws Exception {
        Connection conn = ACFtDBUtility.getConnection("ARCDB");
        return conn;
    }
    
    public void beforeUpdateTrigger(ARCmItemInventory oldItem, ARCmItemInventory newItem) throws Exception {
    	if (newItem.received_quantity.intValue() != 0)
    	{newItem.receive_date = ACFtUtility.now();}
	}
}