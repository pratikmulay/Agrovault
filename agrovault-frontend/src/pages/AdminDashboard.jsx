import React, { useState, useEffect } from 'react';
import api from '../api/api';
import { CheckCircle2, Clock, XCircle, ShieldCheck } from 'lucide-react';

const AdminDashboard = () => {
    const [bookings, setBookings] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchBookings();
    }, []);

    const fetchBookings = async () => {
        try {
            const res = await api.get('/bookings/all');
            if (res.data.success) {
                setBookings(res.data.data);
            }
        } catch {
            console.error('Failed to load bookings');
        } finally {
            setLoading(false);
        }
    };

    const handleStatusUpdate = async (bookingId, newStatus) => {
        try {
            await api.put(`/bookings/${bookingId}/status?status=${newStatus}`);
            fetchBookings();
        } catch {
            alert('Failed to update status');
        }
    };

    const getStatusIcon = (status) => {
        switch (status) {
            case 'CONFIRMED': return <CheckCircle2 className="w-5 h-5 text-green-500" />;
            case 'CANCELLED': return <XCircle className="w-5 h-5 text-red-500" />;
            case 'COMPLETED': return <CheckCircle2 className="w-5 h-5 text-blue-500" />;
            default: return <Clock className="w-5 h-5 text-yellow-500" />;
        }
    };

    return (
        <div className="space-y-8">
            <div className="flex items-center gap-3 bg-white p-6 rounded-xl shadow-sm border border-gray-100 mb-8">
                <div className="bg-red-100 p-3 rounded-full">
                    <ShieldCheck className="w-6 h-6 text-red-600" />
                </div>
                <div>
                    <h2 className="text-xl font-bold text-gray-900">Admin Control Panel</h2>
                    <p className="text-sm text-gray-500">Manage all platform bookings</p>
                </div>
            </div>

            <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
                {loading ? (
                    <div className="p-8 text-center text-gray-500">Loading bookings...</div>
                ) : bookings.length === 0 ? (
                    <div className="p-8 text-center text-gray-500">No bookings exist on the platform.</div>
                ) : (
                    <div className="overflow-x-auto">
                        <table className="min-w-full divide-y divide-gray-200">
                            <thead className="bg-gray-50">
                                <tr>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Facility</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Details</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                                    <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Action</th>
                                </tr>
                            </thead>
                            <tbody className="bg-white divide-y divide-gray-200">
                                {bookings.map(booking => (
                                    <tr key={booking.id} className="hover:bg-gray-50 transition-colors">
                                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                            {new Date(booking.createdAt).toLocaleDateString()}
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap">
                                            <div className="text-sm font-medium text-gray-900">{booking.storageName}</div>
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap">
                                            <div className="text-sm text-gray-900">{booking.produceType}</div>
                                            <div className="text-sm text-gray-500">{booking.quantity} tons</div>
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap">
                                            <div className="flex items-center gap-1.5">
                                                {getStatusIcon(booking.status)}
                                                <span className="text-sm font-medium text-gray-700">{booking.status}</span>
                                            </div>
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                                            <select
                                                className="mt-1 block w-full pl-3 pr-8 py-1.5 text-sm border-gray-300 focus:outline-none focus:ring-green-500 focus:border-green-500 rounded-md border"
                                                value={booking.status}
                                                onChange={(e) => handleStatusUpdate(booking.id, e.target.value)}
                                            >
                                                <option value="PENDING">PENDING</option>
                                                <option value="CONFIRMED">CONFIRMED</option>
                                                <option value="COMPLETED">COMPLETED</option>
                                                <option value="CANCELLED">CANCELLED</option>
                                            </select>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>
        </div>
    );
};

export default AdminDashboard;
