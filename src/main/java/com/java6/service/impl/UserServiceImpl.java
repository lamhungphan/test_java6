package com.java6.service.impl;

import com.java6.controller.request.UserRequest;
import com.java6.entity.Role;
import com.java6.entity.User;
import com.java6.repository.UserRepository;
import com.java6.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Page<User> getAll(String keyword, String sort, int page, int size) {
        Pageable pageable;

        // Mặc định sắp xếp theo fullname ASC
        if (sort != null && !sort.isEmpty()) {
            pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by("fullName").ascending());
        }

        if (keyword == null || keyword.isBlank()) {
            return userRepository.findAll(pageable);
        }

        return userRepository.searchByKeyword(keyword, pageable);
    }

    private Pageable buildPageable(String sort, int page, int size) {
        Sort sortObj = Sort.unsorted();
        if (sort != null && !sort.isBlank()) {
            // Ví dụ: "name,asc" hoặc "email,desc"
            String[] parts = sort.split(",");
            if (parts.length == 2) {
                String sortField = parts[0];
                Sort.Direction direction = Sort.Direction.fromString(parts[1]);
                sortObj = Sort.by(direction, sortField);
            }
        }
        return PageRequest.of(page, size, sortObj);
    }

    private Page<User> searchUsersByKeyword(String keyword, Pageable pageable) {
        return userRepository.findByFullNameOrEmail(keyword, keyword, pageable);
    }

    @Override
    public User getById(String id) {
        return userRepository.getById(id);
    }

    @Override
    public User create(User request) {
        validateUserUnique(request);
        setDefaultRoleIfMissing(request);
        return userRepository.save(request);
    }

    private void validateUserUnique(User user) {
        if (userRepository.existsById(user.getId())) {
            throw new RuntimeException("Id already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
    }

    private void setDefaultRoleIfMissing(User user) {
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
    }

    @Override
    public void update(UserRequest request) {
        User existingUser = userRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        mapRequestToEntity(existingUser, request);
        userRepository.save(existingUser);
    }

    private void mapRequestToEntity(User user, UserRequest request) {
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
    }


    @Override
    public void delete(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
}
