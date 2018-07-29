// 
// Decompiled by Procyon v0.5.30
// 

package com.fbi.plugins.briteideas;

import java.util.HashMap;

import com.fbi.plugins.briteideas.components.CommerceBrowser;
import com.fbi.plugins.briteideas.exception.CommerceException;
import com.fbi.plugins.briteideas.util.property.Property;
import org.slf4j.LoggerFactory;

import java.awt.BorderLayout;
import com.teamdev.jxbrowser.chromium.dom.DOMElement;
import com.teamdev.jxbrowser.chromium.dom.DOMDocument;
import com.teamdev.jxbrowser.chromium.dom.internal.InputElement;
import com.fbi.commerce.shipping.util.CommerceUtil;
import com.teamdev.jxbrowser.chromium.dom.By;
import com.evnt.util.Util;

import java.util.stream.Collectors;
import java.util.Collection;
import java.util.stream.Stream;
import java.util.Collections;

import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.events.ScriptContextEvent;
import com.teamdev.jxbrowser.chromium.events.ScriptContextListener;
import com.teamdev.jxbrowser.chromium.BrowserContext;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import com.teamdev.jxbrowser.chromium.Browser;
import java.util.List;
import java.awt.Component;
import java.util.Map;
import org.slf4j.Logger;
import javax.swing.JPanel;

public class BrowserPanel extends JPanel
{
    private static final Logger LOGGER;
    private static final Map<Component, List<Browser>> ownerToBrowsers;
    private static final String SERVER_LOGIN;
    private static final String SERVER_SIGNUP;
    public static final String DEMO_URL;
    public static final String SERVER_PAGE;
    public static final String SETTINGS_PAGE;
    private Browser browser;
    private BrowserView browserView;
    private upcPlugin plugin;
    
    public BrowserPanel() {
        this.initComponents();
        this.gcBrowser();
    }

    BrowserPanel(final upcPlugin plugin, final BrowserContext browserContext, final String pageUrl) {
        this();
        this.plugin = plugin;
        this.initBrowser(browserContext, pageUrl);
    }

    private void gcBrowser() {
        BrowserPanel.ownerToBrowsers.entrySet().stream().filter(entry -> entry.getKey().getParent() == null).forEach(entry -> entry.getValue().forEach(Browser::dispose));
        BrowserPanel.ownerToBrowsers.entrySet().removeIf(entry -> entry.getValue().stream().anyMatch(Browser::isDisposed));
    }

    private void initBrowser(BrowserContext browserContext, final String pageUrl) {
        this.browser = new CommerceBrowser(browserContext);
        this.browser.addScriptContextListener(new ScriptContextListener() {
            public void onScriptContextCreated(ScriptContextEvent scriptContextEvent) {
                BrowserPanel.this.browser.executeJavaScript("window.__isPlugin__ = true;");
            }

            public void onScriptContextDestroyed(ScriptContextEvent scriptContextEvent) {
            }
        });
        this.browser.addLoadListener(new LoadAdapter() {
            public void onFinishLoadingFrame(FinishLoadingEvent finishLoadingEvent) {
                //BrowserPanel.this.browserAutoLogin(finishLoadingEvent); //remove auto login
                if (BrowserPanel.SETTINGS_PAGE.equals(pageUrl) && !finishLoadingEvent.getValidatedURL().equals(pageUrl)) {
                    finishLoadingEvent.getBrowser().loadURL(pageUrl);
                }

            }
        });
        this.browserView = new BrowserView(this.browser);
        this.initializeBrowser(pageUrl);
        ownerToBrowsers.merge(this.plugin, Collections.singletonList(this.browser), (x, y) -> {
            return (List)Stream.of(x, y).flatMap(Collection::stream).collect(Collectors.toList());
        });
    }
    
    private void initializeBrowser(final String pageUrl) {
        this.removeAll();
        this.browser.getCacheStorage().clearCache();
        this.browser.getCookieStorage().deleteAll();
        this.browser.loadURL(pageUrl);
        this.add((Component)this.browserView, "Center");
    }
    
    private void browserAutoLogin(final FinishLoadingEvent finishLoadingEvent) {
        try {
            final long second = 1000L;
            Thread.sleep(1000L);
        }
        catch (InterruptedException e) {
            BrowserPanel.LOGGER.error("Thread interrupted", (Throwable)e);
        }
        String username = Property.USERNAME.get(this.plugin);
        String password = Property.PASS.get(this.plugin);
        if (!BrowserPanel.SERVER_LOGIN.equals(finishLoadingEvent.getValidatedURL()) || Util.isEmpty(username) || Util.isEmpty(password)) {
            return;
        }
        final DOMDocument document = finishLoadingEvent.getBrowser().getDocument();
        final DOMElement emailElement = document.findElement(By.name("email"));
        if (emailElement != null) {
            try {
                username = CommerceUtil.decrypt(username);
                password = CommerceUtil.decrypt(password);
            }
            catch (CommerceException e2) {
                BrowserPanel.LOGGER.error(e2.getMessage(), (Throwable)e2);
                return;
            }
            ((InputElement)emailElement).setValue(username);
            final DOMElement passwordElement = document.findElement(By.name("password"));
            ((InputElement)passwordElement).setValue(password);
            this.browser.executeJavaScript("(function() {   var scope = angular.element($('.input')[0]).scope();   scope.$apply(function() {     scope.user.email = $('input[name=email]')[0].value;     scope.user.password = $('input[name=password]')[0].value;   }); })();");
            document.findElement(By.name("btnSubmit")).click();
        }
    }
    
    void showSignupPage() {
        this.browser.loadURL(BrowserPanel.SERVER_SIGNUP);
    }
    
    void showLoginPage() {
        this.browser.loadURL(BrowserPanel.SERVER_PAGE);
    }
    
    void reload() {
        this.browser.reload();
    }
    
    void forward() {
        this.browser.goToIndex(this.browser.getCurrentNavigationEntryIndex() + 1);
    }
    
    void back() {
        final int currentIndex = this.browser.getCurrentNavigationEntryIndex();
        if (currentIndex > 1) {
            this.browser.goToIndex(currentIndex - 1);
        }
    }
    
    private void initComponents() {
        this.setName("this");
        this.setLayout(new BorderLayout());
    }
    
    static {
        LOGGER = LoggerFactory.getLogger((Class)BrowserPanel.class);
        ownerToBrowsers = new HashMap<Component, List<Browser>>();
        DEMO_URL = System.getenv("MALCOLM");
        if (BrowserPanel.DEMO_URL != null) {
            SERVER_LOGIN = BrowserPanel.DEMO_URL + "/";
            SERVER_PAGE = BrowserPanel.DEMO_URL + "/shipping";
            SERVER_SIGNUP = BrowserPanel.DEMO_URL + "/shipsignup";
            SETTINGS_PAGE = BrowserPanel.DEMO_URL + "/shipping-settings/account-setup";
        }
        else {
            SERVER_LOGIN = "https://app.fishbowlcommerce.com/";
            SERVER_PAGE = "https://app.fishbowlcommerce.com/shipping";
            SERVER_SIGNUP = "https://app.fishbowlcommerce.com/shipsignup";
            SETTINGS_PAGE = "https://app.fishbowlcommerce.com/shipping-settings/account-setup";
        }
    }
}
