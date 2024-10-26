package com.excelr.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.excelr.model.Rule;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {

}
