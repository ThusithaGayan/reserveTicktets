package com.atlink.reservetickets.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Thusitha Bandara
 * 2022-12-07
 */

@Data
@Builder
public class ReserveSeatsResponse {
    private String ticketId;
    private boolean reserved;
    private String from;
    private String to;
    private List<String> seatsId;
    private double totalPrice;
    private String reservedTime;

}
