package arc.apf.General.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import acf.acf.Static.ACFtUtility;

public class ARCgPDFGenerator {

    public static enum PdfCell {
        VALUE, FONT, VERTICAL_ALIGNMENT, HORIZONTAL_ALIGNMENT, COL_SPAN, ROW_SPAN, BORDER, BORDER_STYLE, CELL_PADDING
    };

    public static enum PdfCellBorder {
        LEFT(PdfPCell.LEFT), RIGHT(PdfPCell.RIGHT), TOP(PdfPCell.TOP), BOTTOM(PdfPCell.BOTTOM), 
        BOX(PdfPCell.BOX), NO_BORDER(PdfPCell.NO_BORDER), 
        LEFT_TOP(PdfPCell.LEFT | PdfPCell.TOP), RIGHT_BOTTOM(PdfPCell.RIGHT | PdfPCell.BOTTOM), 
        LEFT_RIGHT(PdfPCell.LEFT | PdfPCell.RIGHT), 
        LEFT_TOP_RIGHT(PdfPCell.LEFT | PdfPCell.TOP | PdfPCell.RIGHT), LEFT_BOTTOM_RIGHT(PdfPCell.LEFT | PdfPCell.BOTTOM | PdfPCell.RIGHT);

        private final int border;

        PdfCellBorder(int border) {
            this.border = border;
        }

        public int getBorder() {
            return border;
        }
    };

    public static enum PdfCellBorderStyle {
        SOLID, DOTTED, DASHED
    };

    public static enum PdfCellBorderWidth {
        THIN(0.5f), NORMAL(1f), THICK(1.5f);

        private final float borderWidth;

        PdfCellBorderWidth(float borderWidth) {
            this.borderWidth = borderWidth;
        }

        public float getWidth() {
            return borderWidth;
        }
    };

    public static enum PdfPageSize {
        A4_PORTRAIT(PageSize.A4), A4_LANDSCAPE(PageSize.A4.rotate());

        private final Rectangle pageSize;

        PdfPageSize(Rectangle pageSize) {
            this.pageSize = pageSize;
        }

        public Rectangle getPageSize() {
            return pageSize;
        }
    };

    public static enum PdfFontFamily {
        /* iText built-in Font Family */ 
        COURIER, HELVETICA, TIMES_ROMAN, SYMBOL, ZAPFDINGBATS,
        /* Embedded True Type Fonts collection inside mingliu.jar->mingliu.ttc */ 
        MING_LIU, PMING_LIU, MINGLIU_HKSCS
    }
    
    public static class ARCgPdfCell {
        private String value = "";
        private Image image;
        private Chunk chunk;
        private Font font;
        private int verticalAlignment;
        private int horizontalAlignment;
        private int columnSpan = 0;
        private int rowSpan = 0;
        private float cellHeight = 0f;
        private PdfCellBorder border;
        private PdfCellBorderStyle borderStyle;
        private PdfCellBorderWidth borderWidth;
        private Rectangle cellPadding;
        private Object cellType;
        private boolean fitToCell;

        public ARCgPdfCell() {
            super();
        }

        /**
         * @return the fitToCell
         */
        public boolean isFitToCell() {
            return fitToCell;
        }

        /**
         * @param fitToCell the fitToCell to set
         */
        public void setFitToCell(boolean fitToCell) {
            this.fitToCell = fitToCell;
        }

        /**
         * @return the cellHeight
         */
        public float getCellHeight() {
            return cellHeight;
        }

        /**
         * @param cellHeight the cellHeight to set
         */
        public void setCellHeight(float cellHeight) {
            this.cellHeight = cellHeight;
        }

        public Object getCellType() {
            return cellType;
        }

        public void setCellType(Object cellType) {
            this.cellType = cellType;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public Chunk getChunk() {
            return chunk;
        }

        public void setChunk(Chunk chunk) {
            this.chunk = chunk;
        }

        public Font getFont() {
            return font;
        }

        public void setFont(Font font) {
            this.font = font;
        }

        public int getVerticalAlignment() {
            return verticalAlignment;
        }

        public void setVerticalAlignment(int verticalAlignment) {
            this.verticalAlignment = verticalAlignment;
        }

        public int getHorizontalAlignment() {
            return horizontalAlignment;
        }

        public void setHorizontalAlignment(int horizontalAlignment) {
            this.horizontalAlignment = horizontalAlignment;
        }

        public int getColumnSpan() {
            return columnSpan;
        }

        public void setColumnSpan(int columnSpan) {
            this.columnSpan = columnSpan;
        }

        public int getRowSpan() {
            return rowSpan;
        }

        public void setRowSpan(int rowSpan) {
            this.rowSpan = rowSpan;
        }

        public PdfCellBorder getBorder() {
            return border;
        }

        public void setBorder(PdfCellBorder border) {
            this.border = border;
        }

        public PdfCellBorderStyle getBorderStyle() {
            return borderStyle;
        }

        public void setBorderStyle(PdfCellBorderStyle borderStyle) {
            this.borderStyle = borderStyle;
        }

        public PdfCellBorderWidth getBorderWidth() {
            return borderWidth;
        }

        public void setBorderWidth(PdfCellBorderWidth borderWidth) {
            this.borderWidth = borderWidth;
        }

        public Rectangle getCellPadding() {
            return cellPadding;
        }

        public void setCellPadding(Rectangle cellPadding) {
            this.cellPadding = cellPadding;
        }

    }

