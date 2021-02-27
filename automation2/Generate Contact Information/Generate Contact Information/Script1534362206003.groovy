import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory as CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as MobileBuiltInKeywords
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testcase.TestCaseFactory as TestCaseFactory
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testdata.TestDataFactory as TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository as ObjectRepository
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WSBuiltInKeywords
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUiBuiltInKeywords
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

/*
The purpose of this script is to make a random, but valid contact account for a website.
These can be used to fill in fields or run repeatedly to build a contact database within an application
The following fields are generated - Name, Company, Street Address, City, State, Zip, Phone,
Email Address-with a variety of extensions and lengths
It also contains code to provide the full state name such as North Carolina as well as NC
This code is site independent. The variables listed at the end need to be mapped to the appropriate fields.
It uses a variety of arrays to create user names, companies and street names.
The arrays can be shortened as needed.

This test populates the Global Variables that are defined for the project.
It is meant to be called from another test when new Contact information is needed
The values returned would be passed to input fields on the form.
As a stand alone test, it does not generate any output.
*/

def states=['AL','AK','AZ','AR','CA','CO','CT','DE','FL','GA','HI',
'ID','IL','IN','IA','KS','KY','LA','ME','MD','MA','MI','MN','MS',
'MO','MT','NE','NV','NH','NJ','NM','NY','NC','ND','OH','OK','OR',
'PA','RI','SC','SD','TN','TX','UT','VT','VA','WA','WV','WI','WY']

def stateName=['Alabama','Alaska','Arizona','Arkansas','California','Colorado','Connecticut','Delaware',
'Florida','Georgia','Hawaii','Idaho','Illinois','Indiana','Iowa','Kansas','Kentucky','Louisiana','Maine',
'Maryland','Massachusetts','Michigan','Minnesota','Mississippi','Missouri','Montana','Nebraska','Nevada',
'New Hampshire','New Jersey','New Mexico','New York','North Carolina','North Dakota','Ohio','Oklahoma',
'Oregon','Pennsylvania','Rhode Island','South Carolina','South Dakota','Tennessee','Texas','Utah','Vermont',
'Virginia','Washington','West Virginia','Wisconsin','Wyoming']

def cities=['Dentsville','Woodcreek','Las Lomitas','Kings Park','Willisville','Scottsboro','Owens Cross Roads','Gibson Flats',
'Abrams','Corcovado','Monette','Millican','Mabton','Montgomery Creek','Rawlins','McMullen','Rancho Alegre','Lake Lafayette',
'Hazel Crest','Whiterocks','Pocono Woodland Lakes','North Washington','Channahon','Hoyt','Darling','Zimmerman','Mayfield',
'Southbridge Town','Bedford','Camden Point','Underwood','Brocton','Stoystown','West Roy Lake','Lone Pine','Ogden','Sonora',
'Estill','North Creek','Wilber','Togiak','Western Lake','Beachwood','Wenona','Naponee','Spillertown','New Palestine','Crystal Lakes',
'Rockwood','McGregor','Captiva','Parkers Settlement','Montour Falls','Iron Belt','Port Byron','Estelle','Popejoy','Nerstrand','Leyner',
'Lubeck','The Dalles','Prathersville','De Pere','Mathiston','Swan','Brooktrails','Bevington','Neylandville','Oakland Park','Grace',
'Mellette','Fenton','Pewamo','Lake Zurich','Fort Pierce','Amherst','Celoron','High Shoals','Pukwana','Guffey','Kila','Isle of Hope',
'Hatteras','North Plainfield','Bessemer Bend','Macomb','Zuni Pueblo','Dogtown','Cullman','Petersburg','Morehead City','La Harpe','Desert Aire',
'Westboro','Thebes','Gallatin River Ranch','Gakona','Thorntonville','Wilmont','City View']

