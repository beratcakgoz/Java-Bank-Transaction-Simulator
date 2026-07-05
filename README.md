# Java Banking System Simulator

## Overview
This is an object-oriented Java console application that simulates a secure banking environment. It demonstrates core OOP principles such as abstraction, inheritance, and polymorphism through different types of bank accounts and commission models.

## Features
* **Object-Oriented Design:** Implements abstract base classes (`BankAccount`, `Bank`) and derived subclasses (`CheckingAccount`, `FXAccount`, `CommissionBank`, `MinCommissionBank`).
* **Robust Error Handling & Rollbacks:** Uses a custom `BankException` class to manage transaction errors. If a transfer fails during the deposit phase, the system automatically triggers a rollback to refund the sender, ensuring no money is lost.
* **Polymorphic Transactions:** Withdrawals, deposits, and transfers are handled dynamically depending on the account's specific rules (e.g., overdraft limits for Checking accounts, exchange rates for FX accounts).
* **Automated Simulation:** The `Main` class generates 20 random accounts and 3 different banks, then simulates 100 random money transfers to test the system's reliability and commission logic.
