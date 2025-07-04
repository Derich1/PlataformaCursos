
import { loadStripe } from '@stripe/stripe-js';
import { Elements } from '@stripe/react-stripe-js';
import { ComprarCurso } from '../ComprarCurso';

const stripePromise = loadStripe('pk_test_51Qx7AiFDDwihPybqNan3a0nMOmNHeVuabPQRYet4zXp5JH2RuAFjkKrZzVt572yxjJohhdwWQHRJk1FpG7QMfhOc00H1Pc45IF');

export default function PagamentoStripe () {
  return (
    <div className="checkout-container">
      <Elements stripe={stripePromise}>
        <ComprarCurso />
      </Elements>
    </div>
  );
}
