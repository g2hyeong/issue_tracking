import React, { useState, useEffect } from 'react';
import {
  Box,
  Button,
  Checkbox,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  Container,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TextField,
  Typography,
  Paper,
  IconButton,
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import SaveIcon from '@mui/icons-material/Save';

const AdminUserManagement = () => {
  const [users, setUsers] = useState([]);
  const [projects, setProjects] = useState([]);
  const [selectedUsers, setSelectedUsers] = useState([]);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [role, setRole] = useState('');
  const [selectedProject, setSelectedProject] = useState('');
  const [projectMap, setProjectMap] = useState({}); // 객체

  useEffect(() => {
    fetchUsers();
    fetchProjects();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await fetch('http://localhost:8080/users');
      const data = await response.json();
      setUsers(data);
    } catch (error) {
      console.error('Failed to fetch users:', error);
    }
  };

  const fetchProjects = async () => {
    try {
      const response = await fetch('http://localhost:8080/projects');
      const data = await response.json();
      setProjects(data);

      // ID와 제목을 매핑하는 객체 생성
      const projectMap = {};
      data.forEach((project) => {
        projectMap[project.id] = project.title;
      });
      setProjectMap(projectMap);
    } catch (error) {
      console.error('Failed to fetch projects:', error);
    }
  };

  const handleAddUser = async (event) => {
    event.preventDefault();
    if (password !== confirmPassword) {
      alert('Passwords do not match');
      return;
    }
    const newUser = {
      id: username,
      password: password,
      confirmPassword: password,
      role: role,
      project: selectedProject,
    };
    try {
      const response = await fetch('http://localhost:8080/users/create', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(newUser),
      });

      console.log(newUser);
      console.log(response);

      if (response.ok) {
        fetchUsers();
        setUsername('');
        setPassword('');
        setConfirmPassword('');
        setRole('');
        setSelectedProject('');
      } else {
        console.error('Failed to create user');
      }
    } catch (error) {
      console.error('Failed to create user:', error);
    }
  };

  const handleDeleteSelectedUsers = async () => {
    for (const id of selectedUsers) {
      try {
        const response = await fetch(`http://localhost:8080/users/delete`, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ id: id }),
        });
        if (!response.ok) {
          console.error(`Failed to delete user with id ${id}`);
        }
      } catch (error) {
        console.error(`Failed to delete user with id ${id}:`, error);
      }
    }
    fetchUsers();
    setSelectedUsers([]);
  };

  const handleCheckboxChange = (id) => {
    setSelectedUsers((prevSelected) =>
      prevSelected.includes(id) ? prevSelected.filter((user) => user !== id) : [...prevSelected, id]
    );
  };

  return (
    <Container maxWidth="md" sx={{ mt: 4 }}>
    <Paper elevation={4} sx={{ p: 0, borderRadius: 2 }}>
    <Box sx={{ p: 4, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
      <Typography variant="h4" component="h1" gutterBottom sx={{ textAlign: 'center', fontWeight: 'bold' }}>
        Manage User Accounts
      </Typography>
      <Paper elevation={3} sx={{ p: 3, width: '100%', maxWidth: 800, mb: 4 }}>
        <Typography variant="h6" gutterBottom>
          Add New User
        </Typography>
        <form onSubmit={handleAddUser}>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <TextField
              id="username"
              label="Username"
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <TextField
              id="password"
              label="Password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <TextField
              id="confirm-password"
              label="Confirm Password"
              type="password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
            />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <InputLabel htmlFor="role">Role</InputLabel>
            <Select
              id="role"
              value={role}
              onChange={(e) => setRole(e.target.value)}
              required
            >
              <MenuItem value="PL">PL</MenuItem>
              <MenuItem value="dev">dev</MenuItem>
              <MenuItem value="tester">tester</MenuItem>
            </Select>
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <InputLabel htmlFor="projects">Projects</InputLabel>
            <Select
              id="projects"
              value={selectedProject}
              onChange={(e) => setSelectedProject(e.target.value)}
              required
            >
              {projects.map((project) => (
                <MenuItem key={project.id} value={project.title}>
                  {project.title}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          <Box sx={{ display: 'flex', justifyContent: 'flex-end' }}>
            <Button
              variant="contained"
              type="submit"
              startIcon={<SaveIcon />}
              sx={{ backgroundColor: '#2196F3', color: '#fff' }}
            >
              Add User
            </Button>
          </Box>
        </form>
      </Paper>
      <Paper elevation={3} sx={{ p: 3, width: '100%', maxWidth: 800 }}>
        <Typography variant="h6" gutterBottom>
          User List
        </Typography>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell padding="checkbox">
                <Checkbox
                  indeterminate={selectedUsers.length > 0 && selectedUsers.length < users.length}
                  checked={users.length > 0 && selectedUsers.length === users.length}
                  onChange={(e) =>
                    setSelectedUsers(e.target.checked ? users.map((user) => user.userName) : [])
                  }
                />
              </TableCell>
              <TableCell>Username</TableCell>
              <TableCell>Password</TableCell>
              <TableCell>Role</TableCell>
              <TableCell>Project</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {users.map((user) => (
              <TableRow key={user.userName}>
                <TableCell padding="checkbox">
                  <Checkbox
                    checked={selectedUsers.includes(user.userName)}
                    onChange={() => handleCheckboxChange(user.userName)}
                  />
                </TableCell>
                <TableCell>{user.userName}</TableCell>
                <TableCell>{user.userPW}</TableCell>
                <TableCell>{user.userType}</TableCell>
                <TableCell>{projectMap[user.project_id]}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
        <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 2 }}>
          <Button
            variant="contained"
            color="error"
            startIcon={<DeleteIcon />}
            onClick={handleDeleteSelectedUsers}
          >
            Remove Selected Accounts
          </Button>
        </Box>
      </Paper>
    </Box>
    </Paper>
    </Container>
  );
};

export default AdminUserManagement;