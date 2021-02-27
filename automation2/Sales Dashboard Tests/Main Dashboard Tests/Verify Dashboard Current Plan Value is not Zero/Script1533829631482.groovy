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

/* This test confirms the values for Current Plan on the Dashboard is not $0
 * For a user with a budget this should be a dollar figure. For known users
 * if the value is $0, it usually indicates and error with import
 * Updated 04-18-2018 11:22AM by Pete Wilson
 * Updated 08-09-2018 11:54AM by Pete Wilson 
 */

WebUI.navigateToUrl(GlobalVariable.baseurl + '/v3/sales_dashboard')

//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')

//Read the Current Plan Value figure from the main sales dashboard
//Confirm this value isn't $0. If it is, that could indicate a bad data import

long currentPlanValue = WebUI.getText(findTestObject('ABC/Sales Dashboard/Main Dashboard/text-Dashboard - Current Plan Value')).replaceAll("[^0-9.]","").toLong()

log.logWarning('The Current Plan Value for ' + (GlobalVariable.impersonatedUser) + ' is:= ' + currentPlanValue)

if (currentPlanValue<=0) {
	log.logError('ERROR: The Current Plan Value is $0 or less. A problem may have occurred with the import')
	KeywordUtil.markFailed('ERROR: The Current Plan Value is $0 or less. A problem may have occurred with the import')
}