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
import org.openqa.selenium.Keys as Keys
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger

KeywordLogger log = new KeywordLogger()

/* Open the Customer Profile and switch to the 2018 Plan tab
 * Read the first quote on the page and display quote details:
 * Output the number of quotes, the quote name, the customer listed
 * the price quote, the created dated, the expiration date
 * Created 02-07-2018 09:31AM by Pete Wilson
 * Updated 02-22-2018 11:46AM by Pete Wilson
 * Updated 04-10-2018 13:41PM by Pete Wilson
 * Updated 08-16-2018 10:09AM by Pete Wilson
 * Updated 12-19-2018 10:52AM by Pete Wilson
 */

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

WebUI.click(findTestObject('ABC/Customer Profile/Tabs/tab-2018 Plan'))

//Wait for the Submission Date to appear on the new tab
WebUI.waitForElementVisible(findTestObject('ABC/Customer Search/Customer 2018 Plan Tab/text-Budget Submission Date'), 15)

//Read the Submission Date
budgetSubmissionDate = WebUI.getText(findTestObject('ABC/Customer Search/Customer 2018 Plan Tab/text-Budget Submission Date'))

//Verify the Product Categories match the user division
CustomKeywords.'tools.commonCode.verifyProductCategory'(GlobalVariable.userDivision, 'ABC/Customer Search/Customer 2018 Plan Tab/table-Budget Plan')

//Read the Product Category, the YTD Sales, YTD Plan from the table

log.logWarning('--- Read 2018 Plan Details ---')

for (loop = 1; loop <=numberOfRows; loop++) {
	planCategory=WebUI.getText(findTestObject('ABC/Customer Search/Customer 2018 Plan Tab/table-Budget Plan', [('row') : loop, ('column') : 1]))
	planYTDSales=WebUI.getText(findTestObject('ABC/Customer Search/Customer 2018 Plan Tab/table-Budget Plan', [('row') : loop, ('column') : 2]))
	planYTDPlan=WebUI.getText(findTestObject('ABC/Customer Search/Customer 2018 Plan Tab/table-Budget Plan', [('row') : loop, ('column') : 3]))
	log.logWarning('Category: ' + planCategory + ' -:- ' + 'YTD Sales: ' + planYTDSales + ' -:- ' + 'YTD Plan: ' + planYTDPlan)
}

//Read the Total values from the Footer of the table - YTD Sales and YTD Plan
//For the Footer, 2=YTD Sales, 3=YTD Plan - These are the columns of the table

ytdSalesTotal = WebUI.getText(findTestObject('ABC/Customer Search/Customer 2018 Plan Tab/table-Budget Plan', [('row') : tableFooterRow, ('column') : 2]))

ytdSalesPlanTotal = WebUI.getText(findTestObject('ABC/Customer Search/Customer 2018 Plan Tab/table-Budget Plan', [('row') : tableFooterRow, ('column') : 3]))

//Write out the budget details of the submission date, sales total and plan total

log.logWarning('The Budget Submission date is: ' + budgetSubmissionDate)

log.logWarning('The YTD Sales Total from the table is: ' + ytdSalesTotal)

log.logWarning('The YTD Sales Plan Total from the table is: ' + ytdSalesPlanTotal)

//Check the YTD Sales value, it should be greater than $0
if (ytdSalesTotal == 0) {
	log.logError('ERROR: The Budget YTD Sales Amount equals $0 and could indicate a problem')
	KeywordUtil.markError('ERROR: The Budget YTD Sales Amount equals $0 and could indicate a problem')
} else {
	log.logWarning('SUCCESS: The Budget Sales Amount looks to be valid')
}

if (ytdSalesPlanTotal == 0) {
	log.logError('ERROR: The Budget YTD Plan Amount equals $0 and could indicate a problem')
	KeywordUtil.markError('ERROR: The Budget YTD Sales Amount equals $0 and could indicate a problem')
} else {
	log.logWarning('SUCCESS: The Budget Sales Plan Amount looks to be valid')
}