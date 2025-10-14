package com.ufersa.CodePublish.commons.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "tb_programing_language")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProgramingLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
