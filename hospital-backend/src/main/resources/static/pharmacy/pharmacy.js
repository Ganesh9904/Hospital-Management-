
document.addEventListener('DOMContentLoaded', function() {
    fetchPharmacyItems();
});

function fetchPharmacyItems() {
    fetch('http://localhost:8080/api/pharmacy/items')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(items => {
            displayPharmacyItems(items);
        })
        .catch(error => {
            console.error('Error fetching pharmacy items:', error);
            displaySampleItems(); 
        });
}

function displayPharmacyItems(items) {
    const grid = document.getElementById('pharmacy-grid');

    if (items.length === 0) {
        displaySampleItems();
        return;
    }

    items.forEach(item => {
        const card = document.createElement('div');
        card.className = 'pharmacy-item';

        card.innerHTML = `
            <div class="pharmacy-item-info">
                <h3>${item.name}</h3>
                <p>${item.description || 'High-quality medication'}</p>
                <p class="price">₹${item.price || 'N/A'}</p>
            </div>
        `;

        grid.appendChild(card);
    });
}
function displaySampleItems() {
    const grid = document.getElementById('pharmacy-grid');
    grid.innerHTML = '<p style="text-align: center; color: #666;">Loading pharmacy items from database...</p>';
}
