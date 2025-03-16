<%@ page import="java.util.List, cccp.model.Category" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>View Categories</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
    <div class="container">
        <h2 class="mt-4">Category List</h2>
        <table class="table table-bordered">
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Actions</th>
            </tr>
            <% 
                List<Category> categories = (List<Category>) request.getAttribute("categories");
                if (categories != null) {
                    for (Category category : categories) { %>
                        <tr>
                            <td><%= category.getId() %></td>
                            <td><%= category.getName() %></td>
                            <td>
                                <a href="updateCategory.jsp?id=<%= category.getId() %>" class="btn btn-warning btn-sm">Edit</a>
                                <form action="CategoryServlet" method="POST" style="display:inline;">
                                    <input type="hidden" name="action" value="delete" />
                                    <input type="hidden" name="id" value="<%= category.getId() %>" />
                                    <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                                </form>
                            </td>
                        </tr>
            <%      } 
                } else { %>
                    <tr><td colspan="3">No categories found.</td></tr>
            <%  } %>
        </table>

        <!-- Create new category button -->
        <a href="addCategory.jsp" class="btn btn-primary">Add New Category</a>
    </div>
</body>
</html>
