document.addEventListener('DOMContentLoaded', function() {
    const forgotPasswordForm = document.getElementById('forgotPasswordForm');
    const messageDiv = document.getElementById('message');

    forgotPasswordForm.addEventListener('submit', function(e) {
        e.preventDefault();

        const email = document.getElementById('email').value;

        if (!email) {
            messageDiv.textContent = 'Please enter your email address.';
            messageDiv.style.color = 'red';
            return;
        }

      
        fetch('/api/auth/doctor/forgot-password', {   // Send forgot password request to backend
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email }),
        })
        .then(response => response.text())
        .then(data => {
            messageDiv.textContent = data;
            messageDiv.style.color = 'green';
        })
        .catch(error => {
            console.error('Error:', error);
            messageDiv.textContent = 'An error occurred. Please try again.';
            messageDiv.style.color = 'red';
        });
    });
});
