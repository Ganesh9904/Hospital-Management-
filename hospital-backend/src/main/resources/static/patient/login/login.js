
document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');

    loginForm.addEventListener('submit', function(e) {
        e.preventDefault();
        handleLogin();
    });
});

function handleLogin() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    if (!email || !password) {
        alert('Please fill in all fields.');
        return;
    }

    const loginData = {
        email: email,
        password: password
    };

    fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(loginData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Login failed');
        }
        return response.json();
    })
    .then(data => {
       
        localStorage.setItem('patientToken', data.token);
        localStorage.setItem('patientId', data.id);
        localStorage.setItem('patientName', data.name);
        
        window.location.href = '../dashboard/dashboard.html';
    })
    .catch(error => {
        console.error('Login error:', error);
        alert('Login failed. Please check your credentials and try again.');
    });
}
