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
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import groovy.transform.Field
import groovy.time.TimeCategory
import groovy.time.TimeDuration

KeywordLogger log = new KeywordLogger()

/* This script will create a quote for an Existing Customer
 * The test will load the Sales Dashboard, YTD Sales Detail - By Customer tab
 * and read the first name off the list.
 * An inventory search will then be performed to add items to the quote that are
 * available at the Sales Rep branch
 * These items are added to the quote and the quote is saved and sent
 * Multiple validations steps are performed
 * - Check the branch number matches the sales rep
 * - Confirm the account name and name of the quote appear in the header
 * - Confirm the items added to the quote exist and are for the correct branch
 * - Confirm Fuel Surcharge and Delivery are added
 * - Confirm the price of the items is not $0
 * - Confirm the overall price of the quote is over $125
 *
 * Updated 03-13-2019 12:05PM by Pete Wilson
 * Updated 10-08-2019 15:22PM by Pete Wilson
 */

//add the freight option to the quote?
boolean loadFreight=false
//add a custom item to the quote?
boolean addCustomItem=true
//display time it takes to save the quote?
boolean timedTest=true
//Read inventory from the database file or xls spreadshseet?
boolean useDataBase=false
//Use the option to just Save the quote or the Save & Send Quote option?
boolean saveQuote=false
//Determine if an element is visible or not
boolean elementVisible
Date timeStart, timeStop
@Field String customerName
customerName=GlobalVariable.companyName
//Set date parameters
@Field String formattedDate
formattedDate=CustomKeywords.'tools.commonCode.getDateAndTime'()

/* Determine the list of inventory items to add to the quote. Either use a DB query
 * or search for a set of items local to the Sales rep.
 * The other Custom Inventory List reads values from an XLSX file
 */

if (useDataBase==true){
	WebUI.callTestCase(findTestCase('Quotes/Populate Custom Inventory List - DB'), [:], FailureHandling.CONTINUE_ON_FAILURE)
}else{
	//WebUI.callTestCase(findTestCase('Quotes/Populate Custom Inventory List'), [:], FailureHandling.CONTINUE_ON_FAILURE)
	WebUI.callTestCase(findTestCase('Quotes/Populate Inventory Items List'), [:], FailureHandling.CONTINUE_ON_FAILURE)
}

WebUI.navigateToUrl(GlobalVariable.baseurl + '/quotes')

WebUI.delay(2)

log.logWarning('--- Creating New Quote ---')

//Because of slow page load, wait for the Create New Quote button to appear
WebUI.waitForElementVisible(findTestObject('ABC/Quotes/Create Quote/btn-Create New Quote'), 15)

//Click the Create New Quote button
WebUI.click(findTestObject('ABC/Quotes/Create Quote/btn-Create New Quote'))

//Wait for the field Customer Name to be visible-meaning the page has loaded
WebUI.waitForElementVisible(findTestObject('ABC/Quotes/Create Quote/input-Customer Name'), 15)

//Confirm the listed branch based on Division
String allQuoteBranchNames = WebUI.getText(findTestObject('ABC/Quotes/Create Quote/select-Branch Name'))
//The String contains all the options from the dropdown. Parse them out based on CRLF
List allQuoteBranchesList=allQuoteBranchNames.split("\\r?\\n")
/* Get the first name from the dropdown list. This is the default Branch for the user
 * quoteBranchName uses @Field to be accessible by the addInventoryItem method to confirm the location of the item is related
 * to the sales rep location.
 */
@Field String quoteBranchName
quoteBranchName=allQuoteBranchesList[0]

log.logWarning('The listed Branch in the quote is: ' + quoteBranchName)

if (GlobalVariable.userDivision.equalsIgnoreCase('ABC')) {
    if (quoteBranchName.contains('ABC Supply') == false) {
        log.logError('ERROR: There is an error with the user Branch Association on the Quote')

        log.logError('The user is of type - ' + GlobalVariable.userDivision + ' - The Branch listed is - ' + quoteBranchName)
    }
} else {
    if (quoteBranchName.contains('TCI') == false) {
        log.logError('ERROR: There is an error with the user Branch Association on the Quote')

        log.logError('The user is of type - ' + GlobalVariable.userDivision + ' - The Branch listed is - ' + quoteBranchName)
    }
}

