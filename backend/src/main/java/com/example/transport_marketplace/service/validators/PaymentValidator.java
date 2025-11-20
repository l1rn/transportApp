package com.example.transport_marketplace.service.validators;

import com.example.transport_marketplace.enums.BookingStatus;
import com.example.transport_marketplace.enums.PaymentStatus;
import com.example.transport_marketplace.exceptions.booking.BookingCancelledException;
import com.example.transport_marketplace.exceptions.booking.BookingPaidException;
import com.example.transport_marketplace.exceptions.routes.RouteNotFoundException;
import com.example.transport_marketplace.exceptions.user.UserHasNoEmailException;
import com.example.transport_marketplace.model.Booking;
import com.example.transport_marketplace.model.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class PaymentValidator {

    public void validatePaymentEligibility(User user, Booking booking){
        validateUserEligibility(user);
    }

    private void validateUserEligibility(User user){
        if(user.getEmail() == null){
            throw new UserHasNoEmailException("У пользователя не был привязан email!");
        }

        if(!user.isEnabled()){
            throw new RuntimeException("Аккаунт пользователя не был активирован!");
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

    private void validateExistingPayments(Integer bookingId){
        List<PaymentStatus> statuses = Arrays.asList(
                PaymentStatus.PENDING,
                PaymentStatus.SUCCEEDED
        );

    }
}
