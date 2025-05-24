<%@ page import="java.util.List, cccp.model.Category" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="cccp.model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp?error=Please login first");
        return;
    }
    String username = user.getUsername();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Category Listing</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.0.0/dist/tailwind.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="css/styles.css">
</head>
<body>
<div x-data="setup()" :class="{ 'dark': isDark }">
    <div class="min-h-screen flex flex-col flex-auto flex-shrink-0 antialiased bg-white dark:bg-gray-700 text-black dark:text-white">
        <jsp:include page="employee_dashboard_header.jsp" />
        <jsp:include page="employee_dashboard_sidebar.jsp" />
        <div class="h-full ml-14 mt-14 mb-10 md:ml-64 px-4">
            <div class="max-w-4xl mx-auto bg-white dark:bg-gray-800 shadow-lg rounded-lg p-6 mt-10">
                <h1 class="text-2xl font-bold mb-6 text-center dark:text-white">Category Listing</h1>
                <div id="message" class="hidden mb-6 p-4 rounded-md"></div>
                <div class="flex flex-col md:flex-row justify-between items-center mb-6">
                    <div class="w-full md:w-1/3 mb-4 md:mb-0">
                        <input x-model="searchQuery" class="w-full px-4 py-2 rounded-md border border-gray-300 bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                               type="text" placeholder="Search categories..." @input="filterCategories()">
                    </div>
                    <a href="addCategory.jsp">
                        <button class="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 transition duration-300">
                            Add New Category
                        </button>
                    </a>
                </div>
                <%
                    List<Category> categories = (List<Category>) request.getAttribute("categories");
                    boolean hasData = categories != null && !categories.isEmpty();
                %>
                <div id="category-table-container">
                    <% if (hasData) { %>
                        <div class="overflow-x-auto">
                            <table class="min-w-full text-sm text-left text-gray-700 dark:text-gray-200">
                                <thead class="text-xs uppercase bg-gray-100 dark:bg-gray-700">
                                    <tr>
                                        <th class="px-6 py-3">ID</th>
                                        <th class="px-6 py-3">Name</th>
                                        <th class="px-6 py-3 text-center">Actions</th>
                                    </tr>
                                </thead>
                                <tbody id="category-table-body" class="bg-white dark:bg-gray-800 divide-y divide-gray-200 dark:divide-gray-600">
                                    <% for (Category category : categories) { %>
                                        <tr class="hover:bg-gray-100 dark:hover:bg-gray-700" data-category-id="<%= category.getId() %>">
                                            <td class="px-6 py-4"><%= category.getId() %></td>
                                            <td class="px-6 py-4"><%= category.getName() %></td>
                                            <td class="px-6 py-4 text-center">
                                                <div class="flex justify-center space-x-4">
                                                    <a href="editCategory.jsp?id=<%= category.getId() %>" class="transform hover:text-blue-500 hover:scale-110">
                                                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" class="w-5 h-5">
                                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
                                                        </svg>
                                                    </a>
                                                    <button onclick="deleteCategory('<%= category.getId() %>')" class="transform hover:text-red-500 hover:scale-110">
                                                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" class="w-5 h-5">
                                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                                                        </svg>
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    <% } else { %>
                        <p class="text-center text-gray-500 dark:text-gray-400 italic mt-6">No categories found.</p>
                    <% } %>
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
            filterCategories() {
                const rows = document.querySelectorAll('#category-table-body tr');
                rows.forEach(row => {
                    const id = row.children[0].textContent.toLowerCase();
                    const name = row.children[1].textContent.toLowerCase();
                    const query = this.searchQuery.toLowerCase();
                    row.style.display = id.includes(query) || name.includes(query) ? '' : 'none';
                });
            }
        };
    };

    async function deleteCategory(categoryId) {
        const messageDiv = document.getElementById('message');
        try {
            const response = await fetch('/CCCP/CategoryServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams({
                    'action': 'delete',
                    'id': categoryId
                })
            });
            const result = await response.json();
            messageDiv.classList.remove('hidden', 'bg-red-100', 'text-red-700', 'bg-green-100', 'text-green-700');
            messageDiv.textContent = result.message;
            messageDiv.classList.add(result.success ? 'bg-green-100' : 'bg-red-100', result.success ? 'text-green-700' : 'text-red-700');
            
            if (result.success) {
                await refreshCategoryTable();
                setTimeout(() => messageDiv.classList.add('hidden'), 2000);
            }
        } catch (error) {
            console.error('Delete error:', error);
            messageDiv.classList.remove('hidden', 'bg-green-100', 'text-green-700');
            messageDiv.classList.add('bg-red-100', 'text-red-700');
            messageDiv.textContent = 'Error: Failed to delete category';
        }
    }

    async function refreshCategoryTable() {
        try {
            const response = await fetch('/CCCP/CategoryServlet?t=' + Date.now(), {
                method: 'GET',
                headers: {
                    'Accept': 'text/html'
                }
            });
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            const html = await response.text();
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');
            const newTableContainer = doc.querySelector('#category-table-container');
            if (!newTableContainer) {
                throw new Error('Table container not found in response');
            }
            document.getElementById('category-table-container').innerHTML = newTableContainer.innerHTML;
            const searchInput = document.querySelector('input[x-model="searchQuery"]');
            if (searchInput && searchInput._x_model.get()) {
                setup().filterCategories();
            }
        } catch (error) {
            console.error('Error refreshing category table:', error);
            // Suppress error message during polling to avoid user confusion
        }
    }

    // Start polling every 5 seconds
    setInterval(refreshCategoryTable, 5000);
</script>
</body>
</html>