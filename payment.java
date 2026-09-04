<section class="section" id="payment">
  <div class="panel">
    <h2 style="margin:0 0 10px">Secure Checkout</h2>
    <p class="p">Pay securely with Stripe (Card / Wallets where available).</p>

    <form id="payment-form">
      <div id="payment-element" class="panel" style="padding:14px"></div>

      <button id="submit" class="btn primary" style="margin-top:12px;width:100%">
        Pay Now
      </button>

      <div id="payment-message" style="margin-top:10px;color:var(--muted)"></div>
    </form>
  </div>
</section>

<!-- Stripe.js -->
<script src="https://js.stripe.com/v3/"></script>
<script>
  // 1) Put your Stripe publishable key here:
  const stripe = Stripe("pk_test_YOUR_PUBLISHABLE_KEY");

  // 2) Call your server to create a PaymentIntent for a product
  async function startEmbeddedCheckout(productId) {
    const res = await fetch("/api/create-payment-intent", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ productId })
    });
    const { clientSecret } = await res.json();

    const elements = stripe.elements({ clientSecret });
    const paymentElement = elements.create("payment");
    paymentElement.mount("#payment-element");

    const form = document.getElementById("payment-form");
    form.addEventListener("submit", async (e) => {
      e.preventDefault();
      document.getElementById("submit").disabled = true;

      const { error } = await stripe.confirmPayment({
        elements,
        confirmParams: { return_url: window.location.origin + "/success.html" }
      });

      const msg = document.getElementById("payment-message");
      if (error) msg.textContent = error.message;
      document.getElementById("submit").disabled = false;
    });
  }

  // Example: automatically load checkout for your bundle
  startEmbeddedCheckout("everything_bundle");
</script>