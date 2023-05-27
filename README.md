# Inventory Control System

<p>I developed an inventory control system containing some business rules that are described below, this project helped me to evolve a lot, I went through many errors that made me spend a few hours trying to solve them, but that made me evolve, at the moment I am trying to solve some errors in some parts of the code and implementing screens with the back-end for better visualization, I'm using the basics (HTML, CSS and JS) to build the screens.</p>

> Status: Developing ⚠️

### Non-Functional Requirements
+ Data Base H2
+ Spring Security for implementinng constraints, exists two profiles 'Manager' and 'Operator'
+ Swagger for documentation of endpoints

### Functional Requirements:
### Products:
+ Only Manager can save, edit and delete products
+ Only Manager can save and update movement on inventory
+ Must exists only one barcode by product
+ Initial balance must be registered only in the product registration, and cannot be edited
+ Quantity Minimum don't be small than 0
+ If Initial Balance greater than 0, movement type is 'Initial Balance'
+ Barcode do not updated after registration

### Filters Products:
Search by:
+ Name
+ Barcode

### Inventory:

### Filters Inventory:
Search by: 
+ Product Name
+ Barcode
+ Between Time Periods
+ Movement Types
+ Orders by Products, Periods and Movement Types
+ List inventory with fields: Date Movement, Product, Movement Type, Document, Reason, Balance and Situation
+ Product Balance < Quantity Minimum
+ Product Balance > Quantity Minimum

### Movement Registration:
+ If the movement is of the entry type, calculate the entry quantity and current balance
+ If the movement is of the exit type, calculate the output quantity and current balance
+ Only initial balance and entry type movements are allowed for new products
+ Situation: If balance is small than quantity minimum is register as 'Is less than quantity minimum', else is 'Is greater than quantity minimum'
+ Document: Only register if movement type is 'Entry' or 'Exit'
+ Must have movement date and time

> Author: Thalys Henrique

https://www.linkedin.com/in/thalyshenrique7/
