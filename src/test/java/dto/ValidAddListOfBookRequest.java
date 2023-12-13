package dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidAddListOfBookRequest {
    private String userId;
    private List<BookItem> collectionOfIsbns;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BookItem {
        private String isbn;
    }
}