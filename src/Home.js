import React, {useState} from "react";
import ProductManager from "./main/ProductManager";

function Home() {
    const handleLogout = () => {
        window.location.href = "/";
    };

    return (
        <div style={{padding: "20px"}}>
            <ProductManager/>
        </div>
    );
}

export default Home;