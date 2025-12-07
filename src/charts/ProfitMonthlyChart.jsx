import {useEffect, useState} from "react";
import axios from "axios";
import {fetchMonthlyProfit} from "../services/api";
import {LineChart, Line, XAxis, YAxis, Tooltip, CartesianGrid} from "recharts";

function ProfitMonthlyChart() {
    const [data, setData] = useState([]);

    useEffect(() => {
        //axios.get("/api/analytics/profit-monthly")
        fetchMonthlyProfit().then(res => setData(res.data));
        // .catch(err => console.error(err));
    }, []);

    return (
        <div>
            <h2>Profit Per Month</h2>
            <LineChart width={600} height={300} data={data}>
                <CartesianGrid strokeDasharray="3 3"/>
                <XAxis dataKey="month"/>
                <YAxis/>
                <Tooltip/>
                <Line type="monotone" dataKey="profit" stroke="#82ca9d"/>
            </LineChart>
        </div>
    );
}

export default ProfitMonthlyChart;