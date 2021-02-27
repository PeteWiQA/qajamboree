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

/* The purpose of this test is to read the values from 5 tables
 * on the Dashboard and confirm they are not blank. There needs
 * to be text in both the columns. The actual values are not checked
 * 
 * Created 04-01-2020 by Pete Wilson
 * 
 */

String rowName
String columnValue
int loop

//Read YTD Sales and confirm values are not empty or $0
for (loop = 1; loop <=3; loop++) {
	rowName = WebUI.getText(findTestObject('Dashboard/table-YTDSales', [('row') : loop, ('column') : 1]))
	columnValue=WebUI.getText(findTestObject('Dashboard/table-YTDSales', [('row') : loop, ('column') : 2]))
	valueNotEmpty(rowName, columnValue)

}

//Read Top 5 Retailers By Sales and confirm values are not empty or $0
for (loop = 1; loop <=5; loop++) {
	rowName = WebUI.getText(findTestObject('Dashboard/table-Top 5 Retailers', [('row') : loop, ('column') : 1]))
	columnValue=WebUI.getText(findTestObject('Dashboard/table-Top 5 Retailers', [('row') : loop, ('column') : 2]))
	valueNotEmpty(rowName, columnValue)
}

//Read Top 5 Farmers by Sales and confirm values are not empty or $0
for (loop = 1; loop <=5; loop++) {
	rowName = WebUI.getText(findTestObject('Dashboard/table-Top 5 Farmers', [('row') : loop, ('column') : 1]))
	columnValue=WebUI.getText(findTestObject('Dashboard/table-Top 5 Farmers', [('row') : loop, ('column') : 2]))
	valueNotEmpty(rowName, columnValue)
}

//Read Top 5 Crop Protection Products by Sales and confirm values are not empty or $0
for (loop = 1; loop <=5; loop++) {
	rowName = WebUI.getText(findTestObject('Dashboard/table-Top 5 Crop', [('row') : loop, ('column') : 1]))
	columnValue=WebUI.getText(findTestObject('Dashboard/table-Top 5 Crop', [('row') : loop, ('column') : 2]))
	valueNotEmpty(rowName, columnValue)
}

//Read Top 5 Seed Products by Sales and confirm values are not empty or $0
for (loop = 1; loop <=5; loop++) {
	rowName = WebUI.getText(findTestObject('Dashboard/table-Top 5 Crop', [('row') : loop, ('column') : 1]))
	columnValue=WebUI.getText(findTestObject('Dashboard/table-Top 5 Crop', [('row') : loop, ('column') : 2]))
	valueNotEmpty(rowName, columnValue)
}


def valueNotEmpty(String rowName, String columnValue){
	/* Confirm the value from the table. If the value is empty or null
	 * mark the test as Failed.
	 * @param rowName - Text from the cell
	 * @param columnValue - Text value from the column, usually a dollar figure
	 */
	KeywordLogger log = new KeywordLogger()
	log.logWarning(rowName + " : " + columnValue)
	if (rowName=="" || rowName==null || columnValue=="" || columnValue==null || columnValue=='0'){
		log.logWarning("A name, product, or sales figure is missing or 0")
		KeywordUtil.markFailed('ERROR: A name, product, or sales figure is missing or 0')
	}
}