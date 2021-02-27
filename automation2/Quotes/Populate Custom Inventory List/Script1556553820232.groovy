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
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/* This is a custom list of items to be fed into creating a quote
 * This list needs to be built using a Sales Rep that can see these items
 * If these items do not show under, My Branch Only, the test will not function
 */

FileInputStream file = new FileInputStream ("//Users//petewi//Documents//GitHub//katalon//projects//abc//Data Files//quote-items.xlsx")
XSSFWorkbook workbook = new XSSFWorkbook(file);
//XSSFSheet sheet = workbook.getSheetAt(1);
XSSFSheet sheet = workbook.getSheet("SKU");
int rowCount = sheet.getLastRowNum()
//Read data from spreadsheet file

for (loop = 1; loop <=rowCount; loop++) {
	//Assign spreadsheet columns to variables
	String cellType=sheet.getRow(loop).getCell(0).getCellType()
	//Determine if the cell contains Text or Numeric data
	if (cellType=="1"){
		String cellTextValue=sheet.getRow(loop).getCell(0).getStringCellValue()
		GlobalVariable.inventorySKU[loop]=cellTextValue
	}else{
		long cellIntValue=sheet.getRow(loop).getCell(0).getNumericCellValue()
		GlobalVariable.inventorySKU[loop]=cellIntValue
	}
}

file.close()

//A list can built manually using the following method

/*
(GlobalVariable.inventorySKU[2])='25CS38C2'
(GlobalVariable.inventorySKU[3])='ATCS24W1'
*/

/* Sample list for item description:
(GlobalVariable.inventoryList[2])='CS SMART LAP CD 3/8X8" LFTM COLOR'
(GlobalVariable.inventoryList[3])='ALUM TRIM CL SM 24" WHITE W1'
*/