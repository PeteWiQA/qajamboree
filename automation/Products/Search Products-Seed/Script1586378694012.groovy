import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger
KeywordLogger log = new KeywordLogger()

/* The purpose of this test is to verify items on the Products page
 * This test is for Seed items. It confirms a result
 * is returned for Product Name, Manufacturer, Loan Type and Active.
 *
 * Created 04-09-2020 11:47AM by Pete Wilson
 */

String searchText
boolean isChecked

WebUI.click(findTestObject('Home/link-Menu Nav', [('menuName') : 'Products']))

WebUI.waitForElementClickable(findTestObject('Products/tab-Product',[('tabName'):'Seed']), 10)
WebUI.click(findTestObject('Products/tab-Product',[('tabName'):'Seed']))

//Check Product Name
searchText="PXY 300 TRIO"
WebUI.setText(findTestObject('Products/Seed/input-Product Name'), searchText)
WebUI.click(findTestObject('Products/Seed/btn-Search'))
WebUI.waitForJQueryLoad(30)
confirmSearch(searchText, 1)
//Check List price
String listPrice=WebUI.getText(findTestObject('Products/Seed/table-Product Results', [('row') : 1, ('column') : 4]))
if (listPrice=="" || listPrice==null){
	KeywordUtil.markFailed('ERROR: The returned search result does not have a List Price')
}
//Check Price Threshold
String priceThreshold=WebUI.getText(findTestObject('Products/Seed/table-Product Results', [('row') : 1, ('column') : 5]))
if (priceThreshold=="" || priceThreshold==null){
	KeywordUtil.markFailed('ERROR: The returned search result does not have a Price Threshold')
}
WebUI.setText(findTestObject('Products/Seed/input-Product Name'), "")

//Check Manufacturer
searchText="MFGName"
WebUI.selectOptionByLabel(findTestObject('Products/Seed/select-Manufacturer'), searchText, false)
WebUI.click(findTestObject('Products/Seed/btn-Search'))
WebUI.waitForJQueryLoad(30)
confirmSearch(searchText, 2)
WebUI.selectOptionByLabel(findTestObject('Products/Seed/select-Manufacturer'), '--Select--', false)

//Check Loan Type
searchText="DX"
WebUI.selectOptionByLabel(findTestObject('Products/Seed/select-Loan Type'), searchText, false)
WebUI.click(findTestObject('Products/Seed/btn-Search'))
WebUI.waitForJQueryLoad(30)
confirmSearch(searchText, 3)
WebUI.selectOptionByLabel(findTestObject('Products/Seed/select-Loan Type'), '--All Loans--', false)

//Check Active
searchText="Active"
WebUI.selectOptionByLabel(findTestObject('Products/Seed/select-Active'), searchText, false)
WebUI.click(findTestObject('Products/Seed/btn-Search'))
WebUI.waitForJQueryLoad(30)
isChecked=WebUI.verifyElementChecked(findTestObject('Products/Results Table/checkbox-Active', [('row') : 1]),10,FailureHandling.OPTIONAL)
if (isChecked!=true){
//(WebUI.verifyElementChecked(findTestObject('Products/Results Table/checkbox-Active', [('row') : 1]),10,FailureHandling.OPTIONAL)!=true){
	KeywordUtil.markFailed('ERROR: The returned search result is not set to Active')
} else {
	log.logWarning(searchText + " : " + isChecked)
}
WebUI.selectOptionByLabel(findTestObject('Products/Seed/select-Active'), '--All--', false)

//Verify search results
def confirmSearch(String searchText, int column){
	/* Confirm the entered search criteria is contained in the results
	 * Uses a simple "contains" to see if the value exists.
	 * If missing, the test will be marked as Failed
	 * @param searchText - The search criteria entered in the search field
	 * @param column - The column number where to look for the matching text.
	 */
	KeywordLogger log = new KeywordLogger()
	String searchResult=WebUI.getText(findTestObject('Products/Seed/table-Product Results', [('row') : 1, ('column') : column]))
	log.logWarning(searchText + " : " + searchResult)
	if (searchResult.toLowerCase().contains(searchText.toLowerCase())!=true){
		KeywordUtil.markFailed('ERROR: The returned search result does not match the entered search criteria')
	}
}
