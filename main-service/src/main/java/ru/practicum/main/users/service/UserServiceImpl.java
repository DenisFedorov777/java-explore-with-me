package ru.practicum.main.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.exceptions.UserNotFoundException;
import ru.practicum.main.users.model.dto.UserDto;
import ru.practicum.main.users.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.state.Pagination.patternPageable;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        Pageable pagination = patternPageable(from, size);
        if (ids == null || ids.isEmpty()) {
            return repository.findAll(pagination).stream()
                    .map(UserMapper::toDtoUser)
                    .collect(Collectors.toList());
        } else {
            return repository.findAllByIdIn(ids, pagination).stream()
                    .map(UserMapper::toDtoUser)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto) {
        return UserMapper.toDtoUser(repository.save(UserMapper.toUser(userDto)));
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        log.info("Удаление пользователя с идентификатором: {}", userId);
        repository.delete(repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!")));
    }
}
