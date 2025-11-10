
document.addEventListener('DOMContentLoaded', function() {
    checkAuthentication();
    loadDashboardData();
});

function checkAuthentication() {
    const patientToken = localStorage.getItem('patientToken');
    const patientName = localStorage.getItem('patientName');

    if (!patientToken) {
        window.location.href = '../login/login.html';
        return;
    }

    const welcomeMessage = document.getElementById('welcomeMessage');
    welcomeMessage.textContent = `Welcome back, ${patientName}! Manage your appointments and health records.`;
}
function loadDashboardData() {
    const patientId = localStorage.getItem('patientId');

    if (!patientId) {
        return;
    }
    fetch(`http://localhost:8080/api/appointments/history/${patientId}`)
        .then(response => response.json())
        .then(appointments => {
            displayAppointmentHistory(appointments);
            displayUpcomingAppointments(appointments);
        })
        .catch(error => {
            console.error('Error loading appointments:', error);
        });
}

function displayUpcomingAppointments(appointments) {
    const upcomingContainer = document.getElementById('upcomingAppointments');
    const now = new Date();
    const today = new Date(now.getFullYear(), now.getMonth(), now.getDate()); 
    const upcoming = appointments.filter(appointment => {
        const [year, month, day] = appointment.date.split('-').map(Number);
        const appointmentDate = new Date(year, month - 1, day);

        if (appointmentDate > today) {
            return true;
        } else if (appointmentDate.getTime() === today.getTime()) {
            if (appointment.time) {
                const [hours, minutes] = appointment.time.split(':').map(Number);
                const appointmentTime = new Date(appointmentDate);
                appointmentTime.setHours(hours, minutes, 0, 0);
                return appointmentTime > now;
            }
            return false; 
        }
        return false;
    }).slice(0, 5);

    if (upcoming.length === 0) {
        upcomingContainer.innerHTML = '<p class="no-appointments">No upcoming appointments.</p>';
        return;
    }

    upcoming.forEach(appointment => {
        const appointmentDiv = document.createElement('div');
        appointmentDiv.className = 'appointment-item';

        appointmentDiv.innerHTML = `
            <h3>${appointment.doctorName || 'Doctor'}</h3>
            <p><strong>Date:</strong> ${formatDate(appointment.date)}</p>
            <p><strong>Time:</strong> ${formatTime(appointment.time)}</p>
            <p><strong>Reason:</strong> ${appointment.reason}</p>
            <span class="appointment-status status-upcoming">Upcoming</span>
        `;

        upcomingContainer.appendChild(appointmentDiv);
    });
}
function displayAppointmentHistory(appointments) {
    const historyContainer = document.getElementById('appointmentHistory');
    const today = new Date();

    const history = appointments.filter(appointment => {
        const appointmentDate = new Date(appointment.date);
        return appointmentDate < today;
    }).slice(0, 5); 

    if (history.length === 0) {
        historyContainer.innerHTML = '<p class="no-appointments">No appointment history available.</p>';
        return;
    }

    history.forEach(appointment => {
        const appointmentDiv = document.createElement('div');
        appointmentDiv.className = 'appointment-item';

        appointmentDiv.innerHTML = `
            <h3>${appointment.doctorName || 'Doctor'}</h3>
            <p><strong>Date:</strong> ${formatDate(appointment.date)}</p>
            <p><strong>Time:</strong> ${formatTime(appointment.time)}</p>
            <p><strong>Reason:</strong> ${appointment.reason}</p>
            <span class="appointment-status status-completed">Completed</span>
        `;

        historyContainer.appendChild(appointmentDiv);
    });
}

function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
}

function formatTime(timeString) {
    if (!timeString) {
        return 'Time not set';
    }
    const [hours, minutes] = timeString.split(':');
    const hour = parseInt(hours);
    const ampm = hour >= 12 ? 'PM' : 'AM';
    const displayHour = hour % 12 || 12;
    return `${displayHour}:${minutes} ${ampm}`;
}

function logout() {
    localStorage.removeItem('patientToken');
    localStorage.removeItem('patientId');
    localStorage.removeItem('patientName');
    window.location.href = '../login/login.html';
}
