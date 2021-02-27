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

/* The purpose of this test is to read the values from the Main Dashboard
 * The Daily, Monthly and Yearly figures are read. This confirms the dashboard
 * has loaded correctly and outputs the values for reference. This can used as
 * a Before/After test to confirm the sales figures for a user
 * Updated 04-18-2018 11:29AM by Pete Wilson
 * Updated 08-10-2018 10:16AM by Pete Wilson
 * Updated 11-14-2018 12:17PM by Pete Wilson
 * 
 */

//Determine if the user is a Sales Rep or Manager
int accordionValue=CustomKeywords.'tools.commonCode.getUserRole'()

WebUI.navigateToUrl(GlobalVariable.baseurl + '/v3/sales_dashboard')

//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')

// Read all values from the Daily Sales Section of the table
// Read the name of the customer/Sales Rep
customerName = WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/text-Currently Impersonated User'))

log.logWarning('Reading values for customer - ' + customerName)

log.logWarning('------Daily Sales Section ------')

for (loop = 1; loop <=9; loop++) {
	//Loop through the values on the Daily accordion
	//Read the first value in the table using "index" as a variable in the XPATH
	daily_sales_figures = WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Detail Values for Daily-MTD-YTD', [('divIndex') : accordionValue, ('rowIndex') : loop])).replaceAll('\\n|\\r', ' - ')

	log.logWarning('(Daily) - ' + daily_sales_figures)
}

// Read all values from the Monthly Sales Section of the table
log.logWarning('------ Monthly Sales Section ------')

for (loop = 1; loop <=9; loop++) {
	//Loop through the values on the Monthly accordion
	monthly_sales_figures = WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Detail Values for Daily-MTD-YTD', [('divIndex') : (accordionValue+2), ('rowIndex') : loop])).replaceAll('\\n|\\r', ' - ')

	log.logWarning('(Monthly) - ' + monthly_sales_figures)
}

// Read all values from the Yearly Sales Section of the table
log.logWarning('------ Yearly Sales Section ------')

for (loop = 1; loop <=9; loop++) {
	//Loop through the values on the Year to Date accordion
	yearly_sales_figures = WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Detail Values for Daily-MTD-YTD', [('divIndex') : (accordionValue+4), ('rowIndex') : loop])).replaceAll('\\n|\\r', ' - ')

	log.logWarning('(Yearly) - ' + yearly_sales_figures)
}