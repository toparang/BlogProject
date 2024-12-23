package me.toparang.blog.service;

import lombok.RequiredArgsConstructor;
import me.toparang.blog.domain.User;
import me.toparang.blog.dto.AddUserRequest;
import me.toparang.blog.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequest userDto) {
        return userRepository.save(User.builder()
                        .email(userDto.getEmail())
                        .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                .build()).getId();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}
