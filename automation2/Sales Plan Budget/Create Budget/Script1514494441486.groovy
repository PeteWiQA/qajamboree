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
import com.kms.katalon.core.annotation.Keyword as Keyword
import org.openqa.selenium.Keys as Keys
import org.openqa.selenium.interactions.Actions
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger
import com.kms.katalon.core.util.KeywordUtil

KeywordLogger log = new KeywordLogger()

/* This code will work for a budget period that is open
 * If the budget is closed or displays the read only version, this code will fail
 * The test-"Determine Sales Plan Status", handles evaluating the status
 * Updated 02-01-2018 13:17PM by Pete Wilson
 * Updated 02-28-2018 13:44PM by Pete Wilson
 * Updated 04-12-2018 09:41AM by Pete Wilson
 * Updated 04-25-2018 16:47PM by Pete Wilson
 * Updated 08-16-2018 10:33AM by Pete Wilson
 * Updated 05-15-2019 13:31PM by Pete Wilson
 */

//Set up values for the Product Category list
int numberOfRows=0, tableFooter=0
//Determine the number of rows in the table based on the users division
if(GlobalVariable.userDivision.toLowerCase()=="abc"){
	numberOfRows=10 //number of rows in the table
} else {
	numberOfRows=9 //number of rows in the table
}

//Set up random numbers used for the forms
Random rnd = new Random()

WebUI.navigateToUrl(GlobalVariable.baseurl + '/budgets?step=0')

//Read the Header for the page. It should say Sales Planning Process for 2018
log.logWarning('--- Submit Budget - Instructions and History - Step 1 ---')

salesPlanningHeader=WebUI.getText(findTestObject('ABC/Open Sales Plan Budget/text-Sales Planning Process Header'))

if (salesPlanningHeader!='Sales Planning Process for ' + GlobalVariable.budgetYear) {
	log.logError('ERROR: The Planning Process is for the wrong year')
}

//Verify the Product Categories match the user division
CustomKeywords.'tools.commonCode.verifyProductCategory'(GlobalVariable.userDivision, 'ABC/Open Sales Plan Budget/table-Previous 12 Months Summary')

budgetDueDate=WebUI.getText(findTestObject('ABC/Open Sales Plan Budget/text-Budget Due Date'))

if(budgetDueDate.contains('Plans are Due')!=true){
	log.logError('ERROR: There appears to be an error with the Due Date')
}

//Read the Current Plan Value at the start of the budget
currentPlanStartValue = WebUI.getText(findTestObject('ABC/Open Sales Plan Budget/Review Plan/text-Header-Current Plan Value'))

//---- Step 2 Existing Customers ----
WebUI.waitForElementVisible(findTestObject('ABC/Open Sales Plan Budget/Step 1 Next Button'),10)
WebUI.click(findTestObject('ABC/Open Sales Plan Budget/Step 1 Next Button'))

log.logWarning('--- Submit Budget - Existing Customers - Step 2 ---')

WebUI.waitForElementVisible(findTestObject('ABC/Open Sales Plan Budget/text-Step 2 - Set Plan for Existing Customers Header'), 10)

//For the Existing Customer, read the first row of data
//More work will need to be added to this section
//Figures will need to be added for both Percent and Dollar
//It should also check the calculation of how much the value increased
//That may not be needed for a regression test, but can be added
for (loop = 1; loop <= 3; loop++) {
	log.logWarning('Details for Customer #' + loop)

	customerName=WebUI.getText(findTestObject('ABC/Open Sales Plan Budget/Existing Customers/text-Top90 Customer Name', [('Variable') : loop]))

	log.logWarning('Company Name:=' + customerName)

	salesPrev12Months=WebUI.getText(findTestObject('ABC/Open Sales Plan Budget/Existing Customers/text-Sales Previous 12 Months',
			[('Variable') : loop]))

	log.logWarning('Previous 12 Months Sales:=' + salesPrev12Months)

	//getAttribute is used to read the text of an input box when it is “greyed” out. The ‘value’ of the field is read as a String element
	salesChangePercent=WebUI.getAttribute(findTestObject('ABC/Open Sales Plan Budget/Existing Customers/input-Sales Change Percent',
			[('Variable') : loop]), 'value')

	log.logWarning('Sales Change Percent: ' + salesChangePercent)

	salesPlan=WebUI.getAttribute(findTestObject('ABC/Open Sales Plan Budget/Existing Customers/input-Sales Plan Value', [('Variable') : loop]),
	'value')

	log.logWarning('Sales Plan:=' + salesPlan)

	gpPrev12Months=WebUI.getText(findTestObject('ABC/Open Sales Plan Budget/Existing Customers/text-Gross Profit Previous 12 Months',
			[('Variable') : loop]))

	log.logWarning('Gross Profit Previous 12 Months % :=' + gpPrev12Months)

	gpPlan=WebUI.getText(findTestObject('ABC/Open Sales Plan Budget/Existing Customers/text-Gross Profit Plan', [('Variable') : loop]))

	log.logWarning('Gross Profit Plan :=' + gpPlan)
}

