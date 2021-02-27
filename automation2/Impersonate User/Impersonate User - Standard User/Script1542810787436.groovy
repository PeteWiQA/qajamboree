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

/* This test loads the Impersonation page and passes the username
 * It will check that the Impersonation page loads as expected
 * And logs the name of the impersonated user the tests will 
 * be run against
 * Updated 04-19-2018 13:13PM by Pete Wilson
 * Updated 08-09-2018 10:56AM by Pete Wilson
 * Updated 11-21-2018 09:29AM by Pete Wilson
 * Updated 11-26-2018 14:45PM by Pete Wilson
 * Updated 12-03-2018 12:16PM by Pete Wilson
 * Updated 07-17-2019 13:19PM by Pete Wilson
 * Updated 09-26-2019 10:29AM by Pete Wilson
 * 
 * This test was changed to incorporate the users role and determine if the user is a manager
 * This has an impact on the Sales Dashboard. Managers have a dropdown to disply their reps and
 * this shifts the pathing for the Dashboard. Because of that we need the "accordionValue" to locate items
 * 
 * The timing was also changed so if the Impersonation table isn't populated or the user we want to
 * impersonate isn't visible yet, the test will retry rather than impersonating the wrong user.
 */

WebUI.navigateToUrl(GlobalVariable.baseurl + '/impersonation')
WebUI.waitForPageLoad(30)
int counter=0
boolean elementVisible
String userName, userRole

//Check to make sure the Sales Dashboard page loads. Look for the text, View ABC Mobile as another user
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('View ABC Mobile as another user')

elementVisible=WebUI.waitForElementVisible(findTestObject('ABC/Impersonate User/table-User Role Details', [('row') : 1, ('column') : 1]), 30)
if (!elementVisible){
	log.logFailed("ERROR: Error loading Impersonation page...")
	KeywordUtil.markFailedAndStop("ERROR: Error loading Impersonation page...")
}

elementVisible=WebUI.waitForElementVisible(findTestObject('ABC/Impersonate User/btn-View as User'), 30)
if (!elementVisible){
	log.logFailed("ERROR: Error loading Impersonation page...")
	KeywordUtil.markFailedAndStop("ERROR: Error loading Impersonation page...")
}

//Verify there are users listed on the page that can be impersonated
if (elementVisible){
	//Impersonate a user, set user name and role

	WebUI.setText(findTestObject('ABC/Impersonate User/input-Impersonation Search'), GlobalVariable.impersonatedUser)
	WebUI.click(findTestObject('ABC/Impersonate User/btn-Search'))
	WebUI.waitForJQueryLoad(10)

	//User Name in column 1, User Role in column 5
	userName=WebUI.getText(findTestObject('ABC/Impersonate User/table-User Role Details', [('row') : 1, ('column') : 1]))
	while(userName!=GlobalVariable.impersonatedUserName && counter<=20) {
		if (counter>=20){
			log.logError('ERROR: Impersonation has failed. Login and User Name DO NOT match')
			log.logError('Could not find user: ' + GlobalVariable.impersonatedUserName)
			KeywordUtil.markFailedAndStop('ERROR: Impersonation has failed. Login and User Name DO NOT match')			
		}
		WebUI.delay(1)
		userName=WebUI.getText(findTestObject('ABC/Impersonate User/table-User Role Details', [('row') : 1, ('column') : 1]))
		counter++
	}
	
	userRole=WebUI.getText(findTestObject('ABC/Impersonate User/table-User Role Details', [('row') : 1, ('column') : 5]))

	/* Determine if the user is a Sales Rep or  Manager. The Sales Dashboard accordions has different xpath locators based on role
	 * When user is Manager:
	 * Daily Sales - //div[@id='sales_dashboard_index']/div[3]/div/a
	 * 
	 * When user is Sales Rep:
	 * Daily Sales - //div[@id='sales_dashboard_index']/div[2]/div/a
	 */


	if (userRole.contains('manager')==true){
		GlobalVariable.dashboardAccordionValue=3
	} else {
		GlobalVariable.dashboardAccordionValue=2
	}

	WebUI.click(findTestObject('ABC/Impersonate User/btn-View as User'))

	WebUI.waitForPageLoad(30)
	//Wait for the impersonation banner to appear
	WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Common Objects/text-Impersonation Alert'), 20)

	/* Read the user name from the page and output to the log
	 * Make sure we are impersonating the right user
	 * There have been cases where this fails in QA
	 */

	impersonatedUserName=WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/text-Currently Impersonated User'))
	if (userName!=impersonatedUserName){
		log.logWarning('ERROR: Impersonation has failed. Login and User Name DO NOT match')
		log.logWarning('Attempted to Impersonate: ' + GlobalVariable.impersonatedUser)
		log.logWarning('Logged in as: ' + impersonatedUserName)
		log.logFailed('ERROR: Impersonation has failed. Login and User Name DO NOT match')
		KeywordUtil.markFailed('ERROR: Impersonation has failed. Login and User Name DO NOT match')

	} else {
		log.logWarning('Running tests as Impersonated User:=' + impersonatedUserName)
	}
	
} else {
	//The Impersonation page is blank and there is an error
	log.logWarning('ERROR: There was an error loading the impersonation page.')
	log.logFailed('ERROR: There was an error loading the impersonation page.')
	KeywordUtil.markFailed('ERROR: There was an error loading the impersonation page')
	
}