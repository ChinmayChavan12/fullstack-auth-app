import React, { useContext, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios"
import { AppContext } from "../context/AppContext";
import { toast } from "react-toastify";
const login = () => {
  const [isCreateAccount, setIsCreateAccount] = useState(false);
  const [name,setName]=useState("")
  const [email,setEmail]=useState("")
  const [password,setpassword]=useState("")
  const [loading,setLoading]=useState(false)
  const {backendURL,setIsLoggedIn,getUserData} =useContext(AppContext);
  const navigate=useNavigate();

  function handleLogin(){
    setIsCreateAccount(false);
  }
  function handleSignUp(){
    setIsCreateAccount(true)
  }
  const onSubmitHandler= async(e)=>{
      e.preventDefault();
      axios.defaults.withCredentials=true;
      setLoading(true);
      try{
        if(isCreateAccount){
          //register api
          const  response= await axios.post(`${backendURL}/register`,{name,email,password});
          if(response.status===201){
              navigate("/");
              toast.success("Accounted Created Succesfully")
          }else{
            toast.success("Email Already Exists")
          }
        }else{
          //login api 
            const response=await axios.post(`${backendURL}/login`,{email,password});
            if(response.status===200){
              setIsLoggedIn(true);
              getUserData()
              navigate("/");
              toast.success(" Login  SucessFully")
          }else{
            toast.success("Email/Password is Incorrect")
          }
        }
      }catch(err){
          toast.error(err.message)
      }finally{
        setLoading(false)
      }
  }
  return (
    <div
      className="position-relative min-vh-100 d-flex justify-content-center align-items-center"
      style={{
        background: "linear-gradient(90deg,#6a5af9,#8268f9)",
        border: "none",
      }}
    >
      <div
        style={{
          position: "absolute",
          top: "20px",
          left: "30px",
          display: "flex",
          alignItems: "center",
        }}
      >
        <Link
          to="/"
          style={{
            display: "flex",
            gap: 5,
            alignItems: "center",
            fontWeight: "bold",
            fontSize: "24px",
            textDecoration: "none",
          }}
        >
          <img src="" alt="logo" height={32} width={32} />
          <span className="fw-bold fs-4 text-light">Authify</span>
        </Link>
      </div>
      <div className="card p-4" style={{ maxWidth: "400px", width: "100%" }}>
        <h2 className="text-center mb-4">
          {isCreateAccount ? "Create-Account" : "Login"}
        </h2>
        <form onSubmit={onSubmitHandler}>
          {isCreateAccount && (
            <div>
              <label htmlFor="name" className="form-label">
                Name:
              </label>
              <input
                type="text"
                required
                id="name"
                placeholder="Enter your Name"
                className="form-control"
                onChange={(e)=>setName(e.target.value)}
                value={name}
              />
            </div>
          )}
          <div className="mb-3">
            <label htmlFor="email" className="form-label">
              Email ID:
            </label>
            <input
              type="text"
              required
              id="email"
              placeholder="Enter your Email"
              className="form-control"
              onChange={(e)=>setEmail(e.target.value)}
              value={email}
            />
            <label htmlFor="password" className="form-label">
              Password:
            </label>
            <input
              type="password"
              required
              id="password"
              placeholder="Enter Password"
              className="form-control"
              onChange={(e)=>setpassword(e.target.value)}
              value={password}
            />
          </div>
          <div className="d-flex justify-content-between mb-3">
            <Link to="/reset-password" className=" text-decoration-none">
              Forgot password?
            </Link>
          </div>
          <button type="submit" className="btn btn-primary w-100 " disabled={loading} >
            {loading?"Loading.......":isCreateAccount ? "Sign Up" : "Login"}
          </button>
        </form>

        <div className="text-center mb-3">
          <p className="mb-0">
            {isCreateAccount ? (
              <>
                Already have an account?
                <span
                  className="text-decoration-underline"
                  style={{ cursor: "pointer" }}
                  onClick={handleLogin}
                >
                  Login here
                </span>
              </>
            ) : (
              <>
                Dont have an account
                <span
                  className="text-decoration-underline"
                  style={{ cursor: "pointer" }}
                  onClick={handleSignUp}
                >
                  Sign Up
                </span>
              </>
            )}
          </p>
        </div>
      </div>
    </div>
  );
};

export default login;
