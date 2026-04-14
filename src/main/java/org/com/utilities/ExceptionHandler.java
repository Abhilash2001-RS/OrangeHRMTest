package org.com.utilities;

import org.com.BaseFramework;
import org.openqa.selenium.WebDriverException;

import java.util.Arrays;

public class ExceptionHandler extends BaseFramework {

    /**
     * Log Exception.
     *
     * @param e Exception.
     */
    public static void log(Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append("<textarea>" + e.getMessage());
        Arrays.stream(e.getStackTrace()).forEach(st -> sb.append("\n\t at " + st));
        sb.append("</textarea>");
        logger().fail(sb.toString());
    }

    /**
     * Log Exception.
     *
     * @param throwable Throwable Exception.
     */
    public static void log(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        sb.append("<textarea>" + throwable.getMessage());
        Arrays.stream(throwable.getStackTrace()).forEach(st -> sb.append("\n\t at " + st));
        sb.append("</textarea>");
        logger().fail(sb.toString());
    }

    /**
     * Log Exception along with message.
     *
     * @param e       Exception.
     * @param message Message to be logged.
     */
    public static void log(Exception e, String message) {
        logger().fail("<b>" + message + "</b>/n" + e);
    }

    /**
     * Log WebDriverException, Log Fail if any other exception.
     *
     * @param e Exception.
     */
    public static void logAndContinueWebDriverExceptions(Exception e) {
        if (e instanceof WebDriverException) {
            logger().info(e);
        } else {
            logger().fail(e);
        }
    }

    /**
     * Log along with message WebDriverException, Log Fail if any other exception.
     *
     * @param e       Exception.
     * @param message Message to be logged.
     */
    public static void logAndContinueWebDriverExceptions(Exception e, String message) {
        if (e instanceof WebDriverException) {
            logger().info("<b>" + message + "</b>/n" + e);
        } else {
            logger().fail("<b>" + message + "</b>/n" + e);
        }
    }

    /**
     * Log NullPointerException, Log Fail if any other exception.
     *
     * @param e Exception.
     */
    public static void logAndContinueNullPointerException(Exception e) {
        if (e instanceof NullPointerException) {
            logger().info(e);
        } else {
            logger().fail(e);

        }
    }

    /**
     * Log along with message NullPointerException, Log Fail if any other exception.
     *
     * @param e       Exception.
     * @param message Message to be logged.
     */
    public static void logAndContinueNullPointerException(Exception e, String message) {
        if (e instanceof NullPointerException) {
            logger().info("<b>" + message + "</b>/n" + e);
        } else {
            logger().fail("<b>" + message + "</b>/n" + e);
        }
    }
}
