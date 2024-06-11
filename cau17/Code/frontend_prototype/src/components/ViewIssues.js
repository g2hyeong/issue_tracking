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

const ViewIssues = () => {
  const [issues, setIssues] = useState([]);
  const [projectId, setProjectId] = useState('');
  const [role, setRole] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchIssues = async () => {
      const username = localStorage.getItem('user');
      if (username) {
        try {
          const usersResponse = await fetch('http://localhost:8080/users');
          const usersData = await usersResponse.json();
          const user = usersData.find(user => user.userName === username);
          if (user) {
            const userId = user.id;
            const userResponse = await fetch(`http://localhost:8080/users/${userId}`);
            const userData = await userResponse.json();

            setRole(userData.userType);
            setProjectId(userData.project_id);
          } else {
            console.error('User not found');
          }

          const response = await fetch(`http://localhost:8080/issues/project/${projectId}`, {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
            },
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
  }, [projectId, role]);

  const handleRowClick = (id) => {
    navigate(`/view-issues/${id}`);
  };

  const handleSearch = (searchResults) => {
    setIssues(searchResults);
  };

  return (
    <TableContainer component={Paper} sx={{ mt: 2, borderRadius: 2, boxShadow: 3 }}>
      <Box sx={{ p: 2 }}>
        <SearchBar onSearch={handleSearch} />
      </Box>
      {role==='tester' && <Box sx={{ p: 2, display: 'flex', justifyContent: 'flex-end' }}>
        <Link href="/new-issue" underline="none" color="inherit">
          <Button variant="contained" sx={{
            fontWeight: 'bold',
            backgroundColor: '#3f51b5',
            color: '#fff',
            '&:hover': {
              backgroundColor: '#303f9f',
            },
          }}>
            New Issue
          </Button>
        </Link>
      </Box>}
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
    </TableContainer>
  );
};

export default ViewIssues;
