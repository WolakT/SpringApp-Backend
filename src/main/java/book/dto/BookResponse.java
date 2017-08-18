package book.dto;

public class BookResponse {

    private String message;

    public BookResponse(String message) {
        this.message = message;
    }

    public BookResponse() {
    }

    public String getMessage() {
        return message;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BookResponse that = (BookResponse) o;

        return message != null ? message.equals(that.message) : that.message == null;
    }

    @Override public int hashCode() {
        return message != null ? message.hashCode() : 0;
    }
}
