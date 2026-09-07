package be.vdab.tcbackend.categories;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
class CategoryService {

    private final CategoryRepository categoryRepository;


    CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /* Method that returns the count of categories in table products */
    long findCount() {
        return categoryRepository.count();
    }

    /* Method that returns a list of all categories */
    List<Category> findAll() {
        return categoryRepository.findAll(Sort.by("name"));
    }

}
