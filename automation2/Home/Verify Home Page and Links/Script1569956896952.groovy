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

/* The purpose of this test is to check the Home page to verify it loads
 * and confirm the links for Sales Dashboard and View/Edit Tasks
 * are working and load the pages correctly. This consolidates
 * several tests into one since it's all part of the same page.
 * 
 * Updated 10-01-2019 16:02PM by Pete Wilson
 * 
 */

/* The test cases have been created as Methods since they are small
 * and don't follow in a linear pattern
 */

verifyHomePage()
dashboardLinkIsClickable()
dailySalesLinkIsClickable()
editTaskLinkIsClickable()

def verifyHomePage(){
	//Confirm that the Home page loads and displays correctly
	KeywordLogger log = new KeywordLogger()
	boolean elementPresent

	WebUI.navigateToUrl(GlobalVariable.baseurl)
	log.logWarning("--- Checking Home page ---")

	elementPresent=CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Upcoming Tasks')
	if (elementPresent==true){
		log.logWarning('SUCCESS: Upcoming Tasks is visible and loaded')
	} else {
		KeywordUtil.markFailed('ERROR: Upcoming Tasks was not visible and loaded')
	}

	elementPresent=CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Daily Sales')
	if (elementPresent==true){
		log.logWarning('SUCCESS: Daily Sales is visible and loaded')
	} else {
		KeywordUtil.markFailed('ERROR: Daily Sales was not visible and loaded')
	}

	elementPresent=CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('How to: Add this app to the home screen on your device')
	if (elementPresent==true){
		log.logWarning('SUCCESS: Add this app to the home screen on your device is visible and loaded')
	} else {
		KeywordUtil.markFailed('ERROR: Add this app to the home screen on your device was not visible and loaded')
	}
}

def dashboardLinkIsClickable(){
	//Check validity of the Dashboard link on the Home page. This needs to be a clickable link.
	KeywordLogger log = new KeywordLogger()
	boolean isClickable, elementPresent
	
	WebUI.navigateToUrl(GlobalVariable.baseurl)
	log.logWarning("--- Checking Sales Dashboard link ---")
	
	WebUI.waitForElementVisible(findTestObject('ABC/Home/link-To Sales Dashboard'), 15)
	isClickable=WebUI.verifyElementClickable(findTestObject('ABC/Home/link-To Sales Dashboard'))
	if (isClickable==true){
		//On the Home Page, click the link for "Dashboard" listed in the Daily Sales Header.
		WebUI.click(findTestObject('ABC/Home/link-To Sales Dashboard'))
		elementPresent=CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Dashboard')
	
		//Wait for the word, Dashboard to appear, indicating the Sales Dashboard has been loaded
		if (elementPresent==true){
			log.logWarning('SUCCESS: The Home Page Dashboard link correctly loads the Sales Dashboard')
		} else {
			log.logError('ERROR: The Home Page Dashboard link was clickable, but the Sales Dashboard did not load')
			KeywordUtil.markFailed('ERROR: The Home Page Dashboard link was clickable, but the Sales Dashboard did not load')
		}
	} else {
		log.logError('ERROR: The Sales Dashboard is not clickable and should be checked')
		KeywordUtil.markFailed('ERROR: The Sales Dashboard is not clickable and should be checked')
	}
}

def dailySalesLinkIsClickable(){
	/* verify that the Daily Sales figure on the Home page
	 * matches the dollar figure listed on the Sales Dashboard
	 */
	boolean isClickable
	int dailyHomeSalesFigure, dailySalesFigure
	KeywordLogger log = new KeywordLogger()
	
	WebUI.navigateToUrl(GlobalVariable.baseurl)
	log.logWarning("--- Checking link for Daily Sales Figure ---")
	
	//Get Daily Sales dollar figure from the home page
	WebUI.waitForElementVisible(findTestObject('ABC/Home/text-Daily Sales Figure'), 15)
	dailyHomeSalesFigure = WebUI.getText(findTestObject('ABC/Home/text-Daily Sales Figure')).replaceAll("[^0-9-]","").toInteger()
	
	log.logWarning('The current Daily Sales Figure on the Home Page is: ' + dailyHomeSalesFigure)
	
	//Verify the Daily Sales figure is part of a clickable link
	WebUI.waitForElementClickable(findTestObject('ABC/Home/link-Daily Sales Figure'), 15)
	isClickable=WebUI.verifyElementClickable(findTestObject('ABC/Home/link-Daily Sales Figure'))
	if (isClickable==true){
	
		//Open Daily Sales Detail accordion
		WebUI.click(findTestObject('ABC/Home/link-Daily Sales Figure'))
	
		//Get the Daily Sales figure
		dailySalesFigure = WebUI.getText(findTestObject('ABC/Sales Dashboard/Common Objects/table-Sales Detail Values for Daily-MTD-YTD',[('divIndex') : 3, ('rowIndex') : 1])).replaceAll("[^0-9-]","").toInteger()
	
		log.logWarning('The current Daily figure from the Sales Dashboard ' + dailySalesFigure)
	
		//Compare the home page value to the dashboard figure
	
		if (dailyHomeSalesFigure==dailySalesFigure){
			log.logWarning('SUCCESS: The Daily Sales figure on the Home Page and Dashboard match')
		} else {
			KeywordUtil.markFailed('ERROR: The Daily Sales figure on the Home Page and Dashboard DO NOT match. Manual investigation is needed')
		}
	} else {
		log.logError('ERROR: The Daily Sales Dashboard Figure is not clickable and should be checked')
		KeywordUtil.markFailed('ERROR: The Daily Sales Dashboard Figure is not clickable and should be checked')
	}
}

def editTaskLinkIsClickable(){
	//Check validity of the View/Edit Tasks link on the Home page. This needs to be a clickable link.
	boolean isClickable, elementPresent
	KeywordLogger log = new KeywordLogger()
	
	WebUI.navigateToUrl(GlobalVariable.baseurl)
	log.logWarning("--- Checking links for View/Edit Tasks ---")
	
	WebUI.waitForElementVisible(findTestObject('ABC/Home/link-View-Edit Tasks'), 15)
	isClickable=WebUI.verifyElementClickable(findTestObject('ABC/Home/link-View-Edit Tasks'))
	if (isClickable==true){
		//On the Home Page, click the link for "View/Edit Tasks" to confirm it loads
		WebUI.click(findTestObject('ABC/Home/link-View-Edit Tasks'))
		elementPresent=CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('My Open Tasks')
	
		//Wait for the words, My Open Tasks to appear, indicating the Sales Dashboard has been loaded
		if (elementPresent==true){
			log.logWarning('SUCCESS: The View/Edit Tasks link correctly loads the Task page')
		} else {
			log.logError('ERROR: The View/Edit Tasks link was clickable, but the Task page did not load')
			KeywordUtil.markFailed('ERROR: The View/Edit Tasks link was clickable, but the Task page did not load')
		}
	} else {
		log.logError('ERROR: The View/Edit Tasks link is not clickable and should be checked')
		KeywordUtil.markFailed('ERROR: The View/Edit Tasks link is not clickable and should be checked')
	}
}