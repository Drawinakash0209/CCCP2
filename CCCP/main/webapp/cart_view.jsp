<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map, cccp.model.Product" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Shopping Cart</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .message { color: green; }
        .error { color: red; }
    </style>
</head>
<body>
    <h2>Your Cart</h2>
    <% 
        String message = (String) request.getAttribute("message");
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (message != null && !message.isEmpty()) {
    %>
        <p class="message"><%= message %></p>
    <% 
        }
        if (errorMessage != null && !errorMessage.isEmpty()) {
    %>
        <p class="error"><%= errorMessage %></p>
    <% 
        }
        Map<Product, Integer> detailedCart = (Map<Product, Integer>) request.getAttribute("detailedCart");
        Double cartTotal = (Double) request.getAttribute("cartTotal");
        if (detailedCart == null || detailedCart.isEmpty()) {
    %>
        <p>Your cart is empty.</p>
    <% 
        } else {
    %>
        <table>
            <tr>
                <th>Product Name</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Total</th>
                <th>Action</th>
            </tr>
            <% 
                for (Map.Entry<Product, Integer> entry : detailedCart.entrySet()) {
                    Product product = entry.getKey();
                    Integer quantity = entry.getValue();
            %>
                <tr>
                    <td><%= product.getName() %></td>
                    <td><%= product.getPrice() %></td>
                    <td>
                        <form action="<%= request.getContextPath() %>/onlineShop" method="post">
                            <input type="hidden" name="action" value="updateCart">
                            <input type="hidden" name="productId" value="<%= product.getId() %>">
                            <input type="number" name="quantity" value="<%= quantity %>" min="0" style="width: 50px;">
                            <input type="submit" value="Update">
                        </form>
                    </td>
                    <td><%= product.getPrice() * quantity %></td>
                    <td>
                        <form action="<%= request.getContextPath() %>/onlineShop" method="post">
                            <input type="hidden" name="action" value="updateCart">
                            <input type="hidden" name="productId" value="<%= product.getId() %>">
                            <input type="hidden" name="quantity" value="0">
                            <input type="submit" value="Remove">
                        </form>
                    </td>
                </tr>
            <% 
                }
            %>
            <tr>
                <td colspan="3" style="text-align: right;"><strong>Total:</strong></td>
                <td colspan="2"><%= cartTotal %></td>
            </tr>
        </table>
        <br>
        <form action="<%= request.getContextPath() %>/onlineShop" method="post">
            <input type="hidden" name="action" value="checkout">
            <input type="submit" value="Proceed to Checkout">
        </form>
    <% 
        }
    %>
    <br>
    <a href="<%= request.getContextPath() %>/onlineShop?action=viewProducts">Continue Shopping</a>
</body>
</html>