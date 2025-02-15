package com.example.doseo_user.dao;

import com.example.doseo_user.entity.User;
import com.example.doseo_user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDAOImpl implements UserDAO {
    private final UserRepository userRepository;
    @Override
    public void write(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }
}
