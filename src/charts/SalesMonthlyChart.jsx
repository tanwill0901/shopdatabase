import {useEffect, useState} from "react";
import axios from "axios";
import {fetchMonthlySales} from "../services/api";
import {BarChart, Bar, XAxis, YAxis, Tooltip, CartesianGrid} from "recharts";

function SalesMonthlyChart() {
    const [data, setData] = useState([]);

    useEffect(() => {
        // axios.get("/api/analytics/sales-monthly")
        fetchMonthlySales().then(res => setData(res.data));
        //.catch(err => console.error(err));
    }, []);

    return (
        <div>
            <h2>Total Sales Per Month</h2>
            <BarChart width={600} height={300} data={data}>
                <CartesianGrid strokeDasharray="3 3"/>
                <XAxis dataKey="month"/>
                <YAxis/>
                <Tooltip/>
                <Bar dataKey="totalSales" fill="#8884d8"/>
            </BarChart>
        </div>
    );
}

export default SalesMonthlyChart;