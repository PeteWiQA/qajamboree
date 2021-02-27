import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory as CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as MobileBuiltInKeywords
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testcase.TestCaseFactory as TestCaseFactory
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testdata.TestDataFactory as TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository as ObjectRepository
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WSBuiltInKeywords
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUiBuiltInKeywords
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.WebDriver as WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import org.openqa.selenium.By as By
import org.openqa.selenium.WebElement as WebElement
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.annotation.Keyword as Keyword
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger

KeywordLogger log = new KeywordLogger()
/* The purpose of this script is to calculate the Margin % on the Daily Sales Detail - By Customer tab -->
 * For the test, the Customer Name, Sales, GP, and Margin are output to the log
 * The Margin is the Gross Profit (GP$) / Sales * 100 This is compared to the site.
 * In some cases, when values are zero, and NaN or Infinity is returned. If this is encountered
 * the calculatedMargin is set to 0.0. This should match the site value
 * Updated 04-11-2018 11:42AM by Pete Wilson
 * Updated 04-18-2018 12:34PM by Pete Wilson
 * Updated 08-10-2018 11:47AM by Pete Wilson
 */

xpath="//*[@id=\'sales_dash_table\']/tbody/tr"

WebUI.navigateToUrl(GlobalVariable.baseurl + '/v3/sales_dashboard')

//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')

WebUI.click(findTestObject('ABC/Sales Dashboard/Daily Sales Section/accordion-Daily Sales Accordion', [('divIndex') : GlobalVariable.dashboardAccordionValue]))

//Because of slow page load, wait for the accordion to appear with the text Daily Sales Detail
WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Daily Sales Details/accordion-Daily Sales Detail Banner'),15)

//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales Detail
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales Detail')

WebUI.click(findTestObject('ABC/Sales Dashboard/Links/link-By Customer'))

WebUI.delay(2)

int totalRowCount=CustomKeywords.'tools.commonCode.countRowsPerPage'(xpath)
log.logWarning('Rows:=' + totalRowCount)

//Loop through the table and output the information to Log File
//Read columns 1-4, assign each to a variable, then output the result to the Log File

for (int loop = 1; loop <= totalRowCount; loop++) {

	customerName=WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-By Customer Total Values for Daily-MTD-YTD', [('row') : loop, ('column') : 1]))

    log.logWarning('Customer Name:=' + customerName)

    dailySales = WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-By Customer Total Values for Daily-MTD-YTD', [('row') : loop, ('column') : 2])).replaceAll("[^0-9-]","").toDouble()
	
    log.logWarning('Sales Figure Amount:=' + dailySales)

    dailyGP = WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-By Customer Total Values for Daily-MTD-YTD', [('row') : loop, ('column') : 3])).replaceAll("[^0-9-]","").toDouble()

    log.logWarning('Gross Profit:=' + dailyGP)

    dailyMargin = WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-By Customer Total Values for Daily-MTD-YTD', [('row') : loop, ('column') : 4])).replaceAll("[^0-9.-]","").toDouble()

    log.logWarning('Margin % :=' + dailyMargin)

	double calculatedMargin=((dailyGP / dailySales) * 100).round(1)
	//If the result is NaN, set the calculateMargin value to 0.0
	if ((calculatedMargin.isNaN()==true) || (calculatedMargin.isInfinite()==true)){
		calculatedMargin=0.0
	}

	log.logWarning('Margin % for ' + customerName + ' is ' + calculatedMargin + ' --- The site value is ' + dailyMargin)

	//If the Calculated Margin doesn't equal the Site Margin, determine the difference. In many cases, it's small and is due to rounding and isn't an error
	if (calculatedMargin!=dailyMargin){
	log.logWarning('ERROR: Margin values DO NOT match. Margin % for ' + customerName + ' is ' + calculatedMargin + ' --- The site value is ' + dailyMargin)
		if (Math.abs(calculatedMargin-dailyMargin)<=1){
			log.logWarning('NOTE:-- The difference between the calculated and site values is less than 1%, which is most likely a rounding issue')
		} else {
			log.logError('ERROR: Margin values DO NOT match. Margin % for ' + customerName + ' is ' + calculatedMargin + ' --- The site value is ' + dailyMargin)
			KeywordUtil.markError('ERROR: Margin values DO NOT match. Margin % for ' + customerName + ' is ' + calculatedMargin + ' --- The site value is ' + dailyMargin)
		}
	}
}
