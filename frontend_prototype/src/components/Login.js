import React, { useState } from 'react';
import { TextField, Button, Box } from '@mui/material';
import { styled } from '@mui/system';

const StyledTextField = styled(TextField)(({ theme }) => ({
  '& .MuiInputBase-root': {
    padding: '5px',
    fontSize: '14px',
  },
  '& .MuiInputBase-input': {
    padding: '5px',
    color: 'black',
    zIndex: 1,
  },
  '& .MuiInputLabel-root': {
    fontSize: '14px',
    color: 'black',
  },
  '& .MuiOutlinedInput-root': {
    '& fieldset': {
      borderColor: 'grey',
    },
    '&:hover fieldset': {
      borderColor: 'black',
    },
    '&.Mui-focused fieldset': {
      borderColor: 'black',
      backgroundColor: 'white',
    },
  },
}));

const Login = ({ onLogin }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async (event) => {
    event.preventDefault();
    const response = await fetch('http://localhost:8080/users/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ id: username, password: password }),
    });
    const data = await response.json();

    if (data) {
      onLogin(username);
      localStorage.setItem('user', username);
    } else {
      console.error('로그인 실패');
    }
  };

  return (
    <Box
      component="form"
      sx={{
        display: 'flex',
        alignItems: 'center',
        gap: 1,
      }}
      onSubmit={handleSubmit}
    >
      <StyledTextField
        label="Username"
        type="text"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        size="small"
        variant="outlined"
      />
      <StyledTextField
        label="Password"
        type="password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        size="small"
        variant="outlined"
      />
      <Button variant="contained" color="primary" type="submit" size="small">
        Login
      </Button>
    </Box>
  );
};

export default Login;
