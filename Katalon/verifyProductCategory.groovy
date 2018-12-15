def verifyProductCategory(String divisionName, objectName){
	/* Confirm the category is correct for the user division
	 * @param divisionName - The division to check, should be List1 or List2
	 * @return - does not return a value
	 */
	KeywordLogger log = new KeywordLogger()
	List division1Categories=['','Pink Hearts', 'Yellow Moon', 'Orange Stars', 'Green Clovers', 'Blue Diamonds', 'Purple Horseshoes', 'Red Balloons', 'Green Trees', 'Rainbows', 'Blue Moons']
	List division2Categories=['','One Fish', 'Two Fish', 'Red Fish', 'Blue Fish', 'Black Fish', 'Clever Fish', 'Old Fish', New Fish', 'Green Eggs and Ham', 'Fox in Sox', 
	List categoryList=[]
	if (divisionName=='List1'){
		categoryList=division1Categories
	} else {
		categoryList=division2Categories
	}
	for (int loop = 1; loop <=10; loop++) {
		String tempText=WebUI.getText(findTestObject(objectName, [('row') : loop, ('column') : 1]))
		if (tempText!=categoryList[loop]){
			log.logError('ERROR: Category is incorrect. The category should be ' + categoryList[loop])
		}
		log.logWarning('Category: ' + tempText)
	}
}

//String katalonObject="productCategoryTableName"
//verifyProductCategory('List1', katalonObject)
