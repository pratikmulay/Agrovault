import React, { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
import { LogOut, Warehouse } from 'lucide-react';

const Navbar = () => {
    const { user, logout } = useContext(AuthContext);

    if (!user) return null;

    return (
        <nav className="bg-white border-b border-gray-200 sticky top-0 z-50">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex justify-between h-16">
                    <div className="flex items-center gap-2">
                        <Warehouse className="h-6 w-6 text-green-600" />
                        <span className="font-bold text-xl text-gray-900 tracking-tight">AgroVault</span>
                        <span className="ml-2 px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                            {user.role}
                        </span>
                    </div>
                    <div className="flex items-center gap-4">
                        <span className="text-sm text-gray-700 font-medium">{user.name}</span>
                        <button
                            onClick={logout}
                            className="inline-flex items-center justify-center p-2 rounded-md text-gray-500 hover:text-red-600 hover:bg-red-50 focus:outline-none transition-colors"
                            title="Logout"
                        >
                            <LogOut className="h-5 w-5" />
                        </button>
                    </div>
                </div>
            </div>
        </nav>
    );
};

export default Navbar;
