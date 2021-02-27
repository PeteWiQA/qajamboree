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
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger

KeywordLogger log = new KeywordLogger()


/* The purpose of this test is to determine if there is a sort order in place for MTD Sales Detail
 * While not 100% accurate, it reads the second and fourth number on the list 
 * and validates if the first number is larger, presumably sorted
 * Updated 04-19-2018 11:55AM by Pete Wilson
 * Updated 08-10-2018 15:10PM by Pete Wilson
 * Updated 11-14-2018 13:33PM by Pete Wilson
 */

xpath="//*[@id='sales_dash_table']/tbody/tr"

WebUI.navigateToUrl(GlobalVariable.baseurl + '/v3/sales_dashboard')
//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')

WebUI.click(findTestObject('ABC/Sales Dashboard/Monthly Sales Section/accordion-Monthly Sales Accordion', [('divIndex') : (GlobalVariable.dashboardAccordionValue+2)]))

//Because of slow page load, wait for the accordion to appear with the text Month To date Sales Detail
WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Monthly Sales Details/accordion-Month To Date Sales Detail Banner'),15)

//Check to make sure the Sales Dashboard page loads. Look for the text, Month To Date Sales Detail
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Month To Date Sales Detail')

WebUI.click(findTestObject('ABC/Sales Dashboard/Links/link-By Customer'))

WebUI.delay(2)

int totalRowCount=CustomKeywords.'tools.commonCode.countRowsPerPage'(xpath)
log.logWarning('Rows:=' + totalRowCount)
if (totalRowCount>4){
	totalRowCount=4
}

//Loop through the table and output the information to Log File
//Read columns 1-4, assign each to a variable, then output the result to the Log File

//Wait for the Footer totals to appear. Due to timing, the first company name isn't read correctly
WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Common Objects/table-By Customer Total Values for Daily-MTD-YTD',[('row') : 2, ('column') : 1]), 15)

//Get the second company name and sales figure from the list
companyName1=WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-By Customer Total Values for Daily-MTD-YTD', [('row') : 2, ('column') : 1]))
sales1=WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-By Customer Total Values for Daily-MTD-YTD', [('row') : 2, ('column') : 2])).replaceAll("[^0-9-]","").toInteger()

//Get the fourth company name and sales figure from the list
companyName2=WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-By Customer Total Values for Daily-MTD-YTD', [('row') : totalRowCount, ('column') : 1]))
sales2=WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-By Customer Total Values for Daily-MTD-YTD', [('row') : totalRowCount, ('column') : 2])).replaceAll("[^0-9-]","").toInteger()

//Write out the two company name and values for visual verification
log.logWarning('The Sales Value for Company: ' + companyName1 + ' is: ' + sales1)
log.logWarning('The Sales Value for Company: ' + companyName2 + ' is: ' + sales2)

if (sales1>=sales2) {
	log.logWarning('SUCCESS: The customer sort order is correct')
}else{
	log.logError('ERROR: There is a problem with the customer sort order')
	KeywordUtil.markFailed('ERROR: There is a problem with the customer sort order')
}
log.logWarning("--Verifying Product Categories based on Division --")
//Click the link for a customer and confirm Product Category
WebUI.callTestCase(findTestCase('Test Cases/Sales Dashboard Tests/Monthly/Verify MTD Product Category'), [:], FailureHandling.CONTINUE_ON_FAILURE)