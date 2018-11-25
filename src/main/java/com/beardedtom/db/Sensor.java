package com.beardedtom.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
public class Sensor {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int sensorId;

    private String sensorUserName;

    private long sensorRegisterTime;

    private String sensorSeed;

    @ManyToOne
    @JoinColumn(name = "sensorType")
    private SensorType sensorType;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "sensorStatus")
    private SensorStatus sensorStatus;
}