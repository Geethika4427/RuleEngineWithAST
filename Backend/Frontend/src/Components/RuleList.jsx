const RuleList = ({ rules }) => {
  return (
    <div>
      <h2>Rules List</h2>
      <ul>
        {rules.map((rule) => (
          <li key={rule.id}>{rule.ruleString}</li>
        ))}
      </ul>
    </div>
  );
};

export default RuleList;
