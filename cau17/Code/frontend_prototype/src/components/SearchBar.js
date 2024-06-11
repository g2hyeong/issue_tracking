import React, { useState } from 'react';
import { TextField, Button, Typography, Box, Select, MenuItem, FormControl, InputLabel } from '@mui/material';

function SearchBar({ onSearch }) {
  const [searchTerm, setSearchTerm] = useState('');
  const [searchBy, setSearchBy] = useState('title'); // Default search by option

  const searchOptions = ['title', 'reporter', 'status', 'assignee'];

  const handleSearchChange = (event) => {
    setSearchTerm(event.target.value);
  };

  const handleSearchByChange = (event) => {
    setSearchBy(event.target.value);
  };

  const handleSearchSubmit = async () => {
    try {
      const response = await fetch('http://localhost:8080/issues/search', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ property: searchBy, searchWord: searchTerm }),
      });
      const data = await response.json();
      onSearch(data);
    } catch (error) {
      console.error('Error searching issues:', error);
    }
  };

  return (
    <Box
      sx={{
        display: 'flex',
        alignItems: 'center',
        p: 2,
        borderRadius: 2,
        backgroundColor: '#f5f5f5',
        boxShadow: 1,
        '& .MuiInputLabel-root': {
          fontWeight: 'bold',
        },
        '& .MuiButton-contained': {
          backgroundColor: '#1976d2',
          color: '#fff',
          '&:hover': {
            backgroundColor: '#1565c0',
          },
        },
      }}
    >
      <Typography variant="h6" sx={{ mr: 3, color: '#424242' }}>Search Issues:</Typography>
      
      <FormControl variant="outlined" sx={{ mr: 2, width: 200 }}>
        <InputLabel id="search-by-label">Search By</InputLabel>
        <Select
          labelId="search-by-label"
          id="search-by-select"
          value={searchBy}
          onChange={handleSearchByChange}
          label="Search By"
        >
          {searchOptions.map((option) => (
            <MenuItem key={option} value={option}>
              {option.charAt(0).toUpperCase() + option.slice(1)}
            </MenuItem>
          ))}
        </Select>
      </FormControl>

      <TextField
        variant="outlined"
        placeholder="Enter your search term"
        value={searchTerm}
        onChange={handleSearchChange}
        sx={{ flexGrow: 1, mr: 2 }}
      />

      <Button
        variant="contained"
        onClick={handleSearchSubmit}
        sx={{
          textTransform: 'none',
          padding: '6px 16px',
        }}
      >
        Search
      </Button>
    </Box>
  );
}

export default SearchBar;