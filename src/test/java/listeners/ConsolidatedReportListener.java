package listeners;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.com.utilities.ExceptionHandler;
import org.com.webUI.constants.Constants;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Test;
import org.testng.util.Strings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

public class ConsolidatedReportListener implements ITestListener {
    private static final String FILE_NAME = "ConsolidatedReport.html";
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<ExtentTest>();
    private static Instant testStartTime;
    private static Instant testEndTime;
    private static Date today = new Date();
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
    private static String strDate = formatter.format(today);
    private static String OUTPUT_FOLDER = System.getProperty("user.dir") + "/" + Constants.REPORTS_FOLDER_PATH + strDate + "/";
    private static Map<String, ExtentTest> testMap = new HashMap<>();
    private static ExtentReports extent = init();
    private static final Logger logger = LogManager.getLogger(ConsolidatedReportListener.class);

    private static ExtentReports init() {
        var logFilePath = System.getProperty("LogFilePath");
        if (Strings.isNullOrEmpty(logFilePath)) {
            System.setProperty("LogFilePath", OUTPUT_FOLDER);
        } else {
            OUTPUT_FOLDER = logFilePath;
        }
        Path path = Paths.get(OUTPUT_FOLDER);
        // if directory exists?
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                // fail to create directory
                ExceptionHandler.log(e);
            }
        }
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter(OUTPUT_FOLDER + FILE_NAME);
        formatter = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm a");
        strDate = formatter.format(today);
        htmlReporter.config().setDocumentTitle("Consolidated Report");
        htmlReporter.config().setReportName("Consolidated Report " + strDate);
        htmlReporter.config().setTheme(Theme.STANDARD);

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setReportUsesManualConfiguration(true);

        return extent;
    }

    /**
     * Method to get the test execution duration
     *
     * @return test execution duration
     */
//    public static String getTestExecutionTime() {
//        var duration = new Duration(testStartTime, testEndTime);
//        long durationInHours = duration.getStandardHours();
//        long durationInMinutes = duration.getStandardMinutes();
//        long durationInSeconds = duration.getStandardSeconds();
//        return String.format("%dh %dm %ds", durationInHours, durationInMinutes % 60, durationInSeconds % 60);
//    }

    @Override
    public synchronized void onStart(ITestContext context) {
        testStartTime = Instant.now();
        logger.info("Test Suite started!");
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        testEndTime = Instant.now();
        logger.info("Test Suite is ending!");
        extent.flush();
        test.remove();
    }

    @Override
    public synchronized void onTestStart(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        if (testMap.containsKey(methodName)) {
            extent.removeTest(testMap.get(methodName));
        }
        logger.info(methodName + " started!" + "Start time:"+getTime(result.getStartMillis()));
        ExtentTest extentTest = extent.createTest(result.getMethod().getConstructorOrMethod().getMethod().getDeclaredAnnotation(Test.class).testName(),
                result.getMethod().getConstructorOrMethod().getMethod().getDeclaredAnnotation(Test.class).description());

        extentTest.assignCategory(result.getMethod().getConstructorOrMethod().getMethod().getDeclaredAnnotation(Test.class).suiteName());
        testMap.put(methodName, extentTest);
        test.set(extentTest);
        test.get().getModel().setStartTime(getTime(result.getStartMillis()));
    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        logger.info(result.getMethod().getMethodName() + " passed!"+ "End time:"+getTime(result.getEndMillis()));
        test.get().pass("Test Passed");
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        logger.error(result.getMethod().getMethodName() + " failed!"+ "End time:"+getTime(result.getEndMillis()));
        test.get().fail("Test Failed");
        try {
            var message = result.getThrowable().getMessage();
            logger.error(message);
            logger.error(Arrays.toString(result.getThrowable().getStackTrace()));
            test.get().fail(result.getThrowable().getMessage());
            test.get().fail(result.getThrowable());
        } catch (Exception ex) {
            logger.error(String.format("Exception occurred while fetching error logs:%s", ex.getMessage()));
        }
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        logger.error(result.getMethod().getMethodName() + " skipped!"+ "End time:"+getTime(result.getEndMillis()));
        test.get().skip("Test Skipped");
        try {
            var message = result.getThrowable().getMessage();
            logger.error(message);
            logger.error(Arrays.toString(result.getThrowable().getStackTrace()));
            test.get().skip(result.getThrowable().getMessage());
            test.get().skip(result.getThrowable());
        } catch (Exception ex) {
            logger.error(String.format("Exception occurred while fetching error logs:%s", ex.getMessage()));
        }
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
    }

    @Override
    public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.info("onTestFailedButWithinSuccessPercentage for " + result.getMethod().getMethodName());
    }

    /**
     * Get Time
     *
     * @param millis Milliseconds.
     * @return Date
     */
    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }
}
