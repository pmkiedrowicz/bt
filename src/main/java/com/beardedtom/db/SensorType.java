package com.beardedtom.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
public class SensorType {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int sensorTypeId;

    private String sensorTypeName;

    @OneToMany(mappedBy = "sensorType", cascade = CascadeType.ALL)
    public Set<Sensor> sensor;
}
