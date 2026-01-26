import React, { useRef, useState,useContext } from "react";
import { useNavigate } from "react-router-dom";
import { AppContext} from "../context/AppContext";

const MenuBar = () => {
   const {userData} =useContext(AppContext);
    const[dropdown,setDropdown]=useState(false)
    const dropdownref=useRef(false)

  const navigate=useNavigate()
  function handleLogin(){
    navigate('/login')
  }
  return (
    <nav className="navbar bg-white px-5 py-4 d-flex justify-content-between align-align-items-center ">
      <div className="d-flex align-items-center gap-2">
        {/* TODO:add home logo here */}
        <img src="" alt="logo" width={32} height={32} />
        <span className="fw-bold fs-4 text-dark">Authify</span>
      </div>

        {userData?(
            <div className="position-relative" ref={dropdownref}>
                <div onClick={()=>setDropdown((prev)=>!prev)} className="bg-dark text-white rounded-circle d-flex justify-content-center align-items-center" style={{width:"40px",height:"40px",cursor:"pointer",userSelect:"none"}}>
                    {userData.name[0].toUpperCase()}
                </div>
                {dropdown &&(
                  <div className="position-absolute shadow bg-white rounded p-2"style={{top:"50px",right:0,zindex:100}}>

                    {!userData.isAccountVerified &&(
                      <div  className="dropdown-item py-1 px-2" style={{cursor:"pointer"}}> 
                            Verify Email
                      </div>
                    )}
                      <div className="dropdown-item py-1 px-2 text-danger" style={{cursor:"pointer"}}>
                          Logout
                      </div>
                  </div>
                )}
            </div>
        ):(
        <div className="btn btn-outline-dark rounded-pill px-3" onClick={handleLogin}>
        Login<i className="bi bi-arrow-right ms-2"></i>
      </div>)}

      
    </nav>
  );
};

export default MenuBar;
