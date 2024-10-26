// import { useState } from 'react';
// import axios from 'axios';
// import './RuleForm.css'; // Import the CSS file

// const RuleForm = ({ onRuleCreated }) => {
//   const [ruleString, setRuleString] = useState('');

//   const handleSubmit = async (e) => {
//     e.preventDefault();
//     try {
//       const response = await axios.post('http://localhost:9090/api/rules/create', { ruleString });
//       onRuleCreated(response.data);
//       alert('Rule created successfully!'); // Alert for successful creation
//       setRuleString(''); // Clear input after submission
//     } catch (error) {
//       console.error("Error creating rule:", error);
//       alert('Failed to create rule. Please try again.'); // Alert for error
//     }
//   };

//   return (
//     <form className="rule-form" onSubmit={handleSubmit}>
//         <h1>Create Rules</h1>
//         <textarea
//           value={ruleString}
//           onChange={(e) => setRuleString(e.target.value)}
//           placeholder="Enter your rule string (e.g., age > 30 AND department == 'Sales')"
//           required
//         />
      
//       <button type="submit">Create Rule</button>
//     </form>
//   );
// };

// export default RuleForm;


import { useState } from 'react';
import axios from 'axios';
import './RuleForm.css'; // Import the CSS file
import { useNavigate } from 'react-router-dom';

const RuleForm = ({ onRuleCreated }) => {
  const [ruleString, setRuleString] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:9090/api/rules/create', { ruleString });
      onRuleCreated(response.data); // Call the function passed as a prop
      alert('Rule created successfully!'); // Alert for successful creation
      setRuleString(''); // Clear input after submission
    } catch (error) {
      console.error("Error creating rule:", error);
      alert('Failed to create rule. Please try again.'); // Alert for error
    }
  };

  return (

    <form className="rule-form" onSubmit={handleSubmit}>
      <h1>Create Rules</h1>
      <textarea
        value={ruleString}
        onChange={(e) => setRuleString(e.target.value)}
        placeholder="Enter your rule string (e.g., age > 30 AND department == 'Sales')"
        required
      />
      <button type="submit">Create Rule</button>
      <button type="button" onClick={() => navigate('/')}>Back to Dashboard</button>
    </form>
  );

};

export default RuleForm;