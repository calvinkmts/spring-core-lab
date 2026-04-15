package com.calvin.cec.program.domain;

public record ProgramId(Long value) {
    public ProgramId {
        if (value == null) {
            throw new IllegalArgumentException("ProgramId is required");
        }
    }
}
