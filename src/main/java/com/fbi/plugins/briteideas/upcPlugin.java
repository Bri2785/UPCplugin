// 
// Decompiled by Procyon v0.5.30
// 

package com.fbi.plugins.briteideas;

import com.evnt.client.common.EVEManager;
import com.evnt.client.common.EVEManagerUtil;
import com.evnt.common.swing.product.panels.ProductSearchPanel;
import com.evnt.common.swing.swingutil.RefreshTitlePanel;
import com.fbi.gui.button.FBSideToolbar;
import com.fbi.gui.button.FBSideToolbarButton;
import com.fbi.gui.misc.GUIProperties;
import com.fbi.gui.misc.GUISavable;
import com.fbi.gui.misc.IconTitleBorderPanel;
import com.fbi.gui.panel.FBSplitPane;
import com.fbi.gui.table.FBTable;
import com.fbi.gui.table.FBTableModel;
import com.fbi.gui.table.cell.FBComboCell;
import com.fbi.plugins.briteideas.util.filters.FBFilterUomConv;
import com.fbi.plugins.briteideas.util.rowdata.ConversionRowData;
import com.fbi.plugins.briteideas.util.rowdata.ProductConversion;
import com.fbi.plugins.briteideas.util.rowdata.UOMSearchRowData;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.fbi.fbo.message.response.ResponseBase;
import com.fbi.fbo.message.request.RequestBase;
import com.fbi.fbo.impl.ApiCallType;
import com.fbi.plugins.briteideas.exception.FishbowlException;
//import com.fbi.commerce.shipping.util.Compatibility;

//import com.fbi.commerce.shipping.type.ShipService;
//import com.fbi.commerce.shipping.type.ShipServiceEnum;
import com.fbi.fbdata.setup.FbScheduleFpo;
//import SyncShipments;
import com.fbi.fbo.impl.dataexport.QueryRow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.evnt.common.exceptions.FBException;
import com.fbi.gui.util.UtilGui;

//import com.fbi.commerce.shipping.connection.HubApi;
//import com.fbi.commerce.shipping.util.CommerceUtil;
import com.evnt.util.Util;
import com.fbi.sdk.constants.MenuGroupNameConst;
import com.fbi.gui.button.FBMainToolbarButton;

import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import com.fbi.gui.panel.TitlePanel;
import org.slf4j.Logger;
import com.fbi.plugins.briteideas.fbapi.ApiCaller;
import com.fbi.plugins.briteideas.repository.UPCRepository;
import com.fbi.plugins.briteideas.util.property.PropertyGetter;
import com.fbi.plugins.FishbowlPlugin;

public class upcPlugin extends FishbowlPlugin implements PropertyGetter, UPCRepository.RunSql, ApiCaller
{

    private static final Logger LOGGER;
    private UPCRepository UPCRepository;


    //main container
    private FBSplitPane splPaneProductSearch;

    //left component
    private JPanel leftPanel;
    private IconTitleBorderPanel iconTitleBorderPanel1;
    private JPanel searchPanel;
    private ProductSearchPanel productSearchPanel;

    //right component
    private JPanel rightPanel;
    private RefreshTitlePanel pnlTitle; //title panel at top of right side
    private JPanel contentPanel;
    private JPanel itemDetailsPanel; //our final table goes in here

    private FBTable tblProductConversions;
    private FBTableModel<ConversionRowData> tblConversionsModel;

    private JPanel pnlConversions;
    private JScrollPane scrConversions;

    private FBSideToolbar barConversions;
    private FBSideToolbarButton btnAdd;
    private FBSideToolbarButton btnDelete;

    private int loadedProductId;

    EVEManager eveManager = EVEManagerUtil.getEveManager(); //get access to eve manager


    private TitlePanel label1;
    //private JPanel pnlCards; //container for the screen
    private JToolBar mainToolBar;

//    private FBMainToolbarButton btnSetup;
    private FBMainToolbarButton btnSave;