//Use the + button to increase the percent by 3
for (loop = 1; loop <=3; loop++) {
	WebUI.click(findTestObject('ABC/Open Sales Plan Budget/Existing Customers/btn-Change Plus Button',[('Variable') : 1]))
	boolean elementPresent=CustomKeywords.'tools.commonCode.verifyObjectPresent'('ABC/Open Sales Plan Budget/Existing Customers/modal-Percentage Change Confirmation')
	//elementPresent=WebUI.waitForElementVisible(findTestObject('ABC/Open Sales Plan Budget/Existing Customers/modal-Percentage Change Confirmation'),2)

	if (elementPresent==true) {
		WebUI.click(findTestObject('ABC/Open Sales Plan Budget/Existing Customers/btn-Yes Proceed'))
		WebUI.delay(1)
	}
}

WebUI.waitForElementVisible(findTestObject('ABC/Open Sales Plan Budget/Existing Customers/btn-Save'),10)
WebUI.click(findTestObject('ABC/Open Sales Plan Budget/Existing Customers/btn-Save'))
WebUI.waitForElementNotPresent(findTestObject('ABC/Open Sales Plan Budget/Existing Customers/modal-Saving Budget'), 90)
log.logWarning('Increased the Change Percent on Step 2 by 3')

//To confirm the save works, read the third customer name off the page again and confirm it matches what was just read
//We had an error where the page didn't load and gave a 500 error. This should verify the page loads correctly
customerNameAfterSave = WebUI.getText(findTestObject('ABC/Open Sales Plan Budget/Existing Customers/text-Top90 Customer Name', [('Variable') : 3]))

if (customerNameAfterSave!=customerName) {
	log.logError('ERROR: An error has occurred after saving the Change Percent to the Existing Customer. Please confirm manually.')
}

log.logWarning('Customer Name:= ' + customerName + ' Customer Name After Save:= ' + customerNameAfterSave)

WebUI.waitForElementVisible(findTestObject('ABC/Open Sales Plan Budget/btn-Next to Proceed'),10)
WebUI.click(findTestObject('ABC/Open Sales Plan Budget/btn-Next to Proceed'))
WebUI.waitForElementNotPresent(findTestObject('ABC/Open Sales Plan Budget/Existing Customers/modal-Saving Budget'), 90)


//---- Step 3 Add Prospect ----
//Create a new Prospect and fill out each field on the New Prospect Form

log.logWarning('--- Submit Budget - Add Prospect - Step 3 ---')

//The date is added to the company name to make it slightly random and to show when it was made
String formattedDate=CustomKeywords.'tools.commonCode.getDateAndTime'()

//Fill in the fields for Company, Contact, City, State, Zip, etc.
WebUI.waitForElementVisible(findTestObject('ABC/Open Sales Plan Budget/New Prospect/btn-Add New Prospect'),20)
WebUI.click(findTestObject('ABC/Open Sales Plan Budget/New Prospect/btn-Add New Prospect'))

WebUI.waitForElementVisible(findTestObject('ABC/Open Sales Plan Budget/New Prospect/input-Prospect-Company Name'),20)
WebUI.setText(findTestObject('ABC/Open Sales Plan Budget/New Prospect/input-Prospect-Company Name'), 'Allied Bedrock Company - ' + formattedDate)

WebUI.setText(findTestObject('ABC/Open Sales Plan Budget/New Prospect/input-Prospect-Contact Name'), 'Technekes Employee')

WebUI.setText(findTestObject('ABC/Open Sales Plan Budget/New Prospect/input-Prospect-Role or Title'), 'Sales Rep Role or Title')

WebUI.setText(findTestObject('ABC/Open Sales Plan Budget/New Prospect/input-Prospect-Additional Information'), 'Additional Information for QA Prospect')

WebUI.setText(findTestObject('ABC/Open Sales Plan Budget/New Prospect/input-Prospect-Contact Phone'), '9802221111')

WebUI.setText(findTestObject('ABC/Open Sales Plan Budget/New Prospect/input-Prospect-Contact Mobile'), '9803331112')

