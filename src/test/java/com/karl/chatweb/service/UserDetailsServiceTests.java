package com.karl.chatweb.service;

import static org.assertj.core.api.Assertions.*;

import com.karl.chatweb.model.User;
import com.karl.chatweb.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsService userDetailsService;

    @Test
    public void UserDetailsService_LoadByUsername_ReturnsUserDetails() throws UsernameNotFoundException {

        User user = User.builder()
                .color("#FF0000")
                .role("USER")
                .password("password")
                .username("username")
                .badges(new ArrayList<>())
                .build();

        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername("username");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("username");
        assertThat(userDetails.getPassword()).isEqualTo("password");
        assertThat(userDetails.isEnabled()).isTrue();
        assertThat(userDetails.isAccountNonExpired()).isTrue();
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
        assertThat(userDetails.isAccountNonLocked()).isTrue();
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(((SimpleGrantedAuthority) userDetails.getAuthorities().toArray()[0]).getAuthority()).isEqualTo("ROLE_USER");
    }

    @Test
    public void UserDetailsService_LoadByUsername_ThrowsUsernameNotFoundException() {

        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(null);

        assertThatThrownBy(() -> {
            UserDetails userDetails = userDetailsService.loadUserByUsername("username");
        }).isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("username");
    }
}
