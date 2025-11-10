document.addEventListener('DOMContentLoaded', function() {
    const resetPasswordForm = document.getElementById('resetPasswordForm');
    const messageDiv = document.getElementById('message');
    const toggleNewPassword = document.getElementById('toggleNewPassword');
    const toggleConfirmPassword = document.getElementById('toggleConfirmPassword');
    const newPasswordInput = document.getElementById('newPassword');
    const confirmPasswordInput = document.getElementById('confirmPassword');

  
    const urlParams = new URLSearchParams(window.location.search);   // Get token from URL
    const token = urlParams.get('token');

    if (!token) {
        messageDiv.textContent = 'Invalid reset link.';
        messageDiv.style.color = 'red';
        return;
    }

    toggleNewPassword.addEventListener('click', function() {
        if (newPasswordInput.type === 'password') {
            newPasswordInput.type = 'text';
            toggleNewPassword.textContent = '🙈';
        } else {
            newPasswordInput.type = 'password';
            toggleNewPassword.textContent = '👁️';
        }
    });

    toggleConfirmPassword.addEventListener('click', function() {
        if (confirmPasswordInput.type === 'password') {
            confirmPasswordInput.type = 'text';
            toggleConfirmPassword.textContent = '🙈';
        } else {
            confirmPasswordInput.type = 'password';
            toggleConfirmPassword.textContent = '👁️';
        }
    });

    resetPasswordForm.addEventListener('submit', function(e) {
        e.preventDefault();

        const newPassword = newPasswordInput.value;
        const confirmPassword = confirmPasswordInput.value;

        if (!newPassword || !confirmPassword) {
            messageDiv.textContent = 'Please fill in all fields.';
            messageDiv.style.color = 'red';
            return;
        }

        if (newPassword !== confirmPassword) {
            messageDiv.textContent = 'Passwords do not match.';
            messageDiv.style.color = 'red';
            return;
        }


        fetch('/api/auth/patient/reset-password', {         // Send reset password request to backend
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ token, newPassword }),
        })
        .then(response => response.text())
        .then(data => {
            messageDiv.textContent = data;
            messageDiv.style.color = 'green';
            
            setTimeout(() => { 
                window.location.href = '../login/login.html';
            }, 2000);
        })
        .catch(error => {
            console.error('Error:', error);
            messageDiv.textContent = 'An error occurred. Please try again.';
            messageDiv.style.color = 'red';
        });
    });
});
