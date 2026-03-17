import React, { useState, useEffect } from 'react';
import api from '../api/api';
import { Search, Info, CheckCircle2, Clock, XCircle } from 'lucide-react';

const FarmerDashboard = () => {
    const [cities, setCities] = useState([]);
    const [selectedCity, setSelectedCity] = useState('');
    const [storages, setStorages] = useState([]);
    const [bookings, setBookings] = useState([]);
    const [loading, setLoading] = useState(false);

    // Booking Form State
    const [bookingModal, setBookingModal] = useState({ show: false, storage: null });
    const [bookingForm, setBookingForm] = useState({
        produceType: 'Onions',
        quantity: '',
        startDate: '',
        endDate: ''
    });
    const [bookingError, setBookingError] = useState('');
    const [bookingSuccess, setBookingSuccess] = useState('');

    const produceOptions = ['Grapes', 'Onions', 'Pomegranate', 'Tomatoes', 'Mangoes', 'Oranges', 'Bananas'];

    useEffect(() => {
        fetchCities();
        fetchMyBookings();
    }, []);

    const fetchCities = async () => {
        try {
            const res = await api.get('/cities');
            if (res.data.success) setCities(res.data.data);
        } catch (err) {
            console.error('Failed to load cities');
        }
    };

    const fetchMyBookings = async () => {
        try {
            const res = await api.get('/bookings/user');
            if (res.data.success) setBookings(res.data.data);
        } catch (err) {
            console.error('Failed to load bookings');
        }
    };

    const handleSearch = async (e) => {
        e.preventDefault();
        if (!selectedCity) return;

        setLoading(true);
        try {
            const res = await api.get(`/storages/city?city=${selectedCity}`);
            if (res.data.success) setStorages(res.data.data);
        } catch (err) {
            console.error('Search failed');
        } finally {
            setLoading(false);
        }
    };

    const handleBook = async (e) => {
        e.preventDefault();
        setBookingError('');
        setBookingSuccess('');

        try {
            const payload = { ...bookingForm, storageId: bookingModal.storage.id };
            const res = await api.post('/bookings', payload);

            if (res.data.success) {
                setBookingSuccess('Booking successful!');
                fetchMyBookings();
                setTimeout(() => {
                    setBookingModal({ show: false, storage: null });
                    setBookingSuccess('');
                    setBookingForm({ produceType: 'Onions', quantity: '', startDate: '', endDate: '' });
                    handleSearch({ preventDefault: () => { } }); // refresh storages to update capacities
                }, 1500);
            }
        } catch (err) {
            setBookingError(err.response?.data?.message || 'Failed to create booking. Capacity issue?');
        }
    };

    const getStatusIcon = (status) => {
        switch (status) {
            case 'CONFIRMED': return <CheckCircle2 className="w-5 h-5 text-green-500" />;
            case 'CANCELLED': return <XCircle className="w-5 h-5 text-red-500" />;
            default: return <Clock className="w-5 h-5 text-yellow-500" />;
        }
    };

    return (
        <div className="space-y-8">
            {/* Search Header */}
            <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
                <h2 className="text-xl font-bold text-gray-900 mb-4">Find Cold Storage</h2>
                <form onSubmit={handleSearch} className="flex gap-4 items-end">
                    <div className="flex-1 max-w-sm">
                        <label className="block text-sm font-medium text-gray-700 mb-1">Select City</label>
                        <select
                            value={selectedCity}
                            onChange={(e) => setSelectedCity(e.target.value)}
                            className="mt-1 block w-full pl-3 pr-10 py-2.5 text-base border-gray-300 focus:outline-none focus:ring-green-500 focus:border-green-500 sm:text-sm rounded-md shadow-sm border"
                        >
                            <option value="">Choose a city...</option>
                            {cities.map(city => (
                                <option key={city.id} value={city.name}>{city.name}</option>
                            ))}
                        </select>
                    </div>
                    <button
                        type="submit"
                        disabled={!selectedCity || loading}
                        className="inline-flex items-center px-6 py-2.5 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 disabled:opacity-50"
                    >
                        <Search className="w-4 h-4 mr-2" />
                        {loading ? 'Searching...' : 'Search'}
                    </button>
                </form>
            </div>

            {/* Storage Results */}
            {storages.length > 0 && (
                <div>
                    <h3 className="text-lg font-bold text-gray-900 mb-4">Available Facilities in {selectedCity}</h3>
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {storages.map(storage => (
                            <div key={storage.id} className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden hover:shadow-md transition-shadow">
                                <div className="p-6">
                                    <h4 className="text-xl font-bold text-gray-900 mb-1">{storage.name}</h4>
                                    <p className="text-sm text-gray-500 mb-4">Owner: {storage.ownerName}</p>

                                    <div className="space-y-2 mb-6">
                                        <div className="flex justify-between text-sm">
                                            <span className="text-gray-500">Available Capacity:</span>
                                            <span className="font-semibold text-green-600">{storage.availableCapacity} / {storage.totalCapacity} tons</span>
                                        </div>
                                        <div className="flex justify-between text-sm">
                                            <span className="text-gray-500">Temperature Range:</span>
                                            <span className="font-medium">{storage.temperatureMin}°C to {storage.temperatureMax}°C</span>
                                        </div>
                                    </div>

                                    <button
                                        onClick={() => setBookingModal({ show: true, storage })}
                                        className="w-full justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500"
                                    >
                                        Book Space
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            )}

            {/* My Bookings Table */}
            <div>
                <h3 className="text-lg font-bold text-gray-900 mb-4">My Dashboard</h3>
                {bookings.length === 0 ? (
                    <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100 text-center text-gray-500">
                        <Info className="w-8 h-8 mx-auto text-gray-400 mb-2" />
                        <p>You have no bookings yet. Search a city above to get started.</p>
                    </div>
                ) : (
                    <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
                        <table className="min-w-full divide-y divide-gray-200">
                            <thead className="bg-gray-50">
                                <tr>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Facility</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Produce</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Quantity</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Dates</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                                </tr>
                            </thead>
                            <tbody className="bg-white divide-y divide-gray-200">
                                {bookings.map(booking => (
                                    <tr key={booking.id}>
                                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{booking.storageName}</td>
                                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{booking.produceType}</td>
                                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{booking.quantity} tons</td>
                                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                            {booking.startDate} to {booking.endDate}
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap">
                                            <div className="flex items-center gap-1">
                                                {getStatusIcon(booking.status)}
                                                <span className="text-sm font-medium text-gray-700">{booking.status}</span>
                                            </div>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>

            {/* Booking Modal */}
            {bookingModal.show && (
                <div className="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center p-4 z-50">
                    <div className="bg-white rounded-xl w-full max-w-md shadow-xl overflow-hidden">
                        <div className="px-6 py-4 border-b border-gray-200 flex justify-between items-center">
                            <h3 className="text-lg font-medium text-gray-900">Book {bookingModal.storage?.name}</h3>
                            <button
                                onClick={() => setBookingModal({ show: false, storage: null })}
                                className="text-gray-400 hover:text-gray-500"
                            >
                                <span className="sr-only">Close</span>
                                <XCircle className="h-6 w-6" />
                            </button>
                        </div>

                        <form onSubmit={handleBook} className="p-6 space-y-4">
                            {bookingError && <div className="p-3 bg-red-50 text-red-700 text-sm rounded-md">{bookingError}</div>}
                            {bookingSuccess && <div className="p-3 bg-green-50 text-green-700 text-sm rounded-md">{bookingSuccess}</div>}

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Produce Type</label>
                                <select
                                    required
                                    value={bookingForm.produceType}
                                    onChange={e => setBookingForm({ ...bookingForm, produceType: e.target.value })}
                                    className="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-green-500 focus:border-green-500 sm:text-sm rounded-md border shadow-sm"
                                >
                                    {produceOptions.map(opt => <option key={opt} value={opt}>{opt}</option>)}
                                </select>
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Quantity (tons)</label>
                                <input
                                    type="number"
                                    step="0.1"
                                    min="0.1"
                                    max={bookingModal.storage?.availableCapacity}
                                    required
                                    value={bookingForm.quantity}
                                    onChange={e => setBookingForm({ ...bookingForm, quantity: e.target.value })}
                                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500 sm:text-sm"
                                    placeholder={`Max: ${bookingModal.storage?.availableCapacity}`}
                                />
                            </div>

                            <div className="grid grid-cols-2 gap-4">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Start Date</label>
                                    <input
                                        type="date"
                                        required
                                        value={bookingForm.startDate}
                                        onChange={e => setBookingForm({ ...bookingForm, startDate: e.target.value })}
                                        className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500 sm:text-sm"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">End Date</label>
                                    <input
                                        type="date"
                                        required
                                        value={bookingForm.endDate}
                                        onChange={e => setBookingForm({ ...bookingForm, endDate: e.target.value })}
                                        className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500 sm:text-sm"
                                    />
                                </div>
                            </div>

                            <div className="pt-4 flex gap-3">
                                <button
                                    type="button"
                                    onClick={() => setBookingModal({ show: false, storage: null })}
                                    className="flex-1 py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none"
                                >
                                    Cancel
                                </button>
                                <button
                                    type="submit"
                                    className="flex-1 py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700 focus:outline-none flex justify-center"
                                >
                                    Confirm Booking
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default FarmerDashboard;
