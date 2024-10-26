# Rule Engine with Abstract Syntax Tree (AST)
-------------------------------------------------------

# Project Title: Rule Engine with AST

## Overview
The Rule Engine Application determines user eligibility based on customizable rules that involve user attributes like age, department, income, spend, etc. 
The system utilizes an Abstract Syntax Tree (AST) to represent complex conditional rules, allowing for dynamic creation, combination, modification, and evaluation 
of these rules against user data. The application is built using Java with Spring Boot for the backend, MySQL for data storage, and React for the frontend interface.

# Features
Create Rules: Define rules using logical conditions on user attributes. Example: Creating a rule that checks if a user is older than 30 and works in marketing.

Combine Rules: Combine multiple rules into a single AST to streamline evaluations. For instance, combining eligibility criteria for different departments into one logical statement.

Evaluate Rules: Determine if a user meets the conditions specified by a rule. For example, evaluate if a user with age 32 in Marketing meets the defined criteria.

CRUD Operations: Modify, delete, or retrieve stored rules.

Error Handling: The system handles invalid rule strings and missing data attributes, providing informative error messages.

# Technologies Used
Backend: Spring Boot (Java) for API and business logic.
Database: MySQL for storing rules, users.
Frontend: React.js for the user interface.

# Architecture
  ## Diagram

        ┌───────────────────────────┐
        │        Frontend           │
        │        (React)            │
        └────────────┬──────────────┘
                     │
           ┌─────────┴─────────┐
           │       API         │
           │  (Spring Boot)    │
           └─────────┬─────────┘
                     │
           ┌─────────┴─────────┐
           │    Database       │
           │     (MySQL)       │
           └────────────────────┘
1)Frontend (React): User interacts with the interface to create, combine, and evaluate rules.
2)API (Spring Boot): Manages requests, processes rules, and communicates with the database.
3)Database (MySQL): Stores user data, rules, and metadata.

# Prerequisites
Java 17 or later
Node.js (for React frontend)
MySQL database instance
Maven for dependency management
Docker (for database container or if containerizing the whole application)

# Build and Run Instructions
1)Clone the Repository
     git remote add origin https://github.com/Geethika4427/RuleEngineWithAST.git
     cd RuleEngineWithAST

2)Backend Setup
    =>Navigate to the backend directory.
    =>Update MySQL credentials in application.properties:
server.port=9090
spring.datasource.url=jdbc:mysql://localhost:3306/AST?useSSL=false&serverTimezone=UTC
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=Ganesha@31
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
           
    =>Build and run the backend:
      Run as mvn install 
      Run as spring boot app

3)Database Setup
    ->create database databaseName;
    ->use databaseName;
    ->show tables;
    ->desc tableName;
    ->select * from tableName;
    
4)Frontend Setup
   ->create Frontend directory
       >> npm create vite@latest
       >> projectName
       >> cd ProjectName
       >> npm install axios react-router-dom@6
       >> npm run dev

5)Access Application
  Backend: http://localhost:8080
  Frontend: http://localhost:3000

# API Usage
  ## Endpoints
  For this use Postman which is used to test the endpoints
  
1)Create Rule
  POST http://localhost:9090/api/rules/create
JSON Body 
----------

{
  "ruleString": "((age > 30 AND department = 'Marketing')) AND (salary > 20000 OR experience > 5)"
}

 Response
 -----------
 {
  "id": 1,
  "ruleString": "((age > 30 AND department = 'Marketing')) AND (salary > 20000 OR experience > 5)",
  "createdAt": "2024-10-19T14:34:32.123"
 }

 2)Combine Rule 
   POST http://localhost:9090/api/rules/combine 
 JSON Body 
----------  

[
    {"ruleString": "age > 30 AND department = 'Sales'"},
    {"ruleString": "salary < 50000 OR experience > 5"},
    {"ruleString": "age < 25 AND department = 'Marketing'"}
]

Response
---------
(age > 30 AND department = 'Sales' OR salary < 50000 OR experience > 5 OR age < 25 AND department = 'Marketing')

3)Evaluate Rule
POST http://localhost:9090/api/rules/evaluate/{ruleId}
JSON Body 
----------  

{
  "age": 35,
  "department": "Marketing",
  "salary": 40000,
  "experience": 8
}

Response
---------
true

4)Update Rule 
PUT http://localhost:9090/api/rules/{ruleId}

5)Delete Rule
DELEte http://localhost:9090/api/rules/delete/{ruleId}

# Design Choices

Technology Stack
-------------------
Spring Boot: For its robustness, ease of use in building RESTful services, and compatibility with MySQL.
MySQL: To store rules and user data with high reliability and performance.
React: For dynamic frontend interfaces that offer a smooth user experience.

# Abstract Syntax Tree (AST) Design

Node Structure:
--------------------
type: Defines if a node is an operator or operand.
left / right: Links to left and right nodes for expressions.
value: Represents conditions in operand nodes.

# Sample Test Cases

Create and Verify Rule AST
-------------------------------------
Input rule: "((age > 30 AND department = 'Sales') OR (age < 25 AND department = 'Marketing')) AND (salary > 50000 OR experience > 5)"
Expected: AST structure representing the logical conditions.

Combine and Verify Rules
--------------------------------
Combine rule1 and rule2.
Expected: Single AST representing the logical combination.

Evaluate Rule
------------------------
Rule: "age > 30 AND department = 'Sales'"
User Data: {"age": 32, "department": "Sales", "salary": 60000}
Expected: True (user meets criteria).

# Error Handling

Invalid Rules:- Application validates rule strings and throws appropriate errors for missing operators or invalid syntax.
User Data Validation:- Checks that required attributes exist in user data.
Rule Modification:- Allows modifying AST nodes, updating operators, operands, or even adding/removing nodes.







      
     



