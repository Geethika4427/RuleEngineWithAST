import { useEffect, useState } from 'react';
import axios from 'axios';
import './ManageRulesForm.css';
import { useNavigate } from 'react-router-dom';

const ManageRulesForm = () => {
    const [rules, setRules] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [editingRule, setEditingRule] = useState(null);
    const [updatedRuleString, setUpdatedRuleString] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchRules = async () => {
            try {
                const res = await axios.get('http://localhost:9090/api/rules');
                setRules(res.data);
            } catch (err) {
                console.error('Error fetching rules:', err);
                setError('Failed to load rules. Please try again later.');
            } finally {
                setLoading(false);
            }
        };

        fetchRules();
    }, []);

    // Logging successMessage and extending timeout for visibility
    useEffect(() => {
        if (successMessage) {
            console.log('Success Message:', successMessage); // Log only when there's a success message
            const timer = setTimeout(() => setSuccessMessage(''), 3000); // Clear after 5 seconds
            return () => clearTimeout(timer); // Cleanup timer
        }
    }, [successMessage]);
    

    const handleDelete = async (ruleId) => {
        try {
            await axios.delete(`http://localhost:9090/api/rules/delete/${ruleId}`);
            setRules((prevRules) => prevRules.filter((rule) => rule.id !== ruleId));
            setSuccessMessage(`Rule with ID ${ruleId} has been successfully deleted.`);
            alert(`Rule with ID ${ruleId} has been successfully deleted.`); // Display alert for delete action
            console.log('Delete Success:', `Rule with ID ${ruleId} has been successfully deleted.`);
        } catch (err) {
            console.error('Error deleting rule:', err);
            setError('Failed to delete rule. Please try again later.');
        }
    };

    const handleEdit = (rule) => {
        setEditingRule(rule);
        setUpdatedRuleString(rule.ruleString);
    };

    const handleUpdate = async (e) => {
        e.preventDefault();
        if (!updatedRuleString) return;

        try {
            const res = await axios.put(`http://localhost:9090/api/rules/${editingRule.id}`, {
                ruleString: updatedRuleString,
            });
            setRules((prevRules) =>
                prevRules.map((rule) =>
                    rule.id === editingRule.id ? res.data : rule
                )
            );
            setSuccessMessage(`Rule with ID ${editingRule.id} has been successfully updated.`);
            alert(`Rule with ID ${editingRule.id} has been successfully updated.`); // Display alert for update action
            console.log('Update Success:', `Rule with ID ${editingRule.id} has been successfully updated.`);
            setEditingRule(null);
            setUpdatedRuleString('');
        } catch (err) {
            console.error('Error updating rule:', err);
            setError('Failed to update rule. Please try again later.');
        }
    };

    return (
        <div className="manage-rules-container">
            <h2>Manage Rules</h2>

            {/* Display success message when available */}
            {successMessage && <p className="success-message">{successMessage}</p>}
            
            {loading ? (
                <p>Loading rules...</p>
            ) : error ? (
                <p className="error">{error}</p>
            ) : (
                <div>
                    <table>
                        <thead>
                            <tr>
                                <th>Rule String</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {rules.map((rule) => (
                                <tr key={rule.id}>
                                    <td>{rule.ruleString}</td>
                                    <td>
                                        <div className="action-buttons">
                                            <button onClick={() => handleEdit(rule)}>Update</button>
                                            <button onClick={() => handleDelete(rule.id)}>Delete</button>
                                        </div>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>

                    {editingRule && (
                        <form onSubmit={handleUpdate}>
                            <h3>Update Rule</h3>
                            <textarea
                                value={updatedRuleString}
                                onChange={(e) => setUpdatedRuleString(e.target.value)}
                                required
                            />
                            <div className=" form-buttons">
                                <button type="submit">Save Changes</button>
                                <button type="button" onClick={() => setEditingRule(null)}>Cancel</button>
                            </div>
                        </form>
                    )}
                    <button type="button" onClick={() => navigate('/')} className='btn'>Back to Dashboard</button>
                </div>
            )}
        </div>
    );
};

export default ManageRulesForm;
