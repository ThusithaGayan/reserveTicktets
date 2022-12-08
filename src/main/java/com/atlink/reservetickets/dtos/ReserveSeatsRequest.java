package com.atlink.reservetickets.dtos;

/**
 * @author Thusitha Bandara
 * 2022-12-07
 */
public class ReserveSeatsRequest {
    private String from;
    private String to;
    private int numberOfSeats;

    public ReserveSeatsRequest() {
    }

    public ReserveSeatsRequest(String from, String to, int numberOfSeats, double price) {
        this.from = from;
        this.to = to;
        this.numberOfSeats = numberOfSeats;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }
}
