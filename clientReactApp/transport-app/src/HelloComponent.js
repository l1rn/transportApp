import React, { useEffect, useState } from "react";
import axios from "axios";

const HelloComponent = () => {
    const [message, setMessage] = useState("Loading...");

    useEffect(() => {
        axios.get("http://localhost:8080/routes")
            .then(response => {
                setMessage(response.data);
            })
            .catch(error => {
                setMessage("Error: " + error.message);
            });
    }, []);

    return (
        <div>
            <h3>Message from Backend:</h3>
            <p>{message}</p>
        </div>
    );
};

export default HelloComponent;
