package se.lexicon.todo_app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "attachments")

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "todo")
@EqualsAndHashCode(exclude = "todo")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Transient
    // Transient annotation is used to indicate that a field should not be persisted in the database.
    //private LocalDate date;

    private String fileName;
    private String fileType;
    @Lob
    @Column(length = 10485760)
    // 1 MB = 1024 * 1024 Bytes = 1048576 Bytes => 10 MB = 10 * 1048576 Bytes = 10485760 Bytes
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "todo_id")
    private Todo todo;

    public Attachment(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }

    public void setTodo(Todo todo) {
        this.todo = todo;

        // Sync the other side if not already present
        if (todo != null) {
            todo.getAttachments().add(this);
        }
    }

}
