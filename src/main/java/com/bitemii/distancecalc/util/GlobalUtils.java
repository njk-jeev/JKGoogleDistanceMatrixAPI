package com.bitemii.distancecalc.util;

import java.util.logging.Logger;

import com.bitemii.distancecalc.constants.BitemiiConstants;

public class GlobalUtils {
	 static final Logger logger = Logger.getLogger("DistanceCalculatorServiceImpl");
	
	public static void setDouble(String result) {
	      String res[]=result.split(",");
	      Double min=Double.parseDouble(res[0])/60;
	      int dist=Integer.parseInt(res[1])/1000;
	      String duration,distance;
	      duration = ("Duration= " + (int) (min / 60) + " hr " + (int) (min % 60) + BitemiiConstants.MINS_STRING);
	      distance= ("Distance= " + dist + BitemiiConstants.KILOMETERS);
	      logger.warning("duration:: "+duration+ " \n distance:: "+distance);

	  }
	  
	  public static String convertAddressToGoogleAddressString(String inputAddress) {
		  String googleFormatAddr = inputAddress.replaceAll(" ", "+");
		  return googleFormatAddr;
	  }
	  
	  public static Float convertStringToNumeric(String inputString) {
		 return Float.parseFloat(inputString.replaceAll("[^\\d.]",""));
	  }
}
