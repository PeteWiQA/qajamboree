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
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger
import com.kms.katalon.core.webui.common.WebUiCommonHelper

KeywordLogger log = new KeywordLogger()

/* Switch to the Notes tab, count the number of notes
 * Create a new Note, then count the number of notes again
 * The number should increase by 1 if successful
 * Created 02-07-2018 09:31AM by Pete Wilson
 * Updated 08-16-2018 09:45AM by Pete Wilson
 * Updated 11-30-2018 13:24PM by Pete Wilson
 */

//Location of the Note objects on the page
xpath="//*[@role='note-show-body']"
int originalNumberOfNotes=0

WebUI.click(findTestObject('ABC/Customer Profile/Tabs/tab-Notes'))

log.logWarning('--- Count and Create Notes ---')

WebUI.delay(1)

//Check if there are existing Notes on the page and count how many
boolean elementVisible=CustomKeywords.'tools.commonCode.verifyObjectPresent'('ABC/Customer Profile/Notes/text-Existing Note')
if (elementVisible==true){
	originalNumberOfNotes=CustomKeywords.'tools.commonCode.countRowsPerPage'(xpath)
} else {
	log.logWarning('Customer has no Notes available at this time')
}

log.logWarning("Number of Notes listed on the Prospect Profile:= " + originalNumberOfNotes)

WebUI.sendKeys(findTestObject('ABC/Customer Profile/Notes/textarea-New Note'), 'Aenean eu leo quam. Pellentesque ornare sem lacinia quam venenatis vestibulum. Sed posuere consectetur est at lobortis. Integer posuere erat a ante venenatis dapibus posuere velit aliquet. Sed posuere consectetur est at lobortis.')

WebUI.click(findTestObject('ABC/Customer Profile/Notes/btn-Save'))

WebUI.delay(2)

//Get the new number of Notes on the page
int updatedNumberOfNotes=CustomKeywords.'tools.commonCode.countRowsPerPage'(xpath)

log.logWarning("Updated Number of Notes listed on the Prospect Profile:= " + updatedNumberOfNotes)

//Confirm the number of contacts has increased by 1
if (updatedNumberOfNotes-originalNumberOfNotes==1){
	log.logWarning('SUCCESS: A new Note has been created')
} else {
	log.logWarning('ERROR: A problem has occurred creating the Note')
	KeywordUtil.markFailed('ERROR: A problem has occurred creating the Note')
}