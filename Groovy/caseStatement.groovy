//Simple example to show how a SWITCH / CASE statement works
      int switchValue=3;
        switch(switchValue){
            case 1:
                System.out.println("Value was 1");
                break;
            case 2:
                System.out.println("Value was 2");
                break;
            case 3: case 4: case 5:
                System.out.println("Was 3,4 or 5");
                System.out.println("The actual value was " + switchValue);
                break;
            default:
                System.out.println("Was not 1 or 2");
                break;
        }
