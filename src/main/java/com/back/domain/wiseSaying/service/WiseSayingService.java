package com.back.domain.wiseSaying.service;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;

import java.util.List;

public class WiseSayingService {
    private final WiseSayingRepository wiseSayingRepository;

    public WiseSayingService() {
        this.wiseSayingRepository = new WiseSayingRepository();
    }

    public List<WiseSaying> getWiseSayingList() {
        return this.wiseSayingRepository.getWiseSayingList();
    }

    public void delete(WiseSaying wiseSaying) {
        this.wiseSayingRepository.delete(wiseSaying);
    }

    public void modify(WiseSaying wiseSaying, String content, String author) {
        this.wiseSayingRepository.modify(wiseSaying, content, author);
    }

    public WiseSaying findById(int id) {
        return this.wiseSayingRepository.findById(id);
    }

    public WiseSaying write(String author, String content) {

        WiseSaying wiseSaying  = this.wiseSayingRepository.write(author, content);

        return wiseSaying;
    }
}