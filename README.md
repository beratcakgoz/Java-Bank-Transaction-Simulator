# Java Banking System Simulator

## Overview
This is an object-oriented Java console application that simulates a secure banking environment. It demonstrates core OOP principles such as abstraction, inheritance, and polymorphism through different types of bank accounts and commission models[cite: 7].

## Features
* **Object-Oriented Design:** Implements abstract base classes (`BankAccount`, `Bank`) and derived subclasses (`CheckingAccount`, `FXAccount`, `CommissionBank`, `MinCommissionBank`)[cite: 7].
* **Robust Error Handling & Rollbacks:** Uses a custom `BankException` class to manage transaction errors[cite: 7]. If a transfer fails during the deposit phase, the system automatically triggers a rollback to refund the sender, ensuring no money is lost[cite: 7].
* **Polymorphic Transactions:** Withdrawals, deposits, and transfers are handled dynamically depending on the account's specific rules (e.g., overdraft limits for Checking accounts, exchange rates for FX accounts)[cite: 7].
* **Automated Simulation:** The `Main` class generates 20 random accounts and 3 different banks, then simulates 100 random money transfers to test the system's reliability and commission logic[cite: 7].
