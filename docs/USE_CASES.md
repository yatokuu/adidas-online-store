# Use Cases

## Use-case diagram

See [use-case.mmd](diagrams/use-case.mmd).

## UC-01 Register

- **Actor:** Guest
- **Precondition:** Email is not registered.
- **Main flow:** Enter name, email, and password; system validates and saves a CUSTOMER account.
- **Alternative:** Invalid or duplicate data produces an error.

## UC-02 Browse and search catalog

- **Actor:** Guest, Customer, or Administrator
- **Main flow:** View all products or enter a word, category, and maximum price.
- **Result:** Matching products show description, price, and stock.

## UC-03 Manage shopping cart

- **Actor:** Customer
- **Precondition:** Customer is logged in.
- **Main flow:** Add a product, change its quantity, remove it, or view the subtotal.
- **Alternative:** Invalid quantity or unavailable stock produces an error.
- **Result:** The cart is saved in `data/carts.txt`.

## UC-04 Place order

- **Actor:** Customer
- **Precondition:** The cart contains available products.
- **Main flow:** Select a discount, payment method, and optional services; system processes payment, creates the order, reduces stock, clears the cart, and sends confirmation.
- **Alternative:** A failed payment keeps the cart and stock unchanged.

## UC-05 View and track purchases

- **Actor:** Customer
- **Main flow:** View order history or enter an order ID to see items, price, payment, and status.

## UC-06 Cancel order

- **Actor:** Customer
- **Precondition:** The customer's order is not shipped, delivered, or cancelled.
- **Main flow:** System changes the status to CANCELLED, restores stock, saves data, and creates a notification.

## UC-07 Manage product catalog

- **Actor:** Administrator
- **Main flow:** Add a product or update a product's stock or price.
- **Alternative:** Negative stock or non-positive price is rejected.

## UC-08 Manage order status

- **Actor:** Administrator
- **Main flow:** View all orders and move an order to the next valid status.
- **Alternative:** An invalid status transition is rejected.
