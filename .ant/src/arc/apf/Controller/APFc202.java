package arc.apf.Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;

import acf.acf.Dao.ACFoList;
import acf.acf.Dao.ACFoReport;
import acf.acf.Database.ACFdSQLAssSelect;
import acf.acf.General.annotation.ACFgFunction;
import acf.acf.General.core.ACFgRawModel;
import acf.acf.General.core.ACFgRequestParameters;
import acf.acf.General.core.ACFgResponseParameters;
import acf.acf.Service.ACFsReport;
import acf.acf.Static.ACFtUtility;
import acf.acf.Abstract.ACFaAppController;

import arc.apf.Static.APFtMapping;

import arc.apf.Static.APFtUtilityAndGlobal;
import arc.apf.General.report.ARCgPDFGenerator;

@Controller
@Scope("session")
@ACFgFunction(id="APFF202")
@RequestMapping(value=APFtMapping.APFC202)
public class APFc202 extends ACFaAppController {
	
	@Autowired ACFoList listDao;
	@Autowired ACFoReport reportDao;	
	@Autowired ACFsReport reportService;
	
    ARCgPDFGenerator pdf;
	Timestamp modifyfrDate, modifytoDate;

    @RequestMapping(value=APFtMapping.APFC202_MAIN, method=RequestMethod.GET)
	public String index(ModelMap model) throws Exception {
		model.addAttribute("report_formats", reportService.getReportAllowExt("APFF202"));
		return view();
	}	
	
