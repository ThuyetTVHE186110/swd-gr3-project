package swd.project.swdgr3project.service;

import swd.project.swdgr3project.model.dto.UserDTO;
import swd.project.swdgr3project.model.entity.User;
import swd.project.swdgr3project.model.entity.User.AuthProvider;

import java.util.List;

/**
 * Service interface for user-related operations.
 */
public interface UserService {
    
    /**
     * Register a new user with traditional authentication.
     *
     * @param username The username
     * @param email The email address
     * @param password The password (will be hashed)
     * @param fullName The full name
     * @return The created user DTO
     * @throws IllegalArgumentException if username or email already exists
     */
    UserDTO registerUser(String username, String email, String password, String fullName);
    
    /**
     * Register or login a user with OAuth provider.
     *
     * @param email The email from OAuth provider
     * @param fullName The full name from OAuth provider
     * @param provider The OAuth provider (e.g., GOOGLE)
     * @param providerId The unique ID from the provider
     * @return The user DTO
     */
    UserDTO processOAuthUser(String email, String fullName, AuthProvider provider, String providerId);
    
    /**
     * Authenticate a user with username/email and password.
     *
     * @param usernameOrEmail The username or email
     * @param password The password
     * @return The authenticated user DTO
     * @throws IllegalArgumentException if authentication fails
     */
    UserDTO authenticateUser(String usernameOrEmail, String password);
    
    /**
     * Get a user by ID.
     *
     * @param id The user ID
     * @return The user DTO
     * @throws IllegalArgumentException if user not found
     */
    UserDTO getUserById(Long id);
    
    /**
     * Get a user by username.
     *
     * @param username The username
     * @return The user DTO
     * @throws IllegalArgumentException if user not found
     */
    UserDTO getUserByUsername(String username);
    
    /**
     * Get a user by email.
     *
     * @param email The email address
     * @return The user DTO
     * @throws IllegalArgumentException if user not found
     */
    UserDTO getUserByEmail(String email);
    
    /**
     * Update a user's profile information.
     *
     * @param userId The user ID
     * @param fullName The new full name (or null to keep current)
     * @param phone The new phone number (or null to keep current)
     * @param address The new address (or null to keep current)
     * @param profileImageUrl The new profile image URL (or null to keep current)
     * @return The updated user DTO
     * @throws IllegalArgumentException if user not found
     */
    UserDTO updateUserProfile(Long userId, String fullName, String phone, String address, String profileImageUrl);
    
    /**
     * Change a user's password.
     *
     * @param userId The user ID
     * @param currentPassword The current password for verification
     * @param newPassword The new password
     * @return true if password was changed successfully
     * @throws IllegalArgumentException if user not found or current password is incorrect
     */
    boolean changePassword(Long userId, String currentPassword, String newPassword);
    
    /**
     * Get all users.
     *
     * @return List of all user DTOs
     */
    List<UserDTO> getAllUsers();
    
    /**
     * Get users by role.
     *
     * @param role The role to filter by
     * @return List of user DTOs with the specified role
     */
    List<UserDTO> getUsersByRole(User.UserRole role);
    
    /**
     * Deactivate a user account.
     *
     * @param userId The user ID
     * @return true if the user was deactivated
     * @throws IllegalArgumentException if user not found
     */
    boolean deactivateUser(Long userId);
    
    /**
     * Activate a user account.
     *
     * @param userId The user ID
     * @return true if the user was activated
     * @throws IllegalArgumentException if user not found
     */
    boolean activateUser(Long userId);
    
    /**
     * Delete a user account.
     *
     * @param userId The user ID
     * @return true if the user was deleted
     * @throws IllegalArgumentException if user not found
     */
    boolean deleteUser(Long userId);
    
    /**
     * Check if a username is available (not already taken).
     *
     * @param username The username to check
     * @return true if the username is available
     */
    boolean isUsernameAvailable(String username);
    
    /**
     * Check if an email is available (not already taken).
     *
     * @param email The email to check
     * @return true if the email is available
     */
    boolean isEmailAvailable(String email);
}