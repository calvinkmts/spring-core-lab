package com.calvin.cec.program.domain;

public record ProgramDescription(
        String languageCode,
        String name,
        String slug,
        ProgramContent content,
        ProgramImages images) {

    public ProgramDescription {
        if (languageCode == null || languageCode.isBlank()) {
            throw new IllegalArgumentException("Language code is required");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (slug == null || slug.isBlank()) {
            throw new IllegalArgumentException("Slug is required");
        }
    }
}
