import React, { useContext, useRef, useState } from 'react'
import { Link, Navigate, useNavigate } from 'react-router-dom'
import { AppContext } from '../context/AppContext'
import logo from "../assets/logo.png";
import axios from 'axios';
import { toast } from 'react-toastify';

const ResetPassword = () => {

  const inputRef=useRef([])
  const [loading,setLoading]=useState(false)
  const [email,setEmail]=useState("")
  const [newpassword,setNewPassword]=useState("")
  const[isEmailSent,setIsEmailSent]=useState(false)
  const [otp,setOtp]=useState("")
  const [isOtpSubmitted,setIsOtpSubmitted]=useState(false)
  const {getUserData,isloggedIN,userData,backendURL}=useContext(AppContext)
  const nav=useNavigate();

  const handleChange = (e, index) => {
    const value = e.target.value.replace(/\D/, "");
    e.target.value = value;
    if (value && index < 5) {
      inputRef.current[index + 1].focus();
    }
  };
  const handleKeyDown = (e, index) => {
    if (e.key === "Backspace" && !e.target.value && index > 0) {
      inputRef.current[index - 1].focus();
    }
  };

  const handlePaste = (e) => {
  e.preventDefault(); // stop browser from inserting into the focused input
  const paste = e.clipboardData.getData("text").slice(0, 6).split("");

  paste.forEach((digit, i) => {
    if (inputRef.current[i]) {
      inputRef.current[i].value = digit;
    }
  });

  const next = Math.min(paste.length, 5);
  inputRef.current[next]?.focus();
};

  
    const onSubmitEmail=async(e)=>{
        e.preventDefault()
        setLoading(true)
        try{
          axios.defaults.withCredentials=true
          const response=await axios.post(backendURL+"/send-reset-otp?email="+email)
          if(response.status===200){
            toast.success("Password reset otp send  succesfully")
            setIsEmailSent(true)
          }else{
            toast.error("Somethng ven wrong please try again ")
          }
        }catch(error){
          toast.error(error.message)
        }finally{
          setLoading(false)
        }
    }
    const handleVerify= ()=>{
      const otp =inputRef.current.map((input)=>input.value).join("");
      if(otp.length!==6){
        toast.error("Please enter all 6 digits of the otp")
      }
      setOtp(otp)
      setIsOtpSubmitted(true)
    }
    const onSubmitNewPassword=async(e)=>{
      e.preventDefault()
      setLoading(true)
      try{
        const response=await axios.post(backendURL+"/reset-password",{email,otp,newPassword:newpassword})
        if(response.status===200){
          toast.success("Passwored resest done")
          nav("/login")
        }else{
          toast.error("Please try again ")
        }
      }catch(error){
        toast.error(error.message)
      }finally{
        setLoading(false)
      }
    }
  return (
    <div style={{background:"linear-gradient(90deg,#6a5af9,#8268f9",border:"none"}} className=' d-flex align-items-center justify-content-center vh-100 position-relative'>
     
      <Link to="/" className='position-absolute text-decoration-none top-0 start-0 p-4 d-flex align-items-center gap-2' >
        
          <img src={logo} alt="logo" width={32} height={32}/>
          <span className='text-light fw-semibold fs-4'>Authify</span>
      </Link>

        {/*reset password card*/ }
        {!isEmailSent&&(
          <div className=' shadow rounded-4 p-4 bg-white text-center' style={{width:'100%',maxWidth:"400px"}}>
              <h4 className='mb-2'>Reset Password</h4>
              <p className='mb-4'>Enter your registered Email Address</p>
              <form action="">
                <div className='input-group mb-4 bg-secondary bg-opacity-10 rounded-pill'>
                  <span className='input-group-text bg-transparent border-0 ps-4'>
                    <i className='bi bi-envelope '></i>
                  </span>
                  <input value={email} required onChange={(e)=>setEmail(e.target.value)} type='email' style={{height:'50px' }} placeholder='Enter Email' className='email form-control bg-transparent border-0 ps-1 pe-4 rounded-end' />
                </div>
                  <button disabled={loading} onClick={onSubmitEmail} className='btn btn-primary w-100 py-2' type='submit'>{loading?"Loading...":"Submit"}</button>
              </form>
          </div>
                
        )}
        {/* otp card */}
        {!isOtpSubmitted && isEmailSent &&(
          <div className="p-5 rounded-4 shadow bg-white" style={{ width: "400px" }}>
        <h4 className="text-center fw-bold mv-2">Email Verify Otp</h4>
        <p className="text-center text-dark mb-4">
          Enter the 6 digit code send to your email
        </p>
        <div className="d-flex text-center justify-content-between gap-2 mb-2">
          {[...Array(6)].map((_, i) => (
            <input
              key={i}
              type="text"
              maxLength={1}
              className="form-control text-center fs-4 otp-input"
              style={{ width: "40px", fontSize: "20px" }}
              ref={(el) => (inputRef.current[i] = el)}
              onChange={(e) => handleChange(e, i)}
              onKeyDown={(e) => handleKeyDown(e, i)}
              onPaste={handlePaste}
            />
          ))}
        </div>
        <button
          onClick={handleVerify}
          className="btn btn-primary w-100 fw-semibold"
          disabled={loading}
        >
          {loading ? "Verifying....." : "Verify Email"}
        </button>
      </div>
        )}
        {isOtpSubmitted && isEmailSent &&(
          <div className="rounded-4 p-4 bg-white shadow text-center" style={{width:"100%",maxWidth:"400px"}}>
            <h4>New Password</h4>
            <p className="mb-4">Enter the new password below</p>
            <form onSubmit={onSubmitNewPassword} >
              <div className="input-group mb-4 bg-secondary bg-opacity-10 rounded-pill">
                <span className='input-group-text bg-transparent border-0 ps-4'>
                  <i className='bi bi-person-fill-lock'></i>
                </span>
                <input style={{height:'50px'}} value={newpassword} onChange={(e)=>setNewPassword(e.target.value)} type="password" required placeholder='Enter the new Password' className='form-control bg-trasparent ps-1 pe-4 rounded-end' />
              </div>
              <button type='submit' onClick={onSubmitNewPassword} disabled={loading} className='btn btn-primary w-100'>{loading?"Loading....":"Submit"}</button>
            </form>
          </div>
        )}
    </div>
  )
}

export default ResetPassword
