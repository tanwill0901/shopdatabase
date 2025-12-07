import React, {useState} from "react";
import {loginUser} from "../services/api";
import "./Login.css";

function Login() {
    const [username, setUsername] = useState("");
    const [userpass, setUserpass] = useState("");
    const [error, setError] = useState("");

    const handleLogin = async (e) => {
        e.preventDefault();

        if (!username || !userpass) {
            setError("Please enter both username and password.");
            return;
        }

        try {
            const res = await loginUser({username, userpass});

            if (res.data.success) {
                // Save session data
                localStorage.setItem("username", res.data.username);
                localStorage.setItem("userstatus", res.data.userstatus);

                // Redirect to main manager page
                window.location.href = "/main";
            } else {
                setError("Invalid username or password.");
            }
        } catch (err) {
            console.error("Login error:", err);
            setError("Server error during login.");
        }
    };

    return (
        <div className="login-container">
            <div className="login-card">
                <h2>Welcome!</h2>
                <form onSubmit={handleLogin}>
                    <div className="login-inputs">
                        <input
                            type="text"
                            placeholder="Username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                        />
                        <input
                            type="password"
                            placeholder="Password"
                            value={userpass}
                            onChange={(e) => setUserpass(e.target.value)}
                        />
                    </div>
                    <button type="submit" className="login-btn">
                        Login
                    </button>
                </form>
                {error && <p className="login-error">{error}</p>}
            </div>
        </div>
    );
}

export default Login;