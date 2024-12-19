package dev.resto.fal;

import dev.resto.fal.entity.Tag;
import dev.resto.fal.entity.TagType;
import dev.resto.fal.repository.TagRepository;
import dev.resto.fal.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {


@Autowired
private TagRepository tagRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public ApplicationRunner addTagsRunner() {
		return args -> {
			tagRepository.save(new Tag("Cozy", TagType.AMBIANCE, "😌"));
			tagRepository.save(new Tag("Romantic", TagType.AMBIANCE, "❤️"));
			tagRepository.save(new Tag("Luxurious", TagType.AMBIANCE, "💎"));
			tagRepository.save(new Tag("Casual", TagType.AMBIANCE, "👕"));
			tagRepository.save(new Tag("Family Friendly", TagType.AMBIANCE, "👨‍👩‍👧‍👦"));
			tagRepository.save(new Tag("Modern", TagType.AMBIANCE, "🏙️"));
			tagRepository.save(new Tag("Rustic", TagType.AMBIANCE, "🌲"));
			tagRepository.save(new Tag("Trendy", TagType.AMBIANCE, "🔥"));
			tagRepository.save(new Tag("Historic", TagType.AMBIANCE, "🏰"));
			tagRepository.save(new Tag("Quiet", TagType.AMBIANCE, "🤫"));
			tagRepository.save(new Tag("Lively", TagType.AMBIANCE, "💃"));
			tagRepository.save(new Tag("Relaxed", TagType.AMBIANCE, "🛋️"));
			tagRepository.save(new Tag("Elegant", TagType.AMBIANCE, "💍"));
			tagRepository.save(new Tag("Intimate", TagType.AMBIANCE, "🥂"));
			tagRepository.save(new Tag("Vibrant", TagType.AMBIANCE, "🌈"));
			tagRepository.save(new Tag("Upscale", TagType.AMBIANCE, "🍸"));
			tagRepository.save(new Tag("Artistic", TagType.AMBIANCE, "🎨"));

			tagRepository.save(new Tag("Italian", TagType.CUISINE, "🍝"));
			tagRepository.save(new Tag("Chinese", TagType.CUISINE, "🥡"));
			tagRepository.save(new Tag("Japanese", TagType.CUISINE, "🍣"));
			tagRepository.save(new Tag("Indian", TagType.CUISINE, "🍛"));
			tagRepository.save(new Tag("Mexican", TagType.CUISINE, "🌮"));
			tagRepository.save(new Tag("French", TagType.CUISINE, "🍷"));
			tagRepository.save(new Tag("Thai", TagType.CUISINE, "🍜"));
			tagRepository.save(new Tag("American", TagType.CUISINE, "🍔"));
			tagRepository.save(new Tag("Mediterranean", TagType.CUISINE, "🥙"));
			tagRepository.save(new Tag("Korean", TagType.CUISINE, "🍲"));
			tagRepository.save(new Tag("Vegan", TagType.CUISINE, "🥦"));
			tagRepository.save(new Tag("Vegetarian", TagType.CUISINE, "🥗"));
			tagRepository.save(new Tag("Seafood", TagType.CUISINE, "🦞"));
			tagRepository.save(new Tag("BBQ", TagType.CUISINE, "🍖"));
			tagRepository.save(new Tag("Fast Food", TagType.CUISINE, "🍟"));
			tagRepository.save(new Tag("Desserts", TagType.CUISINE, "🍰"));
			tagRepository.save(new Tag("Coffee", TagType.CUISINE, "☕"));
			tagRepository.save(new Tag("Bakery", TagType.CUISINE, "🍞"));
			tagRepository.save(new Tag("Street Food", TagType.CUISINE, "🍢"));
			tagRepository.save(new Tag("African", TagType.CUISINE, "🍠"));
			tagRepository.save(new Tag("Latin American", TagType.CUISINE, "🍹"));
			tagRepository.save(new Tag("Contemporary", TagType.CUISINE, "🍽️"));

			tagRepository.save(new Tag("Date Night", TagType.OCCASION, "🌹"));
			tagRepository.save(new Tag("Business Meeting", TagType.OCCASION, "💼"));
			tagRepository.save(new Tag("Family Gathering", TagType.OCCASION, "👨‍👩‍👧‍👦"));
			tagRepository.save(new Tag("Celebration", TagType.OCCASION, "🎉"));
			tagRepository.save(new Tag("Casual Dining", TagType.OCCASION, "🍕"));
			tagRepository.save(new Tag("Quick Bite", TagType.OCCASION, "🍔"));
			tagRepository.save(new Tag("Solo Dining", TagType.OCCASION, "🍽️"));
			tagRepository.save(new Tag("Brunch", TagType.OCCASION, "🥞"));
			tagRepository.save(new Tag("After Work", TagType.OCCASION, "🍻"));
			tagRepository.save(new Tag("Holiday Gathering", TagType.OCCASION, "🎄"));
			tagRepository.save(new Tag("Anniversary", TagType.OCCASION, "💑"));
			tagRepository.save(new Tag("Birthday", TagType.OCCASION, "🎂"));
			tagRepository.save(new Tag("Friends Outing", TagType.OCCASION, "🍻"));
			tagRepository.save(new Tag("Formal Event", TagType.OCCASION, "🎩"));
			tagRepository.save(new Tag("Wedding", TagType.OCCASION, "💍"));

			tagRepository.save(new Tag("Halal", TagType.SPECIAL_FEATURE, "🕌"));
			tagRepository.save(new Tag("Kosher", TagType.SPECIAL_FEATURE, "✡️"));
			tagRepository.save(new Tag("Gluten Free", TagType.SPECIAL_FEATURE, "🌾🚫"));
			tagRepository.save(new Tag("Organic", TagType.SPECIAL_FEATURE, "🌱"));
			tagRepository.save(new Tag("Locally Sourced", TagType.SPECIAL_FEATURE, "🌍"));
			tagRepository.save(new Tag("Farm to Table", TagType.SPECIAL_FEATURE, "🚜"));
			tagRepository.save(new Tag("All You Can Eat", TagType.SPECIAL_FEATURE, "🍽️"));
			tagRepository.save(new Tag("Late Night", TagType.SPECIAL_FEATURE, "🌙"));

			tagRepository.save(new Tag("Downtown", TagType.LOCATION, "🏙️"));
			tagRepository.save(new Tag("Suburban", TagType.LOCATION, "🏡"));
			tagRepository.save(new Tag("Rooftop", TagType.LOCATION, "🌆"));
			tagRepository.save(new Tag("Waterfront", TagType.LOCATION, "🌊"));

			tagRepository.save(new Tag("Budget", TagType.PRICING, "💸"));
			tagRepository.save(new Tag("Mid Range", TagType.PRICING, "💵"));
			tagRepository.save(new Tag("Expensive", TagType.PRICING, "💰"));
		};
	}
}
