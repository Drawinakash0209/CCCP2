<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="cccp.model.Category, cccp.model.dao.CategoryDAO, java.util.List" %>
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

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Product</title>
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

        <!-- Main Content -->
        <div class="h-full ml-14 mt-14 mb-10 md:ml-64 px-4">
            <div class="max-w-md mx-auto bg-white dark:bg-gray-800 shadow-lg rounded-lg p-6 mt-10">
                <h1 class="text-2xl font-bold mb-6 text-center dark:text-white">Add New Product</h1>
                <div id="message" class="hidden mb-6 p-4 rounded-md text-center"></div>
                <form id="add-product-form" class="space-y-4">
                    <input type="hidden" name="action" value="create">
                    
                    <div>
                        <label class="block text-gray-700 dark:text-gray-200 font-bold mb-2" for="id">Product ID</label>
                        <input class="w-full px-4 py-2 rounded-md border border-gray-300 bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500" 
                               id="id" name="id" type="text" required placeholder="Enter product ID">
                    </div>
                    
                    <div>
                        <label class="block text-gray-700 dark:text-gray-200 font-bold mb-2" for="name">Product Name</label>
                        <input class="w-full px-4 py-2 rounded-md border border-gray-300 bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500" 
                               id="name" name="name" type="text" required placeholder="Enter product name">
                    </div>
                    
                    <div>
                        <label class="block text-gray-700 dark:text-gray-200 font-bold mb-2" for="price">Price</label>
                        <input class="w-full px-4 py-2 rounded-md border border-gray-300 bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500" 
                               id="price" name="price" type="number" step="0.01" required placeholder="Enter product price">
                    </div>
                    
                    <div>
                        <label class="block text-gray-700 dark:text-gray-200 font-bold mb-2" for="category_id">Category</label>
                        <select class="w-full px-4 py-2 rounded-md border border-gray-300 bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500" 
                                id="category_id" name="category_id" required>
                            <option value="" disabled selected>Select a category</option>
                            <%
                                CategoryDAO categoryDAO = new CategoryDAO();
                                List<Category> categories = categoryDAO.viewAllItemsGUI();
                                for (Category category : categories) {
                            %>
                                <option value="<%= category.getId() %>">
                                    <%= category.getName() %>
                                </option>
                            <%
                                }
                            %>
                        </select>
                    </div>
                    
                    <div>
                        <label class="block text-gray-700 dark:text-gray-200 font-bold mb-2" for="reorder_level">Reorder Level</label>
                        <input class="w-full px-4 py-2 rounded-md border border-gray-300 bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500" 
                               id="reorder_level" name="reorder_level" type="number" required placeholder="Enter reorder level">
                    </div>
                    
                    <div class="flex items-center justify-center">
                        <button class="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 transition duration-300" type="submit">
                            Add Product
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Alpine.js for dark mode toggle -->
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

 // In add-product.jsp, modify the form submission script
    document.getElementById('add-product-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const form = e.target;
        const formData = new FormData(form);
        const messageDiv = document.getElementById('message');

        // Convert FormData to URL-encoded string
        const urlEncodedData = new URLSearchParams(formData).toString();

        try {
            const response = await fetch('/CCCP/ActionServlet?option=2', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: urlEncodedData
            });

            const result = await response.json();
            messageDiv.classList.remove('hidden', 'bg-red-100', 'text-red-700', 'bg-green-100', 'text-green-700');
            messageDiv.textContent = result.message;
            messageDiv.classList.add(result.success ? 'bg-green-100' : 'bg-red-100', result.success ? 'text-green-700' : 'text-red-700');

            if (result.success) {
                form.reset();
                setTimeout(() => {
                    messageDiv.classList.add('hidden');
                    window.location.href = '/CCCP/ProductServlet';
                }, 2000);
            }
        } catch (error) {
            messageDiv.classList.remove('hidden', 'bg-green-100', 'text-green-700');
            messageDiv.classList.add('bg-red-100', 'text-red-700');
            messageDiv.textContent = 'Error: Failed to add product';
        }
    });
</script>
</body>
</html>