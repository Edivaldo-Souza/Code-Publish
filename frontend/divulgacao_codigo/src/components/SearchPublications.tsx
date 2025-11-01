'use client';

import { useEffect, useState, FormEvent, ChangeEvent, useMemo } from 'react';
import api from '@/lib/api';
import axios from "axios"
import type { NextPage } from 'next';
import Logout from '@/components/logout';
import Link from 'next/link';
import { FaUserCircle, FaPlus, FaChevronRight,FaChevronLeft } from 'react-icons/fa';
import LoadingSpinner from '@/components/loadindSpinner';
import toast from 'react-hot-toast';
import { usePagination,DOTS } from '@/app/hooks/usePagination';
import { UserDto } from '@/types/user';
import { usePathname, useRouter, useSearchParams } from 'next/navigation';

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

interface SearchPublicationsProps {
    currentUserPublications:string
    isLoggedIn:string,
}

export default function SearchPublications({currentUserPublications,isLoggedIn}:SearchPublicationsProps){
  const[publications,setPublications] = useState<Publication[]>([])
  const[isLoading,setIsLoading] = useState(true)
  const[username,setUsername] = useState<string>('')
  const[isProfileMenuOpen,setIsProfileMenuOpen] = useState(false)
  const[totalPages,setTotalPages]=useState<number>(1)
  const[searchInput,setSearchInput] = useState<string>('')
  const[error,setError] = useState<string | null>(null)

  const router = useRouter()
  const pathname = usePathname()
  const searchParams = useSearchParams()

  const currentPage = useMemo(() => {
    const pageFromUrl = searchParams.get('lastPage');
    const pageNumber = Number(pageFromUrl);

    if (!pageFromUrl || isNaN(pageNumber) || pageNumber < 1) {
      return 1;
    }
    if (pageNumber > totalPages) {
      return totalPages;
    }
    return pageNumber;
  }, [searchParams, totalPages]);

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

      if(newPublications.length===0){
        const params = new URLSearchParams(searchParams.toString());
        params.set('lastPage', String(currentPage-1));
        
        router.push(`${pathname}?${params.toString}`)
      }
      else setPublications(newPublications);
    }
    catch (error){
      console.log(error)
    }
  }

  const fetchPublications = async (pageIndex:number) =>{
      try{
        const response = await api.get(`v1/publications?currentUserPublications=${currentUserPublications}&size=${ITEMS_PER_PAGE}&page=${pageIndex}&q=${searchInput}`)
        setTotalPages(response.data.data.totalPages)
        setPublications(response.data.data.content)
      } catch(error){
        if(axios.isAxiosError(error)){
          toast.error(`${error.response?.data.error}`)
        }
      }
    }

  useEffect(()=>{
    fetchPublications(currentPage-1)
    setIsLoading(false)
    if(isLoggedIn==="true"){
      const username = localStorage.getItem("code_publish_username");
      if(username) {
        setUsername(username)
      }
      else{
        const getCurrentUser = async() =>{
          const userDto = await api.get("v1/users/current");
          localStorage.setItem("code_publish_username",userDto.data.data.username)
          setUsername(userDto.data.data.username) 
        }
        getCurrentUser()
      }
    }
    else localStorage.removeItem("code_publish_username");
  },[])

  const paginationRange = usePagination({
    totalPageCount:totalPages,
    currentPage,
    siblingCount:1,
  })

  const handlePageChange = async (pageNumber: number,shouldFetch:boolean) => {
      if(shouldFetch){
        fetchPublications(pageNumber-1)
      }
      const params = new URLSearchParams(searchParams.toString());
      params.set('lastPage', String(pageNumber));

      router.push(`${pathname}?${params.toString()}`, { scroll: false });
  };

  const handlePrevPage = async () => {
      fetchPublications(currentPage-2)
      handlePageChange(currentPage-1,false)
  };
  
  const handleNextPage = async () => {
      fetchPublications(currentPage)
      handlePageChange(currentPage+1,false)
  };

  const handleSearchInputChange = async (e:ChangeEvent<HTMLInputElement>) =>{
    try{
        let value = e.target.value
        if(value.includes("#")){
          value = value.replace("#","%23")
        }
        if(value.includes("+")){
          value = value.replace("+","%2B").replace("+","%2B")
        }
        const response = await api.get(`v1/publications?currentUserPublications=${currentUserPublications}&size=${ITEMS_PER_PAGE}&page=${0}&q=${value}`)
        setTotalPages(response.data.data.totalPages)
        setPublications(response.data.data.content)
        setSearchInput(value)
    } catch(error){
       if(axios.isAxiosError(error)){
          toast.error(`${error.response?.data.error}`)
        }
    }
  }

    //if(isLoading){
    //  return(<div className="text-center p-10"><LoadingSpinner/>Carregando publicações</div>)
    //}

    return (
    <div className="min-h-screen bg-gray-100">
      <header className="bg-indigo-600  shadow-sm">
        {(isLoggedIn==="true") ? 
          <div className="container mx-auto px-6 py-4 flex justify-between items-center">
                <nav className="flex items-center space-x-2" >
                  <Link
                    href={"/home"}
                    className="text-white hover:bg-purple-50 hover:text-purple-700 rounded-md px-3 py-2 transition-colors duration-200"
                  >
                    Principal
                  </Link>
                  <Link 
                    href={`/publication/search/false/true`} 
                    className= {currentUserPublications==="false" ?
                      "bg-purple-50 text-purple-700 rounded-md px-3 py-2 transition-colors duration-200"
                     : "text-white hover:bg-purple-50 hover:text-purple-700 rounded-md px-3 py-2 transition-colors duration-200"}
                    >
                    Todas as publicações
                  </Link>
                  <Link 
                    href={`/publication/search/true/true`} 
                    className={currentUserPublications==="true" ? 
                      "bg-purple-50 text-purple-700 rounded-md px-3 py-2 transition-colors duration-200"
                      : "text-white hover:bg-purple-50 hover:text-purple-700 rounded-md px-3 py-2 transition-colors duration-200"
                    }
                  >
                    Minhas publicações
                  </Link>
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
                                    <Link
                                      href="/user"
                                      className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                                    >
                                      Editar dados pessoais
                                    </Link>
                                    <Logout>
                                    </Logout>
                                  </div>
                                )}
                </div>
              </div> : 
              <div className="container mx-auto px-6 py-4 flex justify-end items-center">
                <Link
                className="inline-flex items-center px-4 py-2 mr-4 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-400 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                  href={"/login"}
                >
                  Entrar
                </Link>
                <Link
                className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-400 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                  href={"/register"}
                >
                  Cadastrar-se
                </Link>
              </div>
              }
            </header>

      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          <div className="flex justify-between items-center mb-6">
            <input
            onChange={handleSearchInputChange}
            className='px-4 py-2 w-100 bg-white border border-gray-300 text-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 transition' 
            placeholder='Buscar...'></input>
            {(isLoggedIn==="true") ? 
              <Link href="/publication/register" className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                  <FaPlus className="mr-2" />
                  Criar nova publicação
              </Link> : null}
          </div>

          {publications.length > 0 ?  
          <div>
            <div  className="flex flex-col gap-4">
            {publications.map((publication) => (
              <div key={publication.id} className="bg-white overflow-hidden shadow rounded-lg p-4 flex items-center justify-between">
                <div>
                  <h3 className="text-lg font-medium text-gray-900">{publication.title}</h3>
                  <p className="mt-2 text-sm text-gray-500">{publication.programingLanguage.name}</p>
                </div>
                {username===publication.authorName ? (
                  <div className="mt-4 flex-shrink-0 ">
                  <Link href={`/publication/details/${publication.id}?redirect_to=${encodeURIComponent(`${pathname}?lastPage=${currentPage}`)}`} className="inline-flex items-center mr-4 px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
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
                  <Link href={`/publication/details/${publication.id}?redirect_to=${encodeURIComponent(`${pathname}?lastPage=${currentPage}`)}`} className="inline-flex items-center mr-4 px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                  Ver mais
                  </Link>
                </div>
                )}
              </div>
            ))}
          </div>
          <nav className="flex justify-center items-center mt-8" aria-label="Pagination">
          <button 
            onClick={handlePrevPage}
            disabled={currentPage === 1}
            className="p-2 rounded-lg text-slate-500 hover:bg-slate-200 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <FaChevronLeft className="h-5 w-5" />
          </button>
          
          <div className="flex items-center space-x-2 mx-4">
            {paginationRange?.map((pageNumber,index) => {
              
              if (pageNumber === DOTS) {
                return (
                  <span key={`dots-${index}`} className="text-slate-500 px-2">
                    &#8230;
                  </span>
                );
              }

              return (
                <button
                  key={pageNumber}
                  onClick={() => handlePageChange(pageNumber as number,true)}
                  className={`
                    text-sm font-bold w-8 h-8 flex items-center justify-center rounded-lg transition-colors
                    ${currentPage === pageNumber 
                      ? 'bg-indigo-600 text-white' 
                      : 'text-slate-600 hover:bg-slate-200'}
                  `}
                >
                  {pageNumber}
                </button>
              );
            })}
          </div>

          <button 
            onClick={handleNextPage}
            disabled={currentPage === totalPages}
            className="p-2 rounded-lg text-slate-500 hover:bg-slate-200 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <FaChevronRight className="h-5 w-5" />
          </button>
        </nav>
          </div> : <p
          className='text-black'
          >Nenhuma publicação cadastrada</p>}  
          
        </div>
      </main>
    </div>
  );
};