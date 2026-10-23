# Adidas Online Store

This is a simple Java console application for an Adidas-style online store. It was created for a design-patterns assignment and demonstrates the Strategy, Adapter, and Decorator patterns using the same basic Java style as the university exercises.

## Main features

- Customer registration and login
- Administrator and customer roles
- Adidas product catalog with categories, descriptions, prices, and stock
- Product search and filtering by word, category, and maximum price
- Persistent shopping carts
- Add, update, and remove cart items
- Order checkout and persistent order history
- Card, wallet, and cash-on-delivery payments
- Three discount strategies selected at runtime
- Gift wrap, express delivery, and insurance decorators
- Order confirmation and stored notifications
- Order tracking, cancellation, and status management
- Input validation and readable error messages

## Requirements

- Java 17 or newer
- A terminal or IntelliJ IDEA
- No external libraries, Maven, Gradle, or internet connection

## Run from a terminal

Open a terminal in the project folder and run:

```bash
chmod +x build.sh run.sh test.sh
./run.sh
```

The script compiles the source into `build/classes` and starts `Main`.

To compile manually:

```bash
mkdir -p build/classes
javac -d build/classes $(find src -name "*.java" ! -path "src/test/*")
java -cp build/classes Main
```

## Run in IntelliJ IDEA

1. Open this project folder.
2. Mark `src` as the Sources Root if IntelliJ does not detect it.
3. Open `src/Main.java`.
4. Run the `Main.main()` method.
5. Keep the working directory set to the project folder so the `data` folder is found.

## Sample accounts

| Role | Email | Password |
|---|---|---|
| Administrator | `admin@adidas.demo` | `admin123` |
| Customer | `customer@adidas.demo` | `customer123` |

New users can register from the first menu. Passwords are stored as plain text only because this is a small educational simulation. A real store would hash passwords and use a proper database.

## Test special cases

- Enter a card number ending in `0000` to simulate a declined card.
- Enter `blocked` as the wallet name to simulate a rejected wallet payment.
- Try adding more units than the available stock to see validation.
- Try changing an order directly from `PAID` to `SHIPPED` to see status validation.

## Run tests

```bash
./test.sh
```

The project contains 8 functional tests and 3 pattern tests. The current result is **11 passed, 0 failed**.

## Project structure

```text
src/
├── Main.java          application start
├── model/             Product, User, Cart, Order, Notification
├── ui/                console menus (presentation layer)
├── service/           store rules (business logic layer)
├── data/              text-file reading and writing (data layer)
├── strategy/          Strategy Pattern
├── adapter/           Adapter Pattern and payment methods
├── decorator/         Decorator Pattern
└── test/              simple test runner
data/                  persistent product, user, cart, order, notification files
docs/                  requirements, diagrams, design explanation, and tests
```

## Documentation

- [Requirements](docs/REQUIREMENTS.md)
- [Use cases](docs/USE_CASES.md)
- [Architecture and design patterns](docs/DESIGN.md)
- [Data model and persistence](docs/DATA_MODEL.md)
- [Test documentation](docs/TEST_DOCUMENTATION.md)
- [All diagrams in one page](docs/DIAGRAMS.md)

The Mermaid source for the diagrams is in `docs/diagrams`. GitHub and many Markdown editors render Mermaid diagrams automatically.
