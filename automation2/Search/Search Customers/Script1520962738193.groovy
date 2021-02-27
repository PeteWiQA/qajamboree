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

/* This test reads a name from the YTD Sales Detail - By Customer tab
 * and then searches from that customer in the Search - Customers menu
 * The details of the customer are then captured and written to the log
 * This confirms that Customer Search is working and that a customer has details
 * Updated 03-30-2018 14:57PM by Pete Wilson
 * Updated 04-20-2018 13:14PM by Pete Wilson
 * Updated 08-14-2018 15:25PM by Pete Wilson
 * Updated 01-22-2019 11:46AM by Pete Wilson
 * Updated 03-07-2019 09:56AM by Pete Wilson
 */

//Reference to the YTD Sales Detail By Customer table
String xpath="//table[@id='sales_dash_table']/tbody/tr"
List customerName=[]
//Read the first customer name from the YTD Sales Details page
log.logWarning('--- Reading Customer Name from YTD Sales Detail By Customer Tab ---')

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

//Count the number of rows in the YTD Sales Detail table
int customerCount=CustomKeywords.'tools.commonCode.countRowsPerPage'(xpath)
log.logWarning("Customer Count: " + customerCount)

//Read the first 20 customer or the number of results returned, whichever is greater
if (customerCount>=50){
	customerCount=50
}
//Read the first 20 customers in the By Customer table
for (loop = 1; loop <=customerCount; loop++) {
	customerName[loop]=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Customer/table-By Customer-Year to Date Totals', [('row') : loop, ('column') : 1]))
	log.logWarning('--- Customer Name from YTD Sales Detail By Customer Tab --- ' + customerName[loop])
}

//Iterate through all the customers that were just read in. Check the Branch and Rep against the impersonated user
for (loop = 1; loop <=(customerName.size()-1); loop++) {
	WebUI.navigateToUrl(GlobalVariable.baseurl + '/customers')

	//Check the option for, My Branch Only
	if (WebUI.verifyElementChecked(findTestObject('ABC/Customer Search/checkbox-My Branch Only'),10,FailureHandling.OPTIONAL)){
		WebUI.setText(findTestObject('ABC/Customer Search/input-Customer Search Input Field'), customerName[loop])
	} else {
		WebUI.setText(findTestObject('ABC/Customer Search/input-Customer Search Input Field'), customerName[loop])
		WebUI.click(findTestObject('ABC/Customer Search/checkbox-My Branch Only'))
	}

	WebUI.delay(2)

	int resultsFound = WebUI.getText(findTestObject('ABC/Customer Search/text-Customer Search - Results Found')).replaceAll("[^0-9]","").toInteger()

	log.logWarning(resultsFound + ' Results were found')

	if (resultsFound==0) {
		log.logError('ERROR: No results were found for the entered customer name: ' + customerName[loop])
		KeywordUtil.markError()
	} else {
	
		String siteCustomerName=WebUI.getText(findTestObject('ABC/Customer Search/link-Customer Search - Company Name'))

		log.logWarning('The first customer named returned from Search was: ' + siteCustomerName)
		if (siteCustomerName!=customerName[loop]){
			log.logError("ERROR: The Customer Name: " +  customerName[loop] + " and Search Result " + siteCustomerName + " DO NOT match for this customer")
		}

		WebUI.click(findTestObject('ABC/Customer Search/link-Customer Search - Company Name'))

		log.logWarning('Customer Profle Company Name: ' + WebUI.getText(findTestObject('ABC/Customer Search/text-Customer Name Header')))

		log.logWarning('The Customer Profile Information: ')

		log.logWarning('Program: ' + WebUI.getText(findTestObject('ABC/Customer Search/text-Customer Program')))

		log.logWarning('Account #: ' + WebUI.getText(findTestObject('ABC/Customer Search/text-Customer Account Number')))

		log.logWarning('Terms: ' + WebUI.getText(findTestObject('ABC/Customer Search/text-Customer Terms')))

		log.logWarning('Customer Class: ' + WebUI.getText(findTestObject('ABC/Customer Search/text-Customer Class')))

		log.logWarning('Sold Since: ' + WebUI.getText(findTestObject('ABC/Customer Search/text-Customer Sold Since')))

		//Verify the User Division and Branch match the user division

		String branchName=WebUI.getText(findTestObject('ABC/Customer Search/text-ABC Branch'))
		log.logWarning('Branch Name: ' + branchName)
		if (branchName.contains(GlobalVariable.userDivision)!=true){
			log.logError("ERROR: The Sales Rep Division listed on the Customer Profile does not match the Division of the Impersonated User")
			log.logError("Site Branch: " + branchName + " Expected Branch: " + GlobalVariable.userDivision)
			KeywordUtil.markError()
		}

		String branchRepName=WebUI.getText(findTestObject('ABC/Customer Search/text-ABC Rep'))
		log.logWarning('Sales Rep Name: ' + branchRepName)
		if (branchRepName!=GlobalVariable.impersonatedUserName){
			log.logError("ERROR: The Sales Rep Name listed on the Customer Profile does not match the name of the Impersonated User" )
			log.logError("Site Sales Rep: " + branchRepName + ": Expected Rep Name: " + GlobalVariable.impersonatedUserName)
			KeywordUtil.markError()
		}

		String branchDivision=WebUI.getText(findTestObject('ABC/Customer Profile/Profile Details/text-Account Profile Details', [('row') : 6]))
		String repDivision=WebUI.getText(findTestObject('ABC/Customer Profile/Profile Details/text-Account Profile Details', [('row') : 7]))

		if (GlobalVariable.userDivision.equalsIgnoreCase("ABC")){
			if (branchDivision.contains("ABC")!=true){
				log.logError("ERROR: The Branch location on the Customer Profile does not match the Division")
				log.logError("Found: " + branchDivision + ": Expected " + GlobalVariable.userDivision)
				KeywordUtil.markFailed()
			}
			if (repDivision.contains("ABC")!=true){
				log.logError("ERROR: The Rep location on the Customer Profile does not match the Division")
				log.logError("Found: " + repDivision + ": Expected " + GlobalVariable.userDivision)
				KeywordUtil.markFailed()
			}
		} else {
			if (branchDivision.contains("TCI")!=true){
				log.logError("ERROR: The Branch location on the Customer Profile does not match the Division")
				log.logError("Found: " + branchDivision + ": Expected " + GlobalVariable.userDivision)
				KeywordUtil.markFailed()
			}
			if (repDivision.contains("TCI")!=true){
				log.logError("ERROR: The Rep location on the Customer Profile does not match the Division")
				log.logError("Found: " + repDivision + ": Expected " + GlobalVariable.userDivision)
				KeywordUtil.markFailed()
			}
		}
	}
}