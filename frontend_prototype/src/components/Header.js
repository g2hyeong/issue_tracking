import React from 'react';
import { AppBar, Toolbar, Typography, Button, Box, Link } from '@mui/material';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import Login from './Login';

const Header = ({ isLoggedIn, isAdmin, loggedInUser, onLogout, onLogin }) => {
  return (
    <AppBar position="static" sx={{ backgroundColor: '#333', padding: '0.5rem 0' }}>
      <Toolbar>
        <Typography variant="h6" component="div" sx={{ flexGrow: 1, color: 'white', fontWeight: 'bold' }}>
          Ticket
        </Typography>
        <Box sx={{ display: 'flex', alignItems: 'center' }}>
          {isLoggedIn ? (
            <>
              <Box sx={{ display: 'flex', alignItems: 'center', mr: 4 }}>
                <AccountCircleIcon sx={{ color: 'white', mr: 1, fontSize: 30 }} />
                <Typography sx={{ color: 'white', fontWeight: 'bold', fontSize: '1rem' }}>
                  {loggedInUser}
                </Typography>
                <Button sx={{ ml: 2, color: '#fff', borderColor: '#fff' }} variant="outlined" onClick={onLogout}>
                  Logout
                </Button>
              </Box>
              <Box>
                {!isAdmin && (
                  <>
                    <Link href="/view-issues" underline="none" color="inherit" sx={{ mr: 3 }}>
                      <Button variant="text" sx={{ color: 'white', fontWeight: 'bold' }}>
                        View Issues
                      </Button>
                    </Link>
                    <Link href="/my-issues" underline="none" color="inherit" sx={{ mr: 3 }}>
                      <Button variant="text" sx={{ color: 'white', fontWeight: 'bold' }}>
                        My Issues
                      </Button>
                    </Link>
                  </>
                )}
                {isAdmin && (
                  <Link href="/admin" underline="none" color="inherit">
                    <Button variant="text" sx={{ color: 'white', fontWeight: 'bold' }}>
                      Admin
                    </Button>
                  </Link>
                )}
              </Box>
            </>
          ) : (
            <Login onLogin={onLogin} />
          )}
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
