# Integration Test Report

**Module:** CSE202 Object Oriented Analysis and Design with Java
**System:** Banking System (JavaFX + Controllers + Model + JDBC/SQLite)

## 1. Purpose

To check that the Model classes, the DAO (JDBC) classes and the SQLite database work together,
and that data saved in one session is correctly read back in the next.

## 2. How it was tested

- **Automated test:** `src/test/java/bank/IntegrationTest.java` uses a separate database file
  (`test-bank.db`) so real data is not touched. It walks through a full customer journey:
  register, log in, open accounts, deposit, withdraw, pay interest, reload from the database,
  update and delete.
- **Manual GUI test:** the checklist in section 4 is done by hand through the JavaFX screens.

## 3. Automated test results

| No. | Test | Expected | Result |
|---|---|---|---|
| 1 | Register a customer and log in | Customer found | PASS |
| 2 | Login with a wrong password | No customer returned | PASS |
| 3 | One customer opens Savings, Investment and Cheque | 3 accounts stored and reloaded | PASS |
| 4 | Deposit BWP2000 into Savings | Balance 2000.00 after reload | PASS |
| 5 | Investment: open with 1000, deposit 500, withdraw 300 | Balance 1200.00 after reload | PASS |
| 6 | Cheque: deposit 3000, withdraw 500 | Balance 2500.00 after reload | PASS |
| 7 | Withdraw from Savings | Blocked with an error | PASS |
| 8 | Open Investment with BWP200 | Rejected (minimum BWP500) | PASS |
| 9 | Open Cheque with no employer | Rejected | PASS |
| 10 | Monthly interest | Investment 1260.00 (5%), Savings 2001.00 (0.05%) | PASS |
| 11 | Transaction history | 4 records saved for the Investment account | PASS |
| 12 | Update customer address | New address saved | PASS |
| 13 | Delete an account | Account and its transactions removed | PASS |
| 14 | Delete a customer | Customer removed | PASS |
| 15 | Sample data | All 10 sample customers can log in | PASS |

**Summary: 15 tests run, 15 passed, 0 failed.**

## 4. Manual GUI test checklist (complete this yourself and add screenshots)

| No. | Steps | Expected | Result | Screenshot |
|---|---|---|---|---|
| G1 | Start the app, log in as `jay` / `1234` | Dashboard shows Jay's accounts | | |
| G2 | Log in with a wrong password | Error pop-up | | |
| G3 | Register a new customer, then log in | New customer reaches an empty dashboard | | |
| G4 | Open a Savings account | Account appears with balance 0.00 | | |
| G5 | Open an Investment account with 300 | Error: minimum BWP500 | | |
| G6 | Open an Investment account with 500 | Account appears with balance 500.00 | | |
| G7 | Open a Cheque account with no company | Error pop-up | | |
| G8 | Select an account, deposit 100 | Balance goes up, history shows DEPOSIT | | |
| G9 | Withdraw from a Savings account | Error: not allowed | | |
| G10 | Withdraw more than the balance | Error: insufficient funds | | |
| G11 | Click "Apply monthly interest" | Interest rows appear in the history | | |
| G12 | Log out and log in again | Balances and history are still there | | |
| G13 | Close the app, start it again, log in | Data is still there (database works) | | |

## 5. Conclusion

The model, DAO and database layers work together correctly. The automated tests passed, and the
data survives between sessions. The GUI and controller layers are checked by the manual checklist above.