    private List<QueryRow> productConversionsList;


    
    public upcPlugin() {
        this.UPCRepository = new UPCRepository(this);
        this.setModuleName("UPCConv");
        this.setMenuListLocation(4);
        this.setMenuGroup(MenuGroupNameConst.MATERIALS);
        this.setMenuListLocation(1000); //bottom of the list
        this.setIconClassPath("/images/upc48x48.png");
        //this.setDefaultHelpPath("https://www.fishbowlinventory.com/wiki/ShipExpress");
//        this.addAccessRight("Rates Button");
//        this.addAccessRight("Ship Button");
//        this.addAccessRight("View Label Button");
        //

    }

    public void registerSources() {
        //ProductSearchDialog.clearCache();
        //this.eveManager.getRepository().registerSource(ProductModule.SOURCE_TREE_LIST, ProductModule.SOURCE_TREE_TRIGGER, "Product", "getProductTree");
        //this.eveManager.getCacheFBData().registerList(CacheListTypeConst.PRODUCT, "Product", "getListProducts");
    }
    
    
    protected void initModule() {
        super.initModule();
        this.initComponents();
        //LOGGER.error("Made it after the InitComponents method");
        this.setMainToolBar(this.mainToolBar);
        this.initLayout();

        this.productSearchPanel.getInventoryStatsPanel().setEnabled(false);


        this.productSearchPanel.init(this.eveManager);


        this.productSearchPanel.getPagedSearchPanel().addKeyListener((KeyListener)new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent evt) {
            }

            @Override
            public void keyPressed(final KeyEvent evt) {
                if (evt.getKeyCode() == 10) {
                    upcPlugin.this.loadSelectedProduct();
                    final JTable dataTable = (JTable)upcPlugin.this.productSearchPanel.getPagedSearchPanel().getTblData();
                    int selectedRowIndex = dataTable.getSelectedRow();
                    if (selectedRowIndex < dataTable.getRowCount() - 1) {
                        ++selectedRowIndex;
                    }
                    else {
                        selectedRowIndex = 0;
                    }
                    final int selectedID = upcPlugin.this.productSearchPanel.getPagedSearchPanel().getTblModel().getTableData().get(selectedRowIndex).getID();
                    upcPlugin.this.productSearchPanel.getInventoryStatsPanel().setSelectedID(selectedID);
                    dataTable.requestFocus();
                }
            }

            @Override
            public void keyReleased(final KeyEvent evt) {
            }
        });

        //CellEditorManager.registerEditor((Class)UOMConversionFPO.class, (CellEditorFactory)new FBComboCell((FBFilter)new FBFilterUomConv(), (FBTableSettings) UOMSearchRowData.getSettings(), "UOMCellEditedField", 250));

        GUIProperties.registerComponent((GUISavable)this.splPaneProductSearch, this.getModuleName());

        GUIProperties.loadComponents(this.getModuleName());
        this.btnAdd.setEnabled(false);

        this.populateTable(tblProductConversions, new ArrayList<>(), 0);

        //LOGGER.error("Made it after the initLayout method");
    }

    public boolean activateModule(){
        if (this.eveManager.isConnected()) {
            super.activateModule();
            if (this.isInitialized()) {
                LOGGER.error("Execute search");
                this.executeSearch();
            }
            return this.isInitialized();
        }
        return false;
    }
    
    private void initLayout() {
        //set the cell editor for the uom cell
        //TableCellEditor cellEditor;
        this.btnDelete.setEnabled(false);
    }


    private void productSearchPanelMouseClicked(MouseEvent evt) {
        if (evt.getComponent() instanceof JTable) {
            switch (evt.getButton()) {
                case 1: {
                    if (evt.getClickCount() == 2) {
                        this.loadSelectedProduct();
                        break;
                    }
                    break;
                }
                case 2:
                case 3: {
                    final boolean itemsSelected = !this.productSearchPanel.getPagedSearchPanel().getSelectedRowsData().isEmpty();
                    //this.mniInactivate.setEnabled(itemsSelected && this.hasEditRights);
                    //this.mnuSearch.show(evt.getComponent(), evt.getX(), evt.getY());
                    break;
                }
            }
        }

    }

    private void convItemsTableMouseClicked(final MouseEvent evt) {
        //LOGGER.error("Table clicked");
        boolean multipleItemsSelected = false;
        boolean oneThingIsSelected = false;
        if (this.tblConversionsModel.getSelectedRowsData().size() > 1) {
            multipleItemsSelected = true;

            this.btnDelete.setEnabled(false);
        }
        else if (this.tblConversionsModel.getSelectedRowsData().size() == 1) {
            oneThingIsSelected = true;
            this.btnDelete.setEnabled(true);
        }

        this.getController().setModified(true);
        //LOGGER.error("Modified:" + this.getController().isModified());
//        int column = this.tblProductConversions.getColumnModel().getColumnIndexAtX(evt.getX());
//        LOGGER.error("Column index clicked: " + column);
//
//        column = this.tblProductConversions.convertColumnIndexToModel(column);
//        if (evt.getClickCount() == 1) {
//            if (evt.getButton() == 1 && column == ConversionRowData.colUOM.getColIndex()) {
//                //final SOItem selectedItem = this.so.getItem(this.getSelectedSOItemLineNum());
//                final TableCellEditor cellEditor = this.tblProductConversions.getCellEditor();
//                LOGGER.error("CellEditor class: " + cellEditor.getClass().getName());
////                if (cellEditor instanceof FBComboCell.FBComboBoxCellEditor) {
////                    final FBComboCell.FBComboBoxCellEditor editor = (FBComboCell.FBComboBoxCellEditor)cellEditor;
////                    final FBFilterUomConv uomFilter = (FBFilterUomConv) editor.getFilter();
////                    uomFilter.setFilter(selectedItem.getType());
////                }
//            }
//
//        }

        //TODO: set the original item as the selected item in the drop down to avoid acidentally changing it
    }


    private void loadSelectedProduct() {
        final int prodID = this.productSearchPanel.getSelectedID();
        if (prodID == -1) {
            //UtilGui.showErrorMessageDialog(this.bundle.getString("ProductModuleClient.loadSelectedProduct.message", new Object[0]), this.bundle.getString("ProductModuleClient.loadSelectedProduct.title", new Object[0]));
            UtilGui.showErrorMessageDialog("No product selected", "Error");

            return;
        }
        //LOGGER.error("Check for Save" + this.checkForSave());
        if (!this.checkForSave()){
            return;
        }
            this.loadedProductId = prodID;
            this.loadData(prodID);
            this.pnlTitle.setModuleTitle("Conversions: " + this.productSearchPanel.getSelectedData().get(0).getProduct().getPartNum());

    }

    public int getObjectId() {
        return 1;
    }
    
    public void loadData(final int objectId) {

            this.productConversionsList = UPCRepository.getProductConversions(objectId);
            //LOGGER.error("Got the product list from repository");
            this.tblConversionsModel = this.populateTable(tblProductConversions, productConversionsList, objectId);
            this.btnAdd.setEnabled(true);
    }

    @Override
    public boolean checkForSave() {
        //LOGGER.error("check for save return: " + this.checkForSave(""));
        switch (this.checkForSave("")){
            case 0:{
                LOGGER.error("Case 0");
                this.saveChanges();
                return true;
                //needs saved
            }
            case 1:{
                //no save
                this.getController().setModified(false);
            }
            case 2:{
                LOGGER.error("Case 2"); //canceled was selected

                return false;
            }
            default:{
                return true; //yes its saved
            }
        }



    }

    private boolean validateRows(){
        boolean isValid = true;

        for (ConversionRowData row: this.tblConversionsModel.getTableData()) {
            isValid = !row.getProductConversion().getUomName().isEmpty() || !(row.getProductConversion().getUomQty() == new Double(0)) ||
                    !(row.getProductConversion().getUomFromId() == 0) || !(row.getProductConversion().getProductID() == 0) ||
                    !row.getProductConversion().getUpcCode().isEmpty();

            LOGGER.error("Table item: " + row.getProductConversion() + ", Is valid" + isValid);

        }

        return isValid;
    }
    private void saveChanges() {

        if (!validateRows()){
            UtilGui.showMessageDialog("Missing information. Check the records and try again");
            return;
        }

        try {
            //LOGGER.error("Save changes");
            for (ConversionRowData row : this.tblConversionsModel.getTableData()) {
                //if has id then update
                //if not then insert
                if (row.getProductConversion().getId() == -1) {
                    //new row so insert
                    this.UPCRepository.insertProductConversion(row.getProductConversion());
                }
                else{
                    this.UPCRepository.updateProductConversion(row.getProductConversion());
                }

            }

            this.getController().setModified(false); //reset modified
        }
        catch (Exception e){
            LOGGER.error("Error saving: ", e);
        }



    }

    @Override
    public boolean closeModule() {
        GUIProperties.saveComponents(this.getModuleName());
        this.getController().setModified(false);
        return super.closeModule();
    }

    private FBTableModel<ConversionRowData> populateTable(FBTable table, List<QueryRow> productConversionsList, int productId){
        try {
            //LOGGER.error("Populating table");
            //build model
            FBTableModel<ConversionRowData> model = new FBTableModel<ConversionRowData>(table, ConversionRowData.getSettings());

            //LOGGER.error("temp model created");
            table.getColumnModel().getColumn(0).setPreferredWidth(ConversionRowData.colUOM.getColDefWidth());
            table.getColumnModel().getColumn(1).setPreferredWidth(ConversionRowData.colUomQty.getColDefWidth());
            table.getColumnModel().getColumn(2).setPreferredWidth(ConversionRowData.colUPC.getColDefWidth());

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(0);
            table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

            try {
                table.getColumnModel().getColumn(0).setCellEditor((TableCellEditor) new FBComboCell<UOMSearchRowData>(new FBFilterUomConv(this.UPCRepository.getUOMConversionDataList()), UOMSearchRowData.getSettings(), "UOM Conv", 350).create());
            }
            catch (Exception e){
                LOGGER.error("changing cell editor", e);
            }


            //LOGGER.error("Populating table data");
            if (productConversionsList.size() > 0) {
//                productConversionsList.forEach((conversion) ->{
//                    //LOGGER.error("conversion object" + conversion);
//                });

                for (QueryRow row : productConversionsList
                     ) {
                    ProductConversion productConversion = new ProductConversion();
                    productConversion.setId(row.getInt("id"));
                    productConversion.setProductID(productId);
                    productConversion.setUpcCode(row.getString("upcCode"));
                    productConversion.setUomFromId(row.getInt("uomFromId"));
                    productConversion.setUomName(row.getString("UomName"));
                    productConversion.setUomQty(row.getDouble("UomQty"));

                    //LOGGER.error("converting queryRow to productConversion " + productConversion.getUpcCode());
                    model.addRow(new ConversionRowData(productConversion));

                }

            } else {
                LOGGER.error("Product Conversion list is blank");
            }

            //table.sortColumn(0);
            //LOGGER.error("Model built");
            return model;
        }
        catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }
        return  null;
    }

    private void createUIComponents() {
        //LOGGER.error("Creating table");
        this.tblProductConversions = this.createTable(() -> this.tblConversionsModel);
        //LOGGER.error("Table created");
    }

    private FBTable createTable(final Supplier<FBTableModel<ConversionRowData>> model) {
        final FBTable table = new FBTable();
        table.setFont(new Font("Tahoma", 0, 14));
        table.setRowHeight(30);
//        table.addMouseListener((MouseClickListener)e -> {
//            //LOGGER.error("Column Clicked: " + table.getColumnModel().getColumnIndex(CarrierRowData.colAction.getColName()));
//            if (table.getSelectedColumn() == table.getColumnModel().getColumnIndex(CarrierRowData.colAction.getColName())) {
//                //TODO: for freight make sure carriers exist in FB first
//                this.shipRatesDialog.addShippingLineToSo(((CarrierRowData) model.get().getSelectedRowData()).getData());
//            }
//            return;
//        });
        return table;
    }

    private void btnAddActionPerformed() {
        ProductConversion conversion = new ProductConversion();
        conversion.setProductID(this.loadedProductId); //loaded product
        conversion.setId(-1);

        this.tblConversionsModel.addRow(new ConversionRowData(conversion));
        //set focus to the drop down
        this.tblProductConversions.requestFocus();
        this.tblProductConversions.editCellAt(this.tblConversionsModel.getRows().size()-1,0); //last row first field

    }

    private void btnDeleteActionPerformed(){
        //get selected row
        //delete from DB
        //Delete from list
        if (this.tblConversionsModel.getSelectedRowsData().size()==1) {
            //only do one at a time
            if (this.tblConversionsModel.getSelectedRowData().getProductConversion().getId() != -1){
                //been saved to the db
                this.UPCRepository.deleteProductConversion(this.tblConversionsModel.getSelectedRowData().getProductConversion());
            }
            this.tblConversionsModel.removeRow(this.tblConversionsModel.getSelectedRowIndex());

        }

    }

    
    private void enableControls(final boolean enable) {
        this.btnSave.setEnabled(enable);
        //this.btnSync.setEnabled(enable);
        //this.btnBack.setEnabled(enable);
        //this.btnForward.setEnabled(enable);
    }
    
    private void displayConnectionErrors(final Exception e, final String line1Text, final String line2Text) {
//        ShippingPlugin.LOGGER.error(e.getMessage(), (Throwable)e);
//        this.pnlNoAccess.setMessage(line1Text, line2Text);
//        final CardLayout layout = (CardLayout)this.pnlCards.getLayout();
//        layout.show(this.pnlCards, "noAccess");
    }
    
    void saveProperties(final String username, final String password) {
//        try {
//            final String encryptedUsername = CommerceUtil.encrypt(username);
//            final String encryptedPassword = CommerceUtil.encrypt(password);
//            final Map<String, String> properties = new HashMap<String, String>();
//            properties.put(Property.USERNAME.getKey(), encryptedUsername);
//            properties.put(Property.PASS.getKey(), encryptedPassword);
//            this.savePluginProperties((Map)properties);
//        }
//        catch (CommerceException e) {
//            __Plugin.LOGGER.error("Can't save", (Throwable)e);
//            final String message = "Unable to save your ShipExpress integration.";
//            UtilGui.showMessageDialog(message, "Save Error", 1);
//        }
    }

    private void btnSyncActionPerformed() {
        if (Util.isEmpty(this.getProperty("shippoApiToken"))) {
            UtilGui.showMessageDialog("Please enter your Shippo API Key in Account Setup section of the Shipping Settings.", "Missing Shippo Api Key", 1);
            return;
        }
        try {
            UtilGui.showMessageDialog("A sync has started. Please refresh once the sync has completed.", "Sync started", 1);
            this.runScheduledTask("ShipExpress Shipment Sync");
        }
        catch (FBException e) {
            upcPlugin.LOGGER.error(e.getMessage(), (Throwable)e);
        }
    }
    
    public String getProperty(final String key) {
        return this.UPCRepository.getProperty(key);
    }
    
    public List<QueryRow> executeSql(final String query) {
        return (List<QueryRow>)this.runQuery(query);
    }
    
    private void btnSaveActionPerformed() {

        saveChanges();

    }
    
    private void createScheduledTask(final String name, final Class clazz, final String cron) {
        try {
            if (this.getScheduledTask(name) == null) {
                final FbScheduleFpo schedule = this.createSchedule(name, "DO NOT DELETE", clazz, "", cron);
                this.updateScheduledTask(schedule);
            }
        }
        catch (Exception e) {
            upcPlugin.LOGGER.error(e.getMessage(), (Throwable)e);
            throw new IllegalStateException(e);
        }
    }

    
