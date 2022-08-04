package com.CoffeeCommunity.board.controller;

import com.CoffeeCommunity.board.entity.Board;
import com.CoffeeCommunity.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;

    @GetMapping("/board/write") //localhost:8090/board/write  + //GetMapping은 localhost 8080 주소로 들어갔을때 역슬래쉬 경로로 들어왔을때 밑에 메소드를 실행, 즉 boardwrite html실행
    public String boardWriteForm() {

        return "boardwrite";
    }

    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("board", boardService.getBoard());
        return "index";
    }


    @PostMapping("/board/writepro") //boardwrite html안에있는 action과 일치해야한다
    public String boardWritePro(Board board){ //괄호안에 Board는 entity이다, 뒤에 보드는 객체
        boardService.write(board);

        return "redirect:/board/list";
    }


    @GetMapping("/board/list")
    public String boardList(Model model, @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, String searchKeyword) { //Pageable 인터페이스(JPA를 이용한 속성) - 한페이지에 사이즈 10개 // http://localhost:8080/board/list?page=2 이런식으로 페이지 호출

        Page<Board> list = null;

        if(searchKeyword == null) { //서치키워드가 안들어왔을때는 (검색하는 단어가 안들어왔을때) 기존 보드 리스트를 보여주면 되고
            list = boardService.boardList(pageable);
        }else { //만약 들어왔을경우에는 (검색하는 단어가 들어왔을때는) 서치리스트를 사용해서 서치키워드를 받아주고 pageable을 받아줘야한다
            list = boardService.boardSearchList(searchKeyword, pageable);
        }

        int nowPage = pageable.getPageNumber() + 1; // 현재 페이지 //Pageable이 가지고 있는 Page는 0부터 시작하기때문에 1을 더해줘야한다
        int startPage = Math.max(nowPage - 4, 1); //시작페이지
        int endPage = Math.min(nowPage + 5, list.getTotalPages()); //마지막페이지

        model.addAttribute("nowPage", nowPage); //위에 변수들을 넘겨주는 작업
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("list", list); //"boardService.boardList()"이것을 실행하면 리스트가 반환되는데 그 반환된 리스트를 "List"라는 이름으로 받아서 넘기겠다는 것이다
        return "boardlist";
    }


    @GetMapping("/board/view") // localhost:8080/board/view?id=1
    public String boardView(Model model, Integer id) {
        model.addAttribute("board", boardService.boardView(id));
        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id) {
        boardService.boardDelete(id);
        return "redirect:/board/list";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model) { //url의 파라미터를 넘길때 방법이 2가지가 있는데 기존에 쓰는 queryString이 있고 다른하나는 PathVariable을 쓰는건데
        model.addAttribute("board", boardService.boardView(id));
        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board) { //제목이랑 컨텐트를 받아오기 위해서 write컨트롤러와 같이 "Board board" 사용해야한다

        Board boardTemp = boardService.boardView(id); //기존의 있던 내용을 가지고 오고 새로 가져온 내용을 밑의 작업을 통해 덮어씌우기하는 과정
        boardTemp.setTitle(board.getTitle()); //새로 가져온 내용을 밑의 작업을 통해 덮어씌우기하는 작업 -- 수정할때 넘어오는 타이틀 가져오기
        boardTemp.setContent(board.getContent()); //새로 가져온 내용을 밑의 작업을 통해 덮어씌우기하는 작업 -- 수정할때 넘어오는 컨텐트 가져오기

        boardService.write(boardTemp);//덮어씌워진 내용을 저장밑 업데이트

        return "redirect:/board/list";

    }
}
