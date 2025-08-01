package service.security;


import dao.UserDAO;
import details.CustomUserDetails;
import entity.UsersTable;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsersTable user = userDAO.findByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        System.out.println("Loaded user: " + user.getLogin() + ", password hash: " + user.getPasswordSalt());
        return new CustomUserDetails(user);
    }


}
