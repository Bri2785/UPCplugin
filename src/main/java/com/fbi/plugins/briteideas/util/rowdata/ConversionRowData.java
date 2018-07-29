package com.fbi.plugins.briteideas.util.rowdata;

import com.fbi.fbo.UOMConversion;
import com.fbi.fbo.impl.UOMConversionImpl;
import com.fbi.gui.rowdata.RowData;
import com.fbi.gui.table.FBTableColumn;
import com.fbi.gui.table.FBTableColumnEditable;
import com.fbi.gui.table.FBTableColumnSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class ConversionRowData implements RowData {

    private static final Logger LOGGER;

    private static int colCount;
    public static FBTableColumn colUPC; //toDB
    public static FBTableColumn colUOM; //view from the selected id
    public static FBTableColumn colUomQty; //pulled from the uom multiply

    private ProductConversion productConversion; //our table object
    private UOMConversionImpl uomConversion; //the UOM conversion we are using. The drop down comes from this

    public ConversionRowData(final ProductConversion productConversion){
        this.productConversion = productConversion;

    }

    public ProductConversion getProductConversion() {
        return productConversion;
    }

    public int getID() {
        return 0;
    }
    public String getValue() {
        //toString method
        return "ProductID: " + productConversion.getProductID() + ", UPC: " + this.productConversion.getUpcCode() +
                ", UOMid: " + productConversion.getUomFromId() + ", UOMMultiply: " + productConversion.getUomQty() +
                ", UomName: " + productConversion.getUomName();
    }


    public Object[] getRow() {
        final Object[] values = new Object[ConversionRowData.colCount];

        values[ConversionRowData.colUOM.getColIndex()] = this.productConversion.getUomName();
        values[ConversionRowData.colUomQty.getColIndex()] = this.productConversion.getUomQty();
        values[ConversionRowData.colUPC.getColIndex()] = this.productConversion.getUpcCode();

        return values;
    }

    public void setValueAt(int columnID, Object value) {
        //LOGGER.error("Set value of");
        //set value at column index 0 (UOM name)
        //set the productConversion name and qty and fromID number
        try {
            if (columnID == ConversionRowData.colUOM.getColIndex()) {
                UOMSearchRowData searchRowData = ((UOMSearchRowData) value);
                //LOGGER.error("Selected UOM" + searchRowData.getData().getUomCode());
                this.productConversion.setUomName(searchRowData.getData().getUomCode());
                this.productConversion.setUomQty(searchRowData.getData().getMultiply());
                this.productConversion.setUomFromId(searchRowData.getData().getId());

                //repaint?
            }
            else if (columnID == ConversionRowData.colUPC.getColIndex()) {
                //LOGGER.error("UPC column:" + value);
                this.productConversion.setUpcCode(value.toString());
                //repaint?
            }



        }
        catch (Exception e)
        {
            LOGGER.error("Error: ",e);
        }
    }


    public boolean isCellEditable(int i) {
        return true;
    }

    public static FBTableColumnSettings getSettings(){
        //LOGGER.error("getting row settings");
        final FBTableColumnSettings settings = new FBTableColumnSettings(true, true);
        settings.addColumn(ConversionRowData.colUOM);
        settings.addColumn(ConversionRowData.colUomQty);
        settings.addColumn(ConversionRowData.colUPC);
        //LOGGER.error("return settings, column count: " + settings.getColumnCount());
        return settings;
    }

    static {
        ConversionRowData.colCount = 0;
        colUOM = new FBTableColumn(ConversionRowData.colCount++, "UOM Name", (Class)String.class, 300, "", FBTableColumnEditable.ON_CLICK, false, true);
        colUomQty = new FBTableColumn(ConversionRowData.colCount++, "Uom Qty", Double.class, 300, "", FBTableColumnEditable.NOT_EDITABLE, false, false);
        colUPC = new FBTableColumn(ConversionRowData.colCount++, "UPC", String.class, 300, "", FBTableColumnEditable.ON_CLICK, false, false);

        LOGGER = LoggerFactory.getLogger((Class)ConversionRowData.class);
    }
}
