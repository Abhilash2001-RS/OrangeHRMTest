import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import listeners.ConsolidatedReportListener;
import org.com.BaseFramework;
import org.com.utilities.helper.EnvironmentHelper;
import org.com.webUI.constants.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.testng.util.Strings;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class BaseTest extends BaseFramework {

    WebDriverWait wait;

    @BeforeSuite
    public void beforeSuite() {
        EnvironmentHelper.loadProperties();
        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
        String strDate = formatter.format(today);
        String logFilePath = System.getProperty("LogFilePath");
        if (Strings.isNullOrEmpty(logFilePath)) {
            logFilePath = Paths.get(userDir, Constants.REPORTS_FOLDER_PATH, strDate, "/").toString();
            System.setProperty("LogFilePath", logFilePath);
        }
        String logFileLocation = Paths.get(logFilePath, "AutomationReport.html").toString();
        formatter = new SimpleDateFormat("dd MMMM yyyy");
        strDate = formatter.format(today);

        htmlReporter = new ExtentSparkReporter(logFileLocation);
        htmlReporter.config().setDocumentTitle("Automation Report " + strDate);
        htmlReporter.config().setReportName("Automation Report " + strDate);
        htmlReporter.config().setTheme(Theme.STANDARD);

        report = new ExtentReports();
        report.attachReporter(htmlReporter);
        System.out.println("Report will be created at: " + logFileLocation);
    }

    @BeforeMethod(alwaysRun = true)
    public void browserSetup(Method method) throws MalformedURLException {

        var testName = method.getAnnotation(Test.class).testName();
        var suiteName = method.getAnnotation(Test.class).suiteName();

        chromeOptions = new ChromeOptions();
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        chromeOptions.setAcceptInsecureCerts(true);
        chromeOptions.setCapability("webSocketUrl", true);

        String browserName = chromeOptions.getBrowserName();
        System.out.println(browserName);
        chromeOptions.setBrowserVersion("latest");
        driver = new ChromeDriver(chromeOptions);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(EnvironmentHelper.getPropertyValue("url"));
        wait = new WebDriverWait(driver, Duration.ofSeconds(3000));
        actions = new Actions(driver);
        System.out.println("BeforeClass");

        ExtentTest extentTest = startTest(testName, this.getClass().getName());
        test.set(extentTest);
        extentTest.assignCategory(suiteName);
    }

    @AfterMethod
    public void logout(){
        driver.findElement(By.cssSelector(".oxd-icon.bi-caret-down-fill.oxd-userdropdown-icon")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[normalize-space()='Logout']")));
        driver.findElement(By.xpath("//a[normalize-space()='Logout']")).click();
        System.out.println("logout");
    }

    @AfterClass(alwaysRun = true)
    public void closeBrowser() {
        driver.quit();

        System.out.println("Browser closed after all tests in class");
        System.out.println("AfterClass");
    }

    @AfterSuite
    public void globalTeardown() {
        System.out.println("=== Test Suite Completed ===");
        System.out.println("AfterSuite");
    }

}
