//The following Replace command should be used and removes all non-numeric characters
//This includes removing newline and CRLF from text which is present for ABC and Pioneer
//Note the pipe | separator. This combines removeText with the rest of the regex
tempText=tempText.replaceAll(removeText + "|[^0-9.]","").toDouble() //will preserve numbers after the decimal
tempText=tempText.replaceAll(removeText + "|[^\\d.]","").toDouble()
//The removeText at the beginning is can be used to specify specific text to remove that contains numbers
tempText="Showing 1 to 8 of \$5,232,574 entries"
removeText="Showing 1 to 8 of"
tempText=tempText.replaceAll(removeText + "|[^0-9]","").toInteger() //Should be used to Integer values
tempText=tempText.replaceAll(removeText + "|[^0-9]","").toInteger()
tempText=tempText.replaceAll("1 to 8|[^0-9]","").toInteger() //Entering the text to remove without a variable


Replace $
tempText=tempText.replaceAll("[\$,]", "")

Replace Back Slashes \\
businessDays = parsedDate[1].replaceAll('\\)', '')

Replace multiple items on a single line - Use the pipe || if needed
variable = variable.replaceAll('\\n|\\r', ' - ')

//Pass a variable to the replace statement. It uses the pipe delimeter and uses .trim() to remove whitespace.
    text="dollars"
		tempText="\$5,472,184 dollars"
		results=tempText.replaceAll('[\$]|'+text,'').trim()
		println (results)

.replaceAll('\\n|\\r|'+filterListNames[loop], '')
  	//The text reads as - Showing 1 to 8 of Showing 1 to 8 of 2,056 entries
    paginationResults=tempText.replaceAll(" entries", "")
	  paginationResults=WebUI.getText(findTestObject('Object, [('index') : loop])).replaceAll('Showing 1 to 8 of |  entries', "")
