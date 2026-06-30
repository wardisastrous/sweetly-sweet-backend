package com.sweetlysweet.backend.service;

import com.sweetlysweet.backend.entity.*;
import com.sweetlysweet.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CartService {

    private final CartRepository    cartRepository;
    private final UserRepository    userRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository,
                       UserRepository userRepository,
                       ProductRepository productRepository) {
        this.cartRepository    = cartRepository;
        this.userRepository    = userRepository;
        this.productRepository = productRepository;
    }

    public List<Cart> getCart(String email) {
        return cartRepository.findByUserId(getUser(email).getId());
    }

    public List<Cart> addToCart(String email, Long productId, int quantity) {
        User    user    = getUser(email);
        Product product = productRepository.findByIdAndActiveTrue(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cartRepository.findByUserIdAndProductId(user.getId(), productId)
                .ifPresentOrElse(
                        c -> { c.setQuantity(c.getQuantity() + quantity); cartRepository.save(c); },
                        () -> cartRepository.save(Cart.builder()
                                .user(user).product(product).quantity(quantity).build())
                );
        return cartRepository.findByUserId(user.getId());
    }

    public List<Cart> updateQuantity(String email, Long productId, int quantity) {
        User user = getUser(email);
        Cart cart = cartRepository.findByUserIdAndProductId(user.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cart.setQuantity(quantity);
        cartRepository.save(cart);
        return cartRepository.findByUserId(user.getId());
    }

    public List<Cart> removeItem(String email, Long productId) {
        User user = getUser(email);
        cartRepository.findByUserIdAndProductId(user.getId(), productId)
                .ifPresent(cartRepository::delete);
        return cartRepository.findByUserId(user.getId());
    }

    @Transactional
    public void clearCart(String email) {
        cartRepository.deleteByUserId(getUser(email).getId());
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}