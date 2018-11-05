//OR Condition	

  if ((calculatedMargin.isNaN()==true) || (calculatedMargin.isInfinite()==true)){
		calculatedMargin=0.0
	}

//AND Condition
	if ((calculatedMargin.isNaN()==true) && (calculatedMargin.isInfinite()==true)){
		calculatedMargin=0.0
	}
