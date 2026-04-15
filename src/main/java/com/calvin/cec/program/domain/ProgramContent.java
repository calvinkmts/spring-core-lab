package com.calvin.cec.program.domain;

import java.util.List;

public record ProgramContent(
        List<String> goals,
        List<String> materials,
        List<String> includes,
        List<String> requirements) {

    public static ProgramContent empty() {
        return new ProgramContent(List.of(), List.of(), List.of(), List.of());
    }
}
