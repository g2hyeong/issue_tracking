import React from 'react';
import { Link } from 'react-router-dom';
import { Box, List, ListItem, ListItemText, Typography, Divider } from '@mui/material';
import HomeIcon from '@mui/icons-material/Home';
import SettingsIcon from '@mui/icons-material/Settings';
import GroupIcon from '@mui/icons-material/Group';
import BarChartIcon from '@mui/icons-material/BarChart';

const AdminSidebar = () => {
  return (
    <Box
      sx={{
        width: 250,
        height: '100vh',
        backgroundColor: '#2c3e50',
        color: 'white',
        padding: 2,
        boxShadow: '2px 0 5px rgba(0, 0, 0, 0.1)',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'space-between',
      }}
    >
      <Box>
        <Typography variant="h6" sx={{ mb: 2, color: 'white' }}>
          Admin Panel
        </Typography>
        <Divider sx={{ borderColor: 'rgba(255, 255, 255, 0.2)', mb: 2 }} />
        <List>
          <ListItem button component={Link} to="/admin/settings">
            <SettingsIcon sx={{ mr: 2 }} />
            <ListItemText primary="Settings" sx={{ color: 'white' }} />
          </ListItem>
          <ListItem button component={Link} to="/admin/users">
            <GroupIcon sx={{ mr: 2 }} />
            <ListItemText primary="Users" sx={{ color: 'white' }} />
          </ListItem>
          <ListItem button component={Link} to="/admin/statistic">
            <BarChartIcon sx={{ mr: 2 }} />
            <ListItemText primary="Statistic" sx={{ color: 'white' }} />
          </ListItem>
        </List>
      </Box>
      <Box>
        <Divider sx={{ borderColor: 'rgba(255, 255, 255, 0.2)', mb: 2 }} />
        <ListItem button component={Link} to="/">
          <HomeIcon sx={{ mr: 2 }} />
          <ListItemText primary="Home" sx={{ color: 'white' }} />
        </ListItem>
      </Box>
    </Box>
  );
};

export default AdminSidebar;