package listBooks;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class BookDto {
    private SimpleStringProperty title;
    private SimpleStringProperty code;
    private SimpleStringProperty author;
    private SimpleStringProperty publisher;
    private SimpleStringProperty description;
    private SimpleBooleanProperty status;

    public BookDto(String title, String code, String author, String publisher, String description, Boolean status) {
        this.title = new SimpleStringProperty(title);
        this.code = new SimpleStringProperty(code);
        this.author = new SimpleStringProperty(author);
        this.publisher = new SimpleStringProperty(publisher);
        this.description = new SimpleStringProperty(description);
        this.status = new SimpleBooleanProperty(status);
    }

    public String getTitle() {
        return title.get();
    }

    public String getCode() {
        return code.get();
    }

    public String getAuthor() {
        return author.get();
    }

    public String getPublisher() {
        return publisher.get();
    }

    public String getDescription() {
        return description.get();
    }

    public boolean isStatus() {
        return status.get();
    }
}
