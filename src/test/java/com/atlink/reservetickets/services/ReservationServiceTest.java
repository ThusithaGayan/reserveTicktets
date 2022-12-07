package com.atlink.reservetickets.services;

import com.atlink.reservetickets.dtos.ReserveSeatsResponse;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.testng.Assert.*;

@SpringBootTest
@ActiveProfiles("test")
public class ReservationServiceTest {


    ReservationService reservationService = new ReservationService();

    @Test
    public void testCoveredRoutesByJourney() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("AB");
        arrayList.add("BC");
        Assert.assertEquals(arrayList, reservationService.coveredRoutesByJourney('A', 'C'));
    }

    @Test
    public void testCoveredRoutesByJourney_whenReturnJourney() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("DC");
        arrayList.add("CB");
        Assert.assertEquals(arrayList, reservationService.coveredRoutesByJourney('D', 'B'));
    }

    @Test
    public void testCalculatePrice_WhenGoesAtoB() {
        Assert.assertEquals(150d, reservationService.calculatePrice("A", "B", 3));
    }

    @Test
    public void testCheckAvailability_whenRequiredSeatsGreaterThanAvailableSeats() {
        assertFalse(reservationService.checkAvailability('A', 'B', 41));
    }

    @Test
    public void testCheckAvailability_whenRequiredSeatsLessThanAvailableSeats() {
        assertTrue(reservationService.checkAvailability('A', 'B', 30));
    }

    @Test
    public void testReserveTickets_whenSeatsNotAvailable() {
        assertEquals(ReserveSeatsResponse.builder().reserved(false).from("A").to("B").build(), reservationService.reserveTickets('A', 'B', 41, 150d));

    }

}