package ru.intabia.sso.demo.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.intabia.sso.demo.backend.dto.User;
import ru.intabia.sso.demo.backend.mapping.UserMapper;
import ru.intabia.sso.demo.backend.model.UserEntity;
import ru.intabia.sso.demo.backend.repository.ClientRepository;

/**
 * User storage service.
 */
@Service
public class UserStorageService {

    private final ClientRepository clientRepository;
    private final UserMapper userMapper;

    /**
     * Constructor.
     *
     * @param clientRepository ClientRepository bean
     * @param userMapper UserMapper bean
     */
    @Autowired
    public UserStorageService(ClientRepository clientRepository,
                              UserMapper userMapper) {
        this.clientRepository = clientRepository;
        this.userMapper = userMapper;
    }

    /**
     * Save new or update by id exist user.
     *
     * @param user to save
     * @return saved user
     */
    public User saveUser(User user) {
        UserEntity model = userMapper.mapFromDto(user);
        UserEntity saved = this.clientRepository.save(model);
        return userMapper.mapToDto(saved);
    }

    /**
     * Get user.
     *
     * @param userId client if
     * @return user
     */
    public User getUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Cannot get client with null id");
        }
        UserEntity user = this.clientRepository.findOne(userId);
        return userMapper.mapToDto(user);
    }
}
