package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class App {
    static final String DB_DIR = "db/wiseSaying";
    private List<WiseSaying> wiseSayings;
    private int id;
    private Scanner scanner;

    public void run() {
        wiseSayings = loadAllWiseSayings();
        id = loadLastId();
        scanner = new Scanner(System.in);

        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령 ) ");
            String cmd = scanner.nextLine().trim();

            if (cmd.equals("종료")) {
                System.out.println("프로그램을 종료합니다.");
                break;
            }

            if (cmd.equals("등록")) {
                handleRegister();
            }

            if (cmd.equals("목록")) {
                handleList();
            }

            if (cmd.startsWith("삭제?id=")) {
                handleDelete(cmd);
            }

            if (cmd.startsWith("수정?id=")) {
                handleUpdate(cmd);
            }

            if (cmd.equals("빌드")) {
                handleBuild();
            }
        }
    }

    private void handleRegister() { // 등록
        System.out.print("명언 : ");
        String text = scanner.nextLine();
        System.out.print("작가 : ");
        String author = scanner.nextLine();

        WiseSaying ws = new WiseSaying(id, text, author);
        wiseSayings.add(ws);
        saveWiseSayingToFile(ws);
        saveLastId(id + 1);

        System.out.println(id + "번 명언이 등록되었습니다.");
        id++;
    }

    private void handleList() { // 목록
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------------");
        wiseSayings.stream()
                .sorted((a, b) -> b.getId() - a.getId())
                .forEach(ws -> System.out.println(ws.getId() + " / " + ws.getAuthor() + " / " + ws.getText()));
    }

    private void handleDelete(String cmd) { // 삭제
        int deleteId = Integer.parseInt(cmd.substring(cmd.indexOf("=") + 1));

        boolean removed = wiseSayings.removeIf(e -> e.getId() == deleteId);
        if (removed) {
            deleteFile(deleteId);
            System.out.println(deleteId + "번 명언이 삭제되었습니다.");
        } else {
            System.out.println(deleteId + "번 명언이 존재하지 않습니다.");
        }
    }

    private void handleUpdate(String cmd) { // 수정
        int updateId = Integer.parseInt(cmd.substring(cmd.indexOf("=") + 1));

        Optional<WiseSaying> opt = wiseSayings.stream()
                .filter(e -> e.getId() == updateId)
                .findFirst();

        if (opt.isPresent()) {
            WiseSaying ws = opt.get();

            System.out.println("명언(기존) : " + ws.getText());
            System.out.print("명언 : ");
            String newText = scanner.nextLine();

            System.out.println("작가(기존) : " + ws.getAuthor());
            System.out.print("작가 : ");
            String newAuthor = scanner.nextLine();

            ws.setText(newText);
            ws.setAuthor(newAuthor);

            saveWiseSayingToFile(ws);
            System.out.println(updateId + "번 명언이 수정되었습니다.");
        } else {
            System.out.println(updateId + "번 명언이 존재하지 않습니다.");
        }
    }

    private void handleBuild() {
        buildDataFile(wiseSayings);
    }

    // ======= 아래는 기존 static 메서드들 그대로 유지 =======

    static void saveWiseSayingToFile(WiseSaying ws) {
        try {
            Files.createDirectories(Paths.get(DB_DIR));
            String json = "{\n" +
                    "  \"id\": " + ws.getId() + ",\n" +
                    "  \"text\": \"" + escape(ws.getText()) + "\",\n" +
                    "  \"author\": \"" + escape(ws.getAuthor()) + "\"\n" +
                    "}";
            Files.writeString(Paths.get(DB_DIR, ws.getId() + ".json"), json);
        } catch (IOException e) {
            System.out.println("파일 저장 오류: " + e.getMessage());
        }
    }

    static void deleteFile(int id) {
        try {
            Path filePath = Paths.get(DB_DIR, id + ".json");
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("파일 삭제 오류: " + e.getMessage());
        }
    }

    static void saveLastId(int lastId) {
        try {
            Files.createDirectories(Paths.get(DB_DIR));
            Files.writeString(Paths.get(DB_DIR, "lastId.txt"), String.valueOf(lastId));
        } catch (IOException e) {
            System.out.println("lastId 저장 오류: " + e.getMessage());
        }
    }

    static int loadLastId() {
        try {
            Path path = Paths.get(DB_DIR, "lastId.txt");
            if (Files.exists(path)) {
                return Integer.parseInt(Files.readString(path).trim());
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("lastId 불러오기 실패. 기본값 1 사용");
        }
        return 1;
    }

    static List<WiseSaying> loadAllWiseSayings() {
        List<WiseSaying> result = new ArrayList<>();
        try {
            Files.createDirectories(Paths.get(DB_DIR));
            DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(DB_DIR), "*.json");

            for (Path path : stream) {
                WiseSaying ws = loadWiseSayingFromFile(path);
                if (ws != null) result.add(ws);
            }
        } catch (IOException e) {
            System.out.println("명언 불러오기 실패: " + e.getMessage());
        }
        return result;
    }

    static WiseSaying loadWiseSayingFromFile(Path path) {
        try {
            List<String> lines = Files.readAllLines(path);
            int id = 0;
            String text = "", author = "";
            for (String line : lines) {
                line = line.trim();
                if (line.startsWith("\"id\"")) {
                    id = Integer.parseInt(line.split(":")[1].trim().replace(",", ""));
                } else if (line.startsWith("\"text\"")) {
                    text = line.split(":")[1].trim().replaceAll("^\"|\",?$", "");
                } else if (line.startsWith("\"author\"")) {
                    author = line.split(":")[1].trim().replaceAll("^\"|\",?$", "");
                }
            }
            return new WiseSaying(id, text, author);
        } catch (Exception e) {
            System.out.println("파일 파싱 오류 (" + path.getFileName() + ")");
            return null;
        }
    }

    static void buildDataFile(List<WiseSaying> wiseSayings) {
        try {
            Files.createDirectories(Paths.get(DB_DIR));
            Path filePath = Paths.get(DB_DIR, "data.json");

            StringBuilder sb = new StringBuilder();
            sb.append("[\n");

            for (int i = 0; i < wiseSayings.size(); i++) {
                WiseSaying ws = wiseSayings.get(i);
                sb.append("  {\n");
                sb.append("    \"id\": ").append(ws.getId()).append(",\n");
                sb.append("    \"text\": \"").append(escape(ws.getText())).append("\",\n");
                sb.append("    \"author\": \"").append(escape(ws.getAuthor())).append("\"\n");
                sb.append("  }");

                if (i < wiseSayings.size() - 1) {
                    sb.append(",");
                }
                sb.append("\n");
            }

            sb.append("]");
            Files.writeString(filePath, sb.toString());
            System.out.println("data.json 파일이 생성되었습니다.");
        } catch (IOException e) {
            System.out.println("data.json 생성 실패: " + e.getMessage());
        }
    }

    static String escape(String s) {
        return s.replace("\"", "\\\"");
    }
}