    public static class ARCgPdfTable {
        protected enum WidthStyle {ABSOLUTE, F_RELATIVE, I_RELATIVE};
        private List<List<ARCgPdfCell>> tableContent;
        private float[] absoluteWidths;
        private float[] f_relativeWidths;
        private int[] i_relativeWidths;
        private float widthPercentage = 0f;
        private int columns = 1;
        private WidthStyle widthStyle = WidthStyle.ABSOLUTE;
        
        public ARCgPdfTable() {
            super();
        }

        /**
         * @return the tableContent
         */
        public List<List<ARCgPdfCell>> getTableContent() {
            return tableContent;
        }

        /**
         * @param tableContent the tableContent to set
         */
        public void setTableContent(List<List<ARCgPdfCell>> tableContent) {
            this.tableContent = tableContent;
        }

        /**
         * @return the absoluteWidths
         */
        public float[] getAbsoluteWidths() {
            return absoluteWidths;
        }

        /**
         * @param absoluteWidths the absoluteWidths to set
         */
        public void setAbsoluteWidths(float[] absoluteWidths) {
            this.absoluteWidths = absoluteWidths;
            setWidthStyle(WidthStyle.ABSOLUTE);
        }

        /**
         * @return the relativeWidths
         */
        public float[] getRelativeWidths() {
            return f_relativeWidths;
        }

        /**
         * @return the relativeWidths
         */
        public int[] getRelativeWidthsByFactor() {
            return i_relativeWidths;
        }

        /**
         * @param relativeWidths the relativeWidths to set
         */
        public void setRelativeWidths(float[] relativeWidths) {
            this.f_relativeWidths = relativeWidths;
            setWidthStyle(WidthStyle.F_RELATIVE);
        }

        /**
         * @param relativeWidths the relativeWidths to set
         */
        public void setRelativeWidths(int[] relativeWidths) {
            this.i_relativeWidths = relativeWidths;
            setWidthStyle(WidthStyle.I_RELATIVE);
        }

        /**
         * @return the columns
         */
        public int getColumns() {
            return columns;
        }

        /**
         * @param columns the columns to set
         */
        public void setColumns(int columns) {
            this.columns = columns;
        }

        /**
         * @return the widthPercentage
         */
        public float getWidthPercentage() {
            return widthPercentage;
        }

        /**
         * @param widthPercentage the widthPercentage to set
         */
        public void setWidthPercentage(float widthPercentage) {
            this.widthPercentage = widthPercentage;
        }

        /**
         * @return the widthStyle
         */
        public WidthStyle getWidthStyle() {
            return widthStyle;
        }

        /**
         * @param widthStyle the widthStyle to set
         */
        private void setWidthStyle(WidthStyle widthStyle) {
            this.widthStyle = widthStyle;
        }

    }
    
    protected class ARCgPdfNewPage {
        private ARCgPdfTable header;
        private ARCgPdfTable footer;
        private final String action = Chunk.NEWPAGE;
        
        /**
         * @return the header
         */
        public ARCgPdfTable getHeader() {
            return header;
        }
        /**
         * @param header the header to set
         * @throws DocumentException 
         */
        public void setHeader(ARCgPdfTable p_table) {
            this.header = p_table;
        }
        /**
         * @return the footer
         */
        public ARCgPdfTable getFooter() {
            return footer;
        }
        /**
         * @param footer the footer to set
         * @throws DocumentException 
         */
        public void setFooter(ARCgPdfTable p_table) {
            this.footer = p_table;
        }
        /**
         * @return the action
         */
        public String getAction() {
            return action;
        }
        
    }
    
    protected class HeaderTable extends PdfPageEventHelper {
        private PdfPTable table;
        private float tableHeight;
        
        public HeaderTable(ARCgPdfTable p_table) throws DocumentException {
/*            
            for (int i = 0; i < content.size(); i++) {
                List<ARCgPdfCell> cols = content.get(i);
                
                if (cols.size() > max_cols) {
                    max_cols = cols.size();
                }
            }
*/
            buildTable(p_table);
        }
 
