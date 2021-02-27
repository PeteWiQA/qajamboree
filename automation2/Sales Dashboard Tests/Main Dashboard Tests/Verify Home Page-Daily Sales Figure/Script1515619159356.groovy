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

/* The purpose of this test is to read the Daily Sales from the Home page
 * and compare it to the Daily Sales value on the Sales Dashboard. These two
 * figures should match and be equal. If not, there could be an import error
 * Updated 04-18-2018 11:38AM by Pete Wilson
 * Updated 08-10-2018 10:37AM by Pete Wilson
 * Updated 11-14-2018 11:44AM by Pete Wilson
 */

//Determine if the user is a Sales Rep or Manager
int accordionValue=CustomKeywords.'tools.commonCode.getUserRole'()

WebUI.navigateToUrl(GlobalVariable.baseurl)

//On the Home page, read the Daily Sales Figure. This is also a link to the Sales Dashboard
homeDailySalesFigure = WebUI.getText(findTestObject('ABC/Home/text-Daily Sales Figure'))

//Open the Sales Dashboard
WebUI.navigateToUrl(GlobalVariable.baseurl + '/v3/sales_dashboard')

//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')

//Read the Daily Sales Figure from the Dashboard. This is first number from the table.
dashboardDailySalesFigure = WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Detail Values for Daily-MTD-YTD', [('divIndex') : accordionValue, ('rowIndex') : 1]))

log.logWarning('Daily Sales Figure from the Home Page is:= ' + homeDailySalesFigure)
log.logWarning('Daily Sales Figure from the Sales Dashboard is:= ' + dashboardDailySalesFigure)

//The sales figures should match. If they don't there has been an error, possibly an import problem.
//It is possible the Daily Sales could be $0 or negative, due to rebates, but it isn't the norm and should be checked manually.
if (homeDailySalesFigure!=dashboardDailySalesFigure) {
	KeywordUtil.markFailed('ERROR: The Home Page Daily Sales Figure does not match the Dashboard Daily Sales Figure')
} else {
	KeywordUtil.markPassed('SUCCESS: The Home Page Daily Sales Figure matches the Dashboard Daily Sales Figure')
}