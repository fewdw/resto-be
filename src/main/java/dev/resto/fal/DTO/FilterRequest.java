package dev.resto.fal.DTO;

import dev.resto.fal.entity.Tag;
import dev.resto.fal.enums.SortBy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterRequest {

    private String searchBar;
    private boolean strictTags;
    private List<Tag> tags;
    private SortBy sortBy;

}
