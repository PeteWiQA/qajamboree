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

/* The purpose of this test is to calculate the Gross Profit Plan values on the Sales Dashboard
 * This is calculated for Daily, Monthly and YTD
 * Updated 02-20-2018 11:57AM by Pete Wilson
 * Updated 02-21-2018 14:33PM by Pete Wilson
 * Updated 03-01-2018 11:13AM by Pete Wilson
 * Updated 08-09-2018 15:54PM by Pete Wilson
 * Updated 11-13-2018 11:42AM by Pete Wilson
 */

//Determine if the user is a Sales Rep or Manager
int accordionValue=CustomKeywords.'tools.commonCode.getUserRole'()

//Method to calculate the difference between calculated profit value and the site value
def verifyProfit(int grossProfit, int siteProfit){
	KeywordLogger log = new KeywordLogger()
	if (grossProfit != siteProfit) {
		log.logWarning('The site and calculated values DO NOT match!')
		int calcDifference=(grossProfit-siteProfit)
		log.logWarning('The Calculated difference is := ' + calcDifference)
		if (Math.abs(calcDifference)<=5){
			log.logWarning('The difference is less than $5, most likely a rounding difference and is acceptable')
		} else {
			log.logError('ERROR: The difference between the site and the calculated value is greater than $5. Investigation is needed')
			KeywordUtil.markFailed('ERROR: The difference between the site and the calculated value is greater than $5. Investigation is needed')
		}
	}
}

WebUI.navigateToUrl(GlobalVariable.baseurl + '/v3/sales_dashboard')

//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')

importDate = WebUI.getText(findTestObject('ABC/Sales Dashboard/Main Dashboard/text-Sales Import Date', [('index'):(GlobalVariable.dashboardAccordionValue+2)]))

//Split the string into two pieces at the hyphen - character
String[] parsedDate = importDate.split(' - ')

// The first part of the string is the import date with the opening parenthesis removed
importDate = parsedDate[0].replaceAll('\\(', '')

//Call the import date parser to set import date values
WebUI.callTestCase(findTestCase('Sales Dashboard Tests/Main Dashboard Tests/Set Import Date Values'), [:])

//Read the YTD Import date from the YTD Accordion
ytdDate = WebUI.getText(findTestObject('ABC/Sales Dashboard/Main Dashboard/text-Sales Import Date', [('index'):(GlobalVariable.dashboardAccordionValue+4)]))

tempText = (ytdDate.split('-')[1])

//Read the Current Business Day
ytdReportDay = tempText.substring(5, 8)

//Read the Total Business Days
ytdReportMonth = tempText.substring(12, 15)

// Read Daily Gross Profit Plan
siteDailyGrossProfit = WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Detail Values for Daily-MTD-YTD', [('divIndex') : accordionValue, ('rowIndex') : 5])).replaceAll("[^0-9-]","").toInteger()

// Read Monthly Gross Profit Plan
siteMTDGrossProfit = WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Detail Values for Daily-MTD-YTD', [('divIndex') : (accordionValue+2), ('rowIndex') : 5])).replaceAll("[^0-9-]","").toInteger()

// Read Yearly Gross Profit Plan
siteYTDGrossProfit = WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Detail Values for Daily-MTD-YTD', [('divIndex') : (accordionValue+4), ('rowIndex') : 5])).replaceAll("[^0-9-]","").toInteger()

WebUI.click(findTestObject('ABC/Sales Dashboard/Yearly Sales Section/accordion-YTD Sales Accordion', [('divIndex') : (GlobalVariable.dashboardAccordionValue+4)]))

WebUI.click(findTestObject('ABC/Sales Dashboard/Links/link-By Month'))

grossProfitPlanTotal = 0
monthOfYear=Integer.valueOf(GlobalVariable.siteImportMonth)

//Read the Gross Profit Plan Values from the YTD By Month table for the previous months, ie-if the month is December, read Jan-Nov
//Sum up the values
//This is a hack and I know it, but it's the easiest way to handle January
//If the month is January, set the Month to 2, so that it can subtract 1 from the next line and the loop still works.
if (GlobalVariable.siteImportMonth==1){
	monthOfYear=2
}

for (loop = 1; loop <= (monthOfYear - 1); loop++) {
    grossProfitMonth = WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Month Tab/table-Year to Date Totals', [('row') : loop, ('column') : 6])).replaceAll("[^0-9.]","").toInteger()
	grossProfitPlanTotal = grossProfitPlanTotal + grossProfitMonth
}

//Read the Gross Profit Plan value for the current month. monthOfYear is defined above
grossProfitCurrentMonth = WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Month Tab/table-Year to Date Totals', [('row') : monthOfYear, ('column') : 6])).replaceAll("[^0-9.]","").toInteger()

//Calculate Daily Gross Profit Plan which is the Gross Profit Plan for the current month / number of business days in the month
dailyGrossProfitPlan = 0
//Divide the Plan Profit for the current month by the total number of business days in the month
int dailyGrossProfitPlan = (Integer.valueOf(grossProfitCurrentMonth) / Integer.valueOf(GlobalVariable.siteTotalmtdBusinessDays))
log.logWarning('-- Daily Gross Profit Plan value is ' + dailyGrossProfitPlan + ' The site value is ' + siteDailyGrossProfit)
verifyProfit(dailyGrossProfitPlan, siteDailyGrossProfit)

//Calculate MTD Gross Profit Plan which is done by taking the daily plan value and multiplying by number of business days passed for the month
mtdGrossProfitPlan = 0
int mtdGrossProfitPlan = (Integer.valueOf(grossProfitCurrentMonth) / Integer.valueOf(GlobalVariable.siteTotalmtdBusinessDays) * Integer.valueOf(GlobalVariable.sitemtdCurrentBusinessDay))
log.logWarning('-- Monthly Gross Profit Plan value is ' + mtdGrossProfitPlan + ' The site value is ' + siteMTDGrossProfit)
verifyProfit(mtdGrossProfitPlan, siteMTDGrossProfit)

//Calculate YTD Gross Profit Plan
//YTD GP Plan $ = (Sum of Expired Months GP Profit Plan $) + [current month GP Profit Plan $ * (Days Expired in Month/Days in Month)]
ytdGrossProfitPlan = 0
int ytdGrossProfitPlan = Integer.valueOf(grossProfitPlanTotal) + Integer.valueOf(mtdGrossProfitPlan)
log.logWarning('-- Yearly Gross Profit Plan value is ' + ytdGrossProfitPlan + ' The site value is ' + siteYTDGrossProfit)
verifyProfit(ytdGrossProfitPlan, siteYTDGrossProfit)