	@RequestMapping(value=APFtMapping.APFC202_REPORT, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters generateReport(@RequestBody ACFgRequestParameters param) throws Exception {
		
		modifyfrDate = param.get("p_modified_date_fr", Timestamp.class);
		modifytoDate = param.get("p_modified_date_to", Timestamp.class);
		System.out.println("fr " + modifyfrDate + " to " + modifytoDate);
		
		ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
        ass.setCustomSQL(ACFtUtility.getJavaResourceInString("/resource/apff202_get_photo_tx.sql"),modifyfrDate,modifytoDate,modifyfrDate,modifytoDate,modifyfrDate,modifytoDate,modifyfrDate,modifytoDate);
        List<ACFgRawModel> report = ass.executeQuery(getConnection("ARCDB"));
		
        try {
            //** set fonts for report
            Font fontbold = ARCgPDFGenerator.createFont(ARCgPDFGenerator.PdfFontFamily.COURIER, 6, Font.BOLD, new BaseColor(0, 0, 0));
            Font font = ARCgPDFGenerator.createFont(ARCgPDFGenerator.PdfFontFamily.COURIER, 6, Font.NORMAL);
            Font chinesefont = ARCgPDFGenerator.createFont(ARCgPDFGenerator.PdfFontFamily.MING_LIU, 6, Font.NORMAL);
            //** set numeric display format for report
            java.text.DecimalFormat df = new java.text.DecimalFormat("###,##0.00");
            java.text.DecimalFormat pf = new java.text.DecimalFormat("000");
            java.text.DecimalFormat nf = new java.text.DecimalFormat("#####0");
            java.text.SimpleDateFormat tf = new java.text.SimpleDateFormat("HH:mm");

            Rectangle cellpadding = new Rectangle(1f,3f,0f,0f);

    		String rpt_path = ACFtUtility.CreateSymbolicLinkIfNotExist("report_temp", defaultService.getDefaultValue("REPORT_TEMP_PATH"));
            pdf = new ARCgPDFGenerator(rpt_path+"/"+reportDao.selectItem("APFF202").filename + ".pdf", ARCgPDFGenerator.PdfPageSize.A4_LANDSCAPE);
            pdf.setWatermark("");
            
            List<ARCgPDFGenerator.ARCgPdfTable> page = new ArrayList<ARCgPDFGenerator.ARCgPdfTable>();
            ARCgPDFGenerator.ARCgPdfTable table = new ARCgPDFGenerator.ARCgPdfTable();
            List<List<ARCgPDFGenerator.ARCgPdfCell>> tableContent = new ArrayList<List<ARCgPDFGenerator.ARCgPdfCell>>();
            List<ARCgPDFGenerator.ARCgPdfCell> tr = new ArrayList<ARCgPDFGenerator.ARCgPdfCell>();

            //** set page header (optional)
            setPageHeader();
            
            //** set data rows per page to print
            int dataRowsPerPage = 25;
            //** incremental counter for page number printing
            int page_num = 1;
            //** incremental counter for tracking rows printed
            int curRow = 1;
            //** incremental amount of result set
            BigDecimal total_amount = new BigDecimal(0);
            
            //** calculate character width according to font settings
            float charWidth = font.getCalculatedBaseFont(true).getWidthPoint("W", font.getCalculatedSize());
            float chineseCharWidth = chinesefont.getCalculatedBaseFont(true).getWidthPoint("\u96fb", chinesefont.getCalculatedSize());
            
            //** calculate column width according to maximum number of characters of each column
            float[] absoluteWidths = {"XXXXXXXXXX".length()*charWidth+charWidth, 
                                      "CCYYMMDD".length()*charWidth+charWidth, 
                                      "XXXXXXXXXXXXXXXXXXXX".length()*charWidth+charWidth, 
                                      "XXXXXXXXX".length()*charWidth+charWidth, 
                                      //APFtUtilityAndGlobal.getUTFStringSize("XXXXXXXXXXXXXXX\u96fbXXXXXXXXXXXXXX")*chineseCharWidth+chineseCharWidth, 
                                      "XXXXXXXXXX".length()*charWidth+charWidth, // space
                                      "XXXXXXX".length()*charWidth+charWidth, 
                                      "XXXXXXX".length()*charWidth+charWidth, 
                                      "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX".length()*charWidth+charWidth, 
                                      "CCYYMMDD".length()*charWidth+charWidth, 
                                      "CCYYMMDD".length()*charWidth+charWidth, 
                                      "XXXX - XXXX".length()*charWidth+charWidth, 
                                      "XX XXXXXXXXXXXXXXX".length()*charWidth+charWidth, 
                                      "XXXXX.XX".length()*charWidth+charWidth, 
                                      "XXXXX.XX".length()*charWidth+charWidth, 
                                      "XXX,XXX,XXX.XX".length()*charWidth+charWidth, 
                                      "XXXXXXXX".length()*charWidth+charWidth};

            //### start page loop ###
            HttpServletRequest request = ACFtUtility.getRequest();
            ARCgPDFGenerator.PdfCellBorder cellborder = ARCgPDFGenerator.PdfCellBorder.NO_BORDER;
            //** incremental counter to track total number of rows printed
            int row_cnt = 1;
            //String prev_job_no = "";
            
            //** loop through the data result set
            for (ACFgRawModel row : report) {
                //** if it is the first row of page, initialize page variables and print page section detail
                if (curRow == 1) {
                    cellpadding = new Rectangle(1f,0f,0f,0f);
                    cellborder = ARCgPDFGenerator.PdfCellBorder.NO_BORDER;
   
                    page = new ArrayList<ARCgPDFGenerator.ARCgPdfTable>();
                    table = new ARCgPDFGenerator.ARCgPdfTable();
                    tableContent = new ArrayList<List<ARCgPDFGenerator.ARCgPdfCell>>();
                    tr = new ArrayList<ARCgPDFGenerator.ARCgPdfCell>();
   
                    // Report Title 1st Line
                    tr.add(pdf.createPdfCell("Date: "+ACFtUtility.timestampToString(ACFtUtility.now(), "yyyy-MM-dd"), ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 19, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
                    tr.add(pdf.createPdfCell("Page: "+pf.format(page_num), ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
                    tableContent.add(tr);
   
                    // Add Function ID and name as title if HTTP request
                    if(request!=null) {
                        Object attr = null;
                        if ((attr=request.getAttribute("info.func_id"))!=null) {
                            tr = new ArrayList<ARCgPDFGenerator.ARCgPdfCell>();
                            tr.add(pdf.createPdfCell("Ref : APHR4113", ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
                            //tr.add(pdf.createPdfCell("Ref : "+(String)attr, ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
                            //tr.add(pdf.createPdfCell((String)attr, ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
                            if ((attr=request.getAttribute("info.func_name"))!=null) {
                            	String rpt_title = (String)attr + " (DDS 2707 - Photo) for the period of " + ACFtUtility.timestampToString(modifyfrDate,"yyyy/MM/dd")+" to "+ACFtUtility.timestampToString(modifytoDate,"yyyy/MM/dd");
                                tr.add(pdf.createPdfCell(rpt_title, ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 19, 1, fontbold, Element.ALIGN_CENTER, Element.ALIGN_CENTER));
                            }
                            tableContent.add(tr);
                        }
                        //if ((attr=request.getAttribute("info.func_name"))!=null) {
                        //    tr = new ArrayList<ARCgPDFGenerator.ARCgPdfCell>();
                        //    tr.add(pdf.createPdfCell((String)attr, ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 22, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
                        //    tableContent.add(tr);
                        //}
                    }
   
                    //** insert new line
                    tr = new ArrayList<ARCgPDFGenerator.ARCgPdfCell>();
                    tr.add(pdf.createPdfCell("", ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 20, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
                    tableContent.add(tr);

                    printColumnTitle(tableContent);
                    

                }

                //** add bottom border line if it is last data row on page or report
                if (curRow == dataRowsPerPage || row_cnt == report.size()) {
                    cellpadding = new Rectangle(1f,2f,0f,0f);
                    cellborder = ARCgPDFGenerator.PdfCellBorder.NO_BORDER;
                }

                //** insert data row

                //if (!(prev_job_no.equals(row.get("job_no")))) {  ** check same job no for this tx
                //prev_job_no = row.getString("job_no");
                tr = new ArrayList<ARCgPDFGenerator.ARCgPdfCell>();

                tr.add(pdf.createPdfCell(row.getString("job_no"), cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
                //tr.add(pdf.createPdfCell("JOB NO XXX", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
            	tr.add(pdf.createPdfCell(ACFtUtility.timestampToString(row.getTimestamp("modified_at"),"yyyy/MM/dd"), cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
            	
            	tr.add(pdf.createPdfCell(row.getString("request_department"), cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 2, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
            	//tr.add(pdf.createPdfCell("REQ DEPT - XXXXXXXXXX", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 2, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
            	
            	tr.add(pdf.createPdfCell(row.getString("programme_no"), cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
            	//tr.add(pdf.createPdfCell("PROG NO XX", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
            	tr.add(pdf.createPdfCell("", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
            	tr.add(pdf.createPdfCell(APFtUtilityAndGlobal.m_Lpad(nf.format(row.getBigDecimal("from_episode_no"))," ",7), cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
            	tr.add(pdf.createPdfCell(APFtUtilityAndGlobal.m_Lpad(nf.format(row.getBigDecimal("to_episode_no"))," ",7), cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
            	String col_remarks = row.getString("remarks");
            	if (col_remarks.length() > 23)
            	   col_remarks = col_remarks.substring(0,23);
            	boolean chk_rmk = m_IsASCII(col_remarks);
            	if (chk_rmk) {
            	tr.add(pdf.createPdfCell(col_remarks, cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 2, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
            	}
            	else{
            		tr.add(pdf.createPdfCell(col_remarks, cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 2, 1, chinesefont, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
            	}
            	tr.add(pdf.createPdfCell(ACFtUtility.timestampToString(row.getTimestamp("photo_from_date"),"yyyy/MM/dd"), cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
            	tr.add(pdf.createPdfCell(ACFtUtility.timestampToString(row.getTimestamp("photo_to_date"),"yyyy/MM/dd"), cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
            	String col_time = tf.format(row.get("photo_from_time")) + "-" + tf.format(row.get("photo_to_time"));
            	tr.add(pdf.createPdfCell(col_time, cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
            	String col_ac_labour = row.getString("col_type");
            	if (col_ac_labour.length() > 23)
             	   col_ac_labour = col_ac_labour.substring(0,23);
            	tr.add(pdf.createPdfCell(col_ac_labour, cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 2, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
            	tr.add(pdf.createPdfCell(APFtUtilityAndGlobal.m_Lpad(df.format(row.getBigDecimal("col_unit_cost"))," ",5), cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_RIGHT, Element.ALIGN_CENTER));
            	tr.add(pdf.createPdfCell(APFtUtilityAndGlobal.m_Lpad(nf.format(row.getBigDecimal("col_qty"))," ",7), cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_RIGHT, Element.ALIGN_CENTER));
            	tr.add(pdf.createPdfCell(APFtUtilityAndGlobal.m_Lpad(df.format(row.getBigDecimal("col_amount"))," ",7), cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 2, 1, font, Element.ALIGN_RIGHT, Element.ALIGN_CENTER));
            	tr.add(pdf.createPdfCell(row.getString("modified_by"), cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_RIGHT, Element.ALIGN_CENTER));
            	tableContent.add(tr);
            	
            	//** accumulate amount
            	BigDecimal this_amt = row.getBigDecimal("col_amount");
            	total_amount = total_amount.add(this_amt);
            	
                //** insert 2nd data row
                tr = new ArrayList<ARCgPDFGenerator.ARCgPdfCell>();
           	
                tr.add(pdf.createPdfCell("", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));                
            	tr.add(pdf.createPdfCell("", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));           	
            	tr.add(pdf.createPdfCell("", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 2, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
            	String col_prog_name = row.getString("programme_name");
            	if (col_prog_name.length() > 45)
            	   col_prog_name = col_prog_name.substring(0,45);
            	tr.add(pdf.createPdfCell(col_prog_name, cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
             	tr.add(pdf.createPdfCell("", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
            	tr.add(pdf.createPdfCell("", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
            	tr.add(pdf.createPdfCell("", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
            	boolean chk_desc = m_IsASCII(row.getString("job_description"));
            	if (chk_desc) {
                	tr.add(pdf.createPdfCell(row.getString("job_description"), cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 3, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
            	}	
                else {
                	tr.add(pdf.createPdfCell(row.getString("job_description"), cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 3, 1, chinesefont, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
                }
            	tr.add(pdf.createPdfCell("", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 10, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
            	tableContent.add(tr);
                //}
                //else {
                //    prev_job_no = row.getString("job_no");
                //    tr = new ArrayList<ARCgPDFGenerator.ARCgPdfCell>();
                	
                //    tr.add(pdf.createPdfCell("", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));                   
                //	tr.add(pdf.createPdfCell("", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
                //  tr.add(pdf.createPdfCell("", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 2, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
                // 	tr.add(pdf.createPdfCell("", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
                //  tr.add(pdf.createPdfCell("", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
                // 	tr.add(pdf.createPdfCell("", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
                // 	tr.add(pdf.createPdfCell("", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
                // 	tr.add(pdf.createPdfCell("", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 2, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
                // 	tr.add(pdf.createPdfCell("", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
                // 	tr.add(pdf.createPdfCell("", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
                // 	tr.add(pdf.createPdfCell("", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
                // 	String col_ac_labour = row.getString("col_type");
                // 	if (col_ac_labour.length() > 23)
                //  	   col_ac_labour = col_ac_labour.substring(0,23);
                // 	tr.add(pdf.createPdfCell(col_ac_labour, cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 2, 1, font, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
                // 	tr.add(pdf.createPdfCell(APFtUtilityAndGlobal.m_Lpad(df.format(row.getBigDecimal("col_unit_cost"))," ",5), cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_RIGHT, Element.ALIGN_CENTER));
                // 	tr.add(pdf.createPdfCell(APFtUtilityAndGlobal.m_Lpad(nf.format(row.getBigDecimal("col_qty"))," ",7), cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_RIGHT, Element.ALIGN_CENTER));
                // 	tr.add(pdf.createPdfCell(APFtUtilityAndGlobal.m_Lpad(df.format(row.getBigDecimal("col_amount"))," ",7), cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 2, 1, font, Element.ALIGN_RIGHT, Element.ALIGN_CENTER));
                // 	tr.add(pdf.createPdfCell("", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_RIGHT, Element.ALIGN_CENTER));
                // 	tableContent.add(tr);
                	
                //} ** end of if prev job no

            	//** if it is the last data row of page or report, finalize the page and insert into PDF
            	if (curRow == dataRowsPerPage || row_cnt == report.size()) {
            	    curRow = 1;
            	    page_num++;

            	    //TODO ** print balance lines **
            	    
                    //** print end-of-report
                    if (row_cnt == report.size()) {
                        cellpadding = new Rectangle(1f,1f,1f,1f);
                        
                        //** insert new line
                        tr = new ArrayList<ARCgPDFGenerator.ARCgPdfCell>();
                        tr.add(pdf.createPdfCell("", ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 20, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_CENTER));
                        tableContent.add(tr);

                        //** print total amount
                        tr = new ArrayList<ARCgPDFGenerator.ARCgPdfCell>();
                        tr.add(pdf.createPdfCell("Total : ", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 17, 1, font, Element.ALIGN_RIGHT, Element.ALIGN_CENTER));
                        tr.add(pdf.createPdfCell(APFtUtilityAndGlobal.m_Lpad(df.format(total_amount.abs())," ",14), cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 2, 1, font, Element.ALIGN_RIGHT, Element.ALIGN_CENTER));                        
                        tr.add(pdf.createPdfCell("", cellborder, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_RIGHT, Element.ALIGN_CENTER));
                        tableContent.add(tr);
                        
                        //** print end-of-report line
                        tr = new ArrayList<ARCgPDFGenerator.ARCgPdfCell>();
                        tr.add(pdf.createPdfCell(APFtUtilityAndGlobal.APF_END_OF_REPORT, ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 20, 1, fontbold, Element.ALIGN_CENTER, Element.ALIGN_CENTER));
                        tableContent.add(tr);
                    }

                    table.setTableContent(tableContent);
                    table.setColumns(20);
                    table.setAbsoluteWidths(absoluteWidths);
                    
                    page.add(table);
                    
                    pdf.insertPageContent(page);
                    
                    pdf.insertPageContent(pdf.createPdfNewPage(null, null));
            	} else {
            	    curRow++;
            	}
            	
            	row_cnt++;
            	
            }            
                       
            //** generate the PDF

            if (report.size()==0){ //** no record found 
            	tr = new ArrayList<ARCgPDFGenerator.ARCgPdfCell>();
            	tr.add(pdf.createPdfCell(APFtUtilityAndGlobal.APF_NO_RECORD_FOUND, ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 20, 1, fontbold, Element.ALIGN_CENTER, Element.ALIGN_CENTER));
            	tableContent.add(tr);
            	table.setTableContent(tableContent);
            	table.setColumns(20);
            	table.setAbsoluteWidths(absoluteWidths);           
            	page.add(table);         
            	pdf.insertPageContent(page);            
            	pdf.insertPageContent(pdf.createPdfNewPage(null, null));
            }
            
            pdf.generatePDF();

            
        } catch (DocumentException e) {
           throw exceptionService.error("APF001S",e);
        } catch (Exception e) {
           throw e;
        }
		
		return new ACFgResponseParameters();
	}		
	
	private void setPageHeader() throws DocumentException, IOException {
        Rectangle cellpadding = new Rectangle(1f,3f,0f,0f);
        Font font = ARCgPDFGenerator.createFont(ARCgPDFGenerator.PdfFontFamily.COURIER, 6, Font.NORMAL);
        Font chinesefont = ARCgPDFGenerator.createFont(ARCgPDFGenerator.PdfFontFamily.MING_LIU, 6, Font.NORMAL);

        ARCgPDFGenerator.ARCgPdfTable table = new ARCgPDFGenerator.ARCgPdfTable();
        List<List<ARCgPDFGenerator.ARCgPdfCell>> tableContent = new ArrayList<List<ARCgPDFGenerator.ARCgPdfCell>>();
        List<ARCgPDFGenerator.ARCgPdfCell> tr = new ArrayList<ARCgPDFGenerator.ARCgPdfCell>();
        
        tr.add(pdf.createPdfCell(pdf.createImage(APFtUtilityAndGlobal.APF_TVB_LOGO_PATH), ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 2, font, Element.ALIGN_RIGHT, Element.ALIGN_CENTER, true));
        cellpadding = new Rectangle(1f,3f,2f,0f);
        tr.add(pdf.createPdfCell("Television Broadcasts Limited", ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, font, Element.ALIGN_LEFT, Element.ALIGN_TOP));
        //** use Unicode for typing Chinese characters (@link http://www.ifreesite.com/unicode-ascii-ansi.htm)
        tr.add(pdf.createPdfCell("\u96fb\u8996\u5ee3\u64ad\u6709\u9650\u516c\u53f8", ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, chinesefont, Element.ALIGN_LEFT, Element.ALIGN_TOP));
        tableContent.add(tr);
        table.setTableContent(tableContent);
        table.setColumns(2);
        table.setAbsoluteWidths(new float[]{32f, 100f});
        pdf.setPageHeader(table);
	    
	}
		
    @SuppressWarnings("unused")
    private void setPageFooter() throws DocumentException, IOException {
        /* Pending to implement */
    }
     
    private boolean m_IsASCII(String s) {
        boolean bIsASCII = true;

        for (int i = 0; i < s.length(); i++) {
              char c = s.charAt(i);

              // ranges from http://en.wikipedia.org/wiki/UTF-8
              if (c <= 0x007f) {
                    /* DO NOTHING */
              } else {
                    bIsASCII = false;
                    break;
              }
          }
        
          return bIsASCII;
      }

    
    private void printColumnTitle(List<List<ARCgPDFGenerator.ARCgPdfCell>> tableContent) throws DocumentException, IOException {
        Font fontbold = ARCgPDFGenerator.createFont(ARCgPDFGenerator.PdfFontFamily.COURIER, 6, Font.BOLD, new BaseColor(0, 0, 0));
        List<ARCgPDFGenerator.ARCgPdfCell> tr = new ArrayList<ARCgPDFGenerator.ARCgPdfCell>();
        Rectangle cellpadding = new Rectangle(1f,3f,0f,0f);

        //** first column title line
        tr = new ArrayList<ARCgPDFGenerator.ARCgPdfCell>();
        tr.add(pdf.createPdfCell("", ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM));
        tr.add(pdf.createPdfCell("MODIFIED", ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM));
        tr.add(pdf.createPdfCell("", ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 2, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM));
        tr.add(pdf.createPdfCell("PROGRAMME NO./", ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 2, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM));
        tr.add(pdf.createPdfCell("EPI.#", ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM));
        tr.add(pdf.createPdfCell("EPI.#", ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM));
        tr.add(pdf.createPdfCell("REMARKS/", ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 2, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM));
        tr.add(pdf.createPdfCell("PHOTO TAKING", ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 3, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM));        
        tr.add(pdf.createPdfCell("A/C ALLOC DESC/", ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 2, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM));
        tr.add(pdf.createPdfCell("UNIT COST/", ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, fontbold, Element.ALIGN_RIGHT, Element.ALIGN_BOTTOM));
        tr.add(pdf.createPdfCell("QTY/", ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, fontbold, Element.ALIGN_RIGHT, Element.ALIGN_BOTTOM));
        tr.add(pdf.createPdfCell("", ARCgPDFGenerator.PdfCellBorder.NO_BORDER, ARCgPDFGenerator.PdfCellBorderStyle.SOLID, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 3, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM));
        tableContent.add(tr);

        //** second column title line
        tr = new ArrayList<ARCgPDFGenerator.ARCgPdfCell>();
        tr.add(pdf.createPdfCell("JOB NO.", ARCgPDFGenerator.PdfCellBorder.BOTTOM, ARCgPDFGenerator.PdfCellBorderStyle.DASHED, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM));
        tr.add(pdf.createPdfCell("DATE", ARCgPDFGenerator.PdfCellBorder.BOTTOM, ARCgPDFGenerator.PdfCellBorderStyle.DASHED, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM));
        tr.add(pdf.createPdfCell("REQUEST DEPT", ARCgPDFGenerator.PdfCellBorder.BOTTOM, ARCgPDFGenerator.PdfCellBorderStyle.DASHED, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 2, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM));
        tr.add(pdf.createPdfCell("PROGRAMME NAME", ARCgPDFGenerator.PdfCellBorder.BOTTOM, ARCgPDFGenerator.PdfCellBorderStyle.DASHED, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 2, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM));
        tr.add(pdf.createPdfCell("FR", ARCgPDFGenerator.PdfCellBorder.BOTTOM, ARCgPDFGenerator.PdfCellBorderStyle.DASHED, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM));
        tr.add(pdf.createPdfCell("TO", ARCgPDFGenerator.PdfCellBorder.BOTTOM, ARCgPDFGenerator.PdfCellBorderStyle.DASHED, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM));
        tr.add(pdf.createPdfCell("JOB DESC", ARCgPDFGenerator.PdfCellBorder.BOTTOM, ARCgPDFGenerator.PdfCellBorderStyle.DASHED, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 2, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM));
        tr.add(pdf.createPdfCell("DATE FR TO", ARCgPDFGenerator.PdfCellBorder.BOTTOM, ARCgPDFGenerator.PdfCellBorderStyle.DASHED, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 2, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM));
        tr.add(pdf.createPdfCell("TIME FR TO", ARCgPDFGenerator.PdfCellBorder.BOTTOM, ARCgPDFGenerator.PdfCellBorderStyle.DASHED, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM));        
        tr.add(pdf.createPdfCell("LABOUR TYPE DESC", ARCgPDFGenerator.PdfCellBorder.BOTTOM, ARCgPDFGenerator.PdfCellBorderStyle.DASHED, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 2, 1, fontbold, Element.ALIGN_LEFT, Element.ALIGN_BOTTOM));
        tr.add(pdf.createPdfCell("HOUR RATE", ARCgPDFGenerator.PdfCellBorder.BOTTOM, ARCgPDFGenerator.PdfCellBorderStyle.DASHED, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, fontbold, Element.ALIGN_RIGHT, Element.ALIGN_BOTTOM));
        tr.add(pdf.createPdfCell("NO. OF HR", ARCgPDFGenerator.PdfCellBorder.BOTTOM, ARCgPDFGenerator.PdfCellBorderStyle.DASHED, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, fontbold, Element.ALIGN_RIGHT, Element.ALIGN_BOTTOM));
        tr.add(pdf.createPdfCell("AMOUNT", ARCgPDFGenerator.PdfCellBorder.BOTTOM, ARCgPDFGenerator.PdfCellBorderStyle.DASHED, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 2, 1, fontbold, Element.ALIGN_RIGHT, Element.ALIGN_BOTTOM));
        tr.add(pdf.createPdfCell("USER ID", ARCgPDFGenerator.PdfCellBorder.BOTTOM, ARCgPDFGenerator.PdfCellBorderStyle.DASHED, ARCgPDFGenerator.PdfCellBorderWidth.THIN, cellpadding, 1, 1, fontbold, Element.ALIGN_RIGHT, Element.ALIGN_BOTTOM));
        tableContent.add(tr);
        
    }
    
	@RequestMapping(value=APFtMapping.APFC202_GET_PDF, method={RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<byte[]> getPDF(HttpServletResponse response) throws Exception {
		try {
			byte[] body;
			try {
				body = pdf.getPDFFile();
			} catch(javax.crypto.BadPaddingException e) {
				throw exceptionService.error("ACF045E", e);
			} catch(FileNotFoundException e) {
				throw exceptionService.error("ACF046E", e);
			}
			if(body==null)
				throw exceptionService.error("ACF046E");

		    final HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.parseMediaType( listDao.selectItem("ACF_MIME_TYPE", "PDF").list_desc ));
		    headers.set("Content-Disposition", "attachment; filename=\"" + reportDao.selectItem("APFF202").filename + ".pdf" + "\"");
		    headers.set("Content-Transfer-Encoding", "binary");
		    headers.setContentLength(body.length);
		    
		    return new ResponseEntity<byte[]>(body, headers, HttpStatus.OK);
		} catch(Exception e) {			
			e.printStackTrace(System.err);
			HttpHeaders headers = new HttpHeaders();
			return new ResponseEntity<byte[]>(e.getMessage().getBytes(), headers, HttpStatus.BAD_REQUEST);
		}
	}
	
}