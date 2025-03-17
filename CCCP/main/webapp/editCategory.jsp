<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="cccp.model.Category, cccp.model.dao.CategoryDAO" %>
<html>
<head>
    <title>Update Category</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
    <div class="container">
        <h2 class="mt-4">Update Category</h2>
        <%
            int id = Integer.parseInt(request.getParameter("id"));
            CategoryDAO categoryDAO = new CategoryDAO();
            Category category = categoryDAO.searchCategory(id);
        %>
        <form action="CategoryServlet" method="POST">
            <div class="form-group">
                <label for="categoryName">Category Name</label>
                <input type="text" class="form-control" id="categoryName" name="categoryName" value="<%= category.getName() %>" required />
            </div>
            <input type="hidden" name="action" value="update" />
            <input type="hidden" name="id" value="<%= category.getId() %>" />
            <button type="submit" class="btn btn-primary mt-3">Update Category</button>
        </form>
    </div>
</body>
</html>