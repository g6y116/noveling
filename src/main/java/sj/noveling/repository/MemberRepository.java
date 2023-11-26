package sj.noveling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sj.noveling.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Boolean existsByLoginId(String loginId); // 아이디 중복 여부
    Boolean existsByName(String name); // 필명 중복 여부
    Optional<Member> findByLoginIdAndLoginPw(String loginId, String loginPw); // 회원 정보 조회
}
