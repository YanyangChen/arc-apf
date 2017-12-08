package arc.apf.Model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;

import acf.acf.Abstract.ACFaAppModel;
import acf.acf.General.annotation.ACFgTable;

@ACFgTable(name = "arc_labour_type_utlw_ttl")
public class ARCmLabourTypeUTLWTTL extends ACFaAppModel {

    public ARCmLabourTypeUTLWTTL() throws Exception {
        super();
    }


    @Id
    @Column(name = "labour_type")
    public String labour_type;
    
    @Id
    @Column(name = "programme_no")
    public String programme_no;
    
    @Id
    @Column(name = "section_id")
    public String section_id;
    
    @Column(name = "cr_wk_cls_dt")
    public Timestamp cr_wk_cls_dt;
    
    @Column(name = "cr_cls_amt")
    public BigDecimal cr_cls_amt;
    
    @Column(name = "cr_cls_hr")
    public BigDecimal cr_cls_hr;
    
    @Column(name = "lst_wk_cls_dt")
    public Timestamp lst_wk_cls_dt;
    
    @Column(name = "lst_cls_amt")
    public BigDecimal lst_cls_amt;
    
    @Column(name = "lst_cls_hr")
    public BigDecimal lst_cls_hr;
    
    
}