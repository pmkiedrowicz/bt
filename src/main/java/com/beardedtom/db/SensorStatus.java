package com.beardedtom.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
public class SensorStatus {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int sensorStatusId;

    private long sensorStatusLastPost;

    private Boolean sensorStatusUpdate;

    @OneToMany(mappedBy = "sensorStatus", cascade = CascadeType.ALL)
    public Set<Sensor> sensor;
}
