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

/* The purpose of this test is to search for a Branch and confirm the details.
 * On the Search Branch page, enter the zip code from the file
 * If a result is found, compare it the results stored in the file
 * If multiple results are returned, check the results to see if one
 * of the returned branches is the correct one.
 * 
 * Updated 03-21-2019 17:00PM by Pete Wilson
 * Updated 08-06-2019 14:56PM by Pete Wilson
 */

//Using data from the input file, set up the name of the branch to search for
String nameOfBranch=(branchName + " " + branchCity + ", " + branchState)
String branchResult
//Used when multiple branches are returned to indicate if one of them is the correct result
boolean success
int returnedResults

WebUI.navigateToUrl(GlobalVariable.baseurl + '/branches')
//Check to make sure the Branch Search page loads. Look for the text, Branch Search
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Branch Search')

WebUI.waitForElementVisible(findTestObject('ABC/Search Branch/input-Search Branch'), 10)
//Enter the zip code from the data file
WebUI.setText(findTestObject('ABC/Search Branch/input-Search Branch'), branchZip)
WebUI.waitForElementClickable(findTestObject('ABC/Search Branch/btn-Go'), 10)
WebUI.click(findTestObject('ABC/Search Branch/btn-Go'))
WebUI.waitForJQueryLoad(10)

//WebUI.delay(3)
//Confirm there are results
WebUI.waitForElementVisible(findTestObject('ABC/Search Branch/text-Branch Search - Results Found'), 10)
returnedResults=WebUI.getText(findTestObject('ABC/Search Branch/text-Branch Search - Results Found')).replaceAll("[^0-9]","").toInteger()
if (returnedResults==0) {
	log.logError('ERROR: No results were returned. No branch matches the search criteria.')
	log.logError('ERROR: The Branch for Zip Code ' + branchZip + ' is not valid')
	KeywordUtil.markError('ERROR: The Branch for Zip Code ' + branchZip + ' is not valid')
}

if (returnedResults==1) {
	//Does the branch result contain the expected criteria?
	branchResult=WebUI.getText(findTestObject('ABC/Search Branch/link-Name of Returned Branch')).toLowerCase()
	log.logWarning("Search Criteria: " + nameOfBranch)
	log.logWarning("Result Criteria: " + branchResult.toUpperCase())
	if (branchResult.contains(nameOfBranch.toLowerCase())==true){
		log.logWarning('SUCCESS: The expected Branch Name was returned')
	} else {
		log.logError("ERROR: The expected Branch Name: " + nameOfBranch + " with a Zip Code of " + branchZip + " was not returned")
		KeywordUtil.markError("ERROR: The expected Branch Name: " + nameOfBranch + " with a Zip Code of " + branchZip + " was not returned")
	}

}

if (returnedResults>1){
	//Does the branch result contain multiple results? If so, loop over the results to find a match
	log.logWarning("NOTICE: Multiple entries found")
	for (loop = 1; loop <=returnedResults; loop++) {
		branchResult=WebUI.getText(findTestObject('ABC/Search Branch/link-Name of Returned Branch - Data Driven', [('row'):loop+1])).toLowerCase()
		log.logWarning("Search Criteria: #" + loop + ":" + nameOfBranch)
		log.logWarning("Result Criteria: #" + loop + ":" + branchResult.toUpperCase())

		if (branchResult.contains(nameOfBranch.toLowerCase())==true){
			success=true
		}
	}
	if (success==true){
		log.logWarning('SUCCESS: The expected Branch Name was returned')
	} else {
		log.logError("ERROR: The expected Branch Name: " + nameOfBranch + " with a Zip Code of " + branchZip + " was not returned")
		KeywordUtil.markError("ERROR: The expected Branch Name: " + nameOfBranch + " with a Zip Code of " + branchZip + " was not returned")
	}
}
