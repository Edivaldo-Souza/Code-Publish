
"use client";

import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useState, ChangeEvent, FormEvent } from 'react';
import toast from 'react-hot-toast';

interface FormData {
  nome: string;
  email: string;
  senha: string;
  confirmarSenha: string;
}

export default function PaginaCadastro() {
  const router = useRouter()

  const [formData, setFormData] = useState<FormData>({
    nome: '',
    email: '',
    senha: '',
    confirmarSenha: '',
  });

  const [isLoading, setIsLoading] = useState(false)
  const [erros, setErros] = useState<Partial<FormData>>({});
  const [apiError,setApiError] = useState<string | null>(null);

  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const novosErros: Partial<FormData> = {};

    // Validações simples
    if (!formData.nome) novosErros.nome = 'O nome é obrigatório.';
    if (!formData.email) {
      novosErros.email = 'O e-mail é obrigatório.';
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      novosErros.email = 'O formato do e-mail é inválido.';
    }
    if (!formData.senha) {
      novosErros.senha = 'A senha é obrigatória.';
    } else if (formData.senha.length < 8) {
      novosErros.senha = 'A senha deve ter no mínimo 8 caracteres.';
    } else if (formData.senha !== formData.confirmarSenha){
        novosErros.confirmarSenha = 'As senhas não estão iguais'
    }

    setErros(novosErros)
    
    if(Object.keys(novosErros).length === 0){
        setIsLoading(true);
        try{
            const requestForm = {
                username: formData.nome,
                email: formData.email,
                password: formData.senha
            }

            const response = await fetch(process.env.NEXT_PUBLIC_API_URL+"/v1/users",
                {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(requestForm),
                }
            )

            const data = await response.json();

            if(!response.ok){
                throw new Error(data.message || "Não foi possível cadastrar o usuário")
            }

            toast.success("Usuário cadastrado com sucesso!")
            router.push("/login")

        }
        catch(error: any){
            setApiError(error);
        } finally{
            setIsLoading(false);
        }
    }

  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <div className="w-full max-w-md p-8 space-y-2 bg-white rounded-lg shadow-md">
        <h1 className="text-2xl font-bold text-center text-gray-900">
          Crie sua Conta
        </h1>
        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <input
              id="nome"
              name="nome"
              type="text"
              className="w-full px-3 py-2 mt-1 border text-gray-700 border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              placeholder="Nome do usuário"
              value={formData.nome}
              onChange={handleChange}
            />
            {erros.nome && <p className="mt-1 text-xs text-red-600">{erros.nome}</p>}
          </div>

          <div>
            <input
              id="email"
              name="email"
              type="text"
              className="w-full px-3 py-2 mt-1 border text-gray-700 border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              placeholder="Email"
              value={formData.email}
              onChange={handleChange}
            />
            {erros.email && <p className="mt-1 text-xs text-red-600">{erros.email}</p>}
          </div>

          <div>
            <input
              id="senha"
              name="senha"
              type="password"
              required
              className="w-full px-3 py-2 mt-1 border text-gray-700 border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              placeholder="Senha"
              value={formData.senha}
              onChange={handleChange}
            />
            {erros.senha && <p className="mt-1 text-xs text-red-600">{erros.senha}</p>}
          </div>

          <div>
            <input
              id="confirmarSenha"
              name="confirmarSenha"
              type="password"
              required
              className="w-full px-3 py-2 mt-1 border text-gray-700 border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              placeholder="Confirmar senha"
              value={formData.confirmarSenha}
              onChange={handleChange}
            />
            {erros.confirmarSenha && <p className="mt-1 text-xs text-red-600">{erros.confirmarSenha}</p>}
          </div>

            {apiError && (
                <div className="p-2 text-sm text-center text-red-800 bg-red-100 rounded-md">
              {apiError}
            </div>
            )}

          <div>
            <button
              type="submit"
              className="w-full px-4 py-2 text-sm font-medium text-white bg-indigo-600 border border-transparent rounded-md shadow-sm hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
              {isLoading ? 'Cadastrando...' : 'Cadastrar'}
            </button>
          </div>
        </form>
          <Link href='/login'
          className="w-full block text-center px-4 py-2 text-sm font-medium text-indigo-600 bg-transparent border rounded-md shadow-sm hover:bg-indigo-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
          Voltar
          </Link>
      </div>
    </div>
  );
}