Katalon - When doing calculations

Float.valueOf()
Integer.ValueOf()
mtdGrossProfitPlan.toInteger()
mtdGrossProfitPlan.toFloat()
float convertedNumber = Float.parseFloat(string)
float number = new Float(numberAsString).floatValue()
salesFigure = new BigDecimal(SeedSalesmetric)
String.valueOf(loop))
String phoneNumber=String.valueOf(areaCode) + String.valueOf(numPrefix) +String.valueOf(numSuffix)

//Use Big Decimal and Rounding
value1=12.54
value2=89.45

calculatedMargin=((value1 / value2) * 100).setScale(2, RoundingMode.UP)


Before adding values or performing a calculation, the values need to be changed to Integers. In the case of Margin values, they need to be changed to Float. 

Use this command before any math calculations on a value read from the site:

calculatedMargin=(((Float.valueOf(GP) / Float.valueOf(sales)) * 100).round(1))
sum=(((Integer.valueOf(GP) * Integer.valueOf(sales)) * 100))

This is another method
int ytdGrossProfitPlan = grossProfitPlanTotal.toInteger() + mtdGrossProfitPlan.toInteger()

I should probably have more lines that look like this, where the value is read, the $ and , are stripped and converted to an Integer:
// Read Daily Gross Profit Plan 
tempText = WebUI.getText(findTestObject('Daily Gross Profit Plan Value'))
tempText = tempText.replaceAll('[$,]', '')
siteDailyGrossProfit=Integer.valueOf(tempText)


While this works, it’s technically not accurate as it's dividing numbers which would lead to a decimal
It should perform the division as Float, then Round or Truncate to remove the decimal portion
int mtdGrossProfitPlan = (grossProfitCurrentMonth.toInteger() / totalBusinessDays.toInteger()) * currentBusinessDay.toInteger()

/*
BigDecimal
In some cases the dollar values were too large for Float values ($12,457,360.26). The values needed to be in BigDecimal Format
*/
salesFigure = new BigDecimal(SeedSalesmetric)

//Additionally

value1=25697325125.12
value2=35697325125.12

calculatedMargin=(((value1) / value2) * 100).setScale(2, RoundingMode.UP)
calculatedMargin=(((value1) + value2) * 100).setScale(2, RoundingMode.UP)

Truncate and Round:

def doubleValue = 12.5456d

assert 12.546d == doubleValue.round(3)
assert 13 == doubleValue.round()

assert 12 == doubleValue.trunc()
assert 12.54d == doubleValue.trunc(2)

def floatValue = 987.654f

assert 987.65f == floatValue.round(2)
assert 988 == floatValue.round()
assert 987.6f == floatValue.trunc(1)
assert 987 == floatValue.trunc()           
