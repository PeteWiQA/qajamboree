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

/* The purpose of this test is calculate and set the import date values
 * These are read from the Sales Dashboard accordion values and are set as Global Variables
 * Updated 02-20-2018 11:57AM by Pete Wilson
 * Updated 02-21-2018 14:33PM by Pete Wilson
 * Updated 03-01-2018 11:13AM by Pete Wilson
 * Updated 08-09-2018 14:38PM by Pete Wilson
 */


WebUI.navigateToUrl(GlobalVariable.baseurl + '/v3/sales_dashboard')

//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')

mtdImportDate = WebUI.getText(findTestObject('ABC/Sales Dashboard/Main Dashboard/text-Sales Import Date', [('index'):(GlobalVariable.dashboardAccordionValue+2)]))

/*
Split the string into two pieces at the hyphen - character
The first part is the date
The second part is the current business day and total business days in the month
*/
String[] parsedDate = mtdImportDate.split(' - ')

// The first part of the string is the import date with the opening parenthesis removed, ie-12/21/2017
mtdImportDate = parsedDate[0].replaceAll('\\(', '')

// The second part of the string is the business days of the month with the ending parenthesis removed, ie-DAY 15 OF 19
mtdBusinessDays = parsedDate[1].replaceAll('\\)', '')

//Get the current date and set the value for the current day and month
//For an additional check, the day of the week and yesterday's date will be needed
//Get the day and date.
today = new Date()
//Set yesterday to 1 day ago
yesterday = today.previous()
//Set the date format to MM/dd/YYYY. This is done to remove the additional text of the date format such as Sun Dec 31 10:34:18 EST 2017
todayDate = today.format('MM/dd/yyyy')
reportDate = yesterday.format('MM/dd/yyyy')
//Set the day of the week to a word, such as Monday rather than Mon.
dayOfWeek = today.format('EEEE')
//Separate out the month as a two digit number
thisMonth = today.format('MM')

//If the month is less than 10, change the formatting to only dispay 1 digit for month.
//On the site, we pad the day, 1/06/2018, but this is not done for month
if (thisMonth.toInteger()<10) {
  reportDate=yesterday.format("M/dd/yyyy")
  mtdMonthOfYear=mtdImportDate.substring(0,1)
  
	// For the import date, pull out the day
	mtdDayOfMonth=mtdImportDate.substring(2,4)
} else {
    mtdMonthOfYear=mtdImportDate.substring(0,2)

    // For the import date, pull out the day
    mtdDayOfMonth=mtdImportDate.substring(3,5)
}
  
//Parse out the current day and number of days in the month, the 15 in Day 15 of 19
mtdCurrentBusinessDay = mtdBusinessDays.substring(4, 6)

//The number of business days in the month, the 19 in Day 15 of 19

//If the current business day is less than 10, the parsing position needs to
//shift to the right by 1
if (mtdCurrentBusinessDay.toInteger() < 10) {
    mtdCurrentBusinessDay = mtdBusinessDays.substring(4, 5)

    totalmtdBusinessDays = mtdBusinessDays.substring(9, 11)

} else {

    totalmtdBusinessDays = mtdBusinessDays.substring(10, 12)
}

//If the test is run on Monday, we need to back up 3 days to compensate for the weekend and confirm the test ran on Friday
if (dayOfWeek == "Monday") {
    yesterday = (today - 3)
  if (thisMonth.toInteger()<10) {
    reportDate=yesterday.format("M/dd/yyyy")
  } else {

    reportDate = yesterday.format("MM/dd/yyyy")
  }
}

//Read the YTD Import date from the YTD Accordion
ytdDate = WebUI.getText(findTestObject('ABC/Sales Dashboard/Main Dashboard/text-Sales Import Date', [('index'):(GlobalVariable.dashboardAccordionValue+4)]))

tempText = (ytdDate.split('-')[1])

//Read the Current Business Day
ytdReportDay = tempText.substring(5, 8)

//Read the Total Business Days
ytdReportMonth = tempText.substring(12, 15)

GlobalVariable.siteImportDay=mtdDayOfMonth
GlobalVariable.siteImportMonth=mtdMonthOfYear
GlobalVariable.sitemtdCurrentBusinessDay=mtdCurrentBusinessDay
GlobalVariable.siteTotalmtdBusinessDays=totalmtdBusinessDays
GlobalVariable.siteytdReportDay=ytdReportDay
GlobalVariable.siteytdReportMonth=ytdReportMonth
GlobalVariable.siteReportDate=reportDate