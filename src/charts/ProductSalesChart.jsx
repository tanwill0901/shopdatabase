import {useEffect, useState} from "react";
import axios from "axios";
import {fetchProductSales} from "../services/api";
import {BarChart, Bar, XAxis, YAxis, Tooltip, CartesianGrid} from "recharts";

export default function ProductSalesChart() {
    const [data, setData] = useState([]);

    useEffect(() => {
        //axios.get("/api/analytics/product-sales")
        fetchProductSales()
            .then(res => setData(res.data))
        //  .catch(err => console.error(err));
    }, []);

    return (
        <div>
            <h2>Units Sold Per Product</h2>
            <BarChart width={650} height={350} data={data}>
                <CartesianGrid strokeDasharray="3 3"/>
                <XAxis dataKey="product"/>
                <YAxis/>
                <Tooltip/>
                <Bar dataKey="quantitySold" fill="#BA68C8"/>
            </BarChart>
        </div>
    );
}