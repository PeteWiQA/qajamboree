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

KeywordLogger log = new KeywordLogger()

/*Switch to the Contact tab, count the number of contacts
 *Create a new Contact, then count the number of contacts again
 *The number should increase by 1 if successful
 *Created 02-07-2018 09:31AM by Pete Wilson
 *Updated 02-15-2018 10:44AM by Pete Wilson
 *Updated 02-22-2018 11:49AM by Pete Wilson
 *Updated 08-16-2018 09:51AM by Pete Wilson
 *Updated 11-30-2018 13:24PM by Pete Wilson
 */

xpath="//*[@role='contact-show-container']"
int originalNumberOfContacts=0

WebUI.click(findTestObject('ABC/Customer Profile/Tabs/tab-Contacts'))

log.logWarning('--- Count and Create Contacts ---')

WebUI.delay(1)

//Check if there are existing Contacts on the page and count how many
boolean elementVisible=CustomKeywords.'tools.commonCode.verifyObjectPresent'('ABC/Customer Profile/Contacts/text-Existing Contact')
if (elementVisible==true){
	originalNumberOfContacts=CustomKeywords.'tools.commonCode.countRowsPerPage'(xpath)
} else {
	log.logWarning('Customer has no Contacts available at this time')
}

log.logWarning("Number of Contacts listed on the Prospect Profile:= " + originalNumberOfContacts)

//Verify the New Contact button is visible
//elementVisible=WebUI.verifyElementPresent(findTestObject('ABC/Customer Profile/Contacts/btn-New Contact'), 5, FailureHandling.CONTINUE_ON_FAILURE)
elementVisible=CustomKeywords.'tools.commonCode.verifyObjectPresent'('ABC/Customer Profile/Contacts/btn-New Contact')
if (elementVisible==true){

	//If the New Contact buttons exists, Create a new Contact for the Profile
	WebUI.click(findTestObject('ABC/Customer Profile/Contacts/btn-New Contact'))

	WebUI.delay(1)

	//The fields of the form
	WebUI.setText(findTestObject('ABC/Customer Profile/Contacts/input-Full Name'), 'Peter Wilson')

	WebUI.setText(findTestObject('ABC/Customer Profile/Contacts/input-Role or Title'), 'New Title or Role')

	WebUI.sendKeys(findTestObject('ABC/Customer Profile/Contacts/input-Voice Phone Number'), '9802021111')

	WebUI.sendKeys(findTestObject('ABC/Customer Profile/Contacts/input-Mobile Phone Number'), '9803031112')

	WebUI.sendKeys(findTestObject('ABC/Customer Profile/Contacts/input-Office Phone Number'), '9804041113')

	WebUI.setText(findTestObject('ABC/Customer Profile/Contacts/input-Email Address'),'peter.wilson@technekes.com')

	WebUI.setText(findTestObject('ABC/Customer Profile/Contacts/input-Additional Information'), 'More Additional Information')

	WebUI.delay(1)

	//Save the form and give a delay. The page needs to refresh, so we have to pause
	WebUI.click(findTestObject('ABC/Customer Profile/Contacts/btn-Save'))

	WebUI.delay(5)

	//Count the new number of Contacts on the page
	int updatedNumberOfContacts=CustomKeywords.'tools.commonCode.countRowsPerPage'(xpath)

	log.logWarning("Updated Number of Contacts listed on the Prospect Profile:= " + updatedNumberOfContacts)

	//Confirm the number of contacts has increased by 1
	if (updatedNumberOfContacts-originalNumberOfContacts==1){
		log.logWarning('SUCCESS: A new Contact has been created')
	} else {
		log.logError('ERROR: A problem has occurred creating the Contact')
		KeywordUtil.markFailed('ERROR: A problem has occurred creating the Contact')

	}
} else {
	//The Create Contact button doesn't exist. Display an error in the log and mark the test as failed since it didn't run correctly.
	log.logError('ERROR: The New Contact Button is not available for this Customer')
}
