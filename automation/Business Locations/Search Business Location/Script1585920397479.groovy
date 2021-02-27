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

/* The purpose of this test is to perform and validate
 * searches on the Business Locations page. Search criteria
 * are entered and checked in the results table. If the results are
 * missing or blank, the test is marked as Failed.
 *
 * Created 04-03-2020 11:29AM by Pete Wilson
 */

WebUI.click(findTestObject('Home/link-Menu Nav', [('menuName') : 'Business Locations']))

String searchText

//Search by Name
searchText="Dusting"
WebUI.setText(findTestObject('Business Locations/input-Name'), searchText)
WebUI.click(findTestObject('Business Locations/btn-Search'))
WebUI.waitForJQueryLoad(30)
confirmSearch(searchText, 2)
WebUI.setText(findTestObject('Business Locations/input-Name'), "")

//Search by City
searchText="Sacred Heart"
WebUI.setText(findTestObject('Business Locations/input-City'), searchText)
WebUI.click(findTestObject('Business Locations/btn-Search'))
WebUI.waitForJQueryLoad(30)
confirmSearch(searchText, 3)
confirmSearch(searchText, 4)
WebUI.setText(findTestObject('Business Locations/input-City'), "")

//Search by State
searchText="AZ"
WebUI.selectOptionByLabel(findTestObject('Business Locations/select-State'), searchText, false)
WebUI.click(findTestObject('Business Locations/btn-Search'))
WebUI.waitForJQueryLoad(30)
confirmSearch(searchText, 3)
confirmSearch(searchText, 4)
WebUI.selectOptionByLabel(findTestObject('Business Locations/select-State'), '- Select -', false)

//Search by ID
searchText="Aerial Spraying LLC"
WebUI.selectOptionByLabel(findTestObject('Business Locations/select-Id Type'), 'Vendor ID', false)
WebUI.setText(findTestObject('Business Locations/input-ID'), '30623155')
WebUI.click(findTestObject('Business Locations/btn-Search'))
WebUI.waitForJQueryLoad(30)
confirmSearch(searchText, 2)
WebUI.selectOptionByLabel(findTestObject('Business Locations/select-Id Type'), '--Select--', false)
WebUI.setText(findTestObject('Business Locations/input-ID'), '')

//Verify search results
def confirmSearch(String searchText, int column){
	/* Confirm the entered search criteria is contained in the results
	 * Uses a simple "contains" to see if the value exists.
	 * If missing, the test will be marked as Failed
	 * @param searchText - The search criteria entered in the search field
	 * @param column - The column number where to look for the matching text.
	 */
	KeywordLogger log = new KeywordLogger()
	String searchResult=WebUI.getText(findTestObject('Business Locations/table-Business Locations', [('row') : 1, ('column') : column]))
	log.logWarning(searchText + " : " + searchResult)
	if (searchResult.contains(searchText)!=true){
		KeywordUtil.markFailed('ERROR: The returned search result does not match the entered search criteria')
	}
}
