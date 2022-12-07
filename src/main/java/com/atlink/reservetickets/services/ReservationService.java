package com.atlink.reservetickets.services;

import com.atlink.reservetickets.dtos.AvailabilityResponse;
import com.atlink.reservetickets.dtos.ReserveSeatsResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Thusitha Bandara
 * 2022-12-07
 */


@Service
public class ReservationService {
    ConcurrentHashMap<String, String[]> destinationPointReserve = new ConcurrentHashMap<>();
    HashMap<String, Double> pointToPointPrice = new HashMap<>();

    private final String[] seatsIDs = new String[]{"1A", "1B", "1C", "1D", "2A", "2B", "2C", "2D", "3A", "3B", "3C", "3D", "4A", "4B", "4C", "4D", "5A", "5B", "5C", "5D", "6A", "6B", "6C", "6D", "7A", "7B", "7C", "7D", "8A", "8B", "8C", "8D", "9A", "9B", "9C", "9D", "10A", "10B", "10C", "10D"};

    private int availableSeats;

    public ReservationService() {
        destinationPointReserve.put("AB", new String[40]);
        destinationPointReserve.put("BC", new String[40]);
        destinationPointReserve.put("CD", new String[40]);
        destinationPointReserve.put("DC", new String[40]);
        destinationPointReserve.put("CB", new String[40]);
        destinationPointReserve.put("BA", new String[40]);

        pointToPointPrice.put("AB", 50d);
        pointToPointPrice.put("AC", 100d);
        pointToPointPrice.put("AD", 150d);
        pointToPointPrice.put("BC", 50d);
        pointToPointPrice.put("BD", 100d);
        pointToPointPrice.put("CD", 50d);
    }

    /**
     * @param from Journey start from
     * @param to   Journey to
     * @return route to the destination
     * we have to identify route to the destination
     * eg:-If journey start from-A to C it goes through A->B,B->C
     */
    public ArrayList<String> coveredRoutesByJourney(char from, char to) {
        ArrayList<String> routeToDestination = new ArrayList<>();
        if (from < to) {
            for (int i = from; i < to; i++) {
                routeToDestination.add(Character.toString(i) + Character.toString(i + 1));
            }
        } else if (from > to) {
            for (int j = from; j > to; j--) {
                routeToDestination.add(Character.toString(j) + Character.toString(j - 1));
            }
        }
        return routeToDestination;
    }


    /**
     * @param from       Journey start from
     * @param to         Journey to
     * @param numOfSeats number of required seats
     * @return total tickets price for the destination
     */
    public double calculatePrice(String from, String to, int numOfSeats) {
        String pointToPoint = from + to;
        return pointToPointPrice.get(pointToPoint) * numOfSeats;
    }

    /**
     * @return available seats count given destination
     */
    public int getAvailableSeatsCount() {
        return availableSeats;
    }


    /**
     * @param from       journey start from
     * @param to         journey to
     * @param numOfSeats number of required seats
     * @return boolean value if available return true otherwise false
     */
    public boolean checkAvailability(char from, char to, int numOfSeats) {
        String[] routesToDestination = coveredRoutesByJourney(from, to).toArray(new String[0]);
        int availableSeatsForGivenDestination = 0;
        boolean availability = false;
        for (int i = 0; i < 40; i++) {
            int seatAvailablePeriodForGivenRoutes = 0;
            for (String route : routesToDestination) {
                if (destinationPointReserve.get(route)[i] == null) {
                    seatAvailablePeriodForGivenRoutes++;
                }
            }
            //if seats available period equal to routes length mean, seat available for given destination
            if (seatAvailablePeriodForGivenRoutes == routesToDestination.length) {
                availableSeatsForGivenDestination++;
            }
        }
        // This will check available seats count for given destination is equal or greater than number of required seats
        // if available return true otherwise return false
        //variable availableSeats stand for SeatsAvailabilityResponse it includes the  number of seats available
        if (availableSeatsForGivenDestination >= numOfSeats) {
            availability = true;
            availableSeats = availableSeatsForGivenDestination;
        }
        return availability;
    }


    /**
     * To Reserve the tickets for given destination
     *
     * @param from          journey from
     * @param to            journey to
     * @param requiredSeats required seats
     * @param price         total amount
     * @return details of reserved seats
     */
    public synchronized ReserveSeatsResponse reserveTickets(char from, char to, int requiredSeats, double price) {
        Set<String> seatsId = new HashSet<>();
        String[] routesToDestination = coveredRoutesByJourney(from, to).toArray(new String[0]);
        int availableSeatsForGivenDestination = 0;
        if (checkAvailability(from, to, requiredSeats)) {
            String ticketId = UUID.randomUUID().toString();
            //variable i stand for index of seat
            for (int i = 0; i < 40; i++) {
                int seatAvailablePeriodForGivenRoutes = 0;
                int seatIndex = 0;

                for (String route : routesToDestination) {
                    // check seat availability for given routes null mean that seats available for given period(Route)
                    //  But we have to make sure seat available entire period of destination of journey
                    if (destinationPointReserve.get(route)[i] == null) {
                        seatAvailablePeriodForGivenRoutes++;
                        seatIndex = i;
                    }

                }
                //check seat availability till destination and booked the seat no need to worry because we already check availability of seats
                if (seatAvailablePeriodForGivenRoutes == routesToDestination.length) {
                    availableSeatsForGivenDestination++;

                    for (String route : routesToDestination) {

                        String[] seatsForRoute = destinationPointReserve.get(route);
                        //assigned the ticketId for given seats index
                        seatsForRoute[seatIndex] = ticketId;
                        seatsId.add(seatsIDs[seatIndex]);
                        destinationPointReserve.put(route, seatsForRoute);
                    }
                }

                if (availableSeatsForGivenDestination == requiredSeats) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();

                    return ReserveSeatsResponse.builder().reserved(true).from(String.valueOf(from)).to(String.valueOf(to)).seatsId(seatsId.stream().sorted().toList()).ticketId(ticketId).reservedTime(dtf.format(now)).totalPrice(price).build();
                }


            }
        }

        return ReserveSeatsResponse.builder().reserved(false).from(String.valueOf(from)).to(String.valueOf(to)).build();
    }

    public void print() {
        for (Map.Entry<String, String[]> set : destinationPointReserve.entrySet()) {
            System.out.println(set.getKey());
            System.out.println("-------->>>>>");
            for (int i = 0; i < set.getValue().length; i++) {
                System.out.print(set.getValue()[i] + " ");
            }
            System.out.println("-------->>>>>");
        }
    }

    /**
     * @param from       journey from
     * @param to         journey to
     * @param numOfSeats number of seats required
     * @return AvailabilityResponse
     */
    public AvailabilityResponse getAvailabilityResponse(String from, String to, int numOfSeats) {
        boolean availability = checkAvailability(from.charAt(0), to.charAt(0), numOfSeats);
        double price = calculatePrice(from, to, numOfSeats);
        int availableSeats = getAvailableSeatsCount();
        return new AvailabilityResponse.SeatsAvailabilityResponseBuilder().availability(availability).from(from).to(to).numberOfRequestedSeats(numOfSeats).price(price).numberOfAvailableSeats(availableSeats).build();
    }

}
