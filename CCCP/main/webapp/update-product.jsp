<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="cccp.model.Product, cccp.model.dao.ProductDAO" %>

<%
    String productId = request.getParameter("id");
    ProductDAO productDAO = new ProductDAO();
    Product product = productDAO.getProductById(productId);
%>

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
            <jsp:include page="employee_dashboard_header.jsp" />
            <jsp:include page="employee_dashboard_sidebar.jsp" />

            <div class="h-full ml-14 mt-14 mb-10 md:ml-64">
                <div class="max-w-md mx-auto mt-10 bg-white shadow-lg rounded-lg overflow-hidden">
                    <div class="text-2xl py-4 px-6 bg-gray-900 text-white text-center font-bold uppercase">
                        Update Product
                    </div>
                    <form class="py-4 px-6" action="ProductServlet" method="POST">
                        <input type="hidden" name="action" value="update">
                        <div class="mb-4">
                            <label class="block text-gray-700 font-bold mb-2" for="productId">Product ID</label>
                            <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="productId" name="id" type="text" value="<%= product.getId() %>" readonly>
                        </div>
                        <div class="mb-4">
                            <label class="block text-gray-700 font-bold mb-2" for="productName">Product Name</label>
                            <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="productName" name="name" type="text" value="<%= product.getName() %>" required>
                        </div>
                        <div class="mb-4">
                            <label class="block text-gray-700 font-bold mb-2" for="productPrice">Price</label>
                            <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="productPrice" name="price" type="number" step="0.01" value="<%= product.getPrice() %>" required>
                        </div>
                        <div class="mb-4">
                            <label class="block text-gray-700 font-bold mb-2" for="categoryId">Category ID</label>
                            <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="categoryId" name="category_id" type="number" value="<%= product.getCategoryId() %>" required>
                        </div>
                        <div class="mb-4">
                            <label class="block text-gray-700 font-bold mb-2" for="reorderLevel">Reorder Level</label>
                            <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="reorderLevel" name="reorder_level" type="number" value="<%= product.getReorderLevel() %>" required>
                        </div>
                        <div class="flex items-center justify-center mb-4">
                            <button class="bg-gray-900 text-white py-2 px-4 rounded hover:bg-gray-800 focus:outline-none focus:shadow-outline" type="submit">
                                Update Product
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
                    return JSON.parse(window.localStorage.getItem('dark'))
                }
                return !!window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches
            }

            const setTheme = (value) => {
                window.localStorage.setItem('dark', value)
            }

            return {
                loading: true,
                isDark: getTheme(),
                toggleTheme() {
                    this.isDark = !this.isDark
                    setTheme(this.isDark)
                },
            }
        }
    </script>
</body>
