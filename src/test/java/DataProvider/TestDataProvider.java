package DataProvider;

import org.com.utilities.helper.ExcelReader;
import org.testng.annotations.DataProvider;

import java.io.IOException;

public class TestDataProvider {

    @DataProvider(name = "loginData")
    public static Object[][] loginTest() {

        return new Object[][]{
                {"Admin", "admin123"}
                //,{"manager",  "manager123"},
                // {"employee", "emp123"}};

        };
    }

    @DataProvider(name = "searchData")
    public static Object[][] getSearchData() {
        return new Object[][] {
                {"iPhone"},
                {"Samsung"},
                {"Laptop"}
        };
    }

    @DataProvider(name = "loginExcel")
    public Object[][] ExcelReader() throws IOException
    {
        return ExcelReader.getData( (System.getProperty("user.dir") + "/src/test/resources/TestData.xlsx")  , "Logindata");

    }

}
