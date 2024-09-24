package Exceptions

class EmptyCartException : Exception("Your cart is empty.")
class ProductNotFoundException : Exception("Product not found.")
class CheckoutCancelledException : Exception("Checkout was cancelled.")
class ItemAddedSuccessfullyException(val productName: String, val quantity: Int) :
    Exception("$quantity x $productName added to the cart.")
class NegativeQuantityException : Exception("Negative quantity.")