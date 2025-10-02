package com.ufersa.CodePublish.components.publication.api.restcontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufersa.CodePublish.commons.ApiResponse;
import com.ufersa.CodePublish.commons.ResponseUtil;
import com.ufersa.CodePublish.components.publication.api.dtos.*;
import com.ufersa.CodePublish.components.publication.domain.entities.Publication;
import com.ufersa.CodePublish.components.publication.domain.services.impl.PublicationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/v1/publications")
@RequiredArgsConstructor
public class PublicationController {

    private final PublicationService publicationService;
    private final PublicationMapper publicationMapper;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<PublicationDto>> post(
            @RequestPart("data") String createPublicationDtoJson,
            MultipartHttpServletRequest multipartRequest,
            @CookieValue("accessToken") String token,
            HttpServletRequest request
    ) throws Exception {

        Map<String,MultipartFile> files = multipartRequest.getFileMap();

        ObjectMapper objectMapper = new ObjectMapper();
        CreatePublicationDto createPublicationDto = objectMapper.readValue(createPublicationDtoJson, CreatePublicationDto.class);

        Publication newPublication = publicationService.create(
                publicationMapper.CreatePublicationDtoToPublication(createPublicationDto),
                token,
                files
        );

        PublicationDto publicationDto = publicationMapper.publicationToPublicationDto(newPublication);

        ApiResponse<PublicationDto> response = ResponseUtil
                .success(publicationDto,"Publicação criada",request.getRequestURI());


        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<PublicationDto>> put(
            @PathVariable("id") Long id,
            @RequestPart("data") UpdatePublicationDto updatePublicationDto,
            MultipartHttpServletRequest multipartRequest,
            @RequestHeader("Authorization") String token,
            HttpServletRequest request
    ) throws Exception {
        Map<String,MultipartFile> files = multipartRequest.getFileMap();

        Publication newPublication = publicationService.update(
                id,
                publicationMapper.UpdatePublicationDtoToPublication(updatePublicationDto),
                token,
                files
        );

        PublicationDto publicationDto = publicationMapper.publicationToPublicationDto(newPublication);

        ApiResponse<PublicationDto> response = ResponseUtil
                .success(publicationDto,"Publicação atualizada",request.getRequestURI());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PublicationDto>>> get(
            @RequestParam(required = false) String q,
            HttpServletRequest request,
            Pageable pageable
    ){
        Page<Publication> publications = publicationService.getPublicationsByQ(q,pageable);

        Page<PublicationDto> publicationDtos = publications.map(
                publicationMapper::publicationToPublicationDto
        );

        ApiResponse<Page<PublicationDto>> response = ResponseUtil.success(
                publicationDtos,"Publicações encontradas",request.getRequestURI()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PublicationDto>> get(
            @PathVariable("id") Long id,
            HttpServletRequest request
    ) throws Exception {
        Publication publication = publicationService.getById(id);

        PublicationDto publicationDto = publicationMapper.publicationToPublicationDto(publication);

        ApiResponse<PublicationDto> response =
                ResponseUtil.success(publicationDto,"Publicação encontrada",request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable("id") Long id,
            HttpServletRequest request
    ) throws Exception {
        publicationService.delete(id);

        ApiResponse<Void> response =
                ResponseUtil.success(null,"Publicação deletada",request.getRequestURI());

        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
