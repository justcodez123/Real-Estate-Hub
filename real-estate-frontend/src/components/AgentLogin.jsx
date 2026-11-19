import React, { useState } from 'react';
import { useNavigate, useLocation, Link } from 'react-router-dom';
import { authService } from '../services/api';
import './AgentLogin.css';

const AgentLogin = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const location = useLocation();

    const from = location.state?.from?.pathname || '/agent-dashboard';

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            const response = await authService.agentLogin(email, password);

            if (response.data.success && response.data.data) {
                const agentData = response.data.data;
                localStorage.setItem('agent', JSON.stringify(agentData));
                navigate(from, { replace: true });
            } else {
                setError(response.data.message || 'Login failed');
            }
        } catch (err) {
            setError(err.response?.data?.message || 'Login failed. Please check your credentials.');
            console.error('Agent login error:', err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="agent-login-container">
            <div className="agent-login-card">
                <div className="agent-login-header">
                    <div className="agent-login-logo" aria-hidden="true">
                        <svg width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.1" strokeLinecap="round" strokeLinejoin="round">
                            <rect x="4" y="2" width="16" height="20" rx="2" ry="2" />
                            <path d="M9 22v-4h6v4" />
                            <path d="M8 6h.01" />
                            <path d="M16 6h.01" />
                            <path d="M12 6h.01" />
                            <path d="M12 10h.01" />
                            <path d="M12 14h.01" />
                            <path d="M16 10h.01" />
                            <path d="M16 14h.01" />
                            <path d="M8 10h.01" />
                            <path d="M8 14h.01" />
                        </svg>
                    </div>
                    <h1>Agent Portal</h1>
                    <p>Sign in to manage your property portfolio and viewings</p>
                </div>

                {error && (
                    <div className="error-alert">
                        <span>{error}</span>
                    </div>
                )}

                {location.state?.message && (
                    <div className="success-alert">
                        <span>{location.state.message}</span>
                    </div>
                )}

                <form onSubmit={handleSubmit} className="agent-login-form">
                    <div className="form-group">
                        <label htmlFor="email">Agency Email Address</label>
                        <input
                            type="email"
                            id="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            placeholder="agent@realestate.com"
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
                        className="agent-login-btn"
                        disabled={loading}
                    >
                        {loading ? 'Signing in...' : 'Sign In to Agent Portal'}
                    </button>
                </form>

                <div className="agent-login-footer">
                    <p className="signup-prompt">New partner agent? <Link to="/agent-register" className="register-link">Apply for Agent Account</Link></p>
                    <p className="user-prompt">Looking to buy or rent? <Link to="/login" className="user-login-link">Buyer Sign In →</Link></p>
                </div>
            </div>
        </div>
    );
};

export default AgentLogin;
