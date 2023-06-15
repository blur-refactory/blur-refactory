import React from "react";
import "./style/normalize.css";
import "./App.css";

import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import SocialSignInRedirect from "./components/Start/SignIn/socialSignIn";
import ChatPage from "../src/components/Chat/ChatPage";
import Start from "./pages/Start";
import Home from "./pages/Home";
import Meeting from "./pages/Meeting";
import MyInfo from "./pages/MyInfo";
import GuardedRoute from "./RouterGuard";
import NotFound from "./pages/NotFound";

function App() {
  return (
    <div className="App">
      <Router>
        <Routes>
          <Route path="/" element={<Start />}></Route>
          {/* <Route path="/meeting" element={<GuardedRoute />}> */}
          <Route path="/meeting" element={<Meeting />}></Route>
          {/* </Route> */}
          {/* <Route path="/MyInfo" element={<GuardedRoute />}> */}
          <Route path="/MyInfo" element={<MyInfo />}></Route>
          {/* </Route> */}
          {/* <Route path="/home" element={<GuardedRoute />}> */}
          <Route path="/home" element={<Home />}></Route>
          {/* </Route> */}
          <Route path="/oauth/redirect" element={<SocialSignInRedirect />}></Route>
          <Route path="/chat/room/enter/:roomId" element={<ChatPage />}></Route>
          <Route path="/*" element={<NotFound />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
