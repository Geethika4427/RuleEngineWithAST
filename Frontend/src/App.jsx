import { useEffect, useState } from 'react';
import axios from 'axios';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Dashboard from './Components/Dashboard';
import CombineRules from './Components/CombineRules';
import RuleEvaluation from './Components/RuleEvaluation';
import ManageRulesForm from './Components/ManageRulesForm';
import CreateRulesPage from './Components/CreateRulesPage';

const App = () => {

  const [rules, setRules] = useState([]);

  useEffect(() => {
    const fetchRules = async () => {
      try {
        const response = await axios.get('http://localhost:9090/api/rules');
        setRules(response.data);
      } catch (error) {
        console.error("Error fetching rules:", error);
      }
    };

    fetchRules();
  }, []);
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/create-rules" element={<CreateRulesPage/>} />        
        <Route path="/combine-rules" element={<CombineRules />} />
        <Route path="/evaluate-rules" element={<RuleEvaluation rules={rules}/>} />
        <Route path="/manage-rules" element={<ManageRulesForm />} />
      </Routes>
    </Router>
  );
};

export default App;
