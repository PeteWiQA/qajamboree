import org.openqa.selenium.Keys as Keys

//This sends multiple strings in one command. It sends the text, then appends the Enter and Tab keys
WebUI.sendKeys(findTestObject('Dashboard/dropdown-Input'), Keys.chord('User Name',Keys.ENTER,Keys.TAB))

WebUI.sendKeys(findTestObject('Dashboard/dropdown-Input'), Keys.chord(Keys.SHIFT,'User Name',Keys.DOWN, Keys.UP,Keys.ENTER,Keys.TAB))

1) ENTER (1 key)
2) CTRL + A ( 2 keys)
3) CTRL + SHIFT + S (3 keys)

1.) WebUI.sendKeys(findTestObject('yourTestobject'), Keys.ENTER)
2.) WebUI.sendKeys(findTestObject('yourTestobject'), Keys.chord(Keys.CONTROL, 'a'))
3.) WebUI.sendKeys(findTestObject('yourTestobject'), Keys.chord(Keys.CONTROL,Keys.SHIFT, 'S'))


//Alternative method for SendKeys. Is a bit more reliable to clear the field before entering text
import org.openqa.selenium.interactions.Actions
for (loop = 1; loop <= 12; loop += 2) {
	WebUI.clearText(findTestObject('Season/input-Seasonality Change', [('Variable') : loop]))
	WebUI.setText(findTestObject('Season/input-Seasonality Change', [('Variable') : loop]),"7500")
	WebElement elem = driver.findElement(By.xpath("//div[@id='adjustSeasonality']/form/table/tbody/tr[" + (loop+1) +"]/td[4]/div/div[2]/input"))
	WebUI.clearText(findTestObject('Season/input-Seasonality Change', [('Variable') : loop+1]))
	act.sendKeys(elem, (Keys.chord(Keys.CONTROL, "a")))
	act.sendKeys(elem, (Keys.chord("-7500")))
	act.perform()
}

// Example from site:
    WebDriver driver = DriverFactory.getWebDriver()
    WebElement elem = driver.findElement(By.cssSelector("#input-id"))
    Actions act = new Actions(driver)

    act.sendKeys(elem, "stuff")
    act.perform()
