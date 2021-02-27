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
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW

KeywordLogger log = new KeywordLogger()

/* The purpose of this test is to create a prospect for the Sales Rep
 * The name of the Prospect will be Amazing New Company with the
 * current date and time appended. This lets us know when the propsect
 * was created and generates a small level of randomness
 * Updated 01-26-2018 11:20AM by Pete Wilson
 * Updated 02-01-2018 10:58AM by Pete Wilson
 * Updated 03-24-2018 12:39PM by Pete Wilson
 * Updated 04-19-2018 16:32PM by Pete Wilson
 * Updated 08-14-2018 13:55PM by Pete Wilson
 */
xpath = '//*[@role=\'task-show-complete-button\']'

int originalNumberOfTasks = 0
int counter=0

WebUI.navigateToUrl(GlobalVariable.baseurl + '/prospects')

//Check to make sure the Prospect page loads. Look for the text, All Prospects
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('All Prospects')

//Verify that the page loaded successfully and there are no "There was an error retreiving results" messaages
CustomKeywords.'tools.commonCode.checkForAlert'()

//The date is appended to the name of the company to create randomness and show when the Prospect was created
String formattedDate=CustomKeywords.'tools.commonCode.getDateAndTime'()

//The following fills in the fields of the form
WebUI.click(findTestObject('ABC/Prospects/New Prospect Fields/btn-Create New Prospect Button'))

log.logWarning('--- Filling in New Prospect Form ---')
//When the test runs multiple times, multiple dates are appended to the company name. Remove that extra text.
GlobalVariable.companyName=GlobalVariable.companyName.split("-")[0].trim()
WebUI.setText(findTestObject('ABC/Prospects/New Prospect Fields/input-Prospect-Company Name'), (GlobalVariable.companyName +
    ' - ' + formattedDate))

GlobalVariable.companyName = (GlobalVariable.companyName + ' - ' + formattedDate)

WebUI.setText(findTestObject('ABC/Prospects/New Prospect Fields/input-Prospect-First Name Last Name'), 'Peter Wilson')

WebUI.setText(findTestObject('ABC/Prospects/New Prospect Fields/input-Prospect-Role or Title'), 'New Title of Sales Rep')

WebUI.setText(findTestObject('ABC/Prospects/New Prospect Fields/input-Prospect-Contact Phone'), '9802221112')

WebUI.setText(findTestObject('ABC/Prospects/New Prospect Fields/input-Prospect-Contact Mobile'), '9803331113')

WebUI.setText(findTestObject('ABC/Prospects/New Prospect Fields/input-Prospect-Contact Office'), '9804441114')

WebUI.setText(findTestObject('ABC/Prospects/New Prospect Fields/input-Prospect-Contact Email'), 'peter.wilson.contact@technekes.com')

WebUI.setText(findTestObject('ABC/Prospects/New Prospect Fields/input-Prospect-Additional Information'), 'New Prospect Created for QA')

WebUI.setText(findTestObject('ABC/Prospects/New Prospect Fields/input-Prospect-Address Line 1'), '1313 Mockingbird Ln')

WebUI.setText(findTestObject('ABC/Prospects/New Prospect Fields/input-Prospect-City'), 'Charlotte')

WebUI.setText(findTestObject('ABC/Prospects/New Prospect Fields/input-Prospect-State'), 'NC')

WebUI.setText(findTestObject('ABC/Prospects/New Prospect Fields/input-Prospect-Zip Code'), '28203')

WebUI.setText(findTestObject('ABC/Prospects/New Prospect Fields/input-Prospect-Phone Number'), '9805551212')

WebUI.setText(findTestObject('ABC/Prospects/New Prospect Fields/input-Prospect-Email Address'), 'peter.wilson@technekes.com')

WebUI.click(findTestObject('ABC/Prospects/New Prospect Fields/btn-Prospect-Save Button'))

//In many cases a confirmation dialog is presented. This Accepts that dialog and allows the script to continue
counter=0
boolean elementPresent=WebUI.waitForAlert(1)
while(elementPresent!=true && counter<=20){
	if (counter>=20){
		log.logError("ERROR: The item was not saved correctly.")
	}
	elementPresent=WebUI.waitForAlert(1)
	counter++
}

WebUI.acceptAlert()

WebUI.refresh()

//Verify that the page loaded successfully and there are no "There was an error retreiving results" messaages
CustomKeywords.'tools.commonCode.checkForAlert'()

//Search for the newly created Prospect
WebUI.setText(findTestObject('ABC/Prospects/input-Prospect Search'), GlobalVariable.companyName)

log.logWarning('--- Searching for Prospect - ' + GlobalVariable.companyName)

WebUI.click(findTestObject('ABC/Prospects/btn-Go'))

//Confirm the newly created Prospect is the returned Prospect
counter=0
WebUI.waitForElementVisible(findTestObject('ABC/Prospects/text-Prospect Name', [('Variable') : 2]),30)
tempText=WebUI.getText(findTestObject('ABC/Prospects/text-Prospect Name', [('Variable') : 2]))
while(tempText!=GlobalVariable.companyName && counter<=20) {
	if (counter>=20){
		log.logError("ERROR: The Prospect does not appear in the search results.")
		log.logError("ERROR: There could be an error with Elastic Search")
		KeywordUtil.markError('ERROR: There could be an error with Elastic Search')
	}
	WebUI.delay(1)
	tempText=WebUI.getText(findTestObject('ABC/Prospects/text-Prospect Name', [('Variable') : 2]))
	counter++
}

WebUI.click(findTestObject('ABC/Prospects/text-Prospect Name', [('Variable') : 2]))

WebUI.waitForElementVisible(findTestObject('ABC/Customer Profile/Tabs/tab-Tasks'), 15)

WebUI.click(findTestObject('ABC/Customer Profile/Tabs/tab-Tasks'))

//Check if there are existing Tasks on the page and count how many
boolean elementVisible = CustomKeywords.'tools.commonCode.verifyObjectPresent'('ABC/Customer Profile/Tasks/text-Existing Task')

if (elementVisible == true) {
    originalNumberOfTasks = CustomKeywords.'tools.commonCode.countRowsPerPage'(xpath)
} else {
    log.logWarning('Customer has no Tasks available at this time')
}

log.logWarning('Number of Tasks listed on the Prospect Profile:= ' + originalNumberOfTasks)

WebUI.sendKeys(findTestObject('ABC/Customer Profile/Tasks/input-Task Subject'), ('New Prospect Task Subject from QA - ' + formattedDate))

WebUI.sendKeys(findTestObject('ABC/Customer Profile/Tasks/textarea-Task Note'), ('New Prospect Task Note from QA - ' + formattedDate))

//There is an oddity where the date has to entered with the year first.
WebUI.sendKeys(findTestObject('ABC/Customer Profile/Tasks/input-Due Date'), '2019-12-31')

WebUI.click(findTestObject('ABC/Customer Profile/Tasks/btn-Add'))

//Return to Home page and confirm the entered Task is visible

WebUI.navigateToUrl(GlobalVariable.baseurl)

log.logWarning('Searching in Upcoming Tasks for Task Named: New Prospect Task Subject from QA - ' + formattedDate + ' on the Sales Rep Home page')
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('New Prospect Task Subject from QA - ' + formattedDate)
