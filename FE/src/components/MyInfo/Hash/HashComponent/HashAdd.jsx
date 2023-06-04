// 이걸로 화면에 띄워 주면 될 듯       <HashAdd todoList={todoList} />

import React from "react";
import CheckData from "./Checkdata";
import "../Hash.css";

function HashAdd(props) {
  console.log("HashAdd", props.checkData);

  return (
    <div>
      <div className="hashaddiv">
        {props.checkData &&
          props.checkData.map((item) => <CheckData item={item} />)}
      </div>
    </div>
  );
}

export default HashAdd;
