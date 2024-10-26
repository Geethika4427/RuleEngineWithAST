import { useState } from 'react';
import RuleForm from './RuleForm'; 

const CreateRulesPage = () => {
  const [rules, setRules] = useState([]);

  const handleRuleCreated = (newRule) => {
    console.log('New rule created:', newRule);
    setRules((prevRules) => [...prevRules, newRule]);
  };

  return (
    <div>
      <h1>Create New Rule</h1>
      <RuleForm onRuleCreated={handleRuleCreated} />
      <h2>Created Rules</h2>
      <ul>
        {rules.map((rule, index) => (
          <li key={index}>{rule.ruleString}</li> 
        ))}
      </ul>
    </div>
  );
};

export default CreateRulesPage;