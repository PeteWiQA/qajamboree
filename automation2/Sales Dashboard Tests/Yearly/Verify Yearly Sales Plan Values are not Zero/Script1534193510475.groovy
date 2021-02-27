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

/*This script looks at the Year to Date Sales Details to verify the Plan values are greater than $0
 * A Plan value of $0 normally indicates a problem with the import or other data related problem
 * Once a budget has been created and submitted the plan should be a calculated field and unless sales are zero
 * or the user is brand new, the Plan should be a postive value
 * Updated 04-19-2018 12:14PM by Pete Wilson
 * Updated 08-13-2018 15:24PM by Pete Wilson
 */

log.logWarning('--- Begin Confirm Yearly Sales Plan Values are not Zero ---')

WebUI.navigateToUrl(GlobalVariable.baseurl + '/v3/sales_dashboard')
//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')

WebUI.click(findTestObject('ABC/Sales Dashboard/Yearly Sales Section/accordion-YTD Sales Accordion', [('divIndex') : (GlobalVariable.dashboardAccordionValue+4)]))

//Because of slow page load, wait for the accordion to appear with the text Year To date Sales Detail
WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/accordion-Year To Date Sales Detail Banner'),15)

//Check to make sure the Sales Dashboard page loads. Look for the text, Year To Date Sales Detail
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Year To Date Sales Detail')

//Read the Sales Plan figures from the Yearly Sales Detail
//Remove the text, then remove the $ and , to form a number
//Confirm that number is greater than $0
salesPlan=WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Details Row Values - Daily-MTD-YTD', [('index') : 2])).replaceAll("[^0-9-]","").toInteger()

log.logWarning('The Sales Plan Value is ' + salesPlan)

if (salesPlan <= 0) {
	log.logError('ERROR: The Sales Plan Value is $0 or less. A problem may have occurred with the import')
}

grossProfitPlan=WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Details Row Values - Daily-MTD-YTD', [('index') : 5])).replaceAll("[^0-9-]","").toInteger()

log.logWarning('The Gross Profit Plan value is ' + grossProfitPlan)

if (grossProfitPlan <= 0) {
	log.logError('ERROR: The Gross Profit Plan Value is $0 or less. A problem may have occurred with the import')
}

marginPlan=WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Details Row Values - Daily-MTD-YTD', [('index') : 8])).replaceAll("[^0-9.-]","").toDouble()

log.logWarning('The Margin Plan Value is ' + marginPlan)

if (marginPlan <= 0) {
	log.logError('ERROR: The Margin Plan Value is 0 or less. A problem may have occurred with the import')
}