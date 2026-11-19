# Banking System (CSE202 OOAD with Java)

Simple Java project: JavaFX screens + SQLite database + JDBC DAO classes.

## How to run

You need Java 21 and Maven (Maven downloads JavaFX and SQLite for you).

```
mvn javafx:run
```

Log in with any sample customer. The password for all of them is `1234`.

| Username | Name | Accounts |
|---|---|---|
| jay | Jay Molefe | Savings, Investment |
| kabelo | Kabelo Mokoena | Savings, Cheque |
| neo | Neo Modise | Savings, Investment |
| tshepo | Tshepo Kgosi | Savings, Cheque |
| lesedi | Lesedi Sebego | Savings, Investment |
| thato | Thato Ramotswe | Savings, Cheque |
| boitumelo | Boitumelo Seretse | Savings, Cheque |
| masego | Masego Tau | Savings, Cheque |
| onkabetse | Onkabetse Phiri | Savings, Investment |
| keitumetse | Keitumetse Moeng | Savings, Cheque |

The 10 sample customers are inserted automatically the first time the app runs.
The database file `bank.db` is created next to the project. Delete it to start fresh.

## Run the integration test

Open `src/test/java/bank/IntegrationTest.java` in VS Code and click **Run**.
(Or: `mvn test-compile exec:java -Dexec.mainClass=bank.IntegrationTest -Dexec.classpathScope=test`)

## Project structure (read the code in this order)

```
model/       1. Transactable, Payable, Transaction, Account, SavingsAccount,
                InvestmentAccount, ChequeAccount, Customer     <- business rules
resources/   2. schema.sql                                     <- the 3 tables
dao/         3. DatabaseConnection, TransactionDAO, AccountDAO, CustomerDAO,
                SampleData                                     <- JDBC code
view/        4. LoginView, RegisterView, DashboardView,
                OpenAccountView, Alerts                        <- GUI only, no logic
controller/  5. LoginController, RegisterController,
                AccountController                              <- connects it all
MainApp.java 6. starts everything
```

## How the layers talk

```
View  <-->  Controller  <-->  Model
                |
               DAO  <-->  SQLite database
```

- A **view** never touches the model or the database.
- A **controller** validates input, changes the model, then asks a DAO to save it.
- The **model** has no JavaFX and no SQL.
- A **DAO** is the only place with SQL.

## OOP principles (for your presentation)

| Principle | Where |
|---|---|
| Abstraction | `Account` is abstract |
| Inheritance | `SavingsAccount`, `InvestmentAccount`, `ChequeAccount` extend `Account` |
| Interfaces | `Transactable` (implemented by `Account`), `Payable` (Savings and Investment) |
| Polymorphism | `AccountController.handleInterest()` calls `addInterest()` on any `Payable` |
| Overriding | `withdraw()` in `SavingsAccount`, `getAvailableFunds()` in `ChequeAccount` |
| Overloading | `deposit(amount)` and `deposit(amount, type)`, and the `ChequeAccount` constructors |
| Encapsulation | private fields, getters return copies of lists |

## Business rules

- Savings: 0.05% monthly interest, no withdrawals.
- Investment: 5% monthly interest, opening deposit of at least BWP500, withdrawals allowed.
- Cheque: employer name and address required, deposits and withdrawals allowed.
- One customer can hold many accounts.

## Known limits (good to mention if asked)

- Passwords are hashed with plain SHA-256. A real bank would add a salt (e.g. BCrypt).
- The GUI has no screen for editing or deleting customers. The DAOs support it (see the integration test).
- Transactions are a ledger, so `TransactionDAO` only has create and read.
