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
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger

KeywordLogger log = new KeywordLogger()

/* This test reads the Sales and Sales Plan value 
 * from the Year to Date Sales Detail section of the Sales Dashboard
 * The values are compared to the Sales and Sales Plan totals listed in the Footer.
 * Created 03-28-2018 09:37AM by Pete Wilson
 * Updated 04-19-2018 12:13PM by Pete Wilson
 * Updated 08-13-2018 16:34PM by Pete Wilson
 * Updated 11-14-2018 15:58PM by Pete Wilson
 */

WebUI.navigateToUrl(GlobalVariable.baseurl + '/v3/sales_dashboard')
//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')

WebUI.click(findTestObject('ABC/Sales Dashboard/Yearly Sales Section/accordion-YTD Sales Accordion', [('divIndex') : (GlobalVariable.dashboardAccordionValue+4)]))

//Because of slow page load, wait for the accordion to appear with the text Year To date Sales Detail
WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/accordion-Year To Date Sales Detail Banner'),15)

//Check to make sure the Sales Dashboard page loads. Look for the text, Year To Date Sales Detail
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Year To Date Sales Detail')

//Read Sales figure from YTD Header Section
yearlySalesDetails=WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Details Row Values - Daily-MTD-YTD', [('index') : 1])).replaceAll("[^0-9-]","").toInteger()

log.logWarning('Monthly Sales Figure from YTD Sales Detail Header Section -- ' + yearlySalesDetails)

//Read Sales figure from YTD By Month tab Footer Totals
yearlySalesTotal=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Month Tab/table-Footer Row Totals', [('column') : 2])).replaceAll("[^0-9-]","").toInteger()
log.logWarning('Monthly Sales Total from YTD By Month Total Footer -- ' + yearlySalesTotal)

if (yearlySalesDetails==yearlySalesTotal){
	log.logWarning('SUCCESS: The Yearly Sales figure amount matches the YTD Sales Plan Total listed on YTD By Month tab')
} else {
	log.logError('ERROR: The Yearly Sales figure amount DOES NOT match the YTD Sales Plan Total listed on YTD By Month tab')
	KeywordUtil.markFailed('ERROR: The Yearly Sales figure amount DOES NOT match the YTD Sales Plan Total listed on YTD By Month tab')
}

//Read Sales figure from YTD Header Section
//yearlySalesPlanDetails=WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Details Row Values - Daily-MTD-YTD', [('index') : 2])).replaceAll('\\n|\\r', ' - ')
yearlySalesPlanDetails=WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Details Row Values - Daily-MTD-YTD', [('index') : 2])).replaceAll("[^0-9-]","").toInteger()

log.logWarning('Monthly Sales Figure from YTD Sales Detail Header Section -- ' + yearlySalesPlanDetails)

//Read Sales figure from YTD By Month tab Footer Totals
yearlySalesPlanTotal=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Month Tab/table-Footer Row Totals', [('column') : 3])).replaceAll("[^0-9-]","").toInteger()
log.logWarning('Monthly Sales Total from YTD By Month Total Footer -- ' + yearlySalesPlanTotal)


if (yearlySalesPlanDetails==yearlySalesPlanTotal){
	log.logWarning('SUCCESS: The Yearly Sales figure amount matches the YTD Sales Plan Total listed on YTD By Month tab')
} else {
	log.logError('ERROR: The Yearly Sales figure amount DOES NOT match the YTD Sales Plan Total listed on YTD By Month tab')
	KeywordUtil.markFailed('ERROR: The Yearly Sales figure amount DOES NOT match the YTD Sales Plan Total listed on YTD By Month tab')
}