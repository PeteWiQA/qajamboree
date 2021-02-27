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

/* This test case is an example of how to call a Test Case to set Global Variables
 * And the same example using Custom Keywords.
 * The first example calls the Test Case which populates Global Variables for customer information
 * It populates Name, address, city, state, zip, company name, email address and phone number
 * All the fields are populated whether they are needed are not. This would be useful to populate
 * a Contact form with useable, but random information.
 * 
 * The second test gathers the same information but does so using Custom Keywords
 * Each keyword call creates a piece of information, as needed.
 * The same information Name, address, city, state, zip, company name, email address and phone number
 * are created, but individually, rather than in bulk. This means a name can be created whenever it's needed.
 * For example, if 3 user names are needed, a call to the getProperName keyword (function) will produce a new
 * name each time.
 */
WebUI.callTestCase(findTestCase('Generate Contact Information/Generate Contact Information'), [:], FailureHandling.STOP_ON_FAILURE)

log.logWarning('--- Start of Global Variable Test ---')

//Generate a contact card
log.logWarning(GlobalVariable.fullName)
log.logWarning(GlobalVariable.houseAddress + ' ' + GlobalVariable.streetAddress+ ' ' + GlobalVariable.streetSuffix)
log.logWarning(GlobalVariable.cityName + ' ' + GlobalVariable.stateAbbr + ', ' + GlobalVariable.zipCode)
log.logWarning(GlobalVariable.companyName)
log.logWarning(GlobalVariable.emailAddress)
log.logWarning(GlobalVariable.phoneNumber)

log.logWarning('--- End of Global Variable Test ---')

log.logWarning('--- Start of Keywords Test ---')

userFullName=CustomKeywords.'createUserDetails.contactInformation.getProperName'()
log.logWarning(userFullName)

streetAddress=CustomKeywords.'createUserDetails.contactInformation.getStreetName'()
log.logWarning(streetAddress)

cityName=CustomKeywords.'createUserDetails.contactInformation.getCityName'()
stateAbbr=CustomKeywords.'createUserDetails.contactInformation.getStateAbbr'()
zipCode=CustomKeywords.'createUserDetails.contactInformation.getZipCode'()
log.logWarning(cityName + ' ' + stateAbbr + ', ' + zipCode)

companyName=CustomKeywords.'createUserDetails.contactInformation.getCompanyName'()
log.logWarning(companyName)

emailAddress=CustomKeywords.'createUserDetails.contactInformation.createEmail'(companyName, userFullName)
log.logWarning(emailAddress)

phoneNumber=CustomKeywords.'createUserDetails.contactInformation.getPhoneNumber'()
log.logWarning(phoneNumber)

log.logWarning('--- End of Keywords Test ---')