        public void setHeader(ARCgPdfTable p_table) throws DocumentException {
/*            
            for (int i = 0; i < content.size(); i++) {
                List<ARCgPdfCell> cols = content.get(i);
                
                if (cols.size() > max_cols) {
                    max_cols = cols.size();
                }
            }
*/
            buildTable(p_table);
        }
        
        private void buildTable(ARCgPdfTable p_table) throws DocumentException {
            List<List<ARCgPdfCell>> content = p_table.getTableContent();
            
            int max_cols = p_table.getColumns();

            table = new PdfPTable(max_cols);
            switch (p_table.getWidthStyle()) {
            case ABSOLUTE:
                float[] absoluteWidths = p_table.getAbsoluteWidths();
                if (absoluteWidths != null && max_cols == absoluteWidths.length) {
                    table.setTotalWidth(absoluteWidths);
                } else {
                    table.setTotalWidth(pagesize.getWidth());
                }
                break;
            case F_RELATIVE:
                float[] relativeWidths = p_table.getRelativeWidths();
                if (relativeWidths != null && max_cols == relativeWidths.length) {
                    table.setTotalWidth(relativeWidths);
                } else {
                    table.setTotalWidth(pagesize.getWidth());
                }
                break;
            case I_RELATIVE:
                int[] widths = p_table.getRelativeWidthsByFactor();
                if (widths != null && max_cols == widths.length) {
                    table.setWidths(widths);
                } else {
                    table.setTotalWidth(pagesize.getWidth());
                }
                break;
            default:
                table.setTotalWidth(pagesize.getWidth());
                break;
            }
            table.setSpacingBefore(0f);
            table.setSpacingAfter(0f);
            
            if (p_table.getWidthPercentage() > 0) {
                table.setWidthPercentage(p_table.getWidthPercentage());
            } else {
                table.setLockedWidth(true);
            }

            // ** insert table cells
            for (int i = 0; i < content.size(); i++) {
                List<ARCgPdfCell> cols = content.get(i);
                
                for (int j = 0; j < cols.size(); j++) {
                    if (cols.get(j).getCellType() instanceof String) {
                        insertCell(table, (String) cols.get(j).getValue(), (Integer) cols.get(j).getHorizontalAlignment(), (Integer) cols.get(j).getVerticalAlignment(), (Integer) cols.get(j).getColumnSpan(), (Integer) cols.get(j).getRowSpan(), (PdfCellBorder) cols.get(j).getBorder(),
                                (PdfCellBorderStyle) cols.get(j).getBorderStyle(), (PdfCellBorderWidth) cols.get(j).getBorderWidth(), (Rectangle) cols.get(j).getCellPadding(), (Font) cols.get(j).getFont());
                    } else if (cols.get(j).getCellType() instanceof Image) {
                        insertCell(table, (Image) cols.get(j).getImage(), (Integer) cols.get(j).getHorizontalAlignment(), (Integer) cols.get(j).getVerticalAlignment(), (Integer) cols.get(j).getColumnSpan(), (Integer) cols.get(j).getRowSpan(), (PdfCellBorder) cols.get(j).getBorder(),
                                (PdfCellBorderStyle) cols.get(j).getBorderStyle(), (PdfCellBorderWidth) cols.get(j).getBorderWidth(), (Rectangle) cols.get(j).getCellPadding(), (Font) cols.get(j).getFont(), (Boolean) cols.get(j).isFitToCell());
                    }
                }
            }

            tableHeight = table.calculateHeights();
        }
 
        public float getTableHeight() {
            return tableHeight;
        }
 
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            table.writeSelectedRows(0, -1, document.left(), document.top() + ((document.topMargin() + tableHeight) / 2), writer.getDirectContent());
        }
    }
    
    protected class FooterTable extends PdfPageEventHelper {
        protected PdfPTable table;
        
        public FooterTable(ARCgPdfTable p_table) throws DocumentException {
/*            
            for (int i = 0; i < content.size(); i++) {
                List<ARCgPdfCell> cols = content.get(i);
                
                if (cols.size() > max_cols) {
                    max_cols = cols.size();
                }
            }
*/
            buildTable(p_table);
        }
        
        public void setFooter(ARCgPdfTable p_table) throws DocumentException {
/*            
            for (int i = 0; i < content.size(); i++) {
                List<ARCgPdfCell> cols = content.get(i);
                
                if (cols.size() > max_cols) {
                    max_cols = cols.size();
                }
            }
*/
            buildTable(p_table);
        }
        
