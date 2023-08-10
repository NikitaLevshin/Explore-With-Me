package ru.yandex.practicum.comments.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.comments.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByEvent_IdOrderByCreatedDesc(int eventId, Pageable pageable);

    List<Comment> findAllByAuthor_IdOrderByCreatedDesc(int userId, Pageable pageable);
}
