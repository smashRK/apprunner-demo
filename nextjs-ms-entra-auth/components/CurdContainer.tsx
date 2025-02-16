import { useEffect, useState } from "react";

interface Product {
  id: number;
  productName: string;
  description: string;
  quantity: number;
}

const CurdContainer = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [quantity, setQuantity] = useState(1);
  const [editIndex, setEditIndex] = useState<number | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Fetch products from the API (GET request)
  const fetchProducts = async () => {
    try {
      setLoading(true);
      setError(null);
      console.log("Fetching products...");
      const response = await fetch(`/api/users`);
      console.log("Response status:", response.status);
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      const data = await response.json();
      console.log("Received data:", data);
      setProducts(Array.isArray(data) ? data : []);
    } catch (error) {
      console.error("Error fetching products:", error);
      setError("Failed to fetch products. Please try again later.");
    } finally {
      setLoading(false);
    }
  };

  // Add or Update Product (POST or PUT request)
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    try {
      let response;
      console.log("Submitting product data:", { productName: name, description, quantity });

      const productData = {
        productName: name,
        description,
        quantity
      };

      if (editIndex !== null) {
        console.log("Updating product with ID:", products[editIndex].id);
        response = await fetch(
          `/api/users/${products[editIndex].id}`,
          {
            method: "PUT",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(productData),
          }
        );
      } else {
        console.log("Creating new product");
        response = await fetch(`/api/users`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(productData),
        });
      }

      console.log("Response status:", response.status);
      
      if (!response.ok) {
        const errorData = await response.text();
        console.error("Error response:", errorData);
        throw new Error(`HTTP error! status: ${response.status}, message: ${errorData}`);
      }

      const savedProduct = await response.json();
      console.log("Server response:", savedProduct);

      if (editIndex !== null) {
        // Update existing product in the list
        const updatedProducts = [...products];
        updatedProducts[editIndex] = savedProduct;
        setProducts(updatedProducts);
        setEditIndex(null);
      } else {
        // Add new product to the list
        setProducts(prevProducts => [...prevProducts, savedProduct]);
      }

      // Reset form
      setName("");
      setDescription("");
      setQuantity(1);

      // Refresh the product list
      fetchProducts();
    } catch (error) {
      console.error("Error saving product:", error);
      setError(error instanceof Error ? error.message : "Failed to save product");
    }
  };

  // Delete Product (DELETE request)
  const handleDelete = async (id: number) => {
    try {
      setError(null);
      const response = await fetch(`/api/users/${id}`, {
        method: "DELETE",
      });

      if (!response.ok) {
        const errorData = await response.text();
        throw new Error(`HTTP error! status: ${response.status}, message: ${errorData}`);
      }

      // Remove the product from the state
      const updatedProducts = products.filter((p) => p.id !== id);
      setProducts(updatedProducts);
    } catch (error) {
      console.error("Error deleting product:", error);
      setError(error instanceof Error ? error.message : "Failed to delete product");
    }
  };

  // Edit Product
  const handleEdit = (index: number) => {
    const productToEdit = products[index];
    setName(productToEdit.productName);
    setDescription(productToEdit.description);
    setQuantity(productToEdit.quantity);
    setEditIndex(index);
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  return (
    <div className="mt-2 p-6 max-w-2xl mx-auto bg-white shadow-xl rounded-lg">
      <h1 className="text-3xl font-bold text-center text-gray-800 mb-6">
        Product Management
      </h1>
      <form onSubmit={handleSubmit} className="space-y-6 max-w-2xl mx-auto">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Product Name
          </label>
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Enter product name"
            className="w-full h-10 px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
            required
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Product Description
          </label>
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Enter product description"
            className="w-full h-32 px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 resize-none"
            required
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Quantity
          </label>
          <input
            type="number"
            value={quantity}
            onChange={(e) => setQuantity(parseInt(e.target.value) || 1)}
            min="1"
            className="w-full h-10 px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
            required
          />
        </div>
        <button
          type="submit"
          className="w-full h-10 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 transition-colors"
        >
          {editIndex !== null ? "Update Product" : "Add Product"}
        </button>
        {error && (
          <div className="mt-4 p-4 text-red-700 bg-red-100 rounded-md">
            {error}
          </div>
        )}
      </form>
      <ul className="space-y-4">
        {loading ? (
          <div>Loading...</div>
        ) : (
          products.map((product, index) => (
            <li
              key={index}
              className="p-4 bg-gray-50 rounded-lg shadow-sm hover:shadow-md transition-shadow"
            >
              <div className="flex justify-between items-center">
                <div>
                  <h3 className="text-lg font-semibold text-gray-800">
                    {product.productName}
                  </h3>
                  <p className="text-sm text-gray-600 mt-1">
                    {product.description}
                  </p>
                  <p className="text-sm text-gray-600 mt-1">
                    Quantity:{" "}
                    <span className="font-medium">{product.quantity}</span>
                  </p>
                </div>
                <div className="flex space-x-2">
                  <button
                    onClick={() => handleEdit(index)}
                    className="px-3 py-1 bg-yellow-500 text-white rounded-lg hover:bg-yellow-600 focus:outline-none focus:ring-2 focus:ring-yellow-500"
                  >
                    Edit
                  </button>
                  <button
                    onClick={() => handleDelete(product.id)}
                    className="px-3 py-1 bg-red-500 text-white rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-500"
                  >
                    Delete
                  </button>
                </div>
              </div>
            </li>
          ))
        )}
      </ul>
    </div>
  );
};

export default CurdContainer;
