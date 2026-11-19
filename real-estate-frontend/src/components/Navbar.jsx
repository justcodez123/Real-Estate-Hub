import React, { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Navbar.css';

const Navbar = () => {
    const [menuOpen, setMenuOpen] = useState(false);
    const { user, isAuthenticated, isAdmin, hasSubscription, logout } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();

    const toggleMenu = () => {
        setMenuOpen(!menuOpen);
    };

    const handleLogout = () => {
        logout();
        setMenuOpen(false);
        navigate('/');
    };

    const isActive = (path) => location.pathname === path;

    const getInitials = () => {
        if (!user) return 'U';
        const first = user.firstName ? user.firstName[0] : '';
        const last = user.lastName ? user.lastName[0] : '';
        return (first + last).toUpperCase() || 'U';
    };

    return (
        <nav className="navbar">
            <div className="nav-container">
                <Link to="/" className="nav-logo" onClick={() => setMenuOpen(false)}>
                    <span className="nav-logo-mark" aria-hidden="true">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round">
                            <path d="M3 10.5L12 3l9 7.5V20a1 1 0 0 1-1 1h-5.5v-6.5h-5V21H4a1 1 0 0 1-1-1v-9.5z" />
                        </svg>
                    </span>
                    <span className="nav-logo-text">
                        RealEstate<span className="nav-logo-accent">Hub</span>
                    </span>
                </Link>

                <button
                    className="mobile-menu-toggle"
                    onClick={toggleMenu}
                    aria-label="Toggle navigation menu"
                >
                    {menuOpen ? (
                        <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                            <line x1="18" y1="6" x2="6" y2="18" />
                            <line x1="6" y1="6" x2="18" y2="18" />
                        </svg>
                    ) : (
                        <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                            <line x1="3" y1="6" x2="21" y2="6" />
                            <line x1="3" y1="12" x2="21" y2="12" />
                            <line x1="3" y1="18" x2="21" y2="18" />
                        </svg>
                    )}
                </button>

                <ul className={`nav-menu ${menuOpen ? 'active' : ''}`}>
                    <li className="nav-item">
                        <Link
                            to="/"
                            className={`nav-link ${isActive('/') ? 'active' : ''}`}
                            onClick={() => setMenuOpen(false)}
                        >
                            Explore
                        </Link>
                    </li>

                    <li className="nav-item">
                        <Link
                            to="/builders"
                            className={`nav-link ${isActive('/builders') ? 'active' : ''}`}
                            onClick={() => setMenuOpen(false)}
                        >
                            Builders
                        </Link>
                    </li>

                    <li className="nav-item">
                        <Link
                            to="/add-property"
                            className="nav-link nav-link-btn"
                            onClick={() => setMenuOpen(false)}
                        >
                            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.4" strokeLinecap="round" strokeLinejoin="round">
                                <line x1="12" y1="5" x2="12" y2="19" />
                                <line x1="5" y1="12" x2="19" y2="12" />
                            </svg>
                            List Property
                        </Link>
                    </li>

                    {/* BASIC+ subscription features */}
                    {isAuthenticated && hasSubscription('BASIC') && (
                        <>
                            <li className="nav-item">
                                <Link
                                    to="/search"
                                    className={`nav-link ${isActive('/search') ? 'active' : ''}`}
                                    onClick={() => setMenuOpen(false)}
                                >
                                    Advanced Search
                                </Link>
                            </li>
                            <li className="nav-item">
                                <Link
                                    to="/favorites"
                                    className={`nav-link ${isActive('/favorites') ? 'active' : ''}`}
                                    onClick={() => setMenuOpen(false)}
                                >
                                    Saved
                                </Link>
                            </li>
                            <li className="nav-item">
                                <Link
                                    to="/history"
                                    className={`nav-link ${isActive('/history') ? 'active' : ''}`}
                                    onClick={() => setMenuOpen(false)}
                                >
                                    History
                                </Link>
                            </li>
                        </>
                    )}

                    {/* Admin only features */}
                    {isAuthenticated && isAdmin() && (
                        <>
                            <li className="nav-item">
                                <Link
                                    to="/subscriptions"
                                    className={`nav-link ${isActive('/subscriptions') ? 'active' : ''}`}
                                    onClick={() => setMenuOpen(false)}
                                >
                                    Subscriptions
                                </Link>
                            </li>
                            <li className="nav-item">
                                <Link
                                    to="/users"
                                    className={`nav-link ${isActive('/users') ? 'active' : ''}`}
                                    onClick={() => setMenuOpen(false)}
                                >
                                    Users
                                </Link>
                            </li>
                        </>
                    )}

                    {/* Auth buttons */}
                    {isAuthenticated ? (
                        <li className="nav-item user-section">
                            <div className="user-avatar" aria-hidden="true">
                                {getInitials()}
                            </div>
                            <div className="user-info-nav">
                                <span className="user-name">{user.firstName} {user.lastName}</span>
                                <span className={`user-badge ${user.subscriptionType || 'FREE'}`}>
                                    {user.subscriptionType || 'FREE'}
                                </span>
                            </div>
                            <button onClick={handleLogout} className="nav-link logout-btn">
                                Sign Out
                            </button>
                        </li>
                    ) : (
                        <li className="nav-item nav-auth-group">
                            <Link
                                to="/agent-login"
                                className={`nav-link nav-agent-link ${isActive('/agent-login') ? 'active' : ''}`}
                                onClick={() => setMenuOpen(false)}
                            >
                                Agent Portal
                            </Link>
                            <Link
                                to="/login"
                                className="nav-link nav-signin-link"
                                onClick={() => setMenuOpen(false)}
                            >
                                Sign In
                            </Link>
                            <Link
                                to="/register"
                                className="nav-link nav-link-btn"
                                onClick={() => setMenuOpen(false)}
                            >
                                Get Started
                            </Link>
                        </li>
                    )}
                </ul>
            </div>
        </nav>
    );
};

export default Navbar;