def streetName=['James Kaur','Grace Burns','Alfie Robertson','Harry Wilson','Steven Shaw','Jessica Palmer','Chloe Lowe','William Simpson',
'Amy Bell','Michael Campbell','Ruby Wilson','Sarah Smith','Laura Watson','Mary Stevens','Christina Gray','Charlie Reynolds','Olivia Green',
'Robert Ross','Oliver Wright','Lisa Stone','Emily Taylor','Amelia Jackson','Stephanie Roberts','Jessica Johnson','Julie Pearson','Mark Jones',
'Jason Davies','Sophie Reid','Matthew Kennedy','David Taylor','Lily Rose','Ella Mason','Jack Roberts','Thomas Cox','William Williams','Joshua Fox',
'Michelle Pearce','Daniel Parry','Joseph Adams','Kelly Chapman','Jennifer Carr','Brian Atkinson','Melissa Lloyd','Kimberly Khan','Angela Wood',
'Heather Harrison','Nicole Mitchell','Elizabeth Berry','Rebecca Hamilton','Jeffrey Lane','Amanda Ellis','Christopher Bailey','James Johnston',
'John Robinson','Daniel Wells','Kevin Graham','Eric Powell','Thomas Rogers','Richard Brown','Scott Morgan','Kelly Davies','Joseph West','Laura Alexander',
'Robert Newman','Oliver Clarke','Mary Mitchell','Michael Hopkins','Jack Ali','Scott Bennett','Kimberly Harris','Charlie Williams','Mark Evans',
'Daniel Murray','Grace Brown','Amanda James','Ruby James','Olivia Walker','Jennifer Butler','Brian Hunter','Emily Thomas','Jessica Mills','Stephanie Webb',
'Jessica Lewis','Harry Thomas','Sophie Thompson','Daniel Williamson','Chloe Parker','Lily White','Ella Jackson','Michelle Gill','Amelia Cooper','Thomas King',
'Joshua Lee','William Davis','Lisa Smith','James Khan','Alfie Matthews','Amy Martin','Kevin Poole','Rebecca Dawson']

def modernStreets=['South Moor Green','Hillyer Causeway','Faustina Street','Higley Hill','North Hecker Pass','McNair Arcade','Mulcerns Road',
'Edward Hart Quadrant','Marylebone High Hill','Bluff Edge','East Woodside Court','Egleston Mews','West Franrose Spur','Northeast Patocchi Townline',
'La Esperanza Grove North','Stanier Townline','East Ferndale Woods','East Polo Field Way','East Given Oval','South Shasta Close','Ware Woods Alley North',
'Hampden Gurney','Northwest Caldeira Loop','Chickenden Place','West Trunnel Route','West Charley Forest','Conco Walk','West Miowera Lawn','Lawnhurst Mall',
'North Kenhowe Road','Dersingham Heights','Leewater Gate','South Gabriele Green','Kirklinton Loop North','Aussie Lawn','Foxham Street West','Southeast Horsemoor View',
'West Casseday Garth','Bowler Walk','Nanlee Dell','Northeast Piney Terrace','South Bella Monte Loop','West Delmeade Arcade','Ravens Head East','Renwick Court',
'Whiteholme Quadrant','Bridge End Trace','Finlayson Circle','West Upper Court','South Bortfield Knoll','South Skinners Turn Loop','North Foxhunt Quadrant',
'Belburn Hill','West Offley Road','Riggs Hill Spur North','Lanfair Vale','South Ludell Mews','Genyn Byway','La Mar Grade West','Corvina Mews','Southwest Rounthorn Lane',
'Southeast Barksdale Bay','Southeast Princes Park','East Quinlisk Drive','East Sandalfoot View','West Brygger Close','East Woodland Beach Park','East Rotuma Circle',
'South American Legion Viaduct','Tradan Mews','Southeast Lamesa Nook','Hall Moss','East Lockewood Bypass','Fullbrooks Byway','Mira Flores North','West Berkhamsted Close',
'Brann Causeway','Southeast Molasses Run Green','West Carnbrook Walk','Eliz Arch','South Beeson Drive','North Halesworth Oval','East Queens Brigade','South Bountiful Mews',
'Linda Sue Lane','East Burnhill Garth','Martins Landing','Kilpatrick Gardens','North Automotive Garth','Eglington Causeway','Gap View Trace','Mitzy Drive',
'East Jaden Pathway','Lions Park Nook Southwest','West Carinya Spur','Basham Lawn','South Van Emburgh','South Kinlay Quay','North Greybury Nook','South Bullerthorpe Spur']

