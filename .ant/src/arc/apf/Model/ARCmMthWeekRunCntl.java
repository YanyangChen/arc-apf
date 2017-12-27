package arc.apf.Model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;

import acf.acf.Abstract.ACFaAppModel;
import acf.acf.General.annotation.ACFgTable;

@ACFgTable(name = "arc_mth_week_run_cntl")
public class ARCmMthWeekRunCntl extends ACFaAppModel {

    public ARCmMthWeekRunCntl() throws Exception {
        super();
    }

    @Id
    @Column(name = "record_id")
    public String  record_id;
    
    @Id
    @Column(name = "run_no")
    public BigDecimal  run_no;
    
    @Column(name = "ac_mth")
    public BigDecimal  ac_mth;
    
    @Column(name = "request_by")
    public String request_by;
    
    @Column(name = "request_date")
    public Timestamp  request_date;
    
    @Column(name = "approve_by")
    public String approve_by;
    
    @Column(name = "approve_date")
    public Timestamp approve_date;
    
    @Column(name = "batch_run_date")
    public Timestamp batch_run_date;
    
    @Column(name = "mth_cutoff_start_date")
    public Timestamp mth_cutoff_start_date;
    
    @Column(name = "mth_cutoff_end_date")
    public Timestamp mth_cutoff_end_date;
    
    @Column(name = "wk_cutoff_start_date")
    public Timestamp wk_cutoff_start_date;
    
    @Column(name = "wk_cutoff_end_date")
    public Timestamp wk_cutoff_end_date;
    
}