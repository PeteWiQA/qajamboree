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
import groovy.json.JsonSlurper as JsonSlurper
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger
import groovy.time.TimeCategory 
import groovy.time.TimeDuration

/* The purpose of this script is verify pricing for the Acculynx/Eagleview
 * Connect Partner pricing API. It builds a request using customer number,
 * branch, tier code and UOM. These are sent to the API object to retrieve 
 * the price. This returned price is validated against the expected price 
 * in the spreadsheet
 * 
 * Currently, it uses a hardcoded order_id value on the Pricing Request object
 * to send the request. If needed a call can be made to the Initalize Order
 * object to create a new UUID for the test.
 * 
 * Created 04-23-2019 12:52PM by Pete Wilson 
 * 
 */

KeywordLogger log = new KeywordLogger()

def timeStart = new Date()
int responseTimeError, pricingError, statusCodeError, priceAvailableError, messageFailureError=0
int api_response_time
float successRate
String order_id="304fa175-f553-45dd-9e3e-423cf415780d"

/* If needed, this will initialize the order
def order_id = UUID.randomUUID().toString()

//Initialize Order
initialize_order = WS.sendRequest(findTestObject('Initialize Order',
	[('order_id') : order_id,
	('date') : timeStart]))
 */

def slurper = new JsonSlurper()

FileInputStream file = new FileInputStream ("//Users//petewi//Katalon Studio//Postman Testing//Data Files//acculynx-pricing.xlsx")
FileOutputStream outFile = new FileOutputStream(new File("//Users//petewi//Katalon Studio//Postman Testing//Data Files//acculynx-pricing-results.xlsx"))
XSSFWorkbook workbook = new XSSFWorkbook(file);
//XSSFSheet sheet = workbook.getSheetAt(1);
XSSFSheet sheet = workbook.getSheet("pricing-1500-items");
int rowCount = sheet.getLastRowNum()

//Read data from spreadsheet file
for (loop = 1; loop <=rowCount; loop++) {
	//Assign spreadsheet columns to variables
	int customer_number=sheet.getRow(loop).getCell(0).getNumericCellValue()
	int branch_number=sheet.getRow(loop).getCell(1).getNumericCellValue()
	String tier_code=sheet.getRow(loop).getCell(2).getStringCellValue();
	String uom=sheet.getRow(loop).getCell(3).getStringCellValue();
	String item_price=sheet.getRow(loop).getCell(4).getNumericCellValue();
	
	//Send API Request
	api_request = WS.sendRequest(findTestObject('Pricing Request', 
		[('order_id') : order_id,
		('customer_number') : customer_number, 
		('branch_number') : branch_number, 
		('tier_code') : tier_code, 
		('uom') : uom]))
	
	//Store Response time
	api_response_time=api_request.getWaitingTime()
	
	//If the Status of the request is 200 (OK) process the response
	//Otherwise output the error code returned
	if (api_request.getStatusCode()==200){
		
		//Store and parse the api response
		def api_response = slurper.parseText(api_request.getResponseBodyContent())
		//Store the item price
		String itemPrice = api_response.pricing.tiers[0].price
		//Verify there is no failure message
		String failure_message=api_response.pricing.tiers[0].failure_messages
		if (failure_message!='[]'){
			log.logError("ERROR: Failure Message on pricing request " + loop + " " + failure_message)
			sheet.getRow(loop).createCell(5).setCellValue("ERROR: Failure Message on pricing request " + loop + " " + failure_message)
			messageFailureError++
		}
		
		//Verify that Price Available is true within the response
		String price_available = api_response.pricing.tiers[0].price_available
		if (price_available!="true"){
			log.logError("ERROR: Price Available error on pricing request " + loop + " " + price_available)
			sheet.getRow(loop).createCell(5).setCellValue("ERROR: Price Available error on pricing request " + loop + " " + price_available)
			priceAvailableError++
		}
		
		//Display pricing details
		log.logWarning('Tier Code: ' + tier_code + ' <---> ' + 
			'Item Price: ' + itemPrice + ' <---> ' + 
			'Expected Price: ' + item_price + ' <---> ' + 
			'Response time: ' + api_response_time)
		if (itemPrice!=item_price){
			log.logError("ERROR: The returned price for request " + loop + " does not match the expected price")
			sheet.getRow(loop).createCell(5).setCellValue("ERROR: Pricing error for request " + loop + 
				" Tier Code: " + tier_code + " <---> " + 
				"Item Price: " + itemPrice + " <---> " + 
				"Expected Price: " + item_price);
			pricingError++
		}
	} else {
		//Log the error returned
		log.logError("ERROR: There was an error with the pricing request " + loop + ". Error code: " + api_request.getStatusCode())
		sheet.getRow(loop).createCell(5).setCellValue("ERROR: There was an error with the pricing request " + loop + ". Error code: " + api_request.getStatusCode());
		statusCodeError++
	}
	//If the response time is over a threshold, log it as an error
	if (api_response_time>10000){
		log.logError("The server response time is higher than expected with a time of: " + api_response_time)
		responseTimeError++
	}
}

def timeStop = new Date()
TimeDuration duration = TimeCategory.minus(timeStop, timeStart)

//Write out the summary of success/errors
log.logWarning("<--- API Pricing Request Results --->")
log.logWarning(rowCount + " items priced:")
log.logWarning("There were " + responseTimeError + " requests with a higher than average response time")
log.logWarning("There were " + pricingError + " requests with a different price than expected")
log.logWarning("There were " + statusCodeError + " requests with an unexpected status code")
log.logWarning("There were " + priceAvailableError + " requests with a Price Available error")
log.logWarning("There were " + messageFailureError + " requests with a Message Failure error")
successRate = ((rowCount - (Integer.valueOf(pricingError) + Integer.valueOf(statusCodeError))) / rowCount) * 100
log.logWarning("Success rate: " + successRate.round(2) +"%" )
log.logWarning("Execution Time: " + duration)

//Close all files
workbook.write(outFile);
outFile.close();
file.close();