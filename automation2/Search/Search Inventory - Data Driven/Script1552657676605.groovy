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
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger

KeywordLogger log = new KeywordLogger()

/* The purpose of this test is to search through hundreds of
 * inventory items using a data file. The file is based on items
 * listed in the ABC.zip and TCI.zip files from the FTP server
 * These files will need to be downloaded and updated on a regular
 * basis to have the correct inventory list.
 * 
 * Updated 03-15-2019 11:36AM by Pete Wilson
 * Updated 08-06-2019 14:56PM by Pete Wilson 
 * 
 */

String skuNumber
int returnedResults

WebUI.navigateToUrl(GlobalVariable.baseurl + '/items')
//Check to make sure theInventory Search page loads. Look for the text, Inventory Search
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Inventory Search')

WebUI.waitForElementVisible(findTestObject('ABC/Search Inventory/checkbox-My Branch Only'), 10)

//UnCheck the option for "My Branch Only"
if (WebUI.verifyElementChecked(findTestObject('ABC/Search Inventory/checkbox-My Branch Only'),10,FailureHandling.OPTIONAL)){
	WebUI.click(findTestObject('ABC/Search Inventory/checkbox-My Branch Only'))
}

WebUI.waitForElementVisible(findTestObject('ABC/Search Inventory/input-Inventory Search Field'), 10)

//Verify search results for several different kinds of search criteria

//Enter search criteria
WebUI.setText(findTestObject('ABC/Search Inventory/input-Inventory Search Field'), inventorySearch)
WebUI.waitForElementClickable(findTestObject('ABC/Search Inventory/btn-Go'), 10)
WebUI.click(findTestObject('ABC/Search Inventory/btn-Go'))
WebUI.waitForJQueryLoad(10)
//WebUI.delay(2)
//Confirm there are results
WebUI.waitForElementVisible(findTestObject('ABC/Search Inventory/text-Search Results Found'), 10)
returnedResults=WebUI.getText(findTestObject('ABC/Search Inventory/text-Search Results Found')).replaceAll("[^0-9]","").toInteger()
if (returnedResults==0) {
	log.logError('ERROR: No results were returned. No Inventory Item matches the search criteria.')
	log.logError('ERROR: The Inventory Item ' + inventorySearch + ' is not valid')
	KeywordUtil.markError('ERROR: The Inventory Item ' + inventorySearch + ' is not valid')
} else {
	skuNumber=WebUI.getText(findTestObject('ABC/Search Inventory/text-SKU Number'))
	if (inventorySearch==skuNumber){
		log.logWarning("SUCCESS: Search item: " + inventorySearch + " matches search result item: " + skuNumber)
	} else {
		log.logError("ERROR: The SKU number of the search result: " + skuNumber + " does not match the search criteria: " + inventorySearch)
		KeywordUtil.markError("ERROR: The SKU number of the search result: " + skuNumber + " does not match the search criteria: " + inventorySearch)
	}
}