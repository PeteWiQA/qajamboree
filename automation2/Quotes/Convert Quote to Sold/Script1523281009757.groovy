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
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import org.openqa.selenium.Keys as Keys

KeywordLogger log = new KeywordLogger()

/* This script will take a quote for a Sales Rep and set it as Sold
 * It takes the first quote on the page and sets it to Sold Quote using the Action dropdown
 * The first page, Quote Sold, should be filled out, so it clicks the Delivery Details button without making changes
 * On the Confirm Delivery Information screen, the rest of the fields are filled in for Directions, Delivery Date, Time, Obstructions, etc.
 * The email address is set to a Technekes email and the quote is converted
 * NOTE: This test should be run AFTER the Create Quote script
 * Created 04-09-2018 09:40 AM by Pete Wilson
 * Updated 08-15-2018 12:40PM by Pete Wilson
 * Updated 03-01-2019 09:26AM by Pete Wilson
 * Updated 07-22-2019 12:15PM by Pete Wilson
 * Updated 11-04-2019 14:20PM by Pete Wilson
 */

int counter=0
boolean elementPresent
//Email the quote or use the Submit to Branch option?
//User must be part of the Pilot Branch for this option to work (Dax Robinson, Branch 89)
boolean submitToBranch=false
//Output the execution time and confirm status changes?
boolean timedTest=true
String firstQuoteName, soldQuoteName

//Go to the Quotes section and count the number of Quotes assigned to the Sales Rep
WebUI.navigateToUrl(GlobalVariable.baseurl + '/quotes')
if (timedTest==true){
	soldQuoteName=WebUI.getText(findTestObject('ABC/Quotes/Convert Quote to Sold/accordion-Quote Name'))
	log.logWarning("--- Name of the first quote listed ---" + soldQuoteName)
	if (soldQuoteName!=GlobalVariable.quoteName){
		log.logError("ERROR: The first quote listed is not the newly created quote ---")
		KeywordUtil.markFailed("ERROR: The first quote listed is not the newly created quote --- " + soldQuoteName)
	} else {
		log.logWarning("SUCCESS: First quote listed is the one created in previous step")
	}
	String quoteStatusActions = WebUI.getText(findTestObject('ABC/Quotes/Convert Quote to Sold/select-Quote Status Action')).replaceAll("\\r?\\n",":")
	log.logWarning("--- Quote Status Action Options:" + quoteStatusActions)
	if (quoteStatusActions.contains("Sold Quote")!=true){
		log.logError("ERROR: Quote is not set to sold on page load")
		KeywordUtil.markFailed("ERROR: Quote is not set to sold on page load --- " + soldQuoteName)
	} else {
		log.logWarning("SUCCESS: Quote is able to be sold --- " + soldQuoteName)
	}
}
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('Quotes')

//Select the filter to Open Orders. This should be the default of the page, but this is to make sure or else the test will fail
WebUI.click(findTestObject('ABC/Quotes/btn-Filter-Open Quotes'))
counter=0

elementPresent=CustomKeywords.'tools.commonCode.verifyObjectPresent'("ABC/Quotes/text-Results Found")

if (elementPresent == false) {
	KeywordUtil.markFailedAndStop('ERROR: There was an error loading the page. Exiting test.')
}

int quoteResults=WebUI.getText(findTestObject('ABC/Quotes/text-Results Found')).replaceAll("[^0-9]","").toInteger()
//If there are 0 Quotes, we need to exit the test as there will be nothing to read and something is wrong.
while (quoteResults==0){
	if (counter>=10) {
		KeywordUtil.markFailedAndStop('ERROR: There are no Quotes for this Sales Rep. Exiting test.')
	}
	WebUI.delay(1)
	quoteResults=WebUI.getText(findTestObject('ABC/Quotes/text-Results Found')).replaceAll("[^0-9]","").toInteger()
	counter++
}

log.logWarning('The number of quotes listed is:=' + quoteResults)

//--------------//

//Search for the newly created quote
WebUI.setText(findTestObject('ABC/Quotes/input-Search Quote Name'), GlobalVariable.quoteName)
WebUI.click(findTestObject('ABC/Quotes/btn-Go Quote Search'))
WebUI.click(findTestObject('ABC/Quotes/btn-Filter-All Quotes'))
WebUI.delay(2)

//Confirm the newly created quote exists in Elastic Search
quoteResults=WebUI.getText(findTestObject('ABC/Quotes/text-Results Found')).replaceAll("[^0-9]","").toInteger()
counter=0
while(quoteResults==0) {
	//Check to see how many times we have searched for the quote
	if (counter>=10){
		log.logError("ERROR: A new Quote was created but does not appear in the search results. Exiting Test.")
		log.logError("ERROR: There could be an error with Elastic Search")
		KeywordUtil.markFailedAndStop('ERROR: There could be an error with Elastic Search')
		break;
	}
	//If the quote wasn't found, search for it again to update elastic search
	WebUI.setText(findTestObject('ABC/Quotes/input-Search Quote Name'), GlobalVariable.quoteName)
	WebUI.click(findTestObject('ABC/Quotes/btn-Go Quote Search'))
	WebUI.click(findTestObject('ABC/Quotes/btn-Filter-All Quotes'))
	WebUI.delay(1)
	quoteResults=WebUI.getText(findTestObject('ABC/Quotes/text-Results Found')).replaceAll("[^0-9]","").toInteger()
	counter++
}

