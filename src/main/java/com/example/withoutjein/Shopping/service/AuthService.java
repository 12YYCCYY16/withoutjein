package com.example.withoutjein.Shopping.service;

import com.example.withoutjein.Shopping.domain.user.User;
import com.example.withoutjein.Shopping.domain.user.UserRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final CartService cartService;
    private final OrderService orderService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public User signup(User user) {
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        user.setRole("ROLE_USER");

        User userEntity = userRepository.save(user);
        cartService.createCart(user);
        orderService.createOrder(user);

        return userEntity;
    }

    @Transactional
    public User userUpdate(User user) {
        User userEntity = userRepository.save(user);
        return userEntity;
    }
}