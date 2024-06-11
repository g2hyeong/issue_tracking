import React, { useState, useEffect } from 'react';
import { TextField, MenuItem, Select, InputLabel, FormControl, Button, Container, Grid, Box, Paper, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import RichTextEditor from './RichTextEditor';

const NewIssue = () => {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [priority, setPriority] = useState('major');
  const [projectId, setProjectId] = useState('');
  const [reporter, setReporter] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUserAndProject = async () => {
      const username = localStorage.getItem('user');
      if (username) {
        setReporter(username);
        try {
          const usersResponse = await fetch('http://localhost:8080/users');
          const usersData = await usersResponse.json();
          const user = usersData.find(user => user.userName === username);
          if (user) {
            const userId = user.id;
            const userResponse = await fetch(`http://localhost:8080/users/${userId}`);
            const userData = await userResponse.json();

            setProjectId(userData.project_id);
          } else {
            console.error('User not found');
          }
        } catch (error) {
          console.error('Failed to fetch user or project ID:', error);
        }
      }
    };
    fetchUserAndProject();
  }, []);

  const handleSubmit = async (event) => {
    event.preventDefault();

    const newIssue = {
      title,
      description,
      project_id: projectId,
      priority,
      reporter,
    };

    try {
      const response = await fetch('http://localhost:8080/issues/create', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(newIssue),
      });

      const data = await response.json();
      console.log(data);

      if (response.ok) {
        navigate('/view-issues');
      } else {
        console.error('Failed to create issue');
      }
    } catch (error) {
      console.error('Failed to create issue:', error);
    }
  };

  return (
    <Container maxWidth="sm" sx={{ mt: 4 }}>
      <Paper elevation={3} sx={{ p: 4, borderRadius: 2 }}>
        <Box component="form" onSubmit={handleSubmit} noValidate>
          <Grid container spacing={3}>
            <Grid item xs={12}>
              <Typography variant="h4" component="h1" gutterBottom>
                New issue
              </Typography>
            </Grid>
            <Grid item xs={12}>
              <TextField
                label="Title"
                variant="outlined"
                fullWidth
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                required
                sx={{ mb: 2 }}
              />
            </Grid>
            <Grid item xs={12}>
              <FormControl fullWidth variant="outlined" sx={{ mb: 2 }}>
                <InputLabel>Priority</InputLabel>
                <Select
                  value={priority}
                  onChange={(e) => setPriority(e.target.value)}
                  label="Priority"
                >
                  <MenuItem value="blocker">Blocker</MenuItem>
                  <MenuItem value="critical">Critical</MenuItem>
                  <MenuItem value="major">Major</MenuItem>
                  <MenuItem value="minor">Minor</MenuItem>
                  <MenuItem value="trivial">Trivial</MenuItem>
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={12}>
              <RichTextEditor
                value={description}
                onChange={setDescription}
                sx={{ height: 250, mb: 2 }}
              />
            </Grid>
            <Grid item xs={12}>
              <Button type="submit" variant="contained" color="primary" fullWidth>
                Add issue
              </Button>
            </Grid>
          </Grid>
        </Box>
      </Paper>
    </Container>
  );
};

export default NewIssue;