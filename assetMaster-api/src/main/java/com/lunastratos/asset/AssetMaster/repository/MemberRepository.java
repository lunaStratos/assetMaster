package com.lunastratos.asset.AssetMaster.repository;

import com.lunastratos.asset.AssetMaster.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByRefreshToken(String refreshToken);
    boolean existsByLoginId(String loginId);
}
