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

/* The purpose of this test is to sum the Seasonality Plan figures
 * under 4. Adjust Seasonality. It reads and sum both the Plan and
 * Adjusted Plan values and compares to the total listed in the footer
 * The two values need to match or else an error has occurred.
 * A difference of $5 is considered acceptable
 * It also makes sure the Total is displayed. This is for validation of AADSA-106
 * 
 * Created 02-27-2018 16:37PM by Pete Wilson
 * Updated 04-20-2018 12:29PM by Pete Wilson
 * Updated 08-15-2018 13:39PM by Pete Wilson
 */

log.logWarning('--- Begin Sum Seasonality Plan Values ---')

//Read and Sum the Sales Total column
log.logWarning('--- Start Section 4 - Sum Seasonality Plan Values ---')

//Jump to Section 4 - Seasonality and read the first month from the list.
monthText = WebUI.getText(findTestObject('ABC/Read-Only Sales Plan Budget/Section 4 - Seasonality/Seasonality - Month Name',[('Variable') : 1]))

//Convert the name of the month to an integer, ie-April=4
parsedDate = Date.parse('MMM', monthText)

numberOfMonth = ((parsedDate.format('MM')) as int)
planTotal = 0

//Loop through the number of months displayed and read the Plan value
log.logWarning('--- Read Seasonality Plan Values ---')
for (loop = numberOfMonth; loop <= 12; loop++) {
	//Read the Month and Adjusted Plan
	monthText = WebUI.getText(findTestObject('ABC/Read-Only Sales Plan Budget/Section 4 - Seasonality/Seasonality - Month Name',
			[('Variable') : loop]))

	seasonalityPlan=WebUI.getText(findTestObject('ABC/Read-Only Sales Plan Budget/Section 4 - Seasonality/label-Seasonality - Plan Values',
			[('Variable') : loop])).replaceAll("[^0-9-]","").toInteger()

	log.logWarning('Month:= ' + monthText + ' -- Plan Value:= ' + seasonalityPlan)

	//Sum up the Adjusted Plan total
	planTotal = (planTotal + seasonalityPlan)
}

//Read the total from the Plan Footer (Row 13)
sitePlanValue=WebUI.getText(findTestObject('ABC/Read-Only Sales Plan Budget/Section 4 - Seasonality/label-Footer Plan Value Total',[('Variable') : (loop)])).replaceAll("[^0-9-]","").toInteger()
//If the value is missing, display an error and set the value to $0, so the calculation can continue.
if (sitePlanValue==''){
	log.logWarning('ERROR: The Seasonality Plan value is missing under Section 4. Adjust Seasonality')
	log.logError('ERROR: The Seasonality Plan value is missing under Section 4. Adjust Seasonality')
	sitePlanValue='0'
}

log.logWarning('Calculated Plan Value:=' + planTotal + ' --- The site Plan Value is:=' + sitePlanValue)

if (planTotal!=sitePlanValue){
	log.logWarning('ERROR: The calculated Plan Total does not equal the Plan Total from the site')
	if (Math.abs(planTotal-sitePlanValue)<=5){
		log.logWarning('NOTE: The difference between the calculated and site values is less than $5, which is most likely a rounding issue')
	} else {
		log.logError('ERROR: The difference between the calculated and site value is more than $5. Manual investigation is needed')
		KeywordUtil.markFailed('ERROR: The difference between the calculated and site value is more than $5. Manual investigation is needed')
	}
}

//Sum Seaonality Adjusted Plan Values
log.logWarning('--- Start Section 4 - Sum Seasonality Adjusted Plan Values ---')
log.logWarning('--- Read Seasonality Adjusted Plan Values  Values ---')
adjustedPlanTotal=0
//Loop through the number of months displayed and read the Adjusted Plan value
for (loop = numberOfMonth; loop <= 12; loop++) {
	//Read the Month and Adjusted Plan
	monthText=WebUI.getText(findTestObject('ABC/Read-Only Sales Plan Budget/Section 4 - Seasonality/Seasonality - Month Name',
			[('Variable') : loop]))

	adjustedSeasonalityPlan=WebUI.getText(findTestObject('ABC/Read-Only Sales Plan Budget/Section 4 - Seasonality/label-Seasonality - Adjusted Plan Values',
			[('Variable') : loop])).replaceAll("[^0-9-]","").toInteger()

	log.logWarning((('Month:= ' + monthText) + ' -- Adjusted Plan Value:= ') + adjustedSeasonalityPlan)

	//Sum up the Adjusted Plan total
	adjustedPlanTotal = (adjustedPlanTotal + adjustedSeasonalityPlan)
}

//Read the total from the Plan Footer (Row 13)
sitePlanValue=WebUI.getText(findTestObject('ABC/Read-Only Sales Plan Budget/Section 4 - Seasonality/label-Footer Adjusted Plan Value Total',[('Variable') : (loop)])).replaceAll("[^0-9-]","").toInteger()
//If the value is missing, display an error and set the value to $0, so the calculation can continue.
if (sitePlanValue==''){
	log.logWarning('ERROR: The Adjusted Plan value is missing under Section 4. Adjust Seasonality')
	log.logError('ERROR: The Adjusted Plan value is missing under Section 4. Adjust Seasonality')
	sitePlanValue='0'
}
log.logWarning('Calculated Plan Value:=' + adjustedPlanTotal + ' --- The site Adjusted Plan Value is:=' + sitePlanValue)

if (adjustedPlanTotal!=sitePlanValue){
	log.logWarning('ERROR: The calculated Adjusted Plan Total does not equal the Adjusted Plan Total from the site')
	if (Math.abs(adjustedPlanTotal-sitePlanValue)<=5){
		log.logWarning('NOTE: The difference between the calculated and site values is less than $5, which is most likely a rounding issue')
	} else {
		log.logError('ERROR: The difference between the calculated and site value is more than $5. Manual investigation is needed')
		KeywordUtil.markFailed('ERROR: The difference between the calculated and site value is more than $5. Manual investigation is needed')
	}
}

log.logWarning('--- End Sum Seasonality Plan Values ---')