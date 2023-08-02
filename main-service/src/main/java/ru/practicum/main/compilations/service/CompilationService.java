package ru.practicum.main.compilations.service;

import ru.practicum.main.compilations.model.dto.CompilationDto;
import ru.practicum.main.compilations.model.dto.NewCompilationDto;
import ru.practicum.main.compilations.model.dto.UpdateCompilationRequest;

import javax.validation.Valid;
import java.util.List;

public interface CompilationService {
    CompilationDto createCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilationPatch(Long compId, @Valid UpdateCompilationRequest request);

    List<CompilationDto> getCompilationOfEvents(boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationById(Long compId);
}
