package com.bitemii.distancecalc.service;
/**
 * @Auther Jeevan
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.JMException;

import com.bitemii.distancecalc.constants.BitemiiConstants;
import com.bitemii.distancecalc.model.AddressMatrix;
import com.bitemii.distancecalc.model.DistanceMatrix;
import com.bitemii.distancecalc.util.DistanceUtil;
import com.bitemii.distancecalc.util.DurationUtil;
import com.bitemii.distancecalc.util.GlobalUtils;
import com.google.gson.Gson;

public class DistanceCalculatorServiceImpl implements DistanceCalculatorService {
	static final Logger logger = Logger.getLogger("DistanceCalculatorServiceImpl");
	DistanceMatrix distanceMatrix = new DistanceMatrix();

	// Method to call google distance api and
	// convert the response object by calculating difference in distance and
	// duration along with total time and
	// convert to numeric/unit values from string
	public DistanceMatrix getDistanceAndDurationForSingleAddress(String sourceAddress, String destinationAddress,
			String apiKey) {

		String line, outputString = "";
		try {

			URL url = new URL(BitemiiConstants.G_MAPS_JSON_URL_WITH_ORIGINS
					+ GlobalUtils.convertAddressToGoogleAddressString(sourceAddress)
					+ BitemiiConstants.DESTINATION_PARAMETER
					+ GlobalUtils.convertAddressToGoogleAddressString(destinationAddress)
					+ BitemiiConstants.MODE_LANGUAGE_KEY_SELECTION_PARAMETERS + apiKey);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(BitemiiConstants.GET_METHOD);

			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = reader.readLine()) != null) {
				outputString += line;
			}
			if (null != outputString && outputString.contains(BitemiiConstants.ERROR_MESSAGE)) {
				throw new JMException("Error getting google Distance Matrix API Call ::---> " + outputString);

			}

			convertRespStringDataToValidFormatForDistAndDuration(outputString);

		} catch (MalformedURLException e) {
			logger.log(Level.FINER, "MalformedURLException!", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.log(Level.FINER, "IOException!", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.log(Level.FINER, outputString, e.getMessage());
			e.printStackTrace();
		}

		return distanceMatrix;
	}

	public void convertRespStringDataToValidFormatForDistAndDuration(String outputString) {
		distanceMatrix.setOutputJSONString(outputString);
		AddressMatrix response = new Gson().fromJson(outputString, AddressMatrix.class);
		distanceMatrix.setDistanceStringValue(response.getRows().get(0).getElements().get(0).getDistance().getText());
		distanceMatrix.setDurationStringValue(response.getRows().get(0).getElements().get(0).getDuration().getText());

		distanceMatrix.setDistanceValue(GlobalUtils.convertStringToNumeric(distanceMatrix.getDistanceStringValue()));
		distanceMatrix.setDistanceUnit(DistanceUtil.removeDigitsInString(distanceMatrix.getDistanceStringValue()));

		logger.info("distance::by DistanceMatrix::-->>  " + distanceMatrix.getDistanceStringValue());
		logger.warning("distance in float:: " + distanceMatrix.getDistanceValue());
		logger.warning("duration::by DistanceMatrix::-->>  " + distanceMatrix.getDurationStringValue());

		distanceMatrix
				.setTotalTimeInMinutes(distanceMatrix.getDurationStringValue().contains(BitemiiConstants.HOURS_STRING)
						? DurationUtil.extractHoursToCalcMins(distanceMatrix.getDurationStringValue()).getTime()
						: 0 + DurationUtil.extractMinsFromDuration(distanceMatrix.getDurationStringValue()).getTime());
		distanceMatrix.setDurationUnit(
				DurationUtil.extractHoursToCalcMins(distanceMatrix.getDurationStringValue()).getUnit().name());
		logger.info(
				"total time::" + distanceMatrix.getTotalTimeInMinutes() + "units::" + distanceMatrix.getDurationUnit());
	}

	// TO DO
	public AddressMatrix getDistanceAndDurationForMultipleAddress(String sourceAddress, String destinationAddress,
			String apiKey) {
		AddressMatrix addressMatrixResponse = null;
		String line, outputString = "";
		try {
			URL url = new URL(BitemiiConstants.G_MAPS_JSON_URL_WITH_ORIGINS
					+ GlobalUtils.convertAddressToGoogleAddressString(sourceAddress)
					+ BitemiiConstants.DESTINATION_PARAMETER
					+ GlobalUtils.convertAddressToGoogleAddressString(destinationAddress)
					+ BitemiiConstants.MODE_LANGUAGE_KEY_SELECTION_PARAMETERS + BitemiiConstants.API_KEY);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(BitemiiConstants.GET_METHOD);

			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = reader.readLine()) != null) {
				outputString += line;
			}

			if (null != outputString && outputString.contains(BitemiiConstants.ERROR_MESSAGE)) {
				throw new JMException("Error getting google Distance Matrix API Call ::---> " + outputString);

			}

			distanceMatrix.setOutputJSONString(outputString);
			addressMatrixResponse = new Gson().fromJson(outputString, AddressMatrix.class);
			distanceMatrix.setDistanceStringValue(
					addressMatrixResponse.getRows().get(0).getElements().get(0).getDistance().getText());
			distanceMatrix.setDurationStringValue(
					addressMatrixResponse.getRows().get(0).getElements().get(0).getDuration().getText());

			convertDistAndDurationFromStringToUnits();

		} catch (MalformedURLException e) {
			logger.log(Level.FINER, "MalformedURLException!", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.log(Level.FINER, "IOException!", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.log(Level.FINER, outputString, e.getMessage());
			e.printStackTrace();
		}

		return addressMatrixResponse;
	}

	public void convertDistAndDurationFromStringToUnits() {
		distanceMatrix.setDistanceUnit(DistanceUtil.removeDigitsInString(distanceMatrix.getDistanceStringValue()));

		logger.warning("distance::by DistanceMatrix::-->>  " + distanceMatrix.getDistanceStringValue());
		logger.warning("distance in float:: " + distanceMatrix.getDistanceValue());
		logger.warning("duration::by DistanceMatrix::-->>  " + distanceMatrix.getDurationStringValue());

		distanceMatrix
				.setTotalTimeInMinutes(distanceMatrix.getDurationStringValue().contains(BitemiiConstants.HOURS_STRING)
						? DurationUtil.extractHoursToCalcMins(distanceMatrix.getDurationStringValue()).getTime()
						: 0 + DurationUtil.extractMinsFromDuration(distanceMatrix.getDurationStringValue()).getTime());
		distanceMatrix.setDurationUnit(
				DurationUtil.extractHoursToCalcMins(distanceMatrix.getDurationStringValue()).getUnit().name());
		logger.warning(
				"total time::" + distanceMatrix.getTotalTimeInMinutes() + "units::" + distanceMatrix.getDurationUnit());
	}

	
}
