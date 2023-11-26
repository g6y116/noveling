insert into member (member_name, member_login_id, member_login_pw, member_grade)
values ('장성준', 'qwer', 'qwerqwer', 'AUTHOR');



insert into novel (novel_title, novel_description, novel_cover, novel_genre, member_id)
values ('게임속 바바리안으로 살아남기', '소설 설명', '겜바바.jpg', 'FANTASY', 1);

insert into novel (novel_title, novel_description, novel_cover, novel_genre, member_id)
values ('별을 품은 소드마스터', '소설 설명', '별을품은소드마스터.jpg', 'FANTASY', 1);

insert into novel (novel_title, novel_description, novel_cover, novel_genre, member_id)
values ('블랙 배저', '소설 설명', '블랙배저.jpg', 'FANTASY', 1);

insert into novel (novel_title, novel_description, novel_cover, novel_genre, member_id)
values ('오늘만 사는 기사', '소설 설명', '오늘만사는기사.jpg', 'FANTASY', 1);

insert into novel (novel_title, novel_description, novel_cover, novel_genre, member_id)
values ('이세계 기사 식당', '소설 설명', '이세계기사식당.jpg', 'FANTASY', 1);



insert into chapter (chapter_title, chapter_content, novel_id)
values ('1장', '내용', 1);



insert into comment (comment_content, chapter_id, member_id)
values ('잘보고 있습니다~', 1, 1);