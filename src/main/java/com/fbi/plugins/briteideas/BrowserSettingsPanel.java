// 
// Decompiled by Procyon v0.5.30
// 

package com.fbi.plugins.briteideas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.GridBagLayout;
import com.evnt.ui.PercentFormatter;
import com.fbi.gui.misc.IconTitleBorderPanel;
import java.awt.Component;

import com.fbi.plugins.briteideas.util.property.Property;
import com.teamdev.jxbrowser.chromium.BrowserContextParams;
import com.teamdev.jxbrowser.chromium.BrowserContext;
import java.util.HashMap;
import com.fbi.gui.util.UtilGui;
import java.util.Map;
import com.evnt.util.Util;
//import com.fbi.commerce.shipping.connection.HubApi;
import com.evnt.util.Money;
import com.evnt.util.Percent;
import com.fbi.gui.table.FBTableSettings;
import com.fbi.gui.combobox.FBFilter;
import com.fbi.plugins.briteideas.client.filter.CProductSearchRowData;
import com.fbi.plugins.briteideas.client.filter.CProductFilter;
import com.fbi.gui.textfield.FBTextFieldMoney;
import com.fbi.gui.borders.borders.CheckBoxBorderPanel;
import com.evnt.client.modules.product.data.ProductSearchRowData;
import com.fbi.plugins.briteideas.components.CComboBox;
import com.jidesoft.swing.JideTabbedPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BrowserSettingsPanel extends JPanel
{
    private upcPlugin plugin;
    private static final Logger LOGGER;
    private static final String SHIPPING_TAB_TITLE = "ShipExtend";
    private static final String SHIPPING_SETTINGS_TAB_TITLE = "Shipping Settings";
    private JideTabbedPane tbPnlInfo;
    private BrowserPanel pnlBrowser;
    private BrowserPanel pnlShipSettings;
    private JPanel pnplOtherSettings;
    private JPanel pnlOtherSettingsContent;
    private JCheckBox ckAutoFulfillOrders;
    private JCheckBox ckSetCartonCost;
    private JCheckBox ckPurchaseInsurance;
    private JCheckBox ckShowZeroQtyWarning;
    private JCheckBox ckFedEx;
    private JCheckBox ckUPS;
    private JCheckBox ckUSPS;
    private JPanel pnlShippingProduct;
    private JPanel pnlShippingProductContent;
    private JLabel lblShippingProduct;
    private CComboBox<ProductSearchRowData> cboShippingProduct;
    private CheckBoxBorderPanel pnlRateMarkup;
    private JRadioButton rbtnPercent;
    private JFormattedTextField txtPercent;
    private JRadioButton rbtnRate;
    private FBTextFieldMoney txtRate;
    private JCheckBox ckRetailOnly;

    private JLabel lblShippoApiKey;
    private JTextField txtShippoApiKey;
    private JCheckBox ckDebugMode;
    
    BrowserSettingsPanel(final upcPlugin plugin) {
        this.plugin = plugin;
        this.initComponents();
        this.cboShippingProduct.setFilter((FBFilter)new CProductFilter(false), (FBTableSettings)CProductSearchRowData.getSettings());
        this.loadSettings();
    }
    
    private void loadSettings() {
        this.ckAutoFulfillOrders.setSelected(Property.AUTO_FULFILL_ORDERS.get(this.plugin));
        this.ckSetCartonCost.setSelected(Property.SET_CARTON_COST.get(this.plugin));
        this.ckPurchaseInsurance.setSelected(Property.PURCHASE_INSURANCE.get(this.plugin));

        this.ckDebugMode.setSelected(Property.DEBUG_MODE.get(this.plugin));
        this.txtShippoApiKey.setText(Property.SHIPPO_API_TOKEN.get(this.plugin));

        this.ckRetailOnly.setSelected(Property.RETAIL_RATES_ONLY.get(this.plugin));
        this.ckUPS.setSelected(Property.USE_UPS.get(this.plugin));
        this.ckFedEx.setSelected(Property.USE_FEDEX.get(this.plugin));
        this.ckUSPS.setSelected(Property.USE_USPS.get(this.plugin));
        this.ckShowZeroQtyWarning.setSelected(Property.SHOW_ZERO_QTY_WARING.get(this.plugin));
        this.pnlRateMarkup.setSelected((boolean)Property.USE_MARKUP.get(this.plugin));
        if (Property.USE_PERCENT.get(this.plugin)) {
            this.rbtnPercent.setSelected(true);
        }
        else {
            this.rbtnRate.setSelected(true);
        }
        Double percentValue = Property.MARKUP_PERCENT.get(this.plugin);
        if (percentValue == null) {
            percentValue = 0.0;
        }
        final Percent p = new Percent((double)percentValue);
        this.txtPercent.setText(p.toString());
        this.txtRate.setMoney(new Money((Double)Property.MARKUP_RATE.get(this.plugin)));
        final Integer shippingItemId = Property.SHIPPING_ITEM_ID.get(this.plugin);
        if (shippingItemId == null) {
            this.cboShippingProduct.setSelectedID(-1);
        }
        else {
            this.cboShippingProduct.setSelectedID((int)shippingItemId);
        }
        this.enableRateMarkup(Property.USE_MARKUP.get(this.plugin));
    }
    
    void reload() {
//        if ("ShipExpress".equals(this.tbPnlInfo.getTitleAt(this.tbPnlInfo.getSelectedIndex()))) {
//            this.pnlBrowser.reload();
//        }
//        else if ("Shipping Settings".equals(this.tbPnlInfo.getTitleAt(this.tbPnlInfo.getSelectedIndex()))) {
//            this.pnlShipSettings.reload();
//        }
//        else {
            this.loadSettings();
//        }
    }
    
    void showLoginPage() {
        this.pnlBrowser.showLoginPage();
    }
    
    void showSignupPage() {
        this.pnlBrowser.showSignupPage();
    }
    
    void forward() {
//        if ("ShipExpress".equals(this.tbPnlInfo.getTitleAt(this.tbPnlInfo.getSelectedIndex()))) {
//            this.pnlBrowser.forward();
//        }
//        else if ("Shipping Settings".equals(this.tbPnlInfo.getTitleAt(this.tbPnlInfo.getSelectedIndex()))) {
//            this.pnlShipSettings.forward();
//        }
    }
    
    void back() {
//        if ("ShipExpress".equals(this.tbPnlInfo.getTitleAt(this.tbPnlInfo.getSelectedIndex()))) {
//            this.pnlBrowser.back();
//        }
//        else if ("Shipping Settings".equals(this.tbPnlInfo.getTitleAt(this.tbPnlInfo.getSelectedIndex()))) {
//            this.pnlShipSettings.back();
//        }
    }
    
    void saveSettings() {
        if (!this.validateSettings()) {
            return;
        }
        this.saveProperties();
    }
    
    private void saveProperties() {
        LOGGER.error("Saving Properties");
        //final Map<String, String> shippingInfo = HubApi.getShippingInfo(); //removed since we dont have access to the api
//        if (Util.isEmpty((Map)shippingInfo) || Util.isEmpty((String)shippingInfo.get("shippoApiToken"))) {
//            UtilGui.showMessageDialog("Shippo API Key is required.", "Save Error", 1);
//            this.tbPnlInfo.setSelectedIndex(2);
//            return;
//        }
        final Map<String, String> properties = new HashMap<String, String>();
//        properties.put(Property.SHIPPO_API_TOKEN.getKey(), shippingInfo.get("shippoApiToken")); //have to set manually
//        properties.put(Property.LABEL_TEXT.getKey(), shippingInfo.get("labelText"));
//        properties.put(Property.LABEL_FORMAT.getKey(), shippingInfo.get("labelFormat"));
        properties.put(Property.AUTO_FULFILL_ORDERS.getKey(), Boolean.toString(this.ckAutoFulfillOrders.isSelected()));
        properties.put(Property.SET_CARTON_COST.getKey(), Boolean.toString(this.ckSetCartonCost.isSelected()));
        properties.put(Property.PURCHASE_INSURANCE.getKey(), Boolean.toString(this.ckPurchaseInsurance.isSelected()));
        properties.put(Property.RETAIL_RATES_ONLY.getKey(), Boolean.toString(this.ckRetailOnly.isSelected()));

        //LOGGER.error("token property: " + this.txtShippoApiKey.getText());
        properties.put(Property.DEBUG_MODE.getKey(), Boolean.toString(this.ckDebugMode.isSelected()));
        properties.put(Property.SHIPPO_API_TOKEN.getKey(), this.txtShippoApiKey.getText());

        properties.put(Property.USE_UPS.getKey(), Boolean.toString(this.ckUPS.isSelected()));
        properties.put(Property.USE_USPS.getKey(), Boolean.toString(this.ckUSPS.isSelected()));
        properties.put(Property.USE_FEDEX.getKey(), Boolean.toString(this.ckFedEx.isSelected()));
        properties.put(Property.USE_MARKUP.getKey(), Boolean.toString(this.pnlRateMarkup.isSelected()));
        properties.put(Property.USE_PERCENT.getKey(), Boolean.toString(this.rbtnPercent.isSelected()));
        Percent percent = new Percent(0L);
        if (!Util.isEmpty(this.txtPercent.getText())) {
            percent = new Percent(this.txtPercent.getText());
        }
        properties.put(Property.MARKUP_PERCENT.getKey(), Double.toString(percent.doubleValue()));
        properties.put(Property.MARKUP_RATE.getKey(), this.txtRate.getMoney().toFullStringNoSymbolNoGrouping());
        properties.put(Property.SHIPPING_ITEM_ID.getKey(), Integer.toString(this.cboShippingProduct.getSelectedID()));
        properties.put(Property.SHOW_ZERO_QTY_WARING.getKey(), Boolean.toString(this.ckShowZeroQtyWarning.isSelected()));
        this.plugin.savePluginProperties((Map)properties);
        LOGGER.error("Properties Saved");
    }
    
    private boolean validateSettings() {
        if (this.cboShippingProduct.getSelectedData() == null) {
            UtilGui.showMessageDialog("Shipping product is required.", "Save Error", 1);
            this.tbPnlInfo.setSelectedIndex(this.tbPnlInfo.indexOfTab("Plugin Settings"));
            this.cboShippingProduct.requestFocusInWindow();
            return false;
        }
        return true;
    }
    
    private void tbPnlInfoStateChanged() {
        final boolean browserIsShowing = "ShipExtend".equals(this.tbPnlInfo.getTitleAt(this.tbPnlInfo.getSelectedIndex())) || "Shipping Settings".equals(this.tbPnlInfo.getTitleAt(this.tbPnlInfo.getSelectedIndex()));
        this.plugin.enableBack(browserIsShowing);
        this.plugin.enableForward(browserIsShowing);
    }
    
    private void enableRateMarkup(final boolean enable) {
        this.rbtnPercent.setEnabled(enable);
        this.txtPercent.setEnabled(enable && this.rbtnPercent.isSelected());
        this.rbtnRate.setEnabled(enable);
        this.txtRate.setEnabled(enable && this.rbtnRate.isSelected());
    }
    
    private void rbtnRateActionPerformed() {
        this.txtPercent.setEnabled(false);
        this.txtRate.setEnabled(true);
    }
    
    private void rbtnPercentActionPerformed() {
        this.txtPercent.setEnabled(true);
        this.txtRate.setEnabled(false);
    }
    
    private void pnlRateMarkupMouseClicked() {
        this.enableRateMarkup(this.pnlRateMarkup.isSelected());
    }

    private void createUIComponents() {
        BrowserContext browserContext = new BrowserContext(new BrowserContextParams(BrowserContext.defaultContext().getCacheDir()));
        this.pnlBrowser = new BrowserPanel(this.plugin, browserContext, BrowserPanel.SERVER_PAGE);
        //this.pnlShipSettings = new BrowserPanel(this.shippingPlugin, browserContext, BrowserPanel.SETTINGS_PAGE);
    }
    
    public CComboBox<ProductSearchRowData> getCboShippingProduct() {
        return this.cboShippingProduct;
    }
    
    public void showPnlShipExpress(final boolean showPnlShipExpress) {
        if (showPnlShipExpress && this.tbPnlInfo.getTabCount() == 2) {
            this.tbPnlInfo.insertTab("ShipExtend", (Icon)null, (Component)this.pnlBrowser, "", 0);
        }
        else if (!showPnlShipExpress && this.tbPnlInfo.getTabCount() == 3) {
            this.tbPnlInfo.remove((Component)this.pnlBrowser);
        }
    }
    
    private void initComponents() {
        this.createUIComponents();
        this.tbPnlInfo = new JideTabbedPane();
        final JPanel pnlSettings = new JPanel();
        final IconTitleBorderPanel pnlCheckRateSettings = new IconTitleBorderPanel();
        final JPanel pnlSettingsContent = new JPanel();
        this.pnplOtherSettings = new JPanel();
        this.pnlOtherSettingsContent = new JPanel();
        this.ckAutoFulfillOrders = new JCheckBox();
        this.ckSetCartonCost = new JCheckBox();
        this.ckPurchaseInsurance = new JCheckBox();
        this.ckShowZeroQtyWarning = new JCheckBox();

        this.ckDebugMode = new JCheckBox();
        this.lblShippoApiKey = new JLabel();
        this.txtShippoApiKey = new JTextField();

        final IconTitleBorderPanel pnlCheckRateSettings2 = new IconTitleBorderPanel();
        final JPanel pnlSettingsContent2 = new JPanel();
        final JPanel pnlCarrierRates = new JPanel();
        final JPanel pnlCarrierRatesContent = new JPanel();
        final JPanel pnlShowCarrierRatesInfo = new JPanel();
        final JLabel lblCarrierRateMessageHeader = new JLabel();
        final JLabel lblCarrierRatesMessage = new JLabel();
        this.ckFedEx = new JCheckBox();
        this.ckUPS = new JCheckBox();
        this.ckUSPS = new JCheckBox();
        this.pnlShippingProduct = new JPanel();
        this.pnlShippingProductContent = new JPanel();
        this.lblShippingProduct = new JLabel();
        this.cboShippingProduct = new CComboBox<ProductSearchRowData>();
        this.pnlRateMarkup = new CheckBoxBorderPanel();
        final JPanel pnlRateMarkupContent = new JPanel();
        this.rbtnPercent = new JRadioButton();
        this.txtPercent = new JFormattedTextField((JFormattedTextField.AbstractFormatter)new PercentFormatter("0.####%"));
        this.rbtnRate = new JRadioButton();
        this.txtRate = new FBTextFieldMoney();
        this.ckRetailOnly = new JCheckBox();
        this.setName("this");
        this.setLayout(new GridBagLayout());
        ((GridBagLayout)this.getLayout()).columnWidths = new int[] { 0, 0 };
        ((GridBagLayout)this.getLayout()).rowHeights = new int[] { 0, 0 };
        ((GridBagLayout)this.getLayout()).columnWeights = new double[] { 1.0, 1.0E-4 };
        ((GridBagLayout)this.getLayout()).rowWeights = new double[] { 1.0, 1.0E-4 };
        this.tbPnlInfo.setFocusable(false);
        this.tbPnlInfo.setRequestFocusEnabled(false);
        this.tbPnlInfo.setVerifyInputWhenFocusTarget(false);
        this.tbPnlInfo.setTabShape(10);
        this.tbPnlInfo.setBoldActiveTab(true);
        this.tbPnlInfo.setMinimumSize(new Dimension(502, 330));
        this.tbPnlInfo.setPreferredSize(new Dimension(502, 330));
        this.tbPnlInfo.setMaximumSize(new Dimension(502, 330));
        this.tbPnlInfo.setName("tbPnlInfo");
        this.tbPnlInfo.addChangeListener((ChangeListener)new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent e) {
                BrowserSettingsPanel.this.tbPnlInfoStateChanged();
            }
        });
        this.pnlBrowser.setName("pnlBrowser");
        //this.tbPnlInfo.addTab("ShipExpress", (Component)this.pnlBrowser);
        //this.pnlShipSettings.setName("pnlShipSettings");
        //this.tbPnlInfo.addTab("Shipping Settings", (Component)this.pnlShipSettings);
        pnlSettings.setName("pnlSettings");
        pnlSettings.setLayout(new GridBagLayout());
        ((GridBagLayout)pnlSettings.getLayout()).columnWidths = new int[] { 478, 0 };
        ((GridBagLayout)pnlSettings.getLayout()).rowHeights = new int[] { 0, 0, 0 };
        ((GridBagLayout)pnlSettings.getLayout()).columnWeights = new double[] { 0.0, 1.0E-4 };
        ((GridBagLayout)pnlSettings.getLayout()).rowWeights = new double[] { 0.0, 0.0, 1.0E-4 };
        pnlCheckRateSettings.setTitle("General Settings");
        pnlCheckRateSettings.setType(IconTitleBorderPanel.IconConst.Details);
        pnlCheckRateSettings.setIcon((Icon)new ImageIcon(this.getClass().getResource("/icon24/arrowsandnavigation/navigation2/navigate_up2.png")));
        pnlCheckRateSettings.setName("pnlCheckRateSettings");
        pnlCheckRateSettings.setLayout((LayoutManager)new GridBagLayout());
        ((GridBagLayout)pnlCheckRateSettings.getLayout()).columnWidths = new int[] { 0, 0 };
        ((GridBagLayout)pnlCheckRateSettings.getLayout()).rowHeights = new int[] { 0, 0 };
        ((GridBagLayout)pnlCheckRateSettings.getLayout()).columnWeights = new double[] { 1.0, 1.0E-4 };
        ((GridBagLayout)pnlCheckRateSettings.getLayout()).rowWeights = new double[] { 0.0, 1.0E-4 };
        pnlSettingsContent.setName("pnlSettingsContent");
        pnlSettingsContent.setLayout(new GridBagLayout());
        ((GridBagLayout)pnlSettingsContent.getLayout()).columnWidths = new int[] { 0, 0 };
        ((GridBagLayout)pnlSettingsContent.getLayout()).rowHeights = new int[] { 0, 0 };
        ((GridBagLayout)pnlSettingsContent.getLayout()).columnWeights = new double[] { 1.0, 1.0E-4 };
        ((GridBagLayout)pnlSettingsContent.getLayout()).rowWeights = new double[] { 0.0, 1.0E-4 };
        this.pnplOtherSettings.setBorder(new TitledBorder(""));
        this.pnplOtherSettings.setName("pnplOtherSettings");
        this.pnplOtherSettings.setLayout(new GridBagLayout());
        ((GridBagLayout)this.pnplOtherSettings.getLayout()).columnWidths = new int[] { 0, 0 };
        ((GridBagLayout)this.pnplOtherSettings.getLayout()).rowHeights = new int[] { 0, 0 };
        ((GridBagLayout)this.pnplOtherSettings.getLayout()).columnWeights = new double[] { 1.0, 1.0E-4 };
        ((GridBagLayout)this.pnplOtherSettings.getLayout()).rowWeights = new double[] { 1.0, 1.0E-4 };
        this.pnlOtherSettingsContent.setName("pnlOtherSettingsContent");
        this.pnlOtherSettingsContent.setLayout(new GridBagLayout());
        ((GridBagLayout)this.pnlOtherSettingsContent.getLayout()).columnWidths = new int[] { 107, 600, 0};
        ((GridBagLayout)this.pnlOtherSettingsContent.getLayout()).rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
        ((GridBagLayout)this.pnlOtherSettingsContent.getLayout()).columnWeights = new double[] { 0.0, 0.0, 1.0E-4 }; //center get 100% of remaining
        ((GridBagLayout)this.pnlOtherSettingsContent.getLayout()).rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4 };
        this.ckAutoFulfillOrders.setText("Automatically fulfill orders that are shipped in ShipExpress during sync");
        this.ckAutoFulfillOrders.setName("ckAutoFulfillOrders");
        this.pnlOtherSettingsContent.add(this.ckAutoFulfillOrders, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 5, 0), 0, 0));
        this.ckSetCartonCost.setText("Set shipping cost on the shipment carton");
        this.ckSetCartonCost.setName("ckSetCartonCost");
        this.pnlOtherSettingsContent.add(this.ckSetCartonCost, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 5, 0), 0, 0));
        this.ckPurchaseInsurance.setText("Purchase shipping insurance when the declared value is set on a carton");
        this.ckPurchaseInsurance.setName("ckPurchaseInsurance");
        this.pnlOtherSettingsContent.add(this.ckPurchaseInsurance, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 5, 0), 0, 0));
        this.ckShowZeroQtyWarning.setText("Show warning for zero weight items");
        this.ckShowZeroQtyWarning.setToolTipText("Show warning when getting a quote for an order with zero weight items.");
        this.ckShowZeroQtyWarning.setName("ckShowZeroQtyWarning");
        this.pnlOtherSettingsContent.add(this.ckShowZeroQtyWarning, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 5, 0), 0, 0));


        this.ckDebugMode.setText("Debug Mode");
        this.ckDebugMode.setName("ckDebugMode");
        this.pnlOtherSettingsContent.add(this.ckDebugMode, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));

        this.lblShippoApiKey.setText("Shippo API Key");
        this.lblShippoApiKey.setToolTipText("Key from Shippo");
        this.lblShippoApiKey.setName("lblShippoApiKey");
        this.txtShippoApiKey.setToolTipText("Shippo API Key");
        this.txtShippoApiKey.setName("txtShippoApiKey");
        //this.txtShippoApiKey.setPreferredSize(new Dimension(80,20));

        this.pnlOtherSettingsContent.add(this.lblShippoApiKey,new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 5, 0), 0, 0));
        this.pnlOtherSettingsContent.add(this.txtShippoApiKey, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 5, 0), 0, 0));


        this.pnplOtherSettings.add(this.pnlOtherSettingsContent, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 1, new Insets(5, 5, 5, 5), 0, 0));
        pnlSettingsContent.add(this.pnplOtherSettings, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        pnlCheckRateSettings.add((Component)pnlSettingsContent, (Object)new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 1, new Insets(5, 5, 5, 5), 0, 0));
        pnlSettings.add((Component)pnlCheckRateSettings, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 1, new Insets(10, 10, 5, 0), 0, 0));
        pnlCheckRateSettings2.setTitle("Ship Quote Button");
        pnlCheckRateSettings2.setType(IconTitleBorderPanel.IconConst.Details);
        pnlCheckRateSettings2.setIcon((Icon)new ImageIcon(this.getClass().getResource("/icon24/arrowsandnavigation/navigation2/navigate_up2.png")));
        pnlCheckRateSettings2.setName("pnlCheckRateSettings2");
        pnlCheckRateSettings2.setLayout((LayoutManager)new GridBagLayout());
        ((GridBagLayout)pnlCheckRateSettings2.getLayout()).columnWidths = new int[] { 0, 0 };
        ((GridBagLayout)pnlCheckRateSettings2.getLayout()).rowHeights = new int[] { 0, 0 };
        ((GridBagLayout)pnlCheckRateSettings2.getLayout()).columnWeights = new double[] { 1.0, 1.0E-4 };
        ((GridBagLayout)pnlCheckRateSettings2.getLayout()).rowWeights = new double[] { 0.0, 1.0E-4 };
        pnlSettingsContent2.setName("pnlSettingsContent2");
        pnlSettingsContent2.setLayout(new GridBagLayout());
        ((GridBagLayout)pnlSettingsContent2.getLayout()).columnWidths = new int[] { 0, 0 };
        ((GridBagLayout)pnlSettingsContent2.getLayout()).rowHeights = new int[] { 0, 0, 0, 0, 0 };
        ((GridBagLayout)pnlSettingsContent2.getLayout()).columnWeights = new double[] { 1.0, 1.0E-4 };
        ((GridBagLayout)pnlSettingsContent2.getLayout()).rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0E-4 };
        pnlCarrierRates.setBorder(new TitledBorder("Show Carrier Rates"));
        pnlCarrierRates.setName("pnlCarrierRates");
        pnlCarrierRates.setLayout(new GridBagLayout());
        ((GridBagLayout)pnlCarrierRates.getLayout()).columnWidths = new int[] { 0, 0 };
        ((GridBagLayout)pnlCarrierRates.getLayout()).rowHeights = new int[] { 0, 0 };
        ((GridBagLayout)pnlCarrierRates.getLayout()).columnWeights = new double[] { 1.0, 1.0E-4 };
        ((GridBagLayout)pnlCarrierRates.getLayout()).rowWeights = new double[] { 0.0, 1.0E-4 };
        pnlCarrierRatesContent.setName("pnlCarrierRatesContent");
        pnlCarrierRatesContent.setLayout(new GridBagLayout());
        ((GridBagLayout)pnlCarrierRatesContent.getLayout()).columnWidths = new int[] { 0, 0, 0, 0 };
        ((GridBagLayout)pnlCarrierRatesContent.getLayout()).rowHeights = new int[] { 0, 0, 0 };
        ((GridBagLayout)pnlCarrierRatesContent.getLayout()).columnWeights = new double[] { 0.0, 0.0, 1.0, 1.0E-4 };
        ((GridBagLayout)pnlCarrierRatesContent.getLayout()).rowWeights = new double[] { 0.0, 0.0, 1.0E-4 };
        pnlShowCarrierRatesInfo.setName("pnlShowCarrierRatesInfo");
        pnlShowCarrierRatesInfo.setLayout(new GridBagLayout());
        ((GridBagLayout)pnlShowCarrierRatesInfo.getLayout()).columnWidths = new int[] { 0, 0, 0 };
        ((GridBagLayout)pnlShowCarrierRatesInfo.getLayout()).rowHeights = new int[] { 0, 0 };
        ((GridBagLayout)pnlShowCarrierRatesInfo.getLayout()).columnWeights = new double[] { 0.0, 1.0, 1.0E-4 };
        ((GridBagLayout)pnlShowCarrierRatesInfo.getLayout()).rowWeights = new double[] { 0.0, 1.0E-4 };
        lblCarrierRateMessageHeader.setText("Note:");
        lblCarrierRateMessageHeader.setFont(lblCarrierRateMessageHeader.getFont().deriveFont(lblCarrierRateMessageHeader.getFont().getStyle() | 0x1));
        lblCarrierRateMessageHeader.setName("lblCarrierRateMessageHeader");
        pnlShowCarrierRatesInfo.add(lblCarrierRateMessageHeader, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 0, 5), 0, 0));
        lblCarrierRatesMessage.setText("Carriers need to be configured in your Fishbowl ShipExpress account.");
        lblCarrierRatesMessage.setName("lblCarrierRatesMessage");
        pnlShowCarrierRatesInfo.add(lblCarrierRatesMessage, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        pnlCarrierRatesContent.add(pnlShowCarrierRatesInfo, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 5, 0), 0, 0));
        this.ckFedEx.setText("FedEx");
        this.ckFedEx.setName("ckFedEx");
        pnlCarrierRatesContent.add(this.ckFedEx, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 0, 5), 0, 0));
        this.ckUPS.setText("UPS");
        this.ckUPS.setName("ckUPS");
        pnlCarrierRatesContent.add(this.ckUPS, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 0, 5), 0, 0));
        this.ckUSPS.setText("USPS");
        this.ckUSPS.setName("ckUSPS");
        pnlCarrierRatesContent.add(this.ckUSPS, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        pnlCarrierRates.add(pnlCarrierRatesContent, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 1, new Insets(5, 5, 5, 5), 0, 0));
        pnlSettingsContent2.add(pnlCarrierRates, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 5, 0), 0, 0));
        this.pnlShippingProduct.setBorder(new TitledBorder("Default Shipping Product"));
        this.pnlShippingProduct.setName("pnlShippingProduct");
        this.pnlShippingProduct.setLayout(new GridBagLayout());
        ((GridBagLayout)this.pnlShippingProduct.getLayout()).columnWidths = new int[] { 0, 0 };
        ((GridBagLayout)this.pnlShippingProduct.getLayout()).rowHeights = new int[] { 0, 0 };
        ((GridBagLayout)this.pnlShippingProduct.getLayout()).columnWeights = new double[] { 0.0, 1.0E-4 };
        ((GridBagLayout)this.pnlShippingProduct.getLayout()).rowWeights = new double[] { 0.0, 1.0E-4 };
        this.pnlShippingProductContent.setName("pnlShippingProductContent");
        this.pnlShippingProductContent.setLayout(new GridBagLayout());
        ((GridBagLayout)this.pnlShippingProductContent.getLayout()).columnWidths = new int[] { 0, 167, 0 };
        ((GridBagLayout)this.pnlShippingProductContent.getLayout()).rowHeights = new int[] { 0, 0 };
        ((GridBagLayout)this.pnlShippingProductContent.getLayout()).columnWeights = new double[] { 0.0, 0.0, 1.0E-4 };
        ((GridBagLayout)this.pnlShippingProductContent.getLayout()).rowWeights = new double[] { 0.0, 1.0E-4 };
        this.lblShippingProduct.setText("Shipping Product:");
        this.lblShippingProduct.setName("lblShippingProduct");
        this.pnlShippingProductContent.add(this.lblShippingProduct, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 1, new Insets(2, 5, 0, 5), 0, 0));
        this.cboShippingProduct.setDisplayName("Shipping Product");
        this.cboShippingProduct.setName("cboShippingProduct");
        this.pnlShippingProductContent.add((Component)this.cboShippingProduct, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 0, 5), 0, 0));
        this.pnlShippingProduct.add(this.pnlShippingProductContent, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 1, new Insets(5, 5, 5, 5), 0, 0));
        pnlSettingsContent2.add(this.pnlShippingProduct, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 5, 0), 0, 0));
        this.pnlRateMarkup.setTitle("Apply Rate Markup");
        this.pnlRateMarkup.setName("pnlRateMarkup");
        this.pnlRateMarkup.addMouseListener((MouseListener)new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                BrowserSettingsPanel.this.pnlRateMarkupMouseClicked();
            }
        });
        this.pnlRateMarkup.setLayout((LayoutManager)new GridBagLayout());
        ((GridBagLayout)this.pnlRateMarkup.getLayout()).columnWidths = new int[] { 0, 0 };
        ((GridBagLayout)this.pnlRateMarkup.getLayout()).rowHeights = new int[] { 0, 0 };
        ((GridBagLayout)this.pnlRateMarkup.getLayout()).columnWeights = new double[] { 0.0, 1.0E-4 };
        ((GridBagLayout)this.pnlRateMarkup.getLayout()).rowWeights = new double[] { 0.0, 1.0E-4 };
        pnlRateMarkupContent.setName("pnlRateMarkupContent");
        pnlRateMarkupContent.setLayout(new GridBagLayout());
        ((GridBagLayout)pnlRateMarkupContent.getLayout()).columnWidths = new int[] { 107, 104, 0 };
        ((GridBagLayout)pnlRateMarkupContent.getLayout()).rowHeights = new int[] { 0, 0, 0 };
        ((GridBagLayout)pnlRateMarkupContent.getLayout()).columnWeights = new double[] { 0.0, 0.0, 1.0E-4 };
        ((GridBagLayout)pnlRateMarkupContent.getLayout()).rowWeights = new double[] { 0.0, 0.0, 1.0E-4 };
        this.rbtnPercent.setText("Percent:");
        this.rbtnPercent.setName("rbtnPercent");
        this.rbtnPercent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                BrowserSettingsPanel.this.rbtnPercentActionPerformed();
            }
        });
        pnlRateMarkupContent.add(this.rbtnPercent, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 5, 5), 0, 0));
        this.txtPercent.setName("apPercentageFormattedTextField");
        this.txtPercent.setText("0%");
        this.txtPercent.setPreferredSize(new Dimension(30, 20));
        pnlRateMarkupContent.add(this.txtPercent, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 5, 0), 0, 0));
        this.rbtnRate.setText("Rate +");
        this.rbtnRate.setName("rbtnRate");
        this.rbtnRate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                BrowserSettingsPanel.this.rbtnRateActionPerformed();
            }
        });
        pnlRateMarkupContent.add(this.rbtnRate, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 0, 5), 0, 0));
        this.txtRate.setName("txtRate");
        pnlRateMarkupContent.add((Component)this.txtRate, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        this.pnlRateMarkup.add((Component)pnlRateMarkupContent, (Object)new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        pnlSettingsContent2.add((Component)this.pnlRateMarkup, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 10, 1, new Insets(2, 2, 7, 2), 0, 0));
        this.ckRetailOnly.setText("Get retail shipping rates when using the Ship Quote buttons on a sales order");
        this.ckRetailOnly.setName("ckRetailOnly");
        pnlSettingsContent2.add(this.ckRetailOnly, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        pnlCheckRateSettings2.add((Component)pnlSettingsContent2, (Object)new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 1, new Insets(5, 5, 5, 5), 0, 0));
        pnlSettings.add((Component)pnlCheckRateSettings2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 10, 1, new Insets(10, 10, 0, 0), 0, 0));
        this.tbPnlInfo.addTab("Plugin Settings", (Component)pnlSettings);
        this.add((Component)this.tbPnlInfo, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 1, new Insets(10, 10, 10, 10), 0, 0));
        final ButtonGroup buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(this.rbtnPercent);
        buttonGroup1.add(this.rbtnRate);
    }

    static {
        LOGGER = LoggerFactory.getLogger((Class)BrowserSettingsPanel.class);
    }
}
