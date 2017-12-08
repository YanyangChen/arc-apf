package arc.apf.Controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import acf.acf.Abstract.ACFaAppController;
import acf.acf.Abstract.ACFaAppModel;
import acf.acf.Database.ACFdSQLAssDelete;
import acf.acf.Database.ACFdSQLAssInsert;
import acf.acf.Database.ACFdSQLAssSelect;
import acf.acf.Database.ACFdSQLAssUpdate;
import acf.acf.Database.ACFdSQLRule;
import acf.acf.Database.ACFdSQLRule.RuleCase;
import acf.acf.Database.ACFdSQLRule.RuleCondition;
import acf.acf.Database.ACFdSQLWhere;
import acf.acf.General.annotation.ACFgAuditKey;
import acf.acf.General.annotation.ACFgFunction;
import acf.acf.General.annotation.ACFgTransaction;
import acf.acf.General.core.ACFgRawModel;
import acf.acf.General.core.ACFgRequestParameters;
import acf.acf.General.core.ACFgResponseParameters;
import acf.acf.General.core.ACFgSearch;
import acf.acf.Interface.ACFiCallback;
import acf.acf.Interface.ACFiSQLAssWriteInterface;
import acf.acf.Model.ACFmGridResult;
import acf.acf.Service.ACFsSecurity;
import acf.acf.Static.ACFtUtility;
import arc.apf.Service.ARCsPaymentRequest;
import arc.apf.Service.ARCsSection;
import arc.apf.Dao.ARCoPaymentRequest;
import arc.apf.Model.ARCmItemInventory;
import arc.apf.Model.ARCmPaymentRequest;
import arc.apf.Model.ARCmSection;
import arc.apf.Service.ARCsSupplier;
import arc.apf.Static.APFtMapping;
import arc.apf.Static.APFtUtilityAndGlobal;

@Controller
@Scope("session")
@ACFgFunction(id="APFF201")
@RequestMapping(value=APFtMapping.APFF201)
public class APFc201 extends ACFaAppController {

	@Autowired ARCoPaymentRequest paymentRequestDao;
	@Autowired ARCsPaymentRequest paymentRequestService;
	@Autowired ARCsSupplier       supplierService;
	@Autowired ARCsSection SectionService;
	@Autowired ACFsSecurity SecurityService;
	@ACFgAuditKey String payment_form_no;
	@ACFgAuditKey Timestamp request_date;
	@ACFgAuditKey String supplier_code;
	@ACFgAuditKey String section_id;
	
	
	
	
	Timestamp defaultTimestamp = getDefaultTimestamp();
	
	Search search = new Search();
	
	private class Search extends ACFgSearch {
		 public Search() {
	            super();
	            setCustomSQL("select * from (select pr.payment_form_no, pr.payment_purpose, pr.supplier_code, pr.section_id, pr.request_date, pr.created_by, pr.modified_at, pr.printed_by, pr.printed_at, pr.print_indicator, pr.supplier_code, s.supplier_name " +
	                    "from arc_payment_request pr, arc_supplier s where s.supplier_code = pr.supplier_code) " );
	            setKey("payment_form_no");////modify according to arc table's columns apw_ arc_po_header apw_supplier -> dev.arc_supplier
	            addRule(new ACFdSQLRule("section_id", RuleCondition.EQ, null, RuleCase.Sensitive)); //sec_id
	            addRule(new ACFdSQLRule("supplier_code", RuleCondition.EQ, null, RuleCase.Insensitive));
	            addRule(new ACFdSQLRule("created_by", RuleCondition.EQ, null, RuleCase.Insensitive));
	        }

		 @Override
	        public Search setValues(ACFgRequestParameters param) throws Exception {
	            super.setValues(param);
	                if(!param.isEmptyOrNull("request_start_date")) {
	                wheres.and("request_date", ACFdSQLRule.RuleCondition.GE, param.get("request_start_date", Timestamp.class));
	                }
	                if(!param.isEmptyOrNull("request_end_date")) {
	                wheres.and("request_date", ACFdSQLRule.RuleCondition.LT, new Timestamp(param.get("request_end_date", Long.class) + 24*60*60*1000));
	                }
	           
	                if(!param.isEmptyOrNull("payment_form_no_from")) {
	                wheres.and("payment_form_no", ACFdSQLRule.RuleCondition.GE, param.get("payment_form_no_from", String.class));
	                }
	                if(!param.isEmptyOrNull("payment_form_no_to")) {
	                wheres.and("payment_form_no", ACFdSQLRule.RuleCondition.LT, param.get("payment_form_no_to", String.class));
	                }
	           
	            return this;
	        }		
		
	}

	@RequestMapping(value=APFtMapping.APFF201_MAIN, method=RequestMethod.GET)
	public String main(ModelMap model) throws Exception {

//		model.addAttribute("supplierselect", supplierService.getSupplierPairs());
		model.addAttribute("sectionid", SectionService.getSectionId()); 
		model.addAttribute("suppliercode", supplierService.getSupplierPairs()); 
		
		return view();
	}

	public static Timestamp getDefaultTimestamp(){
		return getDateOnly(getTimestamp(1900, Calendar.JANUARY, 1));
	}
		
	public static Timestamp getDateOnly(Timestamp timestamp){
		Date datetime = new Date(timestamp.getTime());
		return new Timestamp(DateUtils.truncate(datetime, Calendar.DATE).getTime());
	}
	
