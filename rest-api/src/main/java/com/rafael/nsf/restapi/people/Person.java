package com.rafael.nsf.restapi.people;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CAD_PESSOA")
public class Person {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "NOME_PESSOA", unique = true)
    private String name;
    private Integer age;
}
