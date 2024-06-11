import React from 'react';
import { useNavigate } from 'react-router-dom';
import { AppBar, Toolbar, Typography, Button, Paper, Box, Container } from '@mui/material';
import SearchBar from './SearchBar';

const ViewIssues = () => {
  const navigate = useNavigate();

  return (
    <div>      
      <Container sx={{ mt: 4 }}>        
        <Paper elevation={3} sx={{ mt: 4, p: 2, textAlign: 'center' }}>
          <Typography variant="h4" component="div">
            Welcome to the Issue Tracker
          </Typography>
          <Typography variant="body1" component="div" sx={{ mt: 2 }}>
            Here you can manage all your projects and tasks efficiently.
          </Typography>
        </Paper>
      </Container>
    </div>
  );
};

export default ViewIssues;
