package com.example.springboot_board_v4.repository;

import com.example.springboot_board_v4.model.member.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    // 회원가입
    public int saveMember(Member member);
    
    // 회원정보 검색(ID)
    public Member findMemberById(String id);

    // 회원정보 검색(ID, PW)
    public Member findMemberByIdAndPassword(Member member);

    // 회원정보 수정
    public int updateMember(Member member);

}
