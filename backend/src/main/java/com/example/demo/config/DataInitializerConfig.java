package com.example.demo.config;

import com.example.demo.entity.Coupon;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.CouponRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
@Slf4j
public class DataInitializerConfig {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository,
                                      ProductRepository productRepository,
                                      CouponRepository couponRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                log.info("Initializing test data...");

                // Create test users
                User buyer = new User();
                buyer.setUsername("buyer");
                buyer.setEmail("buyer@example.com");
                buyer.setPassword(passwordEncoder.encode("password"));
                buyer.setRole(User.Role.BUYER);
                userRepository.save(buyer);

                User seller = new User();
                seller.setUsername("seller");
                seller.setEmail("seller@example.com");
                seller.setPassword(passwordEncoder.encode("password"));
                seller.setRole(User.Role.SELLER);
                seller = userRepository.save(seller);

                // Create test products
                Product product1 = new Product();
                product1.setName("Laptop");
                product1.setDescription("High-performance laptop for professionals");
                product1.setPrice(new BigDecimal("999.99"));
                product1.setStock(10);
                product1.setSellerId(seller.getId());
                product1.setImageUrl("/images/products/laptop.png");
                productRepository.save(product1);

                Product product2 = new Product();
                product2.setName("Wireless Mouse");
                product2.setDescription("Ergonomic wireless mouse with long battery life");
                product2.setPrice(new BigDecimal("29.99"));
                product2.setStock(50);
                product2.setSellerId(seller.getId());
                product2.setImageUrl("/images/products/mouse.png");
                productRepository.save(product2);

                Product product3 = new Product();
                product3.setName("Mechanical Keyboard");
                product3.setDescription("RGB mechanical keyboard with blue switches");
                product3.setPrice(new BigDecimal("79.99"));
                product3.setStock(25);
                product3.setSellerId(seller.getId());
                product3.setImageUrl("/images/products/keyboard.png");
                productRepository.save(product3);

                Product product4 = new Product();
                product4.setName("USB-C Hub");
                product4.setDescription("7-in-1 USB-C hub with HDMI and SD card reader");
                product4.setPrice(new BigDecimal("49.99"));
                product4.setStock(30);
                product4.setSellerId(seller.getId());
                product4.setImageUrl("/images/products/hub.png");
                productRepository.save(product4);

                // Create test coupons
                Coupon coupon1 = new Coupon();
                coupon1.setCode("SAVE10");
                coupon1.setDiscountAmount(new BigDecimal("10.00"));
                coupon1.setActive(true);
                couponRepository.save(coupon1);

                Coupon coupon2 = new Coupon();
                coupon2.setCode("SAVE50");
                coupon2.setDiscountAmount(new BigDecimal("50.00"));
                coupon2.setActive(true);
                couponRepository.save(coupon2);

                log.info("Test data initialized successfully!");
                log.info("Buyer credentials: username=buyer, password=password");
                log.info("Seller credentials: username=seller, password=password");
                log.info("Coupon codes: SAVE10 ($10 off), SAVE50 ($50 off)");
            }
        };
    }
}