WebUI.setText(findTestObject('ABC/Open Sales Plan Budget/New Prospect/input-Prospect-Contact Office'), '9803331113')

WebUI.setText(findTestObject('ABC/Open Sales Plan Budget/New Prospect/input-Prospect-Contact Email'), 'peter.wilson.contact@technekes.com')

WebUI.setText(findTestObject('ABC/Open Sales Plan Budget/New Prospect/input-Prospect-Address1'), '1313 Mockingbird Ln')

WebUI.setText(findTestObject('ABC/Open Sales Plan Budget/New Prospect/input-Prospect-City'), 'Charlotte')

WebUI.setText(findTestObject('ABC/Open Sales Plan Budget/New Prospect/input-Prospect-State'), 'NC')

WebUI.setText(findTestObject('ABC/Open Sales Plan Budget/New Prospect/input-Prospect-ZipCode'), '28203')

WebUI.setText(findTestObject('ABC/Open Sales Plan Budget/New Prospect/input-Prospect-Phone Number'), '9805551212')

WebUI.setText(findTestObject('ABC/Open Sales Plan Budget/New Prospect/input-Prospect-Email Address'), 'testuser@technekes.com')

//Loop through the Sales and Margin columns
//Enter a sales figure from 10000-250000 dollars
//Enter a margin from 1-15%

WebDriver driverMargin = DriverFactory.getWebDriver()
Actions actMargin = new Actions(driverMargin)

for (loop = 1; loop <= numberOfRows; loop++) {
	randomNumber = (1000000 + rnd.nextInt(25000000))

	WebUI.setText(findTestObject('ABC/Open Sales Plan Budget/New Prospect/input-Category Sales Column', [('Variable') : loop]), String.valueOf(
	randomNumber))

	randomNumber = (1 + rnd.nextInt(15))

	WebElement elem = driverMargin.findElement(By.xpath("//div[@id='prospect-modal-new']/div/div/div[2]/div/div/div/table/tbody/tr[" + (loop) +"]/td[3]/div/div[2]/input"))
	actMargin.sendKeys(elem, (Keys.chord(Keys.CONTROL, "a", Keys.DELETE)))
	actMargin.sendKeys(elem, (Keys.chord(String.valueOf(randomNumber))))
	actMargin.perform()
}

WebUI.setText(findTestObject('ABC/Open Sales Plan Budget/New Prospect/input-Prospect-Notes Field'), 'New Test Note from QA created on - ' + formattedDate)

//Save the changes for the new prospect
WebUI.click(findTestObject('ABC/Open Sales Plan Budget/New Prospect/btn-Save Changes'))

WebUI.waitForElementNotPresent(findTestObject('ABC/Open Sales Plan Budget/New Prospect/modal-Add Budget Prospect'), 90)

WebUI.refresh()

//Save the changes for Step 3 of the budget
WebUI.waitForElementVisible(findTestObject('ABC/Open Sales Plan Budget/Existing Customers/btn-Save'), 10)
WebUI.waitForElementClickable(findTestObject('ABC/Open Sales Plan Budget/Existing Customers/btn-Save'), 10)
WebUI.click(findTestObject('ABC/Open Sales Plan Budget/Existing Customers/btn-Save'))

WebUI.waitForElementVisible(findTestObject('ABC/Open Sales Plan Budget/btn-Next to Proceed'),10)
WebUI.waitForElementClickable(findTestObject('ABC/Open Sales Plan Budget/btn-Next to Proceed'), 10)
WebUI.click(findTestObject('ABC/Open Sales Plan Budget/btn-Next to Proceed'))

//---- Step 4 Seasonality ----

log.logWarning('--- Submit Budget - Seasonality - Step 4 ---')

/* Determine where to start in the table based on the current month
 * This is necessary for onboarded users. If a user starts in March, read Mar-Dec
 */
//Get the first month listed on the table
WebUI.waitForElementVisible(findTestObject('ABC/Open Sales Plan Budget/Seasonality/text-Seasonality Month Name', [('Variable') : 1]), 10)
monthText = WebUI.getText(findTestObject('ABC/Open Sales Plan Budget/Seasonality/text-Seasonality Month Name', [('Variable') : 1]))

//Convert the name of the month to an integer, ie-April=4
parsedDate = Date.parse('MMM', monthText)

//Turn the month into an Integer
numberOfMonth = ((parsedDate.format('MM')) as int)

//Determine how many months are available. The current month is added back in since it will be visible
numberOfMonth = ((12 - numberOfMonth) + 1)

