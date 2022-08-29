package com.example.springboot_board_v4.model.member;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Member {
    private String id;
    private String password;
    private String name;
    private GenderType gender;
    private LocalDate birth;
    private String email;
    private RoleType role;


    public UpdateMemberForm toUpdateMemberForm() {
        UpdateMemberForm updateMemberForm = new UpdateMemberForm();
        updateMemberForm.setId(id);
        updateMemberForm.setPassword(password);
        updateMemberForm.setName(name);
        updateMemberForm.setGender(gender);
        updateMemberForm.setBirth(birth);
        updateMemberForm.setEmail(email);
        return updateMemberForm;
    }
}
