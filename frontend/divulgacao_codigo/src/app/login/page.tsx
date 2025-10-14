'use client'; // Necessário no App Router para usar hooks como useState e useRouter

import { useState, FormEvent } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import api from '@/lib/api';
import Link from 'next/link';
import toast from 'react-hot-toast';

export default function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const router = useRouter();
  const searchParams = useSearchParams();

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
  event.preventDefault();
  setIsLoading(true);
  setError(null);

  try {
    const response = await api.post('/v1/auth/signin-cookie', {
      email: email,
      password: password,
    });

    const currentUser = await api.get('v1/users/current')

    localStorage.setItem("code_publish_username",currentUser.data.data.username)
    const redirectTo = searchParams.get('redirect_to');
    toast.success("Login efetuado com sucesso!")
    router.push( redirectTo || '/home');

  } catch (err: any) {
    if (err.response && err.response.status === 401) {
      setError('Credenciais inválidas.');
    } else {
      setError('Ocorreu um erro. Tente novamente mais tarde.');
    }
  } finally {
    setIsLoading(false);
  }
};

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <div className="w-full max-w-md p-8 space-y-6 bg-white rounded-lg shadow-md">
      <h1 className="text-2xl font-bold text-center text-gray-900">
        Entrar
       </h1>
      <form onSubmit={handleSubmit} className="space-y-6">
        <div>
          <input
            id="email"
            type="text"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="w-full px-3 py-2 mt-1 border text-gray-700 border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
            placeholder='Email'
            style={{ width: '100%', padding: '8px' }}
          />
        </div>
        <div>
          <input
            id="password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="w-full px-3 py-2 mt-1 border text-gray-700 border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
            placeholder='Senha'
            style={{ width: '100%', padding: '8px' }}
          />
        </div>
        {error && <p style={{ color: 'red' }}>{error}</p>}
        <button type="submit" disabled={isLoading} 
        className="w-full px-4 py-2 text-sm font-medium text-white bg-indigo-600 border border-transparent rounded-md shadow-sm hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
          {isLoading ? 'Entrando...' : 'Entrar'}
        </button>
      </form>
      <div className='flex text-black justify-center items-center'>
        <p>Não possuí uma conta?</p>
        <Link href='/register' style={{color:'blue',textDecoration:'underline',cursor:'pointer'}}>Cadastre-se</Link>
      </div>
      </div>
    </div>
  );
}