package com.microservices.gateway.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "authorities")
public class Authority {

    @Id
    @Column(length = 50)
    private String name;

    // Constructors
    public Authority() {}

    public Authority(String name) {
        this.name = name;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
