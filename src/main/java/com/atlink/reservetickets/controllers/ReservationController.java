package com.atlink.reservetickets.controllers;

import com.atlink.reservetickets.dtos.ReserveSeatsRequest;
import com.atlink.reservetickets.dtos.ReserveSeatsResponse;
import com.atlink.reservetickets.dtos.AvailabilityResponse;
import com.atlink.reservetickets.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Thusitha Bandara
 * 2022-12-07
 */

@RestController
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @GetMapping("/check")
    public ResponseEntity<AvailabilityResponse> checkAvailableSeats(@RequestParam @Validated String from, @RequestParam @Validated String to, @RequestParam @Validated int numberOfSeats) {

        AvailabilityResponse seatsAvailabilityResponse = reservationService.getAvailabilityResponse(from.toUpperCase(), to.toUpperCase(), numberOfSeats);
        return new ResponseEntity<>(seatsAvailabilityResponse, HttpStatus.OK);
    }

    @PostMapping("/reserve")
    public ResponseEntity<ReserveSeatsResponse> reserveSeats(@RequestBody @Validated ReserveSeatsRequest reserveSeatsRequest) {
        ReserveSeatsResponse reserveSeatsResponse = reservationService.reserveTickets(reserveSeatsRequest.getFrom().toUpperCase().charAt(0), reserveSeatsRequest.getTo().toUpperCase().charAt(0), reserveSeatsRequest.getNumberOfSeats());

        if (reserveSeatsResponse.isReserved()) {
            return new ResponseEntity<>(reserveSeatsResponse, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(reserveSeatsResponse, HttpStatus.OK);
        }

    }
}
