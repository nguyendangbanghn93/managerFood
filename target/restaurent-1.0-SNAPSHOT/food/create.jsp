<%@ page import="jdk.nashorn.internal.ir.RuntimeNode" %>
<%@ page import="entity.Food" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="entity.Category" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String status = (String) request.getAttribute("status");
    ArrayList<Category> categories = (ArrayList<Category>) request.getAttribute("categories");
%>
<html>
<head>
    <title>Register</title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

</head>
<body>
<h4 class="text-center">FOOD</h4>
<div style="width: 80vw; margin: 0 auto">
    <p class="text-danger"><%=status%>
    </p>
    <form method="post" action="/food/create">
        <%--        Tên món ăn--%>
        <div class="form-group">
            <label for="name">Tên món ăn </label>
            <input type="text" class="form-control" id="name" placeholder="Nhập tên món ăn" name="name">
        </div>
        <%--    Danh mục--%>
        <div class="form-group">
            <label for="categoryId">Danh mục</label>
            <select class="form-control" id="categoryId" name="categoryId">
                <%for (Category category:categories) { %>
                <option value=<%=category.getId()%> ><%=category.getName()%></option>
                <%}%>
            </select>
        </div>
        <%--    Mô tả--%>
        <div class="form-group">
            <label for="description">Mô tả </label>
            <textarea class="form-control" id="description" name="description" placeholder="Mô tả" rows="3"></textarea>
        </div>
        <div class="form-group">
            <label for="thumbnail">Link ảnh đại diện</label>
            <input type="text" class="form-control" id="thumbnail" placeholder="Nhập link ảnh" name="thumbnail">
        </div>
        <div class="form-group">
            <label for="price">Giá</label>
            <input type="number" class="form-control" id="price" placeholder="Nhập giá tiền" name="price">
        </div>
        <button type="submit" class="btn btn-primary">Tạo món ăn</button>
    </form>
</div>
</body>
</html>
