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

/* The purpose of this test is to read the Yearly Sales Detail By Product Category figures
 * It is a read-only test to verify that the Sales Detail page loads
 * and the figures for the Sales Rep can be read.
 * Can be used as a Before/After validation test
 * Updated 04-19-2018 12:20PM by Pete Wilson
 * Updated 08-13-2018 14:21PM by Pete Wilson
 * Updated 11-14-2018 16:02PM by Pete Wilson
 * Updated 12-19-2018 10:46AM by Pete Wilson
 */

int numberOfRows=0
//Determine the number of rows in the table based on the users division
if(GlobalVariable.userDivision.toLowerCase()=="abc"){
	numberOfRows=10 //number of rows in the table
} else {
	numberOfRows=9 //number of rows in the table
}

WebUI.navigateToUrl(GlobalVariable.baseurl + '/v3/sales_dashboard')
//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')

WebUI.click(findTestObject('ABC/Sales Dashboard/Yearly Sales Section/accordion-YTD Sales Accordion', [('divIndex') : (GlobalVariable.dashboardAccordionValue+4)]))

//Because of slow page load, wait for the accordion to appear with the text Year To date Sales Detail
WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/accordion-Year To Date Sales Detail Banner'),15)

//Check to make sure the Sales Dashboard page loads. Look for the text, Year To Date Sales Detail
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Year To Date Sales Detail')

WebUI.delay(2)

WebUI.click(findTestObject('ABC/Sales Dashboard/Links/link-By Product Category'))

WebUI.delay(2)
//Wait for the Footer Totals to appear. This should give the page time to load
WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Product Category/table-Footer Row Totals', [('column') : 2]), 15)

//Verify the Product Categories match the user division
CustomKeywords.'tools.commonCode.verifyProductCategory'(GlobalVariable.userDivision, 'ABC/Sales Dashboard/Yearly Sales Details/By Product Category/table-Year to Date Totals')

for (row = 1; row <= numberOfRows; row++) {

	categoryName=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Product Category/table-Year to Date Totals', [('row') : row, ('column') : 1]))

	log.logWarning('Category Name:=' + categoryName)

	sales=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Product Category/table-Year to Date Totals', [('row') : row, ('column') : 2]))

	log.logWarning('Sales Figure Amount:=' + sales)

	plan=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Product Category/table-Year to Date Totals', [('row') : row, ('column') : 3]))

	log.logWarning('Plan Value:=' + plan)

	difference=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Product Category/table-Year to Date Totals', [('row') : row, ('column') : 4]))

	log.logWarning('Difference Value :=' + difference)

	gpDollar=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Product Category/table-Year to Date Totals', [('row') : row, ('column') : 5]))

	log.logWarning('GP$:=' + gpDollar)

	planGP=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Product Category/table-Year to Date Totals', [('row') : row, ('column') : 6]))

	log.logWarning('Plan GP$:=' + planGP)

	planDifference=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Product Category/table-Year to Date Totals', [('row') : row, ('column') : 7]))

	log.logWarning('Plan Difference:=' + planDifference)

	margin=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Product Category/table-Year to Date Totals', [('row') : row, ('column') : 8]))

	log.logWarning('Margin Value :=' + margin)
}

//Read Totals from the bottom of the table (footer)
log.logWarning('-- Table Footer Totals --')
for (column = 1; column <=8; column++) {
	totalRow= WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Product Category/table-Footer Row Totals', [('column') : column]))
	log.logWarning('Totals:=' + totalRow)
}