package server;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

    private String author;
    private Date date;
    private String text;

    public Message() {

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
