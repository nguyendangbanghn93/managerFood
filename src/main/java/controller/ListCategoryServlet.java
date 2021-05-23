package controller;

import com.google.gson.Gson;
import service.CategoryService;
import service.FoodService;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class ListCategoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");
        CategoryService categoryService = new CategoryService();
        String json = new Gson().toJson(categoryService.findAll());
        resp.getWriter().write(json);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
