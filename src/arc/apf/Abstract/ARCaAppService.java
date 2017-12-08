/*
 * ********************************************************************************
 * Project      : CAL
 * Program      : AMMaAppService.java
 * Description  : Application OO Base Service layer for CAL project 
 * Date         : 150324
 * Version      : 1.0.0
 * 
 * Dependency   : Spring 4.0.0
 *              
 * I/O          : None
 * 
 * Date   Version Author                 Change Description
 * ------ ------- ---------------------- ------------------------------------------ 
 * 150324 1.0.0   Kenneth Cheng (KC)     + Initial version
 * 
 * 
 * ********************************************************************************
 */

package arc.apf.Abstract;

import java.sql.Connection;

import org.springframework.stereotype.Service;

import acf.acf.Abstract.ACFaBaseService;
import acf.acf.Static.ACFtDBUtility;

@Service
public abstract class ARCaAppService extends ACFaBaseService {

	public ARCaAppService() throws Exception {
		super();
	}
	
	@Override
	protected Connection getConnection() throws Exception {
		return ACFtDBUtility.getConnection("ARCDB");
	}
}