//Select this as a new customer
WebUI.click(findTestObject('ABC/Quotes/Create Quote/checkbox-New Customer'))

//Enter a customer name
WebUI.setText(findTestObject('ABC/Quotes/Create Quote/input-Customer Name'), customerName)

//Enter the contact name
WebUI.setText(findTestObject('ABC/Quotes/Create Quote/input-Contact Name'), 'Peter Wilson')

//Enter the phone number
WebUI.setText(findTestObject('ABC/Quotes/Create Quote/input-Phone Number'), '980-555-1212')

//Select a phone type of Phone
WebUI.selectOptionByLabel(findTestObject('ABC/Quotes/Create Quote/select-Phone Number Type'), 'Phone', false)

//Enter the name of the Quote
WebUI.setText(findTestObject('ABC/Quotes/Create Quote/input-Quote Name'), ('Test Quote created from QA - ' + formattedDate))
GlobalVariable.quoteName='Test Quote created from QA - ' + formattedDate

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

if (loadFreight==true){
	//Set the Order Type to Delivery. This generates a secondary dropdown
	WebUI.selectOptionByLabel(findTestObject('ABC/Quotes/Create Quote/select-Order Type'), 'Delivery', false)
	//Set the Delivery Method to Ground Drop in the dropdown. This is a required field based on the selection above.
	//This also adds Fuel and Delivery Surcharges which we don't often work with, so that's a good thing
	WebUI.selectOptionByLabel(findTestObject('ABC/Quotes/Create Quote/select-Order Delivery Method'), 'Ground Drop', false)
}

//Enter text for the Quote Notes
WebUI.setText(findTestObject('ABC/Quotes/Create Quote/textarea-Quote Notes'), 'Aenean eu leo quam. Pellentesque ornare sem lacinia quam venenatis vestibulum. Sed posuere consectetur est at lobortis. Integer posuere erat a ante venenatis dapibus posuere velit aliquet. Sed posuere consectetur est at lobortis.\r\rCurabitur blandit tempus porttitor. Nullam quis risus eget urna mollis ornare vel eu leo. Cras justo odio, dapibus ac facilisis in, egestas eget quam. Duis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit. Nulla vitae elit libero, a pharetra augue. Sed posuere consectetur est at lobortis.\r\rDuis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed posuere consectetur est at lobortis. Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor auctor. Integer posuere erat a ante venenatis dapibus posuere velit aliquet. Maecenas sed diam eget risus varius blandit sit amet non magna.\r\rMaecenas sed diam eget risus varius blandit sit amet non magna. Etiam porta sem malesuada magna mollis euismod. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante venenatis dapibus posuere velit aliquet. Vestibulum id ligula porta felis euismod semper. Curabitur blandit tempus porttitor. Aenean lacinia bibendum nulla sed consectetur.')

//Enter text for the Internal Notes
WebUI.setText(findTestObject('ABC/Quotes/Create Quote/textarea-Internal Notes'), 'Aenean eu leo quam. Pellentesque ornare sem lacinia quam venenatis vestibulum. Sed posuere consectetur est at lobortis. Integer posuere erat a ante venenatis dapibus posuere velit aliquet. Sed posuere consectetur est at lobortis.\r\rCurabitur blandit tempus porttitor. Nullam quis risus eget urna mollis ornare vel eu leo. Cras justo odio, dapibus ac facilisis in, egestas eget quam. Duis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit. Nulla vitae elit libero, a pharetra augue. Sed posuere consectetur est at lobortis.\r\rDuis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed posuere consectetur est at lobortis. Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor auctor. Integer posuere erat a ante venenatis dapibus posuere velit aliquet. Maecenas sed diam eget risus varius blandit sit amet non magna.\r\rMaecenas sed diam eget risus varius blandit sit amet non magna. Etiam porta sem malesuada magna mollis euismod. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante venenatis dapibus posuere velit aliquet. Vestibulum id ligula porta felis euismod semper. Curabitur blandit tempus porttitor. Aenean lacinia bibendum nulla sed consectetur.')