def streetSuffixes=['Pl','St','Dr','Rd','Ln','Ave','Blvd','Cir','Place','Street','Drive','Road','Lane','Avenue','Boulevard','Circle']

def properNames=['Imogen Hart','Kenneth Pearce','Joe Lewis','Alexander Mason','Joan Cunningham','Amy Dawson','Esther Day','Asuka James','Vincent Patel',
'Jeremias Dixon','Anna Russell','Laurence Watson','Jessica Kelly','Haruka Ali','Dylan Morgan','Clare Jordan','Rupert Thomas','Stuart Palmer','Kelly Martin',
'Edie Powell','Stan Watts','Raymond Jones','Terry Turner','Grace Rees','Claudia Davis','Gary Walker','Eve Pearson','Estel·la Richardson','Stephanie Taylor',
'Mike Barker','Steward Smith','Juan Hunt','Kate Richards','Cathleen Adams','Peter Clarke','Ethel Foster','Annabel Kennedy','Carrie Marshall','Ruby Andrews',
'Philippa Anderson','Jayne Jones','Bobby Chapman','Debora Taylor','Edna Willis','Abbygail Reid','Douglas Black','Miriam Williams','Gregory Ali','Jess Khan',
'Olivia Harper','Samantha Brown','Emily Davies','Maggie Price','Jim Johnson','Martin Booth','Jessica West','Daniel Moore','Keith Holmes','Zoe Hughes',
'Pamela Fisher','Bessie Phillips','Judith Knight','Susan Harris','Osamu Gibson','Guilermo King','Brenda Evans','Tatsuya Wilson','Oliver Parker','Benjamin Thomas',
'Nicholas Roberts','Sophie Jenkins','Lisa Spencer','Ruth Cooper','William Stewart','Chloe Scott','April Murphy','Fortunata Thompson','Aleix Simpson','Jason Cox',
'Dominic Johnson','Susanita Lewis','Kathy Rose','Harold Lawson','Ryan Robinson','Leonard Lowe','Lily Fletcher','Hector Wood','Ella White','Amelia Ross',
'Toshio Bailey','Mathew Jackson','Matthew Fraser','Jack Wright','Alan Chambers','Eddy Stone','Midori Rodriguez','Lottie Carter','Andrea Walsh','Gayle Roberts',
'Isobel Davies']

def companyNames=['Questa Inc','Bridal Warehouse','Jerry L. Williams','Hawaiian Jewelry Store Inc','Pacific Paradise Clothing CO','T-Shirt Expressions',
'James Butler','W W Group','Soonerclothes','Klassique Jewelers','American Indian Jewelry','Fantasy Diamond Corp','Dalia Koss Unique Jewelry','Petree and Stoudt Assoc',
'James Farley','Gerald Fairley','Trend K Inc','Twin City Motors','Industrial Uniform CO','Windsor Clock and Watch CO','Folklore','Pope Trucking Inc',
'R E Garrison Trucking CO','Ralph Meyers Trucking Inc','NU-Way Transportation Services','Thrift Trucking Inc','Copyrite Printing','The Learning Center',
'US Express And Logistics','Wynne Transport Svc Inc','Dani Lee Productions','Carroll Rehma Motors','Prime Floral Llc','Furness Trucking','Lincoln Chamber Of Commerce',
'Tkms','Wenk Associates','Ashton Transport','Flanary and Sons Trucking','Custom Computers','Maryland Messenger','Above and Beyond Courier Inc','Spry Moving',
'Als Moving Llc','Abracadabra Movers','Heat Express Inc','Meiers Moving and Storage','Allied Systems','Lanter Delivery Systems','A and J Remodeling and Sub-Contarcting',
'Transco Moving US Storage','A Local Van Moving and Stge Ltd','1st Choice Courier Distribution','Expressway Couriers','Spirit Relocation','A Friendly Mover Inc',
'C C Delivery Svc','Coast To Coast Express','Zkr Express Inc','Mdm Movers','Salisbury Storage Warehouse','Valid Mini Self Storage','Intercept Logistics',
'Birthworks Prenatal Education Inc.','Shear Movement','Chevalley Moving and Storage','Canyon State Courier','Valet Cleaners','Airborne Moving and Storage',
'ApartmentSource.com','Bridal Couture By Moni Rae','2 Fellas and A Big Vehicle','Sevde Relocation','Bill Burks','Polimark LLC','Daytech Mfg. Inc.','Champion Couriers',
'Dasco Transport Inc','Tidwell Business And Product Center','Executive Self Storage Assc','Mark Phillips','Old North State Quilts And Gifts','W C Software Services',
'Ryo Isogai','Mary St.','Orion Micro Systems','Bachmann Software','Cindy McClure','Derby Cycle Corporation Usa','Efx Research','Managesoft Corporation','Tectura',
'Zevnik Horton','Glen Gray','Ole Nc Bar-B-Que','Terry Ferns Inc','Stephen Gorman','Salerno Fund Raising','Raphael Larrinaga','Thistle Dew Llc']

