package player.test.statics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    ADMIN("admin"),
    USER("user"),
    SUPERVISOR("supervisor");

    private final String role;
}
