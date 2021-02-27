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
import com.kms.katalon.core.annotation.Keyword as Keyword
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger
import com.kms.katalon.core.util.KeywordUtil

KeywordLogger log = new KeywordLogger()

/* This test reads the Sales Plan value from the YTD Sales Detail page.
 * This value is compared to the Sales Plan Total from the Footer of the page
 * Created 03-28-2018 09:37AM by Pete Wilson
 * Updated 08-13-2018 11:54AM by Pete Wilson
 * Updated 11-14-2018 16:04PM by Pete Wilson
 * 
 */

WebUI.navigateToUrl(GlobalVariable.baseurl + '/v3/sales_dashboard')
//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')

WebUI.click(findTestObject('ABC/Sales Dashboard/Yearly Sales Section/accordion-YTD Sales Accordion', [('divIndex') : (GlobalVariable.dashboardAccordionValue+4)]))

//Because of slow page load, wait for the accordion to appear with the text Year To date Sales Detail
WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/accordion-Year To Date Sales Detail Banner'),15)

//Check to make sure the Sales Dashboard page loads. Look for the text, Year To Date Sales Detail
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Year To Date Sales Detail')

WebUI.delay(2)

WebUI.click(findTestObject('ABC/Sales Dashboard/Links/link-By Customer'))

WebUI.delay(2)
//Wait for the Footer Totals to appear. This should give the page time to load
WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Customer/table-Footer Row Totals', [('column') : 2]), 15)

headerSalesPlanTotal=WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Details Row Values - Daily-MTD-YTD', [('index') : 2])).replaceAll("[^0-9-]","").toInteger()

//Read Totals from the bottom of the table (footer)
footerSalesPlanTotal=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Customer/table-Footer Row Totals', [('column') : 3])).replaceAll("[^0-9-]","").toInteger()

log.logWarning('Sales Plan from Header Section:=' +headerSalesPlanTotal )
log.logWarning('Sales Plan from Footer Section:=' +footerSalesPlanTotal )

if (headerSalesPlanTotal!=footerSalesPlanTotal){
	log.logError('ERROR: The Sales Plan Header value does not match the Sales Plan Footer total')
	KeywordUtil.markFailed('ERROR: The Sales Plan Header value does not match the Sales Plan Footer total')
} else {
	log.logWarning('SUCCESS: The Sales Plan Header value is a match to the Sales Plan Footer total')
}