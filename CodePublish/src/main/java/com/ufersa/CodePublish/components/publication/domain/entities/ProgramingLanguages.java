package com.ufersa.CodePublish.components.publication.domain.entities;

public enum ProgramingLanguages {
    JAVA("JAVA"),
    JAVASCRIPT("JAVASCRIPT"),
    PYTHON("PYTHON");

    private final String value;
    ProgramingLanguages(String value) {
        this.value = value;
    }
}
