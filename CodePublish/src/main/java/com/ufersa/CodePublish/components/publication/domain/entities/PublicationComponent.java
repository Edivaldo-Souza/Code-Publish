package com.ufersa.CodePublish.components.publication.domain.entities;


import com.ufersa.CodePublish.components.files.domain.entities.File;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@Entity
@Table(name = "tb_publication_component")
public class PublicationComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private File file;

    @Transient
    private String fileId;

    @ManyToOne
    @JoinColumn(name = "publication_id")
    private Publication publication;

    private String description;
}
