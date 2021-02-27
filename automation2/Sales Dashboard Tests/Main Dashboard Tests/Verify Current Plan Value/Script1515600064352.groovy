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

import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger

KeywordLogger log = new KeywordLogger()

/* Verify that the value listed for Current Sales Plan Value on the Sales Dashboard is the same
 * as the Total Sales Plan value listed at the bottom of a users budget. An update was issued as a hotfix
 * ABCB-980, so a test was created to validate this.
 * Updated 08-10-2018 10:19AM by Pete Wilson
 */

WebUI.navigateToUrl(GlobalVariable.baseurl + '/v3/sales_dashboard')

//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')

//Get the Current Plan Value from the Sales Dashboard
currentPlanValue = WebUI.getText(findTestObject('ABC/Sales Dashboard/Main Dashboard/text-Dashboard - Current Plan Value'))

//Open Tools and then Sales Plan from the menu
//This needs to be done since the read-only budget needs a guid and going to /budgets generates and error
WebUI.click(findTestObject('ABC/Main NavBar/link-Tools'))

WebUI.delay(1)

//Select Sales Plan from the Nav Bar
WebUI.click(findTestObject('ABC/Main NavBar/link-Tools - Sales Plan'))

WebUI.waitForPageLoad(15)

//Read the first H3 tag text from the page. If the budget period is open, the text will read as Sales Planning Process for 2018
h3HeaderText = WebUI.getText(findTestObject('ABC/Read-Only Sales Plan Budget/text-Sales Plan H3 Header Text'))

if (h3HeaderText == 'Sales Planning Process for ' + GlobalVariable.budgetYear) {
	//For an Open budget, read the Current Plan value from the Header.
	totalSalesPlan = WebUI.getText(findTestObject('ABC/Open Sales Plan Budget/Review Plan/text-Header-Current Plan Value'))

} else {
	//For a Read-only budget, in Section 5, Review Plan, go the Footer of Total Sales Plan and read Total Sales Plan
	totalSalesPlan = WebUI.getText(findTestObject('ABC/Read-Only Sales Plan Budget/Section 5 - Review Plan/label-Total Sales Plan'))
}

//Output the two located values just for confirmation
log.logWarning('The Budget Sales Plan Value is:= ' + totalSalesPlan)
log.logWarning('The Dashboard Current Plan Value is:= ' + currentPlanValue)

//Compare the two values, they need to match or there is an error with the data
if (totalSalesPlan!=currentPlanValue) {
	log.logError('ERROR: There is a discrepency between the Budget Sales Plan and the Sales Dashboard Current Plan')
	KeywordUtil.markFailed('ERROR: There is a discrepency between the Budget Sales Plan and the Sales Dashboard Current Plan')
} else {
	log.logWarning('SUCCESS: The Budget Sales Plan matches the Sales Dashboard Current Plan')
	KeywordUtil.markPassed('SUCCESS: The Budget Sales Plan matches the Sales Dashboard Current Plan')
}
