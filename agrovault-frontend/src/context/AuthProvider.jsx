import React, { useEffect, useState } from 'react';
import api from '../api/api';
import { AuthContext } from './AuthContext';

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(() => Boolean(localStorage.getItem('agrovault_token')));

    useEffect(() => {
        const token = localStorage.getItem('agrovault_token');
        if (!token) {
            return;
        }

        api.get('/auth/me')
            .then((res) => {
                if (res.data.success) {
                    setUser(res.data.data);
                    return;
                }
                localStorage.removeItem('agrovault_token');
            })
            .catch(() => {
                localStorage.removeItem('agrovault_token');
            })
            .finally(() => setLoading(false));
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
