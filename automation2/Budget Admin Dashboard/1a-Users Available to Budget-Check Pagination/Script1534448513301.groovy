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
import org.openqa.selenium.WebDriver as WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import org.openqa.selenium.By as By
import org.openqa.selenium.WebElement as WebElement
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger
import com.kms.katalon.core.util.KeywordUtil

KeywordLogger log = new KeywordLogger()

/* On the Main Budget Admin Dashboard, confirm the pagination buttons at the bottom of the page are functional.
 * Read the first name in the list.
 * Jump to page 5, read the first name off the list
 * Jump back to page 1, and read the name again
 * Confirm the first two name are not the same, which should mean we switched pages
 * Confirm the first name, read twice, is the same
 * The first test compares the name Page 1 and 5, they should be different
 * The second test compares the name on Page 5 back to Page 1, they should be different
 * The third test compares the name from Page 1 and that same name again after using pagination, they should be the same
 *
 * Created 05-04-2018 09:29AM by Pete Wilson
 * Updated 08-16-2018 16:08PM by Pete Wilson
 *
 */

xpath="//*[@id='root']/div/div/div[2]/div/div/div[4]/ul/li"

WebUI.navigateToUrl(GlobalVariable.baseurl + '/admin/budgets')

//Check to make sure the Budget Admin Dashboard page loads. Look for the text, Sales Planning Process for 2019
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Sales Planning Process for ' + GlobalVariable.budgetYear)

/* To test the pagination of another filter, enter is here.
WebUI.click(findTestObject('ABC/Budget Admin Dashboard/Filter-No Budget-Data Not Present'))
*/

WebUI.delay(2)

int lastPageOfPagination=CustomKeywords.'tools.commonCode.countRowsPerPage'(xpath)

/*
 * If there aren't enough results, Page 1 doesn't appear and the start of the pagination index will be one lower.
 * The text, 1-x of x users is position 1
 * The < arrow to go to page 1 is position 2
 * Page 1 starts at position 3
 */
int startOfPagination
if (lastPageOfPagination<=4){
	startOfPagination=2
} else {
	startOfPagination=3
}

//Read the first Sales Rep Name
salesRepName1 = WebUI.getText(findTestObject('ABC/Budget Admin Dashboard/card-Sales Rep Name'))

//Click the last page on the ribbon
lastPage=WebUI.getText(findTestObject('ABC/Budget Admin Dashboard/link-Pagination Ribbon', [('Variable') : (lastPageOfPagination-1)]))

log.logWarning('There are ' + lastPage + ' pages worth of results. Clicking page ' + lastPage)

WebUI.click(findTestObject('ABC/Budget Admin Dashboard/link-Pagination Ribbon', [('Variable') : (lastPageOfPagination-1)]))
WebUI.delay(2)

//Read the first Sale Rep Name for the new page
salesRepName2 = WebUI.getText(findTestObject('ABC/Budget Admin Dashboard/card-Sales Rep Name'))

//Return to page 1 of the list and read the Sales Rep Name
WebUI.click(findTestObject('ABC/Budget Admin Dashboard/link-Pagination Ribbon', [('Variable') : startOfPagination]))
WebUI.delay(2)

salesRepName3 = WebUI.getText(findTestObject('ABC/Budget Admin Dashboard/card-Sales Rep Name'))

log.logWarning('-The Sales Rep Name from Page 1 is:=' + salesRepName1)

log.logWarning('-The Sales Rep Name from the Last Page is:=' + salesRepName2)

log.logWarning('-The Sales Rep Name reread from Page 1 is:=' + salesRepName3)

if(salesRepName1!=salesRepName2){
	log.logWarning('SUCCESS: The Sales Rep Name on Page 1 differs from the Sales Rep Name on the Last Page. Pagination Success')
} else {
	log.logError('ERROR: The Sales Rep Name on Page 1 is the same as the Sales Rep Name on the Last Page.')
	KeywordUtil.markFailed('ERROR: The Sales Rep Name on Page 1 is the same as the Sales Rep Name on the Last Page.')
}

if(salesRepName2!=salesRepName3){
	log.logWarning('SUCCESS: The Sales Rep Name on the Last Page differs from the Sales Rep Name on Page 1 after switching pages. Pagination Success.')
} else {
	log.logError('ERROR: The Sales Rep Name on the Last Page is the same as the Sales Rep Name on Page 1 after reselecting the Pagination tab.')
	KeywordUtil.markFailed('ERROR: The Sales Rep Name on the Last Page is the same as the Sales Rep Name on Page 1 after reselecting the Pagination tab.')
}

if(salesRepName1==salesRepName3){
	log.logWarning('SUCCESS: The Sales Rep Name on the First Page matches the name read previously. Pagination Success.')
} else {
	log.logError('ERROR: A new Sales Rep Name was found on the Page 1 after selecting the start of the Pagination tab.')
	KeywordUtil.markFailed('ERROR: A new Sales Rep Name was found on the Page 1 after selecting the the start of Pagination tab.')
}
