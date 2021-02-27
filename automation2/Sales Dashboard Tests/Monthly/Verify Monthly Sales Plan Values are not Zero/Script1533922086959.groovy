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

/* The purpose of this test if to read Sales Plan figures and
 * verify they are not $0. It reads Sales Plan, Gross Profit Plan
 * and Margin Plan to make sure there hasn't been an import error
 * Updated 04-19-2018 11:45AM by Pete Wilson
 * Updated 08-10-2018 13:36PM by Pete Wilson
 * Updated 11-14-2018 13:57PM by Pete Wilson
 */

//The position of the Plan values is fixed regardless of user type. This value is used to read the Sales Detail table
int accordionValue=3

WebUI.navigateToUrl(GlobalVariable.baseurl + '/v3/sales_dashboard')

//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')

WebUI.click(findTestObject('ABC/Sales Dashboard/Monthly Sales Section/accordion-Monthly Sales Accordion', [('divIndex') : (GlobalVariable.dashboardAccordionValue+2)]))

//Because of slow page load, wait for the accordion to appear with the text Month To date Sales Detail
WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Monthly Sales Details/accordion-Month To Date Sales Detail Banner'),15)

//Check to make sure the Sales Dashboard page loads. Look for the text, Month To Date Sales Detail
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Month To Date Sales Detail')

//Read the Sales Plan figures from the Monthly Sales Detail
//Remove the text, then remove the $ and , to form a number
//Confirm that number is greater than $0

salesPlan = WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Detail Values for Daily-MTD-YTD', [('divIndex') : accordionValue, ('rowIndex') : 2])).replaceAll("[^0-9-]","").toInteger()

log.logWarning('The Sales Plan Value is: ' + salesPlan)

if (salesPlan <= 0) {
	log.logError('ERROR: The Sales Plan Value is $0 or less. A problem may have occurred with the import or the user has no budget')
}

//Read Gross Profit Plan value

grossProfitPlan = WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Detail Values for Daily-MTD-YTD', [('divIndex') : accordionValue, ('rowIndex') : 5])).replaceAll("[^0-9-]","").toInteger()

log.logWarning('The Gross Profit Plan value is: ' + grossProfitPlan)

if (grossProfitPlan <= 0) {
	log.logError('ERROR: The Gross Profit Plan Value is $0 or less. A problem may have occurred with the import or the user has no budget')
}

//Read Margin Plan value

marginPlan = WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Detail Values for Daily-MTD-YTD', [('divIndex') : accordionValue, ('rowIndex') : 8])).replaceAll("[^0-9-.]","").toDouble()

log.logWarning('The Margin Plan Value is: ' + marginPlan)

if (marginPlan <= 0) {
	log.logError('ERROR: The Margin Plan Value is $0 or less. A problem may have occurred with the import or the user has no budget')
}