package arc.apf.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import acf.acf.Database.ACFdSQLAssSelect;
import acf.acf.General.core.ACFgRawModel;
import acf.acf.Static.ACFtDBUtility;

import arc.apf.Dao.ARCoMonthlyManhour;

@Service
public class ARCsMonthlyManhourImpl extends ARCsMonthlyManhour {
	
    public ARCsMonthlyManhourImpl() throws Exception {
        super();
    }
   
	public List<ACFgRawModel> getAllMonthlyManhour() throws Exception {
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
		ass.setConnection(ACFtDBUtility.getConnection("ARCDB"));
		return ass.executeQuery();

	}



}