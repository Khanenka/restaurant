REST Restaurant Service
Описание проекта
REST Restaurant Service — это сервис, реализованный с использованием Java Servlet и JDBC, который предоставляет возможность управления заказами и продуктами в ресторане. Сервис поддерживает CRUD операции для сущностей OrderDetail,Product,ProductCategory.

Технологии
Java 17
Servlet API
JDBC
PostgreSQL
Lombok
JUnit & Mockito
Testcontainers
Maven
Сущности
1. OrderDetail
Поля:
id (Long)
orderStatus (OrderStatus) — статус заказа
products (List<Product>) — список продуктов (связь OneToMany)
totalAmount (BigDecimal) — общая сумма заказа
2. Product
Описание: сущность продукта.
Поля:
id (Long)
name (String) — название продукта
price (BigDecimal) — цена продукта
quantity (int) — количество
available (boolean) — доступность
productCategories (List<ProductCategory>) — список категорий продукта (связь ManyToMany)
3. ProductCategory
Описание: сущность категории продукта.
Поля:
id (Long)
name (String) — название категории
type (CategoryType) — тип категории
products (List<Product>) — список продуктов в категории (связь ManyToMany)
Установка и запуск
Клонируйте репозиторий:

 
Настройте базу данных PostgreSQL:

Создайте базу данных с именем restaurant_db (или другим по вашему выбору).
Создайте пользователя с правами на эту базу данных.
Настройка application.properties:

Создайте файл application.properties в папке src/main/resources и добавьте следующие параметры:

 
Copy
dbUrl=jdbc:postgresql://localhost:5432/restaurant_db
dbUsername=your_db_username
dbPassword=your_db_password
dbDriver=org.postgresql.Driver
Запуск приложения:

Убедитесь, что PostgreSQL запущен.
Запустите ваш сервер приложений (например, Apache Tomcat) и разверните приложение.

Тестирование
Для юнит-тестирования используется JUnit и Mockito.
Интеграционные тесты с использованием Testcontainers для взаимодействия с базой данных.
Для этого запустите Docker
