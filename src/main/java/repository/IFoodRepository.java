package repository;

import entity.Food;

import java.util.ArrayList;

public interface IFoodRepository {
    boolean save(Food food);
    ArrayList<Food> findAll();
    Food findById(Object id);
    ArrayList<Food> findByCondition(String strcm);
    Food update(Food food);
    boolean delete(Object id);
    int countCondition(String str);
}
