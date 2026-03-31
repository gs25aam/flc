package com.flc.repository;

import com.flc.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);

    Optional<Member> findById(String memberId);

    List<Member> findAll();
}
