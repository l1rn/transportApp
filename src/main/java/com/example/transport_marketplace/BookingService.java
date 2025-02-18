package com.example.transport_marketplace;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
public class BookingService {
    private static final String BOOKING_FILE_PATH = "src/main/resources/bookings.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Booking> bookings = new ArrayList<>();
    private int nextId = 1;
    public BookingService(){
        loadBookings();
    }
    private synchronized void loadBookings(){
        File file = new File(BOOKING_FILE_PATH);
        if(file.exists()){
            try {
                List<Booking> loadedBookings = objectMapper.readValue(file, new TypeReference<List<Booking>>() {});
                bookings.addAll(loadedBookings);
                nextId = bookings.stream().mapToInt(Booking::getId).max().orElse(0) + 1;
            }
            catch (IOException e){
                System.err.println("Ошибка загрузки бронирований: " + e.getMessage());
            }
        }
    }
    private synchronized void saveBookings(){
        try {
            objectMapper.writeValue(new File(BOOKING_FILE_PATH), bookings);
        } catch (IOException e) {
            System.err.println("Ошибка сохранения бронирований: " + e.getMessage());
        }
    }
    public synchronized List<Booking> getAllBookings(){
        return bookings;
    }
    public synchronized Booking createBooking(Booking booking){
        booking.setId(nextId++);
        bookings.add(booking);
        saveBookings();
        return  booking;
    }

    public synchronized boolean cancelBooking(int id){
        boolean removed = bookings.removeIf(b -> b.getId() == id);
        if(removed){
            saveBookings();
        }
        return removed;
    }
}
