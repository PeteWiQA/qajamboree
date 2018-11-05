//This is pretty bad as there is a terrible inconsistency between Chrome and Firefox
//Also note the year comes first when using Firefox
//Firefox:
WebUI.setText(findTestObject('Calendar'), '2017-10-30')
//Chrome:
WebUI.sendKeys(findTestObject('Calendar'), Keys.chord('10302017', Keys.ENTER, Keys.TAB))

/* These commands are not interchangeable. They do not work in the other browser and either don't pass the value
 * or generate an error. It's also a problem that the date has to be written 2 different ways
 * This might be a page to investigate later:
 * [How to handle calendar in Selenium Webdriver or Web table in Selenium](http://learn-automation.com/handle-calender-in-selenium-webdriver/)
 */

//This is from ABC:
//There is an oddity where the date has to be entered with the year first.
WebUI.sendKeys(findTestObject('Customer Profile/Tasks/input_due-date'), '2018-12-31')


//This will get around the problem with the different browsers and a datepicker
//It uses setText for Firefox and sendKeys for Chrome
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
browserName=DriverFactory.getExecutedBrowser().getName()
 if (browserName=="FIREFOX_DRIVER"){
	 WebUI.setText(findTestObject('dateField'), '2017-10-31')
 } else {
 	WebUI.sendKeys(findTestObject('dateField'), Keys.chord('10-31-2017', Keys.ENTER, Keys.TAB))
 }
