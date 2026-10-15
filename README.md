# Object Oriented Programming (CEUE203) - Semester 3 Lab Works

This repository contains the complete practical solutions for **Object Oriented Programming (CEUE203)** covering all 14 Practicals (both **Part A** practice programs and **Part B** MiniBank project milestones).

---

## 📁 Repository Structure

```
OOP-Sem-3/
├── Practical-1/              # Control structures, switch expressions, enums
│   ├── RPSLS.java
│   ├── TollBooth.java
│   └── VendingMachine.java
├── Practical-2/              # Classes, encapsulation, static members
│   ├── CinemaShow.java
│   ├── ParkingLot.java
│   └── Thermostat.java
├── Practical-3/              # Object identity, constructors, equals & hashCode
│   ├── Card.java
│   ├── Fraction.java
│   ├── Point.java
│   └── Driver*.java
├── Practical-4/              # Strings, Regex & Pattern Matching
│   ├── chat-log-filter/      # ChatFilter.java
│   ├── password-strength/    # PasswordChecker.java
│   └── template-filler/      # TemplateFiller.java
├── Practical-5/              # Inheritance & Polymorphism
│   ├── Area.java             # Shapes (Circle, Rectangle, Triangle)
│   ├── Salary.java           # Employee hierarchy (Fulltime, Parttime, Intern)
│   └── Main.java
├── Practical-6/              # Interfaces & Lambda Expressions
│   ├── discount-engine/      # DiscountEngine.java
│   ├── notifications/        # Notifications.java
│   └── remote-control/       # RemoteControl.java
├── Practical-7/              # Annotations & Reflection
│   ├── column-mapper/        # ColumnMapper.java
│   ├── form-validator/       # FormValidator.java
│   └── mini-test-runner/     # MiniTestRunner.java
├── Practical-8/              # Exception Handling & Defensive Programming
│   ├── auto-closeable/       # AutoCloseableDemo.java
│   ├── guarded-calculator/   # GuardedCalculator.java
│   ├── warehouse/            # StockIssue.java
│   └── Practical8Demo.java
├── Practical-9/              # Multithreading, Race Conditions & Synchronization
│   ├── counter-race/         # CounterRace.java
│   ├── seat-booking/         # SeatBooking.java
│   ├── array-sum/            # ArraySum.java
│   └── Practical9Demo.java
├── Practical-10/             # Thread Pools, Producer-Consumer & Deadlock
│   ├── thread-pool-runner/   # ThreadPoolRunner.java
│   ├── producer-consumer/    # ProducerConsumer.java
│   ├── deadlock-demo/        # DeadlockDemo.java
│   └── Practical10Demo.java
├── Practical-11/             # Object Serialization & NIO File System
│   ├── save-load/            # SaveLoad.java
│   ├── log-analyzer/         # LogAnalyzer.java
│   ├── tree-walker/          # TreeWalker.java
│   └── Practical11Demo.java
├── Practical-12/             # Java Collections Framework & Comparators
│   ├── word-frequency/       # WordFrequency.java
│   ├── unique-visitors/      # UniqueVisitors.java
│   ├── leaderboard/          # Leaderboard.java
│   └── Practical12Demo.java
├── Practical-13/             # Generics, Type Erasure & Bounded Types
│   ├── generic-containers/   # GenericContainers.java
│   ├── bounded-stats/        # BoundedStats.java
│   ├── generic-stack/        # GenericStack.java
│   └── Practical13Demo.java
├── Practical-14/             # Layered Architecture, Automated Testing & Packaging
│   ├── tiny-layered-app/     # TinyLayeredApp.java
│   ├── packaged-app/         # PackagedApp.java
│   └── Practical14Demo.java
└── Project/
    └── MiniBank/             # Part B: MiniBank Capstone Enterprise Application
        ├── Account.java              # Abstract Account base entity
        ├── SavingsAccount.java       # Savings account with interest & min balance
        ├── CurrentAccount.java       # Current account with overdraft limit
        ├── FixedDepositAccount.java  # Fixed Deposit account
        ├── Customer.java             # Customer & Address entity
        ├── BankingInterfaces.java    # Transactable, InterestBearing, Premium
        ├── ValidationAnnotations.java# @Id, @Positive, @MaxLength
        ├── AnnotationValidator.java  # Reflection-based validator
        ├── BankException.java        # Custom banking exceptions
        ├── CommandParser.java        # Command parsing logic
        ├── StatementFormatter.java   # Account statement formatter
        ├── AccountWorker.java        # Practical 9: Concurrent multithreaded worker
        ├── TransactionProcessor.java # Practical 10: Thread pool & deadlock-free transfer
        ├── StatePersister.java       # Practical 11: Object serialization save/load
        ├── TransactionLog.java       # Practical 11: NIO append-only log writer
        ├── ReportGenerator.java      # Practical 11: NIO directory stream report builder
        ├── AccountStore.java         # Practical 12: Map/List store & comparator sorting
        ├── Repository.java           # Practical 13: Generic entity repository with reflection
        ├── BankService.java          # Practical 14: Core business logic & audit layer
        ├── MiniBankApp.java          # Practical 14: Interactive UI application
        ├── MiniBankTests.java        # Practical 14: Automated unit test suite
        └── minibank.jar              # Runnable executable JAR artifact
```

---

## 🚀 How to Run

### 1. Running Any Part A Practical Demo
Each practical folder contains individual problem files as well as a unified demo runner (`Practical<N>Demo.java`):

```bash
# Example: Practical 9 (Multithreading & Synchronization)
cd Practical-9
javac Practical9Demo.java */*.java
java Practical9Demo

# Example: Practical 10 (Thread Pools & Coordination)
cd Practical-10
javac Practical10Demo.java */*.java
java Practical10Demo

# Example: Practical 11 (Serialization & NIO)
cd Practical-11
javac Practical11Demo.java */*.java
java Practical11Demo

# Example: Practical 12 (Collections & Sorting)
cd Practical-12
javac Practical12Demo.java */*.java
java Practical12Demo

# Example: Practical 13 (Generics & Bounded Types)
cd Practical-13
javac Practical13Demo.java */*.java
java Practical13Demo

# Example: Practical 14 (Layered App & Tests)
cd Practical-14
javac Practical14Demo.java */*.java
java Practical14Demo
```

---

### 2. Running Part B: MiniBank Enterprise Application

You can launch the complete, interactive banking system directly:

```bash
cd Project/MiniBank
java -jar minibank.jar
```

Or run via `java` source compilation:
```bash
cd Project/MiniBank
javac *.java
java MiniBankApp
```

### 3. Running Automated Unit Tests
To run the automated test suite verifying business rules, limits, concurrent thread safety, and persistence:

```bash
cd Project/MiniBank
javac *.java
java MiniBankTests
```
