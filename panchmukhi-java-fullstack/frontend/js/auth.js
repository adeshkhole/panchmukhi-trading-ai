const API_BASE_URL = 'http://localhost:8083/api';

document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');

    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }

    if (registerForm) {
        registerForm.addEventListener('submit', handleRegister);
    }

    // Check if user is already logged in
    const token = localStorage.getItem('authToken');
    if (token && (window.location.pathname.includes('login.html') || window.location.pathname.includes('register.html'))) {
        window.location.href = 'index.html';
    }
});

async function handleLogin(e) {
    e.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const submitBtn = e.target.querySelector('button[type="submit"]');

    // UI Feedback: Loading
    const originalBtnText = submitBtn.innerText;
    submitBtn.innerText = 'Signing In...';
    submitBtn.disabled = true;

    console.log('Attempting login for:', email);

    try {
        const response = await fetch(`${API_BASE_URL}/auth/signin`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        });

        console.log('Login response status:', response.status);

        if (response.ok) {
            const data = await response.json();
            console.log('Login success, token received');
            localStorage.setItem('authToken', data.accessToken);
            localStorage.setItem('refreshToken', data.refreshToken);
            localStorage.setItem('user', JSON.stringify(data.user));
            alert('Login Successful! Redirecting to Dashboard...');
            window.location.href = 'index.html';
        } else {
            const errorText = await response.text();
            console.error('Login failed:', errorText);
            // Try to parse JSON error if possible
            let errorMessage = errorText;
            try {
                const errorObj = JSON.parse(errorText);
                errorMessage = errorObj.message || errorObj.error || errorText;
            } catch (e) { }

            alert('Login Failed: ' + errorMessage);
        }
    } catch (error) {
        console.error('Login network error:', error);
        alert('Login failed due to network error. Ensure Backend is running at ' + API_BASE_URL);
    } finally {
        submitBtn.innerText = originalBtnText;
        submitBtn.disabled = false;
    }
}

async function handleRegister(e) {
    e.preventDefault();
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const phone = document.getElementById('phone').value;
    const password = document.getElementById('password').value;
    const submitBtn = e.target.querySelector('button[type="submit"]');

    // UI Feedback: Loading
    const originalBtnText = submitBtn.innerText;
    submitBtn.innerText = 'Registering...';
    submitBtn.disabled = true;

    console.log('Attempting registration for:', email);

    try {
        const response = await fetch(`${API_BASE_URL}/auth/signup`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name,
                email,
                phone,
                password,
                language: 'en',
                theme: 'dark',
                voiceAlerts: true
            })
        });

        console.log('Registration response status:', response.status);

        if (response.ok) {
            const data = await response.json();
            console.log('Registration success');
            localStorage.setItem('authToken', data.accessToken);
            localStorage.setItem('refreshToken', data.refreshToken);
            localStorage.setItem('user', JSON.stringify(data.user));
            alert('Registration Successful! Redirecting to Dashboard...');
            window.location.href = 'index.html';
        } else {
            const errorText = await response.text();
            console.error('Registration failed:', errorText);
            // Try to parse JSON error if possible
            let errorMessage = errorText;
            try {
                const errorObj = JSON.parse(errorText);
                errorMessage = errorObj.message || errorObj.error || errorText;
            } catch (e) { }

            alert('Registration Failed: ' + errorMessage);
        }
    } catch (error) {
        console.error('Registration network error:', error);
        alert('Registration failed due to network error. Ensure Backend is running at ' + API_BASE_URL);
    } finally {
        submitBtn.innerText = originalBtnText;
        submitBtn.disabled = false;
    }
}

// Global Logout Function
window.logout = function () {
    localStorage.removeItem('authToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
    window.location.href = 'login.html';
};
