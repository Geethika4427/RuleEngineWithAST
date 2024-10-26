package com.excelr.model;

public class ASTNode {
    private String type; // "operator" or "operand"
    private ASTNode left; // Left child
    private ASTNode right; // Right child (only for operators)
    private String value; // Optional value for operand nodes

    // Constructor for operator nodes
    public ASTNode(String type, ASTNode left, ASTNode right) {
        this.type = type;
        this.left = left;
        this.right = right;
    }

    // Constructor for operand nodes
    public ASTNode(String type, String value) {
        this.type = type;
        this.value = value;
    }

    // Getters and Setters
    public String getType() { return type; }
    public ASTNode getLeft() { return left; }
    public ASTNode getRight() { return right; }
    public String getValue() { return value; }
}

