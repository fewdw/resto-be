package dev.resto.fal.specification;

import dev.resto.fal.entity.Restaurant;
import dev.resto.fal.entity.Tag;
import dev.resto.fal.request.FilterRequest;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Set;

public class RestaurantSpecification {

    public static Specification<Restaurant> containsSearchBar(String searchBar) {
        return (root, query, cb) -> {
            if (searchBar == null || searchBar.isBlank()) {
                return cb.conjunction();
            }
            String likeSearch = "%" + searchBar.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("name")), likeSearch),
                    cb.like(cb.lower(root.get("address")), likeSearch)
            );
        };
    }

    public static Specification<Restaurant> hasTags(List<Tag> tags, boolean strictTags) {
        return (root, query, cb) -> {
            if (tags == null || tags.isEmpty()) {
                return cb.conjunction();
            }

            // Join with the RestaurantRating -> Tag
            Join<Object, Object> ratingsJoin = root.join("ratings");
            Join<Object, Object> tagsJoin = ratingsJoin.join("tag");

            if (strictTags) {
                // Only restaurants with ALL selected tags
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<Restaurant> subRoot = subquery.from(Restaurant.class);
                Join<Object, Object> subRatingsJoin = subRoot.join("ratings");
                Join<Object, Object> subTagsJoin = subRatingsJoin.join("tag");

                subquery.select(subRoot.get("id"))
                        .where(cb.and(
                                cb.equal(subRoot.get("id"), root.get("id")),
                                subTagsJoin.get("id").in(tags.stream().map(Tag::getId).toList())
                        ))
                        .groupBy(subRoot.get("id"))
                        .having(cb.equal(cb.count(subTagsJoin.get("id")), tags.size()));

                return cb.exists(subquery);
            } else {
                // Restaurants with ANY of the selected tags
                return tagsJoin.get("id").in(tags.stream().map(Tag::getId).toList());
            }
        };
    }

    public static Specification<Restaurant> sortBy(FilterRequest filterRequest) {
        return (root, query, cb) -> {
            if (filterRequest.getSortBy() == null) {
                query.orderBy(cb.desc(root.get("dateAdded"))); // Default sort
                return cb.conjunction();
            }

            switch (filterRequest.getSortBy()) {
                case MOST_RECENT -> query.orderBy(cb.desc(root.get("dateAdded")));
                case LEAST_RECENT -> query.orderBy(cb.asc(root.get("dateAdded")));
                case POPULAR -> query.orderBy(cb.desc(root.get("numberOfReviews")));
                case LEAST_POPULAR -> query.orderBy(cb.asc(root.get("numberOfReviews")));
                case BEST_MATCH -> {
                    Join<Object, Object> ratingsJoin = root.join("ratings", JoinType.LEFT);
                    query.groupBy(root.get("id"));
                    query.orderBy(cb.desc(cb.count(ratingsJoin.get("id"))));
                }
            }
            return cb.conjunction();
        };
    }
}
