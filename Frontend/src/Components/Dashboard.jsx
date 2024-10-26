import { useNavigate } from 'react-router-dom';
import './Dashboard.css'; 

const Dashboard = () => {
  const navigate = useNavigate();

  const handleNavigate = (path) => {
    navigate(path);
  };

  return (
    <div className="dashboard-container">
      <h1>Rule Engine Dashboard</h1>
      <div className="dashboard-buttons">
        <button onClick={() => handleNavigate('/create-rules')} className="dashboard-button">
          Create Rules
        </button>
        <button onClick={() => handleNavigate('/combine-rules')} className="dashboard-button">
          Combine Rules
        </button>
        <button onClick={() => handleNavigate('/evaluate-rules')} className="dashboard-button">
          Evaluate Rules
        </button>
        <button onClick={() => handleNavigate('/manage-rules')} className="dashboard-button">
          Manage Rules
        </button>
      </div>
    </div>
  );
};

export default Dashboard;
