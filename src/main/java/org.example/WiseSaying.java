package org.example;

public class WiseSaying {
    private int id;
    private String author;
    private String content;

    WiseSaying(int id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    int getId(){
        return id;
    }

    String getAuthor() {
        return author;
    }

    String getContent() {
        return content;
    }

    void setId(int id){
        this.id = id;
    }

    void setAuthor(String author){
        this.author = author;
    }

    void setContent(String content){
        this.content = content;
    }

    public String toJson() {
        return "{\"id\":" + id + ",\"author\":\"" + author + "\",\"content\":\"" + content + "\"}";
    }

    public static WiseSaying fromJson(String json) {
        String[] parts = json.replace("{", "").replace("}", "").replace("\"", "").split(",");
        int wsId = Integer.parseInt(parts[0].split(":")[1]);
        String author = parts[1].split(":")[1];
        String content = parts[2].split(":")[1];
        return new WiseSaying(wsId, author, content);
    }
}
