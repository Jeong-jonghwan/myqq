package com.example.springboot_board_v4.service;

import com.example.springboot_board_v4.model.member.Member;
import com.example.springboot_board_v4.repository.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
// @Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {
    
    private final MemberMapper memberMapper;

    // 회원가입
    public void saveMember(Member member) {
        memberMapper.saveMember(member);
    }

    // 회원정보 검색(ID)
    public Member findMember(String id) {
        return memberMapper.findMemberById(id);
    }

    // 회원정보 검색(ID, PW)
    public Member findMember(Member member) {
        return memberMapper.findMemberByIdAndPassword(member);
    }

    // 회원정보 수정
    public void updateMember(Member member) {
        memberMapper.updateMember(member);
    }
}
