package com.back.domain.wiseSaying.service;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;

import java.util.List;

public class WiseSayingService {
    private final WiseSayingRepository repository;

    // 생성자: Repository 주입
    public WiseSayingService(WiseSayingRepository repository) {
        this.repository = repository;
    }

    // 명언 등록
    public WiseSaying add(String content, String author) {
        WiseSaying ws = new WiseSaying(content, author);
        return repository.save(ws);
    }

    // 명언 조회
    public WiseSaying getById(int id) {
        return repository.findById(id);
    }

    // 명언 삭제
    public boolean removeById(int id) {
        return repository.deleteById(id);
    }

    // 명언 수정
    public void modify(int id, String content, String author) {
        repository.update(id, content, author);
    }

    // 전체 명언 목록
    public List<WiseSaying> getAll() {
        return repository.findAll();
    }

    // data.json 빌드
    public void buildDataJson() {
        repository.saveToDataJson();
    }
}
