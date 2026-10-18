package com.dynamicbank.account.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "client")
@DiscriminatorValue("CLIENT")
@Getter
@Setter
public class Client extends User {

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    public Client() {}

    public Client(String fullName, String username, String email, String passwordHash, LocalDate birthDate) {
        super(fullName, username, email, passwordHash);
        this.birthDate = birthDate;
    }
}