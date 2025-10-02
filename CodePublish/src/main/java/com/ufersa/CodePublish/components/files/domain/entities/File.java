package com.ufersa.CodePublish.components.files.domain.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_file")
@Data
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    private String url;
}
