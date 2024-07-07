<%@ page import="com.khanenka.restapiservlet.model.CategoryType" %>

<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%--<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>--%>

<%--<!DOCTYPE html>--%>
<%--<html lang="en">--%>
<%--<head>--%>
<%--    <title>Your Add Product</title>--%>
<%--</head>--%>
<%--<body>--%>
<%--<h1>Add Product</h1>--%>


<%--<form action="/insert" method="post">--%>
<%--    <label>Name: <input type="text" id="nameProduct" name="nameProduct"></label><br>--%>
<%--    &lt;%&ndash;    <label>Price: <input type="text" name="price"></label><br>&ndash;%&gt;--%>
<%--    <label>Price: <input type="text" id="priceProduct" name="priceProduct"></label><br>--%>
<%--    <label>Quantity: <input type=number id="quantityProduct" name="quantityProduct"></label><br>--%>
<%--    <label>Available: <input type="checkbox" id="availableProduct" name="availableProduct" value="true"></label><br>--%>

<%--    <button type="submit" />Add Product</button>--%>

<%--</form>--%>


<%--</body>--%>
<%--</html>--%>


<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>User Management Application</title>
    <link rel="stylesheet"
          href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
          crossorigin="anonymous">
</head>
<body>

<header>
    <nav class="navbar navbar-expand-md navbar-dark"
         style="background-color: tomato">
        <div>
            <a href="https://www.javaguides.net" class="navbar-brand"> Product Category</a>
        </div>

        <ul class="navbar-nav">
            <li><a href="<%=request.getContextPath()%>/list"
                   class="nav-link">Product Category</a></li>
        </ul>
    </nav>
</header>
<br>
<div class="container col-md-5">
    <div class="card">
        <div class="card-body">
            <c:if test="${productCategory != null}">
            <form action="/update" method="post">
                </c:if>
                <c:if test="${productCategory == null}">
                <form action="product-category-form" method="post">
                    </c:if>

                    <caption>
                        <h2>
                            <c:if test="${productCategory != null}">
                                Edit Product
                            </c:if>
                            <c:if test="${productCategory == null}">
                                Add New Product Category
                            </c:if>
                        </h2>
                    </caption>

                    <%--                    <c:if test="${product != null}">--%>
                    <%--                        <input type="hidden" name="id" value="<c:out value='${product.id}' />" />--%>
                    <%--                    </c:if>--%>

                    <label>Product Category: <input type="text" id="name" name="name"></label><br>
                    <%--    <label>Price: <input type="text" name="price"></label><br>--%>
                    <%--                    <label>Type: <input type="text" id="type" name="type" ></label><br>--%>




                        <label>Type Category: <select name="type">
                        <c:forEach items="${CategoryType.values()}" var="type">
                            <option value="${type}" %>${type}</option>
                        </c:forEach>
                        </select></label>
                        <br>

                    <button type="submit"/>
                    Add Product Category</button>

                </form>


            </form>
        </div>
    </div>
</div>
</body>
</html>
