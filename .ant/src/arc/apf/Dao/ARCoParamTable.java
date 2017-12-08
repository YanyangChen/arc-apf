package arc.apf.Dao;


import java.sql.Connection;

import org.springframework.stereotype.Repository;

import acf.acf.Abstract.ACFaAppDao;
import acf.acf.Static.ACFtDBUtility;
import arc.apf.Model.ARCmAcAllocTx;
import arc.apf.Model.ARCmParamTable;

@Repository
public class ARCoParamTable extends ACFaAppDao<ARCmParamTable> { //it should extends the object under Module file source

    public ARCoParamTable() throws Exception {
        super();
    }
    
    @Override
    protected Connection getConnection() throws Exception {
        Connection conn = ACFtDBUtility.getConnection("ARCDB");
        return conn;
    }
}