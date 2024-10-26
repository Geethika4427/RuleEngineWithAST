import { useState } from 'react';
import axios from 'axios';
import './CombineRules.css'; 
import { useNavigate } from 'react-router-dom';

const CombineRules = () => {
  const [ruleStrings, setRuleStrings] = useState(['']);
  const [combinedRule, setCombinedRule] = useState('');
  const navigate = useNavigate();

  const handleAddRule = () => {
    setRuleStrings([...ruleStrings, '']);
  };

  const handleRuleChange = (index, value) => {
    const newRuleStrings = [...ruleStrings];
    newRuleStrings[index] = value;
    setRuleStrings(newRuleStrings);
  };

  const handleCombine = async () => {
    try {
      const response = await axios.post('http://localhost:9090/api/rules/combine', ruleStrings);
      setCombinedRule(response.data);
      alert('Rules combined successfully!'); // Alert message after successful combination
    } catch (error) {
      console.error("Error combining rules:", error);
    }
  };

  return (
    <div className="combine-rules-container">
      <h2>Combine Rules</h2>
      {ruleStrings.map((rule, index) => (
        <input
          key={index}
          type="text"
          value={rule}
          onChange={(e) => handleRuleChange(index, e.target.value)}
          placeholder="Enter rule"
        />
      ))}
      <button onClick={handleAddRule}>Add Rule</button>
      <button onClick={handleCombine}>Combine Rules</button>
      <button type="button" onClick={() => navigate('/')} className='btn'>Back to Dashboard</button>
      {combinedRule && <p className="combined-rule">Combined Rule: {combinedRule}</p>}
    </div>
  );
};

export default CombineRules;
