package com.znyar.userinfo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_user_info", schema = "user_schema")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_schema.t_user_info_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "c_firstname", nullable = false)
    private String firstName;

    @Column(name = "c_lastname", nullable = false)
    private String lastName;

    @Column(name = "c_address")
    private String address;

    @Column(name = "c_phone", unique = true)
    private String phone;

    @Column(name = "c_birth_date")
    private LocalDate birthDate;

    @Column(name = "c_gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "c_bio", columnDefinition = "TEXT")
    private String bio;

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