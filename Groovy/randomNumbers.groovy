Random rnd = new Random()
randomNumber = (1 + rnd.nextInt(15))
randomNumber = (10000 + rnd.nextInt(250000))

//Generate phone number
int areaCode=Math.abs(new Random().nextInt(799)) + 200;
int numPrefix=Math.abs(new Random().nextInt(899)) + 100;
int numSuffix=Math.abs(new Random().nextInt(9000)) + 1000;
