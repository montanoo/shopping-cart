package Model

class ProductList {
    val products: List<Product> = listOf(
        Product(1, "Laptop", 999.99),
        Product(2, "Smartphone", 699.99),
        Product(3, "Headphones", 199.99),
        Product(4, "Smartwatch", 299.99),
        Product(5, "Tablet", 399.99),
        Product(6, "Bluetooth Speaker", 129.99),
        Product(7, "Gaming Console", 499.99),
        Product(8, "4K TV", 799.99),
        Product(9, "Wireless Mouse", 49.99),
        Product(10, "Mechanical Keyboard", 109.99),
        Product(11, "External Hard Drive", 89.99),
        Product(12, "Portable Charger", 29.99),
        Product(13, "Action Camera", 299.99),
        Product(14, "Drone", 799.99),
        Product(15, "Fitness Tracker", 149.99),
        Product(16, "Smart Home Hub", 199.99),
        Product(17, "VR Headset", 399.99),
        Product(18, "Robot Vacuum", 249.99),
        Product(19, "3D Printer", 999.99),
        Product(20, "Digital SLR Camera", 1199.99),
        Product(21, "Gaming Chair", 249.99),
        Product(22, "Office Desk", 299.99),
        Product(23, "Noise-Cancelling Headphones", 349.99),
        Product(24, "Electric Scooter", 549.99),
        Product(25, "Smart Thermostat", 199.99)
    )

    fun getProductById(productId: Int): Product? {
        return products.find { it.id == productId }
    }

    fun showProducts() {
        println("Available Products:")
        products.forEach { println("${it.id}. ${it.name} - $${it.price}") }
    }

    fun searchProductByName(productName: String): List<Product> {
        return products.filter { it.name.contains(productName, ignoreCase = true) }
    }
}
