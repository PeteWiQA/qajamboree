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

/* The purpose of this test is to read and sum the values
 * for Sales and Gross Profit listed in Section 5 - Review Plan
 * The total is then compared to the Total Sales Plan value in the
 * footer for each column. A difference of up to $5 is acceptable
 * Created 02-27-2018 11:40AM by Pete Wilson
 * Updated 04-20-2018 12:21PM by Pete Wilson
 * Updated 08-15-2018 13:39PM by Pete Wilson
 * Updated 12-19-2018 12:05PM by Pete Wilson
 */

//Set up values for the Product Category list
//Determine the number of rows in the table based on the users division
int numberOfRows=0
int tableFooterRow=0
if(GlobalVariable.userDivision.toLowerCase()=="abc"){
	numberOfRows=10 //number of rows in the table
	tableFooterRow=13 //row for the table footer value
} else {
	numberOfRows=9 //number of rows in the table
	tableFooterRow=12 //row for the table footer value
}

log.logWarning('--- Verifying Product Categories ---')
//Verify the Product Categories match the user division
CustomKeywords.'tools.commonCode.verifyProductCategory'(GlobalVariable.userDivision, 'ABC/Read-Only Sales Plan Budget/Section 5 - Review Plan/table-Review Plan')

log.logWarning('--- Begin Sum Review Plan Figures ---')

//Read and Sum the Sales Total column
log.logWarning('--- Start Section 5 - Review 2018 Plan ---')

int tempSalesTotal=CustomKeywords.'tools.commonCode.sumColumnTotal'('ABC/Read-Only Sales Plan Budget/Section 5 - Review Plan/table-Review Plan', 2, numberOfRows)

log.logWarning('--- Read Sales Footer Value ---')

//Read Sales Total for the Column Footer (Row 13)
reviewPlanTotal=WebUI.getText(findTestObject('ABC/Read-Only Sales Plan Budget/Section 5 - Review Plan/table-Review Plan', [('row') : tableFooterRow, ('column') : 2])).replaceAll("[^0-9-]","").toInteger()

//Display the calculated and site values
log.logWarning('The calculated Sales Total is:=' + tempSalesTotal + ' --- The site Sales Total is:=' + reviewPlanTotal)
CustomKeywords.'tools.commonCode.compareCalculatedToSiteTotals'(tempSalesTotal, reviewPlanTotal, "Sales")

//Read and Sum the Gross Profit column
log.logWarning('--- Read Gross Profit Values ---')
int tempGrossProfitTotal=CustomKeywords.'tools.commonCode.sumColumnTotal'('ABC/Read-Only Sales Plan Budget/Section 5 - Review Plan/table-Review Plan', 3, numberOfRows)

//Read Gross Profit Total for the Column Footer(Row 13)
reviewGrossProfitPlanTotal=WebUI.getText(findTestObject('ABC/Read-Only Sales Plan Budget/Section 5 - Review Plan/table-Review Plan', [('row') : tableFooterRow, ('column') : 3])).replaceAll("[^0-9-]","").toInteger()

//Display the calculated and site values
log.logWarning('The calculated Gross Profit Total is:=' + tempGrossProfitTotal + ' --- The site Gross Profit Total is:=' + reviewGrossProfitPlanTotal)
CustomKeywords.'tools.commonCode.compareCalculatedToSiteTotals'(tempGrossProfitTotal, reviewGrossProfitPlanTotal, "Gross Profit")

log.logWarning('--- End Sum Review Plan Figures ---')