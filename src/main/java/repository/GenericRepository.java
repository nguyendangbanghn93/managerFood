package repository;

import helper.ConnectionHelper;
import helper.ConvertHelper;
import helper.SQLConstant;
import helper.SQLDataTypes;
import modelAnnotion.Column;
import modelAnnotion.Entity;
import modelAnnotion.Id;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenericRepository<T> {
    private Class<T> clazz;

    public GenericRepository(Class<T> clazz) {
        this.clazz = clazz;
    }

    private boolean isEntity() {
        return clazz.isAnnotationPresent(Entity.class);
    }

    public boolean save(T obj) {
        try {
            if (!isEntity()) {
                throw new MyModelError("Not an entity model check your annotation");
            }
            Entity currentEntity = (Entity) clazz.getAnnotation(Entity.class);
            //build sql cmd
            StringBuilder stringCmd = new StringBuilder();
            stringCmd.append(SQLConstant.INSERT_INTO);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(currentEntity.tableName());
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.OPEN_PARENTHESES);
            Field[] fields = clazz.getDeclaredFields();

            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if (!field.isAnnotationPresent(Column.class)) {
                    continue;
                }
                Column currentColumn = field.getAnnotation(Column.class);
                //id checker
                if (field.isAnnotationPresent(Id.class)) {
                    Id currentId = (Id) field.getAnnotation(Id.class);
                    if (currentId.AutoIncrement()) {
                        continue;
                    }
                }
                stringCmd.append(currentColumn.columnName());
                stringCmd.append(SQLConstant.COMMA);
                stringCmd.append(SQLConstant.SPACE);

            }
            stringCmd.setLength(stringCmd.length() - 2);
            stringCmd.append(SQLConstant.CLOSE_PARENTHESES);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.VALUES);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.OPEN_PARENTHESES);
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if (!field.isAnnotationPresent(Column.class)) {
                    continue;
                }
                Column currentColumn = field.getAnnotation(Column.class);
                String columnType = currentColumn.columnType();
                field.setAccessible(true);
                Object value = field.get(obj);
                if (columnType.equals(SQLDataTypes.DATE)) {
                    Date date = (Date) value;
                    value = ConvertHelper.convertJavaDateToSqlDate(date);
                }
                if (columnType.equals(SQLDataTypes.DATETIME) || columnType.equals(SQLDataTypes.TIME_STAMP)) {
                    Date date = (Date) value;
                    value = ConvertHelper.convertJavaDateToSqlDateTime(date);
                }
                //id checker
                if (field.isAnnotationPresent(Id.class)) {
                    Id currentId = (Id) field.getAnnotation(Id.class);
                    if (currentId.AutoIncrement()) {
                        continue;
                    }
                }
                if (value == null) {
                    //append null
                    stringCmd.append(SQLConstant.NULL);
                    stringCmd.append(SQLConstant.COMMA);
                    stringCmd.append(SQLConstant.SPACE);
                    continue;
                }
                if (SQLDataTypes.needApostrophe(columnType)) {
                    stringCmd.append(SQLConstant.APOSTROPHE);
                }
                stringCmd.append(value);
                if (SQLDataTypes.needApostrophe(columnType)) {
                    stringCmd.append(SQLConstant.APOSTROPHE);
                }
                stringCmd.append(SQLConstant.COMMA);
                stringCmd.append(SQLConstant.SPACE);

            }
            stringCmd.setLength(stringCmd.length() - 2);
            stringCmd.append(SQLConstant.CLOSE_PARENTHESES);
            ConnectionHelper.getConnection().createStatement().execute(stringCmd.toString());
            return true;
        } catch (IllegalAccessException | MyModelError | SQLException e) {
            System.err.printf("Save Model Error: %s.\n", e.getMessage());
        }

        return false;
    }

    public T findById(Object id) {
        try {
            if (!isEntity()) {
                throw new MyModelError("Not an entity model check your annotation");
            }
            //select * from teachers where id = {id}
            //build sql cmd
            StringBuilder strCmd = new StringBuilder();
            strCmd.append(SQLConstant.SELECT_ASTERISK);
            strCmd.append(SQLConstant.SPACE);
            strCmd.append(SQLConstant.FROM);
            strCmd.append(SQLConstant.SPACE);
            //get table name
            Entity currentEntity = clazz.getDeclaredAnnotation(Entity.class);
            String tableName = currentEntity.tableName();
            strCmd.append(tableName);
            strCmd.append(SQLConstant.SPACE);
            strCmd.append(SQLConstant.WHERE);
            strCmd.append(SQLConstant.SPACE);
            //get id column name
            for (Field field : clazz.getDeclaredFields()) {
                //skip non column
                if (!field.isAnnotationPresent(Column.class)) {
                    continue;
                }

                if (field.isAnnotationPresent(Id.class)) {
                    Column column = field.getAnnotation(Column.class);
                    strCmd.append(column.columnName());
                    strCmd.append(SQLConstant.SPACE);
                    strCmd.append(SQLConstant.EQUAL);
                    strCmd.append(SQLConstant.SPACE);
                    //append value depends on value type
                    if (!column.columnType().equals(SQLDataTypes.INTEGER)) {
                        strCmd.append(SQLConstant.APOSTROPHE);
                    }
                    strCmd.append(id);
                    if (!column.columnType().equals(SQLDataTypes.INTEGER)) {
                        strCmd.append(SQLConstant.APOSTROPHE);
                    }
                    break;
                }
            }
            System.out.println(strCmd.toString());
            PreparedStatement preparedStatement = ConnectionHelper.getConnection().prepareStatement(strCmd.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) { // trỏ đến các bản ghi cho đến khi trả về false.
                T obj = clazz.newInstance(); // khởi tạo ra đối tượng cụ thể của class T.
                for (Field field : clazz.getDeclaredFields()) {
                    if (!field.isAnnotationPresent(Column.class)) {
                        continue;
                    }
                    field.setAccessible(true);
                    Column columnInfor = field.getAnnotation(Column.class);
                    switch (columnInfor.columnType()) {
                        case SQLDataTypes.INTEGER:
                            // set giá trị của trường đó cho đối tượng mới tạo ở trên.
                            field.set(obj, resultSet.getInt(columnInfor.columnName()));
                            break;
                        case SQLDataTypes.VARCHAR255:
                        case SQLDataTypes.VARCHAR50:
                        case SQLDataTypes.TEXT:
                            field.set(obj, resultSet.getString(columnInfor.columnName()));
                            break;
                        case SQLDataTypes.DOUBLE:
                            field.set(obj, resultSet.getDouble(columnInfor.columnName()));
                            break;
                        case SQLDataTypes.DATE:
                            field.set(obj, ConvertHelper.convertSqlDateToJavaDate(resultSet.getDate(columnInfor.columnName())));
                            break;
                        case SQLDataTypes.DATETIME:
                        case SQLDataTypes.TIME_STAMP:
                            field.set(obj, ConvertHelper.convertSqlTimeStampToJavaDate(resultSet.getTimestamp(columnInfor.columnName())));
                            break;
                    }
                }
                return obj;
            }
        } catch (MyModelError | SQLException | InstantiationException | IllegalAccessException error) {
            System.out.printf("Find by Id model error: %s \n", error.getMessage());
        }
        return null;
    }

    public ArrayList<T> findAll() {
        //select * from table name
        //build sql command
        try {
            if (!isEntity()) {
                throw new MyModelError("Not an entity class");
            }
            String tableName = clazz.getAnnotation(Entity.class).tableName();
            StringBuilder stringCmd = new StringBuilder();
            stringCmd.append(SQLConstant.SELECT_ASTERISK);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.FROM);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(tableName);
            //execute command
            ArrayList<T> listObj = new ArrayList<T>();
            PreparedStatement preparedStatement = ConnectionHelper.getConnection().prepareStatement(stringCmd.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                T obj = clazz.newInstance();
                for (Field field : clazz.getDeclaredFields()) {
                    if (!field.isAnnotationPresent(Column.class)) {
                        continue;
                    }
                    field.setAccessible(true);
                    Column columnInfor = field.getAnnotation(Column.class);
                    switch (columnInfor.columnType()) {
                        case SQLDataTypes.INTEGER:
                            // set giá trị của trường đó cho đối tượng mới tạo ở trên.
                            field.set(obj, resultSet.getInt(columnInfor.columnName()));
                            break;
                        case SQLDataTypes.VARCHAR255:
                        case SQLDataTypes.VARCHAR50:
                            field.set(obj, resultSet.getString(columnInfor.columnName()));
                            break;
                        case SQLDataTypes.DOUBLE:
                            field.set(obj, resultSet.getDouble(columnInfor.columnName()));
                            break;
                        case SQLDataTypes.DATE:
                            field.set(obj, ConvertHelper.convertSqlDateToJavaDate(resultSet.getDate(columnInfor.columnName())));
                            break;
                        case SQLDataTypes.DATETIME:
                        case SQLDataTypes.TIME_STAMP:
                            field.set(obj, ConvertHelper.convertSqlTimeStampToJavaDate(resultSet.getTimestamp(columnInfor.columnName())));
                            break;
                    }
                }
                listObj.add(obj);

            }
            return listObj;
        } catch (MyModelError | InstantiationException | IllegalAccessException | SQLException error) {
            System.err.printf("Find all error %s\n", error.getMessage());
        }
        return null;
    }

    public ArrayList<T> findByCondition(String stringCmd) {
        ArrayList<T> listObj = new ArrayList<T>();
        try {
            if (!isEntity()) {
                throw new MyModelError("Not an entity class");
            }
            System.out.println(stringCmd);
            PreparedStatement preparedStatement = ConnectionHelper.getConnection().prepareStatement(stringCmd.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                T obj = clazz.newInstance();
                for (Field field : clazz.getDeclaredFields()) {
                    if (!field.isAnnotationPresent(Column.class)) {
                        continue;
                    }
                    field.setAccessible(true);
                    Column columnInfor = field.getAnnotation(Column.class);

                    switch (columnInfor.columnType()) {
                        case SQLDataTypes.INTEGER:
                            // set giá trị của trường đó cho đối tượng mới tạo ở trên.
                            field.set(obj, resultSet.getInt(columnInfor.columnName()));
                            break;
                        case SQLDataTypes.VARCHAR255:
                        case SQLDataTypes.VARCHAR50:
                            field.set(obj, resultSet.getString(columnInfor.columnName()));
                            break;
                        case SQLDataTypes.DOUBLE:
                            field.set(obj, resultSet.getDouble(columnInfor.columnName()));
                            break;
                        case SQLDataTypes.DATE:
                            field.set(obj, ConvertHelper.convertSqlDateToJavaDate(resultSet.getDate(columnInfor.columnName())));
                            break;
                        case SQLDataTypes.DATETIME:
                        case SQLDataTypes.TIME_STAMP:
                            field.set(obj, ConvertHelper.convertSqlTimeStampToJavaDate(resultSet.getTimestamp(columnInfor.columnName())));
                            break;
                    }
                }
                listObj.add(obj);
            }
        } catch (MyModelError | InstantiationException | IllegalAccessException | SQLException error) {
            System.err.printf("Find all error %s\n", error.getMessage());
            Logger.getLogger("Hello bug").log(Level.SEVERE, error.getMessage());
        }
        return listObj;
    }

    public T update(T obj) {
        //update {table_name} SET column1 = value 1, column2 = value 2 where id = {id}
        //not allow to update id
        try {
            if (!isEntity()) {
                throw new MyModelError("Not an entity model check your annotation");
            }
            String tableName = clazz.getAnnotation(Entity.class).tableName();
            StringBuilder stringCmd = new StringBuilder();
            stringCmd.append(SQLConstant.UPDATE);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(tableName);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.SET);
            stringCmd.append(SQLConstant.SPACE);
            Field[] fields = clazz.getDeclaredFields();
            //id information
            String idName = "";
            String idValue = "";
            String idType = "";
            for (Field field : fields) {
                if (!field.isAnnotationPresent(Column.class)) {
                    continue;
                }

                field.setAccessible(true);
                Column columnInformation = field.getDeclaredAnnotation(Column.class);
                String columnName = columnInformation.columnName();
                String columnType = columnInformation.columnType();
                Object value = field.get(obj);
                if (columnType.equals(SQLDataTypes.DATE)) {
                    Date date = (Date) value;
                    value = ConvertHelper.convertJavaDateToSqlDate(date);
                }
                if (columnType.equals(SQLDataTypes.DATETIME) || columnType.equals(SQLDataTypes.TIME_STAMP)) {
                    Date date = (Date) value;
                    value = ConvertHelper.convertJavaDateToSqlDateTime(date);
                }
                if (field.isAnnotationPresent(Id.class)) {
                    //dont update id
                    //but get id information
                    idName = columnName;
                    idValue = value.toString();
                    idType = columnType;
                    continue;
                }
                stringCmd.append(columnName);
                stringCmd.append(SQLConstant.SPACE);
                stringCmd.append(SQLConstant.EQUAL);
                stringCmd.append(SQLConstant.SPACE);
                if (value == null) {
                    stringCmd.append(SQLConstant.NULL);
                    stringCmd.append(SQLConstant.COMMA);
                    stringCmd.append(SQLConstant.SPACE);
                    continue;
                }
                if (!columnType.equals(SQLDataTypes.INTEGER)) {
                    stringCmd.append(SQLConstant.APOSTROPHE);
                }
                stringCmd.append(value);
                if (!columnType.equals(SQLDataTypes.INTEGER)) {
                    stringCmd.append(SQLConstant.APOSTROPHE);
                }
                stringCmd.append(SQLConstant.COMMA);
                stringCmd.append(SQLConstant.SPACE);
            }
            stringCmd.setLength(stringCmd.length() - 2);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.WHERE);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(idName);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.EQUAL);
            stringCmd.append(SQLConstant.SPACE);
            if (!idType.equals(SQLDataTypes.INTEGER)) {
                stringCmd.append(SQLConstant.APOSTROPHE);
            }
            stringCmd.append(idValue);
            if (!idType.equals(SQLDataTypes.INTEGER)) {
                stringCmd.append(SQLConstant.APOSTROPHE);
            }
            ConnectionHelper.getConnection().createStatement().execute(stringCmd.toString());
            return obj;
        } catch (MyModelError | IllegalAccessException | SQLException error) {
            System.out.printf("Update  failed error: %s \n", error.getMessage());
        }
        return null;
    }

    public boolean delete(Object id) {
        //delete from {tableName} where id = id
        try {
            if (!isEntity()) {
                throw new MyModelError("Not an entity model check your annotation");
            }
            String tableName = clazz.getAnnotation(Entity.class).tableName();
            StringBuilder stringCmd = new StringBuilder();
            stringCmd.append(SQLConstant.DELETE);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.FROM);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(tableName);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.WHERE);
            //id information
            String idName = "";
            String idType = "";
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(Column.class)) {
                    continue;
                }

                field.setAccessible(true);
                Column columnInformation = field.getDeclaredAnnotation(Column.class);
                String columnName = columnInformation.columnName();
                String columnType = columnInformation.columnType();
                if (field.isAnnotationPresent(Id.class)) {
                    //dont update id
                    //but get id information
                    idName = columnName;
                    idType = columnType;
                    break;
                }
            }
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(idName);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.EQUAL);
            stringCmd.append(SQLConstant.SPACE);
            if (!idType.equals(SQLDataTypes.INTEGER)) {
                stringCmd.append(SQLConstant.APOSTROPHE);
            }
            stringCmd.append(id);
            if (!idType.equals(SQLDataTypes.INTEGER)) {
                stringCmd.append(SQLConstant.APOSTROPHE);
            }
            ConnectionHelper.getConnection().createStatement().execute(stringCmd.toString());
            return true;
        } catch (MyModelError | SQLException error) {
            System.out.printf("Delete failed  error: %s \n", error.getMessage());
        }
        return false;
    }

    public int countCondition(String stringCmd) {
        PreparedStatement preparedStatement = null;
        int paging=0;
        try {
            preparedStatement = ConnectionHelper.getConnection().prepareStatement(stringCmd.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                paging= resultSet.getInt("COUNT(*)");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return paging;
    }
}
