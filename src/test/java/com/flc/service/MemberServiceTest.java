package com.flc.service;

import com.flc.domain.Member;
import com.flc.repository.MemberRepository;
import com.flc.repository.inmemory.InMemoryMemberRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MemberServiceTest {
    @Test
    void registerMemberUsesNextHighestExistingMemberId() {
        MemberRepository memberRepository = new InMemoryMemberRepository();
        memberRepository.save(new Member("M010", "Existing Member"));
        MemberService memberService = new MemberService(memberRepository);

        Member member = memberService.registerMember("New Member");

        assertEquals("M011", member.id());
    }
}
