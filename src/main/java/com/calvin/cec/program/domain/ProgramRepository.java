package com.calvin.cec.program.domain;

import java.util.List;
import java.util.Optional;

public interface ProgramRepository {
    Optional<Program> findById(ProgramId id);

    List<Program> findAll();

    void save(Program program);
}
