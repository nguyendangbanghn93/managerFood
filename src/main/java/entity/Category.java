package entity;

import helper.SQLDataTypes;
import modelAnnotion.Column;
import modelAnnotion.Entity;
import modelAnnotion.Id;

import java.util.Date;

@Entity(tableName = "category")
public class Category {
    @Id(AutoIncrement = true,PrimaryKey = true)
    @Column(columnName = "id", columnType = SQLDataTypes.INTEGER)
    private int id;
    @Column(columnName = "name",columnType = SQLDataTypes.VARCHAR255)
    private String name;
    @Column(columnName = "updatedAt",columnType = SQLDataTypes.DATETIME)
    private Date updatedAt;
    @Column(columnName = "createdAt",columnType = SQLDataTypes.DATETIME)
    private Date createdAt;
    @Column(columnName = "status",columnType = SQLDataTypes.INTEGER)
    private int status;
    public String toStatus(int status){
        switch (status){
            case 0:
                return "Xóa";
            default:
                return "Hoạt động";
        }
    }
    public boolean validate(){
        if(this.getName().trim().length()>0){
            return true;
        }
        return false;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
