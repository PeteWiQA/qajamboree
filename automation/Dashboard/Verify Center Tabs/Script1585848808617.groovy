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

/* The purpose of this test is to click the Transaction tab
 * and verify there is text within the tab. This contains
 * Transaction types and dollar figures. It checks none of the
 * columns are blank.
 * 
 * Currently the test is only for Transactions. It can be expanded
 * to check other tabs at a later date
 * 
 * Created 04-02-2020 13:57PM 
 * 
 */

String transactionType, transactionAmount, totalTransactions, documentText
boolean elementVisible

//Click the Transaction tab
WebUI.click(findTestObject('Object Repository/Dashboard/tab-CenterTab', [('index'):2]))

//Confirm Total Transactions exists
elementVisible=WebUI.verifyElementVisible(findTestObject('Dashboard/text-Total Transactions'))
	if (elementVisible){
		totalTransactions=WebUI.getText(findTestObject('Dashboard/text-Total Transactions'))
	} else {
		KeywordUtil.markFailed('ERROR: The text for Total Transactions is missing')
	}
 
//Read Transaction table which starts at Row 2
for (loop = 2; loop <=10; loop++) {
	transactionType=WebUI.getText(findTestObject('Dashboard/table-Total Transactions', [('row') : loop, ('column') : 1]))
	transactionAmount=WebUI.getText(findTestObject('Dashboard/table-Total Transactions', [('row') : loop, ('column') : 2]))
	valueNotEmpty(transactionType, transactionAmount)
}

//Click Documents tab
WebUI.click(findTestObject('Object Repository/Dashboard/tab-CenterTab', [('index'):3]))

//Read the first 3 documents and confirm the link is clickable
for (loop = 1; loop <=3; loop++) {
	documentText=WebUI.getText(findTestObject('Dashboard/text-Documents', [('row') : loop]))
	isClickable=WebUI.verifyElementClickable(findTestObject('Dashboard/link-Documents', [('row') : loop]))
	log.logWarning(documentText +" : " + isClickable)
	if (documentText=="" || documentText==null){
		KeywordUtil.markFailed('ERROR: The Document has no text')
	}
	if (isClickable!=true){
		KeywordUtil.markFailed('ERROR: The Document link: ' + documentText + ' was not clickable')
	}
}

def valueNotEmpty(String rowName, String columnValue){
	/* Confirm the passed values are not empty or null. Mark the test
	 * as Failed if they are.
	 * @param rowName - The text of the cell
	 * @param columnValue - The numberical value from, usually a dollar figure
	 */
	KeywordLogger log = new KeywordLogger()
	log.logWarning(rowName + " : " + columnValue)
	if (rowName=="" || rowName==null || columnValue=="" || columnValue==null){
		log.logWarning("A name, product, or sales figure is missing")
		KeywordUtil.markFailed('ERROR: A name, product, or sales figure is missing')		
	}
}
