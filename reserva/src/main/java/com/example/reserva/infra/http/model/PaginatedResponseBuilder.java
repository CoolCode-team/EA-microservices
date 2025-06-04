package com.ea.backend.infra.http.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;


@Schema(description = "Paginated response")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaginatedResponseBuilder<Content> {

    @Schema(description = "page", defaultValue = "0")
    public int page;

    @Schema(description = "content")
    public List<Content> content;

    @Schema(description = "page size", defaultValue = "0")
    public int pageSize;

    @Schema(description = "total of pages", defaultValue = "0")
    public int totalOfPages;


    public static ResponseEntity<?> build(Page<?> pageEntity) {
        var map =
                Map.of(
                        "page",
                        pageEntity.getNumber() + 1,
                        "pageSize",
                        pageEntity.getSize(),
                        "totalOfPages",
                        pageEntity.getTotalPages(),
                        "content",
                        pageEntity.get());

        return ResponseEntity.ok().body(map);
    }

    public PaginatedResponseBuilder(Page<Content> pageEntity) {
        this.page = pageEntity.getNumber() + 1;
        this.content = pageEntity.getContent();
        this.pageSize = pageEntity.getSize();
        this.totalOfPages = pageEntity.getTotalPages();
    }

}
