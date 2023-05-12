package com.sonakshi.ebookMaker.services;

import org.springframework.stereotype.Service;

@Service
public class ISBNGeneratorService {

    public String generateISBN() {
        // This is a simple implementation to generate an ISBN-13 number.
        // A real implementation should follow the ISBN standard and include publisher and title information.
        int[] digits = new int[13];
        for (int i = 0; i < 12; i++) {
            digits[i] = (int) (Math.random() * 10);
        }
        digits[12] = calculateCheckDigit(digits);
        StringBuilder isbn = new StringBuilder();
        for (int digit : digits) {
            isbn.append(digit);
        }
        return isbn.toString();
    }

    private int calculateCheckDigit(int[] digits) {
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += (i % 2 == 0 ? 1 : 3) * digits[i];
        }
        return (10 - (sum % 10)) % 10;
    }
}