<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="cccp.model.Category, cccp.model.dao.CategoryDAO" %>
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

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Category</title>
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
                        Edit Category
                    </div>

                    <%
                        int id = Integer.parseInt(request.getParameter("id"));
                        CategoryDAO categoryDAO = new CategoryDAO();
                        Category category = categoryDAO.searchCategory(id);
                    %>

                    <form class="py-4 px-6" action="/CCCP/CategoryServlet" method="POST">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="id" value="<%= category.getId() %>">
                        
                        <div class="mb-4">
                            <label class="block text-gray-700 font-bold mb-2" for="categoryName">Category Name</label>
                            <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" 
                                id="categoryName" name="categoryName" type="text" required placeholder="Enter category name" value="<%= category.getName() %>">
                        </div>
                        
                        <div class="flex items-center justify-center mb-4">
                            <button class="bg-gray-900 text-white py-2 px-4 rounded hover:bg-gray-800 focus:outline-none focus:shadow-outline" type="submit">
                                Update Category
                            </button>
                        </div>
                    </form>

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
