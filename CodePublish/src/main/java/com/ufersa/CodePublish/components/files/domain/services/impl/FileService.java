package com.ufersa.CodePublish.components.files.domain.services.impl;

import com.ufersa.CodePublish.components.files.domain.entities.File;
import com.ufersa.CodePublish.components.files.domain.repositories.FileRepository;
import com.ufersa.CodePublish.components.files.domain.services.FileServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service

public class FileService implements FileServiceInterface {

    private final FileRepository fileRepository;
    private Path fileStorageLocation;

    public FileService(@Value("${file.upload-dir}") String fileStorageLocation, FileRepository fileRepository) {
        this.fileStorageLocation = Paths.get(fileStorageLocation).toAbsolutePath().normalize() ;
        this.fileRepository = fileRepository;
    }

    @Override
    public List<File> getByPublication(Long publicationId) {
        return List.of();
    }

    @Override
    public File getById(Long id) throws Exception {
        return null;
    }

    @Override
    @Transactional
    public File create(MultipartFile file) throws Exception {
        File newFile = new File();

        String uniqueName = saveInDirectory(file);

        newFile.setName(file.getOriginalFilename());
        newFile.setType(file.getContentType());
        newFile.setUrl("https://vivid-hedvige-myproject0211-04255184.koyeb.app/v1/files/" + uniqueName);

        return fileRepository.save(newFile);
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<File> file  = fileRepository.findById(id);
        if(file.isPresent()) {
            String fileName = file.get().getUrl().substring(file.get().getUrl().lastIndexOf("/") + 1);
            try {
                Path targetLocation = this.fileStorageLocation.resolve(fileName);
                Files.delete(targetLocation);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            fileName = null;
            fileRepository.delete(file.get());
        }
    }

    private String saveInDirectory(MultipartFile multipartFile) throws Exception {
        String fileExtension;
        String originalName = multipartFile.getOriginalFilename();

        try{
            fileExtension = originalName.substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        } catch (Exception e){
            fileExtension = "";
        }

        String uniqueName = UUID.randomUUID() + fileExtension;
        try {
            if (originalName.contains("..")) {
                throw new Exception("Nome do arquivo contem caracteres inválidos");
            }
            Path targetLocation = this.fileStorageLocation.resolve(uniqueName);
            Files.copy(multipartFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao tentar salvar arquivo", e);
        }
        return uniqueName;
    }
}
