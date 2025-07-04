import React, { useEffect, useState, type FormEvent } from "react";
import { useStripe, useElements, CardElement } from '@stripe/react-stripe-js';
import axios from "axios";
import { useSelector } from "react-redux";
import type { RootState } from "../../redux/store";


export const ComprarCurso: React.FC = () => {
  const stripe = useStripe();
  const elements = useElements();
  const [clientSecret, setClientSecret] = useState<string>('');
  const curso = useSelector((state: RootState) => state.curso.curso)

  useEffect(() => {
    const createPaymentIntent = async () => {
        if (!curso) return
        try {
            const response = await axios.post('http://localhost:8082/criarPagamento', {
                amount: curso.preco, 
                currency: 'brl'
            });
            setClientSecret(response.data.clientSecret);
        } catch (error) {
            console.error('Erro ao criar PaymentIntent:', error);
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

    const { error, paymentIntent } = await stripe.confirmCardPayment(clientSecret, {
      payment_method: {
        card: cardElement,
      },
    });

    if (error) {
    } else if (paymentIntent && paymentIntent.status === 'succeeded') {
    }
  };

  return (
    <form onSubmit={handleSubmit} className="checkout-form">
      <label>
        Cartão:
        <CardElement className="card-element" />
      </label>
      <button type="submit" disabled={!stripe}>Pagar</button>
    </form>
  );
}
