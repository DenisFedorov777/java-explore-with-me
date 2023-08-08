package ru.practicum.main.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.compilations.model.Compilation;
import ru.practicum.main.compilations.model.dto.CompilationDto;
import ru.practicum.main.compilations.model.dto.NewCompilationDto;
import ru.practicum.main.compilations.model.dto.UpdateCompilationRequest;
import ru.practicum.main.compilations.repository.CompilationRepository;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.repository.EventRepository;
import ru.practicum.main.exceptions.CompilationNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.state.Pagination.patternPageable;

@Service
@RequiredArgsConstructor
@Transactional
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(compilationDto);
        getCompilationsOfEvents(compilationDto, compilation);
        return CompilationMapper.toDto(repository.save(compilation));
    }

    @Override
    public void deleteCompilation(Long compId) {
        repository.delete(repository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Compilation not found and not deleted")));
    }

    @Override
    public CompilationDto updateCompilationPatch(Long compId, UpdateCompilationRequest request) {
        Compilation compilation = repository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Compilation not found"));
        updateCompilationEntity(request, compilation);
        return CompilationMapper.toDto(repository.save(compilation));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilationOfEvents(boolean pinned, Integer from, Integer size) {
        Pageable pagination = patternPageable(from, size);
        if (pinned) {
            return repository.findAllByPinnedTrue(pagination).stream()
                    .map(CompilationMapper::toDto)
                    .collect(Collectors.toList());
        }
        return repository.findAllByPinnedFalse(pagination).stream()
                .map(CompilationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilationById(Long compId) {
        return CompilationMapper.toDto(repository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Compilation not found")));
    }

    private void getCompilationsOfEvents(NewCompilationDto compilationDto, Compilation compilation) {
        List<Event> eventsToCreate = updateListEvents(compilationDto.getEvents());
        compilation.setEvents(eventsToCreate);
    }

    private List<Event> updateListEvents(List<Long> eventsIds) {
        if (eventsIds != null) {
            return eventRepository.findAllByIdIn(eventsIds);
        }
        return Collections.emptyList();
    }

    private void updateCompilationEntity(UpdateCompilationRequest request, Compilation compilation) {
        List<Event> eventsToCreate = updateListEvents(request.getEvents());
        compilation.setEvents(eventsToCreate);
        if (request.getPinned() != null) compilation.setPinned(request.getPinned());
        if (request.getTitle() != null) compilation.setTitle(request.getTitle());
    }
}
