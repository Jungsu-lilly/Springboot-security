package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // View 리턴
public class IndexController {

    // localhost:8080
    @GetMapping({"","/"})
    public String index(){
        // 머스테치 기본 폴더 src/main/resources/
        // 뷰 리졸버 설정 :templates (prefix),.mustache (suffix) => 생략 가능!!
        return "index";
    }
}
