@Keyword
def checkPageLoad(String findText) {
	/* Confirm the correct page has loaded by looking for the requested text
	 * The search text is passed to the Keyword from the calling statement
	 * If the text is missing, determine the kind of error and display more meaningful text
	 */
      
	def elementVisible = WebUI.verifyTextPresent(findText, false, FailureHandling.OPTIONAL)
      
	if (elementVisible == false) {
		//Get the title of the page and determine the kind of error
		def title = WebUI.getWindowTitle()
		log.logError('ERROR: The requested page did not load with the following error:= ' + title )
		if (title=='There was an error'){
			log.logError('ERROR: There is a Data Inconsistency Error')
		} else {
			log.logError('ERROR: The page could not be found - 404 error')
		}
		log.logWarning('--- The test ended unexpectedly with errors ---')
		KeywordUtil.markFailedAndStop('ERROR: Unable to load the requested page - Test has failed')
	}
      
}
