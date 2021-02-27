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

/* This test is to determine if the budget period is open
 * or if the budget is read only
 * If the budget period is open, start at Step 1 and submit the budget
 * If the budget is read only, sum the totals for Section 1, 4, 5
 * Created 02-26-2018 16:43PM by Pete Wilson
 */

WebUI.navigateToUrl(GlobalVariable.baseurl)

//Open Tools and then Sales Plan from the menu
//This needs to be done since the read-only budget needs a guid and going to /budgets generates an error

WebUI.callTestCase(findTestCase('Read Only Budget/Open Tools - Sales Plan'), [:], FailureHandling.STOP_ON_FAILURE)

//Read the first H3 tag text from the page. If the budget period is open, the text will read as Sales Planning Process for 2018
h3HeaderText = WebUI.getText(findTestObject('ABC/Read-Only Sales Plan Budget/text-Sales Plan H3 Header Text'))

if (h3HeaderText == 'Sales Planning Process for ' + GlobalVariable.budgetYear) {
	//If the environment is Production, we don't want to create and submit a budget. We need to exit.
	if (GlobalVariable.baseurl=="https://abcsupplymobile.com"){
		log.logError('--- Verify Budget Status Test ---')
		log.logError('The budget is Open and the environment is Production. No create budget process will run. Exiting test.')	
	} else {
    	//If the budget period is open and we're in QA or Feature, start at Step 1 and submit a budget
    	log.logWarning('The user budget is in an Open Status. Creating and Submitting a budget.')
		WebUI.callTestCase(findTestCase('Sales Plan Budget/Create Budget'), [:], FailureHandling.CONTINUE_ON_FAILURE)
	} 
} else {
	//If the budget period is closed, confirm values and totals
    log.logWarning('The user budget is in a Read-Only status. Verifying calculations.')
	
	WebUI.callTestCase(findTestCase('Read Only Budget/Sum Previous 12 Months Figures'), [:], FailureHandling.CONTINUE_ON_FAILURE)
	WebUI.callTestCase(findTestCase('Read Only Budget/Sum Seasonality Plan Values'), [:], FailureHandling.CONTINUE_ON_FAILURE)
	WebUI.callTestCase(findTestCase('Read Only Budget/Sum Review Plan Figures'), [:], FailureHandling.CONTINUE_ON_FAILURE)
}