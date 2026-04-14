package com.flc.service;

import com.flc.domain.Member;
import com.flc.exception.EntityNotFoundException;
import com.flc.repository.MemberRepository;

import java.util.List;

public final class MemberService {
    private static final String MEMBER_ID_PATTERN = "M\\d+";

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> listMembers() {
        return memberRepository.findAll();
    }

    public Member registerMember(String memberName) {
        String memberId = nextMemberId();
        Member member = new Member(memberId, memberName.trim());
        return memberRepository.save(member);
    }

    public Member getMember(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found: " + memberId));
    }

    private String nextMemberId() {
        int nextNumber = memberRepository.findAll().stream()
                .map(Member::id)
                .mapToInt(this::memberNumber)
                .max()
                .orElse(0) + 1;
        return "M%03d".formatted(nextNumber);
    }

    private int memberNumber(String memberId) {
        if (!memberId.matches(MEMBER_ID_PATTERN)) {
            return 0;
        }
        return Integer.parseInt(memberId.substring(1));
    }
}
