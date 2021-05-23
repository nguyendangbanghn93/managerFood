package repository;

import entity.Category;

import java.util.ArrayList;

public interface ICategoryRepository {
    boolean save(Category category);
    ArrayList<Category> findAll();
    Category findById(Object id);
    ArrayList<Category> findByCondition(String strCmd);
    Category update(Category category);
    boolean delete(Object id);
}
