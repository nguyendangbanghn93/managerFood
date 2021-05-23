package repository;

import entity.Category;

import java.util.ArrayList;

public class CategoryRepository implements ICategoryRepository{
    GenericRepository<Category> repository = new GenericRepository<Category>(Category.class);
    @Override
    public boolean save(Category category) {
        return repository.save(category);
    }

    @Override
    public ArrayList<Category> findAll() {
        return repository.findByCondition("SELECT * FROM category WHERE status != 0");
    }

    @Override
    public Category findById(Object id) {
        return repository.findById(id);
    }

    @Override
    public ArrayList<Category> findByCondition(String strCmd) {
        return repository.findByCondition(strCmd);
    }

    @Override
    public Category update(Category category) {
        return repository.update(category);
    }

    @Override
    public boolean delete(Object id) {
        return repository.delete(id);
    }
}