//Click the Create Quote button to move to the next screen and add an item to the inventory
WebUI.click(findTestObject('ABC/Quotes/Create Quote/btn-Create Quote'))

log.logWarning('--- Completed New Quote Page ---')

//--- End of First Page ---
//Wait for Add Inventory Item button to be visible
WebUI.waitForElementVisible(findTestObject('ABC/Quotes/Create Quote/btn-Add Inventory Item'), 15)

//Confirm the Customer Name is visible at the top of the quote confirmation
verifyCustomerAndAccount(2)

//Confirm the Fuel and Delivery Surcharge has been added due to the Ground Delivery option
if (loadFreight==true){
	elementVisible = CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('FUEL SURCHARGE')
	elementVisible = CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('DELIVERY CHARGE')
	elementVisible = CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('FREIGHT AND HANDLING CHARGE')
}

//Click to Add Inventory
WebUI.click(findTestObject('ABC/Quotes/Create Quote/btn-Add Inventory Item'))

//Wait for the Search field
WebUI.waitForElementVisible(findTestObject('ABC/Quotes/Create Quote/input-Search Inventory'), 15)

//Enter the inventory item based on Division
//Enter several items based on Name and SKU

//The List starts at 2 (technically position 3, since we are skipping 0 and 1.
//So we need to subtract 3 to be 1 less than the total size
for (loop = 2; loop <=(GlobalVariable.inventorySKU.size()-3); loop++) {

	WebUI.setText(findTestObject('ABC/Quotes/Create Quote/input-Search Inventory'), String.valueOf(GlobalVariable.inventorySKU[loop]))
	addInventoryItem()
	searchInventoryItem()
}
	GlobalVariable.inventorySKU[(loop+1)]
	WebUI.setText(findTestObject('ABC/Quotes/Create Quote/input-Search Inventory'), String.valueOf(GlobalVariable.inventorySKU[(loop+1)]))
	addInventoryItem()

log.logWarning('--- Added New Inventory Item ---')

/*
 WebUI.setText(findTestObject('ABC/Quotes/Create Quote/input-Inventory Item Quantity'), '25')
 WebUI.sendKeys(findTestObject('ABC/Quotes/Create Quote/input-Inventory Item Quantity'), Keys.chord(Keys.TAB,
 Keys.TAB))
 */
/* Add Custom Item to Order - The item will not be added correctly using Impersonation - This is a known issue
 * However, it still fills out the fields and makes sure the values can be entered.
 */
WebUI.delay(2)

if (addCustomItem==true){

	//Wait for Add Inventory Item button to be visible
	WebUI.waitForElementVisible(findTestObject('ABC/Quotes/Create Quote/Custom Item/btn-Add Custom Item'), 15)

	//Click to Add Inventory
	WebUI.click(findTestObject('ABC/Quotes/Create Quote/Custom Item/btn-Add Custom Item'))

	//Wait for Product Name field to appear
	WebUI.waitForElementVisible(findTestObject('ABC/Quotes/Create Quote/Custom Item/input-Custom Item Name'), 15)

	//Enter the Custom inventory item
	WebUI.setText(findTestObject('ABC/Quotes/Create Quote/Custom Item/input-Custom Item Name'), 'New Custom Item from TKXS')

	//Enter the Custom inventory item Price
	WebUI.setText(findTestObject('ABC/Quotes/Create Quote/Custom Item/input-Custom Item Price'), '499.99')

	//Select the Custom inventory item UOM
	WebUI.selectOptionByLabel(findTestObject('ABC/Quotes/Create Quote/Custom Item/select-Custom Item UOM'), 'EA - EACH', false)

	//Enter the Custom inventory item Qty
	WebUI.setText(findTestObject('ABC/Quotes/Create Quote/Custom Item/input-Custom Item Qty'), '5')

	//Click to Add Custom Item to Inventory
	WebUI.click(findTestObject('ABC/Quotes/Create Quote/Custom Item/btn-Save Custom Item'))
}

