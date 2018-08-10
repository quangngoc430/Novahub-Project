package vn.novahub.helpdesk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.Category;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

    Page<Category> getAllByNameContaining(String name, Pageable pageable);

    boolean existsByName(String name);

    boolean existsById(long categoryId);

    Category getByName(String name);
}
