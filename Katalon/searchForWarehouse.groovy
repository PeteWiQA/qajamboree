def searchForWarehouse(String warehouseSearchCriteria, String nameOfWarehouse){
	/* Enter a Warehouse and confirm the Warehouse exists
	 * @param warehouseSearchCriteria, search criteria of the Warehouse to look for
	 * @param nameOfWarehouse, the text that should be returned for Warehouse details
	 * @return that the Warehouse was found, otherwise an error
	 */
	KeywordLogger log = new KeywordLogger()
	//Enter search criteria
	WebUI.setText(findTestObject('Project/Search Warehouse/input-Search Warehouse'), warehouseSearchCriteria)
	WebUI.delay(2)
	//Confirm there are results
	int returnedResults=WebUI.getText(findTestObject('Project/Search Warehouse/text-Warehouse Search - Results Found')).replaceAll("[^0-9]","").toInteger()
	if (returnedResults==0) {
		log.logWarning('ERROR: No results were returned. No Warehouse matches the search criteria.')
		log.logWarning('ERROR: The Warehouse ' + warehouseSearchCriteria + ' is not valid')
	} else {
	//Does the Warehouse result contain the expected criteria?
		String WarehouseName=WebUI.getText(findTestObject('Project/Search Warehouse/link-Name of Returned Warehouse'))
		log.logWarning('Warehouse Name= ' + WarehouseName)
		if (WarehouseName.contains(nameOfWarehouse)==true){
			log.logWarning('SUCCESS: The expected Warehouse Name was returned')
		} else {
			log.logError('ERROR: The expected Warehouse Name was not returned')
			KeywordUtil.markFailed('ERROR: The expected Warehouse Name was not returned')
		}
	}
}


//Look for several Warehouse locations and verify results are returned
//Search by City Name
searchForWarehouse('ukiah', 'Project Warehouse #111 UKIAH, CA')
searchForWarehouse('tallahassee', 'Project Warehouse #222 TALLAHASSEE, FL')
searchForWarehouse('worcester', 'Project Warehouse #333 WORCESTER, MA')

//Search by Warehouse Number
searchForWarehouse('646', 'Project Warehouse #646 W MILWAUKEE, WI')
searchForWarehouse('997','Project Warehouse #997 BILOXI, MS')
searchForWarehouse('999','This Warehouse does not exist')

//Search by State
searchForWarehouse('NY','NY')
searchForWarehouse('AZ','AZ')
searchForWarehouse('TX','TX')
