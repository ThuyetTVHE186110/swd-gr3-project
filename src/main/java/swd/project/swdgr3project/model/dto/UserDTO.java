package swd.project.swdgr3project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import swd.project.swdgr3project.model.entity.User;

/**
 * DTO class for transferring user data between layers.
 * Contains only the necessary fields for UI display and doesn't include sensitive information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private String profileImageUrl;
    private User.UserRole role;
    private User.AuthProvider authProvider;
    
    /**
     * Converts a User entity to a UserDTO
     * @param user The User entity to convert
     * @return A UserDTO containing non-sensitive user information
     */
    public static UserDTO fromEntity(User user) {
        if (user == null) return null;
        
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setProfileImageUrl(user.getProfileImageUrl());
        dto.setRole(user.getRole());
        dto.setAuthProvider(user.getAuthProvider());
        
        return dto;
    }
}