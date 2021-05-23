package test;

import entity.Category;
import entity.Food;
import helper.ConnectionHelper;
import helper.SQLConstant;
import modelAnnotion.Column;
import modelAnnotion.Entity;
import modelAnnotion.Id;
import service.CategoryService;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Food food = new Food();
        Category category = new Category();
        createTable(category);
        createTable(food);
//        CategoryService categoryService = new CategoryService();
//        ArrayList<Category> arrayList = categoryService.findAll();
//        for (int i=0;i<arrayList.size();i++){
//            Category category = arrayList.get(i);
//            System.out.println(category.getName());
//        }
    }

    private static void createTable(Object obj) {
        Class currentClass = obj.getClass();
        if (!currentClass.isAnnotationPresent(Entity.class)) {
            return;
        }
        Entity currentEntity = (Entity) currentClass.getAnnotation(Entity.class);
        //build sql cmd
        StringBuilder stringCmd = new StringBuilder();
        stringCmd.append(SQLConstant.CREATE_TABLE);
        stringCmd.append(SQLConstant.SPACE);
        stringCmd.append(currentEntity.tableName());
        stringCmd.append(SQLConstant.SPACE);
        stringCmd.append(SQLConstant.OPEN_PARENTHESES);
        Field[] fields = currentClass.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }
            Column currentColumn = field.getAnnotation(Column.class);
            stringCmd.append(currentColumn.columnName());
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(currentColumn.columnType());
            //id checker
            if (field.isAnnotationPresent(Id.class)) {
                System.out.println("here");
                Id currentId = (Id) field.getAnnotation(Id.class);
                stringCmd.append(SQLConstant.SPACE);
                stringCmd.append(SQLConstant.PRIMARY_KEY);
                //auto icreament checker
                if (currentId.AutoIncrement()) {
                    stringCmd.append(SQLConstant.SPACE);
                    stringCmd.append(SQLConstant.AUTO_INCREMENT);
                }
            }
            stringCmd.append(SQLConstant.COMMA);
            stringCmd.append(SQLConstant.SPACE);

        }
        stringCmd.setLength(stringCmd.length() - 2);
        stringCmd.append(SQLConstant.CLOSE_PARENTHESES);
        System.out.println(stringCmd.toString());
        Connection connection = ConnectionHelper.getConnection();
        try {
            connection.createStatement().execute(stringCmd.toString());
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }


    }


}
