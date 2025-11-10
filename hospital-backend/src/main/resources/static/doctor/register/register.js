document.addEventListener('DOMContentLoaded', function() {
    const registerForm = document.getElementById('registerForm');
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

    
    registerForm.addEventListener('submit', function(e) { // Handle form submission
        e.preventDefault();

        const name = document.getElementById('name').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const specialization = document.getElementById('specialization').value;
        const availableDays = document.getElementById('availableDays').value;
        const availableTime = document.getElementById('availableTime').value;

 
        fetch('/api/doctors/register', {        // Send registration request to backend
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ name, email, password, specialization, availableDays, availableTime }),
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                return response.text().then(text => { throw new Error(text); });
            }
        })
        .then(data => {
            messageDiv.textContent = 'Registration successful! You can now login.';
            messageDiv.style.color = 'green';
           
            setTimeout(() => {   //redirect to login page after a delay
                window.location.href = '../login/login.html';
            }, 2000);
        })
        .catch(error => {
            console.error('Error:', error);
            messageDiv.textContent = error.message || 'Registration failed. Please try again.';
            messageDiv.style.color = 'red';
        });
    });
});
