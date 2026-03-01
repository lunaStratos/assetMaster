package com.lunastratos.asset.AssetMaster.controller;

import com.lunastratos.asset.AssetMaster.common.ApiResponse;
import com.lunastratos.asset.AssetMaster.config.JwtProvider;
import com.lunastratos.asset.AssetMaster.dto.LoginRequest;
import com.lunastratos.asset.AssetMaster.dto.SignupRequest;
import com.lunastratos.asset.AssetMaster.dto.TokenResponse;
import com.lunastratos.asset.AssetMaster.entity.Member;
import com.lunastratos.asset.AssetMaster.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증", description = "회원가입, 로그인, 토큰 갱신")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    public AuthController(MemberService memberService, JwtProvider jwtProvider) {
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
    }

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(@Valid @RequestBody SignupRequest request) {
        memberService.signup(request);
        return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다.", null));
    }

    @Operation(summary = "로그인", description = "로그인 성공 시 accessToken, refreshToken 반환")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        Member member = memberService.authenticate(request.getLoginId(), request.getPassword())
                .orElse(null);

        if (member == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error(401, "아이디 또는 비밀번호가 일치하지 않습니다."));
        }

        String accessToken = jwtProvider.createAccessToken(member.getId(), member.getLoginId());
        String refreshToken = jwtProvider.createRefreshToken(member.getId(), member.getLoginId());
        memberService.updateRefreshToken(member.getId(), refreshToken);

        return ResponseEntity.ok(ApiResponse.success(new TokenResponse(accessToken, refreshToken)));
    }

    @Operation(summary = "토큰 갱신", description = "refreshToken으로 새 accessToken/refreshToken 발급")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(@RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.replace("Bearer ", "");

        if (!jwtProvider.validateToken(token)) {
            return ResponseEntity.status(401).body(ApiResponse.error(401, "유효하지 않은 토큰입니다."));
        }

        Member member = memberService.findByRefreshToken(token).orElse(null);
        if (member == null) {
            return ResponseEntity.status(401).body(ApiResponse.error(401, "유효하지 않은 리프레시 토큰입니다."));
        }

        String newAccessToken = jwtProvider.createAccessToken(member.getId(), member.getLoginId());
        String newRefreshToken = jwtProvider.createRefreshToken(member.getId(), member.getLoginId());
        memberService.updateRefreshToken(member.getId(), newRefreshToken);

        return ResponseEntity.ok(ApiResponse.success(new TokenResponse(newAccessToken, newRefreshToken)));
    }
}
