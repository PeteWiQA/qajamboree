mydate = new Date()  //Must be specified before working with Date functions
println(mydate)

formattedDate = mydate.format("MM")
println(Integer.valueOf(formattedDate))


formattedDate = mydate.format("MM:dd:yyy")
formattedDate = mydate.format("MM/dd/yyy")
println(formattedDate)

formattedDate = mydate.format("EEEE")
println(formattedDate)

formattedDate = mydate.format("dd")
println(formattedDate)

formattedDate = mydate.format("MM")
println(formattedDate)

formattedDate = mydate.format("MMM") //Nov - 3 letter abbreviation
println(formattedDate)

formattedDate = mydate.format("MMMM") //November - Full word of the month
println(formattedDate)

formattedDate = mydate.format("yyy")
println(formattedDate)

//Shows whether it is AM or PM
formattedDate = mydate.format("a")
println(formattedDate)

//Adds AM or PM to the end of the time
dateHour=mydate.format("hh:mm:ss a")
println(dateHour)

dateHour=mydate.format("HH:mm:ss")
println(dateHour)

//Format Date and Time to be used at the end of Item titles such as Quotes and Notes
mydate = new Date()
formattedDate = mydate.format("ddMMyyy")
dateHour=mydate.format("HHmmss")
+ formattedDate + dateHour

today = new Date()

yesterday = today.previous()
println('Yesterday:=' + yesterday)
reportDate = yesterday.format('MM/dd/yyyy')
println('Yesterday:=' + reportDate)

todayDate = today.format('MM/dd/yyyy')

reportDate = yesterday.format('MM/dd/yyyy')

dayOfWeek = today.format('EEEE')

if (dayOfWeek == 'Monday') {
    yesterday = (today - 3)

    print('Yesterday is:' + yesterday)

    reportDate = yesterday.format('MM/dd/yyyy')

    println('The new import date is:' + reportDate)
}
