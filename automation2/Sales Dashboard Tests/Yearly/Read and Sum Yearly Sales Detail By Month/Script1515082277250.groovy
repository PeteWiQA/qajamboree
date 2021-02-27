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
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger

KeywordLogger log = new KeywordLogger()

/* This test navigates to the Year to Date details accordion and selects the By Month tab
 * It reads down each of the 7 columns and sums the total
 * This is then compared to the totals in the footer to make sure they match
 * If there is a discrepancy, it shows the difference. The most likely case is to be off by $1 due to rounding
 * Updated 02-13-2018 11:08AM by Pete Wilson
 * Updated 02-22-2018 11:01AM by Pete Wilson
 * Updated 04-19-2018 12:16PM by Pete Wilson
 * Updated 08-13-2018 13:38PM by Pete Wilson
 * Updated 11-14-2018 16:17PM by Pete Wilson
 * Updated 12-17-2018 12:11PM by Pete Wilson
 */

//The Object Repository Path for the table to sum
String tableToSum="ABC/Sales Dashboard/Yearly Sales Details/By Month Tab/table-Year to Date Totals"

WebUI.navigateToUrl(GlobalVariable.baseurl + '/v3/sales_dashboard')
//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')

WebUI.click(findTestObject('ABC/Sales Dashboard/Yearly Sales Section/accordion-YTD Sales Accordion', [('divIndex') : (GlobalVariable.dashboardAccordionValue+4)]))

//Because of slow page load, wait for the accordion to appear with the text Year To date Sales Detail
WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/accordion-Year To Date Sales Detail Banner'),15)

//Check to make sure the Sales Dashboard page loads. Look for the text, Year To Date Sales Detail
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Year To Date Sales Detail')

WebUI.click(findTestObject('ABC/Sales Dashboard/Links/link-By Month'))

WebUI.delay(2)

//For the Year To Date Totals table, sum Columns 2-7 (Sales and Gross Profit)
int sales=0, plan=0, difference=0, gpDollar=0, planGP=0, planDifference=0

sales=CustomKeywords.'tools.commonCode.sumColumnTotal'(tableToSum, 2, 12)
plan=CustomKeywords.'tools.commonCode.sumColumnTotal'(tableToSum, 3, 12)
difference=CustomKeywords.'tools.commonCode.sumColumnTotal'(tableToSum, 4, 12)
gpDollar=CustomKeywords.'tools.commonCode.sumColumnTotal'(tableToSum, 5, 12)
planGP=CustomKeywords.'tools.commonCode.sumColumnTotal'(tableToSum, 6, 12)
planDifference=CustomKeywords.'tools.commonCode.sumColumnTotal'(tableToSum, 7, 12)

//Read Totals from the bottom of the table (footer)

headerSalesTotal=WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Details Row Values - Daily-MTD-YTD', [('index') : 1])).replaceAll("[^0-9-]","").toInteger()

totalSales=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Month Tab/table-Footer Row Totals', [('column') : 2])).replaceAll("[^0-9-]","").toInteger()

log.logWarning('Calculated Sales Figure:= ' + sales)
log.logWarning('YTD Sales Total from Site Header Section:= ' + headerSalesTotal)
log.logWarning('Total Sales Figure from Site Footer Value:= ' + totalSales)

//Compare the calculated value to the value in the header section
if (totalSales!=headerSalesTotal){
	log.logWarning('ERROR: The Calculated Total does not equal the Sales Total in the Sales Detail Header. The difference is ' + (totalSales-headerSalesTotal))
	if (Math.abs(totalSales-headerSalesTotal)<=5){
		log.logWarning('The difference is less than $5, most likely a rounding difference and is acceptable')
	} else {
		KeywordUtil.markFailed('ERROR: The Calculated Total does not equal the Total on the site. The difference is ' + (totalSales-headerSalesTotal))
	}
}

//Compare calculated sales total vs sales total in the footer
if (totalSales!=sales) {
	log.logWarning('ERROR: The Calculated Total does not equal the Total on the site. The difference is ' + (totalSales-sales))
	if (Math.abs(totalSales-sales)<=5){
		log.logWarning('The difference is less than $5, most likely a rounding difference and is acceptable')
	} else {
		KeywordUtil.markFailed('ERROR: The Calculated Total does not equal the Total on the site. The difference is ' + (totalSales-sales))
	}
}

//Calculate the differnce using the calcDifference method. Pass the Text to display in the output, the calculated figure from above and the column to check in the table
log.logWarning('--- Running Method ---')
CustomKeywords.'tools.commonCode.calcDifference'('Sales',sales,2)
CustomKeywords.'tools.commonCode.calcDifference'('Sales - Plan',plan,3)
CustomKeywords.'tools.commonCode.calcDifference'('Sales - Difference',difference,4)
CustomKeywords.'tools.commonCode.calcDifference'('Gross Profit - GP$',gpDollar,5)
CustomKeywords.'tools.commonCode.calcDifference'('Gross Profit - Plan (GP $)',planGP,6)
CustomKeywords.'tools.commonCode.calcDifference'('Gross Profit',planDifference,7)