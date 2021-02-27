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
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger
import com.kms.katalon.core.util.KeywordUtil

KeywordLogger log = new KeywordLogger()

/* The purpose of this test is to check the Search features on the
 * Users page. A search is done for Name, City, State and Type.
 * Search criteria are entered and checked in the results table. If the results are
 * missing or blank, the test is marked as Failed.
 * 
 * Created 04-02-2020 16:06PM Pete Wilson
 */

WebUI.click(findTestObject('Home/link-Menu Nav', [('menuName') : 'Users']))

String searchResult, searchText

//Confirm search by Name
searchText="Retail"
WebUI.setText(findTestObject('Users/input-Name'), searchText)
WebUI.click(findTestObject('Users/btn-Search'))
WebUI.waitForJQueryLoad(30)
confirmSearch(searchText, 1)

//Confirm search by City
searchText="Akron"
WebUI.setText(findTestObject('Users/input-City'), searchText)
WebUI.click(findTestObject('Users/btn-Search'))
WebUI.waitForJQueryLoad(30)
confirmSearch(searchText, 6)

//Confirm search by State
searchText="AZ"
WebUI.selectOptionByLabel(findTestObject('Users/select-State'), searchText, false)
//WebUI.setText(findTestObject('Users/input-City'), searchText)
WebUI.click(findTestObject('Users/btn-Search'))
WebUI.waitForJQueryLoad(30)
confirmSearch(searchText, 6)

//Confirm search by Type
searchText="Retailer"
WebUI.selectOptionByLabel(findTestObject('Users/select-Type'), searchText, false)
//WebUI.setText(findTestObject('Users/input-City'), searchText)
WebUI.click(findTestObject('Users/btn-Search'))
WebUI.waitForJQueryLoad(30)
confirmSearch(searchText, 2)

//Verify search results
def confirmSearch(String searchText, int column){
	/* Confirm the entered search criteria is contained in the results
	 * Uses a simple "contains" to see if the value exists.
	 * If missing, the test will be marked as Failed
	 * @param searchText - The search criteria entered in the search field
	 * @param column - The column number where to look for the matching text.
	 */
	KeywordLogger log = new KeywordLogger()
	String searchResult=WebUI.getText(findTestObject('Users/text-searchResults', [('row') : 2, ('column') : column]))
	log.logWarning(searchText + " : " + searchResult)
	if (searchResult.contains(searchText)!=true){
		KeywordUtil.markFailed('ERROR: The returned search result does not match the entered search criteria')
	}
	WebUI.click(findTestObject('Users/btn-Clear Fields'))
}
