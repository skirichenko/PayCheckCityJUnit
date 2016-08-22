import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;


@RunWith(Parameterized.class)
public class PayCheckCalculator {
    private static WebDriver driver;
    private String calcDate;
    private String state;
    private String grossPayAmount;
    private String grossPayMethodType;
    private String payFrequencyType;
    private String expectedNetPay;

    @Parameterized.Parameters
    public static Collection testData() {
        return Arrays.asList(
                new Object[][] {
                        {"12/31/2016", "Alaska", "1999", "Pay Per Period", "Weekly", "$1,422.37"},
                        {"12/31/2017", "Hawaii", "75000", "Annually", "Annual", "$49,701.43"},
                }
        );
    }
    public PayCheckCalculator(String calcDate, String state, String grossPayAmount, String grossPayMethodType, String payFrequencyType, String expectedNetPay) {
        this.calcDate = calcDate;
        this.state = state;
        this.grossPayAmount = grossPayAmount;
        this.grossPayMethodType = grossPayMethodType;
        this.payFrequencyType = payFrequencyType;
        this.expectedNetPay = expectedNetPay;
    }

    @BeforeClass

    public static void setUp() throws Exception {

        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10L, TimeUnit.SECONDS);
    }

    @Test
    public void netPay() throws Exception {

        driver.get("http://www.paycheckcity.com/calculator/salary/");
        driver.findElement(By.id("calcDate")).clear();
        driver.findElement(By.id("calcDate")).sendKeys(calcDate);

        driver.findElement(By.id("state")).clear();
        driver.findElement(By.id("state")).sendKeys(state);
        driver.findElement(By.id("generalInformation.grossPayAmount")).clear();
        driver.findElement(By.id("generalInformation.grossPayAmount")).sendKeys(grossPayAmount);
        driver.findElement(By.id("generalInformation.grossPayMethodType")).clear();
        driver.findElement(By.id("generalInformation.grossPayMethodType")).sendKeys(grossPayMethodType);
        driver.findElement(By.id("generalInformation.payFrequencyType")).clear();
        driver.findElement(By.id("generalInformation.payFrequencyType")).sendKeys(payFrequencyType);
        driver.findElement(By.id("calculate")).click();

        new WebDriverWait(driver, 20L).until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath(".//span[text()='Net Pay']/following-sibling::span")),expectedNetPay));

        WebElement result = driver.findElement(By.xpath(".//span[text()='Net Pay']/following-sibling::span"));
        Double actualNetPay = Double.parseDouble(result        .getText().replace("$", "").replace(",", ""));
        Double expected     = Double.parseDouble(expectedNetPay.replace("$", "").replace(",", ""));

        System.out.println(result);
        System.out.print(actualNetPay);
        System.out.print(expectedNetPay);

        assertThat(expected, is(closeTo(actualNetPay, 5)));

    }

    @AfterClass

    public static void tearDown() throws Exception {
        driver.quit();
    }

}

