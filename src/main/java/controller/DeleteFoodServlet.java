package controller;

import service.FoodService;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class DeleteFoodServlet extends HttpServlet {
    FoodService foodService = new FoodService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if(foodService.delete(id)){
            resp.getWriter().write("1");
        }else{
            resp.getWriter().write("Xóa thất bại");
        }
    }
}
