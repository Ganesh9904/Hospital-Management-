document.addEventListener('DOMContentLoaded', function() {
    const doctorNameSpan = document.getElementById('doctorName');
    const upcomingAppointmentsDiv = document.getElementById('upcomingAppointments');
    const previousAppointmentsDiv = document.getElementById('previousAppointments');
    const logoutBtn = document.getElementById('logoutBtn');
    const cancelModal = document.getElementById('cancelModal');
    const closeModal = document.querySelector('.close');
    const confirmCancelBtn = document.getElementById('confirmCancel');
    const cancelReasonTextarea = document.getElementById('cancelReason');

    let currentAppointmentId = null;


    const doctorId = localStorage.getItem('doctorId');    // Check if doctor is logged in
    const doctorName = localStorage.getItem('doctorName');

    if (!doctorId || !doctorName) {
        window.location.href = 'login.html';
        return;
    }

    doctorNameSpan.textContent = doctorName; //Display Doctor Name and Load Appointments
    loadAppointments();

    // Logout functionality
    logoutBtn.addEventListener('click', function() {
        localStorage.removeItem('doctorId');
        localStorage.removeItem('doctorName');
        localStorage.removeItem('doctorEmail');
        window.location.href = '../login/login.html';
    });


    closeModal.addEventListener('click', function() {    // close modal 
        cancelModal.style.display = 'none';
    });

    window.addEventListener('click', function(event) {
        if (event.target === cancelModal) {
            cancelModal.style.display = 'none';
        }
    });


    confirmCancelBtn.addEventListener('click', function() {    // Confirm cancel
        const reason = cancelReasonTextarea.value.trim();
        if (reason === '') {
            alert('Please provide a reason for canceling.');
            return;
        }

        fetch(`/api/appointments/cancel/${currentAppointmentId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ reason }),
        })
        .then(response => response.text())
        .then(data => {
            alert(data);
            cancelModal.style.display = 'none';
            loadAppointments();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Failed to cancel appointment.');
        });
    });

    function loadAppointments() {
        fetch(`/api/appointments/doctor/${doctorId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to load appointments');
            }
            return response.json();
        })
        .then(appointments => {
            displayAppointments(appointments);
        })
        .catch(error => {
            console.error('Error loading appointments:', error);
            alert('Failed to load appointments. Please try again.');
        });
    }

    function displayAppointments(appointments) {
        const now = new Date();
        now.setHours(0, 0, 0, 0); 
        const upcoming = [];
        const previous = [];

        appointments.forEach(appointment => {
            const appointmentDate = new Date(appointment.appointmentDate);
            if (appointmentDate >= now && appointment.status !== 'cancelled') {
                upcoming.push(appointment);
            } else {
                previous.push(appointment);
            }
        });

        displayAppointmentList(upcomingAppointmentsDiv, upcoming, true);
        displayAppointmentList(previousAppointmentsDiv, previous, false);
    }

    function displayAppointmentList(container, appointments, isUpcoming) {
        container.innerHTML = '';
        if (appointments.length === 0) {
            container.innerHTML = '<p>No appointments found.</p>';
            return;
        }

        appointments.forEach(appointment => {
            const appointmentDiv = document.createElement('div');
            appointmentDiv.className = 'appointment-item' + (isUpcoming ? ' upcoming' : '');

            const statusText = appointment.status === 'cancelled' ? ' (Cancelled)' : '';
            appointmentDiv.innerHTML = `
                <div class="appointment-details">
                    <h4>Patient: ${appointment.patientName}</h4>
                    <p>Date: ${appointment.appointmentDate}</p>
                    <p>Time: ${appointment.appointmentTime}</p>
                    <p>Reason: ${appointment.reason}${statusText}</p>
                </div>
                ${isUpcoming && appointment.status !== 'cancelled' ? `<button class="cancel-btn" data-id="${appointment.id}">Cancel</button>` : ''}
            `;

            container.appendChild(appointmentDiv);
        });

       
        if (isUpcoming) {  // cancel buttons
            const cancelBtns = container.querySelectorAll('.cancel-btn');
            cancelBtns.forEach(btn => {
                btn.addEventListener('click', function() {
                    currentAppointmentId = this.getAttribute('data-id');
                    cancelModal.style.display = 'block';
                    cancelReasonTextarea.value = '';
                });
            });
        }
    }
});
