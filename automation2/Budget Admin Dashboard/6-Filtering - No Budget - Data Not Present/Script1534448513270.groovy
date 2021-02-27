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
import org.openqa.selenium.Keys as Keys
import com.kms.katalon.core.util.KeywordUtil

KeywordLogger log = new KeywordLogger()

/* This script validates figures and filters on the Budget Admin Dashboard
 * It performs a branch search and gathers information about the first record returned
 * It searches for a particular sales rep and returns the information listed on the information card
 * It Clicks through each filter and confirms the number of results matches the number in the footer for pagination
 * It Selects - All
 * The filters under No Budget
 * The filters under Budget Present
 * 
 * Updated 04-20-2018 14:35PM by Pete Wilson
 * Updated 08-16-2018 17:02PM by Pete Wilson
 */

//Check the filter by passing the filter name and the filter object
CustomKeywords.'tools.commonCode.verifyFilterToPagination'('No Budget - Data Not Present', 'ABC/Budget Admin Dashboard/Filter-No Budget-Data Not Present')