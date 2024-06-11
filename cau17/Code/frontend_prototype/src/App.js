import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Header from './components/Header';
import Admin from './components/Admin';
import NewIssue from './components/NewIssue';
import ViewIssues from './components/ViewIssues';
import MyIssues from './components/Myissues';
import IssueDetail from './components/ViewIssueDetail';
import Welcome from './components/welcome';
import './App.css';

const App = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [isAdmin, setIsAdmin] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const checkLoginStatus = async () => {
      const storedUser = localStorage.getItem('user');
      if (storedUser) {
        setIsLoggedIn(true);
        const response = await fetch('http://localhost:8080/users/isAdmin', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ id: storedUser }),
        });
        const result = await response.json();
        setIsAdmin(result);
      } else {
        setIsLoggedIn(false);
        setIsAdmin(false);
      }
      setIsLoading(false);
    };
    checkLoginStatus();
  }, []);

  const handleLogin = async (username) => {
    const response = await fetch('http://localhost:8080/users/isAdmin', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ id: username }),
    });
    const result = await response.json();
    setIsLoggedIn(true);
    setIsAdmin(result);
    localStorage.setItem('user', username);
  };

  const handleLogout = () => {
    setIsLoggedIn(false);
    setIsAdmin(false);
    localStorage.removeItem('user');
  };

  return (
    <Router>
      <div className="app">
        <Header isLoggedIn={isLoggedIn} isAdmin={isAdmin} loggedInUser={localStorage.getItem('user')} onLogout={handleLogout} onLogin={handleLogin} />
        {!isLoading && (
          <Routes>
            <Route
              path="/admin/*"
              element={
                isLoggedIn ? (
                  isAdmin ? (
                    <Admin />
                  ) : (
                    <Navigate to="/view-issues" />
                  )
                ) : (
                  <Navigate to="/" />
                )
              }
            />
            <Route
              path="/new-issue/*"
              element={isLoggedIn ? <NewIssue /> : <Navigate to="/" />}
            />
            <Route
              path="/view-issues/*"
              element={isLoggedIn ? <ViewIssues /> : <Navigate to="/" />}
            />
            <Route
              path="/view-issues/:id"
              element={isLoggedIn ? <IssueDetail /> : <Navigate to="/" />}
            />
            <Route
              path="/my-issues/*"
              element={isLoggedIn ? <MyIssues /> : <Navigate to="/" />}
            />
            <Route
              path="/my-issues/:id"
              element={isLoggedIn ? <IssueDetail /> : <Navigate to="/" />}
            />
            <Route path="/" element={<Welcome />} />
            <Route path="*" element={<Navigate to="/" />} />
          </Routes>
        )}
      </div>
    </Router>
  );
};

export default App;
