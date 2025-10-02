package com.ufersa.CodePublish.components.files.domain.services;

import com.ufersa.CodePublish.components.files.domain.entities.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileServiceInterface {
    List<File> getByPublication(Long publicationId);
    File getById(Long id) throws Exception;
    File create(MultipartFile file) throws Exception;
    void delete(Long id) throws Exception;
}
