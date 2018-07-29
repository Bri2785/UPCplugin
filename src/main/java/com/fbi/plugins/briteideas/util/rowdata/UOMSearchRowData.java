package com.fbi.plugins.briteideas.util.rowdata;

import com.fbi.fbdata.misc.UomConversionFpo;
import com.fbi.gui.rowdata.RowData;
import com.fbi.gui.table.FBTableColumn;
import com.fbi.gui.table.FBTableColumnEditable;
import com.fbi.gui.table.FBTableColumnSettings;

public class UOMSearchRowData implements RowData
{
    private UOMData data;
    private static int colCount;
    protected static FBTableColumn colName;
    protected static FBTableColumn colQty;


    protected static void init() {
        UOMSearchRowData.colCount = 0;
        UOMSearchRowData.colName = new FBTableColumn(UOMSearchRowData.colCount++, "Name", String.class, 100, "", FBTableColumnEditable.NOT_EDITABLE, false, true);
        UOMSearchRowData.colQty = new FBTableColumn(UOMSearchRowData.colCount++, "Qty per box", String.class, 55, "", FBTableColumnEditable.NOT_EDITABLE, false, false);
        
    }

    public UOMSearchRowData(final UOMData data) {
        this.data = data;
    }

    public int getID() {
        return this.data.getId();
    }

    public String getValue() {
        return this.data.getUomCode();
    }

    public Object[] getRow() {
        final Object[] values = new Object[UOMSearchRowData.colCount];
        values[UOMSearchRowData.colName.getColIndex()] = this.data.getUomCode();
        values[UOMSearchRowData.colQty.getColIndex()] = this.data.getMultiply();
        return values;
    }

    public void setValueAt(final int columnID, final Object value) {
    }

    public boolean isCellEditable(final int columnID) {
        return false;
    }

    public UOMData getData() {
        return this.data;
    }

    public static FBTableColumnSettings getSettings() {
        final FBTableColumnSettings settings = new FBTableColumnSettings(true, true);
        settings.addColumn(UOMSearchRowData.colName);
        settings.addColumn(UOMSearchRowData.colQty);

        return settings;
    }

    static {
        init();
    }

}
