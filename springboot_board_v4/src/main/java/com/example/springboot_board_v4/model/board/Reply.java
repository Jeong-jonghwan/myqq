package com.example.springboot_board_v4.model.board;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class Reply {
    private long id;
    private long board_id;
    private String member_id;
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime inputTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updateTime;

    public ReplyDTO toDTO() {
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setId(id);
        replyDTO.setBoard_id(board_id);
        replyDTO.setMember_id(member_id);
        replyDTO.setContent(content);
        replyDTO.setInputTime(inputTime);
        replyDTO.setUpdateTime(updateTime);
        return replyDTO;
    }
}
