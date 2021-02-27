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
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger

KeywordLogger log = new KeywordLogger()

/* The purpose of this script is to calculate the column totals for Sales
 * Plan and Difference on the MTD - By Product Category Tab
 * Updated 04-19-2018 11:34AM by Pete Wilson
 * Updated 08-10-2018 16:07PM by Pete Wilson
 * Updated 11-14-2018 14:44PM by Pete Wilson
 * Updated 12-17-2018 13:52PM by Pete Wilson
 */

log.logWarning('--- Begin Calculate Monthly Totals By Product Category Script  ---')

int salesTotal = 0, planTotal = 0, differenceTotal = 0
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

WebUI.click(findTestObject('ABC/Sales Dashboard/Monthly Sales Section/accordion-Monthly Sales Accordion', [('divIndex') : (GlobalVariable.dashboardAccordionValue+2)]))

//Because of slow page load, wait for the accordion to appear with the text Month To date Sales Detail
WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Monthly Sales Details/accordion-Month To Date Sales Detail Banner'),15)

//Check to make sure the Sales Dashboard page loads. Look for the text, Month To Date Sales Detail
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Month To Date Sales Detail')

WebUI.click(findTestObject('ABC/Sales Dashboard/Links/link-By Product Category'))
WebUI.delay(2)

log.logWarning('Monthly Sales Detail - Difference Values By Product Category')

//For each row, add the Sales and Profit values together and compare to the Difference figure
//We are adding horizontally then checking the total in the footer
for (loop = 1; loop <= numberOfRows; loop++) {
	//Read Sales
	salesValue=WebUI.getText(findTestObject('ABC/Sales Dashboard/Monthly Sales Details/table-By Product Category Totals', [('row') : loop, ('column') : 2])).replaceAll("[^0-9-]","").toInteger()

	//Read Plan
	planValue=WebUI.getText(findTestObject('ABC/Sales Dashboard/Monthly Sales Details/table-By Product Category Totals', [('row') : loop, ('column') : 3])).replaceAll("[^0-9-]","").toInteger()

	//Read Difference
	differenceValue=WebUI.getText(findTestObject('ABC/Sales Dashboard/Monthly Sales Details/table-By Product Category Totals', [('row') : loop, ('column') : 4])).replaceAll("[^0-9-]","").toInteger()

	//Sum the values for each category
	salesTotal = (salesValue + salesTotal)

	planTotal = (planValue + planTotal)

	differenceTotal = (differenceValue + differenceTotal)
	
	if ((salesValue-planValue)!=differenceValue){
		log.logWarning('ERROR: There is a discrepancy of Sales-Plan=Difference')
		log.logWarning('The difference is ' + ((salesValue-planValue)-differenceValue))
		if (Math.abs((salesValue-planValue)-differenceValue)<=3){
			log.logWarning('The difference is less than $5 and is most likely due to rounding and should be acceptable')		
		} else {
			log.logError('ERROR: The difference is higher than expected and should be investigated')
		}
	}
}

//Verify the Total in the column is close to the values in the Sales Detail Header
//Pass the total value calculated above and the index represents the "row" value to read from the upper table
log.logWarning('Comparing Sales Total to Sales Value in Detail Section')
CustomKeywords.'tools.commonCode.verifyTotalToDetail'(salesTotal,1)
log.logWarning('Comparing Plan Total to Plan Value in Detail Section')
CustomKeywords.'tools.commonCode.verifyTotalToDetail'(planTotal,2)
log.logWarning('Comparing Difference Total to Difference Value in Detail Section')
CustomKeywords.'tools.commonCode.verifyTotalToDetail'(differenceTotal,3)