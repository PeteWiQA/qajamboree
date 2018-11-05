//Pass two variables in Katalon

WebUI.callTestCase(findTestCase('Profile Page'), [('ID') : '1091504 ', ('Name') : 'Enter User Name Here'], 
    FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('Profile Page'), [('ID') : idNum, ('Name') : userName], 
    FailureHandling.STOP_ON_FAILURE)

//Code to pass two variables
Search_Result = WebUI.getText(findTestObject('search-input/table-Data ', [('rowNo') : '1'
            , ('colNo') : colNo]), FailureHandling.STOP_ON_FAILURE)

Definition for two variables in object
//*/div/table/tbody[@data-reactid=".1.0.0.1.1.5.0.1"]/tr[${rowNo}]/td[${colNo}]

//Read 2017 column data
log.logWarning('Reading 2017 Data')
//Row 1 is the Product name. The Percentage data begins in Row 2, Column 1
for (row = 2; row <=4; row++) {
	//Use the row counter to read Column 2
	for (column=1; column<=4; column++){
		//tempText = WebUI.getText(findTestObject('table-Metrics', [('row') : '2', ('column') : '2']))
		tempText = WebUI.getText(findTestObject('table-Metrics', [('row') : row, ('column') : column]))
		log.logWarning('Column Data:=' + tempText)
	}
}

//Pass Three variables in Katalon
//The table is defined in the following way, where the table name gets a parameter:
//table[@id='DataTable_Name_${index}']/tbody/tr[${row}]/td[${column}]

//To select the correct table, it's index number is passed for the variable, "index". The first table on the site has a number of 0. For the code below, the table number is chosen, then column 3 of each row is read and stored.

tempText=WebUI.getText(findTestObject('table-DataTable', [('index') : 0, ('row') : loop, ('column') : 3]))
