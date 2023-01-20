package com.base.BaseDependencies.Utils;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class AccountNumberGenerator {
    
    public long generateAccountNumber(int length) {
        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return Long.parseLong(new String(digits));
    }
}
