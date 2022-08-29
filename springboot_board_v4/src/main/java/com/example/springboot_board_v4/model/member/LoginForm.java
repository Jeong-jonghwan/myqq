package com.example.springboot_board_v4.model.member;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class LoginForm {
    @Size(min = 4, max = 20)
    private String id;
    @Size(min = 4, max = 30)
    private String password;

    public Member toMember() {
        Member member = new Member();
        member.setId(id);
        member.setPassword(password);
        return member;
    }
}
