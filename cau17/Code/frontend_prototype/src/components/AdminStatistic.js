import React, { useState, useEffect } from 'react';
import {
  Box,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  Typography,
  Paper,
  Container
} from '@mui/material';
import { Bar } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

const AdminStatistic = () => {
  const [projects, setProjects] = useState([]);
  const [issues, setIssues] = useState([]);
  const [projectId, setProjectId] = useState('');
  const [property, setProperty] = useState('daily');
  const [chartData, setChartData] = useState(null);

  useEffect(() => {
    fetchProjects();
  }, []);

  useEffect(() => {
    if (projectId) {
      fetchStatistics();
    }
  }, [projectId, property]);

  const fetchProjects = async () => {
    try {
      const response = await fetch('http://localhost:8080/projects');
      const data = await response.json();
      setProjects(data);
    } catch (error) {
      console.error('Failed to fetch projects:', error);
    }
  };

  const fetchStatistics = async () => {
    try {
      const response = await fetch('http://localhost:8080/issues/statistic', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ project_id: projectId, property }),
      });

      if (response.ok) {
        const data = await response.json();
        formatChartData(data);
      } else {
        console.error('Failed to fetch statistics');
      }
    } catch (error) {
      console.error('Error fetching statistics:', error);
    }
  };

  const formatChartData = (data) => {
    const labels = Object.keys(data);
    const values = Object.values(data);

    setChartData({
      labels,
      datasets: [
        {
          label: property === 'daily' ? 'Daily Issues' : 'Monthly Issues',
          data: values,
          backgroundColor: 'rgba(75, 192, 192, 0.6)',
          borderColor: 'rgba(75, 192, 192, 1)',
          borderWidth: 1,
        },
      ],
    });
  };

  const handleProjectIdChange = (event) => {
    setProjectId(event.target.value);
  };

  const handlePropertyChange = (event) => {
    setProperty(event.target.value);
  };

  return (
    <Container maxWidth="md" sx={{ mt: 4 }}>
      <Paper elevation={3} sx={{ p: 4, borderRadius: 2 }}>
        <Typography variant="h4" component="h1" gutterBottom sx={{ textAlign: 'center', fontWeight: 'bold' }}>
          Issue Statistics
        </Typography>

        <Box sx={{ display: 'flex', justifyContent: 'center', flexWrap: 'wrap', mb: 4 }}>
          <FormControl sx={{ m: 2, minWidth: 200 }}>
            <InputLabel id="project-id-label">Project</InputLabel>
            <Select
              labelId="project-id-label"
              value={projectId}
              label="Project"
              onChange={handleProjectIdChange}
            >
              {projects.map((project) => (
                <MenuItem key={project.id} value={project.id}>
                  {project.title}
                </MenuItem>
              ))}
            </Select>
          </FormControl>

          <FormControl sx={{ m: 2, minWidth: 200 }}>
            <InputLabel id="property-label">Time Period</InputLabel>
            <Select
              labelId="property-label"
              value={property}
              label="Time Period"
              onChange={handlePropertyChange}
            >
              <MenuItem value="daily">Daily</MenuItem>
              <MenuItem value="month">Monthly</MenuItem>
            </Select>
          </FormControl>
        </Box>

        {chartData && (
          <Box sx={{ mt: 4 }}>
            <Bar
              data={chartData}
              options={{
                responsive: true,
                plugins: {
                  legend: {
                    position: 'top',
                  },
                  title: {
                    display: true,
                    text: `Issue Statistics (${property === 'daily' ? 'Daily' : 'Monthly'})`,
                  },
                },
              }}
            />
          </Box>
        )}
      </Paper>
    </Container>
  );
};

export default AdminStatistic;