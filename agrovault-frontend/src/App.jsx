import React, { useContext } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, AuthContext } from './context/AuthContext';
import Navbar from './components/Navbar';
import Auth from './pages/Auth';
import FarmerDashboard from './pages/FarmerDashboard';
import OwnerDashboard from './pages/OwnerDashboard';
import AdminDashboard from './pages/AdminDashboard';

const ProtectedRoute = ({ children, allowedRole }) => {
  const { user, loading } = useContext(AuthContext);

  if (loading) return <div className="min-h-screen flex items-center justify-center">Loading...</div>;

  if (!user) return <Navigate to="/" replace />;
  if (allowedRole && user.role !== allowedRole) return <Navigate to="/" replace />;

  return (
    <>
      <Navbar />
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {children}
      </div>
    </>
  );
};

const AppRoutes = () => {
  const { user, loading } = useContext(AuthContext);

  if (loading) return <div className="min-h-screen flex items-center justify-center">Loading...</div>;

  return (
    <Routes>
      <Route
        path="/"
        element={
          user ? (
            user.role === 'FARMER' ? <Navigate to="/farmer" replace /> :
              user.role === 'STORAGE_OWNER' ? <Navigate to="/owner" replace /> :
                <Navigate to="/admin" replace />
          ) : <Auth />
        }
      />
      <Route
        path="/farmer"
        element={<ProtectedRoute allowedRole="FARMER"><FarmerDashboard /></ProtectedRoute>}
      />
      <Route
        path="/owner"
        element={<ProtectedRoute allowedRole="STORAGE_OWNER"><OwnerDashboard /></ProtectedRoute>}
      />
      <Route
        path="/admin"
        element={<ProtectedRoute allowedRole="ADMIN"><AdminDashboard /></ProtectedRoute>}
      />
    </Routes>
  );
};

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <AppRoutes />
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
