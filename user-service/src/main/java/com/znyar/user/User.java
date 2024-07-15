package com.znyar.user;

import com.znyar.userinfo.UserInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_user", schema = "user_schema")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_schema.t_user_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "c_password", nullable = false)
    private String password;

    @Column(name = "c_email", nullable = false, unique = true, length = 100)
    private String email;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "user_info_id", referencedColumnName = "id")
    private UserInfo userInfo;

    @Column(name = "c_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "c_updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
