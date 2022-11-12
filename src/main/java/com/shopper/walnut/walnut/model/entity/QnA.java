package com.shopper.walnut.walnut.model.entity;

import com.shopper.walnut.walnut.model.status.QnaStatus;
import com.shopper.walnut.walnut.model.type.QnaType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "qna")
@Entity
public class QnA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long qnaId;

    private String subject;
    private String content;
    private String answer;

    @ManyToOne()
    @JoinColumn(name="userId", nullable = false)
    public User user;

    @Enumerated(EnumType.STRING)
    private QnaType type;
    @Enumerated(EnumType.STRING)
    private QnaStatus status;

    public QnA(User user, String subject, String content) {
        this.user = user;
        this.subject = subject;
        this.content = content;
        this.status = QnaStatus.ING;
    }
}
