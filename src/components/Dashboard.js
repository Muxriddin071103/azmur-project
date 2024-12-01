import React, { useState, useEffect } from "react";
import "./Dashboard.css";

const Dashboard = () => {
    const [dropdownVisible, setDropdownVisible] = useState(false);
    const [user, setUser] = useState({});

    useEffect(() => {
        const storedUser = localStorage.getItem("user");
        if (storedUser) {
            setUser(JSON.parse(storedUser));
        }
    }, []);

    const logout = () => {
        localStorage.removeItem("authToken");
        localStorage.removeItem("user");
        window.location.href = "/";
    };

    const toggleDropdown = () => {
        setDropdownVisible(!dropdownVisible);
    };

    return (
        <div className="dashboard">
            {/* Background Video */}
            <video autoPlay muted loop className="background-video">
                <source src="/assets/background-video.mp4" type="video/mp4" />
                Your browser does not support the video tag.
            </video>

            {/* Sidebar */}
            <div className="sidebar">
                <h2>Auto Parts Store</h2>
                <ul>
                    <li><a href="/dashboard">Dashboard</a></li>
                    <li><a href="#">Products</a></li>
                    <li><a href="#">Inventory</a></li>
                    <li><a href="#">Orders</a></li>
                    <li><a href="#">Customers</a></li>
                    <li><a href="#">Reports</a></li>
                </ul>
            </div>

            {/* Main Content */}
            <div className="main-content">
                <div className="top-section">
                    {/* Search Bar */}
                    <div className="search-bar">
                        <input type="text" placeholder="Search for parts, orders..." />
                    </div>

                    {/* User Info */}
                    <div className="user-info" onClick={toggleDropdown}>
                        <img
                            src="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png"
                            alt="User"
                            className="profile-pic"
                        />
                        <span>{user.firstName ? `${user.firstName} ${user.lastName}` : 'Guest'}</span>

                        {/* Dropdown Menu */}
                        {dropdownVisible && (
                            <div className="profile-options-dropdown">
                                <ul>
                                    <li><a href="#">Edit Profile</a></li>
                                    <li><a href="#" onClick={logout}>Logout</a></li>
                                </ul>
                            </div>
                        )}
                    </div>
                </div>

                {/* Dashboard Overview */}
                <div className="dashboard-overview">
                    <div className="overview-item" id="sales">
                        <div className="icon">
                            <i className="fas fa-dollar-sign"></i>
                        </div>
                        <div className="metric">
                            <h3>Total Sales</h3>
                            <p>$25,000</p>
                        </div>
                    </div>
                    <div className="overview-item" id="orders">
                        <div className="icon">
                            <i className="fas fa-box"></i>
                        </div>
                        <div className="metric">
                            <h3>New Orders</h3>
                            <p>150 Orders</p>
                        </div>
                    </div>
                    <div className="overview-item" id="stock">
                        <div className="icon">
                            <i className="fas fa-cogs"></i>
                        </div>
                        <div className="metric">
                            <h3>Available Stock</h3>
                            <p>1,200 Items</p>
                        </div>
                    </div>
                    <div className="overview-item" id="customers">
                        <div className="icon">
                            <i className="fas fa-users"></i>
                        </div>
                        <div className="metric">
                            <h3>Active Customers</h3>
                            <p>350 Customers</p>
                        </div>
                    </div>
                </div>

                <div className="footer">
                    <p>© 2024 Auto Parts Store. All rights reserved.</p>
                </div>
            </div>
        </div>
    );
};

export default Dashboard;
