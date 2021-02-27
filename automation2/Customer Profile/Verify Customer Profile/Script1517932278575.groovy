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
import com.kms.katalon.core.util.KeywordUtil
import org.openqa.selenium.Keys as Keys
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger

KeywordLogger log = new KeywordLogger()

/* This script is designed to load a customer profile and read values from the available tabs
 * The tabs visible could be Contacts, Notes, Tasks, Quotes and 2018 Plan
 * If Contacts, Notes or Tasks if visible, an item will be created
 * If Quotes or 2018 Plan is visible, the values will be read
 * Created 02-07-2018 09:31AM by Pete Wilson
 * Updated 02-15-2018 10:44AM by Pete Wilson
 * Updated 03-30-2018 14:48PM by Pete Wilson
 * Updated 04-10-2018 13:40PM by Pete Wilson
 * Updated 08-15-2018 15:57PM by Pete Wilson
 */

//Read the first customer name from the YTD Sales Details page

WebUI.navigateToUrl(GlobalVariable.baseurl + '/v3/sales_dashboard')

WebUI.click(findTestObject('ABC/Sales Dashboard/Yearly Sales Section/accordion-YTD Sales Accordion', [('divIndex') : (GlobalVariable.dashboardAccordionValue+4)]))

//Because of slow page load, wait for the accordion to appear with the text Year To date Sales Detail
WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/accordion-Year To Date Sales Detail Banner'),15)

//Look for the text, Year To Date Sales Detail on the Page.
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Year To Date Sales Detail')

WebUI.click(findTestObject('ABC/Sales Dashboard/Links/link-By Customer'))

WebUI.delay(2)

//Read the first customer in the By Customer table
customerName=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Customer/table-By Customer-Year to Date Totals', [('row') : 1, ('column') : 1]))

log.logWarning('--- Customer Name from YTD Sales Detail By Customer Tab ---' + customerName)

//Use the customer name to open the Customer Prpfile
log.logWarning('--- Start of Verify Customer Profile Tabs ---')

//Go to the Search Customer section and enter a customer name
WebUI.navigateToUrl(GlobalVariable.baseurl + '/customers')

//Check the option for, My Branch Only
if (WebUI.verifyElementChecked(findTestObject('ABC/Customer Search/checkbox-My Branch Only'),10,FailureHandling.OPTIONAL)){
	WebUI.setText(findTestObject('ABC/Customer Search/input-Customer Search Input Field'), customerName)
} else {
	WebUI.setText(findTestObject('ABC/Customer Search/input-Customer Search Input Field'), customerName)
	WebUI.click(findTestObject('ABC/Customer Search/checkbox-My Branch Only'))
}

WebUI.delay(2)

returnedCompanyName = WebUI.getText(findTestObject('ABC/Customer Search/link-Customer Search - Company Name'))

WebUI.click(findTestObject('ABC/Customer Search/link-Customer Search - Company Name'))

WebUI.delay(1)

customerProfileName=WebUI.getText(findTestObject('ABC/Customer Search/text-Customer Name Header'))
log.logWarning('Reading Customer Profile for Customer:=' + customerName)
if (customerProfileName!=customerName){
	log.logError('ERROR: The Customer Profile does not match the Customer Search query name. The results could be in error')
	KeywordUtil.markFailed('ERROR: The Customer Profile does not match the Customer Search query name. The results could be in error.')
}

//Determine which of the tabs is visible and run the appropriate test
//elementVisible=WebUI.verifyElementPresent(findTestObject('ABC/Customer Profile/Tabs/tab-Contacts'), 5, FailureHandling.CONTINUE_ON_FAILURE)
boolean elementVisible=CustomKeywords.'tools.commonCode.verifyObjectPresent'('ABC/Customer Profile/Tabs/tab-Contacts')
if (elementVisible==true){
	log.logWarning('--- Contacts tab is available, running test ---')
	WebUI.callTestCase(findTestCase('Customer Profile/Contacts'), [:], FailureHandling.CONTINUE_ON_FAILURE)
} else {
	log.logWarning('--- NOTE: The Contacts tab is not available for this customer. No test to execute ---')
	log.logError('--- NOTE: The Contacts tab is not available for this customer. No test to execute ---')
}

elementVisible=CustomKeywords.'tools.commonCode.verifyObjectPresent'('ABC/Customer Profile/Tabs/tab-Notes')
if (elementVisible==true){
	log.logWarning('--- Notes tab is available, running test ---')
	WebUI.callTestCase(findTestCase('Customer Profile/Notes'), [:], FailureHandling.CONTINUE_ON_FAILURE)
} else {
	log.logWarning('--- NOTE: The Notes tab is not available for this customer. No test to execute ---')
	log.logError('--- NOTE: The Notes tab is not available for this customer. No test to execute ---')
}

elementVisible=CustomKeywords.'tools.commonCode.verifyObjectPresent'('ABC/Customer Profile/Tabs/tab-Tasks')
if (elementVisible==true){
	log.logWarning('--- Tasks tab is available, running test ---')
	WebUI.callTestCase(findTestCase('Customer Profile/Tasks'), [:], FailureHandling.CONTINUE_ON_FAILURE)
} else {
	log.logWarning('--- NOTE: The Tasks tab is not available for this customer. No test to execute ---')
	log.logError('--- NOTE: The Tasks tab is not available for this customer. No test to execute ---')
}

elementVisible=CustomKeywords.'tools.commonCode.verifyObjectPresent'('ABC/Customer Profile/Tabs/tab-Quotes')
if (elementVisible==true){
	log.logWarning('--- Quotes tab is available, running test ---')
	WebUI.callTestCase(findTestCase('Customer Profile/Quotes'), [:], FailureHandling.CONTINUE_ON_FAILURE)
} else {
	log.logWarning('--- NOTE: The Quotes tab is not available for this customer. No test to execute ---')
	log.logError('--- NOTE: The Quotes tab is not available for this customer. No test to execute ---')
}

elementVisible=CustomKeywords.'tools.commonCode.verifyObjectPresent'('ABC/Customer Profile/Tabs/tab-2018 Plan')
if (elementVisible==true){
	log.logWarning('--- 2019 Plan tab is available, running test ---')
	WebUI.callTestCase(findTestCase('Customer Profile/2018 Plan'), [:], FailureHandling.STOP_ON_FAILURE)
} else {
	log.logWarning('--- NOTE: The 2019 Plan tab is not available for this customer. No test to execute ---')
	log.logError('--- NOTE: The 2019 Plan tab is not available for this customer. No test to execute ---')
}