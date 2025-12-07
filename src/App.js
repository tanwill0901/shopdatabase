import React from "react";
import {BrowserRouter as Router, Routes, Route, Navigate} from "react-router-dom";
import Login from "./main/Login";
import Home from "./Home";

function App() {
    return (
        <Router>
            <Routes>
                {/* Default login page */}
                <Route path="/" element={<Login/>}/>

                {/* Main page after login */}
                <Route path="/main" element={<Home/>}/>

                {/* Redirect anything unknown back to login */}
                <Route path="*" element={<Navigate to="/"/>}/>
            </Routes>
        </Router>
    );
}

export default App;