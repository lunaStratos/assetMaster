package com.lunastratos.asset.AssetMaster.config;

import com.lunastratos.asset.AssetMaster.entity.Member;
import com.lunastratos.asset.AssetMaster.repository.MemberRepository;
import com.lunastratos.asset.AssetMaster.service.MemberService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    private final MemberRepository memberRepository;

    public DataInitializer(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!memberRepository.existsByLoginId("admin")) {
            Member admin = new Member();
            admin.setLoginId("admin");
            admin.setPassword(MemberService.hashSha512("admin1234"));
            admin.setName("관리자");
            admin.setPhone("010-0000-0000");
            admin.setActiveYn("Y");
            memberRepository.save(admin);
        }
    }
}
