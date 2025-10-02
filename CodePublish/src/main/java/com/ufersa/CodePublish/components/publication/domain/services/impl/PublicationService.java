package com.ufersa.CodePublish.components.publication.domain.services.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ufersa.CodePublish.commons.domain.entities.Tag;
import com.ufersa.CodePublish.commons.domain.repositories.TagRepository;
import com.ufersa.CodePublish.components.authentication.domain.services.TokenService;
import com.ufersa.CodePublish.components.files.domain.services.impl.FileService;
import com.ufersa.CodePublish.components.publication.domain.entities.Publication;
import com.ufersa.CodePublish.components.publication.domain.entities.PublicationComponent;
import com.ufersa.CodePublish.components.publication.domain.repositories.PublicationComponentRepository;
import com.ufersa.CodePublish.components.publication.domain.repositories.PublicationRepository;
import com.ufersa.CodePublish.components.publication.domain.services.PublicationServiceInterface;
import com.ufersa.CodePublish.components.user.domain.entities.User;
import com.ufersa.CodePublish.components.user.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicationService implements PublicationServiceInterface {

    private final PublicationRepository publicationRepository;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final PublicationComponentRepository publicationComponentRepository;
    private final TagRepository tagRepository;


    @Override
    public List<Publication> getAll() {
        return List.of();
    }

    @Override
    @Transactional
    public Publication getById(Long id) throws Exception {
        Optional<Publication> publication = publicationRepository.findById(id);
        if (publication.isEmpty()) {
            throw new Exception("Publicação não encontrada com id = "+id);
        }

        if(publication.get().getTags()!=null) {
            publication.get().getTags().size();
        }
        if(publication.get().getComponents()!=null) {
            publication.get().getComponents().size();
        }

        return publication.get();
    }

    @Transactional
    public Page<Publication> getPublicationsByQ(String q, Pageable pageable) {
        Page<Publication> publications = publicationRepository.findByText(q, pageable);
        publications.forEach(publication -> {
            if (publication.getComponents() != null) {
                publication.getComponents().size();
            }
            if (publication.getTags() != null) {
                publication.getTags().size();
            }
        });
        return publications;
    }

    @Override
    @Transactional
    public Publication create(Publication publication, String token, Map<String,MultipartFile> files) throws Exception {
        DecodedJWT jwt = tokenService.decodeToken(token);
        Optional<User> user = userRepository.getByEmail(jwt.getSubject());

        if (user.isEmpty()) {
            throw new Exception("Não foi possível encontrar o usuário que tentou" +
                    "criar a publicação");
        }

        if(publication.getComponents()!=null){
            for(PublicationComponent component : publication.getComponents()){
                MultipartFile currentFile = files.get(component.getFileId());
                component.setFile(fileService.create(currentFile));

                component.setPublication(publication);
            }
        }

        if(publication.getTags()!=null){
            Set<Tag> tags = new HashSet<>();
            for(Tag tag : publication.getTags()){
                String tagNameNormalized = tag.getName().toLowerCase();

                Optional<Tag> optionalTag = tagRepository.findByName(tagNameNormalized);

                if(optionalTag.isPresent()){
                    tags.add(optionalTag.get());
                }
                else{
                    tag.setName(tagNameNormalized);
                    tags.add(tag);
                }
            }
            publication.setTags(tags);
        }

        publication.setUser(user.get());

        return publicationRepository.save(publication);
    }

    @Override
    @Transactional
    public Publication update(Long id, Publication publication, String token, Map<String,MultipartFile> files) throws Exception {
        Optional<Publication> currentPublication = publicationRepository.findById(id);
        if (currentPublication.isEmpty()) {
            throw new Exception("Não foi encontrar a publicação");
        }

        DecodedJWT jwt = tokenService.decodeToken(token.substring(7));
        Optional<User> user = userRepository.getByEmail(jwt.getSubject());

        if (user.isEmpty()) {
            throw new Exception("Não foi possível encontrar o usuário que tentou" +
                    "criar a publicação");
        }

        if(!Objects.equals(user.get().getId(), currentPublication.get().getUser().getId())){
            throw new Exception("O usuário que está tentando editar a publicação não é o mesmo" +
                    "que a criou");
        }

        publication.setId(id);

        if(publication.getTags()!=null){
            Set<Tag> tags = new HashSet<>();
            for(Tag tag : publication.getTags()){
                String tagNameNormalized = tag.getName().toLowerCase();

                Optional<Tag> optionalTag = tagRepository.findByName(tagNameNormalized);

                if(optionalTag.isPresent()){
                    tags.add(optionalTag.get());
                }
                else{
                    tag.setName(tagNameNormalized);
                    tags.add(tag);
                }
            }
            publication.setTags(tags);
        }

        if(publication.getComponents()!=null){
            for(PublicationComponent component : publication.getComponents()){
                if(component.getFile()==null){

                    if(component.getFileId()==null || !files.containsKey(component.getFileId())){
                        throw new Exception("É preciso que um arquivo esteja associado ao componente" +
                                "sendo atualizado");
                    }

                    Optional<PublicationComponent> publicationComponentOptional =
                            publicationComponentRepository.findById(component.getId());
                    if(publicationComponentOptional.isPresent()){
                        fileService.delete(publicationComponentOptional.get().getFile().getId());
                    }
                    MultipartFile currentFile = files.get(component.getFileId());
                    component.setFile(fileService.create(currentFile));
                }

            }
            List<PublicationComponent> updatedComponents = publicationComponentRepository.saveAll(publication.getComponents());
            for(PublicationComponent component : updatedComponents){
                component.setPublication(publication);
            }
            publication.setComponents(updatedComponents);
        }

        publication.setUser(user.get());

        return publicationRepository.save(publication);
    }

    @Override
    @Transactional
    public void delete(Long id) throws Exception {
        Optional<Publication> publication = publicationRepository.findById(id);

        if(publication.isEmpty()){
            throw new Exception("Publicação não encontrada com id = "+id);
        }

        if(publication.get().getComponents()!=null){
            for(PublicationComponent component : publication.get().getComponents()){
                fileService.delete(component.getFile().getId());
                publicationComponentRepository.delete(component);
            }
        }

        publicationRepository.delete(publication.get());
    }
}
