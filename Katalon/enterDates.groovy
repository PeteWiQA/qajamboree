//This will get around the problem with the different browsers and a datepicker
//It uses setText for Firefox and sendKeys for Chrome
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
browserName=DriverFactory.getExecutedBrowser().getName()
 if (browserName=="FIREFOX_DRIVER"){
	 WebUI.setText(findTestObject('dateField'), '2017-10-31')
 } else {
 	WebUI.sendKeys(findTestObject('dateField'), Keys.chord('10-31-2017', Keys.ENTER, Keys.TAB))
 }
