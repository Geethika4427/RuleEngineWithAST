package com.excelr.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "rules")
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rule_string", columnDefinition = "TEXT")
    private String ruleString;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Add a method to update the rule string
    public void updateRuleString(String newRuleString) {
        this.ruleString = newRuleString;
    }
    
    // No-argument constructor
    public Rule() {
    }

    // Constructor to accept a ruleString
    public Rule(String ruleString) {
        this.ruleString = ruleString;
        this.createdAt = LocalDateTime.now();
    }
   
    // Getters and Setters
    public Long getId() { return id; }
    public String getRuleString() { return ruleString; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    // Setter for ID (mainly for testing)
    public void setId(Long id) {
        this.id = id;
    }
    
}



