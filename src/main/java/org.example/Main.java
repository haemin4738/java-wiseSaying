package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int count = 1; // 명언의 개수

        System.out.println("== 명언 앱 ==");
        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.print("명령 ) ");
            String cmd = scanner.nextLine().trim(); // trim -> 읽은 값 공백 제거

            if (cmd.equals("종료")){
                System.out.println("프로그램을 종료합니다.");
                break;
            }

            if(cmd.equals("등록")){
                System.out.print(("명언 : "));
                String text = scanner.nextLine();
                System.out.print(("작가 : "));
                String author = scanner.nextLine();
                System.out.println(count + "번 명언이 등록되었습니다.");
                count++;
            }
        }
        scanner.close();
    }
}