//Wait for the Save and Send Quote button to be available to submit the order
WebUI.waitForElementVisible(findTestObject('ABC/Quotes/Create Quote/btn-Save and Send Quote'), 15)

WebUI.delay(2)

//Confirm that each inventory item has a dollar value and is not $0
for (loop = 1; loop <= 4; loop++) {
	tempText = WebUI.getAttribute(findTestObject('ABC/Quotes/Create Quote/text-Inventory Item Price', [('row') : loop]), 'value')

	if (tempText == '0.00' || tempText == '' || tempText == '0') {
		log.logError('ERROR: An inventory item has a price of $0.00. This may be incorrect')
		KeywordUtil.markError('ERROR: An inventory item has a price of $0.00. This may be incorrect')
	}
}

if (loadFreight==true){
	//Confirm the Fuel Surcharges have a dollar value
	for (loop = 1; loop <= 3; loop++) {
		tempText = WebUI.getAttribute(findTestObject('ABC/Quotes/Create Quote/text-Fuel Surcharge Item Price', [('row') : loop]), 'value')

		if (tempText == '0.00'|| tempText == '' || tempText == '0') {
			log.logError('ERROR: The Delivery and Fuel Surcharge has a price of $0.00. This may be incorrect')
			KeywordUtil.markError('ERROR: The Delivery and Fuel Surcharge has a price of $0.00. This may be incorrect')
		}
	}
}

//Confirm the price of the quote isn't too low. It should be at least the cost of the items added
tempText = WebUI.getText(findTestObject('ABC/Quotes/Create Quote/text-Quote Total')).replaceAll('[^0-9-.]', '').toDouble()

log.logWarning('The Quote Total for ' + GlobalVariable.quoteName + '<--->' + tempText)

if (tempText < 125) {
	log.logError('ERROR: The Quote Total is lower than expected and does not appear to be correct')

	KeywordUtil.markError('ERROR: The Quote Total is lower than expected and does not appear to be correct')
}

if (timedTest==true){
	timeStart = new Date()
}


WebUI.click(findTestObject('ABC/Quotes/Create Quote/btn-Save and Send Quote'))

if (addCustomItem==true){
	log.logWarning('--- Added Custom Inventory Item ---')
}

//--- End of Second Page ---
//The final page where an email recipient is selected, or in this case, typed in and the Quote sent, along with a PDF of the items.
//Wait for the page to load and then wait for the email addresses to appear.
WebUI.waitForPageLoad(15)

log.logWarning('--- Emailing Quote ---')

//Wait for the Email Input field at the bottom of the page to become visible. We send to our own address, not an ABC Rep.
WebUI.waitForElementVisible(findTestObject('ABC/Quotes/Create Quote/input-Email This Quote To'), 15)

if (timedTest==true){
	timeStop = new Date()
	TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
	log.logWarning("Execution Time: " + duration)
}

//Confirm the Customer Name is visible at the top of the quote confirmation
verifyCustomerAndAccount(1)

//Enter the email address to send the Quote too
WebUI.setText(findTestObject('ABC/Quotes/Create Quote/input-Email This Quote To'), 'peter.wilson@technekes.com')

//Wait for Convert Quote button to appear
WebUI.waitForElementVisible(findTestObject('ABC/Quotes/Create Quote/btn-Send Quote'), 15)

//Click Convert Quote and submit the quote. An email should be generated with a PDF attachement
WebUI.click(findTestObject('ABC/Quotes/Create Quote/btn-Send Quote'))

WebUI.delay(2)

//Confirm the Customer Name is visible at the top of the quote confirmation
verifyCustomerAndAccount(1)

log.logWarning('--- Quote Saved and Submitted ---')

