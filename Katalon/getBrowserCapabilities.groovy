//Built in Katalon code
//This will get the browser type and OS type - FIREFOX_DRIVER or CHROME_DRIVER
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
log.logWarning(DriverFactory.getExecutedBrowser().getName())
log.logWarning(System.getProperty("os.name"))



//Custom Keyword with Import statements for more capabilities
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver
import org.openqa.selenium.remote.RemoteWebDriver;
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.driver.DriverFactory

public class Host {

	static WebDriver driver = DriverFactory.getWebDriver()

	@Keyword
	public static String details() {
		try {

			//Get Browser name and version.
			Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
			String browserName =    "Name of the Browser    : " +caps.getBrowserName()+ "\n";
			String browserVersion = "Browser Version        : " +caps.getVersion();

			java.net.InetAddress i = java.net.InetAddress.getLocalHost();
			String convertedToString = String.valueOf(i);
			convertedToString = "Selenium Automation Script run by Local Host Name/IP Address: " + i+ "\n";
			String details = "Name of the OS         : " + System.getProperty("os.name") + "\n" + "Version of the OS      : " + System.getProperty("os.version") + "\n" + "Architecture of the OS : " + System.getProperty("os.arch")+ "\n";
			return convertedToString+details+browserName+browserVersion;
		} catch (Exception e) {
			e.printStackTrace();
		}
		String x = "Unknown";
		return x;
	}
}
