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
import com.kms.katalon.core.util.KeywordUtil
KeywordLogger log = new KeywordLogger()

/* Go to the Quotes section and count the number of Quotes assigned to the Sales Rep
 * As an additional test, confirm each quote has a name and Bill To Account
 * Updated 01-25-2018 1:12PM by Pete Wilson
 * Updated 04-19-2018 11:22AM by Pete Wilson
 * Updated 08-15-2018 11:51AM by Pete Wilson
 * Updated 03-14-2019 10:21AM by Pete Wilson
 */

WebUI.navigateToUrl(GlobalVariable.baseurl + '/quotes')

//Check to make sure the Quotes page loads. Look for the text, Quotes 
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Quotes')

//Wait for the Results found text to be displayed. This has the number of objects available
//If this doesn't appear, there is a problem with the page. Mark the test as Failed and exit.
elementPresent=WebUI.waitForElementVisible(findTestObject('ABC/Quotes/text-Results Found'), 10)
if (elementPresent==false) {
	KeywordUtil.markFailedAndStop('ERROR: There was an error loading the page. Exiting test.')
}

WebUI.click(findTestObject('ABC/Quotes/btn-Filter-All Quotes'))

WebUI.delay(2)

WebUI.waitForElementVisible(findTestObject('ABC/Quotes/text-Results Found'), 10)

quoteResults=WebUI.getText(findTestObject('ABC/Quotes/text-Results Found')).replaceAll("[^0-9]","").toInteger()

log.logWarning('The number of quotes listed is: ' + quoteResults)

//--------------//
//If there are 0 Prospects, we need to exit the test as there will be nothing to read
if (quoteResults==0) {
	log.logWarning('ERROR: There are no Quotes for ' + GlobalVariable.impersonatedUser + ' Exiting test.')
	log.logError('ERROR: There are no Quotes for ' + GlobalVariable.impersonatedUser + ' Exiting test.')
	KeywordUtil.markFailedAndStop('ERROR: There are no Quotes for ' + GlobalVariable.impersonatedUser + ' Exiting test.')
} else {
	//If there are more than 10 quotes, only print the first 10 since that is a page worth
	if (quoteResults>10) {
		quoteResults=10
	}

	//Print the first X number of Quote Names or 10 if there is an entire page
	//Additionally, check to make sure the quote has a name and a Bill To Account name
	for (loop = 1; loop<=quoteResults; loop++) {
		String quoteResultDetails=WebUI.getText(findTestObject('ABC/Quotes/text-Quote Name', [('Variable') : (loop+1)]))
		List quoteDetails=quoteResultDetails.split("\\r?\\n")
		quoteName=quoteDetails[0]
		billToAccountName=quoteDetails[1]
		quotePrice=WebUI.getText(findTestObject('ABC/Quotes/text-Quote Price', [('Variable') :(loop+1)]))
		//The quote name takes up two lines. This will convert it to one for printing. The text will be separated by a hyphen
		log.logWarning('The name of Quote #' + loop + ' is: ' + quoteName)
		log.logWarning('The name of the Bill To Account is: ' + billToAccountName)
		log.logWarning('The price of the Quote is: ' + quotePrice)
		//Check to make sure the quote has a name and a Bill To Account.
		if (quoteName=="" || billToAccountName=="" || billToAccountName=="#"){
			log.logError("ERROR: The quote does not have a Name or is missing the Bill To Account, which is incorrect")
			KeywordUtil.markError("ERROR: The quote does not have a Name or is missing the Bill To Account, which is incorrect")
		}
	}
}