package com.back.domain.wiseSaying.controller;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.service.WiseSayingService;

import java.util.List;
import java.util.Scanner;

public class WiseSayingController {
    private final Scanner sc;
    private final WiseSayingService service;

    // 생성자: Scanner, Service 주입
    public WiseSayingController(Scanner sc, WiseSayingService service) {
        this.sc = sc;
        this.service = service;
    }

    // 명언 등록 처리
    public void write() {
        System.out.print("명언 : ");
        String content = sc.nextLine().trim();

        System.out.print("작가 : ");
        String author = sc.nextLine().trim();

        WiseSaying ws = service.add(content, author);

        System.out.printf("%d번 명언이 등록되었습니다.\n", ws.getId());
    }

    // 명언 삭제 처리
    public void remove(String command) {
        int id = parseId(command);
        if (service.removeById(id)) {
            System.out.printf("%d번 명언이 삭제되었습니다.\n", id);
        } else {
            System.out.printf("%d번 명언은 존재하지 않습니다.\n", id);
        }
    }

    // 명언 수정 처리
    public void modify(String command) {
        int id = parseId(command);
        WiseSaying ws = service.getById(id);
        if (ws == null) {
            System.out.printf("%d번 명언은 존재하지 않습니다.\n", id);
            return;
        }

        System.out.printf("명언(기존) : %s\n", ws.getContent());
        System.out.print("명언 : ");
        String content = sc.nextLine().trim();

        System.out.printf("작가(기존) : %s\n", ws.getAuthor());
        System.out.print("작가 : ");
        String author = sc.nextLine().trim();

        service.modify(id, content, author);
    }

    // 명언 목록 출력
    public void list(String command) {
        List<WiseSaying> list = service.getAll();
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        for (WiseSaying ws : list) {
            System.out.printf("%d / %s / %s\n", ws.getId(), ws.getAuthor(), ws.getContent());
        }
    }

    // data.json 빌드
    public void build() {
        service.buildDataJson();
        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }

    // id 파싱 (삭제?id=1, 수정?id=2 등)
    private int parseId(String command) {
        String[] parts = command.split("id=");
        if (parts.length < 2) {
            return -1; // 잘못된 id
        }
        try {
            return Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
