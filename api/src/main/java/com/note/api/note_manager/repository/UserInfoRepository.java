package com.note.api.note_manager.repository;

import com.note.api.note_manager.models.UserInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, String> {
  Optional<UserInfo> findUserInfoByEmail(String email);
}
