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
KeywordLogger log = new KeywordLogger()

String result, Data_fromCell

JsonSlurper slurper = new JsonSlurper()

FileInputStream file = new FileInputStream ("//Users//petewi//Downloads//100names.xlsx")
XSSFWorkbook workbook = new XSSFWorkbook(file);
XSSFSheet sheet = workbook.getSheetAt(0);

int rowCount = sheet.getLastRowNum()
int numberOfColumns = sheet.getRow(1).getLastCellNum()

'Read data from excel'
for (loop = 1; loop <=rowCount; loop++) {
	Data_fromCell=sheet.getRow(loop).getCell(0).getStringCellValue();
	response1 = WS.sendRequest(findTestObject('eComm', [('name') : Data_fromCell]))
	result = slurper.parseText(response1.getResponseBodyContent())
	log.logWarning("API Response #" + loop + ": ---> " + result)
}

file.close();
