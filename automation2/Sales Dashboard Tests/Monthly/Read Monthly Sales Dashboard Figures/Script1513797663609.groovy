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
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger

KeywordLogger log = new KeywordLogger()

/* The purpose of this test is to read the Monthly Sales Dashboard Figures
 * This is a read-only tests to confirm the page can be loaded and the Sales Rep
 * has data. It can be used as a Before/After validation check
 * Updated 04-19-2018 11:47AM by Pete Wilson
 * Updated 08-10-2018 16:20PM by Pete Wilson
 * Updated 11-14-2018 14:41PM by Pete Wilson
 */

WebUI.navigateToUrl(GlobalVariable.baseurl + '/v3/sales_dashboard')
//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')

WebUI.click(findTestObject('ABC/Sales Dashboard/Monthly Sales Section/accordion-Monthly Sales Accordion', [('divIndex') : (GlobalVariable.dashboardAccordionValue+2)]))

//Because of slow page load, wait for the accordion to appear with the text Month To date Sales Detail
WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Monthly Sales Details/accordion-Month To Date Sales Detail Banner'),15)

//Check to make sure the Sales Dashboard page loads. Look for the text, Month To Date Sales Detail
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Month To Date Sales Detail')

//Read the top section of the Monthly Sales Details Page
log.logWarning('--Monthly Sales Details---')

for (loop = 1; loop <= 9; loop++) {
	
	monthlySalesDetails=WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Details Row Values - Daily-MTD-YTD', [('index') : loop])).replaceAll('\\n|\\r', ' - ')
	log.logWarning('Monthly-- ' + monthlySalesDetails)
}