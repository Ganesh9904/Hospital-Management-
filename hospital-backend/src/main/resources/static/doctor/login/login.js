document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    const togglePassword = document.getElementById('togglePassword');
    const passwordInput = document.getElementById('password');
    const messageDiv = document.getElementById('message');

    togglePassword.addEventListener('click', function() {
        if (passwordInput.type === 'password') {
            passwordInput.type = 'text';
            togglePassword.textContent = '🙈';
        } else {
            passwordInput.type = 'password';
            togglePassword.textContent = '👁️';
        }
    });

    // Handle form submission
    loginForm.addEventListener('submit', function(e) {
        e.preventDefault();

        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        // Send login request to backend
        fetch('/api/auth/doctor-login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, password }),
        })
        .then(response => response.json())
        .then(data => {
            if (data.id) { // Store doctor info in localStorage
                localStorage.setItem('doctorId', data.id);
                localStorage.setItem('doctorName', data.name);
                localStorage.setItem('doctorEmail', data.email);
                window.location.href = '../dashboard/doctor-dashboard.html';
            } else {
                messageDiv.textContent = data.message || 'Login failed';
                messageDiv.style.color = 'red';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            messageDiv.textContent = 'An error occurred. Please try again.';
            messageDiv.style.color = 'red';
        });
    });
});
