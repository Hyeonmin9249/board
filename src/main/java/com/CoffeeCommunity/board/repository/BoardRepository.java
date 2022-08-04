package com.CoffeeCommunity.board.repository;

import com.CoffeeCommunity.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> { //Board는 엔티티, Integer은 엔티티에 있는 ID가 정수이기때문에 이렇게 지정

    //JPA Repository
    //findBy(컬럼 이름) containing --> 컬럼에서 키워드가 포함된 것을 찾겠다.
    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable); //리턴 타입이 페이지라는것임 // 제네릭을 써서 보드라는 객체를 담겠다
}