//Confirm the quote has the option for Sold
quoteStatusActions = WebUI.getText(findTestObject('ABC/Quotes/Convert Quote to Sold/select-Quote Status Action'))
log.logWarning("--- Quote Status Action Options:" + quoteStatusActions)
counter=0
while (quoteStatusActions.contains("Sold Quote")!=true){
	log.logError("Waiting on Status Change: " + counter)
	//Check to see how many times we have searched for the quote.
	if (counter>=10){
		log.logError("ERROR: A new Quote was created but does not have the correct status. Exiting Test.")
		log.logError("ERROR: There could be an error with Elastic Search")
		KeywordUtil.markFailedAndStop('ERROR: There could be an error with Elastic Search')
	}
	
	//If the quote status is not set to Sold, search for the quote again to update elastic search
	WebUI.setText(findTestObject('ABC/Quotes/input-Search Quote Name'), GlobalVariable.quoteName)
	WebUI.click(findTestObject('ABC/Quotes/btn-Go Quote Search'))
	WebUI.click(findTestObject('ABC/Quotes/btn-Filter-All Quotes'))
	WebUI.delay(1)
	quoteStatusActions = WebUI.getText(findTestObject('ABC/Quotes/Convert Quote to Sold/select-Quote Status Action'))
	counter++
}

//Select the first quote in the list. It's actual position on the page is 2
WebUI.selectOptionByLabel(findTestObject('ABC/Quotes/Convert Quote to Sold/select-Quote Status Action'), 'Sold Quote', false)

log.logWarning('--- Quote has Sold Quote option ---')
//Enter PO#
WebUI.setText(findTestObject('ABC/Quotes/Convert Quote to Sold/input-PO Order Number'), 'PO-111999333')

//Enter Quote Note text
WebUI.setText(findTestObject('ABC/Quotes/Convert Quote to Sold/textarea-Quote Notes'), 'Aenean eu leo quam. Pellentesque ornare sem lacinia quam venenatis vestibulum. Sed posuere consectetur est at lobortis. Integer posuere erat a ante venenatis dapibus posuere velit aliquet. Sed posuere consectetur est at lobortis.\r\rCurabitur blandit tempus porttitor. Nullam quis risus eget urna mollis ornare vel eu leo. Cras justo odio, dapibus ac facilisis in, egestas eget quam. Duis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit. Nulla vitae elit libero, a pharetra augue. Sed posuere consectetur est at lobortis.\r\rDuis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed posuere consectetur est at lobortis. Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor auctor. Integer posuere erat a ante venenatis dapibus posuere velit aliquet. Maecenas sed diam eget risus varius blandit sit amet non magna.\r\rMaecenas sed diam eget risus varius blandit sit amet non magna. Etiam porta sem malesuada magna mollis euismod. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante venenatis dapibus posuere velit aliquet. Vestibulum id ligula porta felis euismod semper. Curabitur blandit tempus porttitor. Aenean lacinia bibendum nulla sed consectetur.')

//Enter Internal Note text
WebUI.setText(findTestObject('ABC/Quotes/Convert Quote to Sold/textarea-Internal Notes'), 'Aenean eu leo quam. Pellentesque ornare sem lacinia quam venenatis vestibulum. Sed posuere consectetur est at lobortis. Integer posuere erat a ante venenatis dapibus posuere velit aliquet. Sed posuere consectetur est at lobortis.\r\rCurabitur blandit tempus porttitor. Nullam quis risus eget urna mollis ornare vel eu leo. Cras justo odio, dapibus ac facilisis in, egestas eget quam. Duis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit. Nulla vitae elit libero, a pharetra augue. Sed posuere consectetur est at lobortis.\r\rDuis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed posuere consectetur est at lobortis. Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor auctor. Integer posuere erat a ante venenatis dapibus posuere velit aliquet. Maecenas sed diam eget risus varius blandit sit amet non magna.\r\rMaecenas sed diam eget risus varius blandit sit amet non magna. Etiam porta sem malesuada magna mollis euismod. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante venenatis dapibus posuere velit aliquet. Vestibulum id ligula porta felis euismod semper. Curabitur blandit tempus porttitor. Aenean lacinia bibendum nulla sed consectetur.')

//Click Delivery Details button
WebUI.click(findTestObject('ABC/Quotes/Convert Quote to Sold/btn-Delivery Details'))

log.logWarning('--- Completed Quote Sold page ---')

//Fill in the additional fields on the Confirm Delivery Information page

//Enter text for Directions
WebUI.setText(findTestObject('ABC/Quotes/Convert Quote to Sold/Confirm Delivery Information/textarea-Directions'), 'Over the river. Through the woods. 1 mile take a left. Enter through side.')