//    private ImportRequest createImportRequest(final Map<String, List<String>> servicesToAdd, final List<QueryRow> carriersAndServices, final String fbFedEx, final String fbFedExDesc, final String fbUsps, final String fbUspsDesc, final String fbUps, final String fbUpsDesc) {
//        final ArrayList<String> importRows = new ArrayList<String>();
//        final StringRowData headerRow = new StringRowData(IECarrierImpl.defaultHeader);
//        importRows.add(headerRow.toString());
//        Integer carrierId = carriersAndServices.get(0).getInt("carrierId");
//        for (final Map.Entry<String, List<String>> entry : servicesToAdd.entrySet()) {
//            for (final String service : entry.getValue()) {
//                final StringRowData dataRow = new StringRowData(IECarrierImpl.defaultHeader);
//                dataRow.setColumnNames(headerRow);
//                dataRow.set("Name", this.getCarrierName(entry.getKey(), fbFedEx, fbUsps, fbUps));
//                dataRow.set("ServiceName", service);
//
//                final String s = "ServiceCode";
//
//                final String carrierName = entry.getKey();
//                final Integer n = carrierId;
//                ++carrierId;
//                dataRow.set(s, this.getServiceCode(service, carrierName, n));
//                dataRow.set("Active", Boolean.TRUE.toString());
//                dataRow.set("ServiceActive", Boolean.TRUE.toString());
//                Compatibility.setCarrierDescription(this.repository, dataRow, entry.getKey(), fbFedExDesc, fbUspsDesc, fbUpsDesc);
//                importRows.add(dataRow.toString());
//            }
//        }
//        final ImportRequest importRequest = (ImportRequest)new ImportRequestImpl();
//        importRequest.setImportType("ImportCarriers");
//        importRequest.setRows((ArrayList)importRows);
//        return importRequest;
//    }

    
//    void enableBack(final boolean browserIsShowing) {
//        this.btnBack.setEnabled(browserIsShowing);
//    }
//
//    void enableForward(final boolean browserIsShowing) {
//        this.btnForward.setEnabled(browserIsShowing);
//    }

    public ResponseBase call(final ApiCallType requestType, final RequestBase requestBase) throws FishbowlException {
        try {
            return this.runApiRequest(requestType, requestBase);
        }
        catch (Exception e) {
            throw new FishbowlException(e);
        }
    }
    
    private void initComponents() {
        this.createUIComponents();

        //main screen
        this.setName("this");
        this.setLayout((LayoutManager)new BorderLayout());

        this.splPaneProductSearch = new FBSplitPane();

        //left component
        this.leftPanel = new JPanel();
        this.iconTitleBorderPanel1 = new IconTitleBorderPanel();
        this.searchPanel = new JPanel();
        this.productSearchPanel = new ProductSearchPanel();



        //right component
        this.rightPanel = new JPanel();
        this.pnlTitle = new RefreshTitlePanel();
        this.contentPanel = new JPanel();

        this.barConversions = new FBSideToolbar();
        this.btnAdd = new FBSideToolbarButton();
        this.btnDelete = new FBSideToolbarButton();

        this.itemDetailsPanel = new JPanel();
        this.pnlConversions = new JPanel();
        this.scrConversions = new JScrollPane();


        //main split panel
        this.splPaneProductSearch.setDividerLocation(220);
        this.splPaneProductSearch.setOneTouchExpandable(true);
        this.splPaneProductSearch.setFocusable(false);
        this.splPaneProductSearch.setName("splPaneProductSearch");
    //left side parts
        //left panel
        this.leftPanel.setMinimumSize(new Dimension(1, 1));
        this.leftPanel.setName("leftPanel");
        this.leftPanel.setLayout(new BorderLayout());

        //left panel inside
        this.iconTitleBorderPanel1.setTitle("Search");
        this.iconTitleBorderPanel1.setType(IconTitleBorderPanel.IconConst.Search);
        this.iconTitleBorderPanel1.setName("iconTitleBorderPanel1");
        this.iconTitleBorderPanel1.setLayout((LayoutManager)new GridBagLayout());
        ((GridBagLayout)this.iconTitleBorderPanel1.getLayout()).columnWidths = new int[] { 0, 0 };
        ((GridBagLayout)this.iconTitleBorderPanel1.getLayout()).rowHeights = new int[] { 0, 0 };
        ((GridBagLayout)this.iconTitleBorderPanel1.getLayout()).columnWeights = new double[] { 1.0, 1.0E-4 };
        ((GridBagLayout)this.iconTitleBorderPanel1.getLayout()).rowWeights = new double[] { 1.0, 1.0E-4 };

        //icon panel inside
        this.searchPanel.setName("searchPanel");
        this.searchPanel.setLayout(new BorderLayout());

        this.productSearchPanel.setName("productSearchPanel");
        this.productSearchPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                upcPlugin.this.productSearchPanelMouseClicked(e);
            }
        });
        //add productSearchpanel to search panel
        this.searchPanel.add(this.productSearchPanel, "Center");
        //add search panel to icon panel
        this.iconTitleBorderPanel1.add((Component)this.searchPanel, (Object)new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        //add icon panel to the left panel
        this.leftPanel.add((Component)this.iconTitleBorderPanel1, "Center");
        //add left lanel to the left component of hte split panel
        this.splPaneProductSearch.setLeftComponent((Component)this.leftPanel);

    //right side parts
        //right panel
        this.rightPanel.setMinimumSize(new Dimension(1, 1));
        this.rightPanel.setName("rightPanel");
        this.rightPanel.setLayout(new BorderLayout());//new GridBagLayout());


        this.pnlTitle.setBorder(new EtchedBorder());
        this.pnlTitle.setModuleIcon(new ImageIcon(this.getClass().getResource("/module/product32.png")));
        this.pnlTitle.setModuleTitle("Conversions");
        this.pnlTitle.setName("pnlTitle");

        this.contentPanel.setName("contentPanel");
        this.contentPanel.setLayout(new GridBagLayout());

        this.barConversions.setFloatable(false);
        this.barConversions.setOrientation(1);
        this.barConversions.setRollover(true);
        this.barConversions.setName("barConv");

        this.btnAdd.setIcon((Icon)new ImageIcon(this.getClass().getResource("/icon16/toolbar/others/add.png")));
        this.btnAdd.setToolTipText("New Conversion");
        this.btnAdd.setName("ConvPaneladdbtn");
        this.btnAdd.addActionListener(e -> this.btnAddActionPerformed());
        this.barConversions.add((Component)this.btnAdd);
        this.btnDelete.setIcon((Icon)new ImageIcon(this.getClass().getResource("/icon16/textanddocuments/documents/document_delete.png")));
        this.btnDelete.setToolTipText("Delete Conversion");
        this.btnDelete.setName("ConvPanelDeletebtn");
        this.btnDelete.addActionListener(e -> this.btnDeleteActionPerformed());
        this.barConversions.add((Component)this.btnDelete);

        final Dimension buttonSize = new Dimension(30, 30);
        this.btnAdd.setMinimumSize(buttonSize);
        this.btnAdd.setMaximumSize(buttonSize);
        this.btnAdd.setPreferredSize(buttonSize);
        this.btnDelete.setMinimumSize(buttonSize);
        this.btnDelete.setMaximumSize(buttonSize);
        this.btnDelete.setPreferredSize(buttonSize);

        this.itemDetailsPanel.setPreferredSize(new Dimension(10, 325));
        this.itemDetailsPanel.setName("itemDetailsPanel");
        this.itemDetailsPanel.setLayout( new GridBagLayout());


        this.scrConversions.setName("scrConversions");
        this.tblProductConversions.setName("tblConversions");
        this.tblProductConversions.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(final MouseEvent e){
                upcPlugin.this.convItemsTableMouseClicked(e);
            }
        });



        this.scrConversions.setViewportView((Component)this.tblProductConversions);

        this.pnlConversions.setName("pnlConversions");
        this.pnlConversions.setLayout(new GridBagLayout());
        ((GridBagLayout)this.pnlConversions.getLayout()).columnWidths = new int[] { 0, 0 };
        ((GridBagLayout)this.pnlConversions.getLayout()).rowHeights = new int[] { 0, 0, 0 };
        ((GridBagLayout)this.pnlConversions.getLayout()).columnWeights = new double[] { 1.0, 1.0E-4 };
        ((GridBagLayout)this.pnlConversions.getLayout()).rowWeights = new double[] { 0.0, 1.0, 1.0E-4 };
        this.pnlConversions.add(this.scrConversions, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));

        //add table panel to the details panel
        this.itemDetailsPanel.add(this.pnlConversions, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        //add item detail panel to content panel
        this.contentPanel.add(this.itemDetailsPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        //add title panel to the right panel
        this.rightPanel.add((Component)this.pnlTitle,"North");//  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        //add cintent panel under the title panel
        this.rightPanel.add((Component)this.contentPanel,"Center");// new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        //add function bar to right side
        this.rightPanel.add((Component) this.barConversions, "East");

        //add right panel to the split view as the right component
        this.splPaneProductSearch.setRightComponent((Component)this.rightPanel);

     //add the title panel to the main screen
        //Title menu
        this.label1 = new TitlePanel();
        this.label1.setModuleIcon((Icon)new ImageIcon(this.getClass().getResource("/images/upc32x32.png")));
        this.label1.setModuleTitle("Product UPC Conversions");
        this.label1.setBackground(new Color(44, 94, 140));
        this.label1.setName("label1");
        this.add((Component)this.label1, (Object)"North");





        //finally add the split panel to the main screen in the center
        this.add((Component)this.splPaneProductSearch, (Object)"Center");






     //toolbar items
        this.mainToolBar = new JToolBar();

        this.mainToolBar.setFloatable(false);
        this.mainToolBar.setRollover(true);
        this.mainToolBar.setName("mainToolBar");

        this.btnSave = new FBMainToolbarButton();
//        this.btnSetup.setIcon((Icon)new ImageIcon(this.getClass().getResource("/icon24/textanddocuments/documents/document_new.png")));
//        this.btnSetup.setText("Setup");
//        this.btnSetup.setHorizontalTextPosition(0);
//        this.btnSetup.setIconTextGap(0);
//        this.btnSetup.setMargin(new Insets(0, 2, 0, 2));
//        this.btnSetup.setName("PartToolbarNewbtn");
//        this.btnSetup.setVerticalTextPosition(3);
//        this.btnSetup.setToolTipText("Setup integration");
//        this.btnSetup.addActionListener((ActionListener)new ActionListener() {
//            @Override
//            public void actionPerformed(final ActionEvent e) {
//                upcPlugin.this.btnSetupActionPerformed();
//            }
//        });
//        this.mainToolBar.add((Component)this.btnSetup);
        this.btnSave.setIcon((Icon)new ImageIcon(this.getClass().getResource("/icon24/filesystem/disks/disk_gold.png")));
        this.btnSave.setText("Save");
        this.btnSave.setToolTipText("Save your ShipExtend settings.");
        this.btnSave.setHorizontalTextPosition(0);
        this.btnSave.setIconTextGap(0);
        this.btnSave.setMargin(new Insets(0, 2, 0, 2));
        this.btnSave.setName("btnSave");
        this.btnSave.setVerticalTextPosition(3);
        this.btnSave.addActionListener((ActionListener)new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                upcPlugin.this.btnSaveActionPerformed();
            }
        });
        this.mainToolBar.add((Component)this.btnSave);


        //seperator in between the button as we add them

