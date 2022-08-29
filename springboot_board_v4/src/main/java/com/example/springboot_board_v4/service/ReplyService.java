package com.example.springboot_board_v4.service;

import com.example.springboot_board_v4.model.board.Reply;
import com.example.springboot_board_v4.repository.ReplyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReplyService {

    private final ReplyMapper replyMapper;

    // 리플 등록
    public void writeReply(Reply reply) {
        replyMapper.saveReply(reply);
    }

    // 리플 목록
    public List<Reply> getReplies(long boardId) {
        return replyMapper.findReplies(boardId);
    }

    // 리플 수정
    public void updateReply(Reply reply, String username) {
        Reply findReply = replyMapper.findReply(reply.getId());
        if (findReply != null) {
            if (findReply.getMember_id().equals(username)) {
                replyMapper.updateReply(reply);
            }
        }
    }

    // 리플 삭제
    public String removeReply(long replyId, String username) {
        Reply findReply = replyMapper.findReply(replyId);
        if (findReply != null) {
            if (findReply.getMember_id().equals(username)) {
                replyMapper.removeReply(replyId);
                return "SUCCESS";
            }
            return "NO_AUTH";
        }
        return "NOT_FOUND";
    }
}
