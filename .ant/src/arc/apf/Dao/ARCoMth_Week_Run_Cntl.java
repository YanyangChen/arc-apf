package arc.apf.Dao;


import java.sql.Connection;

import org.springframework.stereotype.Repository;

import acf.acf.Abstract.ACFaAppDao;
import acf.acf.Static.ACFtDBUtility;
import arc.apf.Model.ARCmMthWeekRunCntl;


@Repository
public class ARCoMth_Week_Run_Cntl extends ACFaAppDao<ARCmMthWeekRunCntl> { //it should extends the object under Module file source

    public ARCoMth_Week_Run_Cntl() throws Exception {
        super();
    }
    
    @Override
    protected Connection getConnection() throws Exception {
        Connection conn = ACFtDBUtility.getConnection("ARCDB");
        return conn;
    }
}