package arc.apf.Service;

import java.util.List;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import acf.acf.Database.ACFdSQLAssSelect;
import acf.acf.General.core.ACFgRawModel;
import acf.acf.Static.ACFtDBUtility;


@Service
public class ARCsPhotoOtherMaterialConsumptionImpl extends ARCsPhotoOtherMaterialConsumption {

    public ARCsPhotoOtherMaterialConsumptionImpl() throws Exception {
        super();
        // TODO Auto-generated constructor stub
    }
   
    public BigDecimal GenerateSequenceNo(String job_no) throws Exception  // for aphf002 autogen sequence number in grid record,CN,2017/0515
    {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
      
        ass.setCustomSQL(
                "SELECT max(sequence_no) as max_seq_no from arc_photo_other_material_consumption m "+
                "WHERE m.job_no = '%s'"
                ,job_no);
        
        List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        BigDecimal max_seq_no = result.get(0).getBigDecimal("max_seq_no");
        if (max_seq_no == null)
            return BigDecimal.ONE;
        else
            return max_seq_no.add(BigDecimal.ONE);
     
    }
}