package arc.apf.Service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import acf.acf.Database.ACFdSQLAssSelect;
import acf.acf.General.core.ACFgRawModel;
import acf.acf.Static.ACFtDBUtility;


@Service
public class ARCsPoDetailsImpl extends ARCsPoDetails {

    public ARCsPoDetailsImpl() throws Exception {
        super();
        // TODO Auto-generated constructor stub
    }
   
    public String getSectionNameById(String section_id) throws Exception
    {
        ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
       /* ass.setCustomSQL("SELECT listagg(staff_name,chr(9)) as staff_name from ( " +
                         "  SELECT v.*, case when chi_name <> '' then chi_name else eng_name end as staff_name FROM APM_LOCAL_TEMP_PROG_PROD_MEMBER_VIEW v " +
                         ") " +
                         "WHERE prog_no = '%s' " +
                         "AND member_type = '%s' "
                         ,prog_no, staff_type);*/
        ass.setCustomSQL(
                "SELECT s.section_name from arc_section s "+
                "WHERE s.section_id = '%s'"
                ,section_id);
        
        List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
        
        return result.size()>0 ? result.get(0).getString("section_name") : "";
    }

    public String getReferencePrice(String item_no) throws Exception
    {
     ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
    
      
//     ass.setCustomSQL(
//    		 " select unit_cost from( "
//    		 + "select pd.unit_cost, pd.purchase_order_no, pd.item_no, ss.purchase_order_no "
//    		 +"from arc_po_details pd, "
//    		 +"( "
//    		 +"select purchase_order_no, purchase_order_date "
//    		 +"from arc_po_header "
//    		 +"where purchase_order_date = "
//    		 +"(select  max(purchase_order_date) "
//    		 +"from "
//    		 +"(select pd.purchase_order_no, po.purchase_order_no, pd.item_no, po.purchase_order_date "
//    		 +"from arc_po_header po, arc_po_details pd "
//    		 +" where pd.purchase_order_no = po.purchase_order_no "
//    		 +"	and  pd.item_no = '%s')) "
//    		 +") ss "
//			 +"where pd.purchase_order_no = ss.purchase_order_no "
//			 +"and  pd.item_no = '%2$s') "
//			 ,item_no ,item_no); //
     
     ass.setCustomSQL(
    		 " select unit_cost from arc_po_details "
    		 + "where purchase_order_no = "
    		 + "("
    		 + "select max(purchase_order_no) from arc_po_details "
    		 + "where item_no = '%s' "
    		 + ")"
    		 + "and item_no = '%2$s'"
			 ,item_no ,item_no);
     
     
     List<ACFgRawModel> result = ass.executeQuery(ACFtDBUtility.getConnection("ARCDB"));
    
     return  (result.size()>0 ? result.get(0).getBigDecimal("unit_cost") : "").toString();
     
      }

}