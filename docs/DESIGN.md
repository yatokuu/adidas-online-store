# Architecture and Design

## Architecture

The application uses four simple layers. `ConsoleUI` displays menus and reads input. Service classes contain store rules. `DataManager` reads and writes files. Model classes represent store information. The pattern packages contain the required interchangeable behavior and wrappers.

See [architecture.mmd](diagrams/architecture.mmd) and [class-diagram.mmd](diagrams/class-diagram.mmd).

## Strategy Pattern

**Problem:** The store has several ways to calculate a discount, and the customer selects one during checkout.

`DiscountStrategy` defines the common method `calculateDiscount(total, itemCount)`. Three concrete strategies implement it:

1. `NoDiscountStrategy` returns zero.
2. `SeasonalDiscountStrategy` returns 10 percent.
3. `BulkDiscountStrategy` returns 8 percent when the cart contains at least three items.

`DiscountCalculator` is the context. It contains a `DiscountStrategy` reference and can switch it with `setStrategy`. `OrderService` uses only the interface, so a new strategy can be added without rewriting the checkout calculation.

Simple defense explanation: **“Strategy changes the algorithm used inside the discount calculator.”**

## Adapter Pattern

**Problem:** Our store expects `PaymentProcessor.processPayment(double dollars)`, but the simulated third-party `ExternalPaymentSystem` provides `makePayment(String cardNumber, int cents)`.

`CardPaymentAdapter` implements our `PaymentProcessor` interface, converts dollars to cents, and calls the external method. The third-party class is not changed. Wallet and cash-on-delivery classes also implement `PaymentProcessor`, giving the store multiple payment methods.

Simple defense explanation: **“Adapter translates our payment interface into the external system's different interface.”**

## Decorator Pattern

**Problem:** Customers can combine optional order services without creating classes for every possible combination.

`OrderPrice` is the component interface. `BasicOrder` is the original component. `OrderDecorator` stores another `OrderPrice`. The three concrete decorators are:

1. `GiftWrapDecorator` adds $5.
2. `ExpressDeliveryDecorator` adds $15.
3. `InsuranceDecorator` adds $8.

Because every decorator implements `OrderPrice`, decorators can wrap each other. For example:

```java
OrderPrice price = new BasicOrder(100);
price = new GiftWrapDecorator(price);
price = new ExpressDeliveryDecorator(price);
```

Simple defense explanation: **“Decorator wraps the same object and adds optional responsibilities one by one.”**

## Important design decisions

- The application uses console menus because the assignment permits a console presentation layer and this keeps the design easy to explain.
- Standard Java text-file classes provide persistence without an external database or library.
- Each service has one main responsibility: users, products, carts, or orders.
- `OrderItem` stores a product name and price snapshot so old orders do not change when the catalog price changes.
- Stock changes only after successful payment and is restored on cancellation.
- Order status transitions are checked to prevent impossible workflows.
