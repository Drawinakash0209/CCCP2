<%@ page import="java.util.List" %>
<%@ page import="cccp.model.Product" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product List</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.0.0/dist/tailwind.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="css/styles.css">
</head>

<div x-data="setup()" :class="{ 'dark': isDark }">
    <div class="min-h-screen flex flex-col flex-auto flex-shrink-0 antialiased bg-white dark:bg-gray-700 text-black dark:text-white">
    
        <!-- Header -->
        <jsp:include page="employee_dashboard_header.jsp" />
        
        <!-- Sidebar -->
        <jsp:include page="employee_dashboard_sidebar.jsp" />
        
        <div class="h-full ml-14 mt-14 mb-10 md:ml-64">
            <div class="container mx-auto px-4 py-8">
                <h1 class="text-3xl font-bold text-center mb-8">Product Listing</h1>
                
                <%
                    String message = (String) request.getAttribute("message");
                    if (message != null) {
                %>
                    <div class="text-center py-2 <%= message.startsWith("Error") ? "text-red-500" : "text-green-500" %>">
                        <%= message %>
                    </div>
                <%
                    }
                %>
                
                <!-- Search and Add Product -->
                <div class="flex flex-col md:flex-row justify-between items-center mb-6">
                    <div class="w-full md:w-1/3 mb-4 md:mb-0">
                        <form action="/CCCP/ActionServlet?option=2" method="GET">
                            <input type="text" name="searchQuery" placeholder="Search products..." class="w-full px-4 py-2 rounded-md border border-gray-300 bg-white focus:outline-none focus:ring-2 focus:ring-blue-500">
                        </form>
                    </div>
                    <a href="add-product.jsp">
                        <button class="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 transition duration-300">
                            Add New Product
                        </button>
                    </a>
                </div>
                
                <!-- Product Table -->
                <div class="overflow-x-auto bg-white rounded-lg shadow">
                    <table class="w-full table-auto">
                        <thead>
                            <tr class="bg-gray-200 text-gray-600 uppercase text-sm leading-normal">
                                <th class="py-3 px-6 text-left">ID</th>
                                <th class="py-3 px-6 text-left">Name</th>
                                <th class="py-3 px-6 text-left">Price</th>
                                <th class="py-3 px-6 text-left">Category ID</th>
                                <th class="py-3 px-6 text-left">Reorder Level</th>
                                <th class="py-3 px-6 text-center">Actions</th>
                            </tr>
                        </thead>
                        <tbody class="text-gray-600 text-sm">
                            <% 
                                List<Product> products = (List<Product>) request.getAttribute("products");
                                if (products != null) {
                                    for (Product product : products) { 
                                        boolean matchesSearch = true;
                                        String searchQuery = request.getParameter("searchQuery") != null ? request.getParameter("searchQuery").toLowerCase() : "";
                                        if (!searchQuery.isEmpty()) {
                                            matchesSearch = product.getId().toLowerCase().contains(searchQuery) ||
                                                            product.getName().toLowerCase().contains(searchQuery);
                                        }
                                        if (matchesSearch) {
                            %>
                                <tr class="border-b border-gray-200 hover:bg-gray-100">
                                    <td class="py-3 px-6 text-left"><%= product.getId() %></td>
                                    <td class="py-3 px-6 text-left"><%= product.getName() %></td>
                                    <td class="py-3 px-6 text-left"><%= product.getPrice() %></td>
                                    <td class="py-3 px-6 text-left"><%= product.getCategoryId() %></td>
                                    <td class="py-3 px-6 text-left"><%= product.getReorderLevel() %></td>
                                    <td class="py-3 px-6 text-center">
                                        <div class="flex justify-center">
                                            <a href="update-product.jsp?id=<%= product.getId() %>" class="w-4 mr-2 transform hover:text-blue-500 hover:scale-110">
                                                <!-- Edit Icon -->
                                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
                                                </svg>
                                            </a>
                                            <form action="/CCCP/ActionServlet?option=2" method="POST" enctype="application/x-www-form-urlencoded" style="display:inline;">
                                                <input type="hidden" name="action" value="delete" />
                                                <input type="hidden" name="id" value="<%= product.getId() %>" />
                                                <button type="submit" class="w-4 mr-2 transform hover:text-red-500 hover:scale-110">
                                                    <!-- Delete Icon -->
                                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                                                    </svg>
                                                </button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            <%  
                                    }
                                }
                            } else { 
                            %>
                                <tr><td colspan="6" class="text-center py-3">No products found.</td></tr>
                            <%  
                                } 
                            %>
                        </tbody>
                    </table>
                </div>
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
            searchQuery: '',
        };
    };
</script>