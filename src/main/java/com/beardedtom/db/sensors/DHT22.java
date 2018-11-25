package com.beardedtom.db.sensors;

import com.beardedtom.db.Sensor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
public class DHT22 {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int dht22Id;

    private long dht22TimeStamp;

    private float dht22Temperature;

    private float dht22Humidity;

    @OneToOne
    @JoinColumn(name = "sensorId")
    private Sensor sensor;
}
