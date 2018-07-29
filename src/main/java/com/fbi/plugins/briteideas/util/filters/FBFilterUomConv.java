package com.fbi.plugins.briteideas.util.filters;

import com.fbi.gui.combobox.FBFilter;
import com.fbi.plugins.briteideas.util.LogicUOMConversions;
import com.fbi.plugins.briteideas.util.rowdata.UOMData;
import com.fbi.plugins.briteideas.util.rowdata.UOMSearchRowData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class FBFilterUomConv extends FBFilter<UOMSearchRowData> {

    private static final Logger LOGGER;
    private LogicUOMConversions logicUOMConversions;

    private List<UOMData> data;



    public FBFilterUomConv(List<UOMData> data){
        this.data = data;
    }

    @Override
    protected List<UOMSearchRowData> getRawData() {
        final  List<UOMSearchRowData> uomSearchRowData = new ArrayList<>();

        for (UOMData conv : data
             ) {
            //LOGGER.error("Conversion: " + conv.getUomCode() + conv.getMultiply());
            //we have the list of UOM Conversions now
            //put them into row data
            uomSearchRowData.add(new UOMSearchRowData(conv));
        }

        return uomSearchRowData;
    }

    @Override
    protected boolean doesRowMatch(UOMSearchRowData uomSearchRowData, String filter) {
        return true;
    }

    @Override
    public int createNew(String s) {
        return -1;
    }

    static {
        LOGGER = LoggerFactory.getLogger((Class)FBFilterUomConv.class);
    }
}
