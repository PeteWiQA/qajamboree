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
import com.kms.katalon.core.util.KeywordUtil

KeywordLogger log = new KeywordLogger()

/* Read the Sales Plan Figure for Daily, Monthly and Yearly and confirm the values are not the same.
 * If these values are the same, it usually indicates an import problem
 * Created 03-12-2018 15:19PM by Pete Wilson
 * Updated 04-18-2018 11:56AM by Pete Wilson
 * Updated 11-14-2018 11:49AM by Pete Wilson
 */

//Determine if the user is a Sales Rep or Manager
int accordionValue=CustomKeywords.'tools.commonCode.getUserRole'()

WebUI.navigateToUrl(GlobalVariable.baseurl + "/v3/sales_dashboard")

//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')

//Read the Daily Sales Plan Figure
dailySalesPlanFigure=WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Detail Values for Daily-MTD-YTD', [('divIndex') : (accordionValue), ('rowIndex') : 2])).replaceAll("[^0-9-]","").toInteger()
log.logWarning('Daily Sales Plan:=' + dailySalesPlanFigure)

//Read the Monthly Sales Plan Figure
monthlySalesPlanFigure=WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Detail Values for Daily-MTD-YTD', [('divIndex') : (accordionValue+2), ('rowIndex') : 2])).replaceAll("[^0-9-]","").toInteger()
log.logWarning('Monthly Sales Plan:=' + monthlySalesPlanFigure)

//Read the Yearly Sales Plan Figure
yearlySalesPlanFigure=WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Detail Values for Daily-MTD-YTD', [('divIndex') : (accordionValue+4), ('rowIndex') : 2])).replaceAll("[^0-9-]","").toInteger()
log.logWarning('Yearly Sales Plan:=' + yearlySalesPlanFigure)

if (dailySalesPlanFigure==monthlySalesPlanFigure){
	log.logError('ERROR: Daily and Monthly Sales Plan Figures are equal. This could indicate an error')
	KeywordUtil.markError('ERROR: Daily and Monthly Sales Plan Figures are equal. This could indicate an error')
} else {
	log.logWarning('SUCCESS: No duplicates')
}

if (monthlySalesPlanFigure==yearlySalesPlanFigure){
	log.logError('ERROR: Monthly and Yearly Sales Plan Figures are equal. This could indicate an error')
	KeywordUtil.markError('ERROR: Monthly and Yearly Sales Plan Figures are equal. This could indicate an error')
} else {
	log.logWarning('SUCCESS: No duplicates')
}