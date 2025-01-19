package dev.resto.fal.specification;

import dev.resto.fal.DTO.FilterRequest;
import dev.resto.fal.entity.RestaurantRating;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Sort;
import dev.resto.fal.entity.Restaurant;
import dev.resto.fal.entity.Tag;

import java.util.List;
import java.util.stream.Collectors;

public class RestaurantSpecification {

    public static Specification<Restaurant> containsSearchBar(String searchBar) {
        return (root, query, criteriaBuilder) -> {
            if (searchBar == null || searchBar.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Predicate namePredicate = criteriaBuilder.like(root.get("name"), "%" + searchBar + "%");
            Predicate addressPredicate = criteriaBuilder.like(root.get("address"), "%" + searchBar + "%");
            return criteriaBuilder.or(namePredicate, addressPredicate);
        };
    }

    public static Specification<Restaurant> hasTags(List<Tag> tags, boolean strictTags) {
        return (root, query, criteriaBuilder) -> {
            if (tags == null || tags.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            if (strictTags) {
                return criteriaBuilder.and(
                        tags.stream()
                                .map(tag -> {
                                    Subquery<Long> subquery = query.subquery(Long.class);
                                    Root<RestaurantRating> subRoot = subquery.from(RestaurantRating.class);
                                    Join<RestaurantRating, Tag> tagJoin = subRoot.join("tag");
                                    subquery.select(criteriaBuilder.count(subRoot))
                                            .where(criteriaBuilder.and(
                                                    criteriaBuilder.equal(subRoot.get("restaurant"), root),
                                                    criteriaBuilder.equal(tagJoin.get("name"), tag.getName())
                                            ));
                                    return criteriaBuilder.greaterThan(subquery, 0L);
                                })
                                .toArray(Predicate[]::new)
                );
            } else {
                Join<Restaurant, RestaurantRating> ratingsJoin = root.join("ratings", JoinType.INNER);
                Join<RestaurantRating, Tag> tagJoin = ratingsJoin.join("tag", JoinType.INNER);

                Predicate tagPredicate = tagJoin.get("name").in(tags.stream()
                        .map(Tag::getName)
                        .collect(Collectors.toList()));
                return criteriaBuilder.or(tagPredicate);
            }
        };
    }



    public static Sort sortBy(FilterRequest filterRequest) {
        // Using the compareTo method in the Restaurant entity for sorting
        // Sort by the number of reviews or any other field
        return Sort.by(Sort.Order.asc("name")); // Use custom field if needed
    }
}
