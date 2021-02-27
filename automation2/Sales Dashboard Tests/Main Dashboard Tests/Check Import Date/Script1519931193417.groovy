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
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil

KeywordLogger log = new KeywordLogger()

/* This test validates the import date. It should always be one day behind.
 * If today is 01/06/2018, the report date should be 01/05/2018
 * The exception is if the test is run on Monday, then the import date should read 3 days ago, to Friday.
 * Updated 4/14/2018 10:52AM by Pete Wilson
 * Updated 08-09-2018 14:56PM by Pete Wilson
 */
//Open the Sales Dashboard
WebUI.navigateToUrl(GlobalVariable.baseurl + '/v3/sales_dashboard')

//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')

//Read the Import date from the Month to Date accordion header. It displays in the format - (1/11/2018 - DAY 8 OF 22)
importDate = WebUI.getText(findTestObject('ABC/Sales Dashboard/Main Dashboard/text-Sales Import Date', [('index') : GlobalVariable.dashboardAccordionValue]))

//Split the string into two pieces at the hyphen - character
String[] parsedDate = importDate.split(' - ')

// The first part of the string is the import date with the opening parenthesis removed
importDate = parsedDate[0].replaceAll('\\(', '')

//Call the import date parser to set import date values
WebUI.callTestCase(findTestCase('Sales Dashboard Tests/Main Dashboard Tests/Set Import Date Values'), [:])

log.logWarning('The import date listed on the site is: ' + importDate)

log.logWarning('The Report Date is ' + GlobalVariable.siteReportDate)

if (importDate == GlobalVariable.siteReportDate) {
    KeywordUtil.markPassed('SUCCESS: The import ran successfully')
} else {
    KeywordUtil.markFailed('ERROR: The import needs to be run')
}

