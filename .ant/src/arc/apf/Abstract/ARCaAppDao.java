/*
 * ********************************************************************************
 * Project      : CAL
 * Program      : AMMaAppModel.java
 * Description  : Application OO Base layer for CAL project 
 * Date         : 150311
 * Version      : 1.0.0
 * 
 * Dependency   : Spring 4.0.0
 *              
 * I/O          : None
 * 
 * Date   Version Author                 Change Description
 * ------ ------- ---------------------- ------------------------------------------ 
 * 150311 1.0.0   Kenneth Cheng (KC)     + Initial version
 * 
 * 
 * ********************************************************************************
 */

package arc.apf.Abstract;

import java.sql.Connection;

import org.springframework.stereotype.Repository;

import acf.acf.Abstract.ACFaBaseDao;
import acf.acf.Abstract.ACFaBaseModel;
import acf.acf.Static.ACFtDBUtility;

@Repository
public abstract class ARCaAppDao<T extends ACFaBaseModel> extends ACFaBaseDao<T> {

	public ARCaAppDao() throws Exception {
		super();
	}
	
	@Override
	protected Connection getConnection() throws Exception {
		return ACFtDBUtility.getConnection("ARCDB");
	}
}
