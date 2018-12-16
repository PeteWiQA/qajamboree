//Define variables that reference the table objects
String katalonObject="Home/table-ytd-totals"
myPredefinedObject = findTestObject(katalonObject, [('row') : 1, ('column') : 1])
xpath=myPredefinedObject.findPropertyValue('xpath').toString().replaceAll('tr\\[1\\]/td\\[1\\]','tr')

//Count the number of Rows in the table, then sum the column
int rowsInTable=CustomKeywords.'tools.commonCode.countRowsPerPage'(xpath)
int siteColumnTotal=sumColumnTotal(katalonObject, 3, rowsInTable)


def sumColumnTotal(String objectName, int columnToSum, int tableRows){
	/* Sum the column of a table
	 * @param objectName - The Object Repository reference to the table
	 * @columnToSum - The column to perform the sum on
	 * @tableRows - The number of rows in a table
	 * @return - the sum of the column
	 */
	KeywordLogger log = new KeywordLogger()
	int columnTotal=0
	log.logWarning('Rows in the table: ' + tableRows)
	for (int loop = 1; loop <=tableRows; loop++) {
		int tempText=WebUI.getText(findTestObject(objectName, [('row') : loop, ('column') : columnToSum])).replaceAll("[^0-9-]","").toInteger()
		if (tempText==''){
			tempText=0
		}
		log.logWarning('Value from the table: ' + tempText)
		columnTotal=columnTotal+tempText
	}
	log.logWarning('Total from the site is: ' + columnTotal)
	return columnTotal
}

//Called by using:
//int siteColumnTotal=sumColumnTotal(katalonObject, 3, rowsInTable)
