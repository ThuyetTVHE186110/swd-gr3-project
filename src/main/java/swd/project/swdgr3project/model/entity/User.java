package swd.project.swdgr3project.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity class representing a user in the system.
 * Users can be customers, managers, sales, or system admins.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String email;
    private String password; // Stored as hashed value
    private String fullName;
    private String phone;
    private String address;
    private String profileImageUrl; // Cloudinary URL
    private UserRole role;
    private AuthProvider authProvider; // GOOGLE or LOCAL
    private String providerId; // For OAuth users
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Enum representing the authentication provider
     */
    public enum AuthProvider {
        LOCAL,
        GOOGLE
    }
    
    /**
     * Enum representing user roles in the system
     */
    public enum UserRole {
        CUSTOMER,
        MANAGER,
        SALES,
        ADMIN
    }
}