@Keyword
//Function to determine if an item exists on the page using WebUiCommonHelper.findWebElement
//Accepts a "critical" flag to determine if the test should exit if an object isn't found
boolean verifyObjectPresent(String objectReference, boolean critical) {
  try {
    WebUiCommonHelper.findWebElement(findTestObject(objectReference),5)
    return true;
  } catch (Exception e) {
    if (critical==true){
      log.logWarning("The object with the name, " + objectReference + " was not found. Execution Halted.")
      KeywordUtil.markFailedAndStop("ERROR: The object with the name, " + objectReference + " was not found. Exiting Test.")
    } else {
      log.logWarning("The object with the name, " + objectReference + " was not found.")
      return false;
    }
  }
}
