package org.example.domain.wiseSaying;

import org.example.WiseSaying;

import java.util.List;

public class WiseSayingService {
    private final WiseSayingRepository wiseSayingRepository;

    WiseSayingService() {
        this.wiseSayingRepository = new WiseSayingRepository();
    }

    public List<WiseSaying> getWiseSayingList() {
        return this.wiseSayingRepository.getWiseSayingList();
    }

    public void delete(WiseSaying wiseSaying) {
        this.wiseSayingRepository.delete(wiseSaying);
    }

    void modify(WiseSaying wiseSaying, String content, String author) {
        this.wiseSayingRepository.modify(wiseSaying, content, author);
    }

    WiseSaying findById(int id) {
        return this.wiseSayingRepository.findById(id);
    }

    public WiseSaying write(String author, String content) {

        WiseSaying wiseSaying  = this.wiseSayingRepository.write(author, content);

        return wiseSaying;
    }
}