//        jSeparator1.setOrientation(1);
//        jSeparator1.setMaximumSize(new Dimension(10, 50));
//        jSeparator1.setName("jSeparator1");
//        this.mainToolBar.add(jSeparator1);

//        this.btnBack.setIcon((Icon)new ImageIcon(this.getClass().getResource("/images/nav_left24.png")));
//        this.btnBack.setButtonSize(new Dimension(53, 50));
//        this.btnBack.setToolTipText("Click to go back");
//        this.btnBack.setText("Back");
//        this.btnBack.setName("btnBack");
//        this.btnBack.addActionListener((ActionListener)new ActionListener() {
//            @Override
//            public void actionPerformed(final ActionEvent e) {
//                upcPlugin.this.btnBackActionPerformed();
//            }
//        });
//        this.mainToolBar.add((Component)this.btnBack);
//        this.btnForward.setIcon((Icon)new ImageIcon(this.getClass().getResource("/images/nav_right24.png")));
//        this.btnForward.setToolTipText("Click to go forward");
//        this.btnForward.setText("Forward");
//        this.btnForward.setName("btnForward");
//        this.btnForward.addActionListener((ActionListener)new ActionListener() {
//            @Override
//            public void actionPerformed(final ActionEvent e) {
//                upcPlugin.this.btnForwardActionPerformed();
//            }
//        });
//        this.mainToolBar.add((Component)this.btnForward);
    }



    static {
        LOGGER = LoggerFactory.getLogger((Class)upcPlugin.class);
    }
}