//Set the Delivery Date based on the browser type
browserName=DriverFactory.getExecutedBrowser().getName()
if (browserName=="FIREFOX_DRIVER"){
	WebUI.setText(findTestObject('ABC/Quotes/Convert Quote to Sold/Confirm Delivery Information/select-Delivery Date'), '2019-12-31')
} else {
	WebUI.sendKeys(findTestObject('ABC/Quotes/Convert Quote to Sold/Confirm Delivery Information/select-Delivery Date'), Keys.chord('12-31-2019', Keys.ENTER, Keys.TAB))
}

//Set the Delivery Time to PM
WebUI.selectOptionByLabel(findTestObject('ABC/Quotes/Convert Quote to Sold/Confirm Delivery Information/select-Delivery Time'), 'PM', false)

//Set the Site Type to New Construction
WebUI.selectOptionByLabel(findTestObject('ABC/Quotes/Convert Quote to Sold/Confirm Delivery Information/select-Site Type'), 'New Construction', false)

//Enter text for Obstructions
WebUI.setText(findTestObject('ABC/Quotes/Convert Quote to Sold/Confirm Delivery Information/textarea-Obstructions'), '100 year old oak tree. Ill tempered Chihuahua. Pothole.')

//Enter text for Accessible Side of Bldg
WebUI.setText(findTestObject('ABC/Quotes/Convert Quote to Sold/Confirm Delivery Information/textarea-Accessible Side of Bldg'), 'All sides are accessible, except, left, right and front.')

//Enter text for Product Placement
WebUI.setText(findTestObject('ABC/Quotes/Convert Quote to Sold/Confirm Delivery Information/input-Product Placement'), 'Items can be left anywhere, as long as they are stacked neatly.')

//Enter text for Number of Stories
WebUI.setText(findTestObject('ABC/Quotes/Convert Quote to Sold/Confirm Delivery Information/input-Number of Stories'), '22')

//Enter text for the Roof Pitch
WebUI.setText(findTestObject('ABC/Quotes/Convert Quote to Sold/Confirm Delivery Information/input-Roof Pitch'), '12')

if (submitToBranch==false){
	//Click Convert to Order button
	WebUI.click(findTestObject('ABC/Quotes/Convert Quote to Sold/btn-Save and Send Order'))

	log.logWarning('--- Completed Delivery Information page ---')

	//The final page where an email recipient is selected, or in this case, typed in and the Quote Converted, along with a PDF of the items.
	//The fields in the Create Quote folder are used as the form is the same

	//Wait for the page to load and then wait for the email addresses to appear.
	WebUI.waitForPageLoad(15)

	//Wait for the Email Input field at the bottom of the page to become visible. We send to our own address, not an ABC Rep.
	WebUI.waitForElementVisible(findTestObject('ABC/Quotes/Create Quote/input-Email This Quote To'), 15)

	//Enter the email address to send the Quote to
	WebUI.setText(findTestObject('ABC/Quotes/Create Quote/input-Email This Quote To'), 'peter.wilson@technekes.com')

	//Wait for Convert to Order button to appear. There are two Convert to Order buttons. This is for the final button where the email address is entered
	WebUI.waitForElementVisible(findTestObject('ABC/Quotes/Convert Quote to Sold/btn-Send Order'), 15)

	//Click Convert to Order and submit the quote. An email should be generated with a PDF attachement
	WebUI.click(findTestObject('ABC/Quotes/Convert Quote to Sold/btn-Send Order'))

} else {
	//Submit To Branch
	WebUI.click(findTestObject('ABC/Quotes/Convert Quote to Sold/btn-Submit To Branch'))
}

WebUI.navigateToUrl(GlobalVariable.baseurl + '/quotes')
log.logWarning('--- Quote has been converted and email sent ---')

if (submitToBranch==true){
	//Click the Submissions button
	WebUI.click(findTestObject('ABC/Quotes/btn-Filter-Submissions'))
}

if (timedTest==true){
	firstQuoteName=WebUI.getText(findTestObject('ABC/Quotes/Convert Quote to Sold/accordion-Quote Name'))
	log.logWarning("--- Name of First Quote on Page --- " + firstQuoteName)
	if (submitToBranch==true){
		//First quote in Submission list should be the quote just created
		if (soldQuoteName==firstQuoteName){
			log.logWarning("SUCCESS: The quote is marked as Submitted correctly.")
		} else {
			log.logError("ERROR: The quote was not marked as Submitted")
			log.logError("Created Quote is: " + soldQuoteName + " --- " + "First listed quote is " + firstQuoteName)
			KeywordUtil.markFailed("ERROR: The quote was not marked as Submitted.")
		}
	} else {
		//First quote in the list should not be the first quote we just created
		if (soldQuoteName==firstQuoteName){
			log.logError("ERROR: The quote is not marked as Sold correctly.")
			KeywordUtil.markFailed("ERROR: The quote is not marked as Sold correctly.")
		} else {
			log.logWarning("SUCCESS: The quote has been marked as Sold")
		}
	}
}