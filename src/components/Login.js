import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Login.css';

const Login = () => {
    const [formData, setFormData] = useState({ username: '', password: '' });
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({ ...prevData, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch('http://localhost:8080/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData),
            });

            if (response.ok) {
                const contentType = response.headers.get('content-type');
                let data;

                if (contentType && contentType.includes('application/json')) {
                    data = await response.json();
                } else {
                    const token = await response.text();
                    data = { token };
                }

                const { access_token, refresh_token } = data;

                localStorage.setItem('authToken', access_token);

                document.cookie = `refresh_token=${refresh_token}; HttpOnly; Secure; SameSite=Strict;`;

                navigate('/dashboard');
            } else {
                const errorData = await response.json();
                setError(errorData.message || 'Login failed. Check your credentials.');
            }
        } catch (error) {
            console.error('Error:', error);
            setError('Something went wrong. Please try again later.');
        }
    };

    const getCookie = (name) => {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
        return null;
    };

    const refreshAccessToken = async () => {
        const refreshToken = getCookie('refresh_token'); // Get refresh token from cookies

        if (!refreshToken) {
            console.error('No refresh token found');
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/auth/refresh', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ refresh_token: refreshToken }),
            });

            if (response.ok) {
                const data = await response.json();
                const { access_token } = data;

                localStorage.setItem('authToken', access_token);
                console.log('Access token refreshed');
            } else {
                console.error('Failed to refresh access token');
            }
        } catch (error) {
            console.error('Error refreshing token:', error);
        }
    };

    const fetchWithAuth = async (url, options) => {
        const accessToken = localStorage.getItem('authToken');

        if (accessToken) {
            options.headers = {
                ...options.headers,
                Authorization: `Bearer ${accessToken}`,
            };
        }

        const response = await fetch(url, options);

        if (response.status === 401) {
            await refreshAccessToken();

            const newAccessToken = localStorage.getItem('authToken');
            options.headers = {
                ...options.headers,
                Authorization: `Bearer ${newAccessToken}`,
            };

            return fetch(url, options);
        }

        return response;
    };

    return (
        <section className="form-container">
            <div className="form-box">
                <div className="form-value">
                    <form id="loginForm" onSubmit={handleSubmit}>
                        <h2>Login</h2>
                        <div className="inputbox">
                            <ion-icon name="mail-outline"></ion-icon>
                            <input
                                type="text"
                                id="username"
                                name="username"
                                value={formData.username}
                                onChange={handleChange}
                                required
                            />
                            <label htmlFor="username">Username</label>
                        </div>
                        <div className="inputbox">
                            <ion-icon name="lock-closed-outline"></ion-icon>
                            <input
                                type="password"
                                id="password"
                                name="password"
                                value={formData.password}
                                onChange={handleChange}
                                required
                            />
                            <label htmlFor="password">Password</label>
                        </div>
                        {error && <p className="error-message">{error}</p>}
                        <button type="submit">Log in</button>
                    </form>
                </div>
            </div>
        </section>
    );
};

export default Login;
