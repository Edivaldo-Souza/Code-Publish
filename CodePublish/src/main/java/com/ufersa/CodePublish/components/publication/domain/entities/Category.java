package com.ufersa.CodePublish.components.publication.domain.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Category {
    DESENVOLVIMENTO_WEB("Desenvolvimento para Web"),
    INTELIGENCIA_ARTIFICIAL("Inteligência Artificial"),
    CIENCIA_DE_DADOS("Ciência de Dados"),
    SEGURANCA_DE_DADOS("Segurança de Dados");

    private final String value;

    Category(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Category fromValue(String value) {
        for (Category c : Category.values()) {
            if (c.getValue().equalsIgnoreCase(value)) {
                return c;
            }
        }
        throw new IllegalArgumentException(value);
    }
}
