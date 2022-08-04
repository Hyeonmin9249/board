package com.CoffeeCommunity.board.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Board {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String content; // private뒤에 적은 id,title,content는 데이터베이스의 table에 있는 요소들이다

}
