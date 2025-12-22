package derich.com.br.curso.DTO;

public record PaymentRequest(
        Long amount,
        String currency,
        String email
) {}
