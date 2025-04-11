<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.lang.String" %>

<head>
    <meta charset="UTF-8">
    <title>Restock Shelf</title>
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
                        Restock Shelf
                    </div>
                    <form class="py-4 px-6" action="ShelfRestockServlet" method="post">
                        <div class="mb-4">
                            <label class="block text-gray-700 font-bold mb-2" for="productId">Product ID</label>
                            <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                   id="productId" name="productId" type="text" required placeholder="Enter Product ID">
                        </div>

                        <div class="mb-4">
                            <label class="block text-gray-700 font-bold mb-2" for="quantity">Quantity</label>
                            <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                   id="quantity" name="quantity" type="number" min="1" required placeholder="Enter quantity">
                        </div>

                        <div class="flex items-center justify-center mb-4">
                            <button class="bg-gray-900 text-white py-2 px-4 rounded hover:bg-gray-800 focus:outline-none focus:shadow-outline" type="submit">
                                Restock Shelf
                            </button>
                        </div>

                        <!-- Success or error messages -->
                        <%
                            String message = (String) request.getAttribute("message");
                            String error = (String) request.getAttribute("error");
                        %>
                        <% if (message != null && !message.isEmpty()) { %>
                            <div class="text-green-600 text-center mt-2"><%= message %></div>
                        <% } %>
                        <% if (error != null && !error.isEmpty()) { %>
                            <div class="text-red-600 text-center mt-2"><%= error %></div>
                        <% } %>
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
