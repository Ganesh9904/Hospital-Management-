
document.addEventListener('DOMContentLoaded', function() {
    loadDoctors();
    setMinDate();

    const appointmentForm = document.getElementById('appointmentForm');
    appointmentForm.addEventListener('submit', function(e) {
        e.preventDefault();
        bookAppointment();
    });
});

function loadDoctors() {
    fetch('http://localhost:8080/api/doctors')
        .then(response => response.json())
        .then(doctors => {
            const doctorSelect = document.getElementById('doctor');
            doctors.forEach(doctor => {
                const option = document.createElement('option');
                option.value = doctor.id;
                option.textContent = doctor.name + ' - ' + (doctor.specialization || 'General Medicine');
                doctorSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Error loading doctors:', error);
        });
}
function setMinDate() {
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('date').setAttribute('min', today);
}

function bookAppointment() {
    const doctorId = document.getElementById('doctor').value;
    const date = document.getElementById('date').value;
    const time = document.getElementById('time').value;
    const reason = document.getElementById('reason').value;

    const patientId = localStorage.getItem('patientId'); //get patient id

    if (!patientId) {
        alert('Please login first to book an appointment.');
        window.location.href = '../login/login.html';
        return;
    }

    if (!doctorId || !date || !time || !reason) {
        alert('Please fill in all fields.');
        return;
    }

    const appointmentData = {
        patientId: parseInt(patientId),
        doctorId: parseInt(doctorId),
        date: date,
        time: time,
        reason: reason
    };
    fetch('http://localhost:8080/api/appointments/book', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(appointmentData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Appointment booking failed');
        }
        return response.json();
    })
    .then(data => {
        alert('Appointment booked successfully!');
        window.location.href = '../dashboard/dashboard.html';
    })
    .catch(error => {
        console.error('Booking error:', error);
        alert('Failed to book appointment. Please try again.');
    });
}
