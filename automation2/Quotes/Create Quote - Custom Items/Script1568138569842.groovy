import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger

KeywordLogger log = new KeywordLogger()

/* The purpose of this quote is to make a quote with multiple Custom Items
 * This is more of a deep dive test and would not normally be used for
 * regression testing. This was made to help with performance testing.
 * 
 * Created 09-10-2019 15:11PM by Pete Wilson 
 * 
 */

//Set the date and time
String formattedDate=CustomKeywords.'tools.commonCode.getDateAndTime'()

WebUI.navigateToUrl(GlobalVariable.baseurl + '/quotes')

WebUI.delay(2)

log.logWarning('--- Creating New Quote ---')

//Because of slow page load, wait for the Create New Quote button to appear
WebUI.waitForElementVisible(findTestObject('ABC/Quotes/Create Quote/btn-Create New Quote'), 15)

//Click the Create New Quote button
WebUI.click(findTestObject('ABC/Quotes/Create Quote/btn-Create New Quote'))

//Wait for the field Customer Name to be visible-meaning the page has loaded
WebUI.waitForElementVisible(findTestObject('ABC/Quotes/Create Quote/input-Customer Name'), 15)

//Select this as a new customer
WebUI.click(findTestObject('ABC/Quotes/Create Quote/checkbox-New Customer'))

//Enter a customer name
WebUI.setText(findTestObject('ABC/Quotes/Create Quote/input-Customer Name'), 'Technekes')

//Enter the contact name
WebUI.setText(findTestObject('ABC/Quotes/Create Quote/input-Contact Name'), 'Peter Wilson')

//Enter the phone number
WebUI.setText(findTestObject('ABC/Quotes/Create Quote/input-Phone Number'), '980-555-1212')

//Select a phone type of Phone
WebUI.selectOptionByLabel(findTestObject('ABC/Quotes/Create Quote/select-Phone Number Type'), 'Phone', false)

//Enter the name of the Quote
WebUI.setText(findTestObject('ABC/Quotes/Create Quote/input-Quote Name'), ('Test Quote created from QA - ' + formattedDate))

//These are the optional fields of the form. They have been added so we have the objects and can use them
//Enter the Job Address 1
WebUI.setText(findTestObject('ABC/Quotes/Create Quote/input-Job Address 1'), '1313 Mockingbird Ln')

//Enter the City
WebUI.setText(findTestObject('ABC/Quotes/Create Quote/input-Job City'), 'Charlotte')

//Select the state from the dropdown
WebUI.selectOptionByLabel(findTestObject('ABC/Quotes/Create Quote/select-Job State'), 'North Carolina', false)

//Enter the zip code
WebUI.setText(findTestObject('ABC/Quotes/Create Quote/input-Quote Job Zip Code'), '28203')

//Enter the Jobsite Contact Name
WebUI.setText(findTestObject('ABC/Quotes/Create Quote/input-Jobsite Contact Name'), 'Peter Wilson')

//Enter Jobsite Contact Phone
WebUI.setText(findTestObject('ABC/Quotes/Create Quote/input-Jobsite Contact Phone'), '980-555-1213')

//Click the Create Quote button to move to the next screen and add an item to the inventory
WebUI.click(findTestObject('ABC/Quotes/Create Quote/btn-Create Quote'))

log.logWarning('--- Completed New Quote Page ---')


for (loop = 1; loop <=150; loop++) {

	WebUI.waitForElementVisible(findTestObject('ABC/Quotes/Create Quote/Custom Item/btn-Add Custom Item'), 15)

	//Click to Add Inventory
	WebUI.click(findTestObject('ABC/Quotes/Create Quote/Custom Item/btn-Add Custom Item'))

	//Wait for Product Name field to appear
	WebUI.waitForElementVisible(findTestObject('ABC/Quotes/Create Quote/Custom Item/input-Custom Item Name'), 15)

	//Enter the Custom inventory item
	WebUI.setText(findTestObject('ABC/Quotes/Create Quote/Custom Item/input-Custom Item Name'), 'Custom Item #' + loop)

	//Enter the Custom inventory item Price
	WebUI.setText(findTestObject('ABC/Quotes/Create Quote/Custom Item/input-Custom Item Price'), '99.99')

	//Select the Custom inventory item UOM
	WebUI.selectOptionByLabel(findTestObject('ABC/Quotes/Create Quote/Custom Item/select-Custom Item UOM'), 'EA - EACH', false)

	//Enter the Custom inventory item Qty
	WebUI.setText(findTestObject('ABC/Quotes/Create Quote/Custom Item/input-Custom Item Qty'), '2')

	//Click to Add Custom Item to Inventory
	WebUI.click(findTestObject('ABC/Quotes/Create Quote/Custom Item/btn-Save Custom Item'))
}