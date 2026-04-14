package org.com.webUI;

import org.com.BaseFramework;
import org.openqa.selenium.WebDriver;

public class BasePage extends BaseFramework {

    private WebDriver driver;

    protected BasePage(WebDriver driver) {
        this.driver = driver;

    }

    public <TPage extends BasePage> TPage getPage(Class<TPage> page) {
        return GetInstance(page, driver);
    }
}