/* The following was broken out into separate methods so they can be reused when adding inventory items
 * Steps needed to search for the inventory item
 * Wait for Add Inventory Item button to be visible
 * Click to Add Inventory
 * Wait for the Search field
 * Steps needed to add the inventory item to the quote
 * Click Go so we get the list of results
 * Wait for the button Add to Quote to appear
 * Read details about the item being added to the quote
 * Click Add to Quote
 *
 */

def searchInventoryItem() {
	WebUI.waitForElementVisible(findTestObject('ABC/Quotes/Create Quote/btn-Add Inventory Item'), 15)
	WebUI.click(findTestObject('ABC/Quotes/Create Quote/btn-Add Inventory Item'))
	WebUI.delay(2)
	CustomKeywords.'tools.commonCode.dismissAlert'()
	WebUI.waitForElementVisible(findTestObject('ABC/Quotes/Create Quote/input-Search Inventory'), 15)
}

def addInventoryItem() {
	KeywordLogger log = new KeywordLogger()
	int inventoryResults
	
	WebUI.waitForElementVisible(findTestObject('ABC/Quotes/Create Quote/btn-Go'), 15)
	WebUI.click(findTestObject('ABC/Quotes/Create Quote/btn-Go'))
	WebUI.delay(2)
	
	//Confirm an inventory item is returned
	CustomKeywords.'tools.commonCode.dismissAlert'()
	inventoryResults=WebUI.getText(findTestObject('ABC/Quotes/text-Results Found')).replaceAll("[^0-9]","").toInteger()
	if (inventoryResults==0) {
		log.logError('ERROR: The requested inventory item does not exist')
		WebUI.waitForElementClickable(findTestObject('ABC/Quotes/Create Quote/btn-Close Search'), 10)
		WebUI.click(findTestObject('ABC/Quotes/Create Quote/btn-Close Search'))		 
		KeywordUtil.markError('ERROR: The requested inventory item does not exist')
	} else {
		WebUI.waitForElementVisible(findTestObject('ABC/Quotes/Create Quote/btn-Add to Quote'), 15)
		String addedInventoryItemDetails = WebUI.getText(findTestObject('ABC/Quotes/Create Quote/text-Added Inventory Item Details'))
		List inventoryItem = addedInventoryItemDetails.readLines()
		String SKUDetail = inventoryItem[0]
		String inventoryItemName = inventoryItem[1]
		String inventoryLocations = inventoryItem[2]

		log.logWarning("--Item Details --")
		log.logWarning("-- SKU: " + SKUDetail)
		log.logWarning("-- Inventory Name: " + inventoryItemName)
		log.logWarning("-- Inventory Item Location: " + inventoryLocations )
		log.logWarning("-- Quote Branch: " + quoteBranchName)
		if (inventoryLocations.contains(quoteBranchName)==false){
			log.logError("ERROR: The Inventory Item location does not match the Branch Location")
			KeywordUtil.markFailed('ERROR: The Inventory Item location does not match the Branch Location')
		}
		WebUI.click(findTestObject('ABC/Quotes/Create Quote/btn-Add to Quote'))
	}
}

def verifyCustomerAndAccount(int rowIndex){
	KeywordLogger log = new KeywordLogger()
	//Confirm the Customer Name is visible at the top of the quote
	String existingCustomerName=WebUI.getText(findTestObject('ABC/Quotes/Create Quote/text-Company and Quote Name', [('row') : rowIndex]))
	if (existingCustomerName.contains(customerName)!=true){
		log.logError("ERROR: The Existing Customer name was not listed correctly on the quote")
		KeywordUtil.markFailed('ERROR: The Existing Customer name was not listed correctly on the quote')
	}
	
	//Confirm the name of the quote is displayed correctly
	if (existingCustomerName.contains("Test Quote created from QA - " + formattedDate)!=true){
		log.logError("ERROR: The entered name of the quote was not listed correctly on the page")
		KeywordUtil.markFailed('The entered name of the quote was not listed correctly on the page')
	}
}
