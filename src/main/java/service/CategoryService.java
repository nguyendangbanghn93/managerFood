package service;

import entity.Category;
import repository.CategoryRepository;
import repository.ICategoryRepository;

import java.util.ArrayList;
import java.util.Date;

public class CategoryService {
    ICategoryRepository iCategoryRepository = new CategoryRepository();
    public boolean create(Category category){
        if(category.validate()){
            category.setCreatedAt(new Date());
            category.setUpdatedAt(new Date());
            return iCategoryRepository.save(category);
        }
        return false;
    };
    public Category edit(Category category){
        if(category.validate()&&iCategoryRepository.findById(category.getId())!=null){
            category.setUpdatedAt(new Date());
            return iCategoryRepository.update(category);
        }
        return null;
    }
    public boolean delete(Object id){
        Category category =iCategoryRepository.findById(id);
        if(category!=null&&category.getStatus()!=0){
            category.setStatus(0);
            iCategoryRepository.update(category);
            return true;
        }
        return false;
    }
    public ArrayList<Category> findAll(){
        return iCategoryRepository.findAll();
    }
}
