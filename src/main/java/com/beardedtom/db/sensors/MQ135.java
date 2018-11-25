package com.beardedtom.db.sensors;

import com.beardedtom.db.Sensor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
public class MQ135 {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int mq135Id;

    private long mq135TimeStamp;

    private float mq135RZero;

    private float mq135CorrectedRZero;

    private float mq135Resistance;

    private float mq135Ppm;

    private float mq135CorrectedPpm;

    @OneToOne
    @JoinColumn(name = "sensorId")
    private Sensor sensor;
}
