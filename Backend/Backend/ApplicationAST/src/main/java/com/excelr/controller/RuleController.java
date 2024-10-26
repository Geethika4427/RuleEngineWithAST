package com.excelr.controller;

import com.excelr.model.Rule;
import com.excelr.model.RuleRequest;
import com.excelr.model.User;
import com.excelr.service.RuleService;
import com.excelr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
public class RuleController {

    @Autowired
    private RuleService ruleService;

    @Autowired
    private UserService userService;

   
    @PostMapping("/create")
    public ResponseEntity<Rule> createRule(@RequestBody RuleRequest ruleRequest) {
        String ruleString = ruleRequest.getRuleString().trim().replaceAll("\\s+", " ");
        Rule createdRule = ruleService.createRule(ruleString);
        return ResponseEntity.ok(createdRule);
    }

    @GetMapping
    public List<Rule> getAllRules() {
        return ruleService.getAllRules();
    }
    
    // Endpoint to combine multiple rules
    @PostMapping("/combine")
    public ResponseEntity<String> combineRules(@RequestBody List<String> ruleStrings) {
        String combinedRule = ruleService.combineRules(ruleStrings);
        return ResponseEntity.ok(combinedRule);
    }

    // Endpoint to evaluate a rule against a user
    @PostMapping("/evaluate/{ruleId}")
    public ResponseEntity<Boolean> evaluateRule(@PathVariable Long ruleId, @RequestBody User user) {
        boolean result = ruleService.evaluateRule(ruleId, user);
        return ResponseEntity.ok(result);
    }

    // Endpoint to create a new user
    @PostMapping("/user/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }
    

    @PutMapping("/{ruleId}")
    public ResponseEntity<Rule> updateRule(@PathVariable Long ruleId, @RequestBody Rule updatedRule) {
        // Call the service method that combines updates
        Rule savedRule = ruleService.updateRule(ruleId, updatedRule.getRuleString());
        return ResponseEntity.ok(savedRule);
    }

    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRule(@PathVariable Long id) {
        ruleService.deleteRule(id);
        return ResponseEntity.ok("Rule with ID " + id + " has been successfully deleted.");
    }
}
