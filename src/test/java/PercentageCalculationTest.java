
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class PercentageCalculationTest {
    private AndroidDriver<MobileElement> androidDriver;
    private IOSDriver<MobileElement> iosDriver;
    private boolean isAndroid = true; // Change to false if testing on iOS

    @BeforeClass
    public void setup() throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();

        if (isAndroid) {
            caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
            caps.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
            caps.setCapability(MobileCapabilityType.APP, "path/to/your/android/app.apk");
            androidDriver = new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), caps);
        } else {
            caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            caps.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone Simulator");
            caps.setCapability(MobileCapabilityType.APP, "path/to/your/ios/app.app");
            iosDriver = new IOSDriver<>(new URL("http://localhost:4723/wd/hub"), caps);
        }
    }

    @Test
    public void testPercentageCalculation() throws IOException {
        MobileElement inputField;
        MobileElement calculateButton;
        MobileElement resultField;

        if (isAndroid) {
            inputField = androidDriver.findElementById("com.example:id/inputField");
            calculateButton = androidDriver.findElementById("com.example:id/calculateButton");
            resultField = androidDriver.findElementById("com.example:id/resultField");
        } else {
            inputField = iosDriver.findElementByAccessibilityId("inputField");
            calculateButton = iosDriver.findElementByAccessibilityId("calculateButton");
            resultField = iosDriver.findElementByAccessibilityId("resultField");
        }

        // Input calculation formula
        inputField.sendKeys("15.5% of 200");

        // Take a screenshot before calculation
        takeScreenshot("before_calculation");

        // Perform calculation
        calculateButton.click();

        // Take a screenshot after calculation
        takeScreenshot("after_calculation");

        // Validate result
        String result = resultField.getText();
        Assert.assertEquals(result, "31", "Calculation result is incorrect!");

        // Save DOM source
        saveDOMSource("dom_source.html");
    }

    private void takeScreenshot(String fileName) throws IOException {
        File screenshot = ((TakesScreenshot) (isAndroid ? androidDriver : iosDriver)).getScreenshotAs(OutputType.FILE);
        File destination = new File(fileName + ".png");
        org.apache.commons.io.FileUtils.copyFile(screenshot, destination);
    }

    private void saveDOMSource(String fileName) throws IOException {
        String pageSource = (isAndroid ? androidDriver : iosDriver).getPageSource();
        FileWriter writer = new FileWriter(fileName);
        writer.write(pageSource);
        writer.close();
    }

    @AfterClass
    public void teardown() {
        if (isAndroid) {
            androidDriver.quit();
        } else {
            iosDriver.quit();
        }
    }
}
