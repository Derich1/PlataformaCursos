import { Button } from "../../components/ui/button";
import { Label } from "../..//components/ui/label"
import { Card, CardContent, CardHeader, CardTitle } from "../../components/ui/card";
import { Input } from "../../components/ui/input";
import axios from "axios";
import { useState } from "react";
import { toast } from "react-toastify";
import { Link, useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { loginSuccess } from "../../redux/userSlice";

export default function Login() {

    const [email, setEmail] = useState("")
    const [senha, setSenha] = useState("")  
    const navigate = useNavigate()
    const dispatch = useDispatch()

    const handleSubmit = async(e: any) => {
        e.preventDefault()
        try {
            const loginDTO = {
                email,
                senha
            }
            const response = await axios.post("http://localhost:8081/usuario/login", loginDTO)

            if (response.status === 200){
                dispatch(loginSuccess(response.data))
                console.log(response.data)
                toast.success("Login efetuado com sucesso!")
                navigate("/")
                return
            }
            toast.error("Erro ao fazer login, verifique suas credenciais.")
        } catch (error) {
            console.log(error)
        }
    }

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100 px-4">
      <Card className="w-full max-w-md shadow-xl rounded-2xl">
        <CardHeader>
          <CardTitle className="text-2xl text-center">Entrar na Plataforma</CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="space-y-2">
              <Label htmlFor="email">E-mail</Label>
              <Input id="email" type="email" placeholder="seu@email.com" value={email} onChange={(e) => setEmail(e.target.value)} required />
            </div>
            <div className="space-y-2">
              <Label htmlFor="password">Senha</Label>
              <Input id="password" type="password" placeholder="••••••••" value={senha} onChange={(e) => setSenha(e.target.value)} required />
            </div>
            <Button type="submit" className="w-full cursor-pointer">
              Entrar
            </Button>
          </form>
          <p className="text-center text-sm text-muted-foreground mt-4">
            Não tem uma conta? <Link to="/cadastrar" className="underline">Cadastre-se</Link>
          </p>
        </CardContent>
      </Card>
    </div>
  )
}