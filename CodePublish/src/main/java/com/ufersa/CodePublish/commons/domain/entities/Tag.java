package com.ufersa.CodePublish.commons.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ufersa.CodePublish.components.publication.domain.entities.Publication;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Table(name = "tb_tag")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany
    @JsonIgnore
    private Set<Publication> publications = new HashSet<>();
}
