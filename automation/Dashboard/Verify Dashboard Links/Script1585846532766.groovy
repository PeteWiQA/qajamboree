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

/* The purpose of this test is to check the links on the
 * Dashboard and check they are clickable
 * 
 * Created 04-02-2020 13:24PM by Pete Wilson
 * 
 */

boolean isClickable
String linkText

//Read the table What would you like to do?
log.logWarning('--The following links are available under What would you like to do?:')
for (loop = 1; loop <=5; loop++) {
	linkText=WebUI.getText((findTestObject('Dashboard/link-What To Do', [('row') : loop])))
	isClickable=WebUI.verifyElementClickable(findTestObject('Dashboard/link-What To Do', [('row') : loop]))
	log.logWarning(linkText +" : " + isClickable)
	if (linkText=="" || linkText==null){
		KeywordUtil.markFailed('ERROR: The link has no text')
	}
	if (isClickable!=true){
		KeywordUtil.markFailed('ERROR: The link: ' + linkText + ' was not clickable')
	}
}

//Read the table Training Center
log.logWarning('--The following links are available under Training Center:')
for (loop = 1; loop <=3; loop++) {
	linkText=WebUI.getText((findTestObject('Dashboard/link-Training Center', [('row') : loop])))
	isClickable=WebUI.verifyElementClickable(findTestObject('Dashboard/link-Training Center', [('row') : loop]))
	log.logWarning(linkText +" : " + isClickable)
	if (linkText=="" || linkText==null){
		KeywordUtil.markFailed('ERROR: The link has no text')
	}
	if (isClickable!=true){
		KeywordUtil.markFailed('ERROR: The link: ' + linkText + ' was not clickable')
	}
}