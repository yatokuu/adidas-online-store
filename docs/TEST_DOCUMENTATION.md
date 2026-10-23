# Test Documentation

## How tests are run

Run `./test.sh` from the project folder. The test class uses a small custom assertion method, so no JUnit or external dependency is needed. Every functional test gets its own files inside `build/test-data-*`; the real `data` folder is not changed.

## Functional test cases

| ID | Test | Expected result | Result |
|---|---|---|---|
| FT-01 | Register customer, then repeat the same email | First registration succeeds; duplicate is rejected | PASS |
| FT-02 | Search `samba`, category Shoes, maximum $150 | Only the matching shoe is returned | PASS |
| FT-03 | Add two items and update quantity to three | Cart contains quantity 3 and correct subtotal | PASS |
| FT-04 | Add more units than available stock | Operation is rejected | PASS |
| FT-05 | Checkout two products | Stock decreases, cart clears, order and notification are saved | PASS |
| FT-06 | Pay with a rejected test card | Cart and stock stay unchanged | PASS |
| FT-07 | Cancel an eligible order | Status becomes CANCELLED and stock is restored | PASS |
| FT-08 | Reload data after checkout | Order and changed stock are read from persistent files | PASS |

## Pattern-specific tests

| ID | Test | Expected result | Result |
|---|---|---|---|
| PT-01 | Switch `DiscountCalculator` between all three strategies | Discounts are $0, $10, and $8 for the example | PASS |
| PT-02 | Pay $12.34 through `CardPaymentAdapter` | External system receives 1234 cents | PASS |
| PT-03 | Wrap a $100 order with all three decorators | Total is $128 and description lists all extras | PASS |

## Invalid-input examples

| Input or action | Handling |
|---|---|
| Letters in a numeric menu field | Menu asks again for a number |
| Empty name | Registration is rejected |
| Email without `@` or `.` | Registration is rejected |
| Password shorter than six characters | Registration is rejected |
| Negative or zero price | Product change is rejected |
| Quantity greater than stock | Cart change is rejected |
| Empty-cart checkout | Checkout is rejected |
| Card ending in `0000` | Payment fails; cart and stock remain unchanged |
| Illegal status transition | Admin receives a clear error |
| Customer requests another user's order | “Order not found” is shown |

## Test result

The verified result is:

```text
TEST RESULT: 11 passed, 0 failed
```

The project was also compiled and the real `Main` class was launched. The guest catalog displayed all eight seeded products successfully.
