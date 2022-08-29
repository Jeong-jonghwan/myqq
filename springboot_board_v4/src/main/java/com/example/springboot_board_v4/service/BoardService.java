package com.example.springboot_board_v4.service;

import com.example.springboot_board_v4.model.board.AttechedFile;
import com.example.springboot_board_v4.model.board.Board;
import com.example.springboot_board_v4.repository.BoardMapper;
import com.example.springboot_board_v4.util.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BoardService {
    
    private final BoardMapper boardMapper;
    private final FileService fileService;

    @Value("${file.upload.path}")
    private String uploadPath;

    // 게시물 등록
    @Transactional
    public void saveBoard(Board board, MultipartFile file) {
        // 첨부파일 저장
        boardMapper.saveBoard(board);
        log.info("file.isEmpty() : {}", file.isEmpty());
        if (file != null && !file.isEmpty()) {
            AttechedFile attechedFile = fileService.saveFile(file);
            attechedFile.setBoard_id(board.getId());
            boardMapper.saveFile(attechedFile);
        }
    }

    // 게시물 전체 검색
    public List<Board> findAllBoards() {
        List<Board> boards = boardMapper.findAllBoards();
        if (boards == null || boards.size() == 0) {
            return null;
        }
        return boards;
    }

    public int getTotal(String searchText) {
        return boardMapper.getTotal(searchText);
    }

    public List<Board> findBoards(String searchText, int startRecord, int countPerPage) {
        // 전체 검색 결과 중 시작 위치와 갯수
        RowBounds rb = new RowBounds(startRecord, countPerPage);

        // 검색어와 읽을 범위 전달
        return boardMapper.findBoards(searchText, rb);
    }

    // 게시물 검색(게시물 ID)
    public Board findBoard(long id) {
        Board findBoard = boardMapper.findBoardById(id);
        return findBoard;
    }

    // 첨부파일 검색(게시물 ID)
    public AttechedFile findAttechedFile(long board_id) {
        return boardMapper.findAttechedFile(board_id);
    }

    // 첨부파일 검색(파일 ID)
    public AttechedFile findAttechedFileByFileId(long id) {
        return boardMapper.findAttechedFileByFileId(id);
    }

    // 게시물 읽기
    public Board readBoard(long id) {
        Board findBoard = boardMapper.findBoardById(id);
        // 조회수 증가
        findBoard.addHit();
        boardMapper.updateBoard(findBoard);
        return findBoard;
    }

    // 게시물 수정
    @Transactional
    public void updateBoard(Board board, MultipartFile file) {
        // 첨부파일이 있으면 기존 파일을 수정하고 업로드
    	log.info("file : {}", file);
    	log.info("file.isEmpty() : {}", file.isEmpty());
    	Board findBoard = boardMapper.findBoardById(board.getId());
        if (file != null && !file.isEmpty()) {
            AttechedFile attechedFile = boardMapper.findAttechedFile(board.getId());
            if (attechedFile != null) {
            	String fullPath = uploadPath + "/" +attechedFile.getSavedFile();
            	fileService.deleteFile(fullPath);            	
            	AttechedFile savedFile = fileService.saveFile(file);
            	savedFile.setId(attechedFile.getId());
            	savedFile.setBoard_id(attechedFile.getBoard_id());
            	boardMapper.updateFile(savedFile);
            } else {
            	AttechedFile savedFile = fileService.saveFile(file);
            	savedFile.setBoard_id(findBoard.getId());
            	boardMapper.saveFile(savedFile);
            }
        }
        
        boardMapper.updateBoard(board);
    }

    // 게시물 삭제
    @Transactional
    public void removeBoard(long id) {
        Board board = boardMapper.findBoardById(id);
        if (board != null) {
            AttechedFile attechedFile = boardMapper.findAttechedFile(id);
            if (attechedFile != null) {
                String fullPath = uploadPath + "/" +attechedFile.getSavedFile();
                boolean result = fileService.deleteFile(fullPath);
                if (result) {
                    log.info("파일 삭제 성공!!");
                    boardMapper.removeFile(id);
                } else {
                    log.info("파일 삭제 실패!!");
                }
            }
            boardMapper.removeBoard(id);
        }
    }
}
