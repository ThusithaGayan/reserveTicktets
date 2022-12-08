package com.atlink.reservetickets.dtos;

/**
 * @author Thusitha Bandara
 * 2022-12-07
 */

public class AvailabilityResponse {

    private String from;
    private String to;
    private boolean availability;
    private double price;
    private int numberOfRequestedSeats;


    private AvailabilityResponse(SeatsAvailabilityResponseBuilder seatsAvailabilityResponseBuilder) {
        this.availability = seatsAvailabilityResponseBuilder.availability;
        this.numberOfRequestedSeats = seatsAvailabilityResponseBuilder.numberOfRequestedSeats;
        this.from = seatsAvailabilityResponseBuilder.from;
        this.to = seatsAvailabilityResponseBuilder.to;
        this.price = seatsAvailabilityResponseBuilder.price;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public boolean isAvailability() {
        return availability;
    }

    public double getPrice() {
        return price;
    }

    public int getNumberOfRequestedSeats() {
        return numberOfRequestedSeats;
    }

    public static class SeatsAvailabilityResponseBuilder {
        private String from;
        private String to;
        private boolean availability;
        private double price;
        private int numberOfRequestedSeats;
        private int numberOfAvailableSeats;

        public SeatsAvailabilityResponseBuilder from(String from) {
            this.from = from;
            return this;
        }

        public SeatsAvailabilityResponseBuilder to(String to) {
            this.to = to;
            return this;
        }

        public SeatsAvailabilityResponseBuilder availability(boolean availability) {
            this.availability = availability;
            return this;
        }

        public SeatsAvailabilityResponseBuilder price(double price) {
            this.price = price;
            return this;
        }

        public SeatsAvailabilityResponseBuilder numberOfRequestedSeats(int numberOfRequestedSeats) {
            this.numberOfRequestedSeats = numberOfRequestedSeats;
            return this;
        }

        public AvailabilityResponse build() {
            return new AvailabilityResponse(this);
        }
    }
}
