package com.sparta.projcalc.security;

import com.sparta.projcalc.common.exception.ProjCalcException;
import com.sparta.projcalc.common.exception.ErrorCode;
import com.sparta.projcalc.domain.user.entity.User;
import com.sparta.projcalc.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Use the correct User type from your domain package
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ProjCalcException(ErrorCode.NOT_FOUND_USER));

        return new UserDetailsImpl(user);
    }

    public UserDetails loadUserById(Long id) {
        // Use the correct User type from your domain package
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ProjCalcException(ErrorCode.NOT_FOUND_USER));

        return new UserDetailsImpl(user);
    }
}
