package com.example.springboot_board_v4.model.board;

import lombok.Data;

@Data
public class AttechedFile {
    private long id;
    private long board_id;
    private String originalFile;
    private String savedFile;
    private String filePath;

    public AttechedFile(String originalFile, String savedFile) {
        this.originalFile = originalFile;
        this.savedFile = savedFile;
    }
}
