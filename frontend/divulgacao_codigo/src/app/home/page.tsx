'use client';

import { useEffect, useState } from 'react';
import api from '@/lib/api';
import type { NextPage } from 'next';
import Logout from '@/components/logout';
import Link from 'next/link';
import { FaUserCircle, FaPlus } from 'react-icons/fa';
import LoadingSpinner from '@/components/loadindSpinner';

interface UserData {
  id: string,
  username: string;
  email: string;
}

const ITEMS_PER_PAGE = 5;

const HomePage:NextPage = () =>{
  const[publications,setPublications] = useState<UserData[]>([])
  const[isLoading,setIsLoading] = useState(true)
  const[isProfileMenuOpen,setIsProfileMenuOpen] = useState(false)
  const[error,setError] = useState<string | null>(null)


  const toggleProfileMenu = () =>{
    setIsProfileMenuOpen(!isProfileMenuOpen);
  }

  const handleLogout = () =>{
    setIsProfileMenuOpen(false)
  }

  useEffect(()=>{
    const fetchPublications = async () =>{
      try{
        const response = await api.get("v1/users")

        setPublications(response.data.data)
      } catch(error: any){
        setError(error)
      } finally{
        setIsLoading(false)
      }
    }

    fetchPublications()
  },[])

    if(isLoading){
      return(<div className="text-center p-10"><LoadingSpinner/>Carregando publicações</div>)
    }

    if (error) {
      return <div className="text-center p-10 text-red-600">Erro: {error}</div>;
    }

    return (
    <div className="min-h-screen bg-gray-100">
      {/* Barra de Navegação */}
      <nav className="bg-indigo-600 shadow-md">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex-shrink-0">
              <input placeholder='Pesquisar...'/>
            </div>
            <div className="relative">
              <button
                onClick={toggleProfileMenu}
                className="p-2 rounded-full text-gray-600 hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                <FaUserCircle size={28} />
              </button>
              {isProfileMenuOpen && (
                <div className="origin-top-right absolute right-0 mt-2 w-48 rounded-md shadow-lg py-1 bg-white ring-1 ring-black ring-opacity-5 z-10">
                  <a
                    href="#"
                    className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                  >
                    Editar dados pessoais
                  </a>
                  <Logout>
                  </Logout>
                </div>
              )}
            </div>
          </div>
        </div>
      </nav>

      {/* Conteúdo Principal */}
      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          <div className="flex justify-between items-center mb-6">
            <h1 className="text-2xl font-semibold text-gray-900">Lista de Entidades</h1>
            <Link href="/publication/register" className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                <FaPlus className="mr-2" />
                Criar Nova Entidade
            </Link>
          </div>

          <div  className="flex flex-col gap-4">
            {publications.map((user) => (
              <div key={user.id} className="bg-white overflow-hidden shadow rounded-lg p-4 flex items-center justify-between">
                <div>
                  <h3 className="text-lg font-medium text-gray-900">{user.username}</h3>
                  <p className="mt-2 text-sm text-gray-500">{user.email}</p>
                </div>
                <div className="mt-4 flex-shrink-0">
                  <Link href={`/entidade/${user.id}`} className="text-indigo-600 hover:text-indigo-900 font-semibold">
                  Ver mais
                  </Link>
                </div>
              </div>
            ))}
          </div>
        </div>
      </main>
    </div>
  );
};

export default HomePage