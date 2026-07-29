# 🎯 AGENT DASHBOARD - COMPLETE COMPONENT CODE

## File Structure
```
src/
├── pages/
│   └── AgentDashboard.jsx (Main component)
├── components/
│   ├── AgentDashboard/
│   │   ├── Sidebar.jsx
│   │   ├── MyProperties.jsx
│   │   ├── PropertyCard.jsx
│   │   ├── AddProperty.jsx
│   │   ├── EditProperty.jsx
│   │   ├── ImageUpload.jsx
│   │   ├── Statistics.jsx
│   │   └── Dashboard.module.css
│   └── Common/
│       └── ProtectedRoute.jsx
└── styles/
    └── Dashboard.css
```

---

## 1. AgentDashboard.jsx (Main Container)

```jsx
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Sidebar from '../components/AgentDashboard/Sidebar';
import MyProperties from '../components/AgentDashboard/MyProperties';
import AddProperty from '../components/AgentDashboard/AddProperty';
import Statistics from '../components/AgentDashboard/Statistics';
import styles from '../components/AgentDashboard/Dashboard.module.css';

const AgentDashboard = () => {
  const [activeSection, setActiveSection] = useState('overview'); // overview, properties, add
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState({
    totalProperties: 0,
    activeListings: 0,
    soldProperties: 0,
    totalViews: 0
  });

  useEffect(() => {
    loadUserData();
  }, []);

  const loadUserData = async () => {
    try {
      const userData = JSON.parse(localStorage.getItem('user'));
      setUser(userData);
      
      if (userData?.id) {
        await fetchStatistics(userData.id);
      }
    } catch (error) {
      console.error('Error loading user data:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchStatistics = async (userId) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/properties/owner/${userId}`
      );
      
      const properties = response.data.data;
      const activeListings = properties.filter(p => p.available).length;
      const soldProperties = properties.filter(p => !p.available).length;

      setStats({
        totalProperties: properties.length,
        activeListings,
        soldProperties,
        totalViews: properties.length * 10 // Mock data
      });
    } catch (error) {
      console.error('Error fetching statistics:', error);
    }
  };

  if (loading) {
    return <div className={styles.loading}>Loading dashboard...</div>;
  }

  if (!user) {
    return <div className={styles.error}>Please login to access dashboard</div>;
  }

  return (
    <div className={styles.dashboardContainer}>
      <Sidebar activeSection={activeSection} onSelect={setActiveSection} user={user} />
      
      <main className={styles.mainContent}>
        <header className={styles.header}>
          <h1>Agent Dashboard</h1>
          <div className={styles.userInfo}>
            <span>{user.firstName} {user.lastName}</span>
            <small>{user.email}</small>
          </div>
        </header>

        <div className={styles.content}>
          {activeSection === 'overview' && (
            <Statistics stats={stats} />
          )}
          {activeSection === 'properties' && (
            <MyProperties userId={user.id} />
          )}
          {activeSection === 'add' && (
            <AddProperty userId={user.id} onPropertyAdded={() => {
              setActiveSection('properties');
              loadUserData();
            }} />
          )}
        </div>
      </main>
    </div>
  );
};

export default AgentDashboard;
```

---

## 2. Sidebar.jsx

```jsx
import React from 'react';
import styles from './Dashboard.module.css';

const Sidebar = ({ activeSection, onSelect, user }) => {
  return (
    <aside className={styles.sidebar}>
      <div className={styles.sidebarHeader}>
        <h2>Real Estate Pro</h2>
        <p className={styles.subtitle}>Agent Portal</p>
      </div>

      <nav className={styles.sidebarNav}>
        <button
          className={`${styles.navItem} ${activeSection === 'overview' ? styles.active : ''}`}
          onClick={() => onSelect('overview')}
        >
          <span className={styles.icon}>📊</span>
          <span>Dashboard</span>
        </button>

        <button
          className={`${styles.navItem} ${activeSection === 'properties' ? styles.active : ''}`}
          onClick={() => onSelect('properties')}
        >
          <span className={styles.icon}>🏠</span>
          <span>My Properties</span>
        </button>

        <button
          className={`${styles.navItem} ${activeSection === 'add' ? styles.active : ''}`}
          onClick={() => onSelect('add')}
        >
          <span className={styles.icon}>➕</span>
          <span>Add Property</span>
        </button>
      </nav>

      <div className={styles.sidebarFooter}>
        <button className={styles.logoutBtn} onClick={() => {
          localStorage.removeItem('user');
          localStorage.removeItem('token');
          window.location.href = '/login';
        }}>
          Logout
        </button>
      </div>
    </aside>
  );
};

