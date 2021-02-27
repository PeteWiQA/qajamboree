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
import com.kms.katalon.core.util.KeywordUtil
KeywordLogger log = new KeywordLogger()

/* Verify that the Months on the YTD By Month Accordion are not duplicated
 * This has come up as an error more than once
 * Created 01/26/2018 by Pete Wilson
 * Updated 02-22-2018 11:33AM by Pete Wilson
 * Updated 04-19-2018 12:28PM by Pete Wilson
 * Updated 08-13-2018 15:24PM by Pete Wilson
 */

xpath="//*[@id=\'byMonth\']/div/table/tbody/tr"
WebUI.navigateToUrl(GlobalVariable.baseurl + '/v3/sales_dashboard')
//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')

WebUI.click(findTestObject('ABC/Sales Dashboard/Yearly Sales Section/accordion-YTD Sales Accordion', [('divIndex') : (GlobalVariable.dashboardAccordionValue+4)]))

//Because of slow page load, wait for the accordion to appear with the text Year To date Sales Detail
WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/accordion-Year To Date Sales Detail Banner'),15)

//Check to make sure the Sales Dashboard page loads. Look for the text, Year To Date Sales Detail
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Year To Date Sales Detail')

WebUI.delay(2)

WebUI.click(findTestObject('ABC/Sales Dashboard/Links/link-By Month'))

WebUI.delay(2)

int totalRowCount=CustomKeywords.'tools.commonCode.countRowsPerPage'(xpath)

//Count the number of months in the table. If there are more than 12, there's an error with duplication
log.logWarning('Number of Months displayed in the Table: ' + totalRowCount)
if (totalRowCount > 12){
	log.logError('There are more than 12 months. This is an error.')
	KeywordUtil.markFailed('ERROR: There are more than 12 months. This is an error.')
}

//Make sure there are more than 3 months available, otherwise the test won't be successful
if (totalRowCount>3){
	log.logWarning('The following months were found in the table:')
	//If the above passes, such as an onboarded user, check that the text of the first month isn't the same as the second
	//Compare the first two months, display the first three for visual comparison
	monthName1=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Month Tab/table-Year to Date Totals', [('row') : 1, ('column') : 1]))
	log.logWarning('Month:=' + monthName1)
	monthName2=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Month Tab/table-Year to Date Totals', [('row') : 2, ('column') : 1]))
	log.logWarning('Month:=' + monthName2)
	monthName3=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Month Tab/table-Year to Date Totals', [('row') : 3, ('column') : 1]))
	log.logWarning('Month:=' + monthName3)

	if (monthName1==monthName2){
		log.logError('The two months sampled have the same text. The months appear to be duplicated.')
		KeywordUtil.markFailed('ERROR: The two months sampled have the same text. The months appear to be duplicated.')
	}

	if (monthName1==monthName3){
		log.logError('The two months sampled have the same text. The months appear to be duplicated.')
		KeywordUtil.markFailed('ERROR: The two months sampled have the same text. The months appear to be duplicated.')
	}

	if (monthName2==monthName3){
		log.logError('The two months sampled have the same text. The months appear to be duplicated.')
		KeywordUtil.markFailed('ERROR: The two months sampled have the same text. The months appear to be duplicated.')
	}
}