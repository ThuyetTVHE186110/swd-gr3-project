package swd.project.swdgr3project.dao;

import swd.project.swdgr3project.model.entity.User;
import swd.project.swdgr3project.model.entity.User.AuthProvider;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for User entity.
 * Provides methods to interact with the user data in the database.
 */
public interface UserDAO {
    
    /**
     * Save a new user to the database
     * 
     * @param user The user to save
     * @return The saved user with generated ID
     */
    User save(User user);
    
    /**
     * Update an existing user in the database
     * 
     * @param user The user to update
     * @return The updated user
     */
    User update(User user);
    
    /**
     * Find a user by their ID
     * 
     * @param id The ID of the user to find
     * @return An Optional containing the user if found, or empty if not found
     */
    Optional<User> findById(Long id);
    
    /**
     * Find a user by their username
     * 
     * @param username The username to search for
     * @return An Optional containing the user if found, or empty if not found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find a user by their email address
     * 
     * @param email The email to search for
     * @return An Optional containing the user if found, or empty if not found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find a user by their OAuth provider ID
     * 
     * @param provider The authentication provider (e.g., GOOGLE)
     * @param providerId The provider-specific user ID
     * @return An Optional containing the user if found, or empty if not found
     */
    Optional<User> findByProviderAndProviderId(AuthProvider provider, String providerId);
    
    /**
     * Get all users from the database
     * 
     * @return A list of all users
     */
    List<User> findAll();
    
    /**
     * Delete a user from the database
     * 
     * @param id The ID of the user to delete
     * @return true if the user was deleted, false otherwise
     */
    boolean delete(Long id);
    
    /**
     * Deactivate a user (set active flag to false)
     * 
     * @param id The ID of the user to deactivate
     * @return true if the user was deactivated, false otherwise
     */
    boolean deactivate(Long id);
    
    /**
     * Activate a user (set active flag to true)
     * 
     * @param id The ID of the user to activate
     * @return true if the user was activated, false otherwise
     */
    boolean activate(Long id);
}