# Data Model and Persistence

## Main entities and relationships

See [er-diagram.mmd](diagrams/er-diagram.mmd).

- A `User` has a CUSTOMER or ADMIN role.
- A customer has one `ShoppingCart`.
- A cart has many `CartItem` objects.
- Each cart item refers to one `Product`.
- A customer can have many `Order` objects.
- An order contains many `OrderItem` snapshots.
- A customer can receive many `Notification` objects.

## Persistent files

| File | Stored information |
|---|---|
| `data/products.txt` | ID, name, category, description, price, stock |
| `data/users.txt` | ID, name, email, password, role |
| `data/carts.txt` | Customer ID and product/quantity pairs |
| `data/orders.txt` | Customer, items, totals, discount, extras, payment, status, date |
| `data/notifications.txt` | Customer, message, date |

The pipe character `|` separates fields. Semicolons separate multiple items inside a cart or order. These files also serve as the initialization data and storage schema. `products.txt` contains eight Adidas example products, and `users.txt` contains one sample admin and customer.

## Storage and retrieval

At startup, `Main` creates `DataManager`. `DataManager.loadData()` reads every file into one `StoreData` object containing `ArrayList` collections. Services work with those collections. After a change, the service calls `DataManager.saveData()`, which rewrites the files with the current data.

This is persistent storage because products, users, carts, orders, and notifications can be loaded again after the Java program exits. It is intentionally simpler than JDBC so the project needs no driver or database installation.
