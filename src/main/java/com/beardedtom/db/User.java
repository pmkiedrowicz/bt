package com.beardedtom.db;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int userId;

    @Length(min = 3, max = 32)
    @NotEmpty
    private String userNick;

    @Email
    @NotEmpty
    private String userEmail;

    @Length(min = 8, max = 60)
    @NotEmpty
    private String userPassword;

    private boolean userEnabled;

    private String userRole;

    private long userRegisterTime;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    public Set<Sensor> sensor;
}
