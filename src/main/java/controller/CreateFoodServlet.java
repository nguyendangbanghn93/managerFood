package controller;

import entity.Category;
import entity.Food;
import service.CategoryService;
import service.FoodService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class CreateFoodServlet extends HttpServlet {
    CategoryService categoryService = new CategoryService();
    FoodService foodService = new FoodService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ArrayList<Category> categories = categoryService.findAll();
        req.setAttribute("status", "");
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/food/create.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");  // Set content type of the response so that jQuery knows what it can expect.
        resp.setCharacterEncoding("UTF-8"); // You want world domination, huh?
        req.setCharacterEncoding("UTF-8");
        String name = req.getParameter("name");
        String categoryId = req.getParameter("categoryId");
        String description = req.getParameter("description");
        String thumbnail = req.getParameter("thumbnail");
        String price = req.getParameter("price").length()>0?req.getParameter("price"):"0";
        Food food = new Food();
        food.setName(name);
        food.setCategoryId(Integer.parseInt(categoryId));
        food.setDescription(description);
        food.setThumbnail(thumbnail);
        food.setPrice(Double.valueOf(price));
        boolean res = foodService.create(food);
        if (!res) {
            resp.getWriter().write("Tạo món ăn thất bại yêu cầu nhập đầy đủ thông tin và đúng định dạng !!!!");
        }else {
            resp.getWriter().write("1");
        }
    }
}
