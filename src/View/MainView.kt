package View

import Controller.CartController
import Exceptions.*
import Model.Cart
import Model.User
import Model.CartItem
import Model.ProductList

class MainView {
    private lateinit var user: User
    private val cart = Cart()  // Cart is still initialized here
    private val cartController = CartController(cart)  // Pass cart to the controller
    private val productList = ProductList()  // Initialize the ProductList

    fun init() {
        println("--------------------------")
        println("Welcome to DSM's Shop")
        println("What's your name?")
        user = User(readln())

        println("Welcome to the Shop, ${user.name}")
        mainMenu()
    }

    fun mainMenu() {
        println("--------------------------")
        println("1. View Products")
        println("2. View Cart")
        println("3. Checkout")
        println("4. Delete a product")
        println("0. Exit")
        println("--------------------------")
        println("Please select an option: ")
        when (readln().toInt()) {
            1 -> viewProducts()
            2 -> {
                try {
                    cartController.viewCart()
                    cart.items.forEach { item ->
                        println("${item.quantity} x ${item.product.name} @ ${item.product.price} each")
                    }
                } catch (e: EmptyCartException) {
                    println(e.message)
                }

                mainMenu()
            }
            3 -> {
                try {
                    cartController.checkout()
                    println("Payment successful. Thank you for shopping!")
                } catch (e: EmptyCartException) {
                    println(e.message)
                } catch (e: CheckoutCancelledException) {
                    println(e.message)
                }

                mainMenu()
            }
            4 -> {
                removeItem()
                mainMenu()
            }
            0 -> println("Thank you for visiting DSM's Shop. Goodbye!")
            else -> {
                println("Invalid option, please try again.")
                mainMenu()
            }
        }
    }

    private fun viewProducts() {
        // Show all available products before prompting the user for action
        productList.showProducts()  // Display all products from ProductList

        println("Would you like to:")
        println("1. Search for a product by name")
        println("2. Add a product by ID directly")
        println("0. Go back to the main menu")
        val choice = readln().toInt()

        if (choice == 0) {
            mainMenu()
            return
        }

        when (choice) {
            1 -> searchByName()  // Go to search by name
            2 -> addById()  // Go to add by ID
            else -> {
                println("Invalid option, please try again.")
                viewProducts()
            }
        }
    }

    private fun searchByName() {
        println("Enter the product name to search (or type 0 to go back):")
        val input = readln()

        if (input == "0") {
            mainMenu()
            return
        }

        try {
            val searchResults = productList.searchProductByName(input)
            if (searchResults.isEmpty()) {
                println("No products found with the name \"$input\".")
                mainMenu()
                return
            } else if (searchResults.size == 1) {
                val selectedProduct = searchResults[0]
                println("You found: ${selectedProduct.name} - $${selectedProduct.price}")
                println("Would you like to add this product to your cart? If yes, enter the quantity. If not, press 0 to go back:")
                val quantity = readln().toInt()

                if (quantity == 0) {
                    mainMenu()
                    return
                }

                // Create CartItem and add to cart
                val cartItem = CartItem(selectedProduct, quantity)
                try {
                    cartController.addItem(cartItem)
                } catch (e: NegativeQuantityException) {
                    println(e.message)
                    return mainMenu()
                }

                throw ItemAddedSuccessfullyException(selectedProduct.name, quantity)
            } else {
                println("Multiple products found:")
                searchResults.forEach { println("${it.id}. ${it.name} - $${it.price}") }
                println("Please enter the product ID from the results to add to your cart (or type 0 to go back):")
                val selectedId = readln().toInt()

                if (selectedId == 0) {
                    mainMenu()
                    return
                }

                val selectedProduct = productList.getProductById(selectedId)

                if (selectedProduct != null) {
                    println("You selected: ${selectedProduct.name} - $${selectedProduct.price}")
                    println("Would you like to add this product to your cart? If yes, enter the quantity. If not, press 0 to go back:")
                    val quantity = readln().toInt()

                    if (quantity == 0) {
                        mainMenu()
                        return
                    }

                    // Create CartItem and add to cart
                    val cartItem = CartItem(selectedProduct, quantity)
                    cartController.addItem(cartItem)

                    throw ItemAddedSuccessfullyException(selectedProduct.name, quantity)
                } else {
                    throw ProductNotFoundException()
                }
            }
        } catch (e: ProductNotFoundException) {
            println(e.message)
        } catch (e: ItemAddedSuccessfullyException) {
            println(e.message)
        }

        mainMenu()
    }

    private fun addById() {
        println("Enter the product ID to add to your cart (or type 0 to go back):")
        val selectedProductId = readln().toInt()

        if (selectedProductId == 0) {
            mainMenu()
            return
        }

        val selectedProduct = productList.getProductById(selectedProductId)
        try {
            if (selectedProduct != null) {
                println("You selected: ${selectedProduct.name} - $${selectedProduct.price}")
                println("How many would you like to add?")
                val quantity = readln().toInt()

                // Create CartItem and add to cart
                val cartItem = CartItem(selectedProduct, quantity)
                try {
                    cartController.addItem(cartItem)
                } catch (e: NegativeQuantityException) {
                    println(e.message)
                    return mainMenu()
                }

                throw ItemAddedSuccessfullyException(selectedProduct.name, quantity)
            } else {
                throw ProductNotFoundException()
            }
        } catch (e: ProductNotFoundException) {
            println(e.message)
        } catch (e: ItemAddedSuccessfullyException) {
            println(e.message)
        }

        mainMenu()
    }

    private fun removeItem() {
        productList.showProducts()  // Display all products from ProductList

        println("Select a product by ID to remove from your cart (or type 0 to go back):")
        val selectedProductId = readln().toInt()

        if (selectedProductId == 0) {
            mainMenu()
            return
        }

        val selectedProduct = productList.getProductById(selectedProductId)
        try {
            if (selectedProduct != null) {
                println("How many would you like to remove?")
                val quantityToRemove = readln().toInt()

                // Call controller to remove item by quantity
                cartController.removeItem(selectedProductId, quantityToRemove)
                println("${quantityToRemove}x ${selectedProduct.name} removed from your cart.")
            } else {
                throw ProductNotFoundException()
            }
        } catch (e: EmptyCartException) {
            println(e.message)
        } catch (e: ProductNotFoundException) {
            println(e.message)
        }
    }
}
