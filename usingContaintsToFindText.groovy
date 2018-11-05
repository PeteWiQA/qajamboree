//Convert the name and search criteria to lowercase to make sure they match
tempResult=recordName.toLowerCase().contains(searchCriteria.toLowerCase())

For this example, "searchCriteria" is the variable that contains my string, "limited"

I then put that to a simple test to confirm the results and output an error if the text didn't match.

//Confirm the company name contains the search string
	if(tempResult!=true){
	log.logError('ERROR: The search result does not match the search criteria')
	KeywordUtil.markFailed('ERROR: The search result does not match the search criteria')
	} else {
		log.logWarning('SUCCESS: The search result matches the search criteria')
	}
  
  //Example to find text in a string from Akrimax
  //Lower 48
if (tempText.contains('Lower 48 = ' + baseIncrease)!=true){
	log.logError('ERROR: The base increase is not correct')
} else {
	log.logWarning('SUCCESS: The base increase is the appropriate value')
}
