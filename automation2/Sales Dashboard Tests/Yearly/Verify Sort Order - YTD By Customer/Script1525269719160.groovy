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
import com.kms.katalon.core.annotation.Keyword as Keyword
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger

KeywordLogger log = new KeywordLogger()

/* The purpose of this test is to click the By Customer tab
 * and verify the sort order for Customer Name and Sales 
 *
 * Created 05-02-2018 10:03AM by Pete Wilson
 * Updated 08-13-2018 16:28PM by Pete Wilson
 * Updated 11-14-2018 15:58PM by Pete Wilson
 */


WebUI.navigateToUrl(GlobalVariable.baseurl + '/v3/sales_dashboard')
//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')

WebUI.click(findTestObject('ABC/Sales Dashboard/Yearly Sales Section/accordion-YTD Sales Accordion', [('divIndex') : (GlobalVariable.dashboardAccordionValue+4)]))

//Because of slow page load, wait for the accordion to appear with the text Year To date Sales Detail
WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/accordion-Year To Date Sales Detail Banner'),15)

//Check to make sure the Sales Dashboard page loads. Look for the text, Year To Date Sales Detail
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Year To Date Sales Detail')

WebUI.click(findTestObject('ABC/Sales Dashboard/Links/link-By Customer'))

WebUI.delay(2)
//Wait for the Footer Totals to appear. This should give the page time to load
WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Customer/table-By Customer-Year to Date Totals', [('row') : 1, ('column') : 1]), 15)

log.logWarning('-- Checking sort for Customer Names - Ascending A to Z --')
WebUI.click(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Customer/link-Customers Column Sort Header'))

WebUI.delay(2)

//Read the first 3 customer names from the list
customerName1=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Customer/table-By Customer-Year to Date Totals', [('row') : 1, ('column') : 1]))
customerName2=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Customer/table-By Customer-Year to Date Totals', [('row') : 2, ('column') : 1]))
customerName3=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Customer/table-By Customer-Year to Date Totals', [('row') : 3, ('column') : 1]))

//Output the names for reference
log.logWarning('Customer Name #1:= ' + customerName1)
log.logWarning('Customer Name #2:= ' + customerName2)
log.logWarning('Customer Name #3:= ' + customerName3)

/* There are 3 sort test. The first name should be less than the second
 * The second name should be less than the third
 * The first should be less than the third
 * If all these pass, the sort should be working
 */

if (customerName1<=customerName2){
	sortSuccess1=true
} else {
	sortSuccess1=false
	log.logError('ERROR: The first sort order for Customers on the By Month tab is incorrect and needs to be investigated.' )
	KeywordUtil.markFailed('ERROR: The first sort order for Customers on the By Month tab is incorrect and needs to be investigated.')
}

if (customerName2<=customerName3){
	sortSuccess2=true
} else {
	sortSuccess2=false
	log.logError('ERROR: The second sort order for Customers on the By Month tab is incorrect and needs to be investigated.' )
	KeywordUtil.markFailed('ERROR: The second sort order for Customers on the By Month tab is incorrect and needs to be investigated.')
}

if (customerName1<=customerName3){
	sortSuccess3=true
} else {
	sortSuccess3=false
	log.logError('ERROR: The third sort order for Customers on the By Month tab is incorrect and needs to be investigated.' )
	KeywordUtil.markFailed('ERROR: The third sort order for Customers on the By Month tab is incorrect and needs to be investigated.')
}

if (sortSuccess1 && sortSuccess2 && sortSuccess3==true){
	log.logWarning('SUCCESS: The sort order for Customers on the By Month tab is correct')
} else {
	log.logError('ERROR: The sort order for Customers on the By Month tab is incorrect and needs to be investigated.' )
	KeywordUtil.markFailed('ERROR: The sort order for Customers on the By Month tab is incorrect and needs to be investigated.')
}

/* Confirm the Sales Sort since we already on the same screen.
 * Click Sales Twice to get it to sort in Descending order
 */

log.logWarning('-- Checking sort for Sales Values - Descending Largest to Smallest --')

WebUI.click(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Customer/link-Sales Column Sort Header'))
WebUI.delay(1)
WebUI.click(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Customer/link-Sales Column Sort Header'))
WebUI.delay(1)

//Read the first 3 Sales values from the list
salesValue1=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Customer/table-By Customer-Year to Date Totals', [('row') : 1, ('column') : 2])).replaceAll("[^0-9-]","").toInteger()
salesValue2=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Customer/table-By Customer-Year to Date Totals', [('row') : 2, ('column') : 2])).replaceAll("[^0-9-]","").toInteger()
salesValue3=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Customer/table-By Customer-Year to Date Totals', [('row') : 3, ('column') : 2])).replaceAll("[^0-9-]","").toInteger()

//Output the names for reference
log.logWarning('Sales Value #1:= ' + salesValue1)
log.logWarning('Sales Value #2:= ' + salesValue2)
log.logWarning('Sales Value #3:= ' + salesValue3)

if (salesValue1>=salesValue2){
	sortSuccess1=true
} else {
	sortSuccess1=false
	log.logError('ERROR: The first sort order for Sales on the By Month tab is incorrect and needs to be investigated.' )
	KeywordUtil.markFailed('ERROR: The first sort order for Sales on the By Month tab is incorrect and needs to be investigated.')
}

if (salesValue2>=salesValue3){
	sortSuccess2=true
} else {
	sortSuccess2=false
	log.logError('ERROR: The second sort order for Sales on the By Month tab is incorrect and needs to be investigated.' )
	KeywordUtil.markFailed('ERROR: The second sort order for Sales on the By Month tab is incorrect and needs to be investigated.')
}

if (salesValue1>=salesValue3){
	sortSuccess3=true
} else {
	sortSuccess3=false
	log.logError('ERROR: The third sort order for Sales on the By Month tab is incorrect and needs to be investigated.' )
	KeywordUtil.markFailed('ERROR: The third sort order for Sales on the By Month tab is incorrect and needs to be investigated.')
}

if (sortSuccess1 && sortSuccess2 && sortSuccess3==true){
	log.logWarning('SUCCESS: The sort order for Sales on the By Month tab is correct')
} else {
	log.logError('ERROR: The sort order for Sales on the By Month tab is incorrect and needs to be investigated.' )
	KeywordUtil.markFailed('ERROR: The sort order for Sales on the By Month tab is incorrect and needs to be investigated.')
}