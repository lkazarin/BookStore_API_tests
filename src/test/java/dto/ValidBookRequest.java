package dto;

import lombok.*;

@Setter@Getter@AllArgsConstructor@NoArgsConstructor@Builder
public class ValidBookRequest {
    private String isbn;
    private String userId;
}
