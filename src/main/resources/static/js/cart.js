const shoppingCart = (function () {
  // Private methods and propeties
  cart = [];

  // Constructor
  function Item(id, barcode, name, price, count) {
      this.id = id;
      this.barcode = barcode;
      this.name = name;
      this.price = price;
      this.count = count;
  }

  // Save cart
  function saveCart() {
      sessionStorage.setItem('shoppingCart', JSON.stringify(cart));
  }

  // Load cart
  function loadCart() {
      cart = JSON.parse(sessionStorage.getItem('shoppingCart'));
  }
  if (sessionStorage.getItem('shoppingCart') != null) {
      loadCart();
  }

  // Public methods and properties
  const obj = {};

  // Add to cart
  obj.addItemToCart = function (id, barcode, name, price, count) {
      for (const item in cart) {
          if (cart[item].name === name) {
              cart[item].count++;
              saveCart();
              return;
          }
      }
      const item = new Item(id, barcode, name, price, count);
      cart.push(item);
      saveCart();
  };
  // Set count from item
  obj.setCountForItem = function (name, count) {
      for (const i in cart) {
          if (cart[i].name === name) {
              cart[i].count = count;
              break;
          }
      }
  };
  // Remove item from cart
  obj.removeItemFromCart = function (name) {
      for (const item in cart) {
          if (cart[item].name === name) {
              cart[item].count--;
              if (cart[item].count === 0) {
                  cart.splice(item, 1);
              }
              break;
          }
      }
      saveCart();
  };

  // Remove all items from cart
  obj.removeItemFromCartAll = function (name) {
      for (const item in cart) {
          if (cart[item].name === name) {
              cart.splice(item, 1);
              break;
          }
      }
      saveCart();
  };

  // Count cart
  obj.totalCount = function () {
      return cart.length;
  };

  // Total cart
  obj.totalCart = function () {
      let totalCart = 0;
      for (const item in cart) {
          totalCart += cart[item].price * cart[item].count;
      }
      return totalCart;
  };

  // List cart
  obj.listCart = function () {
      const cartCopy = [];
      for (i in cart) {
          item = cart[i];
          itemCopy = {};
          for (p in item) {
              itemCopy[p] = item[p];
          }
          itemCopy.total = item.price * item.count;
          cartCopy.push(itemCopy);
      }
      return cartCopy;
  };

  return obj;
})();

// Add item
$('.add-to-cart').on('click', function (event) {
  event.preventDefault();
  const id = $(this).data('id');
  const name = $(this).data('name');
  const price = $(this).data('price');
  const barcode = $(this).data('barcode');
  shoppingCart.addItemToCart(id, barcode, name, price, 1);
  displayCart();
});

function formatCurrency(price) {
  if (price && price != 0) return price.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1.') + ' ₫';
  return 'Liên hệ';
}

function displayCart() {
  const cartArray = shoppingCart.listCart();
  let output = `
    <div class='d-flex justify-content-between align-items-center mb-3'>
        <h6 class='mb-0'>Barcode</h6>
        <h6 class='mb-0'>Name</h6>
        <h6 class='mb-0'>Price</h6>
        <h6 class='mb-0'>Quantity</h6>
        <h6 class='mb-0'>Total price</h6>
        <h6 class='mb-0'>Action</h6>
    </div>`;
  for (let i in cartArray) {
      output += `<div class='card mb-3 shadow-none'>
                      <div class='card-body'>
                          <div class='d-flex justify-content-between align-items-center'>
                              <p class='mb-0'>${cartArray[i].barcode}</p>
                              <p class='mb-0'>${cartArray[i].name}</p>
                              <p class='mb-0'>${formatCurrency(cartArray[i].price)}</p>
                              <div style='width: 140px;'>
                                  <div class='input-group'>
                                      <button class='minus-item input-group-addon btn btn-light' data-name='${
                                          cartArray[i].name
                                      }'>-</button>
                                      <input type='number' min='1' class='item-count form-control' data-name='${
                                          cartArray[i].name
                                      }' value='${cartArray[i].count}'>
                                      <button class='plus-item btn btn-light input-group-addon' data-name='${
                                          cartArray[i].name
                                      }'>+</button>
                                  </div>
                              </div>
                              <p class='mb-0'>${formatCurrency(cartArray[i].total)}</p>
                              <button class='delete-item btn btn-danger' data-name='${
                                  cartArray[i].name
                              }'><i class='fas fa-trash-alt'></i></button>
                          </div>
                      </div>
                  </div>`;
  }
  $('.show-cart').html(output);
  $('.total-cart').html(formatCurrency(shoppingCart.totalCart()));
  $('#total-inp').val(shoppingCart.totalCart());
  $('.total-count').html(shoppingCart.totalCount());
}

// Delete item button
$('.show-cart').on('click', '.delete-item', function (event) {
  const name = $(this).data('name');
  shoppingCart.removeItemFromCartAll(name);
  displayCart();
});

// -1
$('.show-cart').on('click', '.minus-item', function (event) {
  const name = $(this).data('name');
  shoppingCart.removeItemFromCart(name);
  displayCart();
});

// +1
$('.show-cart').on('click', '.plus-item', function (event) {
  const name = $(this).data('name');
  shoppingCart.addItemToCart('', '', name);
  displayCart();
});

// Item count input
$('.show-cart').on('change', '.item-count', function (event) {
  const name = $(this).data('name');
  const count = Number($(this).val());
  shoppingCart.setCountForItem(name, count);
  displayCart();
});

displayCart();