package com.flc.service;

import com.flc.domain.Member;
import com.flc.exception.EntityNotFoundException;
import com.flc.repository.MemberRepository;

import java.util.List;

public final class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> listMembers() {
        return memberRepository.findAll();
    }

    public Member registerMember(String memberName) {
        String memberId = "M%03d".formatted(memberRepository.findAll().size() + 1);
        Member member = new Member(memberId, memberName.trim());
        return memberRepository.save(member);
    }

    public Member getMember(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found: " + memberId));
    }
}
