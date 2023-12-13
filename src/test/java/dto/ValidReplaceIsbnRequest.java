package dto;

import lombok.*;

@Setter@Getter@AllArgsConstructor@NoArgsConstructor@Builder
public class ValidReplaceIsbnRequest {
    private String userId;
    private String isbn;
}