def domainSuffixes=['.edu', '.org', '.net', '.com', '.gov', '.us', '.consulting', '.solutions', '.cloud', '.systems']

/*
Create a Phone Number
Phone Area Code and Prefix - 2 sets of 3 digit numbers. 201 is the lowest area code currently in use
*/

//Generate phone number
areaCode=Math.abs(new Random().nextInt(799)) + 200;
numPrefix=Math.abs(new Random().nextInt(899)) + 100;
numSuffix=Math.abs(new Random().nextInt(9000)) + 1000;
GlobalVariable.phoneNumber=String.valueOf(areaCode) + String.valueOf(numPrefix) + String.valueOf(numSuffix)

//Create zip code
GlobalVariable.zipCode=Math.abs(new Random().nextInt(90000)) + 10000;

//Create random Full Name
rndName=Math.abs(new Random().nextInt(properNames.size()));
GlobalVariable.fullName=properNames[rndName]

//Parse Full Name into First and Last Name
firstName=GlobalVariable.fullName.split(' ')[0]
lastName=GlobalVariable.fullName.split(' ')[1]

//Select a company name
rndCompanyName=Math.abs(new Random().nextInt(companyNames.size()));
GlobalVariable.companyName=companyNames[rndCompanyName]

//Select a domain suffix
rndDomain=Math.abs(new Random().nextInt(domainSuffixes.size()));
domainSuffix=domainSuffixes[rndDomain]

//Create a domain name from the company name and domain suffix
domainName=GlobalVariable.companyName.replaceAll(" ","") + domainSuffix

//Create an email address from first name, last name and company name
tempEmailAddress=firstName+'.'+lastName+'@'+domainName
GlobalVariable.emailAddress=tempEmailAddress.toLowerCase()

//Create house number 2 to 5 digits
GlobalVariable.houseAddress=Math.abs(new Random().nextInt(99999)) + 1;

//Create Street Address
rndAddr=Math.abs(new Random().nextInt(modernStreets.size()));
GlobalVariable.streetAddress=modernStreets[rndAddr]

//Pick a street suffix
rndStreetSuffix=Math.abs(new Random().nextInt(streetSuffixes.size()));
GlobalVariable.streetSuffix=streetSuffixes[rndStreetSuffix]

//Pick a city
rndCity=Math.abs(new Random().nextInt(cities.size()));
GlobalVariable.cityName=cities[rndCity]

//Pick a State Abbreviation
rndState=Math.abs(new Random().nextInt(states.size()));
GlobalVariable.stateAbbr=states[rndState]

//Pick a full state name
//rndStateName=Math.abs(new Random().nextInt(stateName.size()));
//fullStateName=stateName[rndStateName]
//Uses the same random number as the state above so they name and abbreviation match
GlobalVariable.fullStateName=stateName[rndState]