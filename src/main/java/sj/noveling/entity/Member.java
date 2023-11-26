package sj.noveling.entity;

import lombok.*;
import sj.noveling.type.Grade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@ToString(of = {"id", "name"})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_name", nullable = false, length = 16)
    private String name;

    @Column(name = "member_login_id", nullable = false, length = 16)
    private String loginId;

    @Column(name = "member_login_pw", nullable = false, length = 24)
    private String loginPw;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_grade")
    private Grade grade;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Novel> novels = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Member(String name, String loginId, String loginPw) {
        this.name = name;
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.grade = Grade.AUTHOR;
    }

    public Member(String name, String loginId, String loginPw, Grade grade) {
        this.name = name;
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.grade = grade;
    }
}
