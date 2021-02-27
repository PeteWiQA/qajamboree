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

/* The purpose of this test is to count the number of tasks for the given user
 * It uses the Webdriver command set, which is a replacement for StoreXPathCount
 * used in the Selenium IDE
 * This is used to confirm the number of Tasks a user has in a Before/After
 * test set, and to confirm the user has a task after the Create Task test is run
 * Updated 01-25-2018 11:15AM by Pete Wilson
 * Updated 04-19-2018 15:47PM by Pete Wilson
 * Updated 08-14-2018 14:35PM by Pete Wilson
 */

xpath="//div[@class=\'task-detail\']"

WebUI.navigateToUrl(GlobalVariable.baseurl + '/tasks')
//Check to make sure the Tasks page loads. Look for the text, My Open Tasks
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('My Open Tasks')

// Wait for the Task Name to appear on the page.

elementPresent=WebUI.waitForElementPresent(findTestObject('ABC/Tasks/input-Task Name', [('Variable') : 1]), 5)

//If the Task Name isn't present, give an error and exit.
if (elementPresent==false) {
	log.logWarning('ERROR: There are no Tasks for ' + GlobalVariable.impersonatedUser + ' Test was not completed')
	log.logError('ERROR: There are no Tasks for ' + GlobalVariable.impersonatedUser + ' Test was not completed')
} else {

	int totalRowCount=CustomKeywords.'tools.commonCode.countRowsPerPage'(xpath)

	log.logWarning('The number of Task Items is: ' + totalRowCount + ' The first 10 or less will be displayed')
	if (totalRowCount>10){
		totalRowCount=10
	}

	// Print out the Name of the task and the Body Text of the task
	for (loop = 1; loop <= totalRowCount; loop++) {
		taskName = WebUI.getText(findTestObject('ABC/Tasks/input-Task Name', [('Variable') : loop]))

		taskBody = WebUI.getText(findTestObject('ABC/Tasks/text-Task Body Text', [('Variable') : loop]))

		log.logWarning('Task Name #' + loop + ' : ' + taskName)

		log.logWarning('Task Body Text:- ' + taskBody)
	}
}