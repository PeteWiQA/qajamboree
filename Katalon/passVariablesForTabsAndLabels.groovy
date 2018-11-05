//this was used to set the variable for a footer reference. The table value is: //div[@id='DataTables_Table_2_wrapper']/div[2]/div/div
//The 2 is replaced with a variable so a single object can be used with passed parameters
//div[@id='DataTables_Table_${index}_wrapper']/div[2]/div/div
tempText=WebUI.getText(findTestObject('Dashboard/text-Pagination Results Entries', [('index') : 3]))

//Click the Plans tab
WebUI.click(findTestObject('Dashboard/tab-Tab Strip', [('tabIndex') : 3]))

WebUI.selectOptionByLabel(findTestObject('Dashboard/select-Dropdowns',	[('tabIndex') : 4]), '0.1', false)
