package com.bitemii.distancecalc.util;

public class DistanceUtil {

	
	public static String removeDigitsInString(String distanceString) {
		return distanceString.replaceAll("[0-9]", "");
	}
}
