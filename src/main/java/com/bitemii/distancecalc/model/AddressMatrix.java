package com.bitemii.distancecalc.model;

import java.util.List;

public class AddressMatrix {

	List<Rows> rows;
	private List<String> destination_addresses;
	private List<String> origin_addresses;

	public List<String> getDestination_addresses() {
		return destination_addresses;
	}

	public List<String> getOrigin_addresses() {
		return origin_addresses;
	}

	public List<Rows> getRows() {
		return rows;
	}

	public static class Rows {
		List<Element> elements;

		public List<Element> getElements() {
			return elements;
		}
	}

	public class Element {

		Distance distance;
		Duration duration;

		public Distance getDistance() {
			return distance;
		}

		public Duration getDuration() {
			return duration;
		}
	}

}