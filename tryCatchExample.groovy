//Look for the text, Daily Sales Detail on the Page.
//If it's not there, there is a problem loading the page and the test should exit as the rest of the steps will fail
try {
	elementPresent=WebUI.verifyTextPresent("Daily Sales Detail", false)
}

catch (Exception e) {
	title = WebUI.getWindowTitle()
	log.logWarning('ERROR: The title of the Sales Detail Page is:=' + title)
	throw new AssertionError('ERROR: The Sales Detail Page did not load correctly', e)
}



https://www.tutorialspoint.com/groovy/groovy_exception_handling.htm
It might be a good idea to employ the ex.toString() idea to capture the text of an error.
class Example {
   static void main(String[] args) {
      try {
         def arr = new int[3];
         arr[5] = 5;
      }catch(ArrayIndexOutOfBoundsException ex) {
         println(ex.toString());
         println(ex.getMessage());
         println(ex.getStackTrace());  
      } catch(Exception ex) {
         println("Catching the exception");
      }finally {
         println("The final block");
      }
		
      println("Let's move on after the exception");
   } 
}
