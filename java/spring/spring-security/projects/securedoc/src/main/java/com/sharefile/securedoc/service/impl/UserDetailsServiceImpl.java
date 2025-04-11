package com.sharefile.securedoc.service.impl;

/*
//Spring security default Authentication providers uses the UserDetailsService bean to get the user detail by username.
//But in our custom authentication provider we are using UserServiceImpl to get the user information so we do not need UserDetailsService
@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var dbUser = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var q1 = UserDetailsImpl.builder(dbUser);
        return q1;
    }
}*/