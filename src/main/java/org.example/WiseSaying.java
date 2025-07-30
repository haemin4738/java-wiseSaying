package org.example;

class WiseSaying {
    private int id;
    private String text;
    private String author;

    public WiseSaying(int id, String text, String author) {
        this.id = id;
        this.text = text;
        this.author = author;
    }

    public int getId() { return id; }
    public String getText() { return text; }
    public String getAuthor() { return author; }
    public void setText(String text) { this.text = text; }
    public void setAuthor(String author) { this.author = author; }
}

