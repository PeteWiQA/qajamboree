// Reference: [How do I use NumberFormat to format currencies? - Web Tutorials - avajava.com](http://www.avajava.com/tutorials/lessons/how-do-i-use-numberformat-to-format-currencies.html)

import java.text.NumberFormat
import java.util.Locale

KeywordLogger log = new KeywordLogger()
NumberFormat defaultFormat = NumberFormat.getCurrencyInstance()

log.logWarning('Currency Value:' + defaultFormat.format(sumTotal))

//Additional example
double num = 1323.526;
log.logWarning("The currency value is: " + defaultFormat.format(num));
