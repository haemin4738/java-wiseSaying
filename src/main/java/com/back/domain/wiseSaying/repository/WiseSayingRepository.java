package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WiseSayingRepository {
    private static final String DB_DIR = "db/wiseSaying";
    private static final String LAST_ID_PATH = DB_DIR + "/lastId.txt";

    private final List<WiseSaying> wiseSayings = new ArrayList<>();
    private int lastId = 0;

    public WiseSayingRepository() {
        load();
    }

    public WiseSaying save(WiseSaying wiseSaying) {
        wiseSaying.setId(++lastId);
        wiseSayings.add(wiseSaying);
        saveToFile(wiseSaying);
        saveLastId();
        return wiseSaying;
    }

    public WiseSaying findById(int id) {
        return wiseSayings.stream()
                .filter(ws -> ws.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public boolean deleteById(int id) {
        WiseSaying target = findById(id);
        if (target == null) return false;

        wiseSayings.remove(target);
        try {
            Files.deleteIfExists(Paths.get(DB_DIR, id + ".json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public void update(int id, String content, String author) {
        WiseSaying ws = findById(id);
        if (ws != null) {
            ws.setContent(content);
            ws.setAuthor(author);
            saveToFile(ws);
        }
    }

    public List<WiseSaying> findAll() {
        return new ArrayList<>(wiseSayings);
    }

    public void saveToDataJson() {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("data.json"))) {
            bw.write("[\n");
            for (int i = 0; i < wiseSayings.size(); i++) {
                WiseSaying ws = wiseSayings.get(i);
                bw.write(String.format("  {\"id\": %d, \"content\": \"%s\", \"author\": \"%s\"}",
                        ws.getId(), ws.getContent(), ws.getAuthor()));
                if (i < wiseSayings.size() - 1) bw.write(",");
                bw.write("\n");
            }
            bw.write("]");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void load() {
        try {
            Files.createDirectories(Paths.get(DB_DIR));
            Path lastIdFile = Paths.get(LAST_ID_PATH);
            if (Files.exists(lastIdFile)) {
                String text = Files.readString(lastIdFile).trim();
                if (!text.isEmpty()) lastId = Integer.parseInt(text);
            }

            DirectoryStream<Path> files = Files.newDirectoryStream(Paths.get(DB_DIR), "*.json");
            for (Path file : files) {
                if (file.endsWith("lastId.txt")) continue;
                WiseSaying ws = readFromFile(file.toFile());
                if (ws != null) wiseSayings.add(ws);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private WiseSaying readFromFile(File file) {
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            int id = 0;
            String content = "", author = "";

            for (String line : lines) {
                line = line.trim();
                if (line.startsWith("\"id\"")) {
                    id = Integer.parseInt(line.split(":")[1].replace(",", "").trim());
                } else if (line.startsWith("\"content\"")) {
                    content = line.split(":")[1].replace("\"", "").replace(",", "").trim();
                } else if (line.startsWith("\"author\"")) {
                    author = line.split(":")[1].replace("\"", "").trim();
                }
            }
            WiseSaying ws = new WiseSaying(content, author);
            ws.setId(id);
            return ws;

        } catch (IOException e) {
            return null;
        }
    }

    private void saveToFile(WiseSaying wiseSaying) {
        File file = Paths.get(DB_DIR, wiseSaying.getId() + ".json").toFile();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("{\n");
            bw.write(String.format("  \"id\": %d,\n", wiseSaying.getId()));
            bw.write(String.format("  \"content\": \"%s\",\n", wiseSaying.getContent()));
            bw.write(String.format("  \"author\": \"%s\"\n", wiseSaying.getAuthor()));
            bw.write("}");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveLastId() {
        try {
            Files.writeString(Paths.get(LAST_ID_PATH), String.valueOf(lastId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
