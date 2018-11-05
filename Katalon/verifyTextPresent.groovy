elementVisible = WebUI.verifyTextPresent('This is the Plan Summary ' + formattedDate + dateHour, false, FailureHandling.OPTIONAL)
if (elementVisible == false) {
	log.logError('ERROR: The Plan was not saved correctly')
	KeywordUtil.markFailedAndStop('ERROR: The Plan was not saved correctly')
} else {
	log.logWarning('A plan with the Sumamry of: This is the Plan Summary ' + formattedDate + dateHour + ' was located')
}
