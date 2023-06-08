import React from "react";

function UserCheckdata({ item, onRemove }) {
  return (
    <div className="hashcheckdiv">
      <div className="hashcheckbox" onClick={onRemove}>
        {item}
      </div>
    </div>
  );
}

export default UserCheckdata;
