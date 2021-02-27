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
import com.kms.katalon.core.util.KeywordUtil

KeywordLogger log = new KeywordLogger()

/* The purpose of this test is to read a customer name from a Sales Reps read only budget
 * It then reads the Sales Plan value associated with that customer
 * Next it searches for that customer in Search - Customer
 * The customer is selected and Customer Profile is loaded
 * On the 2018 Plan tab, it displaus the Submission date and reads the YTD Plan total from the footer
 * After the budget is submitted, these values should match
 * 
 * Updated 08-16-2018 09:36AM by Pete Wilson 
 */



//Click through the Tools and Sales Plan menus. Since a default budget isn't loaded if they're closed we have to use the menus.
WebUI.click(findTestObject('ABC/Main NavBar/link-Tools'))

WebUI.delay(1)

WebUI.click(findTestObject('ABC/Main NavBar/link-Tools - Sales Plan'))

WebUI.delay(3)

//Get the name of the first customer under the Top 90% of Existing Customers

top90CustomerName = WebUI.getText(findTestObject('ABC/Open Sales Plan Budget/Closed Budget/Top90Customer-Name', [('Variable') : 1]))

//Get the Sales Plan value. GetAttribute is used since this field is greyed out
customerSalesPlan=WebUI.getAttribute(findTestObject('ABC/Open Sales Plan Budget/Closed Budget/Existing Customer Sales Plan', [('Variable') : 1]),'value')

log.logWarning('The chosen customer is ' + top90CustomerName)

log.logWarning('The customer sales plan is ' + customerSalesPlan)

//Nagivate to the Search Customers page and enter the name read from the Top 90%

WebUI.navigateToUrl(GlobalVariable.baseurl + '/customers')

WebUI.setText(findTestObject('ABC/Customer Search/input-Customer Search Input Field'), top90CustomerName)

//WebUI.check(findTestObject('ABC/Customer Search/Customer 2018 Plan Tab/checkbox-Search-My Branch Only'))

WebUI.delay(3)

//Click the first name returned in the list. Hopefully, this is the same customer from the Top 90%
//Due to the way search works on this page, the requested customer may not be the first one
//Unfortunately, there is nothing unique to compare against in this list, so we sort of have to hope it's right

WebUI.click(findTestObject('ABC/Customer Search/link-Customer Search - Company Name'))

//Open the Customer Profile and switch to the 2018 Plan tab
WebUI.click(findTestObject('ABC/Customer Profile/Tabs/tab-2018 Plan'))

elementPresent=WebUI.verifyElementPresent(findTestObject('ABC/Customer Profile/Tabs/tab-2018 Plan'), 5, FailureHandling.CONTINUE_ON_FAILURE)
//If the 2018 Plan tab isn't present, give an error and exit.
if (elementPresent==false) {
	KeywordUtil.markError('ERROR: 2018 Plan tab is not available for this customer. No test to execute')
	throw new AssertionError('ERROR: 2018 Plan tab is not available for this customer. No test to execute')
}

//Read the submission date

budgetSubmissionDate = WebUI.getText(findTestObject('ABC/Customer Search/Customer 2018 Plan Tab/text-Budget Submission Date'))

//Read the Product Category, the YTD Sales, YTD Plan from the table

for (loop = 1; loop <=10; loop++) {
	planCategory=WebUI.getText(findTestObject('ABC/Customer Search/Customer 2018 Plan Tab/text-Plan Tab Category', [('Variable') : loop]))
	planYTDSales=WebUI.getText(findTestObject('ABC/Customer Search/Customer 2018 Plan Tab/text-Plan Tab YTD Sales', [('Variable') : loop]))
	planYTDPlan=WebUI.getText(findTestObject('ABC/Customer Search/Customer 2018 Plan Tab/text-Plan Tab YTD Plan', [('Variable') : loop]))
	log.logWarning('Category: ' + planCategory + ' -:- ' + 'YTD Sales: ' + planYTDSales + ' -:- ' + 'YTD Plan: ' + planYTDPlan)

  }

//Read the Total values from the Footer of the table - YTD Sales and YTD Plan
//For the Footer, 2=YTD Sales, 3=YTD Plan - These are the columns of the table

ytdSalesTotal = WebUI.getText(findTestObject('ABC/Customer Search/Customer 2018 Plan Tab/text-YTD Sales Plan Total', [('Variable') : 2]))

ytdSalesPlanTotal = WebUI.getText(findTestObject('ABC/Customer Search/Customer 2018 Plan Tab/text-YTD Sales Plan Total', [('Variable') : 3]))

//Write out the values and compare if the value from the Top90% is the same as the 2018 Plan figure

log.logWarning('The Budget Submission date is: ' + budgetSubmissionDate)

log.logWarning('Top 90% customer - ' + top90CustomerName + ' has a Budgeted Sales Plan value of: ' + customerSalesPlan)

log.logWarning('The YTD Sales Total from the table is: ' + ytdSalesTotal)

log.logWarning('The YTD Sales Plan Total from the table is: ' + ytdSalesPlanTotal)

if (customerSalesPlan != ytdSalesPlanTotal) {
    log.logError('ERROR: The Budget Sales Plan Amount Does Not Equal the YTD Sales Plan Total')
} else {
    log.logWarning('SUCCESS: The Budget Sales Plan Amount Matches the YTD Sales Plan Total')
}
