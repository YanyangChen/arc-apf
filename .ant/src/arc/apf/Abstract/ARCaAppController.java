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

import org.springframework.stereotype.Controller;

import acf.acf.Abstract.ACFaBaseController;

@Controller
public abstract class ARCaAppController extends ACFaBaseController {

	public ARCaAppController() {
		super();
		system = "ARC";
	}
}
