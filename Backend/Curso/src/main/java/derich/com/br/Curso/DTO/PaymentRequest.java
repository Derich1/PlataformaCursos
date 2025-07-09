package derich.com.br.Curso.DTO;

public record PaymentRequest(
        Long amount,
        String currency
) {}
