import React, { useContext, useState, useRef, useEffect } from "react";
import { toast } from "react-toastify";
import axios from "axios";
import { AppContext } from "../context/AppContext";
import { Link, useNavigate } from "react-router-dom";
const EmailVerify = () => {
  const { backendURL } = useContext(AppContext);
  const inputRef = useRef([]);
  const [loading, setLoading] = useState(false);
  const { getUserData,isloggedIn,userData } = useContext(AppContext);
  const nav = useNavigate();

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
  async function handleVerify() {
    const otp = inputRef.current.map((input) => input.value).join("");
    if (otp.length !== 6) {
      toast.error("Enter all the digits ");
      return;
    }
    setLoading(true);
    try {
      const response = await axios.post(backendURL + "/verify-otp", { otp });
      if (response.status === 200) {
        toast.success("Otp Verified");
        getUserData();
        nav("/");
      } else {
        toast.error("Invalid Otp");
      }
    } catch (err) {
      toast.error("Failed please try again ");
    } finally {
      setLoading(false);
    }
  }
  useEffect(() => {
      isloggedIn&&userData&&userData.isVerified && nav("/")
  },[isloggedIn,userData])
  
  return (
    <div
      style={{
        background: "linear-gradient(90deg,#6a5af9,#8268f9)",
        borderRadius: "none",
      }}
      className="email-verify-container d-flex align-items-center justify-content-center vh-100 position-relative"
    >
      <Link
        to="/"
        className=" text-decoration-none position-absolute top-0 start-0 p-4 d-flex align-items-center gap-2"
      >
        <img src={assets.logo} alt="logo" height={32} width={32} />
        <span className="fs-4 fw-semibold text-light">Authify</span>
      </Link>
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
    </div>
  );
};

export default EmailVerify;
