package com.abccompany.flightmanager.boarding;


import com.abccompany.flightmanager.flight.Flight;
import com.abccompany.flightmanager.flight.FlightCategory;
import com.abccompany.flightmanager.passenger.Passenger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class EconomicFlightServiceTest {
    EconomicFlightService eFS;
    Flight ecoFlight = new Flight();
    Passenger ecoPassenger = new Passenger();
    Passenger vipPassenger = new Passenger();

    @BeforeEach
    void setup(){
        ecoFlight.setId("F2");
        ecoFlight.setPassengers(new HashSet<>());
        ecoFlight.setCategory(FlightCategory.ECONOMIC);

        ecoPassenger.setId("P2");
        ecoPassenger.setName("Joao Netto");
        ecoPassenger.setVip(false);

        vipPassenger.setId("P3");
        vipPassenger.setName("Joao T. Netto");
        vipPassenger.setVip(true);
    }

    @Test
    void addPassenger() {
        eFS = new EconomicFlightService();
        eFS.addPassenger(ecoFlight, ecoPassenger);
        assertEquals(1, ecoFlight.getPassengers().size());
    }

    @Test
    void removePassenger() {
        eFS = new EconomicFlightService();

        eFS.removePassenger(ecoFlight, ecoPassenger);
        assertEquals(0, ecoFlight.getPassengers().size());
    }

    @Test
    @DisplayName("Remove vip passenger from eco flight")
    void removeVipPassengerFromEcoFlight(){
        eFS = new EconomicFlightService();
        eFS.addPassenger(ecoFlight, vipPassenger);
        eFS.removePassenger(ecoFlight, vipPassenger);
        assertEquals(1, ecoFlight.getPassengers().size());
    }
}