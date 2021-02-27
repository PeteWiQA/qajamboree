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
import org.openqa.selenium.WebDriver as WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import org.openqa.selenium.By as By
import org.openqa.selenium.WebElement as WebElement
import com.kms.katalon.core.annotation.Keyword as Keyword
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger

KeywordLogger log = new KeywordLogger()

/* The purpose of this test is to read the Monthly Sales Details By Customer tab values
 * It is a read-only test and writes the output to the log file
 * It then reads the Totals at the bottom for validation
 * Updated 04-19-2018 11:51AM by Pete Wilson
 * Updated 08-10-2018 16:23PM by Pete Wilson
 */

xpath="//*[@id=\'sales_dash_table\']/tbody/tr"

WebUI.navigateToUrl(GlobalVariable.baseurl + '/v3/sales_dashboard')
//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')

WebUI.click(findTestObject('ABC/Sales Dashboard/Monthly Sales Section/accordion-Monthly Sales Accordion', [('divIndex') : (GlobalVariable.dashboardAccordionValue+2)]))

//Because of slow page load, wait for the accordion to appear with the text Month To date Sales Detail
WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Monthly Sales Details/accordion-Month To Date Sales Detail Banner'),15)

//Check to make sure the Sales Dashboard page loads. Look for the text, Month To Date Sales Detail
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Month To Date Sales Detail')

WebUI.delay(2)

WebUI.click(findTestObject('ABC/Sales Dashboard/Links/link-By Customer'))

//Wait for the Footer Total lines to appear
WebUI.delay(2)
WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Common Objects/table-By Customer Total Values for Daily-MTD-YTD',[('row') : 2, ('column') : 1]), 15)

int totalRowCount=CustomKeywords.'tools.commonCode.countRowsPerPage'(xpath)
log.logWarning('Rows:=' + totalRowCount)

//Loop through the table and output the information to Log File
//Read columns 1-4, assign each to a variable, then output the result to the Log File
for (int loop = 1; loop <= totalRowCount; loop++) {
	customerName=WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-By Customer Total Values for Daily-MTD-YTD', [('row') : loop, ('column') : 1]))

	log.logWarning('Customer Name:=' + customerName)
	if (customerName==''){
		log.logError('ERROR: The Customer Name is blank. A problem has occurred')
	}

	sales=WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-By Customer Total Values for Daily-MTD-YTD', [('row') : loop, ('column') : 2]))

	log.logWarning('Sales Figure Amount:=' + sales)

	plan=WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-By Customer Total Values for Daily-MTD-YTD', [('row') : loop, ('column') : 3]))

	log.logWarning('Plan Value:=' + plan)

	difference=WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-By Customer Total Values for Daily-MTD-YTD', [('row') : loop, ('column') : 4]))

	log.logWarning('Difference Value :=' + difference)
}

//Read Totals from the bottom of the table (footer)
log.logWarning('-- Table Totals --')
for (loop = 1; loop <=4; loop++) {
	totalRow= WebUI.getText(findTestObject('ABC/Sales Dashboard/Monthly Sales Details/Monthly Sales Details By Customer Totals', [('Variable') : loop]))
	log.logWarning('Totals:=' + totalRow)
}