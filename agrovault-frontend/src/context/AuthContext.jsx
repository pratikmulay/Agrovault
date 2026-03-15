import React, { createContext, useState, useEffect } from 'react';
import api from '../api/api';

export const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const token = localStorage.getItem('agrovault_token');
        if (token) {
            api.get('/auth/me')
                .then((res) => {
                    if (res.data.success) {
                        setUser(res.data.data);
                    } else {
                        localStorage.removeItem('agrovault_token');
                    }
                })
                .catch(() => {
                    localStorage.removeItem('agrovault_token');
                })
                .finally(() => setLoading(false));
        } else {
            setLoading(false);
        }
    }, []);

    const login = (userData, token) => {
        localStorage.setItem('agrovault_token', token);
        setUser(userData);
    };

    const logout = () => {
        localStorage.removeItem('agrovault_token');
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, login, logout, loading }}>
            {children}
        </AuthContext.Provider>
    );
};
