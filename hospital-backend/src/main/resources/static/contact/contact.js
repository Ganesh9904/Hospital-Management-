document.getElementById('contactForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const message = document.getElementById('message').value;

    try {
        const response = await fetch('http://localhost:8080/api/contact/submit', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ name, email, message })
        });

        const result = await response.text();
        alert(result); // show success or error message

        if (response.ok) {// clear the form on success
            document.getElementById('contactForm').reset();
        }
    } catch (error) {
        alert('An error occurred while sending your message. Please try again.');
        console.error('Error:', error);
    }
});
