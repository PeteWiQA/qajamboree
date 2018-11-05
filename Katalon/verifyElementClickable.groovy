isClickable=WebUI.verifyElementClickable(findTestObject('link-Name'))
if (isClickable==true){
	//On the Home Page, click the first link
	WebUI.click(findTestObject('link-To Dashboard'))
	//Wait for the word, Dashboard to appear, indicating the Sales Dashboard has been loaded
	elementPresent=WebUI.waitForElementPresent(findTestObject('Dashboard/Dashboard Header Text'), 20)
	if (elementPresent==true){
		log.logWarning('SUCCESS: The Home Page Dashboard link correctly loads the Sales Dashboard')
	} else {
		log.logError('ERROR: The Home Page Dashboard link was clickable, but the Sales Dashboard did not load')
	}
} else {
	log.logError('ERROR: The Sales Dashboard is not clickable and should be checked')
	KeywordUtil.markFailed('ERROR: The Sales Dashboard is not clickable and should be checked')
}
