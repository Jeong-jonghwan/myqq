package com.example.springboot_board_v4.controller;

import com.example.springboot_board_v4.config.UserInfo;
import com.example.springboot_board_v4.model.board.AttechedFile;
import com.example.springboot_board_v4.model.board.Board;
import com.example.springboot_board_v4.model.board.UpdateBoardForm;
import com.example.springboot_board_v4.model.board.WriteBoardForm;
import com.example.springboot_board_v4.model.member.Member;
import com.example.springboot_board_v4.service.BoardService;
import com.example.springboot_board_v4.util.PageNavigator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/board")
@Controller
public class BoardController {
    
    private final BoardService boardService;

    @Value("${file.upload.path}")
    private String uploadPath;

    // 게시판 관련 상수값
    final int countPerPage = 10;    // 페이지 당 글 수
    final int pagePerGroup = 5;     // 페이지 이동 그룹 당 표시할 페이지 수

    // 게시물 작성 폼
    @GetMapping(value = "/write")
    public String writeForm(Model model) {
        model.addAttribute("writeBoardForm", new WriteBoardForm());
        return "board/writeForm";
    }

    // 게시물 등록
    @PostMapping(value = "/write")
    public String write(@Validated @ModelAttribute("writeBoardForm") WriteBoardForm writeBoardForm, BindingResult bindingResult, 
                        @RequestParam(required = false) MultipartFile file, 
                        @AuthenticationPrincipal UserInfo userInfo) {
        log.info("writeBoardForm : {}", writeBoardForm);
        log.info("file : {}", file);
        if (bindingResult.hasErrors()) {
            return "board/writeForm";
        }
        Board board = writeBoardForm.toBoard();
        board.setMember_id(userInfo.getUsername());
        boardService.saveBoard(board, file);
        return "redirect:/board/list";
    }

    // 게시물 전체 목록
    @GetMapping(value = "/list")
    public String list(@RequestParam(value = "page", defaultValue = "1") int page,
                       @RequestParam(value = "searchText", defaultValue = "") String searchText,
                       Model model) { 
        log.info("searchText : {}", searchText);

        int total = boardService.getTotal(searchText);

        // 페이지 계산을 위한 객체 생성
        PageNavigator navi = new PageNavigator(countPerPage, pagePerGroup, page, total);

        // 검색어와 시작위치, 페이지 당 글 수를 전달하여 목록 읽기
        List<Board> boards = boardService.findBoards(searchText, navi.getStartRecord(), navi.getCountPerPage());

        log.info("boards : {}", boards);
        model.addAttribute("boards", boards);
        model.addAttribute("navi", navi);
        model.addAttribute("searchText", searchText);
        return "board/list";
    }

    // 게시물 조회
    @GetMapping(value = "/read/{id}")
    public String read(@PathVariable long id, 
                       @AuthenticationPrincipal UserInfo userInfo,
                       Model model) {
        Board board = boardService.readBoard(id);
        AttechedFile file = boardService.findAttechedFile(id);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("board", board);
        model.addAttribute("file", file);
        return "board/read";
    }

    // 게시물 수정 폼
    @GetMapping(value = "/update/{id}")
    public String updateForm(@PathVariable long id, 
                             @AuthenticationPrincipal UserInfo userInfo,
                             Model model) {
        // 게시물 검색
        Board findBoard = boardService.findBoard(id);
        // 게시글 작성자가 맞는지 확인
        if (!userInfo.getUsername().equals(findBoard.getMember_id())) {
            log.info("작성자와 로그인한 사용자가 다름!!");
            return "redirect:/board/list";
        }
        AttechedFile file = boardService.findAttechedFile(id);
        model.addAttribute("file", file);
        model.addAttribute("updateBoardForm", findBoard.toUpdateForm());
        return "board/updateForm";
    }

    // 게시물 수정
    @PostMapping(value = "/update")
    public String update(@Validated @ModelAttribute("updateBoardForm") UpdateBoardForm updateBoardForm, BindingResult bindingResult, 
                         @RequestParam(required = false) MultipartFile file,
                         @AuthenticationPrincipal UserInfo userInfo) {
        log.info("updateBoardForm : {}", updateBoardForm);
        log.info("file : {}", file);
        
        if (bindingResult.hasErrors()) {
           return "board/updateForm";
        }

        Board findBoard = boardService.findBoard(updateBoardForm.getId());
        // 게시글 작성자가 맞는지 확인
        if (!userInfo.getUsername().equals(findBoard.getMember_id())) {
            log.info("작성자와 로그인한 사용자가 다름!!");
            return "redirect:/board/list";
        }

        findBoard.setTitle(updateBoardForm.getTitle());
        findBoard.setContent(updateBoardForm.getContent());
        boardService.updateBoard(findBoard, file);
        return "redirect:/board/list";
    }

    // 게시물 삭제
    @GetMapping(value = "/remove/{id}")
    public String remove(@PathVariable long id, 
                         @AuthenticationPrincipal UserInfo userInfo) {
        log.info("id : {}", id);
        Board findBoard = boardService.findBoard(id);
         // 게시글 작성자가 맞는지 확인
         if (!userInfo.getUsername().equals(findBoard.getMember_id())) {
            log.info("작성자와 로그인한 사용자가 다름!!");
            return "redirect:/board/list";
        }
        boardService.removeBoard(id);
        return "redirect:/board/list";
    }

    // 파일 다운로드
    @GetMapping(value = "/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable long id) throws MalformedURLException {
        log.info("id : {}", id);
        AttechedFile attechedFile = boardService.findAttechedFileByFileId(id);
        String fullPath = uploadPath + "/" +attechedFile.getSavedFile();
        UrlResource resource = new UrlResource("file:" + fullPath);
        String encodingFileName = UriUtils.encode(attechedFile.getOriginalFile(), StandardCharsets.UTF_8);
        String contentDisposition = "attechment; filename=\"" + encodingFileName + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
