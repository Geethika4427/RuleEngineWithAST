import { useState } from 'react';
import axios from 'axios';
import './RuleEvaluation.css';
import { useNavigate } from 'react-router-dom';

const RuleEvaluation = ({ rules = [] }) => {
  const [selectedRuleId, setSelectedRuleId] = useState('');
  const [user, setUser] = useState({ age: '', salary: '', experience: '', department: '' });
  const [evaluationResult, setEvaluationResult] = useState(null);
  const navigate = useNavigate();

  const handleEvaluate = async () => {
    try {
      const response = await axios.post(`http://localhost:9090/api/rules/evaluate/${selectedRuleId}`, user);
      setEvaluationResult(response.data);
    } catch (error) {
      console.error("Error evaluating rule:", error);
    }
  };

  return (
    <div className="evaluation-container">
      <h2>Evaluate Rule</h2>
      {rules && rules.length > 0 ? (
        <select onChange={(e) => setSelectedRuleId(e.target.value)}>
          <option value="">Select Rule</option>
          {rules.map((rule) => (
            <option key={rule.id} value={rule.id}>{rule.ruleString}</option>
          ))}
        </select>
      ) : (
        <p>No rules available to evaluate.</p>
      )}
      <div>
        <input type="number" placeholder="Age" onChange={(e) => setUser({ ...user, age: e.target.value })} />
        <input type="number" placeholder="Salary" onChange={(e) => setUser({ ...user, salary: e.target.value })} />
        <input type="number" placeholder="Experience" onChange={(e) => setUser({ ...user, experience: e.target.value })} />
        <input type="text" placeholder="Department" onChange={(e) => setUser({ ...user, department: e.target.value })} />
      </div>
      <button onClick={handleEvaluate}>Evaluate</button>
      <button type="button" onClick={() => navigate('/')} className='btn'>Back to Dashboard</button>
      {evaluationResult !== null && (
        <p className={`evaluation-result ${evaluationResult ? 'true' : 'false'}`}>
          Evaluation Result: {evaluationResult ? "True" : "False"}
        </p>
      )}
    </div>
  );
};

export default RuleEvaluation;
