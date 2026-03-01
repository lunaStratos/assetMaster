package com.lunastratos.asset.AssetMaster.service;

import com.lunastratos.asset.AssetMaster.dto.SignupRequest;
import com.lunastratos.asset.AssetMaster.entity.Member;
import com.lunastratos.asset.AssetMaster.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Member signup(SignupRequest request) {
        if (memberRepository.existsByLoginId(request.getLoginId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        Member member = new Member();
        member.setLoginId(request.getLoginId());
        member.setPassword(hashSha512(request.getPassword()));
        member.setName(request.getName());
        member.setPhone(request.getPhone());
        member.setActiveYn("Y");

        return memberRepository.save(member);
    }

    public Optional<Member> authenticate(String loginId, String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(m -> "Y".equals(m.getActiveYn()))
                .filter(m -> m.getPassword().equals(hashSha512(password)));
    }

    @Transactional
    public void updateRefreshToken(Long memberId, String refreshToken) {
        memberRepository.findById(memberId).ifPresent(m -> {
            m.setRefreshToken(refreshToken);
            memberRepository.save(m);
        });
    }

    public Optional<Member> findByRefreshToken(String refreshToken) {
        return memberRepository.findByRefreshToken(refreshToken);
    }

    public static String hashSha512(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-512 not available", e);
        }
    }
}
