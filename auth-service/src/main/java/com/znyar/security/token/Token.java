package com.znyar.security.token;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_token", schema = "token_schema")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_schema.t_token_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "c_token", nullable = false)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "c_type", nullable = false)
    private TokenType type;

    @Column(name = "c_expired", nullable = false)
    private boolean expired;

    @Column(name = "c_revoked", nullable = false)
    private boolean revoked;

    @Column(name = "c_user_id", nullable = false)
    private Long userId;

}
