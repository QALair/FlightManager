package com.abccompany.flightmanager.flight;

import com.abccompany.flightmanager.passenger.Passenger;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Flight {
    @Id
    private String id;
    @Enumerated(EnumType.STRING)
    private FlightCategory category;
    @OneToMany()
    private Set<Passenger> passengers;

}
