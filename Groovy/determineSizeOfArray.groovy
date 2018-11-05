months=['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'Decemeber']
println(months.size())

//This is the number of contacts on the page. Use the "classname" to find "notes-wrap"
WebElement Webtable1 = driver1.findElement(By.className('notes-wrap'))

//Get the number of rows in the table and turn it into a List
List<WebElement> updatedTotalItemCount = Webtable1.findElements(By.xpath('//*[@role=\'task-show-complete-button\']'))

updatedNumberOfTasks = updatedTotalItemCount.size()

//Count the number of pages
WebDriver notStartedFilterdriver = DriverFactory.getWebDriver()

WebElement notStartedFilterPagination=notStartedFilterdriver.findElement(By.xpath('//*/div[@id="DataTables_Table_3_paginate"]/ul/li'))

//Get the number of rows in the Pagination ribbon and turn it into a List
List<WebElement> notStartedPaginationListCount=notStartedFilterPagination.findElements(By.xpath('//*/div[@id="DataTables_Table_3_paginate"]/ul/li'))

//Get the last page of Pagination
pages=notStartedPaginationListCount.size()

def states=['AL','AK','AZ','AR','CA','CO','CT','DE','FL','GA','HI',
'ID','IL','IN','IA','KS','KY','LA','ME','MD','MA','MI','MN','MS',
'MO','MT','NE','NV','NH','NJ','NM','NY','NC','ND','OH','OK','OR',
'PA','RI','SC','SD','TN','TX','UT','VT','VA','WA','WV','WI','WY']
//Pick a State Abbreviation
int rndState=Math.abs(new Random().nextInt(states.size()));
String stateAbbr=states[rndState]
