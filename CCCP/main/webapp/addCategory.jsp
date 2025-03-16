<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Add Category</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
    <div class="container">
        <h2 class="mt-4">Add New Category</h2>
        <form action="CategoryServlet" method="POST">
            <div class="form-group">
                <label for="categoryName">Category Name</label>
                <input type="text" class="form-control" id="categoryName" name="name" required />
            </div>
            <input type="hidden" name="action" value="create" />
            <button type="submit" class="btn btn-primary mt-3">Create Category</button>
        </form>
    </div>
</body>
</html>
