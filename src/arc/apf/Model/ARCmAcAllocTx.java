package arc.apf.Model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;

import acf.acf.Abstract.ACFaAppModel;
import acf.acf.General.annotation.ACFgTable;

@ACFgTable(name = "arc_ac_alloc_tx")
public class ARCmAcAllocTx extends ACFaAppModel {

    public ARCmAcAllocTx() throws Exception {
        super();
    }


    @Id
    @Column(name = "programme_no")
    public String programme_no;
    
    @Id
    @Column(name = "section_id")
    public String section_id;
    
    @Id
    @Column(name = "account_allocation")
    public String account_allocation;
    
    @Id
    @Column(name = "amend_date")
    public Timestamp amend_date;
    
    @Column(name = "amend_amount")
    public BigDecimal amend_amount;
    
    
    @Column(name = "previous_date")
    public Timestamp previous_date;
    
    @Column(name = "previous_amount")
    public BigDecimal previous_amount;

}