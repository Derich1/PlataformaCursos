
import { loadStripe } from '@stripe/stripe-js';
import { Elements } from '@stripe/react-stripe-js';
import { ComprarCurso } from '../ComprarCurso';

const stripeKey = import.meta.env.STRIPE_KEY
const stripePromise = loadStripe(stripeKey);

export default function PagamentoStripe () {
  return (
    <div className="checkout-container">
      <Elements stripe={stripePromise}>
        <ComprarCurso />
      </Elements>
    </div>
  );
}
