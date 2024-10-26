package com.excelr.service;

import com.excelr.exceptions.ResourceNotFoundException;
import com.excelr.model.Rule;
import com.excelr.model.User;
import com.excelr.repo.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RuleService {

    @Autowired
    private RuleRepository ruleRepository;

    @Transactional
    public Rule createRule(String ruleString) {
        // Remove leading and trailing whitespace and normalize spaces
        ruleString = ruleString.trim().replaceAll("\\s+", " ");
        Rule rule = new Rule(ruleString);
        return ruleRepository.save(rule);
    }

    // Method to fetch all rules
    public List<Rule> getAllRules() {
        return ruleRepository.findAll(); // This assumes you have a Rule entity and RuleRepository
    }
    
    public String combineRules(List<String> ruleStrings) {
        StringBuilder combinedRule = new StringBuilder();
        for (String rule : ruleStrings) {
            if (combinedRule.length() > 0) {
                combinedRule.append(" OR ");
            }
            combinedRule.append("(").append(rule).append(")");
        }

        // Store the combined rule in the database
        String combinedRuleString = combinedRule.toString();
        createRule(combinedRuleString);  // Save the combined rule using the existing createRule method

        return combinedRuleString;
    }
 
    
    public String getRuleById(Long ruleId) {
        Optional<Rule> ruleOpt = ruleRepository.findById(ruleId);
        // Extracting the ruleString from the rule if it exists
        return ruleOpt.map(Rule::getRuleString).orElse(null);
    }

    public boolean evaluateRule(Long ruleId, User user) {
        String ruleString = getRuleById(ruleId);
        if (ruleString == null) {
            throw new IllegalArgumentException("Rule not found for ID: " + ruleId);
        }
        return evaluateRule(ruleString, user);
    }
    
    private boolean evaluateRule(String ruleString, User user) {
        // Example of breaking down the rule string
        String[] andConditions = ruleString.split(" AND "); // Split based on AND

        boolean result = true; // Initialize result

        for (String condition : andConditions) {
            String[] orConditions = condition.split(" OR "); // Split based on OR

            boolean orResult = false; // Initialize for OR conditions
            for (String orCondition : orConditions) {
                orResult = orResult || evaluateSingleCondition(orCondition.trim(), user);
                // Debugging output to check each orCondition result
                System.out.println("Evaluating OR condition: " + orCondition.trim() + " Result: " + orResult);
            }

            // Combine OR results with AND
            result = result && orResult; 
            // Debugging output for the current result after processing this AND group
            System.out.println("Current AND result: " + result);
        }

        return result;
    }
    

    private boolean evaluateCondition(String ruleString, User user) {
        System.out.println("Evaluating rule: " + ruleString);
        ruleString = ruleString.trim().replaceAll("\\s+", " ");
        
        return evaluateExpression(ruleString, user);
    }

    private boolean evaluateExpression(String expression, User user) {
        Stack<Boolean> valueStack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();

        StringBuilder currentToken = new StringBuilder();
        int parenthesisCount = 0; // To track nested parentheses

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            // Count parentheses
            if (ch == '(') {
                parenthesisCount++;
                currentToken.append(ch);
            } else if (ch == ')') {
                parenthesisCount--;
                currentToken.append(ch);
                // Handle complete sub-expression within parentheses
                if (parenthesisCount == 0) {
                    String token = currentToken.toString().trim();
                    if (!token.isEmpty()) {
                        valueStack.push(evaluateSingleCondition(token, user));
                    }
                    currentToken.setLength(0); // Reset current token after evaluating
                }
            } else if (ch == ' ' && parenthesisCount == 0) {
                // Handle operators when not in parentheses
                if (currentToken.length() > 0) {
                    String token = currentToken.toString().trim();
                    if (!token.isEmpty()) {
                        valueStack.push(evaluateSingleCondition(token, user));
                    }
                    currentToken.setLength(0); // Reset current token
                }
            } else if (ch == '&' && (i + 1 < expression.length() && expression.charAt(i + 1) == '&')) {
                // Process the AND operator
                operatorStack.push("AND");
                i++; // Skip next '&'
            } else if (ch == '|' && (i + 1 < expression.length() && expression.charAt(i + 1) == '|')) {
                // Process the OR operator
                operatorStack.push("OR");
                i++; // Skip next '|'
            } else {
                currentToken.append(ch); // Add character to current token
            }
        }

        // Handle any remaining condition
        if (currentToken.length() > 0) {
            String token = currentToken.toString().trim();
            if (!token.isEmpty()) {
                valueStack.push(evaluateSingleCondition(token, user));
            }
        }

        // Now resolve the values with the operators
        while (!operatorStack.isEmpty()) {
            if (valueStack.size() < 2) {
                throw new IllegalArgumentException("Insufficient values for operator: " + operatorStack.peek());
            }

            String operator = operatorStack.pop();
            boolean rightValue = valueStack.pop();
            boolean leftValue = valueStack.pop();

            boolean result = operator.equals("AND") ? (leftValue && rightValue) : (leftValue || rightValue);
            valueStack.push(result);
        }

        if (valueStack.size() != 1) {
            throw new IllegalArgumentException("Invalid rule format or mismatched conditions.");
        }

        return valueStack.pop();
    }
   
    private boolean evaluateTokens(List<String> tokens, User user) {
        Stack<Boolean> valueStack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();

        for (String token : tokens) {
            if (token.equals("AND") || token.equals("OR")) {
                operatorStack.push(token);
            } else {
                boolean conditionResult = evaluateSingleCondition(token, user);
                valueStack.push(conditionResult);

                // Process any pending operations
                while (!operatorStack.isEmpty() && valueStack.size() >= 2) {
                    String operator = operatorStack.pop();
                    boolean rightValue = valueStack.pop();
                    boolean leftValue = valueStack.pop();

                    boolean result = operator.equals("AND") ? (leftValue && rightValue) : (leftValue || rightValue);
                    valueStack.push(result);
                }
            }
        }

        // Final result should be the last value in the stack
        if (valueStack.size() != 1) {
            throw new IllegalArgumentException("Invalid rule format or mismatched conditions.");
        }

        return valueStack.pop();
    }

    private int findClosingParenthesis(String expression, int openingIndex) {
        int balance = 1; // We found one opening parenthesis
        for (int i = openingIndex + 1; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                balance++;
            } else if (expression.charAt(i) == ')') {
                balance--;
            }

            if (balance == 0) {
                return i; // Found the matching closing parenthesis
            }
        }
        return -1; // No matching closing parenthesis found
    }    

    private boolean evaluateSingleCondition(String condition, User user) {
        condition = condition.trim();

        // Log the condition being evaluated
        System.out.println("Evaluating condition: " + condition);

        // Remove outer parentheses for easier processing
        while (condition.startsWith("(") && condition.endsWith(")")) {
            condition = condition.substring(1, condition.length() - 1).trim();
        }

        // Use regex to parse condition
        String regex = "(\\w+)\\s*(<=|>=|<|>|=)\\s*(\\d+|\"[^\"]+\"|'[^']+')"; 
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(condition);

        // Check if the condition matches the expected format
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid condition format: " + condition);
        }

        String attribute = matcher.group(1);
        String operator = matcher.group(2);
        String value = matcher.group(3).replace("\"", "").replace("'", "");

        // Evaluate based on attribute
        switch (attribute) {
            case "age":
                return evaluateNumericCondition(user.getAge(), Integer.parseInt(value), operator);
            case "salary":
                return evaluateNumericCondition(user.getSalary(), Integer.parseInt(value), operator);
            case "experience":
                return evaluateNumericCondition(user.getExperience(), Integer.parseInt(value), operator);
            case "department":
                return evaluateStringCondition(user.getDepartment(), value, operator);
            default:
                throw new IllegalArgumentException("Invalid attribute: " + attribute);
        }
    }
   
 
    private boolean evaluateNumericCondition(int attributeValue, String operator, int comparisonValue) {
        switch (operator) {
            case ">":
                return attributeValue > comparisonValue;
            case "<":
                return attributeValue < comparisonValue;
            case "=":
                return attributeValue == comparisonValue;
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }

    
    private boolean evaluateNumericCondition(int userValue, int conditionValue, String operator) {
        switch (operator) {
            case ">":
                return userValue > conditionValue;
            case "<":
                return userValue < conditionValue;
            case ">=":
                return userValue >= conditionValue;
            case "<=":
                return userValue <= conditionValue;
            case "=":
                return userValue == conditionValue; // If you want to support equality checks
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

    private boolean evaluateStringCondition(String userValue, String ruleValue, String operator) {
        switch (operator) {
            case "=":
                return userValue.equals(ruleValue);
            default:
                throw new IllegalArgumentException("Invalid operator for string comparison: " + operator);
        }
    }

    private void validateParentheses(String ruleString) {
        int openParentheses = 0;
        for (char ch : ruleString.toCharArray()) {
            if (ch == '(') {
                openParentheses++;
            } else if (ch == ')') {
                openParentheses--;
            }

            if (openParentheses < 0) {
                throw new IllegalArgumentException("Mismatched parentheses in the rule.");
            }
        }

        if (openParentheses != 0) {
            throw new IllegalArgumentException("Mismatched parentheses in the rule.");
        }
    }

    //to update operators,operands, values, or adding/removing sub-expressions 
    public Rule updateRule(Long ruleId, String newRuleString) {
        Optional<Rule> existingRuleOpt = ruleRepository.findById(ruleId);
        if (existingRuleOpt.isPresent()) {
            Rule existingRule = existingRuleOpt.get();

            // Directly use the new rule string
            String modifiedRuleString = modifyRuleString(existingRule.getRuleString(), newRuleString);

            existingRule.updateRuleString(modifiedRuleString);
            return ruleRepository.save(existingRule);
        } else {
            throw new ResourceNotFoundException("Rule not found with ID: " + ruleId);
        }
    }
    private String modifyRuleString(String existingRule, String newRule) {
        // Simply return the new rule string as it is
        return newRule;
    }
    
    // Delete existing rule
    public void deleteRule(Long id) {
        ruleRepository.deleteById(id);
    }
    
}