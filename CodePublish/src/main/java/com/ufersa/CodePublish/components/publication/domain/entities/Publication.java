package com.ufersa.CodePublish.components.publication.domain.entities;

import com.ufersa.CodePublish.commons.domain.entities.Category;
import com.ufersa.CodePublish.commons.domain.entities.ProgramingLanguage;
import com.ufersa.CodePublish.commons.domain.entities.Tag;
import com.ufersa.CodePublish.components.user.domain.entities.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "tb_publication")
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    private Integer upvotesAmount;
    private Integer downvotesAmount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
 
    @OneToMany(mappedBy = "publication", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PublicationComponent> components;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tb_publication_tag",
            joinColumns = @JoinColumn(name = "publication_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "programing_language_id")
    private ProgramingLanguage programingLanguage;
}
