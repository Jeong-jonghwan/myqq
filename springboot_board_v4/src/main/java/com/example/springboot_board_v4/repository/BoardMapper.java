package com.example.springboot_board_v4.repository;

import com.example.springboot_board_v4.model.board.AttechedFile;
import com.example.springboot_board_v4.model.board.Board;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface BoardMapper {
    // 게시물 등록
    public int saveBoard(Board board);

    // 첨부파일 등록
    public int saveFile(AttechedFile file);

    // 게시물 전체 검색
    public List<Board> findAllBoards();

    // 검색 후 총 글 갯수
    public int getTotal(String searchText);

    // 검색 후 현재 페이지 목록
    public List<Board> findBoards(String searchText, RowBounds rb);

    // 첨부파일 검색(게시물 ID)
    public AttechedFile findAttechedFile(long board_id);

    // 첨부파일 검색(첨부파일 ID)
    public AttechedFile findAttechedFileByFileId(long id);

    // 첨부파일 수정
    public int updateFile(AttechedFile file);

    // 게시물 검색(게시물 ID)
    public Board findBoardById(long id);

    // 게시물 수정
    public int updateBoard(Board board);

    // 게시물 삭제
    public int removeBoard(long id);

    // 첨부파일 삭제
    public int removeFile(long id);
}
