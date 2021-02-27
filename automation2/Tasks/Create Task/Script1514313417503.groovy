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

/* The purpose of this test is to create a new task for a Sales Rep.
 * It fills in all the available fields and adds the current date and time
 * to the end to show when the task was created and generate a small
 * amount of randomness
 * Updated 01-26-2018 11:13AM by Pete Wilson
 * Updated 02-01-2018 10:12AM by Pete Wilson
 * Updated 04-19-2018 15:40PM by Pete Wilson
 * Updated 08-14-2018 14:51PM by Pete Wilson
 */

WebUI.navigateToUrl(GlobalVariable.baseurl + '/tasks')

//Check to make sure the Tasks page loads. Look for the text, My Open Tasks
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('My Open Tasks')

//The date and time are used as part of the subject line to show when the Task was created.
String formattedDate=CustomKeywords.'tools.commonCode.getDateAndTime'()

WebUI.click(findTestObject('ABC/Tasks/btn-Create New Task Button'))

log.logWarning('--- Filling in New Task Details ---')

WebUI.setText(findTestObject('ABC/Tasks/input-New Task Subject Field'), 'This is a new Task Subject created - ' + formattedDate)

WebUI.setText(findTestObject('ABC/Tasks/textarea-New Task Note Field'), 'Aenean eu leo quam. Pellentesque ornare sem lacinia quam venenatis vestibulum. Sed posuere consectetur est at lobortis. Integer posuere erat a ante venenatis dapibus posuere velit aliquet. Sed posuere consectetur est at lobortis. Curabitur blandit tempus porttitor. Nullam quis risus eget urna mollis ornare vel eu leo. Cras justo odio, dapibus ac facilisis in, egestas eget quam. Duis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit. Nulla vitae elit libero, a pharetra augue. Sed posuere consectetur est at lobortis. Duis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed posuere consectetur est at lobortis. Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor auctor. Integer posuere erat a ante venenatis dapibus posuere velit aliquet. Maecenas sed diam eget risus varius blandit sit amet non magna. Maecenas sed diam eget risus varius blandit sit amet non magna. Etiam porta sem malesuada magna mollis euismod. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante venenatis dapibus posuere velit aliquet. Vestibulum id ligula porta felis euismod semper. Curabitur blandit tempus porttitor. Aenean lacinia bibendum nulla sed consectetur. ')

//This selects the date picker and then clicks it again to select Today as the default.
//This should be changed in the future to make selecting a date more robust.
WebUI.click(findTestObject('ABC/Tasks/btn-New Task Date Picker'))

WebUI.delay(1)

WebUI.click(findTestObject('ABC/Tasks/btn-New Task Date Picker'))

//Save the Task
WebUI.click(findTestObject('ABC/Tasks/btn-New Task Add Button'))

//In many cases a confirmation dialog is presented. This Accepts that dialog and allows the script to continue.
CustomKeywords.'tools.commonCode.checkForAlert'()

//Return to Home page and confirm the entered Task is visible
WebUI.navigateToUrl(GlobalVariable.baseurl)

WebUI.waitForPageLoad(15)
log.logWarning('Searching for Task: This is a new Task Subject created - ' + formattedDate)
CustomKeywords.'errorCheck.validateSalesDashboard.checkPageLoad'('This is a new Task Subject created - ' + formattedDate)