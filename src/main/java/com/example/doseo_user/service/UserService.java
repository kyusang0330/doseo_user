package com.example.doseo_user.service;

import com.example.doseo_user.dto.UserRequestDTO;
import com.example.doseo_user.dto.UserResponseDTO;
import com.example.doseo_user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
     void write(UserRequestDTO user);
     UserResponseDTO login(String username, String password);
     Optional<User> findById(Long id) ;
     void delete(Long id) ;
     List<User> findAll() ;
}
