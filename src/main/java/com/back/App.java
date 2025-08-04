package com.back;

import com.back.domain.AppContext;
import com.back.domain.system.controller.SystemController;
import com.back.domain.wiseSaying.controller.WiseSayingController;

import java.util.Scanner;

public class App {
    public void run() {
        Scanner sc = AppContext.scanner;
        WiseSayingController wiseSayingController = AppContext.wiseSayingController;
        SystemController systemController = AppContext.systemController;

        System.out.println("== 명언 앱 ==");

        boolean running = true;
        while (running) {
            System.out.print("명령) ");
            String command = sc.nextLine().trim();

            switch (command) {
                case "종료" -> running = false;
                case "등록" -> wiseSayingController.write();
                default -> {
                    if (command.startsWith("삭제")) {
                        wiseSayingController.remove(command);
                    } else if (command.startsWith("수정")) {
                        wiseSayingController.modify(command);
                    } else if (command.startsWith("목록")) {
                        wiseSayingController.list(command);
                    } else if (command.equals("빌드")) {
                        wiseSayingController.build();
                    } else {
                        systemController.unknown(command);
                    }
                }
            }
        }
    }
}