package arc.apf.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;

import acf.acf.Database.ACFdSQLAssSelect;
import acf.acf.General.core.ACFgRawModel;
import acf.acf.Static.ACFtDBUtility;

@Service
public class ARCsLabourConsumptionDetailsImpl extends ARCsLabourConsumptionDetails {

    public ARCsLabourConsumptionDetailsImpl() throws Exception {
        super();
        // TODO Auto-generated constructor stub
    }
   
    public BigDecimal GenerateSequenceNo(Timestamp processdate, String sectionid, String programmeno, BigDecimal from_episodeno, BigDecimal to_episodeno) throws Exception  // for labour consumption autogen sequence number in grid record,CN,2017/05/29
    {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
      
        ass.setCustomSQL(
                "SELECT max(sequence_no) as max_seq_no from arc_labour_consumption_details m "+
                "WHERE m.process_date = '"+processdate+"' " +
                "  AND m.section_id = '"+sectionid+"' " +
                "  AND m.programme_no = '"+programmeno+"' " +
                "  AND m.from_episode_no = '"+from_episodeno+"' " +
                "  AND m.to_episode_no = '"+to_episodeno+"' ");
        
        List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        BigDecimal max_seq_no = result.get(0).getBigDecimal("max_seq_no");
        if (max_seq_no == null)
            return BigDecimal.ONE;
        else
            return max_seq_no.add(BigDecimal.ONE);
     
    }



}