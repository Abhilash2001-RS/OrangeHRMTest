import listeners.ConsolidatedReportListener;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.*;
import DataProvider.TestDataProvider;

import java.time.Duration;

@Listeners(ConsolidatedReportListener.class)
public class OrangeHRM extends BaseTest
{
   @Test(testName = "Login", dataProvider = "loginData", dataProviderClass = TestDataProvider.class)
    public void login(String username, String password) {
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector("[type='submit']")).click();
        driver.findElement(By.xpath("//a[normalize-space()='Admin']")).click();
    }

    //@Test
    public void flipkartLogin() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".b3wTlE")));
        WebElement close = driver.findElement(By.cssSelector(".b3wTlE"));
        if (close.isDisplayed()) {
            close.click();
        }
        driver.findElement(By.name("q")).click();
        WebElement loginButton = driver.findElement(By.xpath("//span[normalize-space()='Login']"));
        new Actions(driver).moveToElement(loginButton)
                .pause(Duration.ofSeconds(1)).build().perform();

        System.out.println("Actions done");
    }

}