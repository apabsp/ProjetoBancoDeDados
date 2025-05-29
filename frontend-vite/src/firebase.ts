// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyA6Fd0z_hRypmwh32XJFhSKkFMrDFP-jdk",
  authDomain: "projetobd-28873.firebaseapp.com",
  projectId: "projetobd-28873",
  storageBucket: "projetobd-28873.firebasestorage.app",
  messagingSenderId: "200257490667",
  appId: "1:200257490667:web:f453025f2b71b5488a78ae",
  measurementId: "G-5DF4NWCZTN"
};

// Initialize Firebase
export const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);