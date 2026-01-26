import { createBrowserRouter, RouterProvider } from "react-router-dom";
import {ToastContainer} from "react-toastify"
import Home from "./components/Home"
import Login from "./components/Login"
import EmailVerify from "./components/EmailVerify"
import ResetPassword from "./components/ResetPassword"
const router=createBrowserRouter([
  {
  path: "/",
  element: <Home />
},
{
  path: "/home",
  element: <Home />
}
,{path:"/login",
    element:<Login/>
  },{path:"/email-verify",
    element:<EmailVerify/>
  },{path:"/reset-password",
    element:<ResetPassword/>
  },{path:"/login",
    element:<Login/>
  },
])


function App() {
  

  return (
    <>
      <ToastContainer/>
      <RouterProvider router={router} />
    </>
  )
}

export default App
