package com.atlink.reservetickets.services;

import com.atlink.reservetickets.dtos.AvailabilityResponse;
import com.atlink.reservetickets.dtos.ReserveSeatsResponse;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

@SpringBootTest
@ActiveProfiles("test")
public class ReservationServiceTest {


    ReservationService reservationService = new ReservationService();

    @Test
    public void testReserveTickets_whenSeatsNotAvailable() {
        assertEquals(reservationService.reserveTickets('A', 'B', 41), ReserveSeatsResponse.builder().reserved(false).from("A").to("B").build());

    }

    @Test
    public void testReserveTickets_whenSeatsAvailable() {
        ReserveSeatsResponse reserveSeatsResponse = reservationService.reserveTickets('A', 'C', 5);
        assertTrue(reserveSeatsResponse.isReserved());
        assertNotNull(reserveSeatsResponse.getSeatsId());
        assertNotNull(reserveSeatsResponse.getReservedTime());
        assertNotNull(reserveSeatsResponse.getTicketId());
        assertEquals(reserveSeatsResponse.getFrom(), "A");
        assertEquals(reserveSeatsResponse.getTo(), "C");
        assertEquals(reserveSeatsResponse.getTotalPrice(), 500d);
    }

    @Test
    public void testReserveTickets_whenBtoCBookedSeatsBookedAtoB_thenVerifyBtoCBookedSeatAvailableForAtoB() {
        List<String> bcReserved = reservationService.reserveTickets('B', 'C', 5).getSeatsId();
        List<String> abReserved = reservationService.reserveTickets('A', 'B', 5).getSeatsId();
        assertEquals(abReserved, bcReserved);

    }

    @Test
    public void testGetAvailabilityResponse_whenSeatsAvailable_thenVerifyExpectedValue() {
        AvailabilityResponse availabilityResponse = reservationService.getAvailabilityResponse("A", "C", 4);
        assertTrue(availabilityResponse.isAvailability());
        assertEquals(availabilityResponse.getFrom(), "A");
        assertEquals(availabilityResponse.getTo(), "C");
        assertEquals(availabilityResponse.getPrice(), 400d);
        assertEquals(availabilityResponse.getNumberOfRequestedSeats(), 4);
    }

    @Test
    public void testGetAvailabilityResponse_whenSeatsAvailableAndReturnJourney_thenVerifyExpectedValue() {
        AvailabilityResponse availabilityResponse = reservationService.getAvailabilityResponse("D", "B", 4);
        assertTrue(availabilityResponse.isAvailability());
        assertEquals(availabilityResponse.getFrom(), "D");
        assertEquals(availabilityResponse.getTo(), "B");
        assertEquals(availabilityResponse.getPrice(), 400d);
        assertEquals(availabilityResponse.getNumberOfRequestedSeats(), 4);
    }
}