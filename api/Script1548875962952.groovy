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
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.RestRequestObjectBuilder
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.testobject.impl.HttpTextBodyContent
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS

import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.util.KeywordUtil
 
import mypackage.SampleRequestObject
import mypackage.SampleResponseObject as SampleResponseObject

import groovy.json.JsonSlurper
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger
KeywordLogger log = new KeywordLogger()

 
SampleRequestObject req = new SampleRequestObject()

/* Parameters
String order_id
String customer_number
String branch_number
String tier_code
String unit_measure

order_id="d6159b8d-b7f9-4101-9234-38741de55d40"
customer_number="141780"
branch_number="436"
tier_code="0.1.30.88.432.23.82.0150080012"
unit_measure="BX"
*/

String order_id
order_id="d6159b8d-b7f9-4101-9234-38741de55d40"

//ResponseObject resp = req.buildPostApiRequest1("3645AAAAA-f553-45dd-9e3e-423cf415780d")
ResponseObject resp = req.buildPostApiRequestDataDriven(order_id, customer_number, branch_number, tier_code, unit_measure)
//ResponseObject resp = req.buildPostApiRequest1("304fa175-f553-45dd-9e3e-423cf415780d")
 
if(SampleResponseObject.getStatusCode(resp) != 200) {
	log.logWarning("ERROR: Status code for the request is not 200 as expected. It is "
			+ SampleResponseObject.getStatusCode(resp))
	KeywordUtil.markFailed("ERROR: Status code for the request is not 200 as expected. It is "
			+ SampleResponseObject.getStatusCode(resp))
} /*else {
	println(SampleResponseObject.getStatusCode(resp))
}
*/

log.logWarning("The API Server Response Time is: " + SampleResponseObject.getWaitingTime(resp))

if(SampleResponseObject.getWaitingTime(resp) > 10000) {
	KeywordUtil.markError("Your request takes more than 10000ms. Actual time is "
			+ SampleResponseObject.getWaitingTime(resp))
}

String jsonString=SampleResponseObject.getResponseText(resp)

JsonSlurper slurper = new JsonSlurper()
Map parsedJson = slurper.parseText(jsonString)
log.logWarning("Full Response from API: " +  parsedJson)

String getSelectedKey = parsedJson.pricing.tiers.unit_price
String APIReturnedPrice = getSelectedKey.replaceAll('\\[|\\]','')

//String getSelectedKey = parsedJson.ok

/* Additional items that can be parsed
 * log.logWarning("Customer Number: " + parsedJson.pricing.customer_number)
 * log.logWarning("Branch: " + parsedJson.pricing.branch_number)
 * log.logWarning("Unit Price: " + parsedJson.pricing.tiers.unit_price)
 */
log.logWarning("Tier Code of Priced Item: " + parsedJson.pricing.tiers.tier_code)
log.logWarning("Price Returned from API: " + parsedJson.pricing.tiers.price)
log.logWarning("Expected Item Price from file: " + item_price)

if (item_price!=APIReturnedPrice){
	log.logError("ERROR: The API Price and Price from file DO NOT Match")
	KeywordUtil.markFailed('ERROR: The API Price and Price from file DO NOT Match')
}

WebUI.delay(1)