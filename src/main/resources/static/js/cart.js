// cart.js

document.addEventListener('DOMContentLoaded', () => {
    const cartItems = document.querySelectorAll('#cart-list .product-info');
    const totalPriceElem = document.getElementById('total-price');

    function updateTotalPrice() {
        let totalPrice = 0;
        cartItems.forEach(item => {
            const price = parseFloat(item.querySelector('.price').textContent);
            const quantity = parseInt(item.querySelector('.quantity').value);
            totalPrice += price * quantity;
        });
        totalPriceElem.textContent = totalPrice.toFixed(2);
    }

    cartItems.forEach(item => {
        const quantityInput = item.querySelector('.quantity');
        const removeButton = item.querySelector('.remove-item');

        quantityInput.addEventListener('change', updateTotalPrice);
        removeButton.addEventListener('click', () => {
            item.parentElement.remove();
            updateTotalPrice();
        });
    });

    updateTotalPrice();
});
