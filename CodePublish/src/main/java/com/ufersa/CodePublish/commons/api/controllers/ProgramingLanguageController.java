package com.ufersa.CodePublish.commons.api.controllers;

import com.ufersa.CodePublish.commons.ApiResponse;
import com.ufersa.CodePublish.commons.ResponseUtil;
import com.ufersa.CodePublish.commons.api.dtos.ProgramingLanguageDto;
import com.ufersa.CodePublish.commons.domain.entities.ProgramingLanguage;
import com.ufersa.CodePublish.commons.domain.repositories.ProgramingLanguageRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("v1/programing-languages")
public class ProgramingLanguageController {

    private final ProgramingLanguageRepository programingLanguageRepository;

    public ProgramingLanguageController(ProgramingLanguageRepository programingLanguageRepository) {
        this.programingLanguageRepository = programingLanguageRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProgramingLanguageDto>>> getAll(HttpServletRequest request) {
        List<ProgramingLanguage> programingLanguages = programingLanguageRepository.findAll();
        List<ProgramingLanguageDto> programingLanguageDtos = new ArrayList<>();

        for(ProgramingLanguage programingLanguage : programingLanguages){
            ProgramingLanguageDto programingLanguageDto = new ProgramingLanguageDto();
            programingLanguageDto.setId(programingLanguage.getId());
            programingLanguageDto.setName(programingLanguage.getName());
            programingLanguageDtos.add(programingLanguageDto);
        }

        ApiResponse<List<ProgramingLanguageDto>> apiResponse = ResponseUtil
                .success(programingLanguageDtos,"Linguagens de programação encontradas",request.getRequestURI());

        return ResponseEntity.ok(apiResponse);
    }
}
