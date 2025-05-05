import React, {FormEvent, useState} from 'react';
import { signInWithEmailAndPassword } from 'firebase/auth';
import {auth} from '../../firebase';
import {useNavigate} from 'react-router-dom';

const Login: React.FC = () => {
  const [email, setEmail] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [error, setError] = useState<string>('');
  const navigate = useNavigate();

  const handleLogin = async (e : FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');
    try {
      await signInWithEmailAndPassword(auth, email, password);
      navigate('/');
    } catch (err: any){
      setError(err.message);
    }
  };

  return (
    <div className = "min-h-screen flex items-center justify-center bg-gray-100 px-4">
      <div className="w-full max-w-md bg-white p-8 rounded shadow">
        <h2 className="text-2xl font-bold mb-6 text-center">Login</h2>
        {error && (
          <div className="mb-4 text-red-600 bg-red-100 p-2 rounded text-sm">{error}</div>
        )}

        <form onSubmit={handleLogin} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
            <input
              type = "email"
              className = "w-full border border-gray-300 rounded px-3 py-2"
              value = {email}
              onChange={(e)=> setEmail(e.target.value)}
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Password</label>
            <input type="password" 
              className="w-full border border-gray-300 rounded px-3 py-2"
              value ={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

        </form>
      </div>
    </div>
  )
}

export default Login