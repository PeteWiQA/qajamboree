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
import com.kms.katalon.core.util.KeywordUtil


/* The purpose of this script is to go to the Sales Reps inventory
 * search and read the first 4 items from the page and store it in a List
 * GlobalVariable.inventorySKU[] for the SKU number
 * GlobalVariable.inventoryList[] for the Item Description
 * This List of items can then be used for Quotes or for Search
 * Because of how the page is laid out, the List starts at Index[2] rather
 * than Index[0].
 * 
 * Updated 02-26-2019 11:29AM by Pete Wilson
 * Updated 08-06-2019 14:57PM by Pete Wilson
 */

KeywordLogger log = new KeywordLogger()

String inventorySearch, inventoryItem, inventoryItemSKU
int counter=0

WebUI.navigateToUrl(GlobalVariable.baseurl + '/items')

//Check to make sure theInventory Search page loads. Look for the text, Inventory Search
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Inventory Search')

if (GlobalVariable.userDivision.equalsIgnoreCase('ABC')) {
	inventorySearch=GlobalVariable.inventoryItem
} else {
	inventorySearch=GlobalVariable.tciInventoryItem
}

//Verify search results for several different kinds of search criteria
//Check the option for "My Branch Only"
if (WebUI.verifyElementChecked(findTestObject('ABC/Search Inventory/checkbox-My Branch Only'),10,FailureHandling.OPTIONAL)){
	WebUI.setText(findTestObject('ABC/Search Inventory/input-Inventory Search Field'), inventorySearch)
	WebUI.click(findTestObject('ABC/Search Inventory/btn-Go'))
	

} else {
	WebUI.click(findTestObject('ABC/Search Inventory/checkbox-My Branch Only'))
	WebUI.setText(findTestObject('ABC/Search Inventory/input-Inventory Search Field'), inventorySearch)
	WebUI.click(findTestObject('ABC/Search Inventory/btn-Go'))
}

WebUI.waitForJQueryLoad(10)

//Confirm there are results
int returnedResults = WebUI.getText(findTestObject('ABC/Search Inventory/text-Search Results Found')).replaceAll('[^0-9]','').toInteger()
//Wait for correct result set to be returned. Only a handful of items should be returned
while (returnedResults>=1000){
	if (counter>=10){
		log.logError('ERROR: Not enough inventory results were returned for item, ' + inventorySearch)
		KeywordUtil.markFailedAndStop('ERROR: Not enough inventory results were returned for item, ' + inventorySearch)
		break;
	}
	WebUI.delay(1)
	returnedResults = WebUI.getText(findTestObject('ABC/Search Inventory/text-Search Results Found')).replaceAll('[^0-9]','').toInteger()
	counter++
}
log.logWarning("Items Found: " + returnedResults)

//We want a full page of results. If there are less than 8, this probably isn't a good item to work with
if (returnedResults <=8) {
    log.logError('ERROR: Not enough inventory results were returned for item, ' + inventorySearch)
	KeywordUtil.markError('ERROR: Not enough inventory results were returned for item, ' + inventorySearch)
} else {
	//Read SKU and Product Description
	if (returnedResults>=6){
		returnedResults=6
	}
	
	//The first inventory item is at position 2, so to make things easier, build the list from that index position
	//The SKU and Description will begin at Index[2]
    for (loop = 2; loop <= returnedResults; loop++) {
        inventoryItem = WebUI.getText(findTestObject('ABC/Search Inventory/link-Inventory Item Description', [('row') : loop]))
        inventoryItemSKU = WebUI.getText(findTestObject('ABC/Search Inventory/link-Inventory SKU Number', [('row') : loop]))
        (GlobalVariable.inventoryList[loop]) = inventoryItem
        (GlobalVariable.inventorySKU[loop]) = inventoryItemSKU
    }
}

log.logWarning("Inventory items read: " + GlobalVariable.inventoryList.size())