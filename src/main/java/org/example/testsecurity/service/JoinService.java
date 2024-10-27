package org.example.testsecurity.service;

import lombok.RequiredArgsConstructor;
import org.example.testsecurity.Entity.UserEntity;
import org.example.testsecurity.dto.JoinDTO;
import org.example.testsecurity.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean joinProcess(JoinDTO joinDTO) {

        if(userRepository.existsByUsername(joinDTO.getUsername()))
            return false;

        UserEntity user = new UserEntity();
        user.setUsername(joinDTO.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword())); //패스워드 암호화
        user.setRole("ROLE_ADMIN");

        userRepository.save(user);
        return true;
    }
}
