package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    Scanner scanner = new Scanner(System.in);
    int lastId = 0;
    List<WiseSaying> wiseSayingList = new ArrayList<>();

    void run () {
        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            String cmd = scanner.nextLine().trim();

            if (cmd.equals("종료")) {
                System.out.println("프로그램을 종료합니다.");
                break;
            } else  if (cmd.equals("등록")) {
                actionWrite();
            } else if (cmd.equals("목록")) {
                actionList();
            } else if (cmd.startsWith("삭제")) {
                actionDelete(cmd);
            } else if (cmd.startsWith("수정")) {
                actionModify(cmd);
            }
        }
        scanner.close();
    }

    void actionWrite() {
        System.out.print("명언: ");
        String content = scanner.nextLine().trim();

        System.out.print("작가: ");
        String author = scanner.nextLine().trim();

        WiseSaying wiseSaying = write(author, content);

        System.out.println("%d번 명언이 등록되었습니다.".formatted(wiseSaying.getId()));
    }

    WiseSaying write (String author, String content) {
        WiseSaying wiseSaying = new WiseSaying(++lastId, author, content );

        wiseSayingList.add(wiseSaying);

        return wiseSaying;
    }

    void actionList() {
        System.out.println("번호 / 작자 / 명언");
        System.out.println("----------------------");

        for (int i = wiseSayingList.size() - 1; i >=0; i--) {
            WiseSaying wiseSaying = wiseSayingList.get(i);
            System.out.println("%d / %s / %s".formatted(wiseSaying.getId(), wiseSaying.getAuthor(), wiseSaying.getContent()));
        }
    }

    void actionDelete(String cmd) {
        int id = CmdSplitId(cmd);
        if (id < 0) {
            return; // ID가 유효하지 않으면 메서드 종료
        }
        WiseSaying wisesaying = findId(id);

        if (wisesaying == null) {
            return; // ID가 유효하지 않으면 메서드 종료
        }

        wiseSayingList.remove(wisesaying);
    }
    void actionModify(String cmd) {
        int id = CmdSplitId(cmd);
        if (id < 0) {
            return; // ID가 유효하지 않으면 메서드 종료
        }
        WiseSaying wisesaying = findId(id);

        if (wisesaying == null) {
            return; // ID가 유효하지 않으면 메서드 종료
        }

        System.out.printf("명언 (기존) : %s\n", wisesaying.getContent());
        System.out.print("명언 : ");
        String content = scanner.nextLine().trim();

        System.out.printf("작자 (기존) : %s\n", wisesaying.getAuthor());
        System.out.print("작자 : ");
        String author = scanner.nextLine().trim();

        wisesaying.setAuthor(author);
        wisesaying.setContent(content);

        System.out.println("%d번 명언이 수정되었습니다.".formatted(wisesaying.getId()));
    }

    int CmdSplitId(String cmd) {
        String[] deleteId = cmd.split("=");

        if(deleteId.length < 2 || deleteId[1].isEmpty()) {
            System.out.println("ID를 입력해주세요");
            return -1;
        }

        return Integer.parseInt(deleteId[1]);
    }

    WiseSaying findId(int id) {
        WiseSaying wiseSaying = null;
        for (int i = 0; i < wiseSayingList.size(); i++) {
            if(wiseSayingList.get(i).getId() == id){
                wiseSaying = wiseSayingList.get(i);
            }

            if(wiseSaying == null) {
                System.out.println(id + "번 명언이 존재하지 않습니다.");
                return null;
            }
        }
        return wiseSaying;
    }
}