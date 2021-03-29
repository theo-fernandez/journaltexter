import React, {useState} from 'react';
import TextBox from "./TextBox";
import './css/LoginPage.css'
import {Link, useHistory} from "react-router-dom";

function LoginPage() {
    const [username, setUserName] = useState("");
    const [password, setPassword] = useState("");
    const history = useHistory();

    function handleClick() {
        console.log(username);
        console.log(password);
        // history.push('/login');
    }

    return (
        <div className="loginBox">
            <h1>JournalTexter</h1>
            <TextBox text="Username" change={setUserName} type="text"/>
            <TextBox text="Password" change={setPassword} type="password"/>
            <p>
                <button onClick={handleClick}>Submit</button>
            </p>
            <p>
                <Link to="/signin">Sign Up!</Link>
            </p>
        </div>
    );
}

export default LoginPage;