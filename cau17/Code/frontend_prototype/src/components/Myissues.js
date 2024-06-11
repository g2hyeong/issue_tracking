import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Box, Typography, Button, Link } from '@mui/material';
import { styled } from '@mui/system';
import SearchBar from './SearchBar';
import PriorityBadge from './PriorityBadge';

const StyledTableCell = styled(TableCell)({
  fontWeight: 'bold',
  backgroundColor: '#fafafa',
  borderBottom: '2px solid #e0e0e0',
  color: '#424242',
  textAlign: 'center',
});

const Myissues = () => {
  const [issues, setIssues] = useState([]);
  const [projectId, setProjectId] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchIssues = async () => {
      const username = localStorage.getItem('user');
      if (username) {
        try {
          const response = await fetch(`http://localhost:8080/issues`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({id: username}),
          });

          const data = await response.json();
          setIssues(data);
        } catch (error) {
          console.error('Failed to fetch issues:', error);
        }
      } else {
        console.error('User not found');
      }
    };

    fetchIssues();
  }, [projectId]);

  const handleRowClick = (id) => {
    navigate(`/my-issues/${id}`);
  };

  const handleSearch = (searchResults) => {
    setIssues(searchResults);
  };

  return (
<TableContainer component={Paper} sx={{ mt: 2, borderRadius: 2, boxShadow: 3 }}>
      <Box sx={{ p: 2 }}>
        <SearchBar onSearch={handleSearch} />
      </Box>
      <Table>
        <TableHead>
          <TableRow>
            <StyledTableCell>Issue</StyledTableCell>
            <StyledTableCell>Title</StyledTableCell>
            <StyledTableCell>Priority</StyledTableCell>
            <StyledTableCell>Status</StyledTableCell>
            <StyledTableCell>Reported Date</StyledTableCell>
            <StyledTableCell>Reporter</StyledTableCell>
            <StyledTableCell>Assignee</StyledTableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {issues.map((issue) => (
            <TableRow
              key={issue.id}
              hover
              onClick={() => handleRowClick(issue.id)}
              sx={{
                cursor: 'pointer',
                '&:nth-of-type(even)': { backgroundColor: '#f9f9f9' },
                '&:hover': { backgroundColor: '#eceff1' },
                transition: 'background-color 0.3s',
              }}
            >
              <TableCell sx={{ textAlign: 'center' }}>{issue.id}</TableCell>
              <TableCell>
                <Typography variant="body1" sx={{ fontWeight: 'bold', color: '#424242' }}>
                  {issue.title}
                </Typography>
              </TableCell>
              <TableCell sx={{ textAlign: 'center' }}>
                <PriorityBadge priority={issue.priority} />
              </TableCell>
              <TableCell sx={{ textAlign: 'center' }}>{issue.status}</TableCell>
              <TableCell sx={{ textAlign: 'center' }}>{issue.reportedDate}</TableCell>
              <TableCell sx={{ textAlign: 'center' }}>{issue.reporter}</TableCell>
              <TableCell sx={{ textAlign: 'center' }}>{issue.assignee}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>  );
};

export default Myissues;
