package com.example.transport_marketplace.service.validators;

import com.example.transport_marketplace.enums.BookingStatus;
import com.example.transport_marketplace.enums.PaymentStatus;
import com.example.transport_marketplace.exceptions.booking.BookingCancelledException;
import com.example.transport_marketplace.exceptions.booking.BookingDoesNotBelongUserException;
import com.example.transport_marketplace.exceptions.booking.BookingPaidException;
import com.example.transport_marketplace.exceptions.payment.PaymentHasProcessedException;
import com.example.transport_marketplace.exceptions.routes.RouteNotFoundException;
import com.example.transport_marketplace.exceptions.user.UserHasNoEmailException;
import com.example.transport_marketplace.model.Booking;
import com.example.transport_marketplace.model.Payment;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.repo.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentValidator {
    @Autowired
    private final PaymentRepository paymentRepository;

    public boolean checkPaymentPendingByBooking(Booking booking){
        return paymentRepository
                .findFirstByBookingIdAndPaymentStatusInOrderByCreatedAtDesc(
                        booking.getId(),
                        List.of(PaymentStatus.PENDING)
                )
                .isPresent();
    }

    public void validatePaymentEligibility(User user, Booking booking){
        validateUserEligibility(user, booking);
        validateBookingEligibility(booking);
        validateExistingPayments(booking.getId());
    }

    private void validateUserEligibility(User user, Booking booking){
        if(user.getEmail() == null){
            throw new UserHasNoEmailException("У пользователя не был привязан email!");
        }

        if(!user.isEnabled()){
            throw new RuntimeException("Аккаунт пользователя не был активирован!");
        }

        if(booking.getUser() != user){
            throw new BookingDoesNotBelongUserException("Booking does not belong to the user");
        }
    }

    private void validateBookingEligibility(Booking booking){
        if(booking.getStatus() == BookingStatus.CANCELLED){
            throw new BookingCancelledException();
        }

        if(booking.getStatus() == BookingStatus.PAID){
            throw new BookingPaidException();
        }
        if (booking.getRoute() == null) {
            throw new RouteNotFoundException("К букингу не привязан маршрут!");
        }
    }

    private void validateExistingPayments(Integer bookingId) {
        List<PaymentStatus> statuses = Arrays.asList(
                PaymentStatus.PENDING,
                PaymentStatus.SUCCEEDED
        );

        Optional<Payment> hasConflictingPayment = paymentRepository.findFirstByBookingIdAndPaymentStatusInOrderByCreatedAtDesc(bookingId, statuses);
        if(hasConflictingPayment.isPresent()){
            throw new PaymentHasProcessedException(hasConflictingPayment.get().getExternalId().toString());
        }
    }
}
