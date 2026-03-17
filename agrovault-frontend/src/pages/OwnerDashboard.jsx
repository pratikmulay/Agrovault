import React, { useState, useEffect } from 'react';
import api from '../api/api';
import { PlusCircle, Thermometer, Edit2, XCircle } from 'lucide-react';

const OwnerDashboard = () => {
    const [storages, setStorages] = useState([]);
    const [cities, setCities] = useState([]);
    const [loading, setLoading] = useState(false);

    // Modals state
    const [showAddModal, setShowAddModal] = useState(false);
    const [addForm, setAddForm] = useState({ name: '', cityId: '', totalCapacity: '', temperatureMin: '', temperatureMax: '' });
    const [apiError, setApiError] = useState('');

    useEffect(() => {
        fetchMyStorages();
        fetchCities();
    }, []);

    const fetchMyStorages = async () => {
        try {
            const res = await api.get('/storages/owner');
            if (res.data.success) setStorages(res.data.data);
        } catch (err) {
            console.error('Failed to load owned storages');
        }
    };

    const fetchCities = async () => {
        try {
            const res = await api.get('/cities');
            if (res.data.success) setCities(res.data.data);
        } catch (err) {
            console.error('Failed to fetch cities');
        }
    };

    const handleAddStorage = async (e) => {
        e.preventDefault();
        setApiError('');
        try {
            const payload = { ...addForm, cityId: parseInt(addForm.cityId) };
            const res = await api.post('/storages', payload);
            if (res.data.success) {
                fetchMyStorages();
                setShowAddModal(false);
                setAddForm({ name: '', cityId: '', totalCapacity: '', temperatureMin: '', temperatureMax: '' });
            }
        } catch (err) {
            setApiError(err.response?.data?.message || 'Failed to add storage');
        }
    };

    const handleUpdateCapacity = async (storage) => {
        const newCap = window.prompt(`Enter new available capacity for ${storage.name}. (Current: ${storage.availableCapacity} / ${storage.totalCapacity})`);
        if (newCap && !isNaN(newCap)) {
            try {
                await api.put(`/storages/${storage.id}/capacity`, { availableCapacity: parseFloat(newCap) });
                fetchMyStorages();
            } catch (err) {
                alert('Capacity update failed.');
            }
        }
    };

    const handleLogTemp = async (storage) => {
        const temp = window.prompt(`Enter current temperature for ${storage.name}:`);
        const hum = window.prompt(`Enter current humidity (%) for ${storage.name}:`);

        if (temp && hum) {
            try {
                await api.post('/temperature-logs', {
                    storageId: storage.id,
                    temperature: parseFloat(temp),
                    humidity: parseFloat(hum)
                });
                alert('Temperature logged successfully!');
            } catch (err) {
                alert('Temperature logging failed.');
            }
        }
    };

    return (
        <div className="space-y-8">
            <div className="flex justify-between items-center bg-white p-6 rounded-xl shadow-sm border border-gray-100">
                <div>
                    <h2 className="text-xl font-bold text-gray-900">My Storages</h2>
                    <p className="text-sm text-gray-500 mt-1">Manage your active cold storage facilities</p>
                </div>
                <button
                    onClick={() => setShowAddModal(true)}
                    className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-green-600 hover:bg-green-700 focus:outline-none"
                >
                    <PlusCircle className="w-5 h-5 mr-2" />
                    Add Storage
                </button>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {storages.map(storage => (
                    <div key={storage.id} className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden relative">
                        <div className="p-6">
                            <h4 className="text-xl font-bold text-gray-900 mb-1">{storage.name}</h4>
                            <p className="text-sm text-gray-500 mb-4">{storage.cityName}</p>

                            <div className="space-y-2 mb-6">
                                <div className="flex justify-between text-sm">
                                    <span className="text-gray-500">Available:</span>
                                    <span className="font-semibold">{storage.availableCapacity} / {storage.totalCapacity} tons</span>
                                </div>
                                <div className="flex justify-between text-sm">
                                    <span className="text-gray-500">Range:</span>
                                    <span className="font-medium">{storage.temperatureMin}°C to {storage.temperatureMax}°C</span>
                                </div>
                            </div>

                            <div className="flex gap-3">
                                <button
                                    onClick={() => handleUpdateCapacity(storage)}
                                    className="flex-1 flex justify-center items-center py-2 px-3 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50 transition-colors"
                                >
                                    <Edit2 className="w-4 h-4 mr-2" />
                                    Capacity
                                </button>
                                <button
                                    onClick={() => handleLogTemp(storage)}
                                    className="flex-1 flex justify-center items-center py-2 px-3 border border-transparent rounded-md text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 transition-colors"
                                >
                                    <Thermometer className="w-4 h-4 mr-2" />
                                    Log Temp
                                </button>
                            </div>
                        </div>

                        <div className="absolute top-0 right-0 h-10 w-10 flex items-center justify-center bg-green-50 rounded-bl-xl border-b border-l border-green-100 text-green-700 font-bold text-xs">
                            Live
                        </div>
                    </div>
                ))}
            </div>

            {showAddModal && (
                <div className="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center p-4 z-50">
                    <div className="bg-white rounded-xl w-full max-w-lg shadow-xl overflow-hidden">
                        <div className="px-6 py-4 border-b border-gray-200 flex justify-between items-center">
                            <h3 className="text-lg font-medium text-gray-900">Add New Storage</h3>
                            <button
                                onClick={() => setShowAddModal(false)}
                                className="text-gray-400 hover:text-gray-500"
                            >
                                <XCircle className="h-6 w-6" />
                            </button>
                        </div>

                        <form onSubmit={handleAddStorage} className="p-6 space-y-4">
                            {apiError && <div className="p-3 bg-red-50 text-red-700 text-sm rounded-md">{apiError}</div>}

                            <div className="grid grid-cols-2 gap-4">
                                <div className="col-span-2">
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Facility Name</label>
                                    <input
                                        required
                                        type="text"
                                        value={addForm.name}
                                        onChange={e => setAddForm({ ...addForm, name: e.target.value })}
                                        className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 sm:text-sm"
                                    />
                                </div>

                                <div className="col-span-2">
                                    <label className="block text-sm font-medium text-gray-700 mb-1">City</label>
                                    <select
                                        required
                                        value={addForm.cityId}
                                        onChange={e => setAddForm({ ...addForm, cityId: e.target.value })}
                                        className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 sm:text-sm"
                                    >
                                        <option value="">Select city</option>
                                        {cities.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
                                    </select>
                                </div>

                                <div className="col-span-2">
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Total Capacity (tons)</label>
                                    <input
                                        required
                                        type="number"
                                        value={addForm.totalCapacity}
                                        onChange={e => setAddForm({ ...addForm, totalCapacity: e.target.value })}
                                        className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 sm:text-sm"
                                    />
                                </div>

                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Min Temp (°C)</label>
                                    <input
                                        required
                                        type="number"
                                        step="0.1"
                                        value={addForm.temperatureMin}
                                        onChange={e => setAddForm({ ...addForm, temperatureMin: e.target.value })}
                                        className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 sm:text-sm"
                                    />
                                </div>

                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Max Temp (°C)</label>
                                    <input
                                        required
                                        type="number"
                                        step="0.1"
                                        value={addForm.temperatureMax}
                                        onChange={e => setAddForm({ ...addForm, temperatureMax: e.target.value })}
                                        className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 sm:text-sm"
                                    />
                                </div>
                            </div>

                            <div className="pt-4 flex justify-end gap-3">
                                <button
                                    type="button"
                                    onClick={() => setShowAddModal(false)}
                                    className="py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none"
                                >
                                    Cancel
                                </button>
                                <button
                                    type="submit"
                                    className="py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700 focus:outline-none"
                                >
                                    Save Facility
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default OwnerDashboard;
