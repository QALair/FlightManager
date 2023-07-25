package com.abccompany.flightmanager.boarding;

import com.abccompany.flightmanager.flight.Flight;
import com.abccompany.flightmanager.flight.FlightCategory;
import com.abccompany.flightmanager.flight.FlightRepository;
import com.abccompany.flightmanager.passenger.Passenger;
import com.abccompany.flightmanager.passenger.PassengerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashSet;
import java.util.Optional;

import org.assertj.core.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BoardingController.class)
class BoardingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightRepository flightRepository;

    @MockBean
    private PassengerRepository passengerRepository;

    @Autowired
    BoardingController boardingController;

    Flight ecoFlight = new Flight();
    Flight busFlight = new Flight();
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

        busFlight.setId("F3");
        busFlight.setPassengers(new HashSet<>());
        busFlight.setCategory(FlightCategory.BUSINESS);

        vipPassenger.setId("P3");
        vipPassenger.setName("Joao T. Netto");
        vipPassenger.setVip(true);
    }

    @Test
    void addPassengerTest() throws Exception {
        when(flightRepository.findById(ecoFlight.getId()))
        .then(invocationOnMock -> Optional.of(ecoFlight));

        when(passengerRepository.findById(ecoPassenger.getId()))
                .then(invocationOnMock -> Optional.of(ecoPassenger));

        mockMvc.perform(MockMvcRequestBuilders.post("/boarding/{flightId}/{passengerId}", ecoFlight.getId(), ecoPassenger.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    void addEcoPassengerToBusinessFlight() throws Exception {
        when(flightRepository.findById(busFlight.getId()))
                .then(invocationOnMock -> Optional.of(busFlight));

        when(passengerRepository.findById(ecoPassenger.getId()))
                .then(invocationOnMock -> Optional.of(ecoPassenger));

        mockMvc.perform(MockMvcRequestBuilders.post("/boarding/{flightId}/{passengerId}", busFlight.getId(), ecoPassenger.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(406));
    }

    @Test
    void addVipPassengerToBusinessFlight() throws Exception {
        when(flightRepository.findById(busFlight.getId()))
                .then(invocationOnMock -> Optional.of(busFlight));

        when(passengerRepository.findById(vipPassenger.getId()))
                .then(invocationOnMock -> Optional.of(vipPassenger));

        assertThat(mockMvc.perform(MockMvcRequestBuilders.post("/boarding/{flightId}/{passengerId}", busFlight.getId(), vipPassenger.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getStatus()).isEqualTo(202);
    }

    @Test
    @DisplayName("Add a vip passenger to an economic flight")
    void addVipPassengerToEconomicFlight() throws Exception{
        when(flightRepository.findById(any()))
                .thenReturn(Optional.of(ecoFlight));

        when(passengerRepository.findById(any()))
                .thenReturn(Optional.of(vipPassenger));

        assertThat(mockMvc.perform(MockMvcRequestBuilders.post("/boarding/{flightId}/{passengerId}", ecoFlight.getId(), vipPassenger.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getStatus()).isEqualTo(202);
    }

    @Test
    @DisplayName("Add a passenger to a non existant flight")
    void addAPassengerToNonExistantFlight() throws Exception {
        when(flightRepository.findById("E1"))
                .thenReturn(Optional.of(busFlight));

        when(passengerRepository.findById(any()))
                .thenReturn(Optional.of(vipPassenger));

        assertThat(mockMvc.perform(MockMvcRequestBuilders.post("/boarding/{flightId}/{passengerId}", "B1", vipPassenger.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getStatus()).isEqualTo(409);
    }

    @Test
    @DisplayName("Add a non existent passenger to a flight")
    void addANonExistentPassengerToAFlight() throws Exception {
        when(flightRepository.findById(any()))
                .thenReturn(Optional.of(busFlight));

        when(passengerRepository.findById("A3"))
                .thenReturn(Optional.of(vipPassenger));

        assertThat(mockMvc.perform(MockMvcRequestBuilders.post("/boarding/{flightId}/{passengerId}", busFlight.getId(), "A1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getStatus()).isEqualTo(409);
    }

    @Test
    @DisplayName("Remove economic passenger from economic flight")
    void rmvAnEcoPassengerFromEcoFlight() throws Exception {
        when(flightRepository.findById(any()))
                .thenReturn(Optional.of(ecoFlight));
        when(passengerRepository.findById(any()))
                .thenReturn(Optional.of(ecoPassenger));
        addPassengerTest();

        assertThat(mockMvc.perform(MockMvcRequestBuilders.delete("/boarding/{flightId}/{passengerId}", ecoFlight.getId(), ecoPassenger.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()));
    }

    @Test
    @DisplayName("Remove a passenger from a non existent flight")
    void rmvAnEcoPassengerFromNonExistentFlight() throws Exception {
        when(flightRepository.findById(ecoFlight.getId()))
                .thenReturn(Optional.of(ecoFlight));
        when(passengerRepository.findById(any()))
                .thenReturn(Optional.of(ecoPassenger));
        addPassengerTest();

        assertThat(mockMvc.perform(MockMvcRequestBuilders.delete("/boarding/{flightId}/{passengerId}", "E9", ecoPassenger.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict()));
    }

    @Test
    @DisplayName("Remove a non existent passenger from an eco flight")
    void rmvANonExistentPassengerFromAnEcoFlight() throws Exception {
        when(flightRepository.findById(any()))
                .thenReturn(Optional.of(ecoFlight));
        when(passengerRepository.findById(ecoPassenger.getId()))
                .thenReturn(Optional.of(ecoPassenger));
        addPassengerTest();

        assertThat(mockMvc.perform(MockMvcRequestBuilders.delete("/boarding/{flightId}/{passengerId}", ecoFlight.getId(), "B9")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict()));
    }

    @Test
    @DisplayName("Dont remove Passenger")
    void dontRemovePassenger() throws Exception {
        when(flightRepository.findById(ecoFlight.getId()))
                .thenReturn(Optional.of(ecoFlight));
        when(passengerRepository.findById(ecoPassenger.getId()))
                .thenReturn(Optional.of(ecoPassenger));

        assertThat(mockMvc.perform(MockMvcRequestBuilders.delete("/boarding/{flightId}/{passengerId}", ecoFlight.getId(), ecoPassenger.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable()));
    }
}