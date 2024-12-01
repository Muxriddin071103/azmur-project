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

                const { token } = data;
                localStorage.setItem('authToken', token);
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
