package com.example.springboot_board_v4.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.springboot_board_v4.model.board.Reply;

@Mapper
public interface ReplyMapper {
    // 리플 등록
    int saveReply(Reply reply);

    // 리플 읽기
    Reply findReply(long replyId);

    // 리플 목록
    List<Reply> findReplies(long boardId);

    // 리플 수정
    int updateReply(Reply reply);

    // 리플 삭제
    int removeReply(long id);
}
