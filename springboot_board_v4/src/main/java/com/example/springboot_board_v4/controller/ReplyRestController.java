package com.example.springboot_board_v4.controller;

import com.example.springboot_board_v4.model.board.ReplyDTO;
import com.example.springboot_board_v4.service.ReplyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.springboot_board_v4.config.UserInfo;
import com.example.springboot_board_v4.model.board.Reply;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequestMapping(path = "/reply")
@RequiredArgsConstructor
@RestController
public class ReplyRestController {
    
    private final ReplyService replyService;
    
    // 리플 등록
    @PostMapping(value = "/write")
    public ResponseEntity<String> writeReply(@ModelAttribute Reply reply,
                                             @AuthenticationPrincipal UserInfo userInfo) {
        log.info("reply : {}", reply);
        reply.setMember_id(userInfo.getUsername());
        replyService.writeReply(reply);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 리플 목록
    @GetMapping(value = "/replies")
    public ResponseEntity<List<ReplyDTO>> getReplies(@RequestParam("boardId") long boardId,
                                                  @AuthenticationPrincipal UserInfo userInfo) {
        List<Reply> replies = replyService.getReplies(boardId);
        List<ReplyDTO> replyDTOs = new ArrayList<>();
        if (replies != null && replies.size() > 0) {
            for (Reply reply : replies) {
                ReplyDTO replyDTO = reply.toDTO();
                if (reply.getMember_id().equals(userInfo.getUsername())) {
                    replyDTO.setWriter(true);
                }
                replyDTOs.add(replyDTO);
            }
        }
        return new ResponseEntity<>(replyDTOs, HttpStatus.OK);
    }

    // 리플 수정
    @PostMapping(value = "/update")
    public ResponseEntity<String> updateReply(@ModelAttribute Reply reply,
                                              @AuthenticationPrincipal UserInfo userInfo) {
        log.info("Reply : {}", reply);
        replyService.updateReply(reply, userInfo.getUsername());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 리플 삭제
    @GetMapping(value = "/remove")
    public ResponseEntity<String> removeReply(@RequestParam("replyId") Long replyId,
                                              @AuthenticationPrincipal UserInfo userInfo) {
        String result = replyService.removeReply(replyId, userInfo.getUsername());
        switch (result) {
            case "SUCCESS" : return new ResponseEntity<>(HttpStatus.OK);
            case "NO_AUTH" : return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            case "NOT_FOUND" : return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
