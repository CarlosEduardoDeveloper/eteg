import React, { useEffect } from 'react';
import { Alert } from 'react-bootstrap';

const Feedback = ({ success, message, onClose }) => {
  useEffect(() => {
    const timer = setTimeout(() => {
      onClose();
    }, 5000);

    return () => clearTimeout(timer);
  }, [onClose]);

  return (
    <Alert
      variant={success ? 'success' : 'danger'}
      dismissible
      onClose={onClose}
      className="mb-4"
    >
      {message}
    </Alert>
  );
};

export default Feedback;