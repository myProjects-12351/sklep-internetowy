const images = [
    '/images/planet-earth.png',
    '/images/planet-earth.png',
    '/images/planet-earth.png',
    '/images/planet-earth.png',
    '/images/planet-earth.png'
];

const links = [
    'https://example.com/page1',
    'https://example.com/page2',
    'https://example.com/page3',
    'https://example.com/page4',
    'https://example.com/page5'
];

function getRandomIndex(arrayLength) {
    return Math.floor(Math.random() * arrayLength);
}

function setRandomImagesAndLinks() {
    const imageElements = document.querySelectorAll('.random-image');
    imageElements.forEach(img => {
        const randomIndex = getRandomIndex(images.length); // Jedno losowanie dla obrazu i linku
        img.src = images[randomIndex];
        img.parentElement.href = links[randomIndex]; 
    });
}

window.onload = setRandomImagesAndLinks;
 


//! ///////////////////////////////
document.addEventListener('DOMContentLoaded', function () {
    const sortForm = document.querySelector('.sorting-form');
    const sortSelect = document.querySelector('#sort');
    const itemsList = document.querySelector('.itemLi');

    function sortItems(criteria) {
        const items = Array.from(itemsList.children);

        items.sort((a, b) => {
            let aValue, bValue;
            if (criteria.field === 'price') {
                aValue = parseFloat(a.querySelector('.price').innerText);
                bValue = parseFloat(b.querySelector('.price').innerText);
            } else if (criteria.field === 'name') {
                aValue = a.querySelector('.name').innerText.toLowerCase();
                bValue = b.querySelector('.name').innerText.toLowerCase();
            }

            if (criteria.type === 'number') {
                return criteria.order === 'asc' ? aValue - bValue : bValue - aValue;
            } else if (criteria.type === 'string') {
                if (aValue < bValue) return criteria.order === 'asc' ? -1 : 1;
                if (aValue > bValue) return criteria.order === 'asc' ? 1 : -1;
                return 0;
            }
        });

        while (itemsList.firstChild) {
            itemsList.removeChild(itemsList.firstChild);
        }

        items.forEach(item => itemsList.appendChild(item));
    }

    sortForm.addEventListener('submit', function (e) {
        e.preventDefault();

        const selectedOption = sortSelect.value;
        let criteria;

        switch (selectedOption) {
            case 'price-asc':
                criteria = { field: 'price', type: 'number', order: 'asc' };
                break;
            case 'price-desc':
                criteria = { field: 'price', type: 'number', order: 'desc' };
                break;
            case 'name-asc':
                criteria = { field: 'name', type: 'string', order: 'asc' };
                break;
            case 'name-desc':
                criteria = { field: 'name', type: 'string', order: 'desc' };
                break;
            default:
                criteria = { field: 'price', type: 'number', order: 'asc' };
        }

        sortItems(criteria);
    });
});