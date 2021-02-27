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

/* Read inventory items for the ABC Web App database
 * and create a list of inventory items to use in
 * quoting. List is based on zone.
 * 
 */

int loop=1, columnIndex=1, rowCount=1
String inventoryData=''
//Connect to Database Object in Data Files
TestData inventoryDB = findTestData('Data Files/Inventory Items Query')
rowCount=inventoryDB.getRowNumbers()
List columnNames=inventoryDB.getColumnNames()
//Buffer the list by adding null to position 0
columnNames.add(0, "")

for (loop = 1; loop <=rowCount; loop++) {
	//Read the column data from the database and assign to a List
	inventoryData=inventoryDB.getValue(columnNames.indexOf("item_number"), loop)
	GlobalVariable.inventorySKU[loop]=inventoryData
}