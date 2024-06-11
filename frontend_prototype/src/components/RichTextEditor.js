import React, { useState } from 'react';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';

const RichTextEditor = ({ value, onChange, sx }) => {
  return (
    <ReactQuill 
      value={value} 
      onChange={onChange} 
      theme="snow" 
      style={{ ...sx }}  // Apply sx styling
    />
  );
};

export default RichTextEditor;