package org.example.spring.propagation.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "log")
public class Log {

    @Id
    @GeneratedValue
    private Long id;
    private String message;

    public Log(String message) {
        this.message = message;
    }
}

