package pl.edu.agh.service;

import pl.edu.agh.dao.CategoryDAO;
import pl.edu.agh.model.Category;

import java.util.List;

public class CategoryService {
    private CategoryDAO categoryDAO = new CategoryDAO();

    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    public void saveCategory(Category category) {
        categoryDAO.saveCategory(category);
    }
}
