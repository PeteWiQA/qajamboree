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
import org.openqa.selenium.Keys as Keys
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger

KeywordLogger log = new KeywordLogger()

/*Switch to the Quotes tab, and count the number of Quotes listed
 *If there is a Quote, read the details for name, company, price, creation date and expiration date
 *Created 02-07-2018 09:31AM by Pete Wilson
 *Updated 02-15-2018 11:10AM by Pete Wilson
 *Updated 02-22-2018 11:37AM by Pete Wilson
 *Updated 08-16-2018 10:16AM by Pete Wilson
 */

WebUI.click(findTestObject('ABC/Customer Profile/Tabs/tab-Quotes'))

log.logWarning('--- Read Quote Details ---')

WebUI.waitForElementVisible(findTestObject('ABC/Customer Profile/Quotes/text-Results found'), 10)

quoteResults=WebUI.getText(findTestObject('ABC/Customer Profile/Quotes/text-Results found')).replaceAll("[^0-9-]","").toInteger()

if (quoteResults==0){
	log.logError('ERROR: There are no Quotes on the Customer Profile')
} else {
//Write out the values from on the quote.
	log.logWarning('Number of quotes listed:= ' + quoteResults)
	log.logWarning('Quote Name:= ' + WebUI.getText(findTestObject('ABC/Customer Profile/Quotes/text-Quote Name')))
	log.logWarning('Customer listed on Quote:= ' + WebUI.getText(findTestObject('ABC/Customer Profile/Quotes/text-Customer Name')))
	log.logWarning('The Price of Quote:= ' + WebUI.getText(findTestObject('ABC/Customer Profile/Quotes/text-Quote Price')))
	log.logWarning('The Created Date of the Quote:= ' + WebUI.getText(findTestObject('ABC/Customer Profile/Quotes/text-Created Date')))
	log.logWarning('The Expiration Date of the Quote:= ' + WebUI.getText(findTestObject('ABC/Customer Profile/Quotes/text-Expiration Date')))
}