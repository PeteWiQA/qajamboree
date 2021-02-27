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

/* This test reads the Sales and Sales Plan value from the Month To Date section of the Sales Dashboard
 * The value is compared to the Monthly value listed on the YTD Sales Detail page under the By Month tab.
 * For example, read the current Monthly Sales value and compare the value listed for March on the YTD Details Page
 * Created 03-28-2018 09:37AM by Pete Wilson
 * Updated 04-19-2018 11:40AM by Pete Wilson
 * Updated 05-01-2018 11:41AM by Pete Wilson
 * Updated 08-13-2018 09:43AM by Pete Wilson
 * Updated 11-14-2018 13:59PM by Pete Wilson
 */

/* To use the Manager/Sales Rep offset, we need to increment the div index by 3
 * A Sales Rep has an index of 5, Manager 6
 */

WebUI.navigateToUrl(GlobalVariable.baseurl + '/v3/sales_dashboard')

//Check to make sure the Sales Dashboard page loads. Look for the text, Daily Sales
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')

//Read Sales figure from Monthly section
monthlySalesFigure=WebUI.getText(findTestObject('ABC/Sales Dashboard/Monthly Sales Section/table-MTD Sales Detail Values', [('divIndex') : (GlobalVariable.dashboardAccordionValue+3), ('rowIndex') : 1])).replaceAll("[^0-9-]","").toInteger()
log.logWarning('Monthly Sales Figure listed on Sales Dashboard -- ' + monthlySalesFigure)

//Read Sales Plan from Monthly section
monthlySalesPlanFigure=WebUI.getText(findTestObject('ABC/Sales Dashboard/Monthly Sales Section/table-MTD Sales Detail Values', [('divIndex') : (GlobalVariable.dashboardAccordionValue+3), ('rowIndex') : 2])).replaceAll("[^0-9-]","").toInteger()
log.logWarning('Monthly Sales Plan Figure listed on Sales Dashboard -- ' + monthlySalesPlanFigure)

WebUI.click(findTestObject('ABC/Sales Dashboard/Yearly Sales Section/accordion-YTD Sales Accordion', [('divIndex') : (GlobalVariable.dashboardAccordionValue+4)]))

//Because of slow page load, wait for the accordion to appear with the text Year To date Sales Detail
WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/accordion-Year To Date Sales Detail Banner'),15)

//Check to make sure the Sales Dashboard page loads. Look for the text, Year To Date Sales Detail
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Year To Date Sales Detail')

today = new Date()
formattedDate = today.format("MM")
thisMonth=(Integer.valueOf(formattedDate))

//Read Sales figure from YTD By Customer tab
yearlySalesFigure=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Month Tab/table-Year to Date Totals', [('row') : thisMonth, ('column') : 2])).replaceAll("[^0-9]","").toInteger()
log.logWarning('Monthly Sales Figure listed in the Sales column of the YTD By Month tab -- ' + yearlySalesFigure)

//Read Sales Plan figure from YTD By Customer tab
yearlySalesPlanFigure=WebUI.getText(findTestObject('ABC/Sales Dashboard/Yearly Sales Details/By Month Tab/table-Year to Date Totals', [('row') : thisMonth, ('column') : 3])).replaceAll("[^0-9]","").toInteger()
log.logWarning('Monthly Sales Figure Plan listed in the Sales column of the YTD By Month tab -- ' + yearlySalesPlanFigure)

//Compare the Monthly Sales value to the Monthly Sales value on the YTD accordion
if (monthlySalesFigure==yearlySalesFigure){
	log.logWarning('SUCCESS: The Monthly Sales figure amount matches the Monthly amount listed on YTD By Month tab')
} else {
	log.logError('ERROR: The Monthly Sales figure amount DOES NOT match the Monthly amount listed on YTD By Month tab')
	KeywordUtil.markFailed('The Monthly Sales figure amount DOES NOT match the Monthly amount listed on YTD By Month tab')
}

//Compare the Monthly Sales Plan value to the Monthly Sales Plan value on the YTD accordion
if (monthlySalesPlanFigure==yearlySalesPlanFigure){
	log.logWarning('SUCCESS: The Monthly Sales Plan figure amount matches the Monthly amount listed on YTD By Month tab')
} else {
	log.logError('ERROR: The Monthly Sales Plan figure amount DOES NOT match the Monthly amount listed on YTD By Month tab')
	KeywordUtil.markFailed('The Monthly Sales Plan figure amount DOES NOT match the Monthly amount listed on YTD By Month tab')
}