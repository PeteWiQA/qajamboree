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
import groovy.time.TimeCategory
import groovy.time.TimeDuration

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

File file1 = new File('/Users/petewi/Downloads/quoteloadtimes.txt')
file1<<"<--- Starting Session --->" + "\n"

//Go to the Quotes section and count the number of Quotes assigned to the Sales Rep
for (loop = 1; loop <=60; loop++) {
	WebUI.navigateToUrl(GlobalVariable.baseurl + '/quotes')
	//Select the filter to Open Orders. This should be the default of the page, but this is to make sure or else the test will fail
	WebUI.click(findTestObject('ABC/Quotes/btn-Filter-Open Quotes'))
	//Search for the newly created quote
	WebUI.setText(findTestObject('ABC/Quotes/input-Search Quote Name'), 'Test Quote created from QA - 10102019133737')
	WebUI.click(findTestObject('ABC/Quotes/btn-Go Quote Search'))
	WebUI.click(findTestObject('ABC/Quotes/btn-Filter-All Quotes'))
	WebUI.delay(2)
	timeStart=new Date()
	WebUI.click(findTestObject('ABC/Quotes/Convert Quote to Sold/accordion-Quote Header'))
	elementVisible=WebUI.waitForElementVisible(findTestObject('ABC/Quotes/Convert Quote to Sold/btn-Send Quote'),5)
	counter=0
	while (elementVisible!=true && counter<=30){
			WebUI.delay(1)
			elementVisible=WebUI.waitForElementVisible(findTestObject('ABC/Quotes/Convert Quote to Sold/btn-Send Quote'),1)
			counter++
	}
	timeStop = new Date()
	TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
	log.logWarning("Execution Time: " + duration)
	file1 << "Execution Time: " + timeStop +"," + duration + "\n"
	WebUI.delay(60)
}