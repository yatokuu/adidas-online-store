# Requirements Specification

## Problem statement

Customers need a simple online store where they can find Adidas products, manage a cart, pay for an order, and follow its status. Store administrators need to manage products, stock, prices, and orders. The system must keep its information after the program is closed.

## Project goals

1. Support the complete customer workflow from registration to order tracking.
2. Give administrators basic catalog and order controls.
3. Save data in persistent text files.
4. demonstrate Strategy, Adapter, and Decorator in working Java code.
5. Keep the source small, readable, and suitable for a university defense.

## Target users and roles

### Customer

A customer can register, log in, browse and search products, manage a personal cart, select a discount and payment method, order products, view order history, track or cancel an order, and read notifications.

### Administrator

An administrator can log in, browse the catalog, add products, change prices and stock quantities, view every order, and move orders through valid statuses.

### Guest

A guest can browse the catalog but must register or log in to buy products.

## Functional requirements

| ID | Requirement |
|---|---|
| FR-01 | The system shall register customers and authenticate existing users. |
| FR-02 | The system shall support CUSTOMER and ADMIN roles. |
| FR-03 | The system shall show products with category, description, price, and stock. |
| FR-04 | The system shall search by name or description and filter by category and price. |
| FR-05 | A customer shall add, update, and remove shopping-cart items. |
| FR-06 | Shopping carts shall be stored in a persistent file. |
| FR-07 | The system shall create an order only after a simulated payment succeeds. |
| FR-08 | The system shall support bank card, wallet, and cash-on-delivery payments. |
| FR-09 | The customer shall select a discount strategy at runtime. |
| FR-10 | The customer shall combine optional gift wrap, express delivery, and insurance. |
| FR-11 | The system shall reduce stock after checkout and restore it after valid cancellation. |
| FR-12 | Customers shall view order history and track an order. |
| FR-13 | Administrators shall update orders through CREATED, PAID, PROCESSING, SHIPPED, DELIVERED, or CANCELLED. |
| FR-14 | The system shall create confirmation and status-change notifications. |
| FR-15 | Products, users, carts, orders, and notifications shall remain after restart. |

## Non-functional requirements

- **Usability:** menus and errors use simple language.
- **Maintainability:** presentation, business logic, data access, domain classes, and patterns are in separate packages.
- **Portability:** the project uses standard Java only and requires no external libraries.
- **Reliability:** invalid input, unavailable stock, rejected payment, and illegal status changes do not corrupt stored data.
- **Performance:** linear searches are acceptable for the small educational data set.
- **Security limitation:** payment is simulated and passwords are plain text. This is acceptable only for the assignment demo.

## Validation and error handling

- Empty names are rejected.
- Emails must contain `@` and `.` and must be unique.
- Passwords must contain at least six characters.
- Product price and quantity must be positive; stock cannot be negative.
- Cart quantity cannot exceed available stock.
- An empty cart cannot be checked out.
- Rejected payments leave stock and cart unchanged.
- Customers cannot access another customer's order.
- Shipped, delivered, or already cancelled orders cannot be cancelled by customers.
- Invalid order-status transitions are rejected.
- Non-numeric menu input is requested again instead of stopping the program.
