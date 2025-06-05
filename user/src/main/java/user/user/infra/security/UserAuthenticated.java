package user.user.infra.security;

import java.util.Collection;
import java.util.Collections; // Para List.of() ou Collections.emptyList()
import java.util.List;

// Removido @Setter para imutabilidade
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import user.user.domain.entity.User;

@Getter // Lombok Getter para o campo 'user' se necessário externamente, mas não para UserDetails
public class UserAuthenticated implements UserDetails {

    private final User user; // Tornar final para imutabilidade

    public UserAuthenticated(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        this.user = user;
    }

    // Método getter explícito para a entidade User, se necessário em outros lugares (ex: no JwtSecurityFilter)
    public User getUserEntity() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.getRole() == null) {
            return Collections.emptyList();
        }
        return switch (user.getRole()) {
            case ADMIN -> List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_TEACHER"));
            case TEACHER -> List.of(new SimpleGrantedAuthority("ROLE_TEACHER"));
            default -> Collections.emptyList(); // Ou lançar exceção se um role desconhecido não for esperado
        };
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        // CORRIGIDO: Deve retornar o email, que é usado como username
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Ou basear em um campo de user.isAccountExpired() se existir
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Ou basear em um campo de user.isLocked() se existir
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Ou basear em um campo de user.isCredentialsExpired() se existir
    }

    @Override
    public boolean isEnabled() {
        return true; // Ou basear em um campo de user.isEnabled() se existir
    }
}
