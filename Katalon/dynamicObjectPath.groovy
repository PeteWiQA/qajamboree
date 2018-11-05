//A Katalon object can be dymanic by change the string of it's name in the FindObject method
//Define two objects with the correct xpath and substitute the xpath reference as a string

object="filter-Row 1 Filters"
object="filter-Row 2 Filters"

//Click the filter to validate
WebUI.click(findTestObject('Data/' + object, [('index') : filterNumber]))


Finding Test Object with id 'Data/filter-Row 1 Filters'
Found 2 web elements with id: 'Data/filter-Row 1 Filters' located by 'By.xpath: //div[@id='map-filters']/div/a[3]'

Finding Test Object with id 'Data/filter-Row 2 Filters'
Found 1 web elements with id: 'Data/filter-Row 2 Filters' located by 'By.xpath: //div[@id='map-filters']/div[2]/a[3]'
