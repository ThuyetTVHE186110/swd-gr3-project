package swd.project.swdgr3project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import swd.project.swdgr3project.entity.User;

import java.time.LocalDateTime;

/**
 * DTO for transferring user data.
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
    private String avatarUrl;
    private String role;
    private String authProvider; // For compatibility with AdminServlet
    private String googleId;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserDTO fromEntity(User user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setRole(user.getRole() != null ? user.getRole() : "CUSTOMER");
        dto.setAuthProvider(user.getGoogleId() != null ? "GOOGLE" : "LOCAL");
        dto.setGoogleId(user.getGoogleId());
        dto.setActive(user.isActive());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        return dto;
    }

    // Convert DTO back to entity
    public User toEntity() {
        User user = new User();
        user.setId(this.id);
        user.setUsername(this.username);
        user.setEmail(this.email);
        user.setFullName(this.fullName);
        user.setPhone(this.phone);
        user.setAddress(this.address);
        user.setAvatarUrl(this.avatarUrl);
        user.setRole(this.role != null ? this.role : "CUSTOMER");
        user.setGoogleId(this.googleId);
        user.setActive(this.active);
        user.setCreatedAt(this.createdAt);
        user.setUpdatedAt(this.updatedAt);
        return user;
    }
}