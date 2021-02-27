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
import org.openqa.selenium.Keys as Keys
import org.openqa.selenium.WebDriver as WebDriver
import org.openqa.selenium.WebElement as WebElement
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import org.openqa.selenium.By as By
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger
import com.kms.katalon.core.webui.common.WebUiCommonHelper

KeywordLogger log = new KeywordLogger()

/*Switch to the Tasks tab, count the number of tasks
 *Create a new Task, then count the number of tasks again
 *Confirm this task is listed on the Home page
 *The number should increase by 1 if successful
 *Created 02-07-2018 09:31AM by Pete Wilson
 *Updated 08-16-2018 10:08AM by Pete Wilson
 *Updated 02-08-2019 14:03PM by Pete Wilson
 */

xpath="//*[@role=\'task-show-complete-button\']"
int originalNumberOfTasks=0

//Store the URL of the customer profile
url = WebUI.getUrl()

//Get the time and date info to add to the Task to make it unique and searchable.
String formattedDate=CustomKeywords.'tools.commonCode.getDateAndTime'()

WebUI.click(findTestObject('ABC/Customer Profile/Tabs/tab-Tasks'))

log.logWarning('--- Count and Create Tasks ---')

WebUI.delay(1)

//Check if there are existing Tasks on the page and count how many
boolean elementVisible=CustomKeywords.'tools.commonCode.verifyObjectPresent'('ABC/Customer Profile/Tasks/text-Existing Task')
if (elementVisible==true){
	originalNumberOfTasks=CustomKeywords.'tools.commonCode.countRowsPerPage'(xpath)
} else {
	log.logWarning('Customer has no Tasks available at this time')
}

log.logWarning('Number of Tasks listed on the Prospect Profile:= ' + originalNumberOfTasks)

WebUI.sendKeys(findTestObject('ABC/Customer Profile/Tasks/input-Task Subject'), 'New Task Subject from QA - ' + formattedDate)

WebUI.sendKeys(findTestObject('ABC/Customer Profile/Tasks/textarea-Task Note'), 'New Task Note from QA - ' + formattedDate)

//There is an oddity where the date has to entered with the year first.
WebUI.sendKeys(findTestObject('ABC/Customer Profile/Tasks/input-Due Date'), '2019-12-31')

WebUI.click(findTestObject('ABC/Customer Profile/Tasks/btn-Add'))

WebUI.delay(2)

//Count the new number of Tasks using the xpath listed above
int updatedNumberOfTasks=CustomKeywords.'tools.commonCode.countRowsPerPage'(xpath)

log.logWarning('Updated Number of Tasks listed on the Prospect Profile:= ' + updatedNumberOfTasks)

//Confirm the number of contacts has increased by 1
if ((updatedNumberOfTasks - originalNumberOfTasks) == 1) {
    log.logWarning('SUCCESS: A new Task has been created')
} else {
    log.logError('ERROR: A problem has occurred creating the Task')
    KeywordUtil.markFailed('ERROR: A problem has occurred creating the Task')
}

//Return to Home page and confirm the entered Task is visible

WebUI.navigateToUrl(GlobalVariable.baseurl)

WebUI.waitForPageLoad(15)
log.logWarning('Searching for Task: New Task Subject from QA - ' + formattedDate)
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('New Task Subject from QA - ' + formattedDate)
WebUI.navigateToUrl(url)
