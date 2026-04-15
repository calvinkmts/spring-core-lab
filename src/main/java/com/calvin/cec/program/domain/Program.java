package com.calvin.cec.program.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
public class Program {
    private final ProgramId id;
    private final Long categoryId;
    private final Set<ProgramDescription> descriptions;

    public Program(ProgramId id, Long categoryId, Set<ProgramDescription> descriptions) {

        if (categoryId == null) {
            throw new IllegalArgumentException("categoryId is required");
        }

        if (descriptions == null || descriptions.isEmpty()) {
            throw new IllegalArgumentException("At least one description is required");
        }

        this.id = id;
        this.categoryId = categoryId;
        this.descriptions = new HashSet<>(descriptions);
    }

    public Optional<ProgramDescription> getDescriptionIn(String languageCode) {
        return descriptions.stream()
                .filter(d -> d.languageCode().equalsIgnoreCase(languageCode))
                .findFirst();
    }

    public String getNameIn(String languageCode) {
        return getDescriptionIn(languageCode)
                .map(ProgramDescription::name)
                .orElse("untitled");
    }

    public Set<ProgramDescription> getDescriptions() {
        return Collections.unmodifiableSet(descriptions);
    }
}
