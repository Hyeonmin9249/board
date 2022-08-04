package com.CoffeeCommunity.board.service;

import com.CoffeeCommunity.board.entity.Board;
import com.CoffeeCommunity.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService { //이 보드 서비스는 보드 컨트롤러에서 이용한다.

    @Autowired // 이것을 사용하면 스프링에서 알아서 읽어와서 boardRepository 객체안에 주입을해준다 (called Dependency Injection)

    private BoardRepository boardRepository;

    //글 작성 처리
    public void write(Board board) { //Board클레스에 board라는 변수를 받아서 넣어준다.
        boardRepository.save(board);
    }

    public List<Board> getBoard() {
        return boardRepository.findAll();
    }

    //게시글 리스트 처리
    public Page<Board> boardList(Pageable pageable) { // 원래는 밑으로 전부 list view delete 이런식이여야하는데 메소드이름은 나중에 통일하기
        return boardRepository.findAll(pageable); //기존에 findAll안에 매개변수가 없는 경우에는 리턴값이 List로 넘어오는데 이경우에는 Page라는 클래스로 리턴하게됨
    }

    //특정 게시글 불러오기
    public Board boardView(Integer id) {
        return boardRepository.findById(id).get();
    }

    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {

        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }


    //특정 게시글 삭제
    public void boardDelete(Integer id) {
        boardRepository.deleteById(id);
    }
}
