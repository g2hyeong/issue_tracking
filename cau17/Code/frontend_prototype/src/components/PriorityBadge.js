import React from 'react';
import { Box } from '@mui/material';

const priorityColors = {
  blocker: {
    backgroundColor: '#ffebee',
    color: '#c62828',
  },
  critical: {
    backgroundColor: '#ffcdd2',
    color: '#d32f2f',
  },
  major: {
    backgroundColor: '#ffe0b2',
    color: '#ef6c00',
  },
  minor: {
    backgroundColor: '#fff8e1',
    color: '#fbc02d',
  },
  trivial: {
    backgroundColor: '#e8f5e9',
    color: '#388e3c',
  },
};

const PriorityBadge = ({ priority }) => {
  const colors = priorityColors[priority] || {
    backgroundColor: '#e0e0e0',
    color: '#757575',
  };

  return (
    <Box
      sx={{
        display: 'inline-block',
        padding: '4px 12px',
        borderRadius: '12px',
        ...colors,
        fontWeight: 'bold',
        textTransform: 'uppercase',
      }}
    >
      {priority}
    </Box>
  );
};

export default PriorityBadge;