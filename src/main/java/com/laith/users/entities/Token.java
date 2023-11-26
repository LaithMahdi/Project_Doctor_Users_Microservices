package com.laith.users.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int code;

    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiredAt);
    }
}
