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

/* The purpose of this test is to load the Prospect page and coount the number of results
 * If there are Prospects on the page, list out the first 10 or less
 * If no Prospects are listed, indicate there are no items to work with and exit out of the test
 * Updated 01-25-2018 11:01AM by Pete Wilson
 * Updated 03-24-2018 10:44AM by Pete Wilson
 * Updated 04-19-2018 16:26PM by Pete Wilson
 * Updated 08-14-2018 11:59AM by Pete Wilson
 */

WebUI.navigateToUrl(GlobalVariable.baseurl + '/prospects')

//Check to make sure the Prospect page loads. Look for the text, All Prospects
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('All Prospects')

CustomKeywords.'tools.commonCode.checkForAlert'()

//Wait for the Results Found text to appear. This contains the number of Prospects on the page.
WebUI.waitForElementVisible(findTestObject('ABC/Prospects/label-Prospect Results Found'), 10)

//Read the number of Prospects and store it
prospectResults=WebUI.getText(findTestObject('ABC/Prospects/label-Prospect Results Found')).replaceAll("[^0-9]","").toInteger()

log.logWarning('The number of Prospects listed is:' + prospectResults + ' - A maxiumum of 10 will be listed.')

//If there are 0 Prospects, we need to exit the test as there will be nothing to read
if (prospectResults==0) {
	log.logWarning('ERROR: There are no Prospects for ' + GlobalVariable.impersonatedUser + ' Test was not completed')
	log.logError('ERROR: There are no Prospects for ' + GlobalVariable.impersonatedUser + ' Test was not completed')
} else {

	//If there are more than 10 Prospects, only print the first 10 since that is a page worth
	if (prospectResults>10) {
		prospectResults=10
	}

	/* The first element on the page starts at position 2, so the loop starts at 2
	 * The last element on the page is at position 11, so we have to add 1 to the total number of Prospects
	 * To print the Prospects correctly, 1 needs to be taken away from the counter since we started at 2
	 * The Prospect at element 2 is Prospect 1 on the page
	 */
	for (loop = 2; loop <= (prospectResults + 1); loop++) {
		customerName=WebUI.getText(findTestObject('ABC/Prospects/text-Prospect Name', [('Variable') : loop]))
		log.logWarning('Prospect name #' + (loop - 1) + ' ' + customerName)
	}
}