package com.example.springboot_board_v4.model.member;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class UpdateMemberForm {
    @Size(min = 4, max = 20)
    private String id;
    @Size(min = 2, max = 20)
    private String name;
    private String password;
    @NotEmpty
    private GenderType gender;
    @Past
    private LocalDate birth;
    private String email;

    public Member toMember() {
        Member member = new Member();
        member.setId(id);
        member.setName(name);
        member.setPassword(password);
        member.setGender(gender);
        member.setBirth(birth);
        member.setEmail(email);
        return member;
    }
}
