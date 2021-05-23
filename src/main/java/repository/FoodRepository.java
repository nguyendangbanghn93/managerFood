package repository;

import entity.Food;

import java.util.ArrayList;

public class FoodRepository implements IFoodRepository {
    GenericRepository<Food> repository = new GenericRepository<Food>(Food.class);
    @Override
    public boolean save(Food food) {
        return repository.save(food);
    }
    @Override
    public int countCondition(String str) {
        return repository.countCondition(str);
    }
    @Override
    public ArrayList<Food> findAll() {
             return repository.findByCondition("SELECT * FROM food WHERE status !=0 ORDER BY updatedAt DESC;");
    }

    @Override
    public Food findById(Object id) {
        return repository.findById(id);
    }

    @Override
    public ArrayList<Food> findByCondition(String strCmd) {
        return repository.findByCondition(strCmd);
    }

    @Override
    public Food update(Food food) {
        return repository.update(food);
    }

    @Override
    public boolean delete(Object id) {
        return repository.delete(id);
    }
}
