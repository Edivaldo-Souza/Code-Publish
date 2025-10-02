package com.ufersa.CodePublish.components.files.api.restcontrollers;

import com.ufersa.CodePublish.commons.ApiResponse;
import com.ufersa.CodePublish.components.files.domain.entities.File;
import com.ufersa.CodePublish.components.files.domain.repositories.FileRepository;
import com.ufersa.CodePublish.components.files.domain.services.impl.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("v1/files")
public class FileController {

    private final FileService fileService;
    private final Path fileStorageLocation;
    private final FileRepository fileRepository;


    FileController(FileService fileService, @Value("${file.upload-dir}")String path, FileRepository fileRepository) {
        this.fileService = fileService;
        this.fileStorageLocation = Paths.get(path).toAbsolutePath().normalize();
        this.fileRepository = fileRepository;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Resource> getByUUID(@PathVariable("uuid") String uuid) {
        if(uuid!=null){
            Path filePath = fileStorageLocation.resolve(uuid).normalize();
            try {
                Resource resource = new UrlResource(filePath.toUri());
                if(resource.exists() && resource.isReadable()){
                    Optional<File> file = fileRepository.findByUrl(uuid);
                    String contentType;
                    if(file.isPresent()){
                        contentType = file.get().getType();
                    }
                    else contentType = "application/octet-stream";

                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(contentType))
                            .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + uuid + "\"")
                            .body(resource);
                }
                else return ResponseEntity.notFound().build();
            }
            catch(MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.badRequest().build();
    }

}
