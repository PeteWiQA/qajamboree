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

/* The purpose of this test is to confirm the daily sales plan is not zero
 * Read the Daily Sales value from the Daily Sales Detail of the Sales Dashboard
 * It reads the Sales Plan, Gross Profit Plan and Margin Plan
 * For a user with a budget, this should be greater than $0
 * Updated 04-18-2018 13:21PM by Pete Wilson
 * Updated 08-10-2018 11:42AM by Pete Wilson
 * Updated 11-14-2018 13:14PM by Pete Wilson
 */

WebUI.navigateToUrl(GlobalVariable.baseurl + '/v3/sales_dashboard')

//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')

log.logWarning('--- Daily Sales ---')

WebUI.click(findTestObject('ABC/Sales Dashboard/Daily Sales Section/accordion-Daily Sales Accordion', [('divIndex') : GlobalVariable.dashboardAccordionValue]))

//Because of slow page load, wait for the accordion to appear with the text Daily Sales Detail
WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Daily Sales Details/accordion-Daily Sales Detail Banner'),15)

//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales Detail
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales Detail')

//Read the Sales Plan figures from the Daily Sales Detail and confirm that number is greater than $0

salesPlan=WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Details Row Values - Daily-MTD-YTD', [('index') : 2])).replaceAll("[^0-9-]","").toInteger()

log.logWarning('The Sales Plan Value is: ' + salesPlan)

if (salesPlan <= 0) {
	log.logError('ERROR: The Sales Plan Value is $0 or less. A problem may have occurred with the import')
} else {
	log.logWarning('SUCCESS: The Sales Plan value appears correct.')
}

//Read Gross Profit Plan value

grossProfitPlan = WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Details Row Values - Daily-MTD-YTD', [('index') : 5])).replaceAll("[^0-9-]","").toInteger()

log.logWarning('The Gross Profit Plan Value is: ' + grossProfitPlan)

if (grossProfitPlan <= 0) {
	log.logError('ERROR: The Gross Profit Plan Value is $0 or less. A problem may have occurred with the import')
} else {
	log.logWarning('SUCCESS: The Gross Profit Plan value appears correct.')
}

//Read Margin Plan value

marginPlan = WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Details Row Values - Daily-MTD-YTD', [('index') : 8])).replaceAll("[^0-9.-]","").toDouble()

log.logWarning('The Margin Plan Value is: ' + marginPlan)

if (marginPlan <= 0) {
	log.logError('ERROR: The Margin Plan Value is $0 or less. A problem may have occurred with the import')
} else {
	log.logWarning('SUCCESS: The Margin Plan Value appears correct.')
}