//If the number of months in the table isn't even, subtract 1 so the budget won't be out of balance.
if ((numberOfMonth % 2) != 0) {
	numberOfMonth = (numberOfMonth - 1)
}

//Add a random Seasonality value, then add the negative of it. Values must be added in pairs.
log.logWarning('Number of Months:= ' + numberOfMonth)

WebDriver driver = DriverFactory.getWebDriver()
Actions act = new Actions(driver)

randomNumber = (1000000 + rnd.nextInt(25000000))
for (loop = 1; loop <= numberOfMonth; loop += 2) {
	WebUI.clearText(findTestObject('ABC/Open Sales Plan Budget/Seasonality/input-Seasonality Change', [('Variable') : loop]))
	WebUI.setText(findTestObject('ABC/Open Sales Plan Budget/Seasonality/input-Seasonality Change', [('Variable') : loop]),String.valueOf(randomNumber))
	WebElement elem = driver.findElement(By.xpath("//div[@id='adjustSeasonality']/form/table/tbody/tr[" + (loop+1) +"]/td[4]/div/div[2]/input"))
	WebUI.clearText(findTestObject('ABC/Open Sales Plan Budget/Seasonality/input-Seasonality Change', [('Variable') : loop+1]))
	act.sendKeys(elem, (Keys.chord(Keys.CONTROL, "a")))
	act.sendKeys(elem, (Keys.chord(String.valueOf(randomNumber*-1))))
	act.perform()
}

WebUI.click(findTestObject('ABC/Open Sales Plan Budget/Existing Customers/btn-Save'))
WebUI.waitForElementVisible(findTestObject('ABC/Open Sales Plan Budget/btn-Next to Proceed'),10)
WebUI.waitForElementClickable(findTestObject('ABC/Open Sales Plan Budget/btn-Next to Proceed'), 10)
WebUI.click(findTestObject('ABC/Open Sales Plan Budget/btn-Next to Proceed'))

//---- Step 5 Review and Submit ----
log.logWarning('--- Submit Budget - Submit to Manager - Step 5 ---')
WebUI.waitForElementVisible(findTestObject('ABC/Open Sales Plan Budget/Review Plan/text-Header-Current Plan Value'), 10)

//Verify the Product Categories match the user division
CustomKeywords.'tools.commonCode.verifyProductCategory'(GlobalVariable.userDivision, 'ABC/Open Sales Plan Budget/Review Plan/table-Review Plan')

//Read the Current Plan from the Header of the page
currentPlanValue = WebUI.getText(findTestObject('ABC/Open Sales Plan Budget/Review Plan/text-Header-Current Plan Value'))

//Read the Totoal Sale Plan value from the Footer of the page. These values should match
//The Footer is in a different row based on the number of Product Categories
if(GlobalVariable.userDivision.toLowerCase()=="abc"){
	tableFooter=13 //number of rows in the table
} else {
	tableFooter=12 //number of rows in the table
}

totalSalesPlan = WebUI.getText(findTestObject('ABC/Open Sales Plan Budget/Review Plan/text-Footer-Total Sales Plan',[('row') : tableFooter]))

log.logWarning('The Original Current Plan for the budget is: ' + currentPlanStartValue)

log.logWarning('The Current Plan for the Budget is: ' + currentPlanValue)

log.logWarning('The Total Sales Plan for the Budget is: ' + totalSalesPlan)

if (currentPlanValue != totalSalesPlan) {
	log.logError('ERROR: The Current Plan and Current Sales Plan values DO NOT match. There is an error with the budget.')
} else {
	log.logWarning('SUCCESS: The Current Plan and Current Sales Plan values match. The budget will be submitted.')
}

currentPlanValue=currentPlanValue.replaceAll("[^0-9-]","").toLong()

currentPlanStartValue=currentPlanStartValue.replaceAll("[^0-9-]","").toLong()

def formatter = java.text.NumberFormat.currencyInstance

log.logWarning('The difference between the start and submitted Budget Plan is ' + (formatter.format(currentPlanValue - currentPlanStartValue)))

if (currentPlanValue - currentPlanStartValue<1000000){
	log.logError("ERROR: The difference is much lower than expected. Confirm a New Prospect was created on Step 3")
	KeywordUtil.markError('ERROR: The difference is much lower than expected. Confirm a New Prospect was created on Step 3 ')
}

//Submit the budget
WebUI.click(findTestObject('ABC/Open Sales Plan Budget/Review Plan/btn-Step 5-Submit to Manager'))

log.logWarning('--- The Budget has been Submitted ---')
