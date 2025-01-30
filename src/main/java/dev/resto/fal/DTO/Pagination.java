package dev.resto.fal.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

public class Pagination {
    @JsonProperty("pages")
    private int maxPage;

    public Pagination(int maxPage) {
        this.maxPage = maxPage;
    }
}
