package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
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
            }
        }
        scanner.close();
    }
}

