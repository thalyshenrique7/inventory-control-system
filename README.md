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
+ Only Manager can save movement on stock for types (Initial Balance, Input Adjustment and Output Adjustment)
+ Must exists only one bar code by product
+ Initial balance must be registered only in the product registration, and cannot be edited
+ Quantity Minimum don't be small than 0
+ If Initial Balance greater than 0, movement type is 'Initial Balance'

### Inventory:

### Filters Inventory:
+ Search 'By Bar Code', 'By Product Name', 'Between Time Periods' and 'By Movement Types'
+ Orders: 'By Products', 'By Periods', 'By Movement Types'
+ List with fields: 'Date Movement', 'Product', 'Movement Type',
'Document', 'Reason', 'Balance' and 'Situation'

### Movement Registration:
+ If exists Initial Balance for a product, musn't register again
+ If not exists a product, throw an exception "Product not register on system"
+ If movement type is 'Input or Output Adjustment', throw an exception "Don't exists releases for product"
+ Situation: If balance is small than quantity minimum is register as ' Is small than Quantity Minimum', else is 'OK'
+ Document: Only register if movement type is 'Input' or 'Output'
+ Must have movement date and time
+ Calculate initial balance plus current balance

> Author: Thalys Henrique

https://www.linkedin.com/in/thalyshenrique7/
