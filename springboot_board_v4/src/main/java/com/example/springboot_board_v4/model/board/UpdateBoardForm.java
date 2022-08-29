package com.example.springboot_board_v4.model.board;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateBoardForm {
    private long id;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private AttechedFile attechedFile;

    public Board toBoard() {
        Board board = new Board();
        board.setId(id);
        board.setTitle(title);
        board.setContent(content);
        board.setAttechedFile(attechedFile);
        return board;
    }
}