package controller;

import entity.Food;
import service.FoodService;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class UpdateFoodServlet extends HttpServlet {
    FoodService foodService = new FoodService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");  // Set content type of the response so that jQuery knows what it can expect.
        resp.setCharacterEncoding("UTF-8"); // You want world domination, huh?
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String categoryId = req.getParameter("categoryId");
        String description = req.getParameter("description");
        String thumbnail = req.getParameter("thumbnail");
        String status = req.getParameter("status");
        String price = req.getParameter("price").length()>0?req.getParameter("price"):"0";
        Food food = new Food();
        food.setId(Integer.parseInt(id));
        food.setName(name);
        food.setCategoryId(Integer.parseInt(categoryId));
        food.setDescription(description);
        food.setThumbnail(thumbnail);
        food.setPrice(Double.valueOf(price));
        food.setStatus(Integer.parseInt(status));
        Food res = foodService.edit(food);
        if (res==null) {
            resp.getWriter().write("Cập nhật món ăn thất bại!!!!");
        }else {
            resp.getWriter().write("1");
        }
    }
}
