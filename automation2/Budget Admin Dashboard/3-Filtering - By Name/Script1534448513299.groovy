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
import org.openqa.selenium.Keys as Keys

KeywordLogger log = new KeywordLogger()

/* This script validates figures and filters on the Budget Admin Dashboard
 * It performs a branch search and gathers information about the first record returned
 * It searches for a particular sales rep and returns the information listed on the information card
 * It Clicks through each filter and confirms the number of results matches the number in the footer for pagination
 * It Selects - All
 * The filters under No Budget
 * The filters under Budget Present
 * 
 * Updated 04-20-2018 14:35PM by Pete Wilson
 * Updated 08-16-2018 16:15PM by Pete Wilson
 */
WebUI.navigateToUrl(GlobalVariable.baseurl + '/admin/budgets')

WebUI.delay(2)

//---- Filter by Name or #: ----
WebUI.setText(findTestObject('ABC/Budget Admin Dashboard/Filter By Name Input Field'), GlobalVariable.impersonatedUserName)

WebUI.delay(5)

//WebUI.sendKeys(findTestObject('ABC/Budget Admin Dashboard/Filter By Name Input Field'), Keys.chord(Keys.TAB))
WebUI.waitForElementVisible(findTestObject('ABC/Budget Admin Dashboard/card-Branch Number'), 15)

log.logWarning('Sales Rep Branch: ' + WebUI.getText(findTestObject('ABC/Budget Admin Dashboard/card-Branch Number')))

//Check the name of the returned Sales Rep against the name entered
salesRepName=WebUI.getText(findTestObject('ABC/Budget Admin Dashboard/card-Sales Rep Name'))
log.logWarning('Sales Rep Name: ' + salesRepName)
if (salesRepName!=GlobalVariable.impersonatedUserName){
	log.logError('ERROR: For the Budget Admin, a Sales Rep name was returned, but it did not match the search criteria')
	log.logError('Search Criteria: ' + GlobalVariable.impersonatedUserName)
	log.logError('Sales Rep name ' + salesRepName)
}

log.logWarning('Sales Rep Number: ' + WebUI.getText(findTestObject('ABC/Budget Admin Dashboard/card-Sales Rep Number')))

log.logWarning('Sales Rep Role: ' + WebUI.getText(findTestObject('ABC/Budget Admin Dashboard/card-Sales Rep Role')))

log.logWarning('Sales Rep Region: ' + WebUI.getText(findTestObject('ABC/Budget Admin Dashboard/card-Sales Rep Region')))

log.logWarning('Sales Rep Total Planned Budget Amount ' + WebUI.getText(findTestObject('ABC/Budget Admin Dashboard/Budget-Total Planned Value')))

WebUI.setText(findTestObject('ABC/Budget Admin Dashboard/Filter By Name Input Field'), '')

