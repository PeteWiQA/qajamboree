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

/* On the Home page, read the Daily Sales Figure Value. 
 * If this is $0 or less, we may have an import problem.
 * While the value can be $0 due to rebates, for the customers we normally check
 * it shouldn't be.
 * Updated 04-18-2018 12:48PM by Pete Wilson
 * Updated 08-10-2018 11:44AM by Pete Wilson
 * 
 */

WebUI.navigateToUrl(GlobalVariable.baseurl)

//On the Home page, read the Daily Sales Figure Value. If this is $0 or less, we may have an import problem.
dailySalesFigure = WebUI.getText(findTestObject('ABC/Home/text-Daily Sales Figure')).replaceAll("[^0-9-]","").toInteger()

log.logWarning('The current Daily Sales Figure on the Home Page is: ' + dailySalesFigure)

//Verify the sales figure is greater than $0
if (dailySalesFigure<=0) {
	log.logWarning('ERROR: The Daily Sales Figure is $0 or less. A problem may have occurred with the import')
	KeywordUtil.markFailed('ERROR: The Daily Sales Figure is $0 or less. A problem may have occurred with the import')
} else {
	log.logWarning('SUCCESS: The Daily Sales Figure looks to be correct.')
}