package com.example.springboot_board_v4.controller;

import com.example.springboot_board_v4.config.UserInfo;
import com.example.springboot_board_v4.model.member.CreateMemberForm;
import com.example.springboot_board_v4.model.member.LoginForm;
import com.example.springboot_board_v4.model.member.Member;
import com.example.springboot_board_v4.model.member.RoleType;
import com.example.springboot_board_v4.model.member.UpdateMemberForm;
import com.example.springboot_board_v4.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/member")
@Controller
public class MemberController {
    
    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입 폼
    @GetMapping(path = "/join")
    public String joinForm(Model model) {
        model.addAttribute("createMemberForm", new CreateMemberForm());
        return "member/joinForm";
    }

    // 회원가입
    @PostMapping(value = "/join")
    public String join(@Validated @ModelAttribute("createMemberForm") CreateMemberForm createMemberForm, BindingResult bindingResult) {
        log.info("createMemberForm : {}", createMemberForm);

        if (bindingResult.hasErrors()) {
            return "member/joinForm";
        }
        Member member = createMemberForm.toMember();
        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
        member.setRole(RoleType.ROLE_USER);
        memberService.saveMember(member);
        return "redirect:/";
    }
    
    // 로그인 폼
    @GetMapping(value = "/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "member/loginForm";
    }

    // 로그인 성공
    @GetMapping(value = "/login-success")
    public String login_success(@AuthenticationPrincipal UserInfo userInfo) {
        log.info("userInfo : {}", userInfo);
        return "redirect:/board/list";
    }

    // 로그인 실패
    @GetMapping(value = "/login-fail")
    public String login_fail() {
        log.info("login-fail");
        return "redirect:/member/login";
    }

    // 로그아웃
    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    // 회원정보 수정 폼
    @GetMapping(value = "/update/{id}")
    public String updateForm(@PathVariable String id, Model model) {
        Member findMember = memberService.findMember(id);
        model.addAttribute("member", findMember.toUpdateMemberForm());
        return "member/updateForm";
    }

    // 회원정보 수정
    @PostMapping(value = "/update")
    public String update(@Validated @ModelAttribute("updateMemberForm") UpdateMemberForm updateMemberForm, BindingResult bindingResult) {
        return "redirect:/";
    }

    // 아이디 중복 체크
    @ResponseBody
//    @GetMapping(value = "/checkIdDuplicate")
    public Member checiIdDuplicate(@RequestParam("id") String id) {
        log.info("id : {}", id);
        Member findMember = memberService.findMember(id);

        return findMember;
    }

    // 아이디 중복 체크
    @ResponseBody
    @GetMapping(value = "/checkIdDuplicate")
    public ResponseEntity checiIdDuplicateV2(@RequestParam("id") String id) {
        log.info("id : {}", id);
        Member findMember = memberService.findMember(id);
        if (findMember == null) {
            return new ResponseEntity(null, HttpStatus.OK);
        }
        return new ResponseEntity(findMember.getId(), HttpStatus.OK);
    }
}