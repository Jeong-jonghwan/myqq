package com.example.springboot_board_v4.model.board;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class Board {
    private long id;
    private String title;
    private String content;
    private Long hit;
    private String member_id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime inputTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updateTime;
    private AttechedFile attechedFile;

    public void addHit() {
        hit += 1;
    }

    public UpdateBoardForm toUpdateForm() {
        UpdateBoardForm updateBoardForm = new UpdateBoardForm();
        updateBoardForm.setId(id);
        updateBoardForm.setTitle(title);
        updateBoardForm.setContent(content);
        updateBoardForm.setAttechedFile(attechedFile);
        return updateBoardForm;
    }
}
