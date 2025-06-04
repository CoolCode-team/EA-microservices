package user.user.domain.dto; // Alterado para o pacote do microsserviço

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import user.user.domain.entity.UserRole;


@Getter
@Setter
public class CreateUserDto {
    @NotBlank
    private String name;

    @NotBlank
    private String password; // Considerar se a senha bruta deve trafegar assim ou se um hash já deveria ser esperado em algum ponto. Para criação, é comum receber a senha bruta.

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String role; // Mantido como String, com a conversão no método toDomainRole()

    private String contactNumber; // Opcional, pode permanecer

    private String course; // Opcional, pode permanecer

    private String schoolUnitId; // Mantido. A lógica de validação/uso será no UserService.

    public UserRole toDomainRole() {
        try {
            return UserRole.valueOf(role.toUpperCase()); // Adicionado toUpperCase() para robustez
        } catch (IllegalArgumentException e) {
            // Aqui você pode decidir como lidar com um valor de role inválido.
            // Lançar uma exceção personalizada, retornar um valor padrão, ou deixar a validação para o serviço.
            // Por simplicidade, pode-se deixar a IllegalArgumentException subir para ser tratada pelo ExceptionHandler.
            throw new IllegalArgumentException("Invalid role value: " + role);
        }
    }
}