	public static Timestamp getTimestamp(int year, int month, int date){
		Calendar c = new GregorianCalendar(year, month, date);
		return new Timestamp(c.getTimeInMillis());
	}
	
	
	@RequestMapping(value=APFtMapping.APFF201_SEARCH_AJAX, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters getGrid(@RequestBody ACFgRequestParameters param) throws Exception {

		search.setConnection(getConnection("ARCDB"));
		search.setValues(param);
		search.setFocus(payment_form_no);
		
		return getResponseParameters().set("grid_payment_request", search.getGridResult());
	}  		

	@RequestMapping(value=APFtMapping.APFF201_GET_FORM_AJAX, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters getForm(@RequestBody ACFgRequestParameters param) throws Exception {
		
		ACFgResponseParameters resParam = this.getResponseParameters();		
		this.getPayment(param.getRequestParameter("grid_payment_request"));

		return resParam;
	}

	@RequestMapping(value=APFtMapping.APFF201_LIST_AJAX, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters getPayment(@RequestBody ACFgRequestParameters reqparam) throws Exception {

		search.setConnection(getConnection("ARCDB"));
		search.setValues(reqparam);
		search.setFocus(payment_form_no);
		
		ACFmGridResult grid = search.getGridResult();

		return this.getResponseParameters().set("grid_payment_request", grid);
	}	
	
	@ACFgTransaction
	@RequestMapping(value=APFtMapping.APFF201_SAVE_AJAX, method=RequestMethod.POST)
	@ResponseBody
	public ACFgResponseParameters save(@RequestBody ACFgRequestParameters param) throws Exception {

        final List<ARCmPaymentRequest> amendments = param.getList("payment_request", ARCmPaymentRequest.class);
        System.out.println(amendments);
        
        ARCmPaymentRequest lastItem = paymentRequestDao.saveItems(amendments);
//        payment_form_no = lastItem!=null? lastItem.payment_form_no: null;
        
        return new ACFgResponseParameters();

     }
	

	public List<ARCmPaymentRequest> process_print (List<ARCmPaymentRequest> RequestList)
	{
		for (ARCmPaymentRequest Request_record : RequestList)
		{
			if (Request_record.print_indicator.equals("1"))
			{
				Request_record.no_of_times_printed = new BigDecimal(Request_record.no_of_times_printed.intValue() + 1);
				Request_record.printed_by = SecurityService.getCurrentUser().user_id;
				Request_record.printed_at = ACFtUtility.now();
			}
		}
		return RequestList;
	}



@ACFgTransaction
@RequestMapping(value=APFtMapping.APFF201_GENERATE_REPORT_AJAX, method=RequestMethod.POST)
@ResponseBody
public ACFgResponseParameters generate_report(@RequestBody ACFgRequestParameters param) throws Exception { 
	//get lists to be printed from client
	List<ARCmPaymentRequest> amendments = param.getList("payment_request", ARCmPaymentRequest.class);
	System.out.println("**************************************Testing list geting from client**********************************");
	System.out.println(amendments);
	ACFgResponseParameters resParam = new ACFgResponseParameters();
	List<ARCmPaymentRequest> filtered = filter(amendments);
	resParam = genReportFormat2(filtered);
    return resParam;
}



public ACFgResponseParameters genReportFormat(List<? extends ACFaAppModel> report) throws Exception
{
	ACFgResponseParameters resParam = new ACFgResponseParameters();
	//** set numeric display format for report
    java.text.DecimalFormat amountFormat = new java.text.DecimalFormat("####0.00");
    java.text.DecimalFormat totalAmountFormat = new java.text.DecimalFormat("###,##0.00");
    java.text.DecimalFormat pageNumberFormat = new java.text.DecimalFormat("00");

    StringBuilder pageStyle = new StringBuilder(); 
    pageStyle
    .append("<style type=\"text/css\">")
    .append("body {")
    .append("  background: rgb(204,204,204); ")
    .append("}")
    .append("page {")
    .append("  background: white;")
    .append("  display: block;")
    .append("  margin: 0 auto;")
    .append("  margin-bottom: 0.5cm;")
    .append("  box-shadow: 0 0 0.5cm rgba(0,0,0,0.5);")
    .append("}")
    .append("page[size=\"A4\"] {  ")
    .append("  width: 21cm;")
    .append("  height: 29.7cm; ")
    .append("}")
    .append("page[size=\"A4\"][layout=\"landscape\"] {")
    .append("  width: 29.7cm;")
    .append("  height: 21cm;  ")
    .append("}")
    .append("page[size=\"A3\"] {")
    .append("  width: 29.7cm;")
    .append("  height: 42cm;")
    .append("}")
    .append("page[size=\"A3\"][layout=\"landscape\"] {")
    .append("  width: 42cm;")
    .append("  height: 29.7cm;  ")
    .append("}")
    .append("page[size=\"A5\"] {")
    .append("  width: 14.8cm;")
    .append("  height: 21cm;")
    .append("}")
    .append("page[size=\"A5\"][layout=\"landscape\"] {")
    .append("  width: 21cm;")
    .append("  height: 14.8cm;  ")
    .append("}")
    .append("@media print {")
    .append("  body, page {")
    .append("    margin: 0;")
    .append("    box-shadow: 0;")
    .append("  }")
    .append("  html, body {height: 99%;}")
    .append("}")
    .append("</style>");
    
    StringBuilder tableStyle = new StringBuilder();
    tableStyle
    .append("<style type=\"text/css\">")
    /* Column widths are based on these cells */
    .append(".col-po_no {")
    .append("  width: 18mm;")
    .append("}")
    .append(".col-particulars {")
    .append("  width: 50mm;")
    .append("}")
    .append(".col-epi_num {")
    .append("  width: 46mm;")
    .append("}")
    .append(".col-amount {")
    .append("  width: 45mm;")
    .append("}")
    .append(".col-remarks {")
    .append("  width: 48mm;")
    .append("}")
    .append(".section-col-1 {")
    .append("  width: 40mm;")
    .append("}")
    .append(".section-col-2 {")
    .append("  width: 45mm;")
    .append("}")
    .append(".section-col-3 {")
    .append("  width: 80mm;")
    .append("}")
    .append(".section-col-4 {")
    .append("  width: 40mm;")
    .append("}")
    /* Overall table layout */
    .append("table.table-fixed {")
    .append("  table-layout: fixed;")
    .append("}")
    .append("table.table-fixed tr.section {")
    .append("  height: 9mm;")
    .append("}")
    .append("table.table-fixed tr.item {")
    .append("  height: 4mm;")
    .append("}")
    /* Overall table font */
    .append(".table-font {")
    .append("  font-family:\"courier new\",monospace;")
    .append("  font-size:0.8em; <font color='red'>/* Never set font sizes in pixels! */</font>")
    .append("  color:#00f;")
    .append("}")
    /* Empty line CSS */
    .append("p.pg-thin {  margin-top: 0.5em; margin-bottom: 0em; }")
    .append("</style>");
    
    /* 
     * Variable substitution format = {variable_name}. Must be a unique variable name for each piece of HTML code insert.
     */
    String V_PAGE_NUMBER = "{page_number}";
    String V_DEPARTMENT = "{department}";
    String V_DATE = "{date}";
    String V_REFERENCE_NO = "{reference_no}";
    String V_DDS_CODE = "{dds_code}";
    String V_PAYEE = "{payee}";;
    String V_PROGRAMME_NAME = "{programme_name}";
    String V_EPI_NUM = "{epi_num}";
    String V_OTHERS = "{others}";
    String V_PURPOSE = "{purpose}";
    String V_ITEM_PO_NO = "{item_po_no}";
    String V_ITEM_PARTICULARS = "{item_particulars}";
    String V_ITEM_EPI_NUM = "{item_epi_num}";
    String V_ITEM_AMOUNT = "{item_amount}";
    String V_ITEM_AMOUNT_TOTAL = "{item_amount_total}";
    String V_ITEM_ACC_ALLOC = "{item_acc_alloc}";
    
    /* Variable content table header */
    StringBuilder tableHeader = new StringBuilder();
    tableHeader
    .append("<p class=\"pg-thin\"></p>")
    .append("<table class=\"table-fixed table-font\">")
    .append("<tbody>")
        .append("<tr class=\"section\">")
        .append("<td class=\"section-col-1\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"section-col-2\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"section-col-3\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"section-col-4\" style=\"text-align: left;\">PAGE"+V_PAGE_NUMBER+"</td>")
        .append("</tr>")
        .append("<tr class=\"section\">")
        .append("<td class=\"section-col-1\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td>"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td>"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"section-col-4\" style=\"text-align: left;\">"+V_DATE+"</td>")
        .append("</tr>")
        .append("<tr class=\"section\">")
        .append("<td class=\"section-col-1\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td>"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td>"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"section-col-4\" style=\"text-align: left;\">"+V_REFERENCE_NO+"</td>")
        .append("</tr>")
        .append("<tr class=\"section\">")
        .append("<td class=\"section-col-1\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td style=\"text-align: left;\">"+V_DEPARTMENT+"</td>")
        .append("<td>"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"section-col-4\" style=\"text-align: left;\">"+V_DDS_CODE+"</td>")
        .append("</tr>")
        .append("<tr class=\"section\">")
        .append("<td class=\"section-col-1\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td colspan=\"3\" style=\"word-break: break-all\">"+V_PAYEE+"</td>")
        .append("</tr>")
        .append("<tr class=\"section\">")
        .append("<td class=\"section-col-1\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td>"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td>"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td>"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("</tr>")
        .append("<tr class=\"section\">")
        .append("<td class=\"section-col-1\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td colspan=\"2\" style=\"text-align: left;\">"+V_PROGRAMME_NAME+"</td>")
        .append("<td class=\"section-col-4\" style=\"text-align: left;\">"+V_EPI_NUM+"</td>")
        .append("</tr>")
        .append("<tr class=\"section\">")
        .append("<td class=\"section-col-1\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td>"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td colspan=\"2\" style=\"text-align: left;\">"+V_OTHERS+"</td>")
        .append("</tr>")
        .append("<tr class=\"section\">")
        .append("<td class=\"section-col-1\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td colspan=\"3\" style=\"word-break: break-all\">"+V_PURPOSE+"</td>")
        .append("</tr>")
        .append("<tr class=\"section\">")
        .append("<td class=\"section-col-1\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td>"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td>"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td>"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("</tr>")
    .append("</tbody>")
    .append("</table>");
    
    /* Fixed content column header */
    StringBuilder columnHeader = new StringBuilder();
    columnHeader
    .append("<tr class=\"item\">")
    .append("<td class=\"col-po_no\" style=\"text-align: center;\">P.O.NO</td>")
    .append("<td class=\"col-particulars\" style=\"text-align: left;\">PARTICULARS</td>")
    .append("<td class=\"col-epi_num\" style=\"text-align: left;\">EPI#</td>")
    .append("<td class=\"col-amount\" style=\"text-align: right; padding-right: 5px;\">AMOUNT(HK$)</td>")
    .append("<td class=\"col-remarks\" style=\"text-align: left; padding-left: 10px;\">ACCOUNT ALLOCATION</td>")
    .append("</tr>")
    .append("<tr class=\"item\">")
    .append("<td class=\"col-po_no\" style=\"text-align: center;\">------</td>")
    .append("<td class=\"col-particulars\" style=\"text-align: left;\">-----------</td>")
    .append("<td class=\"col-epi_num\" style=\"text-align: left;\">----</td>")
    .append("<td class=\"col-amount\" style=\"text-align: right; padding-right: 5px;\">-----------</td>")
    .append("<td class=\"col-remarks\" style=\"text-align: left; padding-left: 10px;\">------------------</td>")
    .append("</tr>");
    
    /* Fixed content page footer */
    StringBuilder pageFooter = new StringBuilder();
    pageFooter
    .append("<table class=\"table-fixed table-font\">")
    .append("<tbody>")
        .append("<tr class=\"item\">")
        .append("<td class=\"col-po_no\" style=\"text-align: center;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"col-particulars\" style=\"text-align: left;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"col-epi_num\" style=\"text-align: left;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"col-amount\" style=\"text-align: right; padding-right: 5px;\">---------</td>")
        .append("<td class=\"col-remarks\" style=\"text-align: left; padding-left: 10px;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("</tr>")
        .append("<tr class=\"item\">")
        .append("<td class=\"col-po_no\" style=\"text-align: center;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"col-particulars\" style=\"text-align: left;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"col-epi_num\" style=\"text-align: left;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"col-amount\" style=\"text-align: right; padding-right: 5px;\">"+V_ITEM_AMOUNT_TOTAL+"</td>")
        .append("<td class=\"col-remarks\" style=\"text-align: left; padding-left: 10px;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("</tr>")
        .append("<tr class=\"item\">")
        .append("<td class=\"col-po_no\" style=\"text-align: center;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"col-particulars\" style=\"text-align: left;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"col-epi_num\" style=\"text-align: left;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"col-amount\" style=\"text-align: right; padding-right: 5px;\">---------</td>")
        .append("<td class=\"col-remarks\" style=\"text-align: left; padding-left: 10px;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("</tr>")
    .append("</tbody>")
    .append("</table>");
    
    /* Variable content item row */
    StringBuilder itemRow = new StringBuilder();
    itemRow
    .append("<tr class=\"item\">")
    .append("<td class=\"col-po_no\" style=\"text-align: center;\">"+V_ITEM_PO_NO+"</td>")
    .append("<td class=\"col-particulars\" style=\"text-align: left;\">"+V_ITEM_PARTICULARS+"</td>")
    .append("<td class=\"col-epi_num\" style=\"text-align: left;\">"+V_ITEM_EPI_NUM+"</td>")
    .append("<td class=\"col-amount\" style=\"text-align: right; padding-right: 5px;\">"+V_ITEM_AMOUNT+"</td>")
    .append("<td class=\"col-remarks\" style=\"text-align: left; padding-left: 10px;\">"+V_ITEM_ACC_ALLOC+"</td>")
    .append("</tr>");
    
    //** construct final HTML result codes
    StringBuilder html = new StringBuilder(); 
    html.append(pageStyle);
    html.append(tableStyle);
    html.append("<page size=\"A4\">");
    
    int dataRowPerPage = 25;
    int row_cnt = 1;
    int cur_row = 1;
    int page_num = 1;
    
    //** loop through the data result set to construct list item HTML codes
    for (ACFaAppModel row : report) {
        if (cur_row == 1) {
            html.append(new StringBuilder(tableHeader));
            APFtUtilityAndGlobal.m_replace(V_PAGE_NUMBER, pageNumberFormat.format(page_num), html);
            APFtUtilityAndGlobal.m_replace(V_DATE, ACFtUtility.timestampToString(row.getTimestamp("request_date"),"dd MMMM yyyy"), html);
            APFtUtilityAndGlobal.m_replace(V_REFERENCE_NO, row.getString("payment_form_no"), html);
            APFtUtilityAndGlobal.m_replace(V_DEPARTMENT, "ART ADMIN", html);
            APFtUtilityAndGlobal.m_replace(V_DDS_CODE, "A301/01", html);
            APFtUtilityAndGlobal.m_replace(V_PAYEE, "MAN YUEN FLOWER SHOP", html);
            APFtUtilityAndGlobal.m_replace(V_PROGRAMME_NAME, "MASTER WONG FEI HUNG", html);
            APFtUtilityAndGlobal.m_replace(V_EPI_NUM, "123-456", html);
            APFtUtilityAndGlobal.m_replace(V_OTHERS, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", html);
            APFtUtilityAndGlobal.m_replace(V_PURPOSE, "ART-PROPS & SETTING EXPENSES", html);
            html
            .append("<table class=\"table-fixed table-font\">")
            .append("<tbody>")
            .append("<p class=\"pg-thin\"></p>")
            .append(columnHeader);
        }
        
        /* insert new item row */
        html.append(new StringBuilder(itemRow));
//        APFtUtilityAndGlobal.m_replace(V_ITEM_PO_NO, ""+row.getInteger("val_integer"), html);
//        APFtUtilityAndGlobal.m_replace(V_ITEM_PARTICULARS, row.getString("report_desc"), html);
//        APFtUtilityAndGlobal.m_replace(V_ITEM_EPI_NUM, "176-180", html);
//        APFtUtilityAndGlobal.m_replace(V_ITEM_AMOUNT, amountFormat.format(row.getBigDecimal("val_decimal")), html);
//        APFtUtilityAndGlobal.m_replace(V_ITEM_ACC_ALLOC, "THE EXORCIST'S METER", html);
//        
        //** if it is the last data row of page or report, finalize the page and insert into HTML
        if (cur_row == dataRowPerPage || row_cnt == report.size()) {
            cur_row = 1;
            page_num++;
            
            html
            .append(APFtUtilityAndGlobal.HTML_TBODY_END)//
            .append(APFtUtilityAndGlobal.HTML_TABLE_END);//

            //** insert page footer
            if (row_cnt == report.size()) {
                html
                .append(new StringBuilder(pageFooter));
                APFtUtilityAndGlobal.m_replace(V_ITEM_AMOUNT_TOTAL, totalAmountFormat.format(8929.10d), html); //
            } else {
                html.append(APFtUtilityAndGlobal.HTML_PAGE_BREAK);//
            }
        } else {
            cur_row++;
        }
       
        row_cnt++;
    }

    html.append("</page>");

    //String report_date = "Date: "+param.get("p_report_date", String.class).trim();
    
    resParam.put("report", html.toString());
    
	return resParam; //return the report
}


public ACFgResponseParameters genReportFormat2(List<? extends ACFaAppModel> report) throws Exception
{
	ACFgResponseParameters resParam = new ACFgResponseParameters();
	//** set numeric display format for report
    java.text.DecimalFormat amountFormat = new java.text.DecimalFormat("####0.00");
    java.text.DecimalFormat totalAmountFormat = new java.text.DecimalFormat("###,##0.00");
    java.text.DecimalFormat pageNumberFormat = new java.text.DecimalFormat("00");

    StringBuilder pageStyle = new StringBuilder(); 
    pageStyle
    .append("<style type=\"text/css\">")
    .append("body {")
    .append("  background: rgb(204,204,204); ")
    .append("}")
    .append("page {")
    .append("  background: white;")
    .append("  display: block;")
    .append("  margin: 0 auto;")
    .append("  margin-bottom: 0.5cm;")
    .append("  box-shadow: 0 0 0.5cm rgba(0,0,0,0.5);")
    .append("  word-wrap: no; }")
    .append("page[size=\"A4\"] {  ")
    .append("  width: 21cm;")
    .append("  height: 29.7cm; ")
    .append("}")
    .append("page[size=\"A4\"][layout=\"landscape\"] {")
    .append("  width: 29.7cm;")
    .append("  height: 21cm;  ")
    .append("}")
    .append("page[size=\"A3\"] {")
    .append("  width: 29.7cm;")
    .append("  height: 42cm;")
    .append("}")
    .append("page[size=\"A3\"][layout=\"landscape\"] {")
    .append("  width: 42cm;")
    .append("  height: 29.7cm;  ")
    .append("}")
    .append("page[size=\"A5\"] {")
    .append("  width: 14.8cm;")
    .append("  height: 21cm;")
    .append("}")
    .append("page[size=\"A5\"][layout=\"landscape\"] {")
    .append("  width: 21cm;")
    .append("  height: 14.8cm;  ")
    .append("}")
    .append("@media print {")
    .append("  body, page {")
    .append("    margin: 0;")
    .append("    box-shadow: 0;")
    .append("  }")
    .append("  html, body {height: 99%;}")
    .append("}"
    		+ "word-wrap: no;")
    .append("</style>");
    
    StringBuilder tableStyle = new StringBuilder();
    tableStyle
    .append("<style type=\"text/css\">")
    /* Column widths are based on these cells */
    .append(".col-po_no {")
    .append("  width: 22mm;")
    .append("}")
    .append(".col-particulars {")
    .append("  width: 60mm;")
    .append("}")
    .append(".col-epi_num {")
    .append("  width: 35mm;")
    .append("}")
    .append(".col-amount {")
    .append("  width: 25mm;")
    .append("}")
    .append(".col-remarks {")
    .append("  width: 55mm;")
    .append("}")
    .append(".section-col-1 {")
    .append("  width: 40mm;")
    .append("}")
    .append(".section-col-2 {")
    .append("  width: 45mm;")
    .append("}")
    .append(".section-col-3 {")
    .append("  width: 80mm;")
    .append("}")
    .append(".section-col-4 {")
    .append("  width: 40mm;")
    .append("}")
    .append(".col-invoice {")
    .append("  width: 1900mm; word-wrap: no;")
    .append("}")
    /* Overall table layout */
    .append("table.table-fixed {")
    .append("  table-layout: fixed;")
    .append("}")
    .append("table.table-fixed tr.section {")
    .append("  height: 9mm;")
    .append("}")
    .append("table.table-fixed tr.item {")
    .append("  height: 4mm;")
    .append("}")
    /* Overall table font */
    .append(".table-font {")
    .append("  font-family:\"courier new\",monospace;")
    .append("  font-size:0.9em; <font color='red'>/* Never set font sizes in pixels! */</font>")
    .append("  color:#00f;")
    .append("}")
    /* Empty line CSS */
    .append("p.pg-thin {  margin-top: 0.5em; margin-bottom: 0em; }")
    .append("</style>");
    
    /* 
     * Variable substitution format = {variable_name}. Must be a unique variable name for each piece of HTML code insert.
     */
    String V_PAGE_NUMBER = "{page_number}";
    String V_DEPARTMENT = "{department}";
    String V_DATE = "{date}";
    String V_REFERENCE_NO = "{reference_no}";
    String V_DDS_CODE = "{dds_code}";
    String V_PAYEE = "{payee}";;
    String V_PROGRAMME_NAME = "{programme_name}";
    String V_EPI_NUM = "{epi_num}";
    String V_OTHERS = "{others}";
    String V_PURPOSE = "{purpose}";
    String V_ITEM_PO_NO = "{item_po_no}";
    String V_ITEM_PARTICULARS = "{item_particulars}";
    String V_INVOIVE_NO = "{invoice_number}";
    String V_ITEM_EPI_NUM = "{item_epi_num}";
    String V_ITEM_AMOUNT = "{item_amount}";
    String V_ITEM_AMOUNT_TOTAL = "{item_amount_total}";
   
    
    /* Variable content table header */
    StringBuilder tableHeader = new StringBuilder();
    tableHeader
    .append("<p class=\"pg-thin\"></p>")
    .append("<table class=\"table-fixed table-font\">")
    .append("<tbody>")
        .append("<tr class=\"section\">")
        .append("<td class=\"section-col-1\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"section-col-2\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"section-col-3\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"section-col-4\" style=\"text-align: left;\">PAGE"+V_PAGE_NUMBER+"</td>")
        .append("</tr>")
        .append("<tr class=\"section\">")
        .append("<td class=\"section-col-1\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td>"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td>"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"section-col-4\" style=\"text-align: left;\">"+V_DATE+"</td>")
        .append("</tr>")
        .append("<tr class=\"section\">")
        .append("<td class=\"section-col-1\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td>"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td>"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"section-col-4\" style=\"text-align: left;\">"+V_REFERENCE_NO+"</td>")
        .append("</tr>")
        .append("<tr class=\"section\">")
        .append("<td class=\"section-col-1\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td style=\"text-align: left;\">"+V_DEPARTMENT+"</td>")
        .append("<td>"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"section-col-4\" style=\"text-align: left;\">"+"2201"+"</td>")
        .append("</tr>")
        .append("<tr class=\"section\">")
        .append("<td class=\"section-col-1\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td colspan=\"3\" style=\"word-break: break-all\">"+V_PAYEE+"</td>")
        .append("</tr>")
        .append("<tr class=\"section\">")
        .append("<td class=\"section-col-1\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td>"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td>"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td>"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("</tr>")
        .append("<tr class=\"section\">")
        .append("<td class=\"section-col-1\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td colspan=\"2\" style=\"text-align: left;\">"+""+"</td>")
        .append("<td class=\"section-col-4\" style=\"text-align: left;\">"+""+"</td>")
        .append("</tr>")
        .append("<tr class=\"section\">")
        .append("<td class=\"section-col-1\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td>"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td colspan=\"2\" style=\"text-align: left;\">"+""+"</td>")
        .append("</tr>")
        .append("<tr class=\"section\">")
        .append("<td class=\"section-col-1\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td colspan=\"3\" style=\"word-break: break-all\">"+V_PURPOSE+"</td>")
        .append("</tr>")
        .append("<tr class=\"section\">")
        .append("<td class=\"section-col-1\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td>"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td>"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td>"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("</tr>")
    .append("</tbody>")
    .append("</table>");
    
    /* Fixed content column header */
    StringBuilder columnHeader = new StringBuilder();
    columnHeader
    .append("<tr class=\"item\">")
    .append("<td class=\"col-po_no\" style=\"text-align: center;\">P.O.NO</td>")
    .append("<td class=\"col-particulars\" style=\"text-align: left;\">PARTICULARS</td>")
    .append("<td class=\"col-epi_num\" style=\"text-align: left;\">EPI#</td>")
    .append("<td class=\"col-amount\" style=\"text-align: right; padding-right: 5px;\">AMOUNT(HK$)</td>")
    .append("<td class=\"col-remarks\" style=\"text-align: left; padding-left: 10px;\">PROG DESCRIPTION</td>")
    .append("</tr>")
    .append("<tr class=\"item\">")
    .append("<td class=\"col-po_no\" style=\"text-align: center;\">------</td>")
    .append("<td class=\"col-particulars\" style=\"text-align: left;\">-----------</td>")
    .append("<td class=\"col-epi_num\" style=\"text-align: left;\">-----------</td>")
    .append("<td class=\"col-amount\" style=\"text-align: right; padding-right: 5px;\">-----------</td>")
    .append("<td class=\"col-remarks\" style=\"text-align: left; padding-left: 10px;\">-----------------</td>")
    .append("</tr>");
    
    /* Fixed content page footer */
    StringBuilder pageFooter = new StringBuilder();
    pageFooter
    .append("<table class=\"table-fixed table-font\">")
    .append("<tbody>")
        .append("<tr class=\"item\">")
        .append("<td class=\"col-po_no\" style=\"text-align: center;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"col-particulars\" style=\"text-align: left;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"col-epi_num\" style=\"text-align: left;\">"+""+"</td>")
        .append("<td class=\"col-amount\" style=\"text-align: right; padding-right: 5px;\">---------</td>")
        .append("<td class=\"col-remarks\" style=\"text-align: left; padding-left: 10px;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("</tr>")
        .append("<tr class=\"item\">")
        .append("<td class=\"col-po_no\" style=\"text-align: center;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"col-particulars\" style=\"text-align: left;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"col-epi_num\" style=\"text-align: left;\">"+""+"</td>")
        .append("<td class=\"col-amount\" style=\"text-align: right; padding-right: 5px;\">"+V_ITEM_AMOUNT_TOTAL+"</td>")
        .append("<td class=\"col-remarks\" style=\"text-align: left; padding-left: 10px;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("</tr>")
        .append("<tr class=\"item\">")
        .append("<td class=\"col-po_no\" style=\"text-align: center;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"col-particulars\" style=\"text-align: left;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"col-epi_num\" style=\"text-align: left;\">"+""+"</td>")
        .append("<td class=\"col-amount\" style=\"text-align: right; padding-right: 5px;\">---------</td>")
        .append("<td class=\"col-remarks\" style=\"text-align: left; padding-left: 10px;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("</tr>")
    .append("</tbody>")
    .append("</table>");
    
    
    StringBuilder pageFooternototal = new StringBuilder();
    pageFooternototal
    .append("<table class=\"table-fixed table-font\">")
    .append("<tbody>")
        .append("<tr class=\"item\">")
        .append("<td class=\"col-po_no\" style=\"text-align: center;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"col-particulars\" style=\"text-align: left;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"col-epi_num\" style=\"text-align: left;\">"+""+"</td>")
        .append("<td class=\"col-amount\" style=\"text-align: right; padding-right: 5px;\">---------</td>")
        .append("<td class=\"col-remarks\" style=\"text-align: left; padding-left: 10px;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("</tr>")
        .append("<tr class=\"item\">")
        .append("<td class=\"col-po_no\" style=\"text-align: center;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"col-particulars\" style=\"text-align: left;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"col-epi_num\" style=\"text-align: left;\">"+""+"</td>")
        
        .append("<td class=\"col-remarks\" style=\"text-align: left; padding-left: 10px;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("</tr>")
        .append("<tr class=\"item\">")
        .append("<td class=\"col-po_no\" style=\"text-align: center;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"col-particulars\" style=\"text-align: left;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("<td class=\"col-epi_num\" style=\"text-align: left;\">"+""+"</td>")
        .append("<td class=\"col-amount\" style=\"text-align: right; padding-right: 5px;\">---------</td>")
        .append("<td class=\"col-remarks\" style=\"text-align: left; padding-left: 10px;\">"+APFtUtilityAndGlobal.HTML_SPACE+"</td>")
        .append("</tr>")
    .append("</tbody>")
    .append("</table>");
    
    /* Variable content item row */
    StringBuilder itemRow = new StringBuilder();
    itemRow
    .append("<tr class=\"item\">")
    .append("<td class=\"col-po_no\" style=\"text-align: center;\">"+V_ITEM_PO_NO+"</td>")
    .append("<td class=\"col-particulars\" style=\"text-align: left;\">"+V_ITEM_PARTICULARS+"</td>")
    .append("<td class=\"col-epi_num\" style=\"text-align: left;\">"+V_ITEM_EPI_NUM+"</td>")
    .append("<td class=\"col-amount\" style=\"text-align: right; padding-right: 5px;\">"+V_ITEM_AMOUNT+"</td>")
    .append("<td class=\"col-remarks\" style=\"text-align: left; padding-left: 10px;\">"+V_PROGRAMME_NAME+"</td>")
    .append("</tr>")
    .append("<tr class=\"item\">") //add a blank row
    .append("<td >"+""+"</td>")
    .append("<td colspan=\"3\">"+"INVOICE NO:"+V_INVOIVE_NO+"</td>")
    //.append("<td class=\"col-epi_num\" style=\"text-align: left;\">"+""+"</td>")
    //.append("<td class=\"col-amount\" style=\"text-align: right; padding-right: 5px;\">"+""+"</td>")
    //.append("<td class=\"col-remarks\" style=\"text-align: left; padding-left: 10px;\">"+""+"</td>")
    .append("</tr>");
    
    //** construct final HTML result codes
    StringBuilder html = new StringBuilder(10000000); 
    html.append(pageStyle);
    html.append(tableStyle);
    html.append("<page size=\"A4\">");
    
    int dataRowPerPage = 10;
    int row_cnt = 1;
    
    int page_num = 1;
    
    //** loop through the payment request
    for (ACFaAppModel row : report) {
    	ACFdSQLAssSelect ass0 = new ACFdSQLAssSelect();
    	//SQL for po details
        ass0.setCustomSQL("select * from (select pr.supplier_code, s.supplier_name from arc_payment_request pr, arc_supplier s "
        		+ "where pr.supplier_code = s.supplier_code and pr.payment_form_no = '%s')",row.getString("payment_form_no") );
      
       List<ACFgRawModel> prSupplierName = ass0.executeQuery(getConnection("ARCDB"));
    	
    	
    	
    	
    	
    	ACFdSQLAssSelect ass = new ACFdSQLAssSelect();
    	//SQL for po details
        ass.setCustomSQL("select * from (select pd.payment_form_no, pd.invoice_no, pd.account_allocation, pd.purchase_order_no, pd.particulars, pd.from_episode_no, pd.to_episode_no, pd.payment_amount, pd.programme_no, "
        		+ "pm.programme_name from arc_payment_details pd, arc_programme_master pm where pm.programme_no = pd.programme_no and pd.payment_form_no = '%s')",row.getString("payment_form_no") );
       
//       ass.setKey("payment_form_no");
//       ass.wheres.and("payment_form_no", row.get(payment_form_no));
       System.out.println( "***************testing row get ****************"+row.getString("payment_form_no"));
       
       List<ACFgRawModel> purchases = ass.executeQuery(getConnection("ARCDB"));
       //can't get contents here!!
       System.out.println( "***************testing row list ****************"+purchases);
//       for (ACFaAppModel purchases_units : purchases) {
            html.append(new StringBuilder(tableHeader));
            APFtUtilityAndGlobal.m_replace(V_PAGE_NUMBER, pageNumberFormat.format(page_num), html);
            APFtUtilityAndGlobal.m_replace(V_DATE, ACFtUtility.timestampToString(row.getTimestamp("request_date"),"dd MMMM yyyy"), html);
            APFtUtilityAndGlobal.m_replace(V_REFERENCE_NO, row.getString("payment_form_no"), html);
            APFtUtilityAndGlobal.m_replace(V_DEPARTMENT, "ART ADMIN", html);
            APFtUtilityAndGlobal.m_replace(V_DDS_CODE, "A301/01", html);
            APFtUtilityAndGlobal.m_replace(V_PAYEE, prSupplierName.get(0).getString("supplier_name"), html);
//            APFtUtilityAndGlobal.m_replace(V_PROGRAMME_NAME, "MASTER WONG FEI HUNG", html);
//            APFtUtilityAndGlobal.m_replace(V_EPI_NUM, "123-456", html);
//            APFtUtilityAndGlobal.m_replace(V_OTHERS, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", html);
            APFtUtilityAndGlobal.m_replace(V_PURPOSE, row.getString("payment_purpose"), html);
            html
            .append("<table class=\"table-fixed table-font\">")
            .append("<tbody>")
            .append("<p class=\"pg-thin\"></p>")
            .append(columnHeader);
//        }
       double amountotal = 0;
       int cur_row = 0;
        //iterate through po_details
            for (ACFgRawModel purchases_units : purchases) {
           
             
           
        html
        .append(new StringBuilder(itemRow)); //null pointer below : modify purchase_order-no to purchase_order_no
        APFtUtilityAndGlobal.m_replace(V_ITEM_PO_NO, purchases_units.getString("purchase_order_no"), html);
        APFtUtilityAndGlobal.m_replace(V_ITEM_PARTICULARS, purchases_units.getString("particulars").substring(0, Math.min(purchases_units.getString("particulars").length(), 26)), html);
        APFtUtilityAndGlobal.m_replace(V_ITEM_EPI_NUM, purchases_units.getBigDecimal("from_episode_no").intValue()+"-"+purchases_units.getBigDecimal("to_episode_no").intValue(), html);
        APFtUtilityAndGlobal.m_replace(V_ITEM_AMOUNT,""+purchases_units.getBigDecimal("payment_amount").doubleValue(), html);
        APFtUtilityAndGlobal.m_replace(V_INVOIVE_NO,""+purchases_units.getString("invoice_no"), html);
        
        //null pointer below : no account_allocation in SQL
        APFtUtilityAndGlobal.m_replace(V_PROGRAMME_NAME, purchases_units.getString("programme_name").substring(0, Math.min(purchases_units.getString("programme_name").length(), 25)), html);
        System.out.println( "***************testing row ****************"+purchases_units.getString("purchase_order_no"));
        amountotal += purchases_units.getBigDecimal("payment_amount").doubleValue();
        cur_row++;
        //if the records reach to the print page limit
        if (cur_row % dataRowPerPage == 0)
        {
        	//set page footers for the previous page
        	  html
              .append(APFtUtilityAndGlobal.HTML_TBODY_END)//
              .append(APFtUtilityAndGlobal.HTML_TABLE_END)//
              //.append(new StringBuilder(pageFooternototal))
              .append(APFtUtilityAndGlobal.HTML_PAGE_BREAK)
              
             
              .append("</page>");
        	
        	//add page
        	page_num++;
        	//add new header
        	html.append(new StringBuilder(tableHeader));
            APFtUtilityAndGlobal.m_replace(V_PAGE_NUMBER, pageNumberFormat.format(page_num), html);
            APFtUtilityAndGlobal.m_replace(V_DATE, ACFtUtility.timestampToString(row.getTimestamp("request_date"),"dd MMMM yyyy"), html);
            APFtUtilityAndGlobal.m_replace(V_REFERENCE_NO, row.getString("payment_form_no"), html);
            APFtUtilityAndGlobal.m_replace(V_DEPARTMENT, "ART ADMIN", html);
            APFtUtilityAndGlobal.m_replace(V_DDS_CODE, "A301/01", html);
            APFtUtilityAndGlobal.m_replace(V_PAYEE, "MAN YUEN FLOWER SHOP", html);
//            APFtUtilityAndGlobal.m_replace(V_PROGRAMME_NAME, "MASTER WONG FEI HUNG", html);
//            APFtUtilityAndGlobal.m_replace(V_EPI_NUM, "123-456", html);
//            APFtUtilityAndGlobal.m_replace(V_OTHERS, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", html);
            APFtUtilityAndGlobal.m_replace(V_PURPOSE, "ART-PROPS & SETTING EXPENSES", html);
            html
            .append("<table class=\"table-fixed table-font\">")
            .append("<tbody>")
            .append("<p class=\"pg-thin\"></p>")
            .append(columnHeader);
        	
        }
            }
            System.out.println(amountotal);
       
      
       html
      .append(APFtUtilityAndGlobal.HTML_TBODY_END)//
      .append(APFtUtilityAndGlobal.HTML_TABLE_END)//
      .append(new StringBuilder(pageFooter))
      .append(APFtUtilityAndGlobal.HTML_PAGE_BREAK)
      
     
      .append("</page>");
       //seems the following line must set behind html.append()
       APFtUtilityAndGlobal.m_replace(V_ITEM_AMOUNT_TOTAL, totalAmountFormat.format(amountotal), html);
       
       page_num++;
//        row_cnt++;
    }

//    html.append("</page>");

    //String report_date = "Date: "+param.get("p_report_date", String.class).trim();
    
    resParam.put("report", html.toString());
    
        System.out.println( html.toString() );
    
    
	return resParam; //return the report
}


public List<ARCmPaymentRequest> filter( List<ARCmPaymentRequest> ItemInv)

{
	ArrayList<ARCmPaymentRequest> filtered = new ArrayList<ARCmPaymentRequest>();
	for (ARCmPaymentRequest elem: ItemInv)
	{
		if (elem.print_indicator.equals("1"))
		{filtered.add(elem);}
	}
	return filtered;
}
}