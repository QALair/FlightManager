package com.abccompany.flightmanager.passenger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PassengerController.class)
@ExtendWith(MockitoExtension.class)
class PassengerControllerTest {

    @Autowired
    PassengerController passengerController;

    @MockBean
    PassengerRepository passengerRepository;

    @Autowired
    MockMvc mockMvc;

    private ObjectMapper objectMapper;

    Passenger passenger;
    PassengerDto passengerDto;

    @BeforeEach
    void setUp(){
        passenger = new Passenger();
        passenger.setId("P4");
        passenger.setName("Joao Netto");
        passenger.setVip(false);
        objectMapper = new ObjectMapper();

        passengerDto = new PassengerDto();
        passengerDto.setId(passenger.getId());
        passengerDto.setName(passenger.getName());
        passengerDto.setVip(passenger.isVip());
    }

    @Test
    void createPassenger() throws Exception {
        when(passengerRepository.findByName(passenger.getName()))
                .then(invocationOnMock -> Optional.empty());
        when(passengerRepository.save(passenger))
                .then(invocationOnMock -> passenger);

        mockMvc.perform(MockMvcRequestBuilders.post("/passenger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passenger)))
                .andExpect(status().isCreated());
    }
    @Test
    @DisplayName("Create an Existing passenger")
    void createAnExistingPassenger() throws Exception {
        when(passengerRepository.findByName(passenger.getName()))
                .then(invocationOnMock -> Optional.of(passenger));

        mockMvc.perform(MockMvcRequestBuilders.post("/passenger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passenger)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Retrieve a passenger")
    void retrieveApassenger() throws Exception {
        when(passengerRepository.findById(any()))
                .thenReturn(Optional.of(passenger));

        mockMvc.perform(MockMvcRequestBuilders.get("/passenger/"+passenger.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Retrieve a non existing passenger")
    void retrieveANonExistingpassenger() throws Exception {
        when(passengerRepository.findById(any()))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/passenger/"+passenger.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update passenger name")
    void updatePassengerName() throws Exception {
        when(passengerRepository.findByName(passenger.getName()))
                .then(invocationOnMock -> Optional.of(passenger));

        when(passengerRepository.save(any()))
                .then(invocationOnMock -> passenger);

        mockMvc.perform(MockMvcRequestBuilders.put("/passenger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passenger)))
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("Update a non existing passenger name")
    void updateANonExistingPassengerName() throws Exception {
        when(passengerRepository.findByName(passenger.getName()))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/passenger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passenger)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete a passenger")
    void deleteAPassenger() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/passenger/"+passenger.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Get all passengers")
    void getAllPassengers() throws Exception {
        when(passengerRepository.findAll())
                .thenReturn(Collections.singletonList(passenger));

        mockMvc.perform(MockMvcRequestBuilders.get("/passenger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}