package vn.novahub.helpdesk.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.collections.IteratorUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import vn.novahub.helpdesk.model.Category;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CategoryRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private List<Category> categories;

    @Before
    public void before() throws IOException {
        initData();
    }

    @Test
    public void testGetAllByNameContaining() {
        categories = IteratorUtils.toList(categoryRepository.findAll().iterator());

        for(int i = categories.size() - 1; i >= 0; i--) {
            if (!categories.get(i).getName().contains("Development")) {
                categories.remove(i);
            }
        }

        Page<Category> categoriesExcepted = new PageImpl<>(categories);
        Page<Category> categoriesActual = categoryRepository.getAllByNameContaining("Development", new PageRequest(0, 20));

        assertEquals(categoriesExcepted.getTotalElements(), categoriesActual.getTotalElements());
    }

    @Test
    public void testExistsByName() {
        assertFalse(categoryRepository.existsByName("ABC"));
        assertTrue(categoryRepository.existsByName("Web Design"));
    }

    @Test
    public void testExistsById() {
        assertFalse(categoryRepository.existsById(20L));
        assertTrue(categoryRepository.existsById(5L));
    }

    private void initData() throws IOException {
         categoryRepository.saveAll(convertJsonFileToObjectList(
                "seeding/categories.json",
                new TypeReference<List<Category>>(){}));
    }
}
