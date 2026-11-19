import React, { useState } from 'react';
import { useNavigate, useLocation, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Login.css';

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const { login } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();

    const from = location.state?.from?.pathname || '/';

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            const result = await login(email, password);

            if (result.success) {
                navigate(from, { replace: true });
            } else {
                setError(result.error);
            }
        } catch (err) {
            setError('An unexpected error occurred. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    const fillDemo = (demoEmail) => {
        setEmail(demoEmail);
        setPassword('password123');
    };

    return (
        <div className="login-container">
            <div className="login-card">
                <div className="login-header">
                    <div className="login-logo" aria-hidden="true">
                        <svg width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round">
                            <path d="M3 10.5L12 3l9 7.5V20a1 1 0 0 1-1 1h-5.5v-6.5h-5V21H4a1 1 0 0 1-1-1v-9.5z" />
                        </svg>
                    </div>
                    <h1>Welcome back</h1>
                    <p>Sign in to your RealEstateHub account</p>
                </div>

                {error && (
                    <div className="error-alert">
                        <svg className="error-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round">
                            <circle cx="12" cy="12" r="10" />
                            <line x1="12" y1="8" x2="12" y2="12" />
                            <line x1="12" y1="16" x2="12.01" y2="16" />
                        </svg>
                        <span>{error}</span>
                    </div>
                )}

                <form onSubmit={handleSubmit} className="login-form">
                    <div className="form-group">
                        <label htmlFor="email">Email Address</label>
                        <input
                            type="email"
                            id="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            placeholder="name@company.com"
                            required
                            autoComplete="email"
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input
                            type="password"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            placeholder="Enter your password"
                            required
                            autoComplete="current-password"
                        />
                    </div>

                    <button
                        type="submit"
                        className="login-btn"
                        disabled={loading}
                    >
                        {loading ? 'Signing in...' : 'Sign In'}
                    </button>
                </form>

                <div className="login-footer">
                    <div className="demo-credentials-grid">
                        <div
                            className="demo-credentials"
                            onClick={() => fillDemo('john.smith@realestate.com')}
                            title="Click to autofill Demo Admin"
                        >
                            <strong>Demo Admin (Click to fill)</strong>
                            <span>john.smith@realestate.com · password123</span>
                        </div>
                        <div
                            className="demo-credentials"
                            onClick={() => fillDemo('emily.davis@email.com')}
                            title="Click to autofill Demo User"
                        >
                            <strong>Demo User (Basic)</strong>
                            <span>emily.davis@email.com · password123</span>
                        </div>
                    </div>
                    <div className="login-links">
                        <p>Don't have an account? <Link to="/register" className="register-link">Create an account</Link></p>
                        <p>Are you a licensed agent? <Link to="/agent-login" className="agent-link">Agent Portal →</Link></p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Login;