        private void buildTable(ARCgPdfTable p_table) throws DocumentException {
            List<List<ARCgPdfCell>> content = p_table.getTableContent();
            
            int max_cols = p_table.getColumns();

            table = new PdfPTable(max_cols);
            switch (p_table.getWidthStyle()) {
            case ABSOLUTE:
                float[] absoluteWidths = p_table.getAbsoluteWidths();
                if (absoluteWidths != null && max_cols == absoluteWidths.length) {
                    table.setTotalWidth(absoluteWidths);
                } else {
                    table.setTotalWidth(pagesize.getWidth());
                }
                break;
            case F_RELATIVE:
                float[] relativeWidths = p_table.getRelativeWidths();
                if (relativeWidths != null && max_cols == relativeWidths.length) {
                    table.setTotalWidth(relativeWidths);
                } else {
                    table.setTotalWidth(pagesize.getWidth());
                }
                break;
            case I_RELATIVE:
                int[] widths = p_table.getRelativeWidthsByFactor();
                if (widths != null && max_cols == widths.length) {
                    table.setWidths(widths);
                } else {
                    table.setTotalWidth(pagesize.getWidth());
                }
                break;
            default:
                table.setTotalWidth(pagesize.getWidth());
                break;
            }
            table.setSpacingBefore(0f);
            table.setSpacingAfter(0f);
            
            if (p_table.getWidthPercentage() > 0) {
                table.setWidthPercentage(p_table.getWidthPercentage());
            } else {
                table.setLockedWidth(true);
            }

            // ** insert table cells
            for (int i = 0; i < content.size(); i++) {
                List<ARCgPdfCell> cols = content.get(i);
                
                for (int j = 0; j < cols.size(); j++) {
                    if (cols.get(j).getCellType() instanceof String) {
                        insertCell(table, (String) cols.get(j).getValue(), (Integer) cols.get(j).getHorizontalAlignment(), (Integer) cols.get(j).getVerticalAlignment(), (Integer) cols.get(j).getColumnSpan(), (Integer) cols.get(j).getRowSpan(), (PdfCellBorder) cols.get(j).getBorder(),
                                (PdfCellBorderStyle) cols.get(j).getBorderStyle(), (PdfCellBorderWidth) cols.get(j).getBorderWidth(), (Rectangle) cols.get(j).getCellPadding(), (Font) cols.get(j).getFont());
                    } else if (cols.get(j).getCellType() instanceof Image) {
                        insertCell(table, (Image) cols.get(j).getImage(), (Integer) cols.get(j).getHorizontalAlignment(), (Integer) cols.get(j).getVerticalAlignment(), (Integer) cols.get(j).getColumnSpan(), (Integer) cols.get(j).getRowSpan(), (PdfCellBorder) cols.get(j).getBorder(),
                                (PdfCellBorderStyle) cols.get(j).getBorderStyle(), (PdfCellBorderWidth) cols.get(j).getBorderWidth(), (Rectangle) cols.get(j).getCellPadding(), (Font) cols.get(j).getFont(), (Boolean) cols.get(j).isFitToCell());
                    }
                }
            }

        }

        public void onEndPage(PdfWriter writer, Document document) {
            table.writeSelectedRows(0, -1, 36, 64, writer.getDirectContent());
        }
    }
    
    /**
     * 
     * @see <b><font color=darkgreen>Table and cell events to draw borders</font></b><br/> 
     * <a href='http://developers.itextpdf.com/examples/tables/table-and-cell-events-draw-borders'>http://developers.itextpdf.com/examples/tables/table-and-cell-events-draw-borders</a>
     *
     */
    protected abstract class CustomBorder implements PdfPCellEvent {
        private int border = 0;
        private float borderwidth = 0.5f;

        public CustomBorder(int border) {
            this.border = border;
        }

        public CustomBorder(int border, float borderwidth) {
            this.border = border;
            this.borderwidth = borderwidth;
        }

        public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
            PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
            canvas.saveState();
            canvas.setLineWidth(borderwidth);
            setLineDash(canvas);
            if ((border & PdfPCell.TOP) == PdfPCell.TOP) {
                canvas.moveTo(position.getRight(), position.getTop());
                canvas.lineTo(position.getLeft(), position.getTop());
            }
            if ((border & PdfPCell.BOTTOM) == PdfPCell.BOTTOM) {
                canvas.moveTo(position.getRight(), position.getBottom());
                canvas.lineTo(position.getLeft(), position.getBottom());
            }
            if ((border & PdfPCell.RIGHT) == PdfPCell.RIGHT) {
                canvas.moveTo(position.getRight(), position.getTop());
                canvas.lineTo(position.getRight(), position.getBottom());
            }
            if ((border & PdfPCell.LEFT) == PdfPCell.LEFT) {
                canvas.moveTo(position.getLeft(), position.getTop());
                canvas.lineTo(position.getLeft(), position.getBottom());
            }
            canvas.stroke();
            canvas.restoreState();
        }

