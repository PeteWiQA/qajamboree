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

/* The purpose of this test is to confirm that a Contact entry on the
 * Customer Profile page can be edited. The test picks the first Contact
 * on the page, selects Edit and enters new information. The Save action
 * is then confirmed
 * 
 * Created 05-02-2019 15:10PM by Pete Wilson 
 */

WebUI.click(findTestObject('ABC/Customer Profile/Tabs/tab-Contacts'))

WebUI.delay(1)

//Check if there are existing Contacts on the page and count how many
boolean elementVisible

//Verify the New Contact button is visible
//elementVisible=WebUI.verifyElementPresent(findTestObject('ABC/Customer Profile/Contacts/btn-New Contact'), 5, FailureHandling.CONTINUE_ON_FAILURE)
elementVisible=CustomKeywords.'tools.commonCode.verifyObjectPresent'('ABC/Customer Profile/Contacts/btn-New Contact')
if (elementVisible==true){
	
	contactName=WebUI.getText(findTestObject('ABC/Customer Profile/Contacts/Edit Contacts/text-Existing Contact Name'))

	//If the New Contact buttons exists, Create a new Contact for the Profile
	WebUI.click(findTestObject('ABC/Customer Profile/Contacts/btn-Edit Contact'))

	WebUI.delay(1)

	//The fields of the form
	
	WebUI.setText(findTestObject('ABC/Customer Profile/Contacts/Edit Contacts/input-Contact Input Field', [('idName') : 'full_name']), contactName + ' - Edit')

	WebUI.setText(findTestObject('ABC/Customer Profile/Contacts/Edit Contacts/input-Contact Input Field', [('idName') : 'role']), 'New Title or Role')

	WebUI.sendKeys(findTestObject('ABC/Customer Profile/Contacts/Edit Contacts/input-Contact Input Field', [('idName') : 'voice_phone_number']), '9802021111')

	WebUI.sendKeys(findTestObject('ABC/Customer Profile/Contacts/Edit Contacts/input-Contact Input Field', [('idName') : 'mobile_phone_number']), '9803031112')

	WebUI.sendKeys(findTestObject('ABC/Customer Profile/Contacts/Edit Contacts/input-Contact Input Field', [('idName') : 'office_phone_number']), '9804041113')

	WebUI.setText(findTestObject('ABC/Customer Profile/Contacts/Edit Contacts/input-Contact Input Field', [('idName') : 'email']), 'peter.wilson@technekes.com')

	WebUI.setText(findTestObject('ABC/Customer Profile/Contacts/Edit Contacts/input-Additional Information'), 'More Additional Information')

	WebUI.delay(1)

	//Save the form and give a delay. The page needs to refresh, so we have to pause
	WebUI.click(findTestObject('ABC/Customer Profile/Contacts/Edit Contacts/btn-Save Contact'))

	WebUI.delay(2)
	
	CustomKeywords.'tools.commonCode.checkForAlert'()

} else {
	//The Create Contact button doesn't exist. Display an error in the log and mark the test as failed since it didn't run correctly.
	log.logError('ERROR: The New Contact Button is not available for this Customer')
}
