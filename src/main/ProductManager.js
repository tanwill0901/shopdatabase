import React, {useEffect, useState} from "react";
import {
    fetchProducts,
    addProduct,
    deleteProduct,
    fetchTransactions,
    addTransaction,
    deleteTransaction,
    fetchUsers,
    addUser,
    deleteUser,
} from "../services/api";
import ProductSalesChart from "../charts/ProductSalesChart";
import ProfitMonthlyChart from "../charts/ProfitMonthlyChart";
import SalesMonthlyChart from "../charts/SalesMonthlyChart";

import "./ProductManager.css";

function ProductManager() {
    // Tabs
    const [activeTab, setActiveTab] = useState("products");

    // Role-based access (read-only from localStorage)
    const userStatus = localStorage.getItem("userstatus") || "Employee";

    // Products
    const [products, setProducts] = useState([]);
    const [newProduct, setNewProduct] = useState({
        pname: "",
        pbuy: "",
        psell: "",
    });

    // Transactions
    const [transactions, setTransactions] = useState([]);
    const [custName, setCustName] = useState("");
    const [selectedProduct, setSelectedProduct] = useState("");
    const [quantity, setQuantity] = useState(1);
    const [expandedId, setExpandedId] = useState(null);
    const [selectedChart, setSelectedChart] = useState(null);


    const [users, setUsers] = useState([]);
    const [newUser, setNewUser] = useState({
        username: "",
        userpass: "",
        userstatus: "",
    });


    useEffect(() => {
        if (activeTab === "products") {
            loadProducts();
        } else if (activeTab === "transactions") {
            loadTransactions();
        } else if (activeTab === "users" && userStatus === "Manager") {
            loadUsers();
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [activeTab]);

    // ===== PRODUCT FUNCTIONS =====
    const loadProducts = async () => {
        const res = await fetchProducts();
        setProducts(res.data);
    };

    const handleAddProduct = async () => {
        if (!newProduct.pname || !newProduct.pbuy || !newProduct.psell) {
            alert("Please fill all product fields.");
            return;
        }
        await addProduct(newProduct);
        setNewProduct({pname: "", pbuy: "", psell: ""});
        loadProducts();
    };

    const handleDeleteProduct = async (pname) => {
        if (window.confirm("Delete this product?")) {
            await deleteProduct(pname);
            loadProducts();
        }
    };

    // ===== TRANSACTION FUNCTIONS =====
    const loadTransactions = async () => {
        const res = await fetchTransactions();
        console.log("Fetched transactions:", res.data);
        setTransactions(res.data);
    };

    const handleAddTransaction = async () => {
        if (!custName || !selectedProduct || quantity <= 0) {
            alert("Please fill in all fields correctly.");
            return;
        }

        const product = products.find((p) => p.pname === selectedProduct);
        if (!product) {
            alert("Product not found!");
            return;
        }

        const transaction = {
            custName,
            items: [
                {
                    productName: product.pname,
                    quantity: parseInt(quantity),
                    productBuy: product.pbuy,
                    productSell: product.psell,
                },
            ],
        };

        await addTransaction(transaction);
        setCustName("");
        setSelectedProduct("");
        setQuantity(1);
        loadTransactions();
    };

    const handleDeleteTransaction = async (id) => {
        if (window.confirm("Delete this transaction?")) {
            await deleteTransaction(id);
            loadTransactions();
        }
    };

    const loadUsers = async () => {
        const res = await fetchUsers();
        setUsers(res.data);
    };

    const handleAddUser = async () => {
        if (!newUser.username || !newUser.userpass || !newUser.userstatus) {
            alert("Please fill in all user fields.");
            return;
        }

        await addUser(newUser);
        setNewUser({username: "", userpass: "", userstatus: ""});
        loadUsers();
    };

    const handleDeleteUser = async (username) => {
        if (window.confirm(`Delete user "${username}"?`)) {
            await deleteUser(username);
            loadUsers();
        }
    };

    // ===== LOGOUT =====
    const handleLogout = () => {
        localStorage.removeItem("userstatus");
        window.location.href = "/";
    };

    return (
        <div className="pm-container">
            {/* Header */}
            <div className="pm-header">
                <h2>
                    Welcome {userStatus === "Manager" ? "Manager" : "Employee"}
                </h2>
                <button className="pm-logout" onClick={handleLogout}>
                    Logout
                </button>
            </div>

            {/* Tabs */}
            <div className="pm-tabs">
                <button
                    className={`pm-tab ${activeTab === "products" ? "active" : ""}`}
                    onClick={() => setActiveTab("products")}
                >
                    🛍 Products
                </button>
                <button
                    className={`pm-tab ${activeTab === "transactions" ? "active" : ""}`}
                    onClick={() => setActiveTab("transactions")}
                >
                    💸 Transactions
                </button>
                {userStatus === "Manager" && (
                    <button
                        className={`pm-tab ${activeTab === "users" ? "active" : ""}`}
                        onClick={() => setActiveTab("users")}
                    >
                        👥 Users
                    </button>
                )}
                <button
                    className={`pm-tab ${activeTab === "charts" ? "active" : ""}`}
                    onClick={() => setActiveTab("charts")}
                >
                    📊 Charts
                </button>
            </div>

            <div className="pm-card">
                {activeTab === "products" && (
                    <>
                        <h3>Manage Products</h3>
                        <div className="pm-inputs">
                            <input
                                placeholder="Name"
                                value={newProduct.pname}
                                onChange={(e) =>
                                    setNewProduct({...newProduct, pname: e.target.value})
                                }
                            />
                            <input
                                placeholder="Buy Price"
                                type="number"
                                value={newProduct.pbuy}
                                onChange={(e) =>
                                    setNewProduct({...newProduct, pbuy: e.target.value})
                                }
                            />
                            <input
                                placeholder="Sell Price"
                                type="number"
                                value={newProduct.psell}
                                onChange={(e) =>
                                    setNewProduct({...newProduct, psell: e.target.value})
                                }
                            />
                            <button className="pm-btn" onClick={handleAddProduct}>
                                Add Product
                            </button>
                        </div>

                        <ul className="pm-list">
                            {products.map((p) => (
                                <li key={p.pname} className="pm-item">
                                    <span>
                                        {p.pname} — Buy: ${p.pbuy}, Sell: ${p.psell}
                                    </span>
                                    <button
                                        className="pm-delete"
                                        onClick={() => handleDeleteProduct(p.pname)}
                                    >
                                        Delete
                                    </button>
                                </li>
                            ))}
                        </ul>
                    </>
                )}

                {activeTab === "transactions" && (
                    <>
                        <h3>Record Transaction</h3>
                        <div className="pm-inputs">
                            <input
                                placeholder="Customer Name"
                                value={custName}
                                onChange={(e) => setCustName(e.target.value)}
                            />
                            <select
                                value={selectedProduct}
                                onChange={(e) => setSelectedProduct(e.target.value)}
                            >
                                <option value="">Select Product</option>
                                {products.map((p) => (
                                    <option key={p.pname} value={p.pname}>
                                        {p.pname}
                                    </option>
                                ))}
                            </select>
                            <input
                                type="number"
                                placeholder="Quantity"
                                min="1"
                                value={quantity}
                                onChange={(e) => setQuantity(e.target.value)}
                            />
                            <button className="pm-btn" onClick={handleAddTransaction}>
                                Add Transaction
                            </button>
                        </div>

                        <h4>Transaction History</h4>
                        <ul className="pm-list">
                            {transactions.length === 0 ? (
                                <p className="pm-placeholder">No transactions recorded yet.</p>
                            ) : (
                                transactions.map((t) => (
                                    <li
                                        key={t.id}
                                        className={`pm-item ${expandedId === t.id ? "expanded" : ""}`}
                                        onClick={() =>
                                            setExpandedId(expandedId === t.id ? null : t.id)
                                        }
                                        style={{cursor: "pointer"}}
                                    >
                                        <div
                                            className="pm-transaction-summary"
                                            style={{
                                                display: "flex",
                                                justifyContent: "space-between",
                                                alignItems: "center",
                                            }}
                                        >
                                            <div>
                                                <strong>{t.custName}</strong> — {t.transDate}
                                                <span style={{marginLeft: 12}}>
                                                    <b>Total:</b> $
                                                    {t.items && t.items.length > 0
                                                        ? t.items
                                                            .reduce((sum, item) => sum + item.productSell * item.quantity, 0)
                                                            .toFixed(2)
                                                        : "0.00"}
                                                </span>
                                            </div>
                                            <button
                                                className="pm-delete"
                                                onClick={(e) => {
                                                    e.stopPropagation();
                                                    handleDeleteTransaction(t.id);
                                                }}
                                                style={{marginLeft: 20}}
                                            >
                                                Delete
                                            </button>
                                        </div>

                                        {expandedId === t.id && (
                                            <ul className="pm-sublist">
                                                {t.items.map((item, i) => (
                                                    <li key={i}>
                                                        {item.productName} x {item.quantity} = $
                                                        {(item.productSell * item.quantity).toFixed(2)}
                                                    </li>
                                                ))}
                                            </ul>
                                        )}
                                    </li>
                                ))
                            )}
                        </ul>
                    </>
                )}

                {activeTab === "users" && userStatus === "Manager" && (
                    <div className="pm-users-section">
                        <h3>Manage Users</h3>

                        {/* Add User Form */}
                        <div className="pm-user-form">
                            <h4>Add New User</h4>
                            <div className="pm-inputs">
                                <input
                                    placeholder="Username"
                                    value={newUser.username}
                                    onChange={(e) =>
                                        setNewUser({
                                            ...newUser,
                                            username: e.target.value,
                                        })
                                    }
                                />
                                <input
                                    placeholder="Password"
                                    type="text"
                                    value={newUser.userpass}
                                    onChange={(e) =>
                                        setNewUser({
                                            ...newUser,
                                            userpass: e.target.value,
                                        })
                                    }
                                />
                                <select
                                    value={newUser.userstatus}
                                    onChange={(e) =>
                                        setNewUser({
                                            ...newUser,
                                            userstatus: e.target.value,
                                        })
                                    }
                                >
                                    <option value="">Select Role</option>
                                    <option value="Manager">Manager</option>
                                    <option value="Employee">Employee</option>
                                </select>
                            </div>
                            <button className="pm-btn" onClick={handleAddUser}>
                                Add User
                            </button>
                        </div>

                        <ul className="pm-list">
                            {users.length === 0 ? (
                                <p className="pm-placeholder">No users found.</p>
                            ) : (
                                users.map((u) => (
                                    <li key={u.username} className="pm-item">
                                        <span>
                                            {u.username} —{" "}
                                            <b>{u.userstatus}</b>
                                        </span>
                                        <button
                                            className="pm-delete"
                                            onClick={() =>
                                                handleDeleteUser(u.username)
                                            }
                                        >
                                            Delete
                                        </button>
                                    </li>
                                ))
                            )}
                        </ul>
                    </div>
                )}

                {activeTab === "charts" && (
                    <div className="pm-charts" style={{textAlign: "center"}}>
                        <h3>Analytics Dashboard</h3>

                        <div
                            className="pm-chart-buttons"
                            style={{
                                display: "flex",
                                justifyContent: "center",
                                gap: "20px",
                                marginBottom: "20px",
                                flexWrap: "wrap",
                            }}
                        >
                            <button className="pm-btn" onClick={() => setSelectedChart("product")}>
                                Product Sales
                            </button>

                            <button className="pm-btn" onClick={() => setSelectedChart("profit")}>
                                Monthly Profit
                            </button>

                            <button className="pm-btn" onClick={() => setSelectedChart("sales")}>
                                Monthly Sales
                            </button>
                        </div>

                        <div
                            className="pm-chart-display"
                            style={{display: "flex", justifyContent: "center"}}
                        >
                            {!selectedChart && <p>Select a chart to view analytics.</p>}

                            {selectedChart === "product" && <ProductSalesChart/>}
                            {selectedChart === "profit" && <ProfitMonthlyChart/>}
                            {selectedChart === "sales" && <SalesMonthlyChart/>}
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}

export default ProductManager;