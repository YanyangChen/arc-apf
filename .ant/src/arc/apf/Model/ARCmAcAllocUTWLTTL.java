package arc.apf.Model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;

import acf.acf.Abstract.ACFaAppModel;
import acf.acf.General.annotation.ACFgTable;

@ACFgTable(name = "arc_ac_alloc_utlw_ttl")
public class ARCmAcAllocUTWLTTL extends ACFaAppModel {

    public ARCmAcAllocUTWLTTL() throws Exception {
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
    
    @Column(name = "current_week_closing_date")
    public Timestamp current_week_closing_date;
    
    @Column(name = "current_closing_amount")
    public BigDecimal current_closing_amount;
    
    @Id
    @Column(name = "last_closing_date")
    public Timestamp last_closing_date;
    
    @Column(name = "last_closing_amount")
    public BigDecimal last_closing_amount;

}