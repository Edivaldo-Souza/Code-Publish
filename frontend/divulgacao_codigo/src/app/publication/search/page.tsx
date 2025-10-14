'use client';

import { useEffect, useState, FormEvent } from 'react';
import api from '@/lib/api';
import type { NextPage } from 'next';
import Logout from '@/components/logout';
import Link from 'next/link';
import { FaUserCircle, FaPlus } from 'react-icons/fa';
import LoadingSpinner from '@/components/loadindSpinner';
import toast from 'react-hot-toast';

interface Publication {
  id: number,
  title: string;
  programingLanguage: {
    id:number,
    name:string
  };
  authorName:string
}

const ITEMS_PER_PAGE = 5;

const HomePage:NextPage = () =>{
  const[publications,setPublications] = useState<Publication[]>([])
  const[isLoading,setIsLoading] = useState(true)
  const[username,setUsername] = useState<string>('')
  const[isProfileMenuOpen,setIsProfileMenuOpen] = useState(false)
  const[error,setError] = useState<string | null>(null)

  const toggleProfileMenu = () =>{
    setIsProfileMenuOpen(!isProfileMenuOpen);
  }

  const deletePublication = async (id:number) =>{
    try{
      const response = await api.delete(`v1/publications/${id}`)
      if(response.status === 204){
        toast.success("Publicação deletada!")
      }
      const newPublications = publications.filter(p => p.id !== id);
      setPublications(newPublications);
    }
    catch (error){
      console.log(error)
    }
  }

  useEffect(()=>{
    const fetchPublications = async () =>{
      try{
        const response = await api.get("v1/publications?q=")

        setPublications(response.data.data.content)
      } catch(error: any){
        setError(error)
      } finally{
        setIsLoading(false)
      }
    }
    fetchPublications()
    const username = localStorage.getItem("code_publish_username");
    if(username) setUsername(username)
  },[])

    if(isLoading){
      return(<div className="text-center p-10"><LoadingSpinner/>Carregando publicações</div>)
    }

    return (
    <div className="min-h-screen bg-gray-100">
      <header className="bg-indigo-600  shadow-sm">
              <div className="container mx-auto px-6 py-4 flex justify-between items-center">
                <nav className="flex items-center space-x-2" >
                  <Link
                    href={"/home"}
                    className="text-white hover:bg-purple-50 hover:text-purple-700 rounded-md px-3 py-2 transition-colors duration-200"
                  >
                    Principal
                  </Link>
                  <a 
                    href="#" 
                    className="text-white hover:bg-purple-50 hover:text-purple-700 rounded-md px-3 py-2 transition-colors duration-200"
                  >
                    Todas as publicações
                  </a>
                  <a 
                    href="#" 
                    className="text-white hover:bg-purple-50 hover:text-purple-700 rounded-md px-3 py-2 transition-colors duration-200"
                  >
                    Minhas publicações
                  </a>
                </nav>
                <div className='w-240 flex justify-end'>
                  <p className=''>{username}</p>
                </div>
                <div className="p-2.5 bg-purple-100 rounded-full cursor-pointer">
                  <button 
                    onClick={toggleProfileMenu}
                    className="p-1 rounded-full text-gray-600 hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                    <FaUserCircle className="h-6 w-6 text-purple-600" />
                  </button>
                  {isProfileMenuOpen && (
                                  <div className="origin-top-right absolute right-2 mt-2 w-48 rounded-md shadow-lg py-1 bg-white ring-1 ring-black ring-opacity-5 z-10">
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
            </header>

      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          <div className="flex justify-between items-center mb-6">
            <input className='px-4 py-2 w-100 bg-white border border-gray-300 text-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 transition' placeholder='Buscar...'></input>
            <Link href="/publication/register" className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                <FaPlus className="mr-2" />
                Criar nova publicação
            </Link>
          </div>

          <div  className="flex flex-col gap-4">
            {publications.map((publication) => (
              <div key={publication.id} className="bg-white overflow-hidden shadow rounded-lg p-4 flex items-center justify-between">
                <div>
                  <h3 className="text-lg font-medium text-gray-900">{publication.title}</h3>
                  <p className="mt-2 text-sm text-gray-500">{publication.programingLanguage.name}</p>
                </div>
                {username===publication.authorName ? (
                  <div className="mt-4 flex-shrink-0 ">
                  <Link href={`/publication/details/${publication.id}`} className="inline-flex items-center mr-4 px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                  Ver mais
                  </Link>
                  <Link href={`/publication/update/${publication.id}`} className="mr-4 inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                  Editar
                  </Link>
                  <button onClick={()=>deletePublication(publication.id)} className="mr-4 inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500">
                    Deletar
                  </button>
                </div>
                ) : (
                  <div className="mt-4 flex-shrink-0 ">
                  <Link href={`/publication/details/${publication.id}`} className="inline-flex items-center mr-4 px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                  Ver mais
                  </Link>
                </div>
                )}
              </div>
            ))}
          </div>
        </div>
      </main>
    </div>
  );
};

export default HomePage