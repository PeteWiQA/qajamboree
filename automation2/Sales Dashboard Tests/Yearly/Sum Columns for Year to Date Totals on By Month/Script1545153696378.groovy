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

/* The purpose of this test is to Sum the Columns for Sales (Sales, Plan, Differnece) and Gross Profit (GP, Plan GP, Difference)
 * on the Year To Date Totals table on the Yearly Sales Detail Page - By Month.
 * The total is then compared against the footer to see if they match
 * 
 * Updated 12-18-2018 10:40AM by Pete Wilson
 */

//Define the table object, number of rows, and the row for the footer total
String tableToSum="ABC/Sales Dashboard/Yearly Sales Details/By Month Tab/table-Year to Date Totals"
int numberOfRows=12 //number of rows in the table
//int footerRowTotal=10 //row where the footer total will display

WebUI.navigateToUrl(GlobalVariable.baseurl + '/v3/sales_dashboard')
//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')

WebUI.click(findTestObject('ABC/Sales Dashboard/Yearly Sales Section/accordion-YTD Sales Accordion', [('divIndex') : (GlobalVariable.dashboardAccordionValue+4)]))

//Because of slow page load, wait for the accordion to appear with the text Year To date Sales Detail
WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/accordion-Year To Date Sales Detail Banner'),15)

//Check to make sure the Sales Dashboard page loads. Look for the text, Month To Date Sales Detail
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Year To Date Sales Detail')

WebUI.click(findTestObject('ABC/Sales Dashboard/Links/link-By Month'))

WebUI.delay(2)

//Sum the columns of the table
int calculatedSalesTotal=CustomKeywords.'tools.commonCode.sumColumnTotal'(tableToSum, 2, numberOfRows)
int calculatedGPTotal=CustomKeywords.'tools.commonCode.sumColumnTotal'(tableToSum, 3, numberOfRows)
int calculatedSalesDifferenceTotal=CustomKeywords.'tools.commonCode.sumColumnTotal'(tableToSum, 4, numberOfRows)
int calculatedGPProfitTotal=CustomKeywords.'tools.commonCode.sumColumnTotal'(tableToSum, 5, numberOfRows)
int calculatedPlanGPTotal=CustomKeywords.'tools.commonCode.sumColumnTotal'(tableToSum, 6, numberOfRows)
int calculatedProfitDifferenceTotal=CustomKeywords.'tools.commonCode.sumColumnTotal'(tableToSum, 7, numberOfRows)

//Get the total from the footer of the table
int siteSalesTotal=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Month Tab/table-Footer Row Totals', [('column') : 2])).replaceAll("[^0-9-]","").toInteger()
int siteGPTotal=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Month Tab/table-Footer Row Totals', [('column') : 3])).replaceAll("[^0-9-]","").toInteger()
int siteSalesDifferenceTotal=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Month Tab/table-Footer Row Totals', [('column') : 4])).replaceAll("[^0-9-]","").toInteger()
int siteGPProfitTotal=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Month Tab/table-Footer Row Totals', [('column') : 5])).replaceAll("[^0-9-]","").toInteger()
int sitePlanGPTotal=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Month Tab/table-Footer Row Totals', [('column') : 6])).replaceAll("[^0-9-]","").toInteger()
int siteProfitDifferenceTotal=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Month Tab/table-Footer Row Totals', [('column') : 7])).replaceAll("[^0-9-]","").toInteger()


//Output the values for Calculated and Site Totals
	log.logWarning('Calculated Daily Sales Total: ' + calculatedSalesTotal + ', Sales Total from Site: ' + siteSalesTotal)
	log.logWarning('Calculated GP Sales Total: ' + calculatedGPTotal + ', GP Total from Site: ' + siteGPTotal)
	log.logWarning('Calculated Sales Difference Sales Total: ' + calculatedSalesDifferenceTotal + ', Sales Difference Total from Site: ' + siteSalesDifferenceTotal)
	log.logWarning('Calculated GP Profit Total: ' + calculatedGPProfitTotal + ', GP Profit Total from Site: ' + siteGPProfitTotal)
	log.logWarning('Calculated Plan GP Total: ' + calculatedPlanGPTotal + ', Plan GP Total from Site: ' + sitePlanGPTotal)
	log.logWarning('Calculated Sales Profit Difference Sales Total: ' + calculatedProfitDifferenceTotal + ', Sales Profit Difference Total from Site: ' + siteProfitDifferenceTotal)
	

//Compare the calculated total to the site total
CustomKeywords.'tools.commonCode.compareCalculatedToSiteTotals'(calculatedSalesTotal, siteSalesTotal, "Sales - Sales")
CustomKeywords.'tools.commonCode.compareCalculatedToSiteTotals'(calculatedGPTotal, siteGPTotal, "Sales - GP")
CustomKeywords.'tools.commonCode.compareCalculatedToSiteTotals'(calculatedSalesDifferenceTotal, siteSalesDifferenceTotal, "Sales - Difference")
CustomKeywords.'tools.commonCode.compareCalculatedToSiteTotals'(calculatedGPProfitTotal, siteGPProfitTotal, "Gross Profit - GP\$")
CustomKeywords.'tools.commonCode.compareCalculatedToSiteTotals'(calculatedPlanGPTotal, sitePlanGPTotal, "Gross Profit - Plan (GP\$) 	")
CustomKeywords.'tools.commonCode.compareCalculatedToSiteTotals'(calculatedProfitDifferenceTotal, siteProfitDifferenceTotal, "Gross Profit - Difference")
