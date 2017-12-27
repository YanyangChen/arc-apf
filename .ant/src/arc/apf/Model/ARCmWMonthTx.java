package arc.apf.Model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;

import acf.acf.Abstract.ACFaAppModel;
import acf.acf.General.annotation.ACFgTable;

@ACFgTable(name = "arc_w_month_tx")
public class ARCmWMonthTx extends ACFaAppModel {

    public ARCmWMonthTx() throws Exception {
        super();
    }

    @Column(name = "ac_mth")
    public String ac_mth;
    
    @Column(name = "doc_no")
    public String doc_no;
    
    @Column(name = "adj_ind")
    public String adj_ind;
    
    @Column(name = "section_id")
    public String section_id;
    
    @Column(name = "sub_section_id")
    public String sub_section_id;
    
    @Column(name = "dds_code")
    public String dds_code;
        
    @Column(name = "prog_no")
    public String prog_no;
    
    @Column(name = "busi_platform")
    public String busi_platform;
    
    @Column(name = "dept_nature")
    public String dept_nature;
    
    @Column(name = "from_epi_no")
    public BigDecimal from_epi_no;
    
    @Column(name = "to_epi_no")
    public BigDecimal to_epi_no;
    
    @Column(name = "job_desc")
    public String job_desc;
    
    @Column(name = "colour_copy_amt")
    public BigDecimal colour_copy_amt;
    
    @Column(name = "new_purchase_amt")
    public BigDecimal new_purchase_amt;
    
    @Column(name = "ex_stock_amt")
    public BigDecimal ex_stock_amt;
    
    @Column(name = "ac_alloc_code")
    public String ac_alloc_code;
    
    @Column(name = "labour_type")
    public String labour_type;
    
    @Column(name = "labour_amt")
    public BigDecimal labour_amt;
    
    @Column(name = "labour_hr")
    public BigDecimal labour_hr;
    
    
}