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

/* Search for a Prospect and validate the fields are present
 * Read the company name, address, phone and email from the search results
 * Open the Prospect Profile, read the same values from the card and compare to the search results
 * Created 03-14-2018 10:41AM by Pete Wilson
 * Updated 03-24-2018 12:39PM by Pete Wilson
 * Updated 04-20-2018 09:53AM by Pete Wilson
 * Updated 08-14-2018 13:02PM by Pete Wilson
 * Updated 12-04-2018 14:06PM by Pete Wilson
 */

WebUI.navigateToUrl(GlobalVariable.baseurl + '/prospects')
//Check to make sure the Prospect page loads. Look for the text, All Prospects
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('All Prospects')

//Verify that the page loaded successfully and there are no "There was an error retreiving results" messaages
CustomKeywords.'tools.commonCode.checkForAlert'()

/* Use the date to find a Prospect for Today. 
 * This script is usually run after the Create Prospect
 * and should find the script created today. 
 */

//Use the date to look for a Prospect of today's date
mydate = new Date()
formattedDate = mydate.format("MMddyyy")

//Wait for the Results Found text to appear. This contains the number of Prospects on the page.
WebUI.waitForElementVisible(findTestObject('ABC/Prospects/label-Prospect Results Found'), 10)

//Read the number of Prospects and store it
prospectResults=WebUI.getText(findTestObject('ABC/Prospects/label-Prospect Results Found')).replaceAll("[^0-9]","").toInteger()

//If there are 0 Prospects, we need to exit the test as there will be nothing to read
if (prospectResults==0) {
	log.logWarning('ERROR: No Prospects exist for this Sales Rep. Exiting test')
	log.logError('ERROR: No Prospects exist for this Sales Rep. Exiting test')
} else {
	//If the Sales Rep has prospects, attempt a search

	WebUI.setText(findTestObject('ABC/Prospects/input-Prospect Search'), GlobalVariable.companyName)

	log.logWarning('--- Searching for Prospect - ' + GlobalVariable.companyName)

	WebUI.click(findTestObject('ABC/Prospects/btn-Go'))

	WebUI.delay(3)

	prospectResults=WebUI.getText(findTestObject('ABC/Prospects/label-Prospect Results Found')).replaceAll("[^0-9]","").toInteger()
	//If no results are returned for the prospect search, we need to exit the test as there will be nothing to read
	if (prospectResults==0) {
		log.logWarning('ERROR: No Prospect by the name of ' + GlobalVariable.companyName + ' exists for this Sales Rep. Exiting test')
		log.logError('ERROR: No Prospect by the name of ' + GlobalVariable.companyName + ' exists for this Sales Rep. Exiting test')
	} else {

		//Get the name of the Prospect, then click to get the details. The first prospect is actually at position 2
		prospectCompanyName=WebUI.getText(findTestObject('ABC/Prospects/text-Prospect Name', [('Variable') : 2]))
		log.logWarning('Located Prospect Company Name:=' + prospectCompanyName)

		//Read the Prospect details from the search results
		prospectName=WebUI.getText(findTestObject('ABC/Prospects/Prospect Search Details/label-Prospect Details', [('Variable') : 1]))
		log.logWarning('Located Prospect Name:=' + prospectName)

		prospectAddress=WebUI.getText(findTestObject('ABC/Prospects/Prospect Search Details/label-Prospect Details', [('Variable') : 2])).replaceAll('Address: ','')
		log.logWarning('Located Prospect Address:=' + prospectAddress)

		prospectPhone=WebUI.getText(findTestObject('ABC/Prospects/Prospect Search Details/label-Prospect Details', [('Variable') : 3])).replaceAll('Phone: ','')
		log.logWarning('Located Prospect Phone:=' + prospectPhone)

		prospectEmail=WebUI.getText(findTestObject('ABC/Prospects/Prospect Search Details/label-Prospect Details', [('Variable') : 4])).replaceAll('Email: ','')
		log.logWarning('Located Prospect Email:=' + prospectEmail)

		//Open the Prospect to get to Prospect Details page
		WebUI.click(findTestObject('ABC/Prospects/text-Prospect Name', [('Variable') : 2]))

		WebUI.delay(3)

		//Read the Header for the name of the Prospect
		prospectProfileCompanyName=WebUI.getText(findTestObject('ABC/Prospects/Prospect Profile/label-Prospect Name'))

		//Read the Prospect Details as a text block
		prospectProfileFullAddress=WebUI.getText(findTestObject('ABC/Prospects/Prospect Profile/label-Prospect Details Text Block'))

		/* Read Prospect Profile Address and Compare to the address details from the previous page
		 * Get the Prospect Profile Address from the return results and parse it into multiple lines
		 * The address will be compared to the address from the Search Results page
		 * Use the ReadLines() command to read the block
		 * Each line can then be set to a variable
		 */

		prospectProfileDetails = prospectProfileFullAddress.readLines()
		prospectProfileName=prospectProfileDetails[0]
		prospectProfileAddress=prospectProfileDetails[1] + ' ' + prospectProfileDetails[2]
		prospectProfilePhone=prospectProfileDetails[3]
		prospectProfileEmail=prospectProfileDetails[4].replace('@ ','')
		//Remove the first @ from the retrieved email address. It comes in as @name@domain.com

		//Write out the Prospect Profile Details
		log.logWarning('Prospect Profile Company Name:=' + prospectProfileCompanyName)
		log.logWarning('Prospect Profile Name:=' + prospectProfileName)
		log.logWarning('Prospect Profile Address:=' + prospectProfileAddress)
		log.logWarning('Prospect Profile Phone:=' + prospectProfilePhone)
		log.logWarning('Prospect Profile Email:= ' + prospectProfileEmail)

		/* Compare the details from the Search Results and Prospect Profile
		 * If the values don't match, there is an error reading data
		 * And the test should be marked as Failed
		 */

		//Compare the search results to the profile results. Pass the search result, the profile results and the type of item it is
		compareSearchToProfile(prospectCompanyName, prospectProfileCompanyName, "Company Name")
		compareSearchToProfile(prospectName, prospectProfileName, "Name")
		compareSearchToProfile(prospectAddress, prospectProfileAddress, "Address")
		compareSearchToProfile(prospectPhone, prospectProfilePhone, "Phone")
		compareSearchToProfile(prospectEmail, prospectProfileEmail, "Email")
	}
}

def compareSearchToProfile(String searchResults, String profileResult, String dataType){
	/* Compare the information from the Prospect Page with the data displayed on the Prospect Profile
	 * @param searchResults - value read from the Prospect Page
	 * @param profileResult - value read from the Prospect Profile Page
	 * @param dataType - the data type to check such as email, phone, address
	 * return true if values match, otherwise false
	 *
	 * Take in the value from the Search Criteria and the Profile Data. dataType is line item, such Name, Email, Address
	 */
	KeywordLogger log = new KeywordLogger()
	if (searchResults!=profileResult){
		log.logError('ERROR: Prospect ' + dataType + ' does not match')
		KeywordUtil.markFailed('ERROR: Prospect ' + dataType + ' does not match')
	} else {
		log.logWarning('SUCCESS: Prospect ' + dataType + ' is a match')
	}
}