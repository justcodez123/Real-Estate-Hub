import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { propertyService } from '../services/api';
import { useAuth } from '../context/AuthContext';
import PropertyCard from './PropertyCard';
import ConnectionError from './ConnectionError';
import './PropertyList.css';

const PropertyList = () => {
    const [properties, setProperties] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [filters, setFilters] = useState({
        city: '',
        propertyType: '',
        listingType: '',
    });
    const { user, isAuthenticated } = useAuth();

    useEffect(() => {
        fetchProperties();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const fetchProperties = async () => {
        try {
            setLoading(true);
            const response = await propertyService.getAvailableProperties();
            const apiResponse = response.data;
            const data = apiResponse.data || apiResponse;
            setProperties(Array.isArray(data) ? data : (data.content || []));
            setError(null);
        } catch (err) {
            setError(err);
            console.error('Error fetching properties:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleFilterChange = (e) => {
        setFilters({
            ...filters,
            [e.target.name]: e.target.value,
        });
    };

    const applyFilters = () => {
        return properties.filter((property) => {
            return (
                (!filters.city || (property.city && property.city.toLowerCase().includes(filters.city.toLowerCase()))) &&
                (!filters.propertyType || property.propertyType === filters.propertyType) &&
                (!filters.listingType || property.listingType === filters.listingType)
            );
        });
    };

    const clearFilters = () => {
        setFilters({
            city: '',
            propertyType: '',
            listingType: '',
        });
    };

    const handlePropertyDeleted = (propertyId) => {
        setProperties(prev => prev.filter(p => p.id !== propertyId));
    };

    const filteredProperties = applyFilters();
    const hasActiveFilters = Boolean(filters.city || filters.propertyType || filters.listingType);

    if (loading) {
        return <div className="loading">Loading curated properties...</div>;
    }

    if (error) {
        return <ConnectionError error={error} />;
    }

    return (
        <div className="property-list-page">
            {/* Hero Section with Floating Search Bar */}
            <section className="hero-banner">
                <div className="hero-banner-inner">
                    <span className="hero-eyebrow">
                        <span className="hero-eyebrow-dot" />
                        Verified Residential & Commercial Listings
                    </span>
                    <h1 className="hero-title">
                        Find a space designed for how you live
                    </h1>
                    <p className="hero-subtitle">
                        Explore handpicked homes, luxury villas, and modern apartments across premier neighborhoods.
                    </p>

                    <div className="filters-bar">
                        <div className="filter-field filter-field-search">
                            <svg className="filter-field-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                <circle cx="11" cy="11" r="8" />
                                <line x1="21" y1="21" x2="16.65" y2="16.65" />
                            </svg>
                            <div className="filter-field-content">
                                <label htmlFor="filter-city">Location</label>
                                <input
                                    id="filter-city"
                                    type="text"
                                    name="city"
                                    placeholder="Search by city..."
                                    value={filters.city}
                                    onChange={handleFilterChange}
                                />
                            </div>
                        </div>

                        <div className="filter-divider" />

                        <div className="filter-field">
                            <div className="filter-field-content">
                                <label htmlFor="filter-property-type">Property Type</label>
                                <select
                                    id="filter-property-type"
                                    name="propertyType"
                                    value={filters.propertyType}
                                    onChange={handleFilterChange}
                                >
                                    <option value="">All Property Types</option>
                                    <option value="HOUSE">House</option>
                                    <option value="APARTMENT">Apartment</option>
                                    <option value="CONDO">Condo</option>
                                    <option value="VILLA">Villa</option>
                                    <option value="COMMERCIAL">Commercial</option>
                                </select>
                            </div>
                        </div>

                        <div className="filter-divider" />

                        <div className="filter-field">
                            <div className="filter-field-content">
                                <label htmlFor="filter-listing-type">Listing Status</label>
                                <select
                                    id="filter-listing-type"
                                    name="listingType"
                                    value={filters.listingType}
                                    onChange={handleFilterChange}
                                >
                                    <option value="">Buy or Rent</option>
                                    <option value="FOR_SALE">For Sale</option>
                                    <option value="FOR_RENT">For Rent</option>
                                </select>
                            </div>
                        </div>

                        {hasActiveFilters && (
                            <button onClick={clearFilters} className="clear-btn" type="button">
                                Reset
                            </button>
                        )}
                    </div>
                </div>
            </section>

            <div className="property-list-container">
                {/* Compact Profile Bar - Show only if user is authenticated */}
                {isAuthenticated && user && (
                    <div className="profile-section">
                        <div className="profile-card">
                            <div className="profile-header">
                                <div className="profile-avatar">
                                    {(user.firstName?.[0] || '') + (user.lastName?.[0] || '')}
                                </div>
                                <div>
                                    <h2>Welcome back, {user.firstName} {user.lastName}</h2>
                                    <p className="profile-subtitle">Signed in to your RealEstateHub account</p>
                                </div>
                            </div>
                            <div className="profile-content">
                                <div className="profile-info">
                                    <div className="info-item">
                                        <span className="info-label">Email</span>
                                        <span className="info-value">{user.email}</span>
                                    </div>
                                    <div className="info-item">
                                        <span className="info-label">Phone</span>
                                        <span className="info-value">{user.phone || 'Not provided'}</span>
                                    </div>
                                    <div className="info-item">
                                        <span className="info-label">Role</span>
                                        <span className="info-value">{user.userType}</span>
                                    </div>
                                    <div className="info-item">
                                        <span className="info-label">Plan</span>
                                        <div className="info-plan-row">
                                            <span className="info-value subscription-badge">{user.subscriptionType}</span>
                                            <Link to="/upgrade" className="profile-upgrade-link">Manage Plan</Link>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                )}

                {/* Listings Header & Quick Tabs */}
                <div className="listings-toolbar">
                    <div className="listings-heading">
                        <h2>Featured Listings</h2>
                        <span className="listings-count-pill">
                            {filteredProperties.length} {filteredProperties.length === 1 ? 'property' : 'properties'}
                        </span>
                    </div>

                    <div className="listings-quick-tabs">
                        <button
                            type="button"
                            className={`quick-tab ${filters.listingType === '' ? 'active' : ''}`}
                            onClick={() => setFilters({ ...filters, listingType: '' })}
                        >
                            All
                        </button>
                        <button
                            type="button"
                            className={`quick-tab ${filters.listingType === 'FOR_SALE' ? 'active' : ''}`}
                            onClick={() => setFilters({ ...filters, listingType: 'FOR_SALE' })}
                        >
                            For Sale
                        </button>
                        <button
                            type="button"
                            className={`quick-tab ${filters.listingType === 'FOR_RENT' ? 'active' : ''}`}
                            onClick={() => setFilters({ ...filters, listingType: 'FOR_RENT' })}
                        >
                            For Rent
                        </button>
                    </div>
                </div>

                <div className="property-grid">
                    {filteredProperties.length === 0 ? (
                        <div className="no-properties">
                            <div className="no-properties-icon" aria-hidden="true">
                                <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
                                    <circle cx="11" cy="11" r="8" />
                                    <line x1="21" y1="21" x2="16.65" y2="16.65" />
                                </svg>
                            </div>
                            <h3>No matching properties found</h3>
                            <p>Try adjusting your city or property type filters to see more listings.</p>
                            {hasActiveFilters && (
                                <button onClick={clearFilters} className="clear-btn-Empty">
                                    Clear All Filters
                                </button>
                            )}
                        </div>
                    ) : (
                        filteredProperties.map((property) => (
                            <PropertyCard
                                key={property.id}
                                property={property}
                                showActions={true}
                                onDelete={handlePropertyDeleted}
                            />
                        ))
                    )}
                </div>
            </div>
        </div>
    );
};

export default PropertyList;
