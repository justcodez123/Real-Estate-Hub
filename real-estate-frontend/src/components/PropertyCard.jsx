import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { favoriteService, propertyImageService, propertyService } from '../services/api';
import { useAuth } from '../context/AuthContext';
import './PropertyCard.css';

const PropertyCard = ({ property, userId = null, showFavoriteButton = true, showActions = false, onDelete }) => {
    const { user } = useAuth();
    const navigate = useNavigate();
    const finalUserId = userId || user?.id;

    const [isFavorite, setIsFavorite] = useState(false);
    const [loading, setLoading] = useState(false);
    const [, setImages] = useState([]);
    const [primaryImage, setPrimaryImage] = useState(null);

    useEffect(() => {
        if (property?.id) {
            fetchPropertyImages();
            if (finalUserId && finalUserId > 0) {
                checkFavoriteStatus();
            } else {
                setIsFavorite(false);
            }
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [property?.id, finalUserId]);

    const checkFavoriteStatus = async () => {
        try {
            const response = await favoriteService.isFavorite(finalUserId, property.id);
            let isFav = false;

            if (response && response.data) {
                if (typeof response.data === 'boolean') {
                    isFav = response.data;
                } else if (response.data.data !== undefined) {
                    isFav = response.data.data === true;
                } else if (response.data.isFavorite !== undefined) {
                    isFav = response.data.isFavorite === true;
                } else if (response.data.success !== undefined) {
                    isFav = response.data.success === true;
                }
            }

            setIsFavorite(isFav);
        } catch (error) {
            console.error('Error checking favorite status:', error);
            setIsFavorite(false);
        }
    };

    const fetchPropertyImages = async () => {
        try {
            const response = await propertyImageService.getPropertyImages(property.id);
            const imageData = response.data.data || response.data;
            const imageArray = Array.isArray(imageData) ? imageData : [];

            setImages(imageArray);

            const primary = imageArray.find(img => img.isPrimary);
            setPrimaryImage(primary || imageArray[0] || null);
        } catch (error) {
            console.error('Error fetching property images:', error);
            setPrimaryImage(null);
        }
    };

    const formatPrice = (price) => {
        return new Intl.NumberFormat('en-IN', {
            style: 'currency',
            currency: 'INR',
            minimumFractionDigits: 0,
        }).format(price);
    };

    const formatListingType = (type) => {
        return type === 'FOR_SALE' ? 'For Sale' : 'For Rent';
    };

    const formatPropertyType = (type) => {
        if (!type) return 'Property';
        return type.charAt(0) + type.slice(1).toLowerCase().replace('_', ' ');
    };

    const handleToggleFavorite = async (e) => {
        e.preventDefault();
        e.stopPropagation();

        if (!finalUserId) {
            navigate('/login');
            return;
        }

        try {
            setLoading(true);
            await favoriteService.toggleFavorite(finalUserId, property.id);
            setIsFavorite(!isFavorite);
        } catch (error) {
            console.error('Error toggling favorite:', error);
            setIsFavorite(isFavorite);
        } finally {
            setLoading(false);
        }
    };

    const canEditOrDelete = () => {
        if (!user) return false;
        const isAdmin = user.userType === 'ADMIN' || user.role === 'ADMIN';
        const isAgent = user.userType === 'AGENT' || user.role === 'AGENT';
        const isOwner = property.owner?.id === user.id;
        return isAdmin || isAgent || isOwner;
    };

    const handleEdit = (e) => {
        e.preventDefault();
        e.stopPropagation();
        navigate(`/edit-property/${property.id}`);
    };

    const handleDelete = async (e) => {
        e.preventDefault();
        e.stopPropagation();

        if (!window.confirm('Are you sure you want to delete this property?')) {
            return;
        }

        try {
            await propertyService.deleteProperty(property.id);
            alert('Property deleted successfully!');
            if (onDelete) {
                onDelete(property.id);
            }
        } catch (error) {
            console.error('Error deleting property:', error);
            alert('Failed to delete property');
        }
    };

    return (
        <article
            className="property-card"
            onClick={() => navigate(`/property/${property.id}`)}
        >
            <div className="property-image">
                {primaryImage ? (
                    <img src={primaryImage.imageUrl} alt={property.title} loading="lazy" />
                ) : (
                    <div className="no-image">
                        <svg width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
                            <path d="M3 10.5L12 3l9 7.5V20a1 1 0 0 1-1 1h-5.5v-6.5h-5V21H4a1 1 0 0 1-1-1v-9.5z" />
                        </svg>
                        <span>Photo pending</span>
                    </div>
                )}

                <div className="card-badges">
                    <span className={`listing-badge ${property.listingType === 'FOR_RENT' ? 'rent' : 'sale'}`}>
                        {formatListingType(property.listingType)}
                    </span>
                    <span className="property-type-pill">
                        {formatPropertyType(property.propertyType)}
                    </span>
                </div>

                {showFavoriteButton && (
                    <button
                        type="button"
                        className={`favorite-btn ${isFavorite ? 'favorited' : ''}`}
                        onClick={handleToggleFavorite}
                        disabled={loading}
                        title={isFavorite ? 'Remove from saved' : 'Save property'}
                        aria-label={isFavorite ? 'Remove from saved' : 'Save property'}
                    >
                        <svg width="18" height="18" viewBox="0 0 24 24" fill={isFavorite ? 'currentColor' : 'none'} stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                            <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z" />
                        </svg>
                    </button>
                )}
            </div>

            <div className="property-details">
                <div className="property-price-row">
                    <p className="property-price">
                        {formatPrice(property.price)}
                        {property.listingType === 'FOR_RENT' && <span className="price-period">/mo</span>}
                    </p>
                </div>

                <h3 className="property-title">{property.title}</h3>

                <p className="property-address">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                        <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z" />
                        <circle cx="12" cy="10" r="3" />
                    </svg>
                    <span>{property.address}, {property.city}, {property.state} {property.zipCode}</span>
                </p>

                <div className="property-features">
                    <span className="feature">
                        <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.9" strokeLinecap="round" strokeLinejoin="round">
                            <path d="M2 4v16" />
                            <path d="M2 8h18a2 2 0 0 1 2 2v10" />
                            <path d="M2 17h20" />
                            <path d="M6 8v9" />
                        </svg>
                        <strong>{property.bedrooms}</strong> Beds
                    </span>
                    <span className="feature">
                        <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.9" strokeLinecap="round" strokeLinejoin="round">
                            <path d="M9 6 6.5 3.5a1.5 1.5 0 0 0-1-.5C4.683 3 4 3.683 4 4.5V17a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2v-5" />
                            <line x1="10" y1="5" x2="8" y2="7" />
                            <line x1="2" y1="12" x2="22" y2="12" />
                        </svg>
                        <strong>{property.bathrooms}</strong> Baths
                    </span>
                    <span className="feature">
                        <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.9" strokeLinecap="round" strokeLinejoin="round">
                            <rect x="3" y="3" width="18" height="18" rx="2" />
                            <path d="M3 9h18" />
                            <path d="M9 21V9" />
                        </svg>
                        <strong>{property.squareFeet}</strong> sqft
                    </span>
                </div>

                <div className="property-card-footer" onClick={(e) => e.stopPropagation()}>
                    <Link to={`/property/${property.id}`} className="view-details-btn">
                        <span>View Details</span>
                        <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round">
                            <line x1="5" y1="12" x2="19" y2="12" />
                            <polyline points="12 5 19 12 12 19" />
                        </svg>
                    </Link>

                    {showActions && canEditOrDelete() && (
                        <div className="property-actions">
                            <button type="button" className="edit-btn" onClick={handleEdit}>
                                Edit
                            </button>
                            <button type="button" className="delete-btn" onClick={handleDelete}>
                                Delete
                            </button>
                        </div>
                    )}
                </div>
            </div>
        </article>
    );
};

export default PropertyCard;
