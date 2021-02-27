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

/* The purpose of this test is to Sum the Previous 12 Month Figures on the
 * read-only version of the Sales Budget
 * It reads the Values for Sales and Gross Profit and compares them to the
 * values listed on the site.
 * It then sums the total and compares the value to the total on the page
 * If the difference is less than $5 (rounding), the figues will be considered a match
 * Created 02-27-2018 13:57PM by Pete Wilson
 * Updated 04-20-2018 12:15PM by Pete Wilson
 * Updated 08-15-2018 13:25PM by Pete Wilson
 * Updated 12-19-2018 11:29AM by Pete Wilson
 */

//Set up values for the Product Category list
//Determine the number of rows in the table based on the users division
int numberOfRows=0
int tableFooterRow=0
if(GlobalVariable.userDivision.toLowerCase()=="abc"){
	numberOfRows=10 //number of rows in the table
	tableFooterRow=11 //row for the table footer value
} else {
	numberOfRows=9 //number of rows in the table
	tableFooterRow=10 //row for the table footer value
}

//Verify the Product Categories match the user division
log.logWarning('--- Verifying Product Categories ---')
CustomKeywords.'tools.commonCode.verifyProductCategory'(GlobalVariable.userDivision, 'ABC/Read-Only Sales Plan Budget/Section 1 - Previous 12 Months/table-Previous 12 Months Summary')

log.logWarning('--- Begin Sum Previous 12 Months Figures ---')

/* Read and Sum the Sales Total column for Previous 12 Months Sales
 * Under Section 1, Sum the Category Values under the label Previous 12 Months Summary
 */

log.logWarning('--- Start Section 1 - Previous 12 Months Summary ---')
int tempSalesTotal=CustomKeywords.'tools.commonCode.sumColumnTotal'('ABC/Read-Only Sales Plan Budget/Section 1 - Previous 12 Months/table-Previous 12 Months Summary', 2, numberOfRows)

//Read Sales Total for the Column Footer (Row 11)
log.logWarning('--- Read Previous 12 Months Sales Values ---')
reviewPlanTotal=WebUI.getText(findTestObject('ABC/Read-Only Sales Plan Budget/Section 1 - Previous 12 Months/table-Previous 12 Months Summary', [('row') : tableFooterRow, ('column') : 2])).replaceAll("[^0-9-]","").toInteger()

//Display the calculated and site values
log.logWarning('The calculated Sales Total is:=' + tempSalesTotal + ' --- The site Sales Total is:=' + reviewPlanTotal)
CustomKeywords.'tools.commonCode.compareCalculatedToSiteTotals'(tempSalesTotal, reviewPlanTotal, "Sales")

//Read and Sum the Gross Profit column
log.logWarning('--- Read Previous 12 Months Gross Profit Values ---')
int tempGrossProfitTotal=CustomKeywords.'tools.commonCode.sumColumnTotal'('ABC/Read-Only Sales Plan Budget/Section 1 - Previous 12 Months/table-Previous 12 Months Summary', 3, numberOfRows)

//Read Gross Profit Total for the Column Footer(Row 11)
reviewGrossProfitPlanTotal=WebUI.getText(findTestObject('ABC/Read-Only Sales Plan Budget/Section 1 - Previous 12 Months/table-Previous 12 Months Summary', [('row') : tableFooterRow, ('column') : 3])).replaceAll("[^0-9-]","").toInteger()

//Display the calculated and site values
log.logWarning('The calculated Gross Profit Total is:=' + tempGrossProfitTotal + ' --- The site Gross Profit Total is:=' + reviewGrossProfitPlanTotal)
CustomKeywords.'tools.commonCode.compareCalculatedToSiteTotals'(tempGrossProfitTotal, reviewGrossProfitPlanTotal, "Gross Profit")

log.logWarning('--- End Sum Previous 12 Months Figures ---')