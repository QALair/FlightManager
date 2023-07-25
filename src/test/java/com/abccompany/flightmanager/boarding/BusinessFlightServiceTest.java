package com.abccompany.flightmanager.boarding;

import com.abccompany.flightmanager.flight.Flight;
import com.abccompany.flightmanager.flight.FlightCategory;
import com.abccompany.flightmanager.passenger.Passenger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class BusinessFlightServiceTest {

    Flight flight;
    Passenger passenger;
    BusinessFlightService bFS;

    @BeforeEach
    void setup(){
        flight = new Flight();
        passenger = new Passenger();
        bFS = new BusinessFlightService();

        flight.setId("F2");
        flight.setPassengers(new HashSet<>());
        flight.setCategory(FlightCategory.BUSINESS);

        passenger.setId("P2");
        passenger.setName("Joao Netto");
        passenger.setVip(true);
    }

    @Test
    void addPassenger() {
        bFS.addPassenger(flight,passenger);
        assertEquals(1,flight.getPassengers().size());
    }

    @Test
    void removePassenger() {
        bFS.removePassenger(flight,passenger);
        assertEquals(0, flight.getPassengers().size());
    }
}