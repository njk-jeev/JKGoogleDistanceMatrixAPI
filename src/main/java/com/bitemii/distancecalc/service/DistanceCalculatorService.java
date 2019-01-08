package com.bitemii.distancecalc.service;
/**
 * @Auther Jeevan
 */

import com.bitemii.distancecalc.model.AddressMatrix;
import com.bitemii.distancecalc.model.DistanceMatrix;

public interface DistanceCalculatorService {

	
	
	/**
     * @param sourceAddress
     * @param destinationAddress
     * @param apiKey
     * @return
     */

	public DistanceMatrix  getDistanceAndDurationForSingleAddress(String sourceAddress, String destinationAddress, String apiKey);
	
	/**
     * @param sourceAddress
     * @param destinationAddress
     * @param apiKey
     * @return
     */

	public AddressMatrix getDistanceAndDurationForMultipleAddress(String sourceAddress, String destinationAddress, String apiKey);
	
	
	
}
