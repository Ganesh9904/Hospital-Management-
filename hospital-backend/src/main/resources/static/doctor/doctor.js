
document.addEventListener('DOMContentLoaded', function() {
    fetchDoctors();
});

function fetchDoctors() {
    fetch('http://localhost:8080/api/doctors')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(doctors => {
            displayDoctors(doctors);
        })
        .catch(error => {
            console.error('Error fetching doctors:', error);
            displayError('Unable to load doctors at this time. Please try again later.');
        });
}

function getDoctorImage(doctorName) {
    const doctorImages = {
        'Dr. Arjun Kumar': '../images/Doctors/Arjun Kumar.jpg',
        'Dr. Priya Nair': '../images/Doctors/Priya Nair.jpg',
        'Dr. John Mathew': '../images/Doctors/John Mathew.jpg',
        'Dr. Sneha Reddy': '../images/Doctors/Sneha Reddy.jpg',
        'Dr. Michael Raj': '../images/Doctors/Michael Raj.jpg',
        'Dr. Deepa Thomas': '../images/Doctors/Deepa Thomas.jpg',
        'Dr. Kevin Das': '../images/Doctors/Kevin Das.jpg',
        'Dr. Anjali Menon': '../images/Doctors/Anjali Menon.jpg',
        'Dr. Daniel Joseph': '../images/Doctors/Daniel Joseph.jpg'
    };

    return doctorImages[doctorName] || '../images/Doctors/default-doctor.jpg';
}
function displayDoctors(doctors) {
    const grid = document.getElementById('doctor-grid');
    if (doctors.length === 0) { 
        grid.innerHTML = '<p style="text-align: center; color: #666;">Loading doctors from database...</p>';
        return;
    }

    doctors.forEach(doctor => { // specific image of doctor based on name
        const card = document.createElement('div');
        card.className = 'doctor-card';

        const imageSrc = getDoctorImage(doctor.name);   

        card.innerHTML = `
            <img src="${imageSrc}" alt="${doctor.name}" class="doctor-image" onerror="this.src='https://www.bing.com/th/id/OIP.9mHnGxjdY7Xb7-y-aq0_qQHaE8?w=248&h=211&c=8&rs=1&qlt=90&o=6&dpr=1.3&pid=3.1&rm=2'">
            <div class="doctor-info">
                <h3>${doctor.name}</h3>
                <p><strong>Specialization:</strong> ${doctor.specialization || 'General Medicine'}</p>
                <p><strong>Schedule:</strong> ${doctor.availableDays || 'Mon-Fri'} ${doctor.availableTime || '9AM-5PM'}</p>
                <button class="book-btn" onclick="bookAppointment(${doctor.id})">Book Appointment</button>
            </div>
        `;

        grid.appendChild(card);
    });
}

function bookAppointment(doctorId) {
    const patientToken = localStorage.getItem('patientToken');
    const patientId = localStorage.getItem('patientId');

    if (!patientToken || !patientId) {// if user is not logged in it goes to the login page 
        alert('Please login first to book an appointment.');
        window.location.href = '../patient/login/login.html';
        return;
    }

    localStorage.setItem('selectedDoctorId', doctorId);
    window.location.href = '../patient/appointment/appointment.html';
}

function displayError(message) {
    const grid = document.getElementById('doctor-grid');
    grid.innerHTML = `<p style="color: red; text-align: center;">${message}</p>`;
}
