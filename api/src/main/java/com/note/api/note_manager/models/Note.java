package com.note.api.note_manager.models;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "note")
public class Note {
  @Id @Column private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserInfo user;

  @Column(name = "create_at")
  private Date createdAt;

  @Column private String title;

  @Column private String content;
}
