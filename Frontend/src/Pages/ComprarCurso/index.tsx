import React, { useEffect, useState, type FormEvent } from "react";
import { useStripe, useElements, CardElement } from "@stripe/react-stripe-js";
import axios from "axios";
import { useSelector } from "react-redux";
import type { RootState } from "../../redux/store";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

export const ComprarCurso: React.FC = () => {
  const stripe = useStripe();
  const elements = useElements();
  const navigate = useNavigate();
  const [clientSecret, setClientSecret] = useState<string | null>(null);
  const curso = useSelector((state: RootState) => state.curso.curso);
  const usuario = useSelector((state: RootState) => state.user.user);

  useEffect(() => {
    const createPaymentIntent = async () => {
      if (!curso) return;

      if (!usuario) return;

      try {
        const response = await axios.post(
          "http://localhost:8082/curso/criarPagamento",
          {
            amount: curso.preco,
            currency: "brl",
            email: usuario.email,
          }
        );
        setClientSecret(response.data.clientSecret);
      } catch (error) {
        console.error("Erro ao criar PaymentIntent:", error);
      }
    };

    createPaymentIntent();
  }, []);

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (!stripe || !elements || !clientSecret) return;

    const cardElement = elements.getElement(CardElement);

    if (!cardElement) {
      return;
    }

    const { error, paymentIntent } = await stripe.confirmCardPayment(
      clientSecret,
      {
        payment_method: {
          card: cardElement,
        },
      }
    );

    if (error) {
      toast.error("Ocorreu um erro ao processar pagamento.");
    } else if (paymentIntent && paymentIntent.status === "succeeded") {
      toast.success("Pagamento efetuado com sucesso!");
      navigate("/");
    }
  };

  return (
    <>
      {/* Informações do curso */}
      <div className="w-full max-w-md mx-auto mt-8 bg-white shadow-md rounded-xl p-6 space-y-4 text-center">
        <p className="text-xl font-bold text-gray-800">{curso?.nome}</p>
        <p className="text-lg text-blue-600 font-semibold">
          R$ {curso?.preco?.toFixed(2)}
        </p>
        <p className="text-sm text-gray-600 italic">
          Professor: {curso?.professor}
        </p>
      </div>

      {/* Formulário de pagamento */}
      {clientSecret && (
        <form
          onSubmit={handleSubmit}
          className="w-full max-w-md mx-auto mt-6 p-6 bg-white shadow-lg rounded-xl space-y-6"
        >
          <div>
            <label className="block text-gray-700 font-semibold mb-2">
              Cartão:
            </label>
            <div className="border border-gray-300 rounded-md p-3 bg-gray-50 focus-within:ring-2 focus-within:ring-blue-500">
              <CardElement className="card-element" />
            </div>
          </div>

          <button
            type="submit"
            disabled={!stripe}
            className={`w-full py-2 px-4 rounded-md font-semibold text-white transition-colors duration-300 ${
              stripe
                ? "bg-blue-600 hover:bg-blue-700"
                : "bg-gray-400 cursor-not-allowed"
            }`}
          >
            Pagar
          </button>
        </form>
      )}
    </>
  );
};
