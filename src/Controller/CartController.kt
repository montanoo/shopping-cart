package Controller

import Exceptions.CheckoutCancelledException
import Exceptions.EmptyCartException
import Exceptions.NegativeQuantityException
import Model.Cart
import Model.CartItem

class CartController (private val cart: Cart) {

    fun addItem (item: CartItem){
        if (item.quantity < 0) {
            throw NegativeQuantityException()
        }
         cart.items.add(item)
    }

    fun removeItem(productId: Int, quantityToRemove: Int) {
        val cartItem = cart.items.find { it.product.id == productId }

        if (cartItem != null) {
            if (cartItem.quantity > quantityToRemove) {
                cartItem.quantity -= quantityToRemove
            } else {
                cart.items.remove(cartItem)
            }
        } else {
            throw EmptyCartException()
        }
    }

    fun getTotalPrice () : Double {
        return cart.items.sumOf { it.product.price * it.quantity }
    }

    fun viewCart() {
        if (cart.items.isEmpty()) {
            throw EmptyCartException() // Throw exception if the cart is empty
        }
    }

    fun checkout() {
        if (cart.items.isEmpty()) {
            throw EmptyCartException() // Throw exception if the cart is empty
        }

        val total = getTotalPrice()
        println("The total price is: $$total")
        println("Proceed to payment? (y/n)")
        val answer = readln()
        if (answer.lowercase() == "n") {
            throw CheckoutCancelledException() // Throw exception if checkout is canceled
        }

        cart.items.clear() // Empty the cart after successful checkout
    }
}