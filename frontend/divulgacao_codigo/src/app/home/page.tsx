'use client';

import { useEffect, useState, FormEvent } from 'react';
import api from '@/lib/api';
import type { NextPage } from 'next';
import Logout from '@/components/logout';
import Link from 'next/link';
import { FaUserCircle, FaPlus, FaSearch } from 'react-icons/fa';
import { UserDto } from '@/types/user';

const HomePage:NextPage = () =>{
  const[isProfileMenuOpen,setIsProfileMenuOpen] = useState(false)
  const[user,setUser] = useState<UserDto>()

  const toggleProfileMenu = () =>{
    setIsProfileMenuOpen(!isProfileMenuOpen);
  }

  useEffect(()=>{

    const getCurrentUser = async() =>{
      const userDto = await api.get("v1/users/current");
      if(userDto) {
        const userProfile:UserDto = userDto.data.data
        setUser(userProfile)
      };
    }
    getCurrentUser();
  },[])


    return (
    <div className="min-h-screen bg-gray-100">
      <header className="bg-indigo-600  shadow-sm">
        <div className="container mx-auto px-6 py-4 flex justify-end items-center">
          <nav className="flex items-center space-x-2" >

          </nav>
          <p className='mr-4'>{user?.username}</p>
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

      <main className="container mx-auto px-6 py-12">
        <section className="flex flex-col md:flex-row md:space-x-12 mb-12">
          <div className="mb-8 md:mb-0">
            <p className="text-gray-500 text-lg">Código compartilhados:</p>
            <h1 className="text-6xl font-bold text-gray-800">{user?.totalPublications}</h1>
          </div>
          <div>
            <p className="text-gray-500 text-lg">Total de avaliações:</p>
            <h1 className="text-6xl font-bold text-gray-800">{user?.totalRatings}</h1>
          </div>
        </section>

        <section className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8">
          <Link className="bg-white p-8 h-56 rounded-xl shadow-md hover:shadow-xl transition-shadow duration-300 flex flex-col items-center justify-center space-y-4 cursor-pointer"
              href="/publication/register">

            <span className="text-lg font-medium text-gray-700">Criar publicação</span>
            <FaPlus className="h-5 w-5 text-black" />
          </Link>

          <Link className="bg-white p-8 h-56 rounded-xl shadow-md hover:shadow-xl transition-shadow duration-300 flex flex-col items-center justify-center space-y-4 cursor-pointer"
                href={"/publication/search/false/true"}>
            <span className="text-lg font-medium text-gray-700">Ver publicações</span>
            <FaSearch className="h-5 w-5 text-black" />
          </Link>

          <Link className="bg-white p-8 h-56 rounded-xl shadow-md hover:shadow-xl transition-shadow duration-300 flex flex-col items-center justify-center space-y-4 cursor-pointer"
            href={"/publication/search/true/true"}>
            <span className="text-lg font-medium text-gray-700">Ver minhas publicações</span>
            <FaSearch className="h-5 w-5 text-black" />
          </Link>
        </section>
      </main>
    </div>
  );
};

export default HomePage