import "./index.css";
import React from "react";
import { Link } from "react-router-dom";

function NotFound() {
  return (
    <div>
      <Link to="/home">
        <div className="NotFoundbox">
          <div className="squish-unsquish"></div>
        </div>
      </Link>
      ;<span className="NotFoundText"> 404 NOT FOUND &gt; &lt; </span>
    </div>
  );
}

export default NotFound;
