<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="cccp.model.Category, cccp.model.dao.CategoryDAO, cccp.model.Product, cccp.model.dao.ProductDAO, java.util.List" %>
<%@ page import="cccp.model.User" %>
<%
    // Retrieve the user object from the session
    User user = (User) session.getAttribute("user");

    // Basic check if user is logged in
    if (user == null) {
        response.sendRedirect("login.jsp?error=Please login first");
        return; // Stop further processing of the page
    }

    // Assuming User has a getUsername() method
    String username = user.getUsername();
%>

<html>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Product</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.0.0/dist/tailwind.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="css/styles.css">
</head>

<body>
    <div x-data="setup()" :class="{ 'dark': isDark }">
        <div class="min-h-screen flex flex-col flex-auto flex-shrink-0 antialiased bg-white dark:bg-gray-700 text-black dark:text-white">
            
            <!-- Header -->
            <jsp:include page="employee_dashboard_header.jsp" />
            
            <!-- Sidebar -->
            <jsp:include page="employee_dashboard_sidebar.jsp" />
            
            <div class="h-full ml-14 mt-14 mb-10 md:ml-64">
                <div class="max-w-md mx-auto mt-10 bg-white shadow-lg rounded-lg overflow-hidden">
                    <div class="text-2xl py-4 px-6 bg-gray-900 text-white text-center font-bold uppercase">
                        Update Product
                    </div>
                    <%
                        String productId = request.getParameter("id");
                        ProductDAO productDAO = new ProductDAO();
                        Product product = productDAO.getProductById(productId);
                        if (product == null) {
                    %>
                        <div class="text-center py-2 text-red-500">
                            Product not found.
                        </div>
                    <%
                        } else {
                            String message = (String) request.getAttribute("message");
                            if (message != null) {
                    %>
                        <div class="text-center py-2 <%= message.startsWith("Error") ? "text-red-500" : "text-green-500" %>">
                            <%= message %>
                        </div>
                    <%
                            }
                    %>
                    <form class="py-4 px-6" action="/CCCP/ActionServlet?option=2" method="POST" enctype="application/x-www-form-urlencoded">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="id" value="<%= product.getId() %>">
                        
                        <div class="mb-4">
                            <label class="block text-gray-700 font-bold mb-2" for="name">Product Name</label>
                            <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" 
                                id="name" name="name" type="text" required placeholder="Enter product name" value="<%= product.getName() %>">
                        </div>
                        
                        <div class="mb-4">
                            <label class="block text-gray-700 font-bold mb-2" for="price">Price</label>
                            <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" 
                                id="price" name="price" type="number" step="0.01" required placeholder="Enter product price" value="<%= product.getPrice() %>">
                        </div>
                        
                        <div class="mb-4">
                            <label class="block text-gray-700 font-bold mb-2" for="category_id">Category</label>
                            <select class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" 
                                id="category_id" name="category_id" required>
                                <%
                                    CategoryDAO categoryDAO = new CategoryDAO();
                                    List<Category> categories = categoryDAO.viewAllItemsGUI();
                                    for (Category category : categories) {
                                %>
                                    <option value="<%= category.getId() %>" <%= category.getId() == product.getCategoryId() ? "selected" : "" %>>
                                        <%= category.getName() %>
                                    </option>
                                <%
                                    }
                                %>
                            </select>
                        </div>
                        
                        <div class="mb-4">
                            <label class="block text-gray-700 font-bold mb-2" for="reorder_level">Reorder Level</label>
                            <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" 
                                id="reorder_level" name="reorder_level" type="number" required placeholder="Enter reorder level" value="<%= product.getReorderLevel() %>">
                        </div>
                        
                        <div class="flex items-center justify-center mb-4">
                            <button class="bg-gray-900 text-white py-2 px-4 rounded hover:bg-gray-800 focus:outline-none focus:shadow-outline" type="submit">
                                Update Product
                            </button>
                        </div>
                    </form>
                    <%
                        }
                    %>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/gh/alpinejs/alpine@v2.8.0/dist/alpine.min.js" defer></script>
    <script>
        const setup = () => {
            const getTheme = () => {
                if (window.localStorage.getItem('dark')) {
                    return JSON.parse(window.localStorage.getItem('dark'));
                }
                return !!window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;
            };
            
            const setTheme = (value) => {
                window.localStorage.setItem('dark', value);
            };
            
            return {
                isDark: getTheme(),
                toggleTheme() {
                    this.isDark = !this.isDark;
                    setTheme(this.isDark);
                },
            };
        };
    </script>
</body>
</html>