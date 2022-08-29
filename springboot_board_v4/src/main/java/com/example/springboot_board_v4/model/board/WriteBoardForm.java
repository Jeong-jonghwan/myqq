package com.example.springboot_board_v4.model.board;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WriteBoardForm {
    @NotBlank
    private String title;
    @NotBlank
    private String content;

    public Board toBoard() {
        Board board = new Board();
        board.setTitle(title);
        board.setContent(content);
        return board;
    }
}