
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';

// Define schema with Zod
const registrationSchema = z.object({
  name: z.string().min(2, 'O nome deve ter pelo menos 3 caracteres'),
  email: z.string().email('E-mail inválido'),
  password: z.string().min(6, 'Senha deve ter no mínimo 6 caracteres'),
});

type RegistrationData = z.infer<typeof registrationSchema>;

export default function RegistrationPage() {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<RegistrationData>({
    resolver: zodResolver(registrationSchema),
  });

  const onSubmit = async (data: RegistrationData) => {
    try {
      console.log('Submitting', data);
      // TODO: chamar API de cadastro
      alert('Cadastro realizado com sucesso!');
    } catch (error) {
      console.error(error);
      alert('Erro no cadastro');
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10 p-6 bg-white shadow-md rounded-md">
      <h2 className="text-2xl font-semibold text-center mb-6">Cadastro de Usuário</h2>
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        <div>
          <label className="block text-sm font-medium mb-1">Nome</label>
          <input
            type="text"
            {...register('name')}
            className="w-full border border-gray-300 rounded p-2"
          />
          {errors.name && <p className="text-red-500 text-sm mt-1">{errors.name.message}</p>}
        </div>

        <div>
          <label className="block text-sm font-medium mb-1">E-mail</label>
          <input
            type="email"
            {...register('email')}
            className="w-full border border-gray-300 rounded p-2"
          />
          {errors.email && <p className="text-red-500 text-sm mt-1">{errors.email.message}</p>}
        </div>

        <div>
          <label className="block text-sm font-medium mb-1">Senha</label>
          <input
            type="password"
            {...register('password')}
            className="w-full border border-gray-300 rounded p-2"
          />
          {errors.password && <p className="text-red-500 text-sm mt-1">{errors.password.message}</p>}
        </div>

        <button
          type="submit"
          disabled={isSubmitting}
          className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 transition"
        >
          {isSubmitting ? 'Cadastrando...' : 'Cadastrar'}
        </button>
      </form>
    </div>
  );
}
