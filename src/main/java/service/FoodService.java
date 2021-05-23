package service;

import entity.Food;
import repository.FoodRepository;
import repository.IFoodRepository;

import java.util.ArrayList;
import java.util.Date;

public class FoodService {
    IFoodRepository iFoodRepository = new FoodRepository();

    public boolean create(Food food) {
        if (food.validate()) {
            food.setStatus(1);
            food.setCreatedAt(new Date());
            food.setUpdatedAt(new Date());
            food.setSaleStartDate(new Date());
            return iFoodRepository.save(food);
        }
        return false;
    }

    ;

    public Food edit(Food food) {
        Food f = iFoodRepository.findById(food.getId());
        if (food.validate() && f != null) {
            food.setUpdatedAt(new Date());
            food.setCreatedAt(f.getCreatedAt());
            food.setSaleStartDate(f.getSaleStartDate());
            food.setUpdatedAt(new Date());
            return iFoodRepository.update(food);
        }
        return null;
    }

    public boolean delete(Object id) {
        Food food = iFoodRepository.findById(id);
        if (food != null && food.getStatus() != 0) {
            food.setStatus(0);
            iFoodRepository.update(food);
            return true;
        }
        return false;
    }

    public ArrayList<Food> findAll(int page, int num) {
        return iFoodRepository.findAll();
    }

    public int countFood() {
        return iFoodRepository.countCondition("SELECT COUNT(*) FROM food WHERE status !=0");
    }

    public Food findById(Object id) {
        return iFoodRepository.findById(id);
    }

    public ArrayList<Food> findByPage(int p, int n) {
        int s = (p - 1) * n;
        int e = e = p * n;
        return iFoodRepository.findByCondition("SELECT * FROM ( SELECT *, ROW_NUMBER() OVER (ORDER BY updatedAt DESC) AS RowNum FROM food WHERE status !=0) AS foods WHERE foods.RowNum > " + s + " AND " + "foods.RowNum <= " + e);
    }
}
