package diploma.elders.up.auth;
import diploma.elders.up.dao.UserRole;
import diploma.elders.up.dao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Simonas on 5/10/2016.
 */
@Repository
public class AuthUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository users;
    private User userdetails;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        diploma.elders.up.dao.documents.User user = getUserDetail(username);

        userdetails = new User(user.getUsername(),
                user.getPassword(),
                enabled,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                getAuthorities(user.getRole())
        );

        return userdetails;
    }
    public List<GrantedAuthority> getAuthorities(UserRole role) {

        List<GrantedAuthority> authList = new ArrayList<>();
        if (Objects.equals(UserRole.ADMIN, role)) {
            authList.add(new SimpleGrantedAuthority(UserRole.ADMIN.name()));
        } else if (Objects.equals(UserRole.USER, role)) {
            authList.add(new SimpleGrantedAuthority(UserRole.USER.name()));
        }

        return authList;
    }

    private diploma.elders.up.dao.documents.User getUserDetail(String username){


        return users.findByUsername(username);
    }
}