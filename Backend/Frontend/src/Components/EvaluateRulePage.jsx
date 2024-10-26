// import { useEffect, useState } from 'react';
// import RuleEvaluation from './RuleEvaluation';
// import RuleList from './RuleList'; // Import the RuleList component
// import axios from 'axios';

// const App = () => {
//   const [rules, setRules] = useState([]);

//   useEffect(() => {
//     const fetchRules = async () => {
//       try {
//         const response = await axios.get('http://localhost:9090/api/rules'); // Adjust the endpoint as necessary
//         setRules(response.data); // Assuming response.data is an array of rules
//       } catch (error) {
//         console.error("Error fetching rules:", error);
//       }
//     };

//     fetchRules();
//   }, []);

//   return (
//     <div>
//       <h1>Rule Evaluation App</h1>
//       <RuleList rules={rules} /> {/* Render the RuleList component */}
//       <RuleEvaluation rules={rules} /> {/* Render the RuleEvaluation component */}
//     </div>
//   );
// };

// export default App;