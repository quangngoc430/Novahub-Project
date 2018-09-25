package vn.novahub.helpdesk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.Category;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

    @Query("FROM Category category " +
           "WHERE lower(category.name) LIKE CONCAT('%', lower(trim(:name)) , '%') ")
    Page<Category> getAllByNameContaining(@Param("name") String name,
                                          Pageable pageable);

    boolean existsByName(String name);

    boolean existsById(long categoryId);
}
