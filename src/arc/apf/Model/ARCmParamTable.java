package arc.apf.Model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;

import acf.acf.Abstract.ACFaAppModel;
import acf.acf.General.annotation.ACFgTable;

@ACFgTable(name = "arc_param_table")
public class ARCmParamTable extends ACFaAppModel {

    public ARCmParamTable() throws Exception {
        super();
    }


    @Id
    @Column(name = "parameter_id")
    public String parameter_id;
    
    @Column(name = "parameter")
    public String parameter;
          
    
}