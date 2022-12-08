package com.atlink.reservetickets.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class RestClient {

    public static void main(String[] args) {
        RestClient restClient = new RestClient();
        restClient.checkAvailableSeatsClient("D", "C", 10);
        restClient.reserveSeatsClient("A", "B", 5);
    }

    public void checkAvailableSeatsClient(String from, String to, int numberOfSeats) {
        try {

            URL url = new URL("http://localhost:8080/check?from=" + from + "&to=" + to + "&numberOfSeats=" + numberOfSeats);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            conn.disconnect();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    public void reserveSeatsClient(String from, String to, int seatId) {
        try {
            URL url = new URL("http://localhost:8080/reserve");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            String input = "{\"from\":\"" + from + "\",\"to\":\"" + to + "\",\"numberOfSeats\":\"" + seatId + "\"}";

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

//        if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED ||conn.getResponseCode() !=HttpURLConnection.HTTP_OK) {
//            throw new RuntimeException("Failed : HTTP error code : "
//                    + conn.getResponseCode());
//        }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            conn.disconnect();

        } catch (
                IOException e) {

            e.printStackTrace();

        }
    }
}
