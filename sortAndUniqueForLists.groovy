//Lists
numberList = [87453, 974639, 6394, 187298, 435, 205735, 18, 43298, 11340625]
nameList=['Tangerine','Orange','Carrot','Watermelon','Pear','Grape','Banana']
repeatedList=['to','too','two','for','four','fore','too','four','four','for','two']

//Sort Ascending
numberList=numberList.toSorted()
nameList=nameList.toSorted()
log.logWarning('Ascending:=' + numberList)
log.logWarning('Ascending:=' + nameList)

//Sort Descending
numberList=numberList.reverse()
nameList=nameList.reverse()
log.logWarning('Descending:=' + numberList)
log.logWarning('Descending:=' + nameList)

//Unique List
repeatedList=repeatedList.toUnique()
log.logWarning('Unique List:=' + repeatedList)

Output:
Ascending:=[18, 435, 6394, 43298, 87453, 187298, 205735, 974639, 11340625]
Ascending:=[Banana, Carrot, Grape, Orange, Pear, Tangerine, Watermelon]
Descending:=[11340625, 974639, 205735, 187298, 87453, 43298, 6394, 435, 18]
Descending:=[Watermelon, Tangerine, Pear, Orange, Grape, Carrot, Banana]
Unique List:=[to, too, two, for, four, fore]
