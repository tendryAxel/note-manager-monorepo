package com.note.api.note_manager.models;

import com.note.api.note_manager.utils.date.DateTypeEnumMapper;
import com.note.api.note_manager.utils.date.DateUtils;
import jakarta.persistence.*;
import java.time.Duration;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "session_token")
public class SessionToken {
  @Id
  @Column(nullable = false)
  private String token;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserInfo user;

  @Column(name = "create_at")
  private Date createdAt;

  @Column(name = "expire_at")
  private Date expireAt;

  public SessionToken(String token, UserInfo user, Duration expiration) {
    var calendar = new DateUtils(new DateTypeEnumMapper());
    this.token = token;
    this.user = user;
    this.createdAt = new Date();
    this.expireAt = calendar.add(this.createdAt, expiration);
  }
}
