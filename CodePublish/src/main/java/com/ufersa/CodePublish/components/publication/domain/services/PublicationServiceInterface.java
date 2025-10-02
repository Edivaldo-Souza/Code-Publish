package com.ufersa.CodePublish.components.publication.domain.services;

import com.ufersa.CodePublish.components.publication.domain.entities.Publication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface PublicationServiceInterface {
    List<Publication> getAll();
    Publication getById(Long id) throws Exception;
    Publication create(Publication publication, String token, Map<String,MultipartFile> files) throws Exception;
    Publication update(Long id,Publication publication, String token, Map<String,MultipartFile> file) throws Exception;
    void delete(Long id) throws Exception;
}
