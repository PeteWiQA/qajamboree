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

/* This test checks the branch and state details on the Product Details page
 * It is called from the Search Inventory test
 * Created 08-27-2019 11:30AM by Pete Wilson 
 */

List states=['AL','AK','AZ','AR','CA','CO','CT','DE','FL','GA','HI',
	'ID','IL','IN','IA','KS','KY','LA','ME','MD','MA','MI','MN','MS',
	'MO','MT','NE','NV','NH','NJ','NM','NY','NC','ND','OH','OK','OR',
	'PA','RI','SC','SD','TN','TX','UT','VT','VA','WA','WV','WI','WY']

//Set the Sort By method. This can be "Promixity" or "Amount Available"
WebUI.waitForElementClickable(findTestObject('ABC/Search Inventory/Product Details/select-Sort By'), 10)
WebUI.click(findTestObject('ABC/Search Inventory/Product Details/select-Sort By'))
WebUI.waitForElementClickable(findTestObject('ABC/Search Inventory/Product Details/select-Sort By Value',[('sortMethod') : 'Amount Available']), 10)
WebUI.click(findTestObject('ABC/Search Inventory/Product Details/select-Sort By Value',[('sortMethod') : 'Amount Available']))

//Get number of pages listed in the pagination ribbon. Remove trailing characters so we only have the LI element
String xpath=findTestObject('ABC/Search Inventory/Product Details/pagination-All Branches', [('page') : 1])
	.findPropertyValue("xpath")
	.replaceAll('li\\[1\\]/a','li')
int numberOfPages=CustomKeywords.'tools.commonCode.countRowsPerPage'(xpath)

int paginationResults=WebUI.getText(findTestObject('ABC/Search Inventory/Product Details/pagination-All Branches', [('page') : (numberOfPages-1)])).toInteger()
String tempText, branchState
for (pagination = 1; pagination<=paginationResults; pagination++) {
	//Determine the number of rows available in the table. Remove trailing characters so we only have TR element
	xpath=findTestObject('ABC/Search Inventory/Product Details/link-BranchName', [('row') : 1])
		.findPropertyValue("xpath")
		.replaceAll('tr\\[1\\]/td','tr')
	int rowsPerPage=CustomKeywords.'tools.commonCode.countRowsPerPage'(xpath)
	
	//Check every other row since we have the pricing row in the table as well
	for (loop = 1; loop <=rowsPerPage; loop+=2) {
		tempText=WebUI.getText(findTestObject('ABC/Search Inventory/Product Details/link-BranchName', [('row') : loop]))
		branchState=tempText.split(',')[1].trim()
		log.logWarning("Branch Location: " + tempText +" Branch State location: " + branchState)
		//Make sure the branches are for the correct division, that they aren't blank, and have a 2 letter state at the end.
		if (tempText=='' || tempText==null){
			log.logError("ERROR: The branch name is blank")
		}
		if (tempText.contains(GlobalVariable.userDivision)!=true){
			log.logError("ERROR: Listed branch is incorrect for the user division")
		}
		if (branchState.length()!=2){
			log.logError("ERROR: Branch is listed without a state")
		}
		//Confirm if the state of the branch is a valid state from the List
		if (states.contains(branchState)!=true){
			log.logError("ERROR: The state associated with the branch is not valid")
		}
	}
	//Move to the next page of results and wait for the table to populate
	WebUI.waitForElementClickable(findTestObject('ABC/Search Inventory/Product Details/btn-Next'), 10)
	WebUI.click(findTestObject('ABC/Search Inventory/Product Details/btn-Next'))
	WebUI.waitForJQueryLoad(10)
}