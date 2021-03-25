package com.example.reddit.service;

import com.example.reddit.model.User;
import com.example.reddit.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;

import static java.util.Collections.singletonList;


// Authentication 的 getAuthorities() 可以返回当前 Authentication 对象拥有的权限，即当前用户拥有的权限。
// 其返回值是一个 GrantedAuthority 类型的数组，每一个 GrantedAuthority 对象代表赋予给当前用户的一种权限。
// GrantedAuthority 是一个接口，其通常是通过 UserDetailsService 进行加载，然后赋予给 UserDetails 的。
//
// GrantedAuthority 中只定义了一个 getAuthority() 方法，该方法返回一个字符串，
// 表示对应权限的字符串表示，如果对应权限不能用字符串表示，则应当返回 null。
//
// Spring Security 针对 GrantedAuthority 有一个简单实现 SimpleGrantedAuthority。
// 该类只是简单的接收一个表示权限的字符串。
// Spring Security 内部的所有 AuthenticationProvider 都是使用 SimpleGrantedAuthority 来封装 Authentication 对象。

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username not found"));
        return new org.springframework.security
                .core.userdetails.User(user.getUsername(), user.getPassword(),
                user.getEnabled(), true, true,
                true, getAuthorities("USER"));
    }

    // 固定格式
    //public Collection<? extends GrantedAuthority> getAuthorities() {
    //    Auto-generated method stub
    //    return null;


    /*
    private static void specific(List<Animal> param) { }
    private static void wildcard(List<? extends Animal> param) { }

        ********************************************************

        Without the extends syntax you can only use the exact class in the signature

            specific(new ArrayList<Horse>()); // <== compiler error
        With the wildcard extends you can allow any subclasses of Animal
            wildcard(new ArrayList<Horse>());  // <== OK
        It's generally better to use the ? extends syntax as it makes your code more reusable and future-proof.
     */
    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return singletonList(new SimpleGrantedAuthority(role));
    }
}
