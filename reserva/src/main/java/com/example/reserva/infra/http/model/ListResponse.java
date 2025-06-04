package com.ea.backend.infra.http.model;

import org.springframework.http.ResponseEntity;

import java.util.List;

public class ListResponse<ListContent> {


  public static ResponseEntity<List<?>> build(List<?> objects) {

    return ResponseEntity.ok(objects);
  }
}
