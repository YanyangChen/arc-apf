package arc.apf.Model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;

import acf.acf.Abstract.ACFaAppModel;
import acf.acf.General.annotation.ACFgTable;

@ACFgTable(name = "arc_job_cntl")
public class ARCmJobCntl extends ACFaAppModel {

    public ARCmJobCntl() throws Exception {
        super();
    }

    @Id
    @Column(name = "job_id")
    public String job_id;
    
    @Column(name = "job_frequency")
    public String job_frequency;
    
    @Column(name = "job_desc")
    public String job_desc;
    
    @Column(name = "ac_mth")
    public String ac_mth;
    
    @Column(name = "start_date")
    public String start_date;
    
    @Column(name = "end_date")
    public String end_date;
        
    @Column(name = "week_no")
    public String week_no;
    
    
}