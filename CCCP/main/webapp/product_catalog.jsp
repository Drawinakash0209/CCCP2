<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, cccp.model.Product" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Catalog</title>
    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        /* Custom styles for modern UI */
        .card {
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }
        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
        }
        .btn-primary {
            background: linear-gradient(to right, #3b82f6, #1e40af);
            transition: background 0.3s ease;
        }
        .btn-primary:hover {
            background: linear-gradient(to right, #1e40af, #3b82f6);
        }
        .btn-secondary {
            background: linear-gradient(to right, #6b7280, #4b5563);
        }
        .btn-secondary:hover {
            background: linear-gradient(to right, #4b5563, #6b7280);
        }
        .quantity-input {
            transition: all 0.2s ease;
        }
        .quantity-input:focus {
            outline: none;
            ring: 2px solid #3b82f6;
            box-shadow: 0 0 5px rgba(59, 130, 246, 0.5);
        }
        /* Dark mode support */
        @media (prefers-color-scheme: dark) {
            body {
                background-color: #1f2937;
                color: #f9fafb;
            }
            .card {
                background-color: #374151;
                border-color: #4b5563;
            }
            .message-success, .message-error {
                color: #f9fafb;
            }
        }
        /* Responsive grid */
        .product-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 1.5rem;
        }
    </style>
</head>
<body class="bg-gray-50 font-sans antialiased">
   <header class="bg-gradient-to-r from-blue-600 to-indigo-700 text-white py-6">
    <div class="container mx-auto px-4 flex justify-between items-center">
        <h1 class="text-3xl font-extrabold tracking-tight">Syntex - Product Catalog</h1>
  	 <a href="<%= request.getContextPath() %>/customer.jsp" 
   class="btn-secondary text-white px-6 py-2 rounded-lg font-medium hover:shadow-lg transition-all">
    Go Back
	</a>
    </div>
</header>

    <main class="container mx-auto px-4 py-10">
        <% 
            String message = (String) request.getAttribute("message");
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (message != null && !message.isEmpty()) {
        %>
            <div class="message-success bg-green-50 border-l-4 border-green-500 text-green-700 p-4 mb-8 rounded-lg shadow-md" role="alert">
                <%= message %>
            </div>
        <% 
            }
            if (errorMessage != null && !errorMessage.isEmpty()) {
        %>
            <div class="message-error bg-red-50 border-l-4 border-red-500 text-red-700 p-4 mb-8 rounded-lg shadow-md" role="alert">
                <%= errorMessage %>
            </div>
        <% 
            }
        %>

        <div class="product-grid">
            <% 
                List<Product> products = (List<Product>) request.getAttribute("products");
                if (products != null) {
                    for (Product product : products) {
            %>
                <div class="card bg-white border border-gray-200 rounded-xl p-6 shadow-lg">
                    <h3 class="text-lg font-semibold text-gray-800"><%= product.getName() %></h3>
                    <p class="text-xl font-bold text-blue-600 mb-4">Rs.<%= String.format("%.2f", product.getPrice()) %></p>
                    <form action="<%= request.getContextPath() %>/onlineShop" method="post" class="flex items-center space-x-3">
                        <input type="hidden" name="action" value="addToCart">
                        <input type="hidden" name="productId" value="<%= product.getId() %>">
				   <input type="number" name="quantity" value="1" min="1" 
				       class="quantity-input w-20 border border-gray-300 rounded-lg px-3 py-2 text-sm text-black focus:ring-2 focus:ring-blue-500"
				       aria-label="Quantity for <%= product.getName() %>">

                        <input type="submit" value="Add to Cart" 
                               class="btn-primary flex-1 text-white px-4 py-2 rounded-lg font-medium hover:shadow-lg cursor-pointer"
                               aria-label="Add <%= product.getName() %> to cart">
                    </form>
                </div>
            <% 
                    }
                } else {
            %>
                <div class="col-span-full text-center py-10 text-gray-500 text-lg">
                    No products available.
                </div>
            <% 
                }
            %>
        </div>

        <div class="mt-8 text-center">
            <a href="<%= request.getContextPath() %>/onlineShop?action=viewCart" 
               class="btn-secondary inline-block text-white px-8 py-3 rounded-lg font-medium hover:shadow-lg transition-all">
                View Cart
            </a>
        </div>
    </main>
</body>
</html>