# Project Diagrams

## Use-case diagram

```mermaid
flowchart LR
    Guest[Guest] --> Browse[Browse and search products]
    Guest --> Register[Register]
    Customer[Customer] --> Browse
    Customer --> Cart[Manage cart]
    Customer --> Checkout[Place order]
    Customer --> History[View and track orders]
    Customer --> Cancel[Cancel eligible order]
    Customer --> Notices[Read notifications]
    Admin[Administrator] --> Catalog[Manage products and stock]
    Admin --> Orders[View orders and update status]
```

## Architecture diagram

```mermaid
flowchart TD
    User[Customer / Administrator] --> UI[Presentation Layer: ConsoleUI]
    UI --> Services[Business Logic: Services]
    Services --> Patterns[Strategy / Adapter / Decorator]
    Services --> DM[Data Access: DataManager]
    Services --> Models[Domain Model]
    DM --> Files[(Persistent text files)]
```

## Entity relationship diagram

```mermaid
erDiagram
    USER ||--o| SHOPPING_CART : owns
    USER ||--o{ ORDER : places
    USER ||--o{ NOTIFICATION : receives
    SHOPPING_CART ||--o{ CART_ITEM : contains
    PRODUCT ||--o{ CART_ITEM : selected_as
    ORDER ||--|{ ORDER_ITEM : contains
    PRODUCT ||--o{ ORDER_ITEM : copied_to
```

## UML class diagram

The complete UML source is in `diagrams/class-diagram.mmd`. The most important pattern relationships are shown below.

```mermaid
classDiagram
    class DiscountStrategy {
        <<interface>>
        calculateDiscount(total, itemCount)
    }
    DiscountStrategy <|.. NoDiscountStrategy
    DiscountStrategy <|.. SeasonalDiscountStrategy
    DiscountStrategy <|.. BulkDiscountStrategy

    class PaymentProcessor {
        <<interface>>
        processPayment(amount)
    }
    PaymentProcessor <|.. CardPaymentAdapter
    CardPaymentAdapter --> ExternalPaymentSystem

    class OrderPrice {
        <<interface>>
        getCost()
        getDescription()
    }
    OrderPrice <|.. BasicOrder
    OrderPrice <|.. OrderDecorator
    OrderDecorator o-- OrderPrice
    OrderDecorator <|-- GiftWrapDecorator
    OrderDecorator <|-- ExpressDeliveryDecorator
    OrderDecorator <|-- InsuranceDecorator
```