export default Sidebar;
```

---

## 3. MyProperties.jsx

```jsx
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import PropertyCard from './PropertyCard';
import styles from './Dashboard.module.css';

const MyProperties = ({ userId }) => {
  const [properties, setProperties] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [editingId, setEditingId] = useState(null);

  useEffect(() => {
    fetchProperties();
  }, [page, userId]);

  const fetchProperties = async () => {
    setLoading(true);
    try {
      const response = await axios.get(
        `http://localhost:8080/api/properties/owner/${userId}/paged?page=${page}&size=6`
      );
      
      setProperties(response.data.data.content);
      setTotalPages(response.data.data.totalPages);
    } catch (error) {
      console.error('Error fetching properties:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (propertyId) => {
    if (window.confirm('Are you sure you want to delete this property?')) {
      try {
        await axios.delete(`http://localhost:8080/api/properties/${propertyId}`);
        setProperties(properties.filter(p => p.id !== propertyId));
        alert('Property deleted successfully');
      } catch (error) {
        console.error('Error deleting property:', error);
        alert('Failed to delete property');
      }
    }
  };

  return (
    <div className={styles.propertiesSection}>
      <h2>My Properties ({properties.length})</h2>

      {loading ? (
        <p>Loading properties...</p>
      ) : properties.length === 0 ? (
        <div className={styles.emptyState}>
          <p>No properties yet. Create your first listing!</p>
        </div>
      ) : (
        <>
          <div className={styles.propertiesGrid}>
            {properties.map(property => (
              <PropertyCard
                key={property.id}
                property={property}
                onEdit={() => setEditingId(property.id)}
                onDelete={() => handleDelete(property.id)}
              />
            ))}
          </div>

          {totalPages > 1 && (
            <div className={styles.pagination}>
              <button 
                disabled={page === 0}
                onClick={() => setPage(page - 1)}
              >
                Previous
              </button>
              <span>{page + 1} / {totalPages}</span>
              <button 
                disabled={page >= totalPages - 1}
                onClick={() => setPage(page + 1)}
              >
                Next
              </button>
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default MyProperties;
```

---

## 4. AddProperty.jsx

```jsx
import React, { useState } from 'react';
import axios from 'axios';
import ImageUpload from './ImageUpload';
import styles from './Dashboard.module.css';

const AddProperty = ({ userId, onPropertyAdded }) => {
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    price: '',
    address: '',
    city: '',
    state: '',
    zipCode: '',
    propertyType: 'HOUSE',
    listingType: 'FOR_SALE',
    bedrooms: '',
    bathrooms: '',
    squareFeet: '',
    available: true
  });

  const [propertyId, setPropertyId] = useState(null);
  const [loading, setLoading] = useState(false);
  const [step, setStep] = useState(1); // 1: Details, 2: Images

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type === 'checkbox' ? checked : value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      // Create property with owner_id
      const response = await axios.post(
        'http://localhost:8080/api/properties',
        {
          ...formData,
          owner: { id: userId }
        }
      );

      setPropertyId(response.data.data.id);
      setStep(2); // Move to image upload
    } catch (error) {
      console.error('Error creating property:', error);
      alert('Failed to create property. ' + (error.response?.data?.message || ''));
    } finally {
      setLoading(false);
    }
  };

  const handleImagesComplete = () => {
    onPropertyAdded();
  };

  return (
    <div className={styles.addPropertySection}>
      {step === 1 ? (
        <>
          <h2>Add New Property</h2>
          <form onSubmit={handleSubmit} className={styles.form}>
            {/* Basic Info */}
            <fieldset>
              <legend>Basic Information</legend>
              <input
                type="text"
                name="title"
                placeholder="Property Title"
                value={formData.title}
                onChange={handleChange}
                required
              />
              <textarea
                name="description"
                placeholder="Property Description"
                value={formData.description}
                onChange={handleChange}
                rows={4}
              />
            </fieldset>

            {/* Price & Location */}
            <fieldset>
              <legend>Price & Location</legend>
              <input
                type="number"
                name="price"
                placeholder="Price (INR)"
                value={formData.price}
                onChange={handleChange}
                required
              />
              <input
                type="text"
                name="address"
                placeholder="Street Address"
                value={formData.address}
                onChange={handleChange}
                required
              />
              <input
                type="text"
                name="city"
                placeholder="City"
                value={formData.city}
                onChange={handleChange}
              />
              <input
                type="text"
                name="state"
                placeholder="State"
                value={formData.state}
                onChange={handleChange}
              />
              <input
                type="text"
                name="zipCode"
                placeholder="Zip Code"
                value={formData.zipCode}
                onChange={handleChange}
              />
            </fieldset>

            {/* Property Details */}
            <fieldset>
              <legend>Property Details</legend>
              <select
                name="propertyType"
                value={formData.propertyType}
                onChange={handleChange}
              >
                <option value="HOUSE">House</option>
                <option value="APARTMENT">Apartment</option>
                <option value="CONDO">Condo</option>
                <option value="LAND">Land</option>
                <option value="COMMERCIAL">Commercial</option>
              </select>

              <select
                name="listingType"
                value={formData.listingType}
                onChange={handleChange}
              >
                <option value="FOR_SALE">For Sale</option>
                <option value="FOR_RENT">For Rent</option>
                <option value="FOR_LEASE">For Lease</option>
              </select>

              <input
                type="number"
                name="bedrooms"
                placeholder="Bedrooms"
                value={formData.bedrooms}
                onChange={handleChange}
              />
              <input
                type="number"
                name="bathrooms"
                placeholder="Bathrooms"
                value={formData.bathrooms}
                onChange={handleChange}
              />
              <input
                type="number"
                name="squareFeet"
                placeholder="Square Feet"
                value={formData.squareFeet}
                onChange={handleChange}
              />
            </fieldset>

            {/* Status */}
            <fieldset>
              <label>
                <input
                  type="checkbox"
                  name="available"
                  checked={formData.available}
                  onChange={handleChange}
                />
                Available for sale/rent
              </label>
            </fieldset>

            <button type="submit" disabled={loading} className={styles.submitBtn}>
              {loading ? 'Creating...' : 'Next: Upload Images'}
            </button>
          </form>
        </>
      ) : (
        <ImageUpload
          propertyId={propertyId}
          onComplete={handleImagesComplete}
        />
      )}
    </div>
  );
};

export default AddProperty;
```

---

## 5. ImageUpload.jsx

```jsx
import React, { useState } from 'react';
import axios from 'axios';
import styles from './Dashboard.module.css';

const ImageUpload = ({ propertyId, onComplete }) => {
  const [images, setImages] = useState([]);
  const [uploading, setUploading] = useState(false);
  const [uploadProgress, setUploadProgress] = useState(0);

  const handleFileSelect = (e) => {
    const files = Array.from(e.target.files);
    setImages(files);
  };

  const handleUpload = async () => {
    if (images.length === 0) {
      alert('Please select at least one image');
      return;
    }

    setUploading(true);
    let uploaded = 0;

    try {
      for (const file of images) {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('caption', `Image ${uploaded + 1}`);
        formData.append('isPrimary', uploaded === 0); // First image is primary

        await axios.post(
          `http://localhost:8080/api/properties/${propertyId}/images/upload`,
          formData,
          {
            headers: { 'Content-Type': 'multipart/form-data' }
          }
        );

        uploaded++;
        setUploadProgress(Math.round((uploaded / images.length) * 100));
      }

      alert('All images uploaded successfully!');
      onComplete();
    } catch (error) {
      console.error('Error uploading images:', error);
      alert('Failed to upload images');
    } finally {
      setUploading(false);
    }
  };

  return (
    <div className={styles.imageUploadSection}>
      <h2>Upload Property Images</h2>
      
      <div className={styles.uploadArea}>
        <input
          type="file"
          multiple
          accept="image/*"
          onChange={handleFileSelect}
          disabled={uploading}
        />
        <p>Select multiple images (JPG, PNG, GIF)</p>
      </div>

      {images.length > 0 && (
        <div className={styles.imagePreview}>
          <h3>Selected Images ({images.length})</h3>
          <div className={styles.thumbnails}>
            {images.map((file, idx) => (
              <div key={idx} className={styles.thumbnail}>
                <p>{file.name}</p>
              </div>
            ))}
          </div>
        </div>
      )}

      {uploading && (
        <div className={styles.progressBar}>
          <div style={{ width: `${uploadProgress}%` }}></div>
          <span>{uploadProgress}%</span>
        </div>
      )}

      <div className={styles.actions}>
        <button 
          onClick={handleUpload} 
          disabled={uploading || images.length === 0}
          className={styles.uploadBtn}
        >
          {uploading ? `Uploading... ${uploadProgress}%` : 'Upload Images'}
        </button>
        <button 
          onClick={onComplete}
          className={styles.skipBtn}
        >
          Skip for Now
        </button>
      </div>
    </div>
  );
};

export default ImageUpload;
```

---

## 6. PropertyCard.jsx

```jsx
import React from 'react';
import styles from './Dashboard.module.css';

const PropertyCard = ({ property, onEdit, onDelete }) => {
  const imageUrl = property.images?.length > 0 
    ? property.images[0].imageUrl 
    : 'https://via.placeholder.com/300x200?text=No+Image';

  return (
    <div className={styles.propertyCard}>
      <div className={styles.cardImage}>
        <img src={imageUrl} alt={property.title} />
        <span className={`${styles.badge} ${property.available ? styles.active : styles.inactive}`}>
          {property.available ? 'Active' : 'Inactive'}
        </span>
      </div>

      <div className={styles.cardContent}>
        <h3>{property.title}</h3>
        <p className={styles.address}>{property.address}, {property.city}</p>
        
        <div className={styles.details}>
          <span>{property.bedrooms} Beds</span>
          <span>{property.bathrooms} Baths</span>
          <span>{property.squareFeet} Sq Ft</span>
        </div>

        <div className={styles.price}>₹{property.price?.toLocaleString()}</div>

        <div className={styles.actions}>
          <button onClick={onEdit} className={styles.editBtn}>Edit</button>
          <button onClick={onDelete} className={styles.deleteBtn}>Delete</button>
        </div>
      </div>
    </div>
  );
};

export default PropertyCard;
```

---

## 7. Statistics.jsx

```jsx
import React from 'react';
import styles from './Dashboard.module.css';

const Statistics = ({ stats }) => {
  return (
    <div className={styles.statisticsSection}>
      <h2>Dashboard Overview</h2>
      
      <div className={styles.statsGrid}>
        <div className={styles.statCard}>
          <h3>{stats.totalProperties}</h3>
          <p>Total Properties</p>
        </div>
        <div className={styles.statCard}>
          <h3>{stats.activeListings}</h3>
          <p>Active Listings</p>
        </div>
        <div className={styles.statCard}>
          <h3>{stats.soldProperties}</h3>
          <p>Sold Properties</p>
        </div>
        <div className={styles.statCard}>
          <h3>{stats.totalViews}</h3>
          <p>Total Views</p>
        </div>
      </div>
    </div>
  );
};

export default Statistics;
```

---

## 8. Dashboard.module.css

```css
/* Container */
.dashboardContainer {
  display: flex;
  min-height: 100vh;
  background: #f5f5f5;
}

/* Sidebar */
.sidebar {
  width: 250px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.sidebarHeader h2 {
  margin: 0;
  font-size: 24px;
}

.subtitle {
  margin: 5px 0 0 0;
  opacity: 0.8;
}

.sidebarNav {
  display: flex;
  flex-direction: column;
  margin-top: 30px;
  gap: 10px;
}

.navItem {
  background: rgba(255, 255, 255, 0.1);
  border: none;
  color: white;
  padding: 12px;
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 10px;
  transition: all 0.3s;
}

.navItem:hover,
.navItem.active {
  background: rgba(255, 255, 255, 0.3);
  transform: translateX(5px);
}

.sidebarFooter {
  margin-top: 50px;
}

.logoutBtn {
  width: 100%;
  padding: 10px;
  background: rgba(255, 0, 0, 0.3);
  border: none;
  color: white;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
}

.logoutBtn:hover {
  background: rgba(255, 0, 0, 0.6);
}

/* Main Content */
.mainContent {
  flex: 1;
  padding: 40px;
  overflow-y: auto;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40px;
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.header h1 {
  margin: 0;
  color: #333;
}

.userInfo {
  display: flex;
  flex-direction: column;
  text-align: right;
}

.userInfo span {
  font-weight: 600;
  color: #333;
}

.userInfo small {
  color: #999;
}

.content {
  background: white;
  border-radius: 10px;
  padding: 30px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* Properties Grid */
.propertiesGrid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin: 20px 0;
}

.propertyCard {
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  overflow: hidden;
  transition: all 0.3s;
}

.propertyCard:hover {
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
  transform: translateY(-5px);
}

.cardImage {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.cardImage img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.badge {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 5px 10px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
}

.badge.active {
  background: #4caf50;
  color: white;
}

.badge.inactive {
  background: #f44336;
  color: white;
}

.cardContent {
  padding: 15px;
}

.cardContent h3 {
  margin: 0 0 10px 0;
  color: #333;
}

.address {
  color: #666;
  font-size: 14px;
  margin: 0 0 10px 0;
}

.details {
  display: flex;
  gap: 10px;
  font-size: 12px;
  color: #999;
  margin-bottom: 10px;
}

.price {
  font-size: 18px;
  font-weight: bold;
  color: #667eea;
  margin-bottom: 10px;
}

.actions {
  display: flex;
  gap: 10px;
}

.editBtn,
.deleteBtn {
  flex: 1;
  padding: 8px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s;
}

.editBtn {
  background: #667eea;
  color: white;
}

.editBtn:hover {
  background: #5568d3;
}

.deleteBtn {
  background: #f44336;
  color: white;
}

.deleteBtn:hover {
  background: #da190b;
}

/* Form */
.form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form fieldset {
  border: 1px solid #e0e0e0;
  padding: 20px;
  border-radius: 8px;
}

.form legend {
  font-weight: 600;
  color: #333;
  padding: 0 10px;
}

.form input,
.form textarea,
.form select {
  width: 100%;
  padding: 10px;
  margin: 10px 0;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-family: inherit;
}

.form input:focus,
.form textarea:focus,
.form select:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.submitBtn {
  background: #667eea;
  color: white;
  padding: 12px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 16px;
  font-weight: 600;
  transition: all 0.3s;
}

.submitBtn:hover:not(:disabled) {
  background: #5568d3;
  transform: translateY(-2px);
}

.submitBtn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Image Upload */
.uploadArea {
  border: 2px dashed #667eea;
  border-radius: 8px;
  padding: 30px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.uploadArea:hover {
  background: #f5f5ff;
}

.uploadArea input {
  display: none;
}

.thumbnails {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  gap: 10px;
  margin-top: 20px;
}

.thumbnail {
  background: #f0f0f0;
  padding: 10px;
  border-radius: 6px;
  text-align: center;
  font-size: 12px;
}

.progressBar {
  width: 100%;
  height: 20px;
  background: #e0e0e0;
  border-radius: 10px;
  overflow: hidden;
  margin: 20px 0;
  position: relative;
}

.progressBar div {
  height: 100%;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  transition: width 0.3s;
}

.progressBar span {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: white;
  font-size: 12px;
  font-weight: 600;
}

/* Statistics */
.statsGrid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.statCard {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 20px;
  border-radius: 10px;
  text-align: center;
}

.statCard h3 {
  margin: 0;
  font-size: 32px;
}

.statCard p {
  margin: 10px 0 0 0;
  opacity: 0.9;
}

/* Pagination */
.pagination {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 20px;
}

.pagination button {
  padding: 8px 16px;
  border: 1px solid #ddd;
  background: white;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
}

.pagination button:hover:not(:disabled) {
  background: #667eea;
  color: white;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* Empty State */
.emptyState {
  text-align: center;
  padding: 40px;
  color: #999;
}

/* Responsive */
@media (max-width: 768px) {
  .dashboardContainer {
    flex-direction: column;
  }

  .sidebar {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .sidebarNav {
    flex-direction: row;
    margin-top: 0;
  }

  .mainContent {
    padding: 20px;
  }

  .header {
    flex-direction: column;
    text-align: left;
  }

  .propertiesGrid {
    grid-template-columns: 1fr;
  }
}
```

---

## IMPLEMENTATION CHECKLIST

- [ ] Create AgentDashboard.jsx
- [ ] Create Sidebar.jsx
- [ ] Create MyProperties.jsx
- [ ] Create PropertyCard.jsx
- [ ] Create AddProperty.jsx
- [ ] Create ImageUpload.jsx
- [ ] Create Statistics.jsx
- [ ] Create Dashboard.module.css
- [ ] Add route in App.js
- [ ] Create ProtectedRoute component
- [ ] Test property creation
- [ ] Test image upload
- [ ] Test property deletion
- [ ] Test on mobile devices