        public abstract void setLineDash(PdfContentByte canvas);
    }

    protected class SolidBorder extends CustomBorder {
        public SolidBorder(int border) {
            super(border);
        }

        public SolidBorder(int border, float borderwidth) {
            super(border, borderwidth);
        }

        public void setLineDash(PdfContentByte canvas) {
        }
    }

    protected class DottedBorder extends CustomBorder {
        public DottedBorder(int border) {
            super(border);
        }

        public DottedBorder(int border, float borderwidth) {
            super(border, borderwidth);
        }

        public void setLineDash(PdfContentByte canvas) {
            canvas.setLineCap(PdfContentByte.LINE_CAP_ROUND);
            canvas.setLineDash(0, 4, 2);
        }
    }

    protected class DashedBorder extends CustomBorder {
        public DashedBorder(int border) {
            super(border);
        }

        public DashedBorder(int border, float borderwidth) {
            super(border, borderwidth);
        }

        public void setLineDash(PdfContentByte canvas) {
            canvas.setLineDash(3, 3);
        }
    }

    /**
     * 
     * @see <b><font color=darkgreen>Inner class to add a watermark to every page.</font></b><br/> 
     * <a href='http://developers.itextpdf.com/examples/page-events-itext5/page-events-watermarking'>http://developers.itextpdf.com/examples/page-events-itext5/page-events-watermarking</a>
     *
     */
    protected class Watermark extends PdfPageEventHelper {
        /**
         *  <b><font color=blue>GrayColor(0.1f .. 1f) : larger value lighter color</font></b>
         */
        Font FONT = new Font(FontFamily.HELVETICA, 42, Font.BOLD, new GrayColor(0.9f));
 
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            //** .showTextAligned(com.itextpdf.text.pdf.PdfContentByte canvas, int alignment, com.itextpdf.text.Phrase phrase, 
            //**                  float x, float y, float rotation)
            //** where (x,y) is center point of page
            ColumnText.showTextAligned(writer.getDirectContentUnder(), Element.ALIGN_CENTER, new Phrase(getWatermark(), FONT), 
                                       pagesize.getWidth()/2, pagesize.getHeight()/2, writer.getPageNumber() % 2 == 1 ? 45 : -45);
        }

    }

    //### instance variables ###
    private String outputFilePath;
    private FileOutputStream outputStream;

    private PdfWriter writer;
    private Document document;
    private Rectangle pagesize;
    private String watermark = "TELEVISION BROADCASTS LIMITED";
    private List<Object> pageList;
    private HeaderTable header;
    private FooterTable footer;

    public ARCgPDFGenerator(String outputFilePath, PdfPageSize pageSize) throws FileNotFoundException, DocumentException {
        outputStream = new FileOutputStream(outputFilePath);
        this.pagesize = pageSize.getPageSize();
        this.outputFilePath = outputFilePath;

        pageList = new ArrayList<Object>();
    }

    public Document getDocument() {
        return document;
    }

    public String getWatermark() {
        return watermark;
    }

    public void setWatermark(String watermark) {
        this.watermark = watermark;
    }

    private float getPageHeaderHeight() {
        if (header != null) {
            return header.getTableHeight();
        }
        
        return 0f;
    }
    
    /**
     * @param header the header to set
     * @throws DocumentException 
     */
    public void setPageHeader(ARCgPdfTable p_table) throws DocumentException {
        header = new HeaderTable(p_table);
    }

    /**
     * @param footer the footer to set
     * @throws DocumentException 
     */
    public void setPageFooter(ARCgPdfTable p_table) throws DocumentException {
        footer = new FooterTable(p_table);
    }

    public ARCgPdfCell createPdfCell(String value, PdfCellBorder border, PdfCellBorderStyle borderStyle, PdfCellBorderWidth borderWidth, Rectangle cellPadding, int colSpan, int rowSpan, Font font, int horizontalAlignment, int verticalAlignment) {
        ARCgPdfCell tc = new ARCgPdfCell();
        tc.setValue(value);
        tc.setBorder(border);
        tc.setBorderStyle(borderStyle);
        tc.setBorderWidth(borderWidth);
        tc.setCellPadding(cellPadding);
        tc.setColumnSpan(colSpan);
        tc.setRowSpan(rowSpan);
        tc.setFont(font);
        tc.setHorizontalAlignment(horizontalAlignment);
        tc.setVerticalAlignment(verticalAlignment);
        tc.setCellType(value);

        return tc;
    }

    public ARCgPdfCell createPdfCell(Image image, PdfCellBorder border, PdfCellBorderStyle borderStyle, PdfCellBorderWidth borderWidth, Rectangle cellPadding, int colSpan, int rowSpan, Font font, int horizontalAlignment, int verticalAlignment, boolean fitToCell) {
        ARCgPdfCell tc = new ARCgPdfCell();
        tc.setImage(image);
        tc.setBorder(border);
        tc.setBorderStyle(borderStyle);
        tc.setBorderWidth(borderWidth);
        tc.setCellPadding(cellPadding);
        tc.setColumnSpan(colSpan);
        tc.setRowSpan(rowSpan);
        tc.setFont(font);
        tc.setHorizontalAlignment(horizontalAlignment);
        tc.setVerticalAlignment(verticalAlignment);
        tc.setCellType(image);
        tc.setFitToCell(fitToCell);

        return tc;
    }

    /**
     * @param p_header null = no header
     * @param p_footer null = no footer
     * @return {@link arc.apf.General.report.ARCgPDFGenerator.ARCgPdfNewPage}
     * @throws DocumentException
     */
    public ARCgPdfNewPage createPdfNewPage(ARCgPdfTable p_header, ARCgPdfTable p_footer) throws DocumentException {
        ARCgPdfNewPage newpage = new ARCgPdfNewPage();
        
        if (p_header != null) {
            newpage.setHeader(p_header);
        }
        
        if (p_footer != null) {
            newpage.setFooter(p_footer);
        }

        return newpage;
    }

    public Image createImage(String path) throws DocumentException, IOException {
        Image img = Image.getInstance(getClass().getClassLoader().getResource(path));
        return img;
    }
    
    public Image createImage(String path, float width, float height) throws DocumentException, IOException {
        Image img = Image.getInstance(ACFtUtility.getJavaResourceInString(path));
        img.setScaleToFitHeight(false);
        img.scaleAbsolute(width, height);
        return img;
    }
    
    public Image createScaledImage(String path, float width, float height) throws DocumentException, IOException {
        Image img = Image.getInstance(ACFtUtility.getJavaResourceInString(path));
        img.scaleToFit(width, height);
        return img;
    }
    
    public void insertPageContent(List<ARCgPdfTable> content) throws DocumentException {
        if (content == null || (content != null && content.size() <= 0)) {
            throw new DocumentException("Page content not found !");
        }
        
        //** remove empty list
        for (int i = content.size()-1; i >= 0; i--) {
            Object row = content.get(i);

            if (row == null) {
                content.remove(i);
            }
        }

        for (int i = 0; i < content.size(); i++) {
            Object row = content.get(i);
            
            pageList.add(createTable((ARCgPdfTable)row));
        }

    }

    public void insertPageContent(ARCgPdfNewPage p_newpage) throws DocumentException {
        pageList.add(p_newpage);
        
    }
    
    private PdfPTable createTable(ARCgPdfTable p_table) throws DocumentException {
/*        
        for (int i = 0; i < content.size(); i++) {
            List<ARCgPdfCell> cols = content.get(i);
            
            if (cols.size() > max_cols) {
                max_cols = cols.size();
            }
        }
*/
        List<List<ARCgPdfCell>> content = p_table.getTableContent();
        
        int max_cols = p_table.getColumns();

        PdfPTable table = new PdfPTable(max_cols);
        switch (p_table.getWidthStyle()) {
        case ABSOLUTE:
            float[] absoluteWidths = p_table.getAbsoluteWidths();
            if (absoluteWidths != null && max_cols == absoluteWidths.length) {
                table.setTotalWidth(absoluteWidths);
            } else {
                table.setTotalWidth(pagesize.getWidth());
            }
            break;
        case F_RELATIVE:
            float[] relativeWidths = p_table.getRelativeWidths();
            if (relativeWidths != null && max_cols == relativeWidths.length) {
                table.setTotalWidth(relativeWidths);
            } else {
                table.setTotalWidth(pagesize.getWidth());
            }
            break;
        case I_RELATIVE:
            int[] widths = p_table.getRelativeWidthsByFactor();
            if (widths != null && max_cols == widths.length) {
                table.setWidths(widths);
            } else {
                table.setTotalWidth(pagesize.getWidth());
            }
            break;
        default:
            table.setTotalWidth(pagesize.getWidth());
            break;
        }
        table.setSpacingBefore(0f);
        table.setSpacingAfter(0f);
        
        if (p_table.getWidthPercentage() > 0) {
            table.setWidthPercentage(p_table.getWidthPercentage());
        } else {
            table.setLockedWidth(true);
        }

        // ** insert table cells
        for (int i = 0; i < content.size(); i++) {
            List<ARCgPdfCell> cols = content.get(i);
            
            for (int j = 0; j < cols.size(); j++) {
                if (cols.get(j).getCellType() instanceof String) {
                    insertCell(table, (String) cols.get(j).getValue(), (Integer) cols.get(j).getHorizontalAlignment(), (Integer) cols.get(j).getVerticalAlignment(), (Integer) cols.get(j).getColumnSpan(), (Integer) cols.get(j).getRowSpan(), (PdfCellBorder) cols.get(j).getBorder(),
                            (PdfCellBorderStyle) cols.get(j).getBorderStyle(), (PdfCellBorderWidth) cols.get(j).getBorderWidth(), (Rectangle) cols.get(j).getCellPadding(), (Font) cols.get(j).getFont());
                } else if (cols.get(j).getCellType() instanceof Image) {
                    insertCell(table, (Image) cols.get(j).getImage(), (Integer) cols.get(j).getHorizontalAlignment(), (Integer) cols.get(j).getVerticalAlignment(), (Integer) cols.get(j).getColumnSpan(), (Integer) cols.get(j).getRowSpan(), (PdfCellBorder) cols.get(j).getBorder(),
                            (PdfCellBorderStyle) cols.get(j).getBorderStyle(), (PdfCellBorderWidth) cols.get(j).getBorderWidth(), (Rectangle) cols.get(j).getCellPadding(), (Font) cols.get(j).getFont(), (Boolean) cols.get(j).isFitToCell());
                }
            }
        }
        
        return table;
    }
    
    private void insertCell(PdfPTable table, String text, int horizontalAlign, int verticalAlign, int colspan, int rowspan, PdfCellBorder border, PdfCellBorderStyle borderStyle, PdfCellBorderWidth borderWidth, Rectangle cellpadding, Font font) {

        // create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(5, text.trim(), font));
        // set the cell alignment
        cell.setHorizontalAlignment(horizontalAlign);
        cell.setVerticalAlignment(verticalAlign);
        // set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        // set the cell ROW span in case you want to merge two or more cells
        cell.setRowspan(rowspan);
        // set border style
        PdfPCellEvent cellEvent;
        switch (borderStyle) {
        case SOLID:
            cellEvent = new SolidBorder(border.getBorder(), borderWidth.getWidth());
            break;
        case DASHED:
            cellEvent = new DashedBorder(border.getBorder(), borderWidth.getWidth());
            break;
        case DOTTED:
            cellEvent = new DottedBorder(border.getBorder(), borderWidth.getWidth());
            break;
        default:
            cellEvent = new SolidBorder(PdfPCell.BOX, borderWidth.getWidth());
            break;
        }
        // set the cell border
        switch (border.getBorder()) {
        case PdfPCell.NO_BORDER:
            cell.setBorder(border.getBorder());
            break;
        default:
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(cellEvent);
            break;
        }
        // set the cell padding
        cell.setPaddingLeft(cellpadding.getLeft());
        cell.setPaddingRight(cellpadding.getRight());
        cell.setPaddingTop(cellpadding.getTop());
        cell.setPaddingBottom(cellpadding.getBottom());
        // set wrap or no wrap
        cell.setNoWrap(true);
        // in case there is no text and you wan to create an empty row
        if (text.trim().equalsIgnoreCase("")) {
            cell.setMinimumHeight(10f);
        }
        // add the call to the table
        table.addCell(cell);

    }

    private void insertCell(PdfPTable table, Image image, int horizontalAlign, int verticalAlign, int colspan, int rowspan, PdfCellBorder border, PdfCellBorderStyle borderStyle, PdfCellBorderWidth borderWidth, Rectangle cellpadding, Font font, boolean bFitToCell) {

        // create a new cell with the specified image
        PdfPCell cell = new PdfPCell(image, bFitToCell);
        // set the cell alignment
        cell.setHorizontalAlignment(horizontalAlign);
        cell.setVerticalAlignment(verticalAlign);
        // set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        // set the cell ROW span in case you want to merge two or more cells
        cell.setRowspan(rowspan);
        // set border style
        PdfPCellEvent cellEvent;
        switch (borderStyle) {
        case SOLID:
            cellEvent = new SolidBorder(border.getBorder(), borderWidth.getWidth());
            break;
        case DASHED:
            cellEvent = new DashedBorder(border.getBorder(), borderWidth.getWidth());
            break;
        case DOTTED:
            cellEvent = new DottedBorder(border.getBorder(), borderWidth.getWidth());
            break;
        default:
            cellEvent = new SolidBorder(PdfPCell.BOX, borderWidth.getWidth());
            break;
        }
        // set the cell border
        switch (border.getBorder()) {
        case PdfPCell.NO_BORDER:
            cell.setBorder(border.getBorder());
            break;
        default:
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(cellEvent);
            break;
        }
        // set the cell padding
        cell.setPaddingLeft(cellpadding.getLeft());
        cell.setPaddingRight(cellpadding.getRight());
        cell.setPaddingTop(cellpadding.getTop());
        cell.setPaddingBottom(cellpadding.getBottom());
        // set wrap or no wrap
        cell.setNoWrap(true);
        // add the call to the table
        table.addCell(cell);

    }
    
    public static Font createFont(PdfFontFamily family, float size, int style, BaseColor color) throws DocumentException, IOException {
        Font font = new Font(FontFamily.COURIER, 10, Font.NORMAL);
        BaseFont basefont;

        switch (family) {
        case COURIER: 
            font = new Font(FontFamily.COURIER, size, style, color);
            break;
        case HELVETICA: 
            font = new Font(FontFamily.HELVETICA, size, style, color);
            break;
        case TIMES_ROMAN: 
            font = new Font(FontFamily.TIMES_ROMAN, size, style, color);
            break;
        case SYMBOL:
            font = new Font(FontFamily.SYMBOL, size, style, color);
            break;
        case ZAPFDINGBATS:
            font = new Font(FontFamily.ZAPFDINGBATS, size, style, color);
            break;
        case MING_LIU: 
            basefont = BaseFont.createFont("mingliu.ttc,0", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            font = new Font(basefont, size, style, color);
            break;
        case PMING_LIU: 
            basefont = BaseFont.createFont("mingliu.ttc,1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            font = new Font(basefont, size, style, color);
            break;
        case MINGLIU_HKSCS:
            basefont = BaseFont.createFont("mingliu.ttc,2", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            font = new Font(basefont, size, style, color);
            break;
        default:
            break;
        }
        
        return font;
    }
    
    public static Font createFont(PdfFontFamily family, float size, int style) throws DocumentException, IOException {
        Font font = new Font(FontFamily.COURIER, 10, Font.NORMAL);
        BaseFont basefont;

        switch (family) {
        case COURIER: 
            font = new Font(FontFamily.COURIER, size, style);
            break;
        case HELVETICA: 
            font = new Font(FontFamily.HELVETICA, size, style);
            break;
        case TIMES_ROMAN: 
            font = new Font(FontFamily.TIMES_ROMAN, size, style);
            break;
        case SYMBOL:
            font = new Font(FontFamily.SYMBOL, size, style);
            break;
        case ZAPFDINGBATS:
            font = new Font(FontFamily.ZAPFDINGBATS, size, style);
            break;
        case MING_LIU: 
            basefont = BaseFont.createFont("mingliu.ttc,0", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            font = new Font(basefont, size, style);
            break;
        case PMING_LIU: 
            basefont = BaseFont.createFont("mingliu.ttc,1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            font = new Font(basefont, size, style);
            break;
        case MINGLIU_HKSCS:
            basefont = BaseFont.createFont("mingliu.ttc,2", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            font = new Font(basefont, size, style);
            break;
        default:
            break;
        }
        
        return font;
    }
    
    /**
     * <b>Generate PDF that scale to fit</b>
     * @throws IOException
     * @throws DocumentException
     */
    public void generatePDF() throws IOException, DocumentException {
        document = new Document(pagesize,0,0,getPageHeaderHeight(),0);
        
        writer = PdfWriter.getInstance(document, outputStream);
        
        if (getWatermark() != null && !getWatermark().trim().equals("")) {
            writer.setPageEvent(new Watermark());
        }
        if (header != null) {
            writer.setPageEvent(header);
        }
        if (footer != null) {
            writer.setPageEvent(footer);
        }
        
        document.open();
        document.setMargins(0, 0, getPageHeaderHeight(), 0);

        if (pageList != null && pageList.size() > 0) {
            
            for (int i = 0; i < pageList.size(); i++) {
                if (pageList.get(i) instanceof PdfPTable) {
                    document.add((PdfPTable)pageList.get(i));
                } else if (pageList.get(i) instanceof ARCgPdfNewPage) {
                    ARCgPdfNewPage newpage = (ARCgPdfNewPage)pageList.get(i);
                    if (newpage.getHeader() != null) {
                        header.setHeader(newpage.getHeader());
                    }
                    if (newpage.getFooter() != null) {
                        footer.setFooter(newpage.getFooter());
                    }
                    document.newPage();
                }
            }

/*            
            table.setTotalWidth(pagesize.getWidth());
            table.setLockedWidth(true);
            PdfContentByte canvas = writer.getDirectContent();
            PdfTemplate template = canvas.createTemplate(table.getTotalWidth(), table.getTotalHeight());
            table.writeSelectedRows(0, -1, 0, table.getTotalHeight(), template);
            Image img = Image.getInstance(template);
            img.scaleToFit(pagesize.getWidth(), pagesize.getHeight());
            img.setAbsolutePosition(0, pagesize.getHeight() - table.getTotalHeight());
            document.add(img);
*/        
        }

        document.close();
        outputStream.close();
    }

    public byte[] getPDFFile() throws Exception {
        File file = new File(outputFilePath);

        byte[] buffer = new byte[(int) file.length()];
        InputStream ios = null;
        try {
            ios = new FileInputStream(file);
            if (ios.read(buffer) == -1) {
                throw new Exception("EOF reached while trying to read the whole file");
            }
        } finally {
            try {
                if (ios != null)
                    ios.close();
            } catch (IOException e) {
            }
        }

        return buffer;
    }

}
