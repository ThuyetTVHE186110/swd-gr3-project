package swd.project.swdgr3project.service.impl;

import swd.project.swdgr3project.dao.UserDAO;
import swd.project.swdgr3project.dao.impl.UserDAOImpl;
import swd.project.swdgr3project.model.dto.UserDTO;
import swd.project.swdgr3project.entity.User;
import swd.project.swdgr3project.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of UserService interface.
 */
public class UserServiceImpl implements UserService {
    
    private final UserDAO userDAO = new UserDAOImpl();
    
    @Override
    public UserDTO registerUser(String username, String email, String password, String fullName) {
        // Check if username or email already exists
        if (userDAO.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userDAO.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(hashPassword(password)); // You should implement proper password hashing
        user.setFullName(fullName);
        user.setRole("CUSTOMER");
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        User savedUser = userDAO.save(user);
        return UserDTO.fromEntity(savedUser);
    }
    
    @Override
    public UserDTO processGoogleUser(String email, String fullName, String googleId) {
        User existingUser = userDAO.findByEmail(email).orElse(null);
        
        if (existingUser != null) {
            // Update existing user with Google ID
            existingUser.setGoogleId(googleId);
            existingUser.setUpdatedAt(LocalDateTime.now());
            User updatedUser = userDAO.update(existingUser);
            return UserDTO.fromEntity(updatedUser);
        } else {
            // Create new user
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFullName(fullName);
            newUser.setUsername(generateUsernameFromEmail(email));
            newUser.setGoogleId(googleId);
            newUser.setRole("CUSTOMER");
            newUser.setActive(true);
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setUpdatedAt(LocalDateTime.now());
            
            User savedUser = userDAO.save(newUser);
            return UserDTO.fromEntity(savedUser);
        }
    }
    
    @Override
    public UserDTO authenticateUser(String usernameOrEmail, String password) {
        User user = userDAO.findByUsername(usernameOrEmail).orElse(null);
        if (user == null) {
            user = userDAO.findByEmail(usernameOrEmail).orElse(null);
        }
        
        if (user == null || !verifyPassword(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        
        if (!user.isActive()) {
            throw new IllegalArgumentException("Account is deactivated");
        }
        
        return UserDTO.fromEntity(user);
    }
    
    @Override
    public UserDTO getUserById(Long id) {
        User user = userDAO.findById(id).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return UserDTO.fromEntity(user);
    }
    
    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userDAO.findByUsername(username).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return UserDTO.fromEntity(user);
    }
    
    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userDAO.findByEmail(email).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return UserDTO.fromEntity(user);
    }
    
    @Override
    public UserDTO updateUserProfile(Long userId, String fullName, String phone, String address, String profileImageUrl) {
        User user = userDAO.findById(userId).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        
        if (fullName != null) user.setFullName(fullName);
        if (phone != null) user.setPhone(phone);
        if (address != null) user.setAddress(address);
        if (profileImageUrl != null) user.setAvatarUrl(profileImageUrl); // Map to avatarUrl
        user.setUpdatedAt(LocalDateTime.now());
        
        User updatedUser = userDAO.update(user);
        return UserDTO.fromEntity(updatedUser);
    }
    
    @Override
    public boolean changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userDAO.findById(userId).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        
        if (!verifyPassword(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        
        user.setPassword(hashPassword(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        
        userDAO.update(user);
        return true;
    }
    
    @Override
    public List<UserDTO> getAllUsers() {
        return userDAO.findAll().stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<UserDTO> getUsersByRole(String role) {
        return userDAO.findByRole(role).stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<UserDTO> getActiveUsers() {
        return userDAO.findByActive(true).stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<UserDTO> getUsersWithPagination(int page, int limit) {
        return userDAO.findWithPagination(page, limit).stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean deactivateUser(Long userId) {
        User user = userDAO.findById(userId).orElse(null);
        if (user == null) {
            return false;
        }
        
        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        userDAO.update(user);
        return true;
    }
    
    @Override
    public boolean activateUser(Long userId) {
        User user = userDAO.findById(userId).orElse(null);
        if (user == null) {
            return false;
        }
        
        user.setActive(true);
        user.setUpdatedAt(LocalDateTime.now());
        userDAO.update(user);
        return true;
    }
    
    @Override
    public boolean deleteUser(Long userId) {
        return userDAO.delete(userId);
    }
    
    @Override
    public UserDTO updateUserRole(Long userId, String newRole) {
        User user = userDAO.findById(userId).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        
        user.setRole(newRole);
        user.setUpdatedAt(LocalDateTime.now());
        
        User updatedUser = userDAO.update(user);
        return UserDTO.fromEntity(updatedUser);
    }
    
    @Override
    public long getTotalUsersCount() {
        return userDAO.count();
    }
    
    @Override
    public long getActiveUsersCount() {
        return userDAO.findByActive(true).size();
    }
    
    @Override
    public long getUsersCountByRole(String role) {
        return userDAO.findByRole(role).size();
    }
    
    @Override
    public boolean isUsernameAvailable(String username) {
        return !userDAO.findByUsername(username).isPresent();
    }
    
    @Override
    public boolean isEmailAvailable(String email) {
        return !userDAO.findByEmail(email).isPresent();
    }

    // Helper methods
    
    private String hashPassword(String password) {
        // TODO: Implement proper password hashing (BCrypt, Argon2, etc.)
        // For now, return as-is (NEVER do this in production!)
        return password;
    }
    
    private boolean verifyPassword(String password, String hashedPassword) {
        // TODO: Implement proper password verification
        // For now, simple comparison (NEVER do this in production!)
        return password.equals(hashedPassword);
    }
    
    private String generateUsernameFromEmail(String email) {
        String[] parts = email.split("@");
        String baseUsername = parts[0].toLowerCase().replaceAll("[^a-z0-9]", "");
        
        // Check if username exists, if so, append number
        String username = baseUsername;
        int counter = 1;
        while (userDAO.findByUsername(username).isPresent()) {
            username = baseUsername + counter;
            counter++;
        }
        
        return username;
    }
}
