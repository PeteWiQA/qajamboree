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
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil

boolean elementVisible

KeywordLogger log = new KeywordLogger()

/* This test loads the Impersonation page and passes the username
 * It will check that the Impersonation page loads as expected
 * And logs the name of the impersonated user the tests will 
 * be run against
 * 
 * Updated 04-19-2018 13:13PM by Pete Wilson
 * Updated 08-09-2018 10:56AM by Pete Wilson
 * Updated 11-16-2018 10:22AM by Pete Wilson
 * 
 * A separate Impersonation test was created based on the issue in https://technekes.atlassian.net/browse/AADSA-1174
 * Interal users should not be able to access the website outside the firewall
 * Those with "external" as part of their role can access the website as normal
 * This test impersonates user cesar.aguilera, to confirm they get the message
 * "You are not allowed to access the site from your current location"
 * The site details should not load correctly for these users from our location
 */

//Login as an "external" user
WebUI.callTestCase(findTestCase('Login/Login - Standard User'), [:])

WebUI.navigateToUrl(GlobalVariable.baseurl + '/impersonation')

//Check to make sure the Sales Dashboard page loads. Look for the text, View ABC Mobile as another user
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('View ABC Mobile as another user')

//Impersonate Cesar Aguilera as he is an internal user and should not be able to access the site from our location
WebUI.setText(findTestObject('ABC/Impersonate User/input-Impersonation Search'), 'joseph.ailstock')

WebUI.delay(1)

WebUI.click(findTestObject('ABC/Impersonate User/btn-View as User'))

WebUI.delay(1)

/* Confirm the error message, "You are not allowed to access the site from your current location" is displayed
 * This is what an internal user should see. If the page loads correctly, that is an error
 */

elementVisible = WebUI.verifyTextPresent('You are not allowed to access the site from your current location', false, FailureHandling.CONTINUE_ON_FAILURE)

if (elementVisible == true) {
    log.logWarning('SUCCESS: The Internal Sales Rep is not able to access the site outside the firewall')
} else {
    log.logWarning('ERROR: The Internal Sales Rep is able to access the site outside the firewall')

    log.logFailed('ERROR: The Internal Sales Rep is able to access the site outside the firewall')

    KeywordUtil.markFailed('ERROR: The Internal Sales Rep is able to access the site outside the firewall')
}

//Close out the browser session as it won't be used for testing
WebUI.deleteAllCookies()
WebUI.closeBrowser()