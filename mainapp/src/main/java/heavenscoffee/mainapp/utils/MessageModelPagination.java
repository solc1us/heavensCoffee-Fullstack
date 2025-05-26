package heavenscoffee.mainapp.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageModelPagination {

    private String message;
    private Object data;
    private Integer currentPage;
    private Integer totalItems;
    private Integer totalPages;
    private Integer numberOfElement;
}
