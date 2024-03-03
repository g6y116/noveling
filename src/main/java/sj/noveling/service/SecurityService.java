package sj.noveling.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sj.noveling.entity.Member;
import sj.noveling.exception.DataNotFoundException;
import sj.noveling.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SecurityService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> member = memberRepository.findByLoginId(username);

        if (member.isPresent()) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            return new User(member.get().getName(), member.get().getLoginPw(), authorities);
        } else {
            throw new DataNotFoundException("존재하지 않는 회원입니다.");
        }
    }
}
