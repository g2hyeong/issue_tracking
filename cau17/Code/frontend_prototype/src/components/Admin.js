import React from 'react';
import Sidebar from './AdminSidebar';
import Setting from './AdminProjectSettings';
import UserManagement from './AdminUserManagement';
import Statistic from './AdminStatistic';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';


//import './Admin.css';

const Admin = () => {
  return (
    <div className="main-layout">
      <Sidebar />
      <Routes>
        <Route path="settings" element={<Setting />} />
        <Route path="users" element={<UserManagement />} />
        <Route path="statistic" element={<Statistic />} />
        {/* Default route or other routes can go here */}
      </Routes>
    </div>
  );
};


export default Admin;