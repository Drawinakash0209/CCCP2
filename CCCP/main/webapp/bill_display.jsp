<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="cccp.model.Bill, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bill Details</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
    <h2>Bill Details</h2>
    <% 
        Bill bill = (Bill) request.getAttribute("bill");
        if (bill != null) {
    %>
        <p>Bill ID: <%= bill.getBillId() %></p>
        <p>Date: <%= bill.getBillDate() %></p>
        <table>
            <tr>
                <th>Product Code</th>
                <th>Product Name</th>
                <th>Quantity</th>
                <th>Unit Price</th>
                <th>Total Price</th>
            </tr>
            <% 
                List<Bill.BillItem> billItems = bill.getBillItems();
                if (billItems != null) {
                    for (Bill.BillItem item : billItems) {
            %>
                <tr>
                    <td><%= item.getProductCode() %></td>
                    <td><%= item.getproductName() %></td>
                    <td><%= item.getQuantity() %></td>
                    <td><%= item.getPrice() %></td>
                    <td><%= item.getTotalPrice() %></td>
                </tr>
            <% 
                    }
                }
            %>
        </table>
        <p><strong>Total Price:</strong> <%= bill.getTotalPrice() %></p>
        <p><strong>Cash Tendered:</strong> <%= bill.getCashTendered() %></p>
        <% 
            if (bill.getDiscount() > 0) {
        %>
            <p><strong>Discount:</strong> <%= bill.getDiscount() %></p>
        <% 
            }
        %>
        <p><strong>Change Amount:</strong> <%= bill.getChangeAmount() %></p>
    <% 
        }
    %>
    <br>
    <a href="<%= request.getContextPath() %>/onlineShop?action=viewProducts">Return to Shopping</a>
</body>
</html>