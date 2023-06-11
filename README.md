# Inventory Control System

<p>I developed an inventory control system containing some business rules that are described below, this project helped me to evolve a lot, I went through many errors that made me spend a few hours trying to solve them, but that made me evolve, at the moment I am trying to solve some errors in some parts of the code and implementing screens with the back-end for better visualization, I'm using the basics (HTML, CSS and JS) to build the screens.</p>

> Status: Developing ⚠️

### Non-Functional Requirements
+ MySQL Database
+ Swagger for documentation of endpoints
+ Observer pattern send notifications of saved or updated products and movements and whenever the current balance of a product is below the minimum quantity
+ Scheduled to delete reports of inventories deleted after 30 days

### Functional Requirements:
+ Only user 'Ceo' can register new users with 2 profiles disponible 'Manager' and 'Operator' and delete users
+ User 'Manager' can update others users

### Products and Inventory:
+ Only Ceo and Manager can save, update and delete products and movements
+ Must exists only one barcode by product
+ Initial balance must be registered only in the product registration, and cannot be edited
+ Quantity Minimum don't be small than 0
+ If Initial Balance greater than 0, movement type is 'Initial Balance'
+ Barcode do not updated after registration

### Filters Users:
#### Search by:
+ List All
+ Login

### Filters Products:
#### Search by:
+ Name
+ Barcode

### Filters Inventory:
#### Search by: 
+ Product Name
+ Barcode
+ Between Periods
+ Movement Types
+ Orders by Products, Periods and Movement Types
+ List inventory with fields: Movement Date, Product, Movement Type, Document, Reason, Balance and Situation
+ Product Balance < Quantity Minimum
+ Product Balance > Quantity Minimum
+ Product Best Seller

### Movement Registration:
+ Movements types 'Entry' and 'Exit' are allowed just if product exist in inventory
+ If the movement is of the entry type, calculate the entry quantity and current balance
+ If the movement is of the exit type, calculate the output quantity and current balance
+ If movement type is 'Output', generate invoice for this product calculating price total and do not allowed quantity greater than current balance
+ If invoice cancel, reset quantity for this product
+ Only initial balance type movement is allowed for new products
+ Situation: If balance is small than quantity minimum is register as 'Is less than quantity minimum', else is 'Is greater than quantity minimum'
+ Document: Only register if movement type is 'Entry' or 'Exit'
+ Must have movement date and time
+ Reports of inventories deleted must be saved in a bin
+ Whenever a movement is registered or updated send a report on what was done

### Screens created for now:

> Register Product

![register](https://github.com/thalyshenrique7/inventory-control-system/assets/100730757/f4909b48-0853-4291-874b-05674eae69a8)

> Update Product

![image](https://github.com/thalyshenrique7/inventory-control-system/assets/100730757/82965405-2a22-4f75-9eac-5bdf142387d8)

> Movement Save

![image](https://github.com/thalyshenrique7/inventory-control-system/assets/100730757/485460bd-9664-4143-95ec-1170a479a88e)

> Technologies Used:

<table>
<tr align="center">
<td>Java</td>
<td>Spring Boot</td>
<td>MySQL</td>
<td>Swagger</td>
<td>JUnit</td>
<td>Selenium</td>
</tr>

<tr align="center">
<td>11</td>
<td>2.7.12</td>
<td>8.0.32</td>
<td>2.9.2</td>
<td>5</td>
<td>4.9.1</td>
</tr>
</table>

> Author: Thalys Henrique

https://www.linkedin.com/in/thalyshenrique7/
