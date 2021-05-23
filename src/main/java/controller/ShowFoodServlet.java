package controller;

import com.google.gson.Gson;
import com.mysql.cj.xdevapi.JsonString;
import entity.Food;
//import com.google.gson.Gson;
//import com.google.gson.JsonElement;
import service.FoodService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShowFoodServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");
        FoodService foodService = new FoodService();
        int id = Integer.parseInt(req.getParameter("id"));
        String json = new Gson().toJson(foodService.findById(id));
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
