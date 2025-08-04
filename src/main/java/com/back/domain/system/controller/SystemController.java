package com.back.domain.system.controller;

public class SystemController {
    public void actionExit(){
        System.out.println("프로그램을 종료합니다.");
    }
    public void unknown(String command) {
        System.out.println("알 수 없는 명령어입니다: " + command);
    }
}