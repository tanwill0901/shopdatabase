import axios from "axios";

const API = axios.create({
    baseURL: "http://localhost:8080/shop-web/api",
});
// const API = axios.create({
//     baseURL: process.env.REACT_APP_API_URL
// });



export const loginAdmin = (username, userpass) =>
    API.post("/login", {username, userpass});

export const registerUser = (user) => API.post("/users", {...user, action: "register"});
export const loginUser = (credentials) => API.post("/login", {...credentials, action: "login"});

export const fetchProducts = () => API.get("/products");
export const addProduct = (product) => API.post("/products", product);
export const deleteProduct = (pname) => API.delete(`/products?pname=${encodeURIComponent(pname)}`);

export const fetchTransactions = () => API.get("/transactions");
export const addTransaction = (transaction) => API.post("/transactions", transaction);

export const deleteTransaction = (transactionId) =>
    API.delete(`/transactions/${transactionId}`);

export const fetchUsers = () => API.get("/users");
export const addUser = (user) => API.post("/users", user);
export const deleteUser = (username) =>
    API.delete(`/users?username=${encodeURIComponent(username)}`);

export const fetchMonthlySales = () =>
    API.get("/analytics/sales-monthly");

export const fetchMonthlyProfit = () =>
    API.get("/analytics/profit-monthly");

export const fetchWeeklySales = () =>
    API.get("/analytics/weekly-sales");

export const fetchExpenseIncome = () =>
    API.get("/analytics/expense-income");

export const fetchProductSales = () =>
    API.get("/analytics/product-sales");

export default API;