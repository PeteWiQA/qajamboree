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

/* This test loads the Impersonation page and passes the username
 * read from the data file. It then Calls each test case listed
 * Currently, Katalon does not support passing users to a Test Suite
 * so this is the workaround.
 * The users are stored in Impersonate User List in the Data Files folder
 * Updated 04-19-2018 13:13PM by Pete Wilson
 * Updated 03-04-2019 15:49PM by Pete Wilson
 */

WebUI.navigateToUrl(GlobalVariable.baseurl + '/impersonation')

//Check to make sure the Sales Dashboard page loads. Look for the text, View ABC Mobile as another user
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('View ABC Mobile as another user')

WebUI.setText(findTestObject('ABC/Impersonate User/input-Impersonation Search'), impersonateUser)

WebUI.delay(1)

//Set environment variables
GlobalVariable.impersonatedUser=impersonateUser
GlobalVariable.impersonatedUserName=WebUI.getText(findTestObject('ABC/Impersonate User/table-User Role Details', [('row') : 1, ('column') : 1]))

log.logWarning("Impersonated User: " + GlobalVariable.impersonatedUser)
log.logWarning("Impersonated User: " + GlobalVariable.impersonatedUserName)

WebUI.click(findTestObject('ABC/Impersonate User/btn-View as User'))

WebUI.delay(1)

//Read the user name from the page and output to the log
impersonatedUserName=WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/text-Currently Impersonated User'))
log.logWarning('Running tests as Impersonated User:=' + impersonatedUserName)

WebUI.callTestCase(findTestCase('Test Cases/Home/Verify Home Page and Links'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Main Dashboard Tests/Check Import Date'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Main Dashboard Tests/Verify Dashboard Current Plan Value is not Zero'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Main Dashboard Tests/Calculate Gross Profit Plan for Daily, MTD and YTD'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Main Dashboard Tests/Verify Gross Profit Figures are not Duplicated'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Main Dashboard Tests/Verify Gross Profit Plan Figures are not Duplicated'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Main Dashboard Tests/Verify Sales Figures are not Duplicated'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Main Dashboard Tests/Verify Sales Plan Figures are not Duplicated'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Main Dashboard Tests/Read Summary Sales Dashboard Figures'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Main Dashboard Tests/Verify Current Plan Value'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Main Dashboard Tests/Verify Home Page-Daily Sales Figure'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Daily/Verify Daily Sales Values are not Zero'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Daily/Verify Daily Sales Plan Values are not Zero'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Daily/Calculate Margin Daily Percent By Customer'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Daily/Calculate Margin Daily Percent By Product Category'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Daily/Sum Columns for Daily Totals on By Product Category'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Monthly/Verify Monthly Sales Plan Values are not Zero'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Monthly/Validate Sort Order MTD By Customer'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Monthly/Calculate Monthly Sales Detail Totals By Product Category'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Monthly/Read Monthly Sales Dashboard Figures'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Monthly/Read Monthly Sales Detail By Customer'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Monthly/Read Monthly Sales Detail By Product Category'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Monthly/Compare Monthly Sales Value to YTD By Month Value'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Monthly/Sum Columns for Month to Date Totals on By Product Category'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Yearly/Read Yearly Sales Dashboard Figures'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Yearly/Read Yearly Sales Detail By Customer'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Yearly/Read Yearly Sales Detail By Month'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Yearly/Read and Sum Yearly Sales Detail By Month'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Yearly/Read Yearly Sales Detail By Product Category'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Yearly/Verify Yearly Sales Plan Values are not Zero'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Yearly/Verify Months on YTD By Customer are not Duplicated'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Yearly/Verify Sort Order - YTD By Customer'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Yearly/Sum Columns for Year to Date Totals on By Month'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Yearly/Sum Columns for Year to Date Totals on By Product Category'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Yearly/Verify Yearly Sales Plan Header Figure Match Yearly Sales Plan Footer Total'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Customer Profile/Verify Customer Profile'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Prospects/Count Prospects'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Tasks/Count Tasks'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Quotes/Count Quotes'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Search/Search Inventory'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Search/Search Customers'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Search/Search Branches'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Tasks/Create Task'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Prospects/Create Prospect'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Quotes/Create Quote - New Customer - Custom Inventory Listr'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Quotes/Convert Quote to Sold'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Quotes/Create Quote - Existing Customer - Custom Inventory List'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Search/Search Prospects'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('Test Cases/Read Only Budget/Determine Sales Plan Status'), [:], FailureHandling.CONTINUE_ON_FAILURE)

