package com.flc.repository.inmemory;

import com.flc.domain.Member;
import com.flc.repository.MemberRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class InMemoryMemberRepository implements MemberRepository {
    private final Map<String, Member> members = new LinkedHashMap<>();

    @Override
    public Member save(Member member) {
        members.put(member.id(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(String memberId) {
        return Optional.ofNullable(members.get(memberId));
    }

    @Override
    public List<Member> findAll() {
        List<Member> result = new ArrayList<>(members.values());
        result.sort(Comparator.comparing(Member::id));
        return result;
    }
}
