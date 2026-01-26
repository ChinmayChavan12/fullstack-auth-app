import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import 'bootstrap/dist/css/bootstrap.min.css';        // Bootstrap CSS
import 'bootstrap-icons/font/bootstrap-icons.css';    // Bootstrap Icons
import 'bootstrap/dist/js/bootstrap.bundle.js';       // Bootstrap JS (for dropdowns etc.)
import { AppContextProvider } from './context/AppContext.jsx';

createRoot(document.getElementById('root')).render(
    <AppContextProvider>
        <App />
    </AppContextProvider>
    
  
)
