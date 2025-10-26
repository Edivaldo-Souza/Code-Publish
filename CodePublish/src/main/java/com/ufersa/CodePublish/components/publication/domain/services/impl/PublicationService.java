package com.ufersa.CodePublish.components.publication.domain.services.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ufersa.CodePublish.commons.domain.entities.Category;
import com.ufersa.CodePublish.commons.domain.entities.ProgramingLanguage;
import com.ufersa.CodePublish.commons.domain.entities.Tag;
import com.ufersa.CodePublish.commons.domain.repositories.CategoryRepository;
import com.ufersa.CodePublish.commons.domain.repositories.ProgramingLanguageRepository;
import com.ufersa.CodePublish.commons.domain.repositories.TagRepository;
import com.ufersa.CodePublish.components.authentication.domain.services.TokenService;
import com.ufersa.CodePublish.components.files.domain.services.impl.FileService;
import com.ufersa.CodePublish.components.publication.domain.entities.Publication;
import com.ufersa.CodePublish.components.publication.domain.entities.PublicationComponent;
import com.ufersa.CodePublish.components.publication.domain.entities.PublicationUserRating;
import com.ufersa.CodePublish.components.publication.domain.entities.RatingId;
import com.ufersa.CodePublish.components.publication.domain.repositories.PublicationComponentRepository;
import com.ufersa.CodePublish.components.publication.domain.repositories.PublicationRepository;
import com.ufersa.CodePublish.components.publication.domain.repositories.PublicationUserRatingRepository;
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
    private final CategoryRepository categoryRepository;
    private final ProgramingLanguageRepository programingLanguageRepository;
    private final PublicationUserRatingRepository publicationUserRatingRepository;

    @Override
    @Transactional
    public List<Publication> getAll(String q) {
        // return publicationRepository.findByText(q);
        return null;
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
    public Page<Publication> getPublicationsByQ(
            String q,
            Boolean currentUserPublication,
            String token,
            Pageable pageable) {
        if(currentUserPublication){
            DecodedJWT decodedJWT = tokenService.decodeToken(token);
            Optional<User> user = userRepository.getByEmail(decodedJWT.getSubject());

            if(user.isPresent()){
                List<Long> ids = publicationRepository.findIdsWithUser(q,user.get().getId());
                return publicationRepository.findByTextAndUser(ids,pageable);
            }
        }
        List<Long> ids = publicationRepository.findIds(q);
        return publicationRepository.findByTextAndUser(ids, pageable);
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

        if(publication.getComponents()==null || publication.getComponents().isEmpty()){
            throw new Exception("No mínimo um arquivo precisa ser informado ");
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

        if(publication.getCategory().getId()==null){
            throw new Exception("No mínimo uma categoria precisa ser informada");
        }
        Optional<Category> category = categoryRepository.findById(publication.getCategory().getId());
        if(category.isPresent()) publication.setCategory(category.get());

        if(publication.getProgramingLanguage().getId()==null){
            throw new Exception("No mínimo uma linguagem de programação precisa ser informada");
        }

        Optional<ProgramingLanguage> programingLanguage = programingLanguageRepository.findById(
                publication.getProgramingLanguage().getId()
        );
        if(programingLanguage.isPresent()) publication.setProgramingLanguage(programingLanguage.get());

        publication.setUser(user.get());

        user.get().setPublicationAmount(user.get().getPublicationAmount()+1);
        userRepository.save(user.get());

        publication.setDownvotesAmount(0);
        publication.setUpvotesAmount(0);

        return publicationRepository.save(publication);
    }

    @Override
    @Transactional
    public Publication update(Long id, Publication publication, String token, Map<String,MultipartFile> files) throws Exception {
        Optional<Publication> currentPublication = publicationRepository.findById(id);
        if (currentPublication.isEmpty()) {
            throw new Exception("Não foi encontrar a publicação");
        }

        DecodedJWT jwt = tokenService.decodeToken(token);
        Optional<User> user = userRepository.getByEmail(jwt.getSubject());

        if (user.isEmpty()) {
            throw new Exception("Não foi possível encontrar o usuário que tentou" +
                    "criar a publicação");
        }

        if(!Objects.equals(user.get().getId(), currentPublication.get().getUser().getId())){
            throw new Exception("O usuário que está tentando editar a publicação não é o mesmo" +
                    "que a criou");
        }

        if(publication.getComponents()==null || publication.getComponents().isEmpty()){
            throw new Exception("No mínimo um arquivo precisa ser informado ");
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
                if(component.getId()==null){
                    if(component.getFileId()==null || !files.containsKey(component.getFileId())){
                        throw new Exception("É preciso que um arquivo esteja associado ao componente" +
                                "sendo atualizado");
                    }

                    MultipartFile currentFile = files.get(component.getFileId());
                    component.setFile(fileService.create(currentFile));
                }
                else if(component.getFile()==null){

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

        if(publication.getCategory().getId()==null){
            throw new Exception("No mínimo uma categoria precisa ser informada");
        }
        Optional<Category> category = categoryRepository.findById(publication.getCategory().getId());
        if(category.isPresent()) publication.setCategory(category.get());

        if(publication.getProgramingLanguage().getId()==null){
            throw new Exception("No mínimo uma linguagem de programação precisa ser informada");
        }

        Optional<ProgramingLanguage> programingLanguage = programingLanguageRepository.findById(
                publication.getProgramingLanguage().getId()
        );
        if(programingLanguage.isPresent()) publication.setProgramingLanguage(programingLanguage.get());

        publication.setUser(user.get());

        publication.setUpvotesAmount(currentPublication.get().getUpvotesAmount());
        publication.setDownvotesAmount(currentPublication.get().getDownvotesAmount());

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
                component.setFile(null);
                publicationComponentRepository.delete(component);
            }
        }

        publicationUserRatingRepository.deleteByIdPublicationId(id);

        User user = publication.get().getUser();
        user.setPublicationAmount(user.getPublicationAmount()-1);
        userRepository.save(user);

        publicationRepository.delete(publication.get());
    }

    public Boolean setRating(Long id, Boolean isPositive, String token) throws Exception {
        Optional<Publication> currentPublication = publicationRepository.findById(id);
        if (currentPublication.isEmpty()) {
            throw new Exception("Não foi encontrar a publicação");
        }

        DecodedJWT jwt = tokenService.decodeToken(token);
        Optional<User> user = userRepository.getByEmail(jwt.getSubject());

        if (user.isEmpty()) {
            throw new Exception("Não foi possível encontrar o usuário que tentou" +
                    "criar a publicação");
        }

        if(Objects.equals(user.get().getId(), currentPublication.get().getUser().getId())){
            throw new Exception("O usuário não pode avaliar sua própria publicação");
        }

        RatingId ratingId = new RatingId();
        ratingId.setPublicationId(currentPublication.get().getId());
        ratingId.setUserId(user.get().getId());

        Optional<PublicationUserRating> publicationUserRating = publicationUserRatingRepository.findById(ratingId);

        if(publicationUserRating.isPresent() &&
           publicationUserRating.get().getPositive().equals(isPositive)){
            throw new Exception("O usuário só pode fazer uma avaliação por publicação");
        }

        PublicationUserRating rating = new PublicationUserRating();
        rating.setId(ratingId);

        if(isPositive){
            if(publicationUserRating.isPresent() && !publicationUserRating.get().getPositive()){
                currentPublication.get().setDownvotesAmount(currentPublication.get().getDownvotesAmount()-1);
                publicationUserRating.get().setPositive(true);
            }
            int currentUpvotes = currentPublication.get().getUpvotesAmount();
            currentPublication.get().setUpvotesAmount(currentUpvotes + 1);
            rating.setPositive(true);
        }
        else{
            if(publicationUserRating.isPresent() && publicationUserRating.get().getPositive()){
                currentPublication.get().setUpvotesAmount(currentPublication.get().getUpvotesAmount()-1);
                publicationUserRating.get().setPositive(false);
            }
            int currentDownvotes = currentPublication.get().getDownvotesAmount();
            currentPublication.get().setDownvotesAmount(currentDownvotes + 1);
            rating.setPositive(false);
        }


        publicationRepository.save(currentPublication.get());
        if(publicationUserRating.isPresent()){
            publicationUserRatingRepository.save(publicationUserRating.get());
            return true;
        }
        else {
            user.get().setRatingAmount(user.get().getRatingAmount()+1);
            userRepository.save(user.get());
            publicationUserRatingRepository.save(rating);
            return false;
        }
    }
}
