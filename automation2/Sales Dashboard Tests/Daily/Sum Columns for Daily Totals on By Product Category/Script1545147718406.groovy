import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger
import com.kms.katalon.core.util.KeywordUtil

KeywordLogger log = new KeywordLogger()

/* The purpose of this test is to Sum the Columns for Sales and GP
 * on the Daily Totals table on the Daily Sales Detail Page.
 * The total is then compared against the footer to see if they match
 * 
 * Updated 12-18-2018 10:40AM by Pete Wilson
 */

//Define the table object, number of rows, and the row for the footer total
String tableToSum="ABC/Sales Dashboard/Daily Sales Details/table-By Product Category - Daily Totals"
int numberOfRows=0
int footerRowTotal=0
//Determine the number of rows in the table based on the users division
if(GlobalVariable.userDivision=="ABC"){
	numberOfRows=10 //number of rows in the table
	footerRowTotal=11 //row where the footer total will display
} else {
	numberOfRows=9 //number of rows in the table
	footerRowTotal=10 //row where the footer total will display
}

WebUI.navigateToUrl(GlobalVariable.baseurl + '/v3/sales_dashboard')

//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')

WebUI.click(findTestObject('ABC/Sales Dashboard/Daily Sales Section/accordion-Daily Sales Accordion', [('divIndex') : GlobalVariable.dashboardAccordionValue]))

//Because of slow page load, wait for the accordion to appear with the text Daily Sales Detail
WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Daily Sales Details/accordion-Daily Sales Detail Banner'),15)

//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales Detail
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales Detail')

WebUI.click(findTestObject('ABC/Sales Dashboard/Links/link-By Product Category'))

WebUI.delay(2)

//Sum the columns of the table
int calculatedSalesTotal=CustomKeywords.'tools.commonCode.sumColumnTotal'(tableToSum, 2, numberOfRows)
int calculatedGPTotal=CustomKeywords.'tools.commonCode.sumColumnTotal'(tableToSum, 3, numberOfRows)

//Get the total from the footer of the table
int siteSalesTotal=WebUI.getText(findTestObject(tableToSum, [('row') : footerRowTotal, ('column') : 2])).replaceAll("[^0-9-]","").toInteger()
int siteGPTotal=WebUI.getText(findTestObject(tableToSum, [('row') : footerRowTotal, ('column') : 3])).replaceAll("[^0-9-]","").toInteger()

//Output the values for Calculated and Site Totals
	log.logWarning('Calculated Daily Sales Total: ' + calculatedSalesTotal + ', Sales Total from Site: ' + siteSalesTotal)
	log.logWarning('Calculated GP Sales Total: ' + calculatedGPTotal + ', GP Total from Site: ' + siteGPTotal)

//Compare the calculated total to the site total
CustomKeywords.'tools.commonCode.compareCalculatedToSiteTotals'(calculatedSalesTotal, siteSalesTotal, "Sales")
CustomKeywords.'tools.commonCode.compareCalculatedToSiteTotals'(calculatedGPTotal, siteGPTotal, "GP")