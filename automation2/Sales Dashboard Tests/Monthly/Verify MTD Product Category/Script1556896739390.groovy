import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger
import com.kms.katalon.core.util.KeywordUtil

/* The purpose of this test is to validate the Product Categories listed
 * on the Category Sales modal on the Customer By Product page
 * reflect the correct list. ABC and TCI users should see a different
 * list and the list should not contain both sets of categories.
 * 
 * Created 05-03-2019 13:09PM by Pete Wilson 
 * 
 */
KeywordLogger log = new KeywordLogger()

xpath="//div[@id='mtd_customer_category_details']/div/div/div/div[2]/div"

//Click the link for a customer and confirm Product Category
WebUI.click(findTestObject('ABC/Sales Dashboard/Common Objects/table-By Customer Total Values for Daily-MTD-YTD', [('row') : 1, ('column') : 1]))

WebUI.delay(3)

WebUI.waitForElementVisible(findTestObject('ABC/Sales Dashboard/Monthly Sales Details/By Product Category/Product Category Modal Header'),20)

println(WebUI.getText(findTestObject('ABC/Sales Dashboard/Monthly Sales Details/By Product Category/table-MTD Customer Category Details', 
            [('row') : 3, ('column') : 1])))

int numberOfCategories=CustomKeywords.'tools.commonCode.countRowsPerPage'(xpath)
log.logWarning("Number of Product Categories: " + Integer.valueOf(numberOfCategories-2))
if (numberOfCategories>12){
	log.logError("ERROR: The number of Product Categories listed is larger than expected")
	KeywordUtil.markFailed('ERROR: The number of Product Categories listed is larger than expected')
}

CustomKeywords.'tools.commonCode.verifyProductCategory'(GlobalVariable.userDivision, 'ABC/Sales Dashboard/Monthly Sales Details/By Product Category/table-MTD Customer Category Details')

WebUI.click(findTestObject('ABC/Sales Dashboard/Monthly Sales Details/By Product Category/btn-Close')) 