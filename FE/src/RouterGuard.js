import React from "react";
import { useSelector } from "react-redux";
import { Outlet, Navigate } from "react-router-dom";

const GuardedRoute = () => {
  const token = useSelector((state) => state.strr.token);

  // console.log(token);

  return token ? <Outlet /> : <Navigate to="/" />;
};

export default GuardedRoute;
