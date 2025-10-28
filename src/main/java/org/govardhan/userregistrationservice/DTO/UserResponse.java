package org.govardhan.userregistrationservice.DTO;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username; // changed from
    private String email;
    private String message;

    public UserResponse(Long id, String username, String email, String message) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.message = message;
    }
}
