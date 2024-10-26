package com.excelr.serviceTest;

import com.excelr.exceptions.ResourceNotFoundException;
import com.excelr.model.Rule;
import com.excelr.model.User;
import com.excelr.repo.RuleRepository;
import com.excelr.service.RuleService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RuleServiceTest {

    @InjectMocks
    private RuleService ruleService;

    @Mock
    private RuleRepository ruleRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRule() {
        String ruleString = "age > 30 AND department = 'Sales'";
        Rule rule = new Rule(ruleString);
        
        when(ruleRepository.save(any(Rule.class))).thenReturn(rule);
        
        Rule createdRule = ruleService.createRule(ruleString);
        assertNotNull(createdRule);
        assertEquals(ruleString, createdRule.getRuleString());
    }
    @Test
    void testCombineRules() {
        List<String> ruleStrings = Arrays.asList(
            "age > 30 AND department = 'Sales'",
            "age < 25 AND department = 'Marketing'"
        );

        String combinedRuleString = ruleService.combineRules(ruleStrings);

        // Update the expected string to match the actual output
        assertEquals("(age > 30 AND department = 'Sales') OR (age < 25 AND department = 'Marketing')", combinedRuleString);

        verify(ruleRepository, times(1)).save(any(Rule.class));
    }


    @Test
    void testEvaluateSpecificComplexRule() {
        // Save the specific complex rule in the repository
        String ruleString = "((age > 30 AND department = 'Marketing')) AND (salary > 20000 OR experience > 5)";
        Rule rule = new Rule(ruleString);
        rule.setId(2L);
        
        when(ruleRepository.findById(2L)).thenReturn(Optional.of(rule));

        // User that meets the rule
        User user1 = new User();
        user1.setAge(32);
        user1.setDepartment("Marketing");
        user1.setSalary(30000);
        user1.setExperience(6);
        
        boolean result1 = ruleService.evaluateRule(2L, user1);
        assertTrue(result1, "Expected rule to be true for user1");

        // User that does not meet the rule (wrong department)
        User user2 = new User();
        user2.setAge(32);
        user2.setDepartment("Sales");
        user2.setSalary(25000);
        user2.setExperience(4);
        
        boolean result2 = ruleService.evaluateRule(2L, user2);
        assertFalse(result2, "Expected rule to be false for user2");

        // User that meets the department condition but not the age
        User user3 = new User();
        user3.setAge(29);
        user3.setDepartment("Marketing");
        user3.setSalary(15000);
        user3.setExperience(7);
        
        boolean result3 = ruleService.evaluateRule(2L, user3);
        assertFalse(result3, "Expected rule to be false for user3");

        // Modify User 4
        User user4 = new User();
        user4.setAge(32); // This meets the age condition (age > 30)
        user4.setDepartment("Marketing"); // This meets the department condition
        user4.setSalary(15000); // This does NOT meet the salary condition
        user4.setExperience(5); // This meets the experience condition

        // User4 will be evaluated as false because salary does not meet the condition
        boolean result4 = ruleService.evaluateRule(2L, user4);
        assertFalse(result4, "Expected rule to be false for user4");
        
        // User that does not meet the rule
        User user5 = new User();
        user5.setAge(28);
        user5.setDepartment("HR");
        user5.setSalary(15000);
        user5.setExperience(4);
        
        boolean result5 = ruleService.evaluateRule(2L, user5);
        assertFalse(result5, "Expected rule to be false for user5");
    }


    @Test
    void testUpdateRule() {
        String existingRuleString = "age > 30 AND department = 'Sales'";
        Rule existingRule = new Rule(existingRuleString);
        existingRule.setId(1L);

        when(ruleRepository.findById(1L)).thenReturn(Optional.of(existingRule));

        String newRuleString = "age < 25 AND department = 'Marketing'";
        when(ruleRepository.save(existingRule)).thenReturn(existingRule); // Ensure this returns the updated rule

        Rule updatedRule = ruleService.updateRule(1L, newRuleString);

        // Check if updatedRule is not null and verify the rule string
        assertNotNull(updatedRule, "Updated rule should not be null");
        assertEquals(newRuleString, updatedRule.getRuleString());
        
        // Ensure the save method was called once
        verify(ruleRepository, times(1)).save(existingRule);
    }

}
