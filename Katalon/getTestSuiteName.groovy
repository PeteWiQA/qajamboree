testSuite.getTestSuiteId()
TestSuiteContext
TestCaseContext


You can use Test Listeners in this case to get current test case as I can see it provides an API for you to get current test case ID already:

class NewTestListener {
    /**
     * Executes before every test case starts.
     * @param testCaseContext related information of the executed test case.
     */
    @BeforeTestCase
    def sampleBeforeTestCase(TestCaseContext testCaseContext) {
        println testCaseContext.getTestCaseId()
    }

    /**
     * Executes after every test case ends.
     * @param testCaseContext related information of the executed test case.
     */
    @AfterTestCase
    def sampleAfterTestCase(TestCaseContext testCaseContext) {
        println testCaseContext.getTestCaseId()
    }
    
    
class NewTestListener {
    /**
     * Executes before every test case starts.
     * @param testCaseContext related information of the executed test case.
     */
    @BeforeTestCase
    def sampleBeforeTestCase(TestCaseContext testCaseContext) {
        println testCaseContext.getTestCaseId()
    }

    /**
     * Executes after every test case ends.
     * @param testCaseContext related information of the executed test case.
     */
    @AfterTestCase
    def sampleAfterTestCase(TestCaseContext testCaseContext) {
        println testCaseContext.getTestCaseId()
    }
    
    
    
    
    
class NewTestListener {
  /**
   * Executes before every test case starts.
   * @param testCaseContext related information of the executed test case.
   */
  @BeforeTestCase
  def sampleBeforeTestCase(TestCaseContext testCaseContext) {
    println "testCaseContext.getTextCaseId()=${testCaseContext.getTestCaseId()}"
    GlobalVariable.currentTestCaseId = testCaseContext.getTestCaseId()
  }
  
  
WebUI.comment(">>> GlobalVariable.currentTestCaseId is ${GlobalVariable.currentTestCaseId}") 
