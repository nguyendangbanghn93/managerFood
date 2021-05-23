package controller;

import entity.Food;
import service.CategoryService;
import service.FoodService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class ListFoodServlet extends HttpServlet {
    CategoryService categoryService = new CategoryService();
    FoodService foodService = new FoodService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int count = foodService.countFood();
        int page = Integer.parseInt(req.getParameter("page")==null?"1":req.getParameter("page"));
        int num = Integer.parseInt(req.getParameter("num")==null?"5":req.getParameter("num"));
        int numPage = (int) Math.ceil(count/num);//để hiển thị số trang
        System.out.println(count+"--"+page+"--"+num);
        req.setAttribute("numPage", numPage);
        req.setAttribute("foods", foodService.findByPage(page,num));
        req.setAttribute("categories", categoryService.findAll());
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
