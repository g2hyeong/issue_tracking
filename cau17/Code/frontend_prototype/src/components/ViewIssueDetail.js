import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import { Typography, Grid, TextField, Button, Paper, Divider, MenuItem, Select, FormControl, InputLabel, Card, CardContent, Box } from '@mui/material';
import { AccountCircle, PriorityHigh, AssignmentInd, Event, Person, CheckCircle } from '@mui/icons-material';
import { styled } from '@mui/system';


const ViewIssueDetail = () => {
  const [defectData, setDefectData] = useState(null);
  const [comment, setComment] = useState('');
  const [comments, setComments] = useState([]);
  const [role, setRole] = useState('');
  const [username, setUsername] = useState('');
  const [selectedAssignee, setSelectedAssignee] = useState(''); // 선택된 담당자 ID 저장
  const [projectDevelopers, setProjectDevelopers] = useState([]); // 프로젝트 개발자 목록
  const [recommendedDeveloper, setRecommendedDeveloper] = useState(''); // 추천 개발자 상태 추가


  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation(); // useLocation 훅 추가


  const GlowTypography = styled(Typography)`
  @keyframes glow {
    0% {
      text-shadow: 0 0 5px #fff, 0 0 10px #fff, 0 0 15px #ff00ff, 0 0 20px #ff00ff, 0 0 25px #ff00ff, 0 0 30px #ff00ff, 0 0 35px #ff00ff;
    }
    50% {
      text-shadow: 0 0 10px #fff, 0 0 20px #fff, 0 0 30px #ff00ff, 0 0 40px #ff00ff, 0 0 50px #ff00ff, 0 0 60px #ff00ff, 0 0 70px #ff00ff;
    }
    100% {
      text-shadow: 0 0 5px #fff, 0 0 10px #fff, 0 0 15px #ff00ff, 0 0 20px #ff00ff, 0 0 25px #ff00ff, 0 0 30px #ff00ff, 0 0 35px #ff00ff;
    }
  }

  animation: glow 1.5s infinite;
`;

  const fetchIssueDetails = async () => {
    try {
      const response = await fetch('http://localhost:8080/issues/details', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ id: id }),
      });

      if (response.ok) {
        const data = await response.json();
        setDefectData(data);
      } else {
        console.error('Failed to fetch issue details');
      }
    } catch (error) {
      console.error('Error fetching issue details:', error);
    }
  };

  const fetchComments = async () => {
    try {
      const response = await fetch('http://localhost:8080/issue-comments', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ issue_id: id }),
      });

      if (response.ok) {
        const data = await response.json();
        setComments(data);
      } else {
        console.error('Failed to fetch comments');
      }
    } catch (error) {
      console.error('Error fetching comments:', error);
    }
  };

  const fetchRecommendedDeveloper = async (projectId) => {
    try {
      const response = await fetch('http://localhost:8080/issue-statuses/recommend', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ project_id: projectId }),
      });

      if (response.ok) {
        const data = await response.json();
        if (data.length > 0) {
          setRecommendedDeveloper(data[0]); // 추천 개발자 이름 설정
        }
      } else {
        console.error('Failed to fetch recommended developer');
      }
    } catch (error) {
      console.error('Error fetching recommended developer:', error);
    }
  };

  useEffect(() => {
    const fetchUserRole = async () => {
      const storedUser = localStorage.getItem('user');
      if (storedUser) {
        setUsername(storedUser);

        try {
          const usersResponse = await fetch('http://localhost:8080/users');
          const usersData = await usersResponse.json();
          const user = usersData.find(user => user.userName === storedUser);
          if (user) {
            const userId = user.id;
            const userResponse = await fetch(`http://localhost:8080/users/${userId}`);
            const userData = await userResponse.json();

            setRole(userData.userType);
          } else {
            console.error('User not found');
          }
        } catch (error) {
          console.error('Failed to fetch user or project ID:', error);
        }
      }
    };

    fetchIssueDetails();
    fetchComments();
    fetchUserRole();
  }, [id]);

  useEffect(() => {
    const fetchProjectDevelopers = async () => {
      if (defectData) {
        try {
          const projectId = defectData.project_id;
    
          const response = await fetch('http://localhost:8080/users');
          const users = await response.json();
   
          // 프로젝트 ID가 일치하고 userType이 'dev'인 유저만 필터링
          const developers = users.filter(
            (user) => user.project_id === projectId && user.userType === 'dev'
          );
          setProjectDevelopers(developers);
          await fetchRecommendedDeveloper(projectId);

        } catch (error) {
          console.error('Error fetching project developers:', error);
        }
      }
    };

    fetchProjectDevelopers();
  }, [defectData]);

  const handleCommentChange = (event) => {
    setComment(event.target.value);
  };

  const handleAddComment = async () => {
    if (comment.trim() !== '') {
      const newComment = {
        issue_id: id,
        content: comment,
        user_id: username,
      };

      try {
        const response = await fetch('http://localhost:8080/issue-comments/create', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(newComment),
        });

        if (response.ok) {
          const savedComment = await response.json();
          setComment(''); // Clear the comment input field
          fetchComments(); // Fetch the updated list of comments
        } else {
          console.error('Failed to add comment');
        }
      } catch (error) {
        console.error('Error adding comment:', error);
      }
    }
  };

  const handleStatusUpdate = async (newStatus) => {
    const requestPayload = {
      issue_id: id,
      user_id: username,
    };

    try {
      const response = await fetch('http://localhost:8080/issue-statuses/update', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestPayload),
      });

      if (response.ok) {
        // Update the defect data status
        await fetchIssueDetails();
      } else {
        console.error('Failed to update issue status');
      }
    } catch (error) {
      console.error('Error updating issue status:', error);
    }
  };

  const handleAssigneeChange = (event) => {
    setSelectedAssignee(event.target.value);
  };


  const handleAssigneeSubmit = async () => {
    if (selectedAssignee.trim() !== '') {
      const assigneeRequest = {
        issue_id: id,
        assignee: selectedAssignee,
        user_id: username,
      };
  
      try {
        const response = await fetch('http://localhost:8080/issue-statuses/assignee', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(assigneeRequest),
        });
  
        if (response.ok) {
          console.log('Assignee allocated successfully');
          await fetchIssueDetails(); // Fetch the updated issue details to refresh the UI
        } else {
          console.error('Failed to allocate assignee');
        }
      } catch (error) {
        console.error('Error allocating assignee:', error);
      }
    }
  };
    


  const renderActionButton = () => {
    if (!role || !defectData) return null;

    if (role === 'dev' && defectData.assignee === username && defectData.status === 'assigned') {
      return (
        <Button variant="contained" onClick={() => handleStatusUpdate('Fixed')}>
          To Fixed
        </Button>
      );
    }
    if (role === 'tester' && defectData.reporter === username && defectData.status === 'fixed') {
      return (
        <Button variant="contained" onClick={() => handleStatusUpdate('Resolved')}>
          To Resolved
        </Button>
      );
    }
    if (role === 'PL' && defectData.status === 'resolved') {
      return (
        <Button variant="contained" onClick={() => handleStatusUpdate('Closed')}>
          To Closed
        </Button>
      );
    }
    if (role === 'PL' && defectData.status === 'new') {
      return (
        <Grid container spacing={2} alignItems="center" justifyContent="flex-end">
          <Grid item align="center">
            <Typography variant="body2" color="textSecondary">
              Recommended:
            </Typography>
            <GlowTypography variant="body2" color="textSecondary" style={{ marginLeft: "20px" }}>
              {recommendedDeveloper}
            </GlowTypography>
          </Grid>

          <Grid item>
            <FormControl fullWidth>
              <InputLabel id="assignee-select-label">Assignee</InputLabel>
              <Select
                labelId="assignee-select-label"
                id="assignee-select"
                value={selectedAssignee}
                label="Assignee"
                onChange={handleAssigneeChange}
              >
                {projectDevelopers.map((developer) => (
                  <MenuItem key={developer.id} value={developer.userName}>
                    {developer.userName} ({developer.userType})
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </Grid>
          <Grid item>
            <Button variant="contained" onClick={handleAssigneeSubmit} disabled={!selectedAssignee}>
              Assignee allocate
            </Button>
          </Grid>
        </Grid>
        );
    }
    return null;
  };

  if (!defectData) {
    return <Typography variant="h6" component="h2">Loading...</Typography>;
  }

  return (
    <Paper elevation={3} sx={{ p: 3, maxWidth: 800, margin: '20px auto' }}>
      <Card variant="outlined" sx={{ p: 3, mt: 3, backgroundColor: '#f5f5f5', borderRadius: 2 }}>
        <CardContent>
          <Grid container spacing={2} alignItems="center">
            <Grid item xs={12}>
              <Typography variant="h4" component="h2" sx={{ fontWeight: 'bold', color: '#3f51b5' }}>
                #{defectData.title}
              </Typography>
            </Grid>
            <Grid item xs={6}>
              <Box display="flex" alignItems="center">
                <Person sx={{ mr: 1, color: '#3f51b5' }} />
                <Typography variant="body1">
                  <strong>Reporter:</strong> {defectData.reporter}
                </Typography>
              </Box>
            </Grid>
            <Grid item xs={6}>
              <Box display="flex" alignItems="center">
                <PriorityHigh sx={{ mr: 1, color: '#f44336' }} />
                <Typography variant="body1">
                  <strong>Priority:</strong> {defectData.priority}
                </Typography>
              </Box>
            </Grid>
            <Grid item xs={6}>
              <Box display="flex" alignItems="center">
                <AssignmentInd sx={{ mr: 1, color: '#4caf50' }} />
                <Typography variant="body1">
                  <strong>Assingee:</strong> {defectData.assignee}
                </Typography>
              </Box>
            </Grid>
            <Grid item xs={6}>
              <Box display="flex" alignItems="center">
                <CheckCircle sx={{ mr: 1, color: '#3f51b5' }} />
                <Typography variant="body1">
                  <strong>Status:</strong> {defectData.status}
                </Typography>
              </Box>
            </Grid>
            <Grid item xs={6}>
              <Box display="flex" alignItems="center">
                <AccountCircle sx={{ mr: 1, color: '#ff9800' }} />
                <Typography variant="body1">
                  <strong>Fixer:</strong> {defectData.fixer || '-'}
                </Typography>
              </Box>
            </Grid>
            <Grid item xs={6}>
              <Box display="flex" alignItems="center">
                <Event sx={{ mr: 1, color: '#3f51b5' }} />
                <Typography variant="body1">
                  <strong>ReportedDate:</strong> {defectData.reportedDate}
                </Typography>
              </Box>
            </Grid>
            <Grid item xs={12}>
              <Divider sx={{ my: 3 }} />
              <Typography variant="h6" component="h3" sx={{ mb: 1, color: '#3f51b5' }}>
                Description
              </Typography>
              <Typography variant="body2" dangerouslySetInnerHTML={{ __html: defectData.description }} />
            </Grid>
          </Grid>
        </CardContent>
        <Box sx={{ textAlign: 'right', mt: 3 }}>
          {renderActionButton()}
        </Box>
      </Card>

      <Divider sx={{ my: 3 }} />

      <Grid container spacing={2}>
        <Grid item xs={12}>
          <Typography variant="h6" component="h3">
            Comments
          </Typography>
        </Grid>
        {comments.map((comment) => (
          <Grid item xs={12} key={comment.id}>
            <Card variant="outlined">
              <CardContent style={{backgroundColor: "#F3F3F3"}}>
                <Box display="flex" alignItems="center" mb={1}>
                  <Typography variant="body1" sx={{ fontWeight: 'bold', mr: 1 }}>
                    {comment.commenter} ({comment.userType}):
                  </Typography>
                  <Typography variant="caption" color="textSecondary">
                    {comment.commentedDate}
                  </Typography>
                </Box>
                <Typography variant="body2">{comment.comment}</Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      <Divider sx={{ my: 3 }} />

      <Grid container spacing={2} alignItems="center">
        <Grid item xs={12}>
          <Typography variant="h6" component="h3">
            New comment
          </Typography>
        </Grid>
        <Grid item xs={12}>
          <TextField 
            fullWidth 
            multiline 
            rows={4} 
            placeholder="Comments are available here." 
            value={comment} 
            onChange={handleCommentChange} 
          />
        </Grid>
        <Grid item xs={12} sx={{ textAlign: 'right' }}>
          <Button variant="contained" onClick={handleAddComment}>
            Save comment
          </Button>
        </Grid>
      </Grid>
      <Button
              variant="contained"
              color="primary"
              onClick={() => navigate(location.pathname.startsWith('/my-issues') ? '/my-issues' : '/view-issues')}
            >
        to List
      </Button>
    </Paper>
  );
}

export default ViewIssueDetail;