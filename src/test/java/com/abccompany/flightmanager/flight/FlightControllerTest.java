package com.abccompany.flightmanager.flight;

import com.abccompany.flightmanager.boarding.BoardingController;
import com.abccompany.flightmanager.passenger.Passenger;
import com.abccompany.flightmanager.passenger.PassengerDto;
import com.abccompany.flightmanager.passenger.PassengerRepository;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;

@WebMvcTest(FlightController.class)
class FlightControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private FlightRepository flightRepository;

    @MockBean
    private PassengerRepository passengerRepository;

    @Autowired
    FlightController flightController;

    Flight ecoFlight = new Flight();
    Flight busFlight = new Flight();
    Passenger ecoPassenger = new Passenger();
    Passenger vipPassenger = new Passenger();
    FlightDto flightDto = new FlightDto();

    PassengerDto passengerDto = new PassengerDto();

    Gson gson;
    GsonBuilder builder;

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

        passengerDto.setId(ecoPassenger.getId());
        passengerDto.setVip(ecoPassenger.isVip());
        passengerDto.setName(ecoPassenger.getName());

        flightDto.setId(ecoFlight.getId());
        flightDto.setCategory(ecoFlight.getCategory());
        flightDto.passengers = new ArrayList<>();
        flightDto.passengers.listIterator().add(passengerDto);
    }

    @Test
    @DisplayName("Get All Flights")
    void getAllFlightsTest() throws Exception {
        when(flightRepository.findById(any()))
                .thenReturn(Optional.of(ecoFlight));
        
        mockMvc.perform(MockMvcRequestBuilders.get("/flight")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Create Flight")
    void createFlightTest() throws Exception {
        builder = new GsonBuilder();
        gson = builder.create();

        when(flightRepository.findById(any()))
                .thenReturn(Optional.empty());

        when(flightRepository.save(any()))
                .thenReturn(ecoFlight);

        mockMvc.perform(MockMvcRequestBuilders.post("/flight")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(gson.toJson(flightDto))
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Cant create flight because it is duplicated")
    void createDuplicatedFlightAndFail() throws Exception {
        builder = new GsonBuilder();
        gson = builder.create();

        when(flightRepository.findById(any()))
                .thenReturn(Optional.of(ecoFlight));

        when(flightRepository.save(any()))
                .thenReturn(ecoFlight);

        mockMvc.perform(MockMvcRequestBuilders.post("/flight")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(gson.toJson(flightDto))
        ).andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Get an existing flight")
    void getAnExistingFlight() throws Exception {
        when(flightRepository.findById(any()))
                .thenReturn(Optional.of(ecoFlight));
        mockMvc.perform(MockMvcRequestBuilders.get("/flight/"+ecoFlight.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get a non existing flight")
    void getANonExistingFlight() throws Exception {
        when(flightRepository.findById(any()))
                .thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/flight/"+ecoFlight.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update an existing flight")
    void updateAnExistingFlight() throws Exception {
        builder = new GsonBuilder();
        gson = builder.create();

        when(flightRepository.findById(any()))
                .thenReturn(Optional.of(ecoFlight));

        when(flightRepository.save(any()))
                .thenReturn(ecoFlight);

        mockMvc.perform(MockMvcRequestBuilders.put("/flight")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(gson.toJson(flightDto))
        ).andExpect(status().isOk());
    }
    @Test
    @DisplayName("Update a non existing flight")
    void updateANonExistingFlight() throws Exception {
        builder = new GsonBuilder();
        gson = builder.create();

        when(flightRepository.findById(any()))
                .thenReturn(Optional.empty());

        when(flightRepository.save(any()))
                .thenReturn(ecoFlight);

        mockMvc.perform(MockMvcRequestBuilders.put("/flight")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(gson.toJson(flightDto))
        ).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete an existing flight")
    void deleteAnExistingFlight() throws Exception {
        builder = new GsonBuilder();
        gson = builder.create();

        when(flightRepository.findById(any()))
                .thenReturn(Optional.empty());

        when(flightRepository.save(any()))
                .thenReturn(ecoFlight);

        mockMvc.perform(MockMvcRequestBuilders.delete("/flight/"+ecoFlight.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(gson.toJson(flightDto))
        ).andExpect(status().isNoContent());
    }
}