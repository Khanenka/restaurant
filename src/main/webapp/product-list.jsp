<%--
  Created by IntelliJ IDEA.
  User: leonid
  Date: 01.07.2024
  Time: 10:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Bootstrap Example</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>

<div class="container">
    <h2>Basic Table</h2>
    <p>The .table class adds basic styling (light padding and horizontal dividers) to a table:</p>
    <table class="table">
        <thead>
        <tr>
            <th>nameProduct</th>
            <th>priceProduct</th>
            <th>quantityProduct</th>
            <th>availableProduct</th>
            <th>Actions</th>

        </tr>
        </thead>
        <tbody>
        <c:forEach items="${listProduct}" var="product" varStatus="count" >
            <tr>
                <td>${product.nameProduct }</td>
                <td>${product.priceProduct }</td>
                <td>${product.quantityProduct }</td>
                <td>${product.availableProduct }</td>
                <td><a href="edit?nameProduct=<c:out value='${product.nameProduct}' />">Edit</a>
                    &nbsp;&nbsp;&nbsp;&nbsp; <a
                            href="delete?nameProduct=<c:out value='${product.nameProduct}' />">Delete</a></td>

            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

</body>
</